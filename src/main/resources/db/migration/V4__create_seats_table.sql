CREATE TABLE seats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flight_id UUID NOT NULL REFERENCES flights(id) ON DELETE CASCADE,
    seat_number VARCHAR(4) NOT NULL,
    cabin VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    location VARCHAR(20) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_flight_seat UNIQUE (flight_id, seat_number)
);

CREATE INDEX idx_seats_flight ON seats(flight_id);
CREATE INDEX idx_seats_flight_cabin ON seats(flight_id, cabin);
CREATE INDEX idx_seats_flight_status ON seats(flight_id, status);
