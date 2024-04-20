package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BeeForagingSimulationGUI extends JFrame {
    private Environment environment;

    public BeeForagingSimulationGUI(Environment environment) {
        this.environment = environment;
        setTitle("Bee Foraging Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create and add components to the GUI
        initGUI();
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(environment.getWidth(), environment.getHeight()));

        // Create JLabels to represent grid cells
        JLabel[][] gridLabels = new JLabel[environment.getWidth()][environment.getHeight()];
        for (int i = 0; i < environment.getWidth(); i++) {
            for (int j = 0; j < environment.getHeight(); j++) {
                gridLabels[i][j] = new JLabel();
                gridLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridPanel.add(gridLabels[i][j]);
            }
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Update GUI with initial state of the simulation
        updateGUI();
    }

    private void updateGUI() {
        // Update JLabels based on the state of the simulation (bees, food sources,
        // etc.)
        List<Bee> bees = environment.getBees();
        FoodSource[][] grid = environment.getGrid();

        // Example: Update grid labels to represent bees and food sources
        for (int i = 0; i < environment.getWidth(); i++) {
            for (int j = 0; j < environment.getHeight(); j++) {
                if (grid[i][j] != null) {
                    // Food source present at grid position
                    // Set background color of JLabel to indicate food source
                    // You can customize this based on your requirements
                    gridLabels[i][j].setBackground(Color.GREEN);
                } else {
                    gridLabels[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    public static void main(String[] args) {
        // Create an instance of Environment
        Environment environment = new Environment(10, 10);

        // Place some food sources
        environment.placeFoodSource(2, 3, 0.8);
        environment.placeFoodSource(7, 8, 0.6);

        // Add some bees to the environment
        environment.addBee(new Worker());
        environment.addBee(new Scout());
        environment.addBee(new Observer());

        // Create and display the GUI
        SwingUtilities.invokeLater(() -> {
            BeeForagingSimulationGUI gui = new BeeForagingSimulationGUI(environment);
            gui.setVisible(true);
        });
    }
}
