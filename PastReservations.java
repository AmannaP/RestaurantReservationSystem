import java.time.format.DateTimeFormatter;
import java.util.*;

 class PastReservations {
    private Queue<Reservation> queue;

    public PastReservations() {
        this.queue = new LinkedList<>();
    }

    public void enqueueReservation(Reservation reservation) {
        queue.offer(reservation);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(queue);
    }
    
    public void viewPastReservations() {
        for (Reservation reservation : queue) {
            System.out.println(reservation.toString());
        }
    }

    public String viewMonthWithHighBookings() {
        Map<String, Integer> monthCount = new HashMap<>();

        for (Reservation reservation : queue) {
            String month = reservation.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthCount.put(month, monthCount.getOrDefault(month, 0) + 1);
        }

        String maxMonth = monthCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return maxMonth != null ? "Month with highest bookings: " + maxMonth + " with " + monthCount.get(maxMonth) + " bookings" : "No bookings found";
    }

    public String predictFutureBookings(String targetMonth) {
        Map<String, Integer> monthCount = new HashMap<>();
        for (Reservation reservation : queue) {
            String month = reservation.getDateTime().format(DateTimeFormatter.ofPattern("MM"));
            monthCount.put(month, monthCount.getOrDefault(month, 0) + 1);
        }

        // Simple moving average prediction
        int totalBookings = monthCount.values().stream().mapToInt(Integer::intValue).sum();
        int averageBookings = totalBookings / monthCount.size();
        
        // Adjust prediction based on the trend of the last 3 months
        List<String> lastThreeMonths = getLastThreeMonths(targetMonth);
        int trendAdjustment = lastThreeMonths.stream()
                .mapToInt(month -> monthCount.getOrDefault(month, averageBookings) - averageBookings)
                .sum() / 3;

        int prediction = averageBookings + trendAdjustment;
        return "Predicted bookings for " + targetMonth + ": " + prediction;
    }

    private List<String> getLastThreeMonths(String targetMonth) {
        List<String> months = Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        int targetIndex = months.indexOf(targetMonth);
        List<String> lastThree = new ArrayList<>();
        for (int i = 3; i > 0; i--) {
            lastThree.add(months.get((targetIndex - i + 12) % 12));
        }
        return lastThree;
    }

    public String generateMonthlySummary(String month) {
        Map<Integer, Integer> tableBookings = new HashMap<>();
        int totalGuests = 0;
        int totalBookings = 0;

        for (Reservation reservation : queue) {
            if (reservation.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM")).equals(month)) {
                tableBookings.put(reservation.getTable().getTableId(), tableBookings.getOrDefault(reservation.getTable().getTableId(), 0) + 1);
                totalGuests += reservation.getNumberOfPeople();
                totalBookings++;
            }
        }

        if (totalBookings == 0) {
            return "No bookings found for the month: " + month;
        }

        StringBuilder summary = new StringBuilder();
        summary.append("Monthly Summary for ").append(month).append(":\n");
        summary.append("Total Bookings: ").append(totalBookings).append("\n");
        summary.append("Total Guests: ").append(totalGuests).append("\n");
        summary.append("Average Group Size: ").append(String.format("%.2f", (double) totalGuests / totalBookings)).append("\n");
        summary.append("Bookings per Table:\n");
        
        tableBookings.forEach((tableId, bookings) -> 
            summary.append("  Table ").append(tableId).append(": ").append(bookings).append(" bookings\n"));

        return summary.toString();}
    

       
}