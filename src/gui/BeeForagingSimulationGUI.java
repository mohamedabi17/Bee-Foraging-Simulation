package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random; // Import Random for random number generation

import entities.Bee;
import entities.Worker;
import entities.Observer;
import entities.Scout;
import simulation.Environment;
import simulation.FoodSource;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener; // Add this import statement for ChangeListener

public class BeeForagingSimulationGUI extends JFrame {
    private Environment environment;
    private JLabel[][] gridLabels; // Declare gridLabels as a class member

    private ImageIcon workerIcon;
    private ImageIcon scoutIcon;
    private ImageIcon observerIcon;
    private ImageIcon foodSourceIcon;
    private ImageIcon hiveIcon;

    private Random random; // Declare Random for random number generation

    public BeeForagingSimulationGUI(Environment environment) {
        this.environment = environment;
        setTitle("Bee Foraging Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        random = new Random(); // Initialize Random

        // Load images/icons
        loadIcons();

        // Create and add components to the GUI
        initGUI();
    }

    private void loadIcons() {
        // Load images for bees
        workerIcon = new ImageIcon(getClass().getResource("resources/images/Worker Bee.jpg"));
        // Load other icons similarly
        scoutIcon = new ImageIcon(getClass().getResource("resources/images/Scout Bee.jpg"));
        observerIcon = new ImageIcon(getClass().getResource("resources/images/Observer Bee.jpg"));
        // Load image for food source
        foodSourceIcon = new ImageIcon(getClass().getResource("resources/images/Bee Food.jpg"));
        // Load image for hive
        hiveIcon = new ImageIcon(getClass().getResource("resources/images/Hive.png"));
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(environment.getWidth(), environment.getHeight()));

        // Initialize gridLabels
        gridLabels = new JLabel[environment.getWidth()][environment.getHeight()];

        // Create JLabels to represent grid cells
        for (int i = 0; i < environment.getWidth(); i++) {
            for (int j = 0; j < environment.getHeight(); j++) {
                gridLabels[i][j] = new JLabel();
                gridLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridPanel.add(gridLabels[i][j]);
            }
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Add parameter controls
        addParameterControls();

        // Update GUI with initial state of the simulation
        updateGUI();
    }

    private void updateGUI() {
        // Update JLabels based on the state of the simulation (bees, food sources,
        // etc.)
        List<Bee> bees = environment.getBees();
        FoodSource[][] grid = environment.getGrid();

        // Clear previous state
        for (int i = 0; i < environment.getWidth(); i++) {
            for (int j = 0; j < environment.getHeight(); j++) {
                gridLabels[i][j].setIcon(null); // Clear icon
            }
        }

        // Update grid labels to represent food sources and hive
        for (int i = 0; i < environment.getWidth(); i++) {
            for (int j = 0; j < environment.getHeight(); j++) {
                if (grid[i][j] != null) {
                    if (grid[i][j].isHive()) { // Check if it's a hive
                        gridLabels[i][j].setIcon(hiveIcon);
                    } else {
                        gridLabels[i][j].setIcon(foodSourceIcon);
                    }
                }
            }
        }

        // Place bees on the grid
        for (Bee bee : bees) {
            int x = bee.getPositionX();
            int y = bee.getPositionY();
            ImageIcon beeIcon = null;

            if (bee instanceof Worker) {
                beeIcon = workerIcon;
            } else if (bee instanceof Scout) {
                beeIcon = scoutIcon;
            } else if (bee instanceof Observer) {
                beeIcon = observerIcon;
            }

            if (beeIcon != null && x >= 0 && x < environment.getWidth() && y >= 0 && y < environment.getHeight()) {
                gridLabels[x][y].setIcon(beeIcon);
            }
        }
    }

    private void addParameterControls() {
        JPanel controlPanel = new JPanel(new GridLayout(4, 2)); // Update the layout to accommodate the new button

        // ... existing code ...

        // Add a button to trigger the simulation
        JButton simulateButton = new JButton("Simulate");
        controlPanel.add(simulateButton);

        // Add an ActionListener to the button
        simulateButton.addActionListener(e -> {
            // Trigger the simulation
            environment.simulate();

            // Update the GUI to reflect the new state of the simulation
            updateGUI();
        });

        JLabel label1 = new JLabel("Parameter 1:");
        JTextField textField1 = new JTextField();
        controlPanel.add(label1);
        controlPanel.add(textField1);

        JLabel label2 = new JLabel("Parameter 2:");
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        controlPanel.add(label2);
        controlPanel.add(slider);

        JLabel label3 = new JLabel("Parameter 3:");
        JCheckBox checkBox = new JCheckBox("Enable");
        controlPanel.add(label3);
        controlPanel.add(checkBox);

        // Add listener for slider value change
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider) event.getSource();
                if (!source.getValueIsAdjusting()) {
                    int value = source.getValue();
                    // Update parameter value based on slider
                    // Example: environment.setParameter3(value);
                }
            }
        });

        // Add control panel to main panel
        add(controlPanel, BorderLayout.SOUTH);

        // Add control panel to main panel
        add(controlPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // Create an instance of Environment
        Environment environment = new Environment(10, 10, 10);

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
