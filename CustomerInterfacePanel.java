import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CustomerInterfacePanel extends JDialog {
    private Restaurant restaurant;
    private JTextField nameField;
    private JDatePickerImpl datePicker;
    private JComboBox<String> timeField;
    private JComboBox<String> tableField;
    private RestaurantReservationManager restaurantReservationManager;

    // Constructor should match this signature
    public CustomerInterfacePanel(JFrame parent, Restaurant restaurant, RestaurantReservationManager manager) {
        super(parent, "Customer", true);
        this.restaurant = restaurant;
        this.restaurantReservationManager = manager; // Fix: Correctly assigning manager
        setLayout(new BorderLayout());
        setSize(800, 600);



        // Create the form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        
        // Customer Name
        formPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        // Date Picker
        formPanel.add(new JLabel("Date (YYYY-MM-DD): "));
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        formPanel.add(datePicker);

        // Time Picker
        formPanel.add(new JLabel("Time (HH:MM): "));
        String[] timeSlots = {
            "07:00 - 09:00", "09:00 - 11:00", "11:00 - 13:00",
            "13:00 - 15:00", "15:00 - 17:00", "17:00 - 19:00"
        };
        timeField = new JComboBox<>(timeSlots);
        formPanel.add(timeField);

        // Table Selector
        formPanel.add(new JLabel("Number of People:"));
        String[] tables = {
            "Table 1: Capacity 2, NOT VIP, $50.00",
            "Table 2: Capacity 4, VIP, $80.00",
            "Table 3: Capacity 6, NOT VIP, $120.00",
            "Table 4: Capacity 8, VIP, $150.00",
            "Table 5: Capacity 1, NOT VIP, $30.00",
            "Table 6: Capacity 8, VIP, $150.00",
            "Table 7: Capacity 3, VIP, $70.00",
            "Table 8: Capacity 8, NOT VIP, $150.00",
            "Table 9: Capacity 5, VIP, $110.00",
            "Table 10: Capacity 2, VIP, $50.00",
            "Table 11: Capacity 7, NOT VIP, $130.00",
            "Table 12: Capacity 8, VIP, $150.00",
            "Table 13: Capacity 7, NOT VIP, $70.00",
            "Table 14: Capacity 3, VIP, $70.00",
            "Table 15: Capacity 4, NOT VIP, $80.00"
        };
        tableField = new JComboBox<>(tables);
        formPanel.add(tableField);

        // Add form panel to main layout
        add(formPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        JButton makeReservationButton = new JButton("Make Reservation");
        JButton cancelReservationButton = new JButton("Cancel Reservation");
        JButton viewAvailableTablesButton = new JButton("View Available Tables");
        JButton viewReservationsButton=new JButton("View Reservation");
        JButton backButton = new JButton("Back");

        // Add buttons to the button panel
        buttonPanel.add(makeReservationButton);
        buttonPanel.add(cancelReservationButton);
        buttonPanel.add(viewAvailableTablesButton);
        buttonPanel.add(viewReservationsButton);
        buttonPanel.add(backButton);

        // Add button panel to main layout
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        makeReservationButton.addActionListener(e -> makeReservation());
        cancelReservationButton.addActionListener(e -> cancelReservation());
        viewAvailableTablesButton.addActionListener(e -> viewAvailableTables());
        viewReservationsButton.addActionListener(e -> viewReservations());
        backButton.addActionListener(e -> dispose());
    }

    private void makeReservation() {
        String customerName = nameField.getText();
        Date selectedDate = (Date) datePicker.getModel().getValue();
        String selectedTime = (String) timeField.getSelectedItem();
        String selectedTable = (String) tableField.getSelectedItem();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(selectedDate);
            String startTime = selectedTime.split(" - ")[0];
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(formattedDate), LocalTime.parse(startTime));
            
            int capacity = Integer.parseInt(selectedTable.split("Capacity ")[1].split(",")[0]);
            boolean success = restaurant.makeReservation(customerName, dateTime, formattedDate, capacity);
            JOptionPane.showMessageDialog(this, success ? "Reservation successful!" : "Reservation failed. No available tables.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check your data.");
        }
    }

    private void cancelReservation() {
        String customerName = nameField.getText();
        Date selectedDate = (Date) datePicker.getModel().getValue();
        String selectedTime = (String) timeField.getSelectedItem();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(selectedDate);
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(formattedDate), LocalTime.parse(selectedTime.split(" - ")[0]));
            boolean success = restaurant.cancelReservation(customerName, dateTime);
            JOptionPane.showMessageDialog(this, success ? "Reservation cancelled." : "Reservation cancellation failed.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check your data.");
        }
    }

    private void viewAvailableTables() {
        Date selectedDate = (Date) datePicker.getModel().getValue();
        String selectedTime = (String) timeField.getSelectedItem();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(selectedDate);
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(formattedDate), LocalTime.parse(selectedTime.split(" - ")[0]));
            restaurant.showAvailableTables(dateTime);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check your data.");
        }
    }
    
    private void viewReservations() {
        String customerName = nameField.getText();
        
        try {
            List<Reservation> reservations = restaurantReservationManager.getReservationsByCustomer(customerName);
    
            if (reservations.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No reservations found for the specified customer.");
            } else {
                StringBuilder reservationsText = new StringBuilder("Reservations:\n");
                for (Reservation reservation : reservations) {
                    reservationsText.append(reservation.toString()).append("\n");
                }
                JOptionPane.showMessageDialog(this, reservationsText.toString());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred while retrieving reservations.");
        }
    }
    
    
    


    // Private inner class for date formatting
    class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws java.text.ParseException {
            if (value != null) {
                java.util.Calendar cal = (java.util.Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}
