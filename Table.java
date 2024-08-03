import java.time.LocalDateTime;
import java.time.Duration;

 class Table {
    private int tableId;
    private int capacity;
    private boolean isVIP;
    private double downPayment;
    private Reservation[] reservations;
    private static final int MAX_RESERVATIONS_PER_DAY = 6;
    private static final int MAX_HOURS_PER_RESERVATION = 2;

    public Table(int tableId, int capacity, boolean isVIP, double downPayment) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.isVIP = isVIP;
        this.downPayment = downPayment;
        this.reservations = new Reservation[MAX_RESERVATIONS_PER_DAY];
    }

    // Existing getters
    public int getTableId() { return tableId; }
    public int getCapacity() { return capacity; }
    public boolean isVIP() { return isVIP; }
    public double getDownPayment() { return downPayment; }

    @Override
    public String toString() {
        return "Table " + tableId + " (Capacity: " + capacity + ", VIP: " + isVIP + ", Price: $" + downPayment + ")";
    }

    public boolean isAvailable(LocalDateTime dateTime) {
        for (Reservation reservation : reservations) {
            if (reservation != null && !reservation.isCancelled()) {
                if (isTimeOverlap(reservation.getDateTime(), dateTime)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isTimeOverlap(LocalDateTime existingTime, LocalDateTime newTime) {
        Duration duration = Duration.between(existingTime, newTime).abs();
        return duration.toHours() < MAX_HOURS_PER_RESERVATION;
    }

    public boolean reserveTable(Reservation reservation) {
        if (isAvailable(reservation.getDateTime())) {
            for (int i = 0; i < reservations.length; i++) {
                if (reservations[i] == null || reservations[i].isCancelled()) {
                    reservations[i] = reservation;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cancelReservation(String customerName, LocalDateTime dateTime) {
        for (int i = 0; i < reservations.length; i++) {
            if (reservations[i] != null &&
                reservations[i].getCustomerName().equals(customerName) &&
                reservations[i].getDateTime().equals(dateTime)) {
                reservations[i].cancel();
                // reservations[i].setCancelled(reservations[i].getCancelled());
                return true;
            }
        }
        return false;
    }

    public int getAvailableReservationSlots() {
        int availableSlots = 0;
        for (Reservation reservation : reservations) {
            if (reservation == null || reservation.isCancelled()) {
                availableSlots++;
            }
        }
        return availableSlots;
    }

    public Reservation[] getReservations() {
        return reservations;
    }
}
