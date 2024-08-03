import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 class CurrentReservations {
    private class Node {
        Reservation reservation;
        Table table;
        Node next;

        Node(Reservation reservation, Table table) {
            this.reservation = reservation;
            this.table = table;
        }
    }

    private Node head;
    private int size;

    public CurrentReservations() {
        this.head = null;
        this.size = 0;
    }

    public void addReservation(Reservation reservation, Table table) {
        Node newNode = new Node(reservation, table);
        if (head == null) {
            head = newNode;
            head.next = head;
        } else {
            Node temp = head;
            while (temp.next != head) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.next = head;
        }
        size++;
    }

    public void removeReservation(String customerName, LocalDateTime dateTime) {
        if (head == null) return;
        Node current = head;
        Node previous = null;

        do {
            if (current.reservation.getCustomerName().equals(customerName) && 
                current.reservation.getDateTime().equals(dateTime)) {
                if (previous != null) {
                    previous.next = current.next;
                } else {
                    head = head.next;
                    Node temp = head;
                    while (temp.next != current) {
                        temp = temp.next;
                    }
                    temp.next = head;
                }
                size--;
                return;
            }
            previous = current;
            current = current.next;
        } while (current != head);
    }

    public Reservation findReservation(String customerName, LocalDateTime dateTime) {
        Node current = head;
        if (head != null) {
            do {
                if (current.reservation.getCustomerName().equals(customerName) && 
                    current.reservation.getDateTime().equals(dateTime)) {
                    return current.reservation;
                }
                current = current.next;
            } while (current != head);
        }
        return null;
    }

    public void displayReservations() {
        if (head == null) {
            System.out.println("No active reservations.");
            return;
        }
        Node current = head;
        do {
            System.out.println(current.reservation + " - Table: " + current.table.getTableId());
            current = current.next;
        } while (current != head);
    }

    

    public int getSize() {
        return size;
    }

    public Table getMostBookedTable(LocalDateTime startDate, LocalDateTime endDate) {
        Map<Table, Integer> bookingCounts = new HashMap<>();
        Node current = head;
        if (head != null) {
            do {
                LocalDateTime reservationDateTime = current.reservation.getDateTime();
                if (reservationDateTime.isAfter(startDate) && reservationDateTime.isBefore(endDate)) {
                    bookingCounts.put(current.table, bookingCounts.getOrDefault(current.table, 0) + 1);
                }
                current = current.next;
            } while (current != head);
        }
        return bookingCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> allReservations = new ArrayList<>();
        if (head == null) {
            return allReservations;  // Return an empty list if there are no reservations
        }
        
        Node current = head;
        do {
            allReservations.add(current.reservation);
            current = current.next;
        } while (current != head);
        
        return allReservations;
    }

    public double getAverageGroupSize() {
        if (size == 0) return 0;
        int totalPeople = 0;
        Node current = head;
        do {
            totalPeople += current.reservation.getNumberOfPeople();
            current = current.next;
        } while (current != head);
        return (double) totalPeople / size;
    }

    
}