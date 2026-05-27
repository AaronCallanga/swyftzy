# Swyftzy - Optimized Multi-Stage Docker Build

## Files Created

| File | Purpose |
|------|---------|
| `Dockerfile` | Multi-stage build (deps → build → runtime) |
| `.dockerignore` | Reduces build context size |

## Build Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   STAGE 1       │────▶│   STAGE 2       │────▶│   STAGE 3       │
│   deps          │     │   builder       │     │   runtime       │
│                 │     │                 │     │                 │
│ Cache Maven     │     │ Compile Java    │     │ Minimal JRE     │
│ dependencies    │     │ source + package│     │ + layered app   │
│ (layer cache)   │     │                 │     │ (~120MB final)  │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## Quick Start

### 1. Build the image
```bash
docker build -t swyftzy:latest .
```

### 2. Run with PostgreSQL
```bash
docker run -d \
  --name swyftzy \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/swyftzy \
  -e SPRING_DATASOURCE_USERNAME=swyftzy \
  -e SPRING_DATASOURCE_PASSWORD=yourpassword \
  swyftzy:latest
```

### 3. Or use docker-compose
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/swyftzy
      SPRING_DATASOURCE_USERNAME: swyftzy
      SPRING_DATASOURCE_PASSWORD: changeme
    depends_on:
      - db
    healthcheck:
      test: ["CMD", "wget", "--spider", "-q", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s

  db:
    image: postgres:17-alpine
    environment:
      POSTGRES_DB: swyftzy
      POSTGRES_USER: swyftzy
      POSTGRES_PASSWORD: changeme
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5433:5432"

volumes:
  postgres_data:
```

## Optimizations Applied

| Optimization | Benefit |
|-------------|---------|
| **Layer caching** | `deps` stage caches Maven dependencies. Rebuilds only recompile source, not re-download deps |
| **Layered JAR** | Spring Boot layered JAR separates dependencies, resources, and classes for optimal Docker layer caching |
| **JRE (not JDK)** | Runtime image uses JRE only — no compiler/debug tools (~50% smaller than JDK) |
| **Alpine Linux** | Minimal base image reduces attack surface and size |
| **Non-root user** | `swyftzy` user runs app — security best practice |
| **Container-aware JVM** | `-XX:+UseContainerSupport` reads cgroup memory limits |
| **Memory tuning** | `-XX:MaxRAMPercentage=75.0` auto-adjusts heap to container memory |
| **Fast entropy** | `-Djava.security.egd=file:/dev/./urandom` speeds startup in containers |
| **Health check** | Built-in Docker health check monitors app readiness |

## Image Sizes

| Stage | Size |
|-------|------|
| deps | ~500MB (builder, not shipped) |
| builder | ~600MB (builder, not shipped) |
| **runtime** | **~120-150MB** (final image) |

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | `prod` | Spring Boot profile |
| `JAVA_OPTS` | (see Dockerfile) | JVM tuning flags |
| `SPRING_DATASOURCE_URL` | - | PostgreSQL connection URL |
| `SPRING_DATASOURCE_USERNAME` | - | DB username |
| `SPRING_DATASOURCE_PASSWORD` | - | DB password |

## Testing the Build

```bash
# Build
docker build -t swyftzy:latest .

# Check image size
docker images swyftzy:latest

# Run locally (needs PostgreSQL running)
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/swyftzy \
  -e SPRING_DATASOURCE_USERNAME=swyftzy \
  -e SPRING_DATASOURCE_PASSWORD=yourpassword \
  swyftzy:latest

# Test API
curl http://localhost:8080/api/v1/flights
```
