import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ManagerInterfacePanel extends JDialog {
    private Restaurant restaurant;
    private RestaurantReservationManager restaurantReservationManager;

    public ManagerInterfacePanel(JFrame parent, Restaurant restaurant, RestaurantReservationManager manager) {
        super(parent, "Manager Interface", true);
        this.restaurant = restaurant;
        this.restaurantReservationManager = manager;
        setLayout(new BorderLayout());
        setSize(800, 600);
        initializeUI();
    }

    private void initializeUI() {
        JButton viewReservationsButton = new JButton("View All Reservations");
        viewReservationsButton.addActionListener(e -> viewAllReservations());

        JButton viewActiveReservationsButton = new JButton("View Active Reservations");
        viewActiveReservationsButton.addActionListener(e -> viewActiveReservations());

        JButton viewPastReservationsButton = new JButton("View Past Reservations");
        viewPastReservationsButton.addActionListener(e -> viewPastReservations());

        JPanel panel = new JPanel();
        panel.add(viewReservationsButton);
        panel.add(viewActiveReservationsButton);
        panel.add(viewPastReservationsButton);

        add(panel, BorderLayout.NORTH);
    }

    private void viewAllReservations() {
        List<Reservation> reservations = restaurant.showAllReservations();

        // Create table model
        String[] columnNames = {"Customer Name", "Date", "Time", "Number of People", "Table"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Reservation res : reservations) {
            Object[] row = {
                res.getCustomerName(),
                res.getDateTime().toLocalDate(),
                res.getDateTime().toLocalTime(),
                res.getNumPeople(),
                res.getTable().toString()
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("All Reservations"), BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Display table in dialog
        JOptionPane.showMessageDialog(this, tablePanel, "Reservations", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewActiveReservations() {
        List<Reservation> reservations = restaurantReservationManager.getActiveReservations();

        // Create table model
        String[] columnNames = {"Customer Name", "Date", "Time", "Number of People", "Table"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Reservation res : reservations) {
            Object[] row = {
                res.getCustomerName(),
                res.getDateTime().toLocalDate(),
                res.getDateTime().toLocalTime(),
                res.getNumPeople(),
                res.getTable().toString()
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("Active Reservations"), BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Display table in dialog
        JOptionPane.showMessageDialog(this, tablePanel, "Active Reservations", JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewPastReservations() {
        List<Reservation> reservations = restaurantReservationManager.getPastReservations();

        // Create table model
        String[] columnNames = {"Customer Name", "Date", "Time", "Number of People", "Table"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Reservation res : reservations) {
            Object[] row = {
                res.getCustomerName(),
                res.getDateTime().toLocalDate(),
                res.getDateTime().toLocalTime(),
                res.getNumPeople(),
                res.getTable().toString()
            };
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JLabel("Past Reservations"), BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Display table in dialog
        JOptionPane.showMessageDialog(this, tablePanel, "Past Reservations", JOptionPane.INFORMATION_MESSAGE);
    }
}
2. Verify Methods in RestaurantReservationManager
Make sure that methods like getActiveReservations() and getPastReservations() in the RestaurantReservationManager are correctly implemented and return the appropriate data.

Here’s an example:

java
Copy code
public List<Reservation> getActiveReservations() {
    return new ArrayList<>(activeReservations);
}

public List<Reservation> getPastReservations() {
    return new ArrayList<>(pastReservations);
}