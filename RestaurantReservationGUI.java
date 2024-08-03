import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class RestaurantReservationGUI {
    private Restaurant restaurant;
    private RestaurantReservationManager manager; // Added manager
    private JFrame frame;
    private JPanel mainPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestaurantReservationGUI().createAndShowGUI());
    }

    public RestaurantReservationGUI() {
        restaurant = new Restaurant();
        manager = new RestaurantReservationManager(); // Initialize manager
    }

    private void createAndShowGUI() {
        frame = new JFrame("Restaurant Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        JButton customerButton = new JButton("Customer Interface");
        JButton managerButton = new JButton("Manager Interface");
        JButton exitButton = new JButton("Exit");

        customerButton.addActionListener(e -> showCustomerInterface());
        managerButton.addActionListener(e -> showManagerInterface());
        exitButton.addActionListener(e -> System.exit(0));

        mainPanel.add(customerButton);
        mainPanel.add(managerButton);
        mainPanel.add(exitButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void showCustomerInterface() {
        // Pass the manager object to the CustomerInterfacePanel constructor
        CustomerInterfacePanel customerPanel = new CustomerInterfacePanel(frame, restaurant, manager);
        customerPanel.setVisible(true);
    }

    private void showManagerInterface() {
        // Ensure ManagerInterfacePanel has the correct constructor if needed
        ManagerInterfacePanel managerPanel = new ManagerInterfacePanel(frame, restaurant, manager);
        managerPanel.setVisible(true);
    }
}
