-- Seed Business and Economy seat layouts for all flights (including PH routes)

-- =========================================================
-- BR123
-- =========================================================
DO $$
DECLARE
    v_flight_id UUID;
    seat_letter VARCHAR(1);
BEGIN
SELECT id INTO v_flight_id
FROM flights
WHERE flight_number = 'BR123';

-- BUSINESS (rows 3–8)
FOR row_num IN 3..8 LOOP
        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'BUSINESS',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;

    -- ECONOMY (rows 12–42, skip 13)
FOR row_num IN 12..42 LOOP
        CONTINUE WHEN row_num = 13;

        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'ECONOMY',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;
END $$;


-- =========================================================
-- BR456
-- =========================================================
DO $$
DECLARE
    v_flight_id UUID;
    seat_letter VARCHAR(1);
BEGIN
SELECT id INTO v_flight_id
FROM flights
WHERE flight_number = 'BR456';

-- BUSINESS (1–4)
FOR row_num IN 1..4 LOOP
        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'BUSINESS',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;

    -- ECONOMY (10–43, skip 13)
FOR row_num IN 10..43 LOOP
        CONTINUE WHEN row_num = 13;

        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'ECONOMY',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;
END $$;


-- =========================================================
-- BR789
-- =========================================================
DO $$
DECLARE
    v_flight_id UUID;
    seat_letter VARCHAR(1);
BEGIN
SELECT id INTO v_flight_id
FROM flights
WHERE flight_number = 'BR789';

-- BUSINESS (3–8)
FOR row_num IN 3..8 LOOP
        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'BUSINESS',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;

    -- ECONOMY (12–40, skip 13)
FOR row_num IN 12..40 LOOP
        CONTINUE WHEN row_num = 13;

        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'ECONOMY',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;
END $$;


-- =========================================================
-- CX101
-- =========================================================
DO $$
DECLARE
    v_flight_id UUID;
    seat_letter VARCHAR(1);
BEGIN
SELECT id INTO v_flight_id
FROM flights
WHERE flight_number = 'CX101';

-- BUSINESS (1–4)
FOR row_num IN 1..4 LOOP
        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'BUSINESS',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;

    -- ECONOMY (10–35, skip 13)
FOR row_num IN 10..35 LOOP
        CONTINUE WHEN row_num = 13;

        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'ECONOMY',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;
END $$;


-- =========================================================
-- TG201
-- =========================================================
DO $$
DECLARE
    v_flight_id UUID;
    seat_letter VARCHAR(1);
BEGIN
SELECT id INTO v_flight_id
FROM flights
WHERE flight_number = 'TG201';

-- BUSINESS (1–6)
FOR row_num IN 1..6 LOOP
        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'BUSINESS',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;

    -- ECONOMY (10–40, skip 13)
FOR row_num IN 10..40 LOOP
        CONTINUE WHEN row_num = 13;

        FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
            INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                v_flight_id,
                row_num || seat_letter,
                'ECONOMY',
                'AVAILABLE',
                CASE
                    WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                    WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                    ELSE 'AISLE'
                END
            );
END LOOP;
END LOOP;
END $$;


-- =========================================================
-- PHILIPPINE AIRLINES (PR SERIES)
-- =========================================================
DO $$
DECLARE
    v_flight_id UUID;
    flight TEXT;
    seat_letter VARCHAR(1);
BEGIN
FOR flight IN
SELECT flight_number
FROM flights
WHERE flight_number LIKE 'PR%'
    LOOP

SELECT id INTO v_flight_id
FROM flights
WHERE flight_number = flight;

-- BUSINESS (2–5)
FOR row_num IN 2..5 LOOP
            FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
                INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                    v_flight_id,
                    row_num || seat_letter,
                    'BUSINESS',
                    'AVAILABLE',
                    CASE
                        WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                        WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                        ELSE 'AISLE'
                    END
                );
END LOOP;
END LOOP;

        -- ECONOMY (10–30, skip 13)
FOR row_num IN 10..30 LOOP
            CONTINUE WHEN row_num = 13;

            FOREACH seat_letter IN ARRAY ARRAY['A','B','C','D','E','F'] LOOP
                INSERT INTO seats (flight_id, seat_number, cabin, status, location) VALUES (
                    v_flight_id,
                    row_num || seat_letter,
                    'ECONOMY',
                    'AVAILABLE',
                    CASE
                        WHEN seat_letter IN ('A','F') THEN 'WINDOW'
                        WHEN seat_letter IN ('B','E') THEN 'MIDDLE'
                        ELSE 'AISLE'
                    END
                );
END LOOP;
END LOOP;

END LOOP;
END $$;