import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class RestaurantReservationManager {
    private Restaurant restaurant;
    private Scanner scanner;
    private List<Reservation> activeReservations;
    private List<Reservation> pastReservations;
    private CurrentReservations currentReservations;
    

    public static void main(String[] args) {
        RestaurantReservationManager manager = new RestaurantReservationManager();
        manager.start();
    }

    public RestaurantReservationManager() {
        restaurant = new Restaurant();
        scanner = new Scanner(System.in);
        activeReservations = new ArrayList<>();
        pastReservations = new ArrayList<>();
        currentReservations = new CurrentReservations();
    }

    public void start() {
        while (true) {
            System.out.println("Welcome to the Restaurant Reservation System");
            System.out.println("1. Customer Interface");
            System.out.println("2. Manager Interface");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    customerInterface();
                    break;
                case 2:
                    managerInterface();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void customerInterface() {
        while (true) {
            System.out.println("\nCustomer Interface");
            System.out.println("1. Make a reservation");
            System.out.println("2. Cancel a reservation");
            System.out.println("3. View available tables");
            System.out.println("4. View User Reservation");
            System.out.println("5. Return to main menu");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    makeReservation();
                    break;
                case 2:
                    cancelReservation();
                    break;
                case 3:
                    viewAvailableTables();
                    break;
                case 4:
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    getReservationsByCustomer(customerName);
                    break; 
                case 5:
                    return; // Return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void managerInterface() {
        while (true) {
            System.out.println("\nManager Interface");
            System.out.println("1. View all reservations");
            System.out.println("2. View month with high bookings");
            System.out.println("3. Generate monthly summary");
            System.out.println("4. Predict future bookings");
            System.out.println("5. View most booked table");
            System.out.println("6. View average group size");
            System.out.println("7. View active reservations");
            System.out.println("8. View past reservations");
            System.out.println("9. Update reservation status");
            System.out.println("10.  Return to main menu");
            System.out.print("Enter your choice: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    restaurant.showAllReservations();
                    break;
                case 2:
                    System.out.println(restaurant.viewMonthWithHighBookings());
                    break;
                case 3:
                    generateMonthlySummary();
                    break;
                case 4:
                    predictFutureBookings();
                    break;
                case 5:
                    viewMostBookedTable();
                    break;
                case 6:
                    restaurant.showAverageGroupSize();
                    break;
                case 7:
                    viewActiveReservations();
                    break;
                case 8:
                    viewPastReservations();
                    break;
                case 9:
                    updateReservationStatus();
                    break;
                case 10:
                    return; // Return to main menu
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private void makeReservation() {
        try {
            System.out.print("Enter customer name: ");
            String customerName = scanner.nextLine();
            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Enter time (HH:MM): ");
            String time = scanner.nextLine();
            System.out.print("Enter number of people: ");
            int numPeople = getIntInput();

            LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
            
            if (restaurant.makeReservation(customerName, dateTime, time, numPeople)) {
                System.out.println("Reservation successful!");
                if (dateTime.isAfter(LocalDateTime.now())) {
                    Table currTable = restaurant.findAvailableTable(dateTime, numPeople);
                    Reservation newReserve = new Reservation(customerName, dateTime, numPeople, currTable);
                    activeReservations.add(newReserve);
                }
            } else {
                System.out.println("Reservation failed. No available tables for " + numPeople + " people at this time.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Wrong Date/Time format entered. Please use YYYY-MM-DD for date and HH:MM for time.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void viewActiveReservations() {
        System.out.println("Active Reservations:");
        for (Reservation reservation : activeReservations) {
            System.out.println(reservation);
        }
    }

    private void viewPastReservations() {
        System.out.println("Past Reservations:");
        for (Reservation reservation : pastReservations) {
            System.out.println(reservation);
        }
    }

    public void updateReservationStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> toMove = new ArrayList<>(currentReservations.getAllReservations());
        
        for (Reservation reservation : toMove) {
            if (reservation.getDateTime().isBefore(now)) {
                currentReservations.removeReservation(reservation.getCustomerName(), reservation.getDateTime());
                pastReservations.add(reservation);
            }
        }
    }

    private void cancelReservation() {
        System.out.print("Enter customer name: ");
        String customerName = scanner.nextLine();
        System.out.print("Enter reservation date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter reservation time (HH:MM): ");
        String time = scanner.nextLine();
    
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
    
        boolean found = false;
        Iterator<Reservation> iterator = activeReservations.iterator();
        while (iterator.hasNext()) {
            Reservation reserve = iterator.next();
            if (reserve.getCustomerName().equals(customerName) && 
                reserve.getDateTime().equals(dateTime) && 
                !reserve.isCancelled()) {
                reserve.cancel();
                iterator.remove();
                found = true;
                System.out.println("Reservation cancelled.");
                break;
            }
        }
    
        if (!found) {
            System.out.println("Reservation cancellation failed. Reservation not found or already cancelled.");
        }
    
        // Update the restaurant's reservation system
        restaurant.cancelReservation(customerName, dateTime);
    }
    private void viewAvailableTables() {
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter time (HH:MM): ");
        String time = scanner.nextLine();

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
        restaurant.showAvailableTables(dateTime);
    }

    private void generateMonthlySummary() {
        System.out.print("Enter month (YYYY-MM): ");
        String month = scanner.nextLine();
        restaurant.showMonthlySummary(month);
    }

    private void predictFutureBookings() {
        System.out.print("Enter target month (MM): ");
        String targetMonth = scanner.nextLine();
        restaurant.showPredictedBookings(targetMonth);
    }

    private void viewMostBookedTable() {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate).atTime(23, 59, 59);

        restaurant.showMostBookedTable(start, end);
    }
    
    public List<Reservation> getReservationsByCustomer(String userName) {
        List<Reservation> customerReservations = new ArrayList<>();
        
        for (Reservation res : activeReservations) {  // Use the initialized list
            if (res.getCustomerName().equalsIgnoreCase(userName)) {
                customerReservations.add(res);
            }
        }
        
        return customerReservations;
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}

