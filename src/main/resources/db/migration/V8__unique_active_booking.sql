-- Prevent double-booking at the database level by enforcing a UNIQUE constraint on (flight_id, seat_id) for active (CONFIRMED) bookings only.
-- This acts as a safety net on top of the application-level pessimistic locking.
-- CANCELLED bookings are excluded, allowing the same seat to be rebooked after cancellation.
CREATE UNIQUE INDEX CONCURRENTLY IF NOT EXISTS idx_unique_active_booking
ON bookings(flight_id, seat_id)
WHERE status = 'CONFIRMED';
