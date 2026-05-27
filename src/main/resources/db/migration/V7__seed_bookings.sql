DO $$
DECLARE
    v_flight_id UUID;
    v_seat_id UUID;
    v_counter INT := 1;
    v_names TEXT[] := ARRAY[
        'John Smith', 'Emma Johnson', 'Michael Chen', 'Sarah Williams',
        'David Brown', 'Lisa Davis', 'Robert Wilson', 'Jennifer Miller',
        'James Taylor', 'Maria Garcia', 'William Martinez', 'Patricia Anderson',
        'Thomas Robinson', 'Linda Thomas', 'Charles Jackson', 'Elizabeth White',
        'Daniel Harris', 'Barbara Martin', 'Matthew Thompson', 'Susan Clark',
        'Christopher Lee', 'Amanda Walker', 'Kevin Hall', 'Nancy Allen',
        'Mark Young', 'Betty King', 'Paul Wright', 'Sandra Lopez',
        'Steven Hill', 'Donna Scott', 'Andrew Green', 'Caroline Adams',
        'Joshua Baker', 'Melissa Nelson', 'Ryan Carter', 'Stephanie Mitchell',
        'Gary Perez', 'Rebecca Roberts', 'Jacob Turner', 'Laura Phillips',
        'Nicholas Campbell', 'Amy Parker', 'Eric Evans', 'Kathleen Edwards',
        'Jonathan Collins', 'Angela Stewart', 'Aaron Sanchez', 'Ruth Morris',
        'Adam Rogers', 'Janice Reed', 'Benjamin Cook', 'Dorothy Morgan',
        'Jason Bell', 'Evelyn Murphy', 'Jeffrey Bailey', 'Cheryl Rivera',
        'Frank Cooper', 'Marilyn Richardson', 'Scott Cox', 'Andrea Ward',
        'Larry Torres', 'Brenda Peterson', 'Justin Gray', 'Catherine Ramirez',
        'Raymond James', 'Sharon Watson', 'Brandon Brooks', 'Kathryn Kelly',
        'Gregory Sanders', 'Gloria Price', 'Samuel Bennett', 'Joan Wood',
        'Patrick Barnes', 'Diana Ross', 'Alexander Henderson', 'Ann Coleman',
        'Jack Jenkins', 'Joyce Perry', 'Dennis Powell', 'Alice Long',
        'Jerry Patterson', 'Julie Hughes', 'Henry Flores', 'Terry Washington'
    ];
    v_emails TEXT[] := ARRAY[
        'john.smith@email.com', 'emma.j@email.com', 'mchen@email.com', 's.williams@email.com',
        'dbrown@email.com', 'lisa.davis@email.com', 'rwilson@email.com', 'jen.m@email.com',
        'james.t@email.com', 'maria.g@email.com', 'will.m@email.com', 'patty.a@email.com',
        'tom.robinson@email.com', 'linda.t@email.com', 'charles.j@email.com', 'liz.white@email.com',
        'dan.harris@email.com', 'barb.m@email.com', 'matt.t@email.com', 'susan.c@email.com',
        'chris.lee@email.com', 'amanda.w@email.com', 'kevin.h@email.com', 'nancy.a@email.com',
        'mark.y@email.com', 'betty.k@email.com', 'paul.w@email.com', 'sandra.l@email.com',
        'steve.h@email.com', 'donna.s@email.com', 'andrew.g@email.com', 'caroline.a@email.com',
        'josh.b@email.com', 'melissa.n@email.com', 'ryan.c@email.com', 'steph.m@email.com',
        'gary.p@email.com', 'becca.r@email.com', 'jacob.t@email.com', 'laura.p@email.com',
        'nick.c@email.com', 'amy.p@email.com', 'eric.e@email.com', 'kathy.e@email.com',
        'jon.c@email.com', 'angie.s@email.com', 'aaron.s@email.com', 'ruth.m@email.com',
        'adam.r@email.com', 'janice.r@email.com', 'ben.c@email.com', 'dot.m@email.com',
        'jason.b@email.com', 'eve.m@email.com', 'jeff.b@email.com', 'cheryl.r@email.com',
        'frank.c@email.com', 'marilyn.r@email.com', 'scott.c@email.com', 'andrea.w@email.com',
        'larry.t@email.com', 'brenda.p@email.com', 'justin.g@email.com', 'cathy.r@email.com',
        'ray.j@email.com', 'sharon.w@email.com', 'brandon.b@email.com', 'kat.k@email.com',
        'greg.s@email.com', 'gloria.p@email.com', 'sam.b@email.com', 'joan.w@email.com',
        'pat.b@email.com', 'diana.r@email.com', 'alex.h@email.com', 'ann.c@email.com',
        'jack.j@email.com', 'joyce.p@email.com', 'denny.p@email.com', 'alice.l@email.com',
        'jerry.p@email.com', 'julie.h@email.com', 'henry.f@email.com', 'terry.w@email.com'
    ];
    v_name TEXT;
    v_email TEXT;
    v_ref TEXT;
BEGIN

-- =========================================================
-- BR123: TPE -> HKG (Boeing 777-300ER)
-- 6 Business, 15 Economy
-- =========================================================
SELECT id INTO v_flight_id FROM flights WHERE flight_number = 'BR123';

-- Business
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '3A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-20 10:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '3F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-21 14:15:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '4B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 09:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '5C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-18 16:20:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '6D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 11:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '8F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 08:30:00+00');

-- Economy
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '12A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-20 13:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '12E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-21 10:20:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '14B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 15:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '15C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 09:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '16D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-19 11:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '17F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 14:20:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '18A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 08:15:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '20B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-21 16:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '22C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 12:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '25D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-20 18:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '28E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-18 20:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '30F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 07:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '35A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 11:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '40B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-21 09:45:00+00');


-- =========================================================
-- BR456: TPE -> BKK (Airbus A350-900)
-- 4 Business, 12 Economy
-- =========================================================
SELECT id INTO v_flight_id FROM flights WHERE flight_number = 'BR456';

-- Business
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '1A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 10:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '2F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 14:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '3C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-20 16:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '4E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 08:45:00+00');

-- Economy
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '10A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 12:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '11B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 09:15:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '12C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-21 15:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '14D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 10:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '15E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-19 13:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '16F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 11:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '20A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 08:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '25B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-21 14:15:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '30C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 09:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '35D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 07:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '40E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 16:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '42F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-21 10:30:00+00');


-- =========================================================
-- CX101: HKG -> TPE (Airbus A321neo)
-- 3 Business, 8 Economy
-- =========================================================
SELECT id INTO v_flight_id FROM flights WHERE flight_number = 'CX101';

-- Business
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '1A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 09:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '2F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 13:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '4B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-21 15:00:00+00');

-- Economy
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '10A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 11:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '12B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 08:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '14C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 14:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '16D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-21 10:15:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '20E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-20 12:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '25F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 07:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '30A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 15:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '35B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 09:15:00+00');


-- =========================================================
-- PR201: MNL -> HKG (Airbus A321neo)
-- 2 Business, 6 Economy
-- =========================================================
SELECT id INTO v_flight_id FROM flights WHERE flight_number = 'PR201';

-- Business
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '2A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 08:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '3F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 12:00:00+00');

-- Economy
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '10A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 10:45:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '12B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 14:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '15C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-21 09:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '18D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 11:15:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '20E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 07:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '25F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 13:00:00+00');


-- =========================================================
-- PR301: MNL -> TPE (Airbus A330-300)
-- 3 Business, 10 Economy
-- =========================================================
SELECT id INTO v_flight_id FROM flights WHERE flight_number = 'PR301';

-- Business
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '2A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 10:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '3C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 14:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '5F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-22 08:00:00+00');

-- Economy
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '10A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 12:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '11B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 09:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '12C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 15:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '14D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 11:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '16E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-21 10:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '18F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 08:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '20A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 13:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '22B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 09:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '25C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 14:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '28D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 10:30:00+00');


-- =========================================================
-- TG201: BKK -> TPE (Boeing 777-200ER)
-- 5 Business, 12 Economy
-- =========================================================
SELECT id INTO v_flight_id FROM flights WHERE flight_number = 'TG201';

-- Business
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '1A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 11:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '2F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 15:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '3B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-22 09:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '4E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 13:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '6C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 08:30:00+00');

-- Economy
SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '10A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 10:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '12B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 14:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '14C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 11:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '16D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 09:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '18E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CANCELLED', '2026-05-21 15:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '20F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 12:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '22A';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 08:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '25B';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 14:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '28C';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 10:30:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '30D';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-23 07:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '35E';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-22 13:00:00+00');

SELECT id INTO v_seat_id FROM seats WHERE flight_id = v_flight_id AND seat_number = '38F';
v_name := v_names[((v_counter - 1) % array_length(v_names, 1)) + 1];
v_email := v_emails[((v_counter - 1) % array_length(v_emails, 1)) + 1];
v_counter := v_counter + 1;
INSERT INTO bookings (booking_reference, flight_id, seat_id, passenger_name, passenger_email, status, booked_at)
VALUES ('SWZ-' || upper(substring(md5(random()::text) from 1 for 6)), v_flight_id, v_seat_id, v_name, v_email, 'CONFIRMED', '2026-05-24 09:00:00+00');

END $$;
