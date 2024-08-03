import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 class Restaurant {
    private List<Table> tables;
    private List<Reservation> reservations;
    private CurrentReservations currentReservations;
    private PastReservations pastReservations;

    public Restaurant() {
        tables = new ArrayList<>();
        currentReservations = new CurrentReservations();
        pastReservations = new PastReservations();
        initializeTables();
    }

    private void initializeTables() {
        tables.add(new Table(1, 2, false, 50.0));
        tables.add(new Table(2, 4, true, 80.0));
        tables.add(new Table(3, 6, false, 120.0));
        tables.add(new Table(4, 8, true, 150.0));
        tables.add(new Table(5, 1, false, 30.0));
        tables.add(new Table(6, 8, true, 150.0));
        tables.add(new Table(7, 3, true, 70.0));
        tables.add(new Table(8, 8, false, 150.0));
        tables.add(new Table(9, 5, true, 110.0));
        tables.add(new Table(10, 2, true, 50.0));
        tables.add(new Table(11, 7, false, 130.0));
        tables.add(new Table(12, 8, true, 150.0));
        tables.add(new Table(13, 7, false, 130.0));
        tables.add(new Table(14, 3, true, 70.0));
        tables.add(new Table(15, 4, false, 80.0));
    }

    public String viewMonthWithHighBookings() {
    Map<String, Integer> monthCount = new HashMap<>();

    // Count reservations from current reservations
    for (Reservation reservation : currentReservations.getAllReservations()) {
        String month = reservation.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        monthCount.put(month, monthCount.getOrDefault(month, 0) + 1);
    }

    // Count reservations from past reservations
    for (Reservation reservation : pastReservations.getAllReservations()) {
        String month = reservation.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        monthCount.put(month, monthCount.getOrDefault(month, 0) + 1);
    }

    if (monthCount.isEmpty()) {
        return "No bookings found";
    }

    String maxMonth = monthCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);

    return maxMonth != null ? "Month with highest bookings: " + maxMonth + " with " + monthCount.get(maxMonth) + " bookings" : "No bookings found";
}

    public void showMostBookedTable(LocalDateTime startDate, LocalDateTime endDate) {
        Table mostBooked = currentReservations.getMostBookedTable(startDate, endDate);
        if (mostBooked != null) {
            System.out.println("Most booked table: " + mostBooked);
        } else {
            System.out.println("No bookings in the given period.");
        }
    }

    public boolean makeReservation(String customerName, LocalDateTime dateTime, String time, int numPeople) {
        Table availableTable = findAvailableTable(dateTime, numPeople);
        if (availableTable != null) {
            Reservation newReservation = new Reservation(customerName, dateTime, numPeople, availableTable);
            if (dateTime.isBefore(LocalDateTime.now())) {
                pastReservations.enqueueReservation(newReservation);
            } else {
                currentReservations.addReservation(newReservation, availableTable);
            }
            return true;
        }
        return false;
    }
        
    public Reservation makeReservation(String customerName, LocalDateTime dateTime, int numPeople) {
        Table availableTable = findAvailableTable(dateTime, numPeople);
        if (availableTable != null) {
            Reservation newReservation = new Reservation(customerName, dateTime, numPeople, availableTable);
            reservations.add(newReservation);
            return newReservation;
        }
        return null;
    }


    public Table findAvailableTable(LocalDateTime dateTime, int numPeople) {
        for (Table table : tables) {
            if (table.getCapacity() >= numPeople && table.isAvailable(dateTime)) {
                return table;
            }
        }
        return null;
    }

    public void moveToPastReservations(Reservation reservation) {
        currentReservations.removeReservation(reservation.getCustomerName(), reservation.getDateTime());
        pastReservations.enqueueReservation(reservation);
    }

    public void showPredictedBookings(String targetMonth) {
        System.out.println(pastReservations.predictFutureBookings(targetMonth));
    }

    public void showMonthlySummary(String month) {
        System.out.println(pastReservations.generateMonthlySummary(month));
    }

    public void showAverageGroupSize() {
        System.out.println("Average group size: " + currentReservations.getAverageGroupSize());
    }


    public boolean cancelReservation(String customerName, LocalDateTime dateTime) {
        for (Table table : tables) {
            if (table.cancelReservation(customerName, dateTime)) {
                currentReservations.removeReservation(customerName, dateTime);
                
                return true;
            }
        }
        return false;
    }

    public void showAvailableTables(LocalDateTime dateTime) {
        for (Table table : tables) {
            if (table.isAvailable(dateTime)) {
                System.out.println(table);
            }
        }
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> allReservations = new ArrayList<>();
        for (Table table : tables) {
            for (Reservation reservation : table.getReservations()) {
                if (reservation != null && !reservation.isCancelled()) {
                    allReservations.add(reservation);
                }
            }
        }
        return allReservations;
    }

    public void showAllReservations() {
        System.out.println("Current Reservations:");
        List<Reservation> currentReservationsList = currentReservations.getAllReservations();
        if (currentReservationsList.isEmpty()) {
            System.out.println("No current reservations found.");
        } else {
            for (Reservation reservation : currentReservationsList) {
                System.out.println(reservation);
            }
        }
    
        System.out.println("\nPast Reservations:");
        List<Reservation> pastReservationsList = pastReservations.getAllReservations();
        if (pastReservationsList.isEmpty()) {
            System.out.println("No past reservations found.");
        } else {
            for (Reservation reservation : pastReservationsList) {
                System.out.println(reservation);
            }
        }
    }
}
