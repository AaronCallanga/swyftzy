const API_BASE = '';

let currentPage = 0;
let currentFlightFilter = '';
let currentStatusFilter = '';
let cancelBookingRef = '';

document.addEventListener('DOMContentLoaded', () => {
    loadBookings();
    loadFlightOptions();
});

async function loadBookings(page = 0) {
    currentPage = page;
    const container = document.getElementById('bookingsList');
    container.innerHTML = '<div class="loading"><div class="spinner"></div>Loading bookings...</div>';
    
    try {
        const params = new URLSearchParams();
        params.append('page', page);
        params.append('size', '10');
        params.append('sort', 'bookedAt,desc');
        
        let url;
        if (currentFlightFilter) {
            url = `${API_BASE}/api/v1/bookings/flight/${currentFlightFilter}?${params}`;
        } else {
            url = `${API_BASE}/api/v1/bookings?${params}`;
        }
        
        const response = await fetch(url);
        const data = await response.json();
        
        let bookings = data.content || [];
        if (currentStatusFilter) {
            bookings = bookings.filter(b => b.status === currentStatusFilter);
        }
        
        renderBookings(bookings, data.totalElements);
        renderPagination(data, 'bookingsPagination', loadBookings);
    } catch (error) {
        container.innerHTML = '<p class="empty-state">Failed to load bookings</p>';
    }
}

function renderBookings(bookings, totalCount) {
    const container = document.getElementById('bookingsList');
    document.getElementById('bookingCount').textContent = 
        `${totalCount} booking${totalCount !== 1 ? 's' : ''}`;
    
    if (!bookings || bookings.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="3" y="3" width="18" height="18" rx="2"/>
                    <path d="M9 12l2 2 4-4"/>
                </svg>
                <h3>No bookings found</h3>
                <p>Book a flight to see your reservations here</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = bookings.map(booking => `
        <div class="booking-card">
            <div class="booking-info">
                <div class="booking-ref">${booking.bookingReference}</div>
                <div class="booking-route">${booking.flightNumber} — ${booking.seatNumber}</div>
                <div class="booking-meta">
                    <span>${booking.passengerName}</span>
                    <span>${formatDateTime(booking.bookedAt)}</span>
                </div>
            </div>
            <div class="booking-actions">
                <span class="status-badge ${booking.status.toLowerCase()}">${booking.status}</span>
                ${booking.status === 'CONFIRMED' ? `
                    <button class="btn btn-danger btn-sm" onclick="showCancelModal('${booking.bookingReference}')">
                        Cancel
                    </button>
                ` : ''}
            </div>
        </div>
    `).join('');
}

async function loadFlightOptions() {
    try {
        const response = await fetch(`${API_BASE}/api/v1/flights?size=100`);
        const data = await response.json();
        
        const select = document.getElementById('flightFilter');
        data.content.forEach(flight => {
            const option = document.createElement('option');
            option.value = flight.id;
            option.textContent = `${flight.flightNumber} (${flight.origin} → ${flight.destination})`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Failed to load flights');
    }
}

function filterByFlight() {
    currentFlightFilter = document.getElementById('flightFilter').value;
    loadBookings(0);
}

function filterByStatus() {
    currentStatusFilter = document.getElementById('statusFilter').value;
    loadBookings(0);
}

async function searchByReference() {
    const ref = document.getElementById('searchRef').value.trim();
    if (!ref) {
        loadBookings(0);
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/api/v1/bookings/reference/${ref}`);
        if (!response.ok) {
            document.getElementById('bookingsList').innerHTML = `
                <div class="empty-state">
                    <h3>Booking not found</h3>
                    <p>No booking found with reference: ${ref}</p>
                </div>
            `;
            document.getElementById('bookingCount').textContent = '0 bookings';
            document.getElementById('bookingsPagination').innerHTML = '';
            return;
        }
        
        const booking = await response.json();
        renderBookings([booking], 1);
        document.getElementById('bookingsPagination').innerHTML = '';
    } catch (error) {
        showToast('Search failed', 'error');
    }
}

function showCancelModal(bookingReference) {
    cancelBookingRef = bookingReference;
    document.getElementById('cancelRef').textContent = bookingReference;
    document.getElementById('cancelModal').classList.remove('hidden');
}

function closeCancelModal() {
    document.getElementById('cancelModal').classList.add('hidden');
    cancelBookingRef = '';
}

async function confirmCancel() {
    try {
        const response = await fetch(`${API_BASE}/api/v1/bookings/${cancelBookingRef}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            throw new Error('Cancel failed');
        }
        
        const result = await response.json();
        showToast(`Booking ${cancelBookingRef} cancelled`, 'success');
        closeCancelModal();
        loadBookings(currentPage);
    } catch (error) {
        showToast('Failed to cancel booking', 'error');
    }
}

function renderPagination(data, containerId, callback) {
    const container = document.getElementById(containerId);
    if (data.totalPages <= 1) {
        container.innerHTML = '';
        return;
    }

    let html = '';

    html += `<button ${data.first ? 'disabled' : ''} onclick="${callback.name}(${data.number - 1})">&lt;</button>`;

    for (let i = 0; i < data.totalPages; i++) {
        html += `<button class="${i === data.number ? 'active' : ''}" onclick="${callback.name}(${i})">${i + 1}</button>`;
    }

    html += `<button ${data.last ? 'disabled' : ''} onclick="${callback.name}(${data.number + 1})">&gt;</button>`;

    container.innerHTML = html;
}

function showToast(message, type) {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type}`;
    toast.classList.remove('hidden');
    
    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3000);
}

function formatDateTime(isoString) {
    return new Date(isoString).toLocaleString('en-US', {
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
    });
}
