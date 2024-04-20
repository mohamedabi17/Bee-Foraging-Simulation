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
    private int foodSourceCount;
    private double lowQualityThreshold;
    private double mediumQualityThreshold;
    private double highQualityThreshold;

    private Random random; // Declare Random for random number generation

    public BeeForagingSimulationGUI(Environment environment) {
        this.environment = environment;
        setTitle("Bee Foraging Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

            // Declare instance variables
    private JTextField workerCountField;
    private JTextField scoutCountField;
    private JTextField observerCountField;
    private JTextField foodSourceCountField;
    private JTextField lowQualityThresholdField;
    private JTextField mediumQualityThresholdField;
    private JTextField highQualityThresholdField;

    // Inside the constructor or initialization method
    workerCountField=new JTextField();scoutCountField=new JTextField();observerCountField=new JTextField();foodSourceCountField=new JTextField();lowQualityThresholdField=new JTextField();mediumQualityThresholdField=new JTextField();highQualityThresholdField=new JTextField();

    random=new Random(); // Initialize Random

    // Load images/icons
    loadIcons();

    // Create and add components to the GUI
    initGUI();

    private void loadIcons() {
            // Load images for bees
            workerIcon = new ImageIcon(new ImageIcon(getClass().getResource("resources/images/Worker Bee.jpg"))
                    .getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            scoutIcon = new ImageIcon(new ImageIcon(getClass().getResource("resources/images/Scout.jpg"))
                    .getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            observerIcon = new ImageIcon(new ImageIcon(getClass().getResource("resources/images/Observer Bee.jpg"))
                    .getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            // Load images for food sources based on quality
            ImageIcon flowerIconLowQuality = new ImageIcon(
                    new ImageIcon(getClass().getResource("resources/images/Flower_low_quality.jpeg"))
                            .getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            ImageIcon flowerIconMediumQuality = new ImageIcon(
                    new ImageIcon(getClass().getResource("resources/images/Flower_medium_quality.jpeg"))
                            .getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            ImageIcon flowerIconHighQuality = new ImageIcon(
                    new ImageIcon(getClass().getResource("resources/images/Flower_high_quality.jpeg"))
                            .getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            // Load image for hive
            hiveIcon = new ImageIcon(new ImageIcon(getClass().getResource("resources/images/Hive.png"))
                    .getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));

            // Set descriptions for icons
            workerIcon.setDescription("Worker Bee");
            scoutIcon.setDescription("Scout Bee");
            observerIcon.setDescription("Observer Bee");
            hiveIcon.setDescription("Hive");

            // Assign icons based on food source quality thresholds
            double lowQualityThreshold = environment.getLowQualityThreshold();
            double mediumQualityThreshold = environment.getMediumQualityThreshold();
            double highQualityThreshold = environment.getHighQualityThreshold();

            // Low-quality food source
            if (lowQualityThreshold < mediumQualityThreshold) {
                foodSourceIcon = flowerIconLowQuality;
                foodSourceIcon.setDescription("Food Source (Low Quality)");
            }
            // Medium-quality food source
            else if (mediumQualityThreshold < highQualityThreshold) {
                foodSourceIcon = flowerIconMediumQuality;
                foodSourceIcon.setDescription("Food Source (Medium Quality)");
            }
            // High-quality food source
            else {
                foodSourceIcon = flowerIconHighQuality;
                foodSourceIcon.setDescription("Food Source (High Quality)");
            }
        }

    // Inside the simulateButton ActionListener
    // Inside the simulateButton ActionListener
    simulateButton.addActionListener(e->

    {
        // Parse user input and set simulation parameters
        int workerCount = Integer.parseInt(workerCountField.getText());
        int scoutCount = Integer.parseInt(scoutCountField.getText());
        int observerCount = Integer.parseInt(observerCountField.getText());
        int foodSourceCount = Integer.parseInt(foodSourceCountField.getText());
        double lowQualityThreshold = Double.parseDouble(lowQualityThresholdField.getText());
        double mediumQualityThreshold = Double.parseDouble(mediumQualityThresholdField.getText());
        double highQualityThreshold = Double.parseDouble(highQualityThresholdField.getText());

        // Set simulation parameters
        environment.setWorkerCount(workerCount);
        environment.setScoutCount(scoutCount);
        environment.setObserverCount(observerCount);
        environment.setFoodSourceCount(foodSourceCount);
        environment.setLowQualityThreshold(lowQualityThreshold);
        environment.setMediumQualityThreshold(mediumQualityThreshold);
        environment.setHighQualityThreshold(highQualityThreshold);

        // Trigger the simulation
        environment.simulate();

        // Update the GUI to reflect the new state of the simulation
        updateGUI();
    });

    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        JPanel gridPanel = new JPanel(new GridLayout(environment.getWidth(), environment.getHeight()));
        gridPanel.setBackground(Color.BLACK);

        gridLabels = new JLabel[environment.getWidth()][environment.getHeight()];

        for (int i = 0; i < environment.getWidth(); i++) {
            for (int j = 0; j < environment.getHeight(); j++) {
                gridLabels[i][j] = new JLabel();
                gridLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Set border color to white
                gridLabels[i][j].setForeground(Color.WHITE);
                gridPanel.add(gridLabels[i][j]);
                // Set tooltip for each JLabel
                gridLabels[i][j].setToolTipText(workerIcon.getDescription()); // Replace with the appropriate icon
            }
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        add(mainPanel);

        addParameterControls();

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
                gridLabels[i][j].setToolTipText(null); // Clear tooltip
            }
        }

        // Update grid labels to represent food sources and hive
        for (int i = 0; i < environment.getWidth(); i++) {
            for (int j = 0; j < environment.getHeight(); j++) {
                if (grid[i][j] != null) {
                    if (grid[i][j].isHive()) { // Check if it's a hive
                        gridLabels[i][j].setIcon(hiveIcon);
                        gridLabels[i][j].setToolTipText("Hive");
                    } else {
                        gridLabels[i][j].setIcon(foodSourceIcon);
                        gridLabels[i][j].setToolTipText("Food Source");
                    }
                }
            }
        }

        // Place bees on the grid
        for (Bee bee : bees) {
            int x = bee.getPositionX();
            int y = bee.getPositionY();
            ImageIcon beeIcon = null;
            String beeType = "";

            if (bee instanceof Worker) {
                beeIcon = workerIcon;
                beeType = "Worker Bee";
            } else if (bee instanceof Scout) {
                beeIcon = scoutIcon;
                beeType = "Scout Bee";
            } else if (bee instanceof Observer) {
                beeIcon = observerIcon;
                beeType = "Observer Bee";
            }

            if (beeIcon != null && x >= 0 && x < environment.getWidth() && y >= 0 && y < environment.getHeight()) {
                gridLabels[x][y].setIcon(beeIcon);
                gridLabels[x][y].setToolTipText(beeType); // Set tooltip for bee
            }
        }

        // Display the hive at the center of the grid
        int hiveX = environment.getWidth() / 2;
        int hiveY = environment.getHeight() / 2;
        gridLabels[hiveX][hiveY].setIcon(hiveIcon);
        gridLabels[hiveX][hiveY].setToolTipText("Hive");
    }

    private void addParameterControls() {
    JPanel controlPanel = new JPanel(new GridLayout(6, 2)); // Update the layout to accommodate the new parameters

    // Add controls for configuring simulation parameters
    JLabel label1 = new JLabel("Number of Workers:");
    workerCountField = new JTextField();
    controlPanel.add(label1);
    controlPanel.add(workerCountField);

    JLabel label2 = new JLabel("Number of Scouts:");
    scoutCountField = new JTextField();
    controlPanel.add(label2);
    controlPanel.add(scoutCountField);

    JLabel label3 = new JLabel("Number of Observers:");
    observerCountField = new JTextField();
    controlPanel.add(label3);
    controlPanel.add(observerCountField);

    JLabel label4 = new JLabel("Number of Food Sources:");
    foodSourceCountField = new JTextField();
    controlPanel.add(label4);
    controlPanel.add(foodSourceCountField);

    JLabel label5 = new JLabel("Low Quality Threshold:");
    lowQualityThresholdField = new JTextField();
    controlPanel.add(label5);
    controlPanel.add(lowQualityThresholdField);

    JLabel label6 = new JLabel("Medium Quality Threshold:");
    mediumQualityThresholdField = new JTextField();
    controlPanel.add(label6);
    controlPanel.add(mediumQualityThresholdField);

    JLabel label7 = new JLabel("High Quality Threshold:");
    highQualityThresholdField = new JTextField();
    controlPanel.add(label7);
    controlPanel.add(highQualityThresholdField);

    JLabel label8 = new JLabel("Maximum Trial Count:");
    JSlider trialCountSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 50);
    trialCountSlider.setMajorTickSpacing(10);
    trialCountSlider.setMinorTickSpacing(1);
    trialCountSlider.setPaintTicks(true);
    trialCountSlider.setPaintLabels(true);
    controlPanel.add(label8);
    controlPanel.add(trialCountSlider);

    // Add listener for slider value change
    trialCountSlider.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent event) {
            JSlider source = (JSlider) event.getSource();
            if (!source.getValueIsAdjusting()) {
                int value = source.getValue();
                environment.setMaximumTrialCount(value); // Update maximum trial count in the environment
            }
        }
    });

    // Add a button to trigger the simulation
    JButton simulateButton = new JButton("Simulate");
    controlPanel.add(simulateButton);

    // Add an ActionListener to the button
    simulateButton.addActionListener(e -> {
        // Parse user input and set simulation parameters
        int workerCount = Integer.parseInt(workerCountField.getText());
        int scoutCount = Integer.parseInt(scoutCountField.getText());
        int observerCount = Integer.parseInt(observerCountField.getText());
        int foodSourceCount = Integer.parseInt(foodSourceCountField.getText());
        double lowQualityThreshold = Double.parseDouble(lowQualityThresholdField.getText());
        double mediumQualityThreshold = Double.parseDouble(mediumQualityThresholdField.getText());
        double highQualityThreshold = Double.parseDouble(highQualityThresholdField.getText());

        // Set simulation parameters
        environment.setWorkerCount(workerCount);
        environment.setScoutCount(scoutCount);
        environment.setObserverCount(observerCount);
        environment.setFoodSourceCount(foodSourceCount);
        environment.setLowQualityThreshold(lowQualityThreshold);
        environment.setMediumQualityThreshold(mediumQualityThreshold);
        environment.setHighQualityThreshold(highQualityThreshold);

        // Trigger the simulation
        environment.simulate();

        // Update the GUI to reflect the new state of the simulation
        updateGUI();
    });

    // Add control panel to main panel
    add(controlPanel, BorderLayout.SOUTH);
}
    }
    public static void main(String[] args) {
        // Create an instance of Environment
        Environment environment = new Environment(10, 10, 10);

        // Place some food sources
        // Place three food sources with different qualities
        environment.placeFoodSource(2, 3, 0.3); // Low quality
        environment.placeFoodSource(5, 5, 0.6); // Medium quality
        environment.placeFoodSource(7, 8, 0.9); // High quality

        environment.addHive();
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
