const API_BASE = '';

let currentFlightId = null;
let currentSeatId = null;
let currentSeatNumber = null;
let currentCabin = 'BUSINESS';

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('departureDate').valueAsDate = new Date();
    searchFlights();
});

async function searchFlights(page = 0) {
    const origin = document.getElementById('origin').value.toUpperCase();
    const destination = document.getElementById('destination').value.toUpperCase();
    const departureDate = document.getElementById('departureDate').value;
    
    const params = new URLSearchParams();
    if (origin) params.append('origin', origin);
    if (destination) params.append('destinations', destination);
    if (departureDate) params.append('departureDate', departureDate);
    params.append('page', page);
    params.append('size', '10');
    
    showLoading('flightsList');
    
    try {
        const response = await fetch(`${API_BASE}/api/v1/flights?${params}`);
        const data = await response.json();
        
        renderFlights(data);
        renderPagination(data, 'pagination', searchFlights);
        
        document.getElementById('resultsSection').classList.remove('hidden');
        document.getElementById('resultCount').textContent = 
            `${data.totalElements} flight${data.totalElements !== 1 ? 's' : ''} found`;
    } catch (error) {
        showError('Failed to load flights');
    }
}

function renderFlights(data) {
    const container = document.getElementById('flightsList');
    
    if (!data.content || data.content.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M2 12h20M2 12l5-5m-5 5l5 5"/>
                </svg>
                <h3>No flights found</h3>
                <p>Try adjusting your search criteria</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = data.content.map(flight => `
        <div class="flight-card">
            <div class="flight-route">
                <div>
                    <div class="flight-code">${flight.origin}</div>
                    <div class="flight-time">${formatTime(flight.departureTime)}</div>
                </div>
                <div class="flight-arrow">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M5 12h14M12 5l7 7-7 7"/>
                    </svg>
                </div>
                <div>
                    <div class="flight-code">${flight.destination}</div>
                    <div class="flight-time">${formatTime(flight.arrivalTime)}</div>
                </div>
            </div>
            <div class="flight-info">
                <div class="flight-meta">
                    <span>${flight.flightNumber}</span>
                    <span>${flight.aircraftType}</span>
                    <span>${formatDuration(flight.departureTime, flight.arrivalTime)}</span>
                </div>
            </div>
            <div class="flight-action">
                <button class="btn btn-primary" onclick="openSeatModal('${flight.id}', '${flight.flightNumber}', '${flight.origin}', '${flight.destination}', '${flight.departureTime}')">
                    Select Seat
                </button>
            </div>
        </div>
    `).join('');
}

async function openSeatModal(flightId, flightNumber, origin, destination, departureTime) {
    currentFlightId = flightId;
    currentSeatId = null;
    currentSeatNumber = null;
    
    document.getElementById('flightSummary').innerHTML = `
        <div>
            <strong>${flightNumber}</strong> ${origin} → ${destination}
        </div>
        <div style="color: var(--text-light); font-size: 0.875rem;">
            ${formatDateTime(departureTime)}
        </div>
    `;
    
    document.getElementById('seatModal').classList.remove('hidden');
    document.getElementById('bookingForm').classList.add('hidden');
    
    document.querySelectorAll('.cabin-tabs .tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelector('.cabin-tabs .tab-btn:first-child').classList.add('active');
    currentCabin = 'BUSINESS';
    
    await loadSeatMap(flightId, currentCabin);
}

function closeSeatModal() {
    document.getElementById('seatModal').classList.add('hidden');
}

async function switchCabin(cabin) {
    currentCabin = cabin;
    document.querySelectorAll('.cabin-tabs .tab-btn').forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    await loadSeatMap(currentFlightId, cabin);
}

async function loadSeatMap(flightId, cabin) {
    const container = document.getElementById('seatMap');
    container.innerHTML = '<div class="loading"><div class="spinner"></div>Loading seats...</div>';
    
    try {
        const response = await fetch(`${API_BASE}/api/v1/flights/${flightId}/seats?cabin=${cabin}&size=200`);
        const data = await response.json();
        
        renderSeatMap(data.content);
    } catch (error) {
        container.innerHTML = '<p class="empty-state">Failed to load seats</p>';
    }
}

function renderSeatMap(seats) {
    const container = document.getElementById('seatMap');
    
    if (!seats || seats.length === 0) {
        container.innerHTML = '<p class="empty-state">No seats available</p>';
        return;
    }
    
    const rows = {};
    seats.forEach(seat => {
        const rowMatch = seat.seatNumber.match(/^(\d+)/);
        const row = rowMatch ? rowMatch[1] : '0';
        if (!rows[row]) rows[row] = [];
        rows[row].push(seat);
    });
    
    const sortedRows = Object.keys(rows).sort((a, b) => parseInt(a) - parseInt(b));
    
    container.innerHTML = sortedRows.map(rowNum => {
        const rowSeats = rows[rowNum];
        const seatOrder = ['A', 'B', 'C', 'D', 'E', 'F'];
        rowSeats.sort((a, b) => {
            const aLetter = a.seatNumber.slice(-1);
            const bLetter = b.seatNumber.slice(-1);
            return seatOrder.indexOf(aLetter) - seatOrder.indexOf(bLetter);
        });
        
        let seatHtml = '';
        let leftSide = true;
        
        rowSeats.forEach((seat, index) => {
            const letter = seat.seatNumber.slice(-1);
            const isBooked = seat.status === 'BOOKED';
            const isSelected = currentSeatId === seat.id;
            
            if (letter === 'D' && leftSide) {
                seatHtml += '<div class="aisle"></div>';
                leftSide = false;
            }
            
            seatHtml += `
                <div class="seat-box ${isSelected ? 'selected' : isBooked ? 'booked' : 'available'}" 
                     ${!isBooked ? `onclick="selectSeat('${seat.id}', '${seat.seatNumber}')"` : ''}>
                    ${letter}
                </div>
            `;
        });
        
        return `
            <div class="seat-row">
                <span class="row-number">${rowNum}</span>
                ${seatHtml}
            </div>
        `;
    }).join('');
}

function selectSeat(seatId, seatNumber) {
    currentSeatId = seatId;
    currentSeatNumber = seatNumber;
    
    document.querySelectorAll('.seat-box').forEach(box => box.classList.remove('selected'));
    event.target.classList.add('selected');
    
    document.getElementById('bookingForm').classList.remove('hidden');
    document.getElementById('selectedSeatNumber').textContent = seatNumber;
}

async function confirmBooking() {
    const passengerName = document.getElementById('passengerName').value.trim();
    const passengerEmail = document.getElementById('passengerEmail').value.trim();
    
    if (!passengerName || !passengerEmail) {
        showError('Please fill in all passenger details');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/api/v1/bookings`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                flightId: currentFlightId,
                seatNumber: currentSeatNumber,
                passengerName: passengerName,
                passengerEmail: passengerEmail
            })
        });
        
        if (!response.ok) {
            const error = await response.text();
            throw new Error(error);
        }
        
        const booking = await response.json();
        showBookingSuccess(booking);
        closeSeatModal();
    } catch (error) {
        showError('Booking failed: ' + error.message);
    }
}

function showBookingSuccess(booking) {
    document.getElementById('bookingDetails').innerHTML = `
        <div class="booking-details-row">
            <span>Reference</span>
            <strong>${booking.bookingReference}</strong>
        </div>
        <div class="booking-details-row">
            <span>Flight</span>
            <span>${booking.flightNumber}</span>
        </div>
        <div class="booking-details-row">
            <span>Seat</span>
            <span>${booking.seatNumber}</span>
        </div>
        <div class="booking-details-row">
            <span>Passenger</span>
            <span>${booking.passengerName}</span>
        </div>
    `;
    document.getElementById('successModal').classList.remove('hidden');
}

function closeSuccessModal() {
    document.getElementById('successModal').classList.add('hidden');
}

function renderPagination(data, containerId, callback) {
    const container = document.getElementById(containerId);
    if (data.totalPages <= 1) {
        container.innerHTML = '';
        return;
    }
    
    let html = '';
    
    html += `<button ${data.first ? 'disabled' : ''} onclick="${callback.name}(${data.number - 1})"></span></button>`;
    
    for (let i = 0; i < data.totalPages; i++) {
        html += `<button class="${i === data.number ? 'active' : ''}" onclick="${callback.name}(${i})">${i + 1}</button>`;
    }
    
    html += `<button ${data.last ? 'disabled' : ''} onclick="${callback.name}(${data.number + 1})">></button>`;
    
    container.innerHTML = html;
}

function showLoading(containerId) {
    document.getElementById(containerId).innerHTML = 
        '<div class="loading"><div class="spinner"></div>Loading...</div>';
}

function showError(message) {
    alert(message);
}

function formatTime(isoString) {
    return new Date(isoString).toLocaleTimeString('en-US', { 
        hour: '2-digit', 
        minute: '2-digit',
        hour12: false 
    });
}

function formatDateTime(isoString) {
    return new Date(isoString).toLocaleString('en-US', {
        weekday: 'short',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
    });
}

function formatDuration(departure, arrival) {
    const dep = new Date(departure);
    const arr = new Date(arrival);
    const diff = arr - dep;
    const hours = Math.floor(diff / 3600000);
    const mins = Math.round((diff % 3600000) / 60000);
    return `${hours}h ${mins}m`;
}
