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
    private ImageIcon flowerIconLowQuality;
    private ImageIcon flowerIconMediumQuality;
    private ImageIcon flowerIconHighQuality;

    private ImageIcon hiveIcon;

    private JButton simulateButton;
    private JTextField workerCountField;
    private JTextField scoutCountField;
    private JTextField observerCountField;

    private Random random; // Declare Random for random number generation

    public BeeForagingSimulationGUI(Environment environment) {
        this.environment = environment;
        setTitle("Bee Foraging Simulation");
        setSize(1200, 600);
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
        workerIcon = new ImageIcon(new ImageIcon(getClass().getResource("resources/images/Worker Bee.jpg"))
                .getImage().getScaledInstance(100, 35, Image.SCALE_FAST));
        scoutIcon = new ImageIcon(new ImageIcon(getClass().getResource("resources/images/Scout.jpg"))
                .getImage().getScaledInstance(100, 35, Image.SCALE_FAST));
        observerIcon = new ImageIcon(new ImageIcon(getClass().getResource("resources/images/Observer Bee.jpg"))
                .getImage().getScaledInstance(100, 35, Image.SCALE_FAST));
        // Load images for food sources based on quality
        flowerIconLowQuality = new ImageIcon(
                new ImageIcon(getClass().getResource("resources/images/Flower_low_quality.jpg"))
                        .getImage().getScaledInstance(100, 35, Image.SCALE_FAST));
        flowerIconMediumQuality = new ImageIcon(
                new ImageIcon(getClass().getResource("resources/images/Flower_medium_quality.jpg"))
                        .getImage().getScaledInstance(100, 35, Image.SCALE_FAST));
        flowerIconHighQuality = new ImageIcon(
                new ImageIcon(getClass().getResource("resources/images/Flower_high_quality.jpg"))
                        .getImage().getScaledInstance(100, 35, Image.SCALE_FAST));
        // Load image for hive
        hiveIcon = new ImageIcon(new ImageIcon(getClass().getResource("resources/images/Hive.jpg"))
                .getImage().getScaledInstance(100, 35, Image.SCALE_FAST));

        // Set descriptions for icons
        workerIcon.setDescription("Worker Bee");
        scoutIcon.setDescription("Scout Bee");
        observerIcon.setDescription("Observer Bee");
        hiveIcon.setDescription("Hive");

    }

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
                // Adjust the preferred size of the label to increase height
                gridLabels[i][j].setPreferredSize(new Dimension(100, 135)); // Increase height to 135 pixels
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
                        // Set food source icon based on quality
                        double quality = grid[i][j].getQuality();
                        if (quality < environment.getLowQualityThreshold()) {
                            gridLabels[i][j].setIcon(flowerIconLowQuality);
                            gridLabels[i][j].setToolTipText("Food Source (Low Quality)");
                        } else if (quality < environment.getMediumQualityThreshold()) {
                            gridLabels[i][j].setIcon(flowerIconMediumQuality);
                            gridLabels[i][j].setToolTipText("Food Source (Medium Quality)");
                        } else {
                            gridLabels[i][j].setIcon(flowerIconHighQuality);
                            gridLabels[i][j].setToolTipText("Food Source (High Quality)");
                        }
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
        JPanel controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add controls for configuring simulation parameters
        JLabel label1 = new JLabel("Number of Workers:");
        controlPanel.add(label1, gbc);
        gbc.gridx++;
        workerCountField = new JTextField(10);
        controlPanel.add(workerCountField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel label2 = new JLabel("Number of Scouts:");
        controlPanel.add(label2, gbc);
        gbc.gridx++;
        scoutCountField = new JTextField(10);
        controlPanel.add(scoutCountField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        JLabel label3 = new JLabel("Number of Observers:");
        controlPanel.add(label3, gbc);
        gbc.gridx++;
        observerCountField = new JTextField(10);
        controlPanel.add(observerCountField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Add note about food quality
        JLabel qualityNoteLabel = new JLabel("Note: The more red the food is, the higher the quality.");
        qualityNoteLabel.setForeground(Color.RED); // Set the text color to red
        controlPanel.add(qualityNoteLabel, gbc);
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.gridy++;

        JLabel label8 = new JLabel("Maximum Trial Count:");
        controlPanel.add(label8, gbc);
        gbc.gridx++;
        JSlider trialCountSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 100);
        trialCountSlider.setMajorTickSpacing(10);
        trialCountSlider.setMinorTickSpacing(1);
        trialCountSlider.setPaintTicks(true);
        trialCountSlider.setPaintLabels(true);
        controlPanel.add(trialCountSlider, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

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
        simulateButton = new JButton("Simulate"); // Assign the value to the class member simulateButton
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        controlPanel.add(simulateButton, gbc);

        // Add control panel to main panel
        add(controlPanel, BorderLayout.SOUTH);
    }

    public JButton getSimulateButton() {
        return simulateButton;
    }

    // Getter method for workerCountField
    public JTextField getWorkerCountField() {
        return workerCountField;
    }

    // Getter method for scoutCountField
    public JTextField getScoutCountField() {
        return scoutCountField;
    }

    // Getter method for observerCountField
    public JTextField getObserverCountField() {
        return observerCountField;
    }

    public static void main(String[] args) {
        // Create an instance of Environment
        Environment environment = new Environment(10, 10);

        // Create an instance of BeeForagingSimulationGUI to access its components
        BeeForagingSimulationGUI gui = new BeeForagingSimulationGUI(environment);

        // Add action listener to the simulateButton to trigger simulation
        gui.getSimulateButton().addActionListener(e -> {
            // Validate and parse input for numeric fields
            try {
                int workerCount = Integer.parseInt(gui.getWorkerCountField().getText());
                int scoutCount = Integer.parseInt(gui.getScoutCountField().getText());
                int observerCount = Integer.parseInt(gui.getObserverCountField().getText());

                for (int i = 0; i < workerCount; i++) {
                    environment.addBee(new Worker());
                }

                for (int i = 0; i < scoutCount; i++) {
                    environment.addBee(new Scout());
                }

                for (int i = 0; i < observerCount; i++) {
                    environment.addBee(new Observer());
                }

                environment.addHive();

                // Trigger the simulation
                environment.simulate();

                // Update the GUI to reflect the new state of the simulation
                gui.updateGUI();
            } catch (NumberFormatException ex) {
                // Handle parsing errors
                JOptionPane.showMessageDialog(gui, "Please enter valid numeric values for all fields.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Display the GUI
        SwingUtilities.invokeLater(() -> {
            gui.setVisible(true);
        });
    }

}
