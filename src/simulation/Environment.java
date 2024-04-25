package simulation;

import entities.Bee;
import entities.Observer;
import entities.Hive;
import entities.Scout;
import entities.Tour;
import entities.Worker;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Image;
import javax.swing.JLabel;
import gui.BeeForagingSimulationGUI;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JOptionPane;
import javax.swing.*;

public class Environment {
    private FoodSource[][] grid;
    private JLabel[][] gridLabels;
    public List<Bee> bees;
    private int width;
    private int height;
    private Random random;
    private Hive hive;
    private double lowQualityThreshold;
    private double mediumQualityThreshold;
    private double highQualityThreshold;
    private int numberOfCities = 5; // Define this variable
    private static final double BEST_SOURCE_QUALITY_THRESHOLD = 0.5;
    private static int MAX_ATTEMPTS = 10;
    private static Environment instance;
    private ImageIcon DANCE_ICON;
    private ImageIcon workerIcon;
    private ImageIcon scoutIcon;
    private ImageIcon observerIcon;
    private final Object lock = new Object();
    private boolean updateComplete = false;

    private ImageIcon flowerIconLowQuality;
    private ImageIcon flowerIconMediumQuality;
    private ImageIcon flowerIconHighQuality;
    private ImageIcon hiveIcon;

    private BeeForagingSimulationGUI gui;

    public void setGUI(BeeForagingSimulationGUI gui) {
        this.gui = gui;
    }

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;

        this.grid = new FoodSource[width][height];
        this.bees = new ArrayList<>();
        this.random = new Random();

        lowQualityThreshold = 0.4;
        mediumQualityThreshold = 0.6;
        highQualityThreshold = 0.8;

        initializeFoodSources();
        addHive();
        loadIcons();
    }

    public void placeFoodSource(int posX, int posY, double quality) {
        while (!isValidPosition(posX, posY)) {
            // Randomly generate new position until it's valid
            posX = random.nextInt(this.getWidth());
            posY = random.nextInt(this.getHeight());
        }

        // Once a valid position is found, place the food source
        grid[posX][posY] = new FoodSource(posX, posY, quality);
    }

    public void loadIcons() {
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

    public void addBee(String type) {
        int posX, posY; // Declare variables to hold position
        Bee bee = null; // Declare bee variable outside of if blocks

        if (type.equals("Scout")) {
            // Randomly generate new position until it's valid
            posX = random.nextInt(this.getWidth());
            posY = random.nextInt(this.getHeight());
            while (!isValidPosition(posX, posY)) {
                posX = random.nextInt(this.getWidth());
                posY = random.nextInt(this.getHeight());
            }
            bee = new Scout(posX, posY);
        } else if (type.equals("Worker")) {
            // Randomly generate new position until it's valid
            posX = random.nextInt(this.getWidth());
            posY = random.nextInt(this.getHeight());
            while (!isValidPosition(posX, posY)) {
                posX = random.nextInt(this.getWidth());
                posY = random.nextInt(this.getHeight());
            }
            bee = new Worker(posX, posY);
        } else if (type.equals("Observer")) {
            // Randomly generate new position until it's valid
            posX = random.nextInt(this.getWidth());
            posY = random.nextInt(this.getHeight());
            while (!isValidPosition(posX, posY)) {
                posX = random.nextInt(this.getWidth());
                posY = random.nextInt(this.getHeight());
            }
            bee = new Observer(posX, posY);
        }

        // Add the bee to the list if it's not null
        if (bee != null) {
            bees.add(bee);
        }
    }

    public void addHive() {
        hive = new Hive(width / 2, height / 2); // Place hive at the center of the environment
    }

    public int simulate() {
        final boolean[] bestSourceFound = { false }; // Declare as final array
        final int[] exhaustedFoodSources = { 0 }; // Declare as final array

        SwingUtilities.invokeLater(() -> {

        });

        for (Bee bee : bees) {
            // Update the GUI after each bee moves

            gui.updateBeePositionOnGrid(bee);

            // Look for food
            int result = LookForFood(bee, bestSourceFound);

            // Trigger the simulation after displaying bees
            UpdatefoodSourceQuality(exhaustedFoodSources);

            if (result == 1) {
                bestSourceFound[0] = true;
                displayEndOfSimulation("Best Food Source Found");
                return 1;
            }

            // Update food source quality
            // UpdatefoodSourceQuality(exhaustedFoodSources);

            // Check if any food source is exhausted
            if (exhaustedFoodSources[0] == 3) {
                System.out.println("All food sources exhausted. Simulation terminated.5");
                displayEndOfSimulation("All food sources exhausted. Simulation terminated.4");
                return 1; // End the simulation
            }

            try {
                Thread.sleep(1000); // Introduce a delay of 1 second (adjust as needed)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // Display popup dialog with results
        if (bestSourceFound[0] || exhaustedFoodSources[0] == 3) {
            String resultMessage = bestSourceFound[0] ? "Best source found." : "All food sources exhausted.";
            // appendToSimulationLog(resultMessage);
        }

        return 0;
    }

    // public void simulateUntilFoodDepleted(int maxIterations) {
    // int iteration = 0;

    // while (!isFoodDepleted() && iteration < maxIterations) {
    // simulate(); // Perform one iteration of the simulation
    // iteration++;
    // }

    // // Check if the simulation ended due to food depletion or maximum iterations
    // // reached
    // if (isFoodDepleted()) {
    // System.out.println("Food is depleted. Simulation terminated.");
    // displayEndOfSimulation(gridLabels, "Food is depleted");
    // }
    // }

    public int LookForFood(Bee bee, boolean[] bestSourceFound) {

        System.out.println("LookForFood Checkin");

        // Get the coordinates of the hive
        int hiveX = width / 2;
        int hiveY = height / 2;

        int x = bee.getPositionX(); // Get the bee's previous X position
        int y = bee.getPositionY(); // Get the bee's previous Y position

        System.out.println(grid[x][y]);
        // System.out.println(grid[x][y].getQuality());
        System.out.println(bee.getCurrentFoodSource());

        // Debug message: Log bee's current position
        System.out.println(bee.getClass().getSimpleName() + " is at position (" + x + ", " + y + ")");

        // Check if the grid cell contains a food source with quality above threshold
        if (grid[x][y] != null && grid[x][y].getQuality() > 0.2) {
            grid[x][y].incrementAttemptCounter();
            System.out.println(" Food Checked ,Counter of Food Incremented By" + bee.getClass().getSimpleName() + ".");

            // If the bee doesn't have a current food source or the current food source
            // quality is less than the current grid cell quality, assign the current grid
            // cell as the bee's assigned food source
            if (bee.getCurrentFoodSource() == null
                    || bee.getCurrentFoodSource().getQuality() > grid[x][y].getQuality()) {
                bee.setAssignedFoodSource(grid[x][y]);
                System.out.println(" Food Checked, Counter of Food Incremented By" + bee.getClass().getSimpleName()
                        + "Quality is better and  new food Assigned");

            }

        }
        // Check if the bee has a current food source
        if (bee.getCurrentFoodSource() != null) {
            // Perform actions based on the type of bee
            if (bee instanceof Worker) {

                ((Worker) bee).returnToHive(hiveX, hiveY); // Return to the hive
                System.out.println(bee.getClass().getSimpleName() + " returning to hive.");

                // Check if the worker bee is returning to the hive and display its behavior
                // accordingly
                for (Worker worker : this.getWorkers()) {
                    if (worker.getReturingToHive()) {
                        performDance(worker);
                        worker.communicateSourceInformation(grid[x][y]);
                        System.out.println(bee.getClass().getSimpleName() + " performing dance at hive.");
                    }
                    // worker.exploreNeighborhood(this);
                }
            } else if (bee instanceof Scout) {
                // If the scout bee attempted to find food more than 3 times in the same cell,
                // remove the food source from the grid
                if (grid[x][y].getAttemptCount() > 3) {
                    gridLabels[x][y].setIcon(null);
                    grid[x][y] = null;
                    System.out.println(
                            "Food source at position (" + x + ", " + y + ") removed due to multiple attempts.");
                } else {
                    ((Scout) bee).exploreSource(this, grid[x][y]);
                    ((Scout) bee).exploreNeighborhood(this);
                }
                System.out.println(bee.getClass().getSimpleName() + " choosing random food source.");
            } else if (bee instanceof Observer) {
                ((Observer) bee).observeAndChooseFoodSource(this);
                System.out.println(bee.getClass().getSimpleName() + " observing and choosing food source.");
            }

            // Check if the best source is found
            if (bee.getCurrentFoodSource().getQuality() >= BEST_SOURCE_QUALITY_THRESHOLD) {
                bestSourceFound[0] = true;
                System.out.println("Best source found by " + bee.getClass().getSimpleName() + ".");
                displayEndOfSimulation("Best source found by");
                return 1;
            }
        }

        return 0;
    }

    public int UpdatefoodSourceQuality(int[] exhaustedFoodSources) {

        // SwingUtilities.invokeLater(() -> {
        // Trigger the simulation after displaying bees
        // Update food source qualities
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] != null) {
                    // Update food source quality based on some criteria
                    double newQuality = calculateNewQuality(grid[i][j]);
                    // if (newQuality < 0.2) {
                    // System.out.println("All food sources exhausted. Simulation terminated.");
                    // displayEndOfSimulation("Quality exhausted .");
                    // return 1;
                    // }
                    grid[i][j].setQuality(newQuality);
                    System.out.println("Food source at (" + i + ", " + j + ") quality updated to:" + newQuality);

                    // Check if the food source quality is under 0.2
                    if (newQuality < 0.2) {
                        exhaustedFoodSources[0]++; // Increment the count of exhausted food sources
                        if (exhaustedFoodSources[0] == 3) { // If all food sources are exhausted,
                            // exit simulation
                            System.out.println("All food sources exhausted. Simulation terminated.3");
                            displayEndOfSimulation("All food sources exhausted. Simulation terminated.2");

                            return 1; // End the simulation
                        }
                    }
                }
            }
        }
        return 0;
    }

    public boolean isFoodDepleted() {
        // Check if all food sources have quality less than a certain threshold (e.g.,
        // 0.2)

        int counter = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] != null && grid[i][j].getQuality() >= 0.2) {
                    return false; // Food source found with quality above threshold
                }
                if (grid[i][j] != null && grid[i][j].getQuality() <= 0.2) {
                    gridLabels[i][j].setIcon(flowerIconLowQuality);
                    counter++;

                }
            }
        }

        if (counter == 3) {
            displayEndOfSimulation("food depleated");
            return true;
        }
        return false;

    }

    public void clearSimulation() {
        // Reset the grid
        grid = new FoodSource[width][height];

        // Remove all bees
        bees.clear();

        // Reinitialize the food sources
        initializeFoodSources();
    }

    public void displayEndOfSimulation(String message) {
        // Get the center coordinates of the grid to display the message
        int centerX = width / 2;
        int centerY = height / 2;

        // Update the specified grid label with the end of simulation message
        gridLabels[centerX][centerY].setText(message);
        JOptionPane.showMessageDialog(null, message, "Simulation Results", JOptionPane.INFORMATION_MESSAGE);

        // // Stop the application
        // System.exit(0);
    }

    private boolean otherTerminationConditionsMet() {
        // Additional termination conditions logic can be implemented here
        return false;
    }

    public void performDance(Worker worker) {
        // Display the dance icon photo
        displayDanceIcon();

        // After 30 seconds, hide the dance icon photo and reset the worker's state
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                hideDanceIcon(gridLabels);
                worker.setReturingToHive(false);
            }
        }, 30000); // 30 seconds in milliseconds
    }

    private void displayDanceIcon() {
        // Access the grid from the environment

        int width = this.getWidth();
        int height = this.getHeight();

        // Get the coordinates of the hive from the environment
        int hiveX = width / 2;
        int hiveY = height / 2;

        // Initialize DANCE_ICON
        try {
            String imagePath = "/resources/images/Bee Dance.jpg";
            java.net.URL imageURL = getClass().getResource(imagePath);
            if (imageURL != null) {
                DANCE_ICON = new ImageIcon(
                        new ImageIcon(imageURL).getImage().getScaledInstance(100, 35, Image.SCALE_FAST));
            } else {
                System.err.println("Could not find resource: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }

        // Display the dance icon photo at the hive location on the GUI
        gridLabels[hiveX][hiveY].setIcon(DANCE_ICON);
    }

    private void hideDanceIcon(JLabel[][] gridLabels) {
        // Access the grid from the environment

        int width = this.getWidth();
        int height = this.getHeight();

        // Get the coordinates of the hive from the environment
        int hiveX = width / 2;
        int hiveY = height / 2;

        // Hide the dance icon photo from the hive location on the GUI
        gridLabels[hiveX][hiveY].setIcon(null);
    }

    public double calculateNewQuality(FoodSource foodSource) {
        // Example: Simulate decrease in quality over time
        return foodSource.getQuality() * 0.95;
    }

    public boolean isValidPosition(int posX, int posY) {
        return posX >= 0 && posX < width && posY >= 0 && posY < height;
    }

    public Environment getInstance() {
        return this;
    }

    private void initializeFoodSources() {
        // Place default food sources in the environment
        placeDefaultFoodSources();
    }

    private void placeDefaultFoodSources() {
        // Ensure that exactly three food sources are placed
        int placedCount = 0;

        // Attempt to place food sources until exactly three are successfully placed
        while (placedCount < 3) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            // Check if the position is valid and not already occupied by a food source
            if (isValidPosition(x, y) && grid[x][y] == null) {
                // Place the food source with the specified qualities
                if (placedCount == 0) {
                    placeFoodSource(x, y, 0.4);
                } else if (placedCount == 1) {
                    placeFoodSource(x, y, 0.6);
                } else if (placedCount == 2) {
                    placeFoodSource(x, y, 0.8);
                }
                placedCount++; // Increment the count of successfully placed food sources
            }
        }
    }

    public void setMaximumTrialCount(int maxAttempts) {
        MAX_ATTEMPTS = maxAttempts; // Update MAX_ATTEMPTS with the new value
    }

    public FoodSource[][] getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setGridLabels(JLabel[][] gridLabels) {
        this.gridLabels = gridLabels;
    }

    public JLabel[][] getGridLabels() {
        return (this.gridLabels);
    }

    public List<Bee> getBees() {
        return bees;
    }

    public List<FoodSource> getNeighboringFoodSources(Bee bee) {
        // This method should return a list of FoodSource objects that are in the
        // neighborhood of the given bee.
        // The definition of "neighborhood" depends on your specific project
        // requirements.
        // Here is a simple implementation that considers all food sources in the
        // grid
        // as neighbors.

        List<FoodSource> neighboringSources = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] != null) {
                    neighboringSources.add(grid[i][j]);
                }
            }
        }
        return neighboringSources;
    }

    public List<FoodSource> getFoodSources() {
        // This method should return a list of all food sources in the environment.
        // Here is a simple implementation that returns all food sources in the grid.

        List<FoodSource> foodSources = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] != null) {
                    foodSources.add(grid[i][j]);
                }
            }
        }
        return foodSources;
    }

    public List<Worker> getWorkers() {
        // This method should return a list of all worker bees in the environment.
        // Here is a simple implementation that filters all bees in the bees list and
        // returns only the workers.

        List<Worker> workers = new ArrayList<>();
        for (Bee bee : bees) {
            if (bee instanceof Worker) {
                workers.add((Worker) bee);
            }
        }
        return workers;
    }

    public int getWorkerCount() {
        int count = 0;
        for (Bee bee : bees) {
            if (bee instanceof Worker) {
                count++;
            }
        }
        return count;
    }

    public int getScoutCount() {
        int count = 0;
        for (Bee bee : bees) {
            if (bee instanceof Scout) {
                count++;
            }
        }
        return count;
    }

    public int getObserverCount() {
        int count = 0;
        for (Bee bee : bees) {
            if (bee instanceof Observer) {
                count++;
            }
        }
        return count;
    }

    public void removeWorker() {
        // Find and remove a worker bee from the list of bees
        for (int i = 0; i < bees.size(); i++) {
            Bee bee = bees.get(i);
            if (bee instanceof Worker) {
                bees.remove(i);
                return; // Exit the loop after removing the worker bee
            }
        }
    }

    public void removeScout() {
        // Find and remove a scout bee from the list of bees
        for (int i = 0; i < bees.size(); i++) {
            Bee bee = bees.get(i);
            if (bee instanceof Scout) {
                bees.remove(i);
                return; // Exit the loop after removing the scout bee
            }
        }
    }

    public void removeObserver() {
        // Find and remove an observer bee from the list of bees
        for (int i = 0; i < bees.size(); i++) {
            Bee bee = bees.get(i);
            if (bee instanceof Observer) {
                bees.remove(i);
                return; // Exit the loop after removing the observer bee
            }
        }
    }

    public void setWorkerCount(int count) {
        // Clear existing workers if the count is less than the current count
        if (count < getWorkerCount()) {
            int difference = getWorkerCount() - count;
            for (int i = 0; i < difference; i++) {
                removeWorker();
            }
        }
        // Add new workers if the count is greater than the current count
        else if (count > getWorkerCount()) {
            int difference = count - getWorkerCount();
            for (int i = 0; i < difference; i++) {
                addBee("Worker");
            }
        }
    }

    public void setScoutCount(int count) {
        // Clear existing scouts if the count is less than the current count
        if (count < getScoutCount()) {
            int difference = getScoutCount() - count;
            for (int i = 0; i < difference; i++) {
                removeScout();
            }
        }
        // Add new scouts if the count is greater than the current count
        else if (count > getScoutCount()) {
            int difference = count - getScoutCount();
            for (int i = 0; i < difference; i++) {
                addBee("Scout");
            }
        }
    }

    public void setObserverCount(int count) {
        // Clear existing observers if the count is less than the current count
        if (count < getObserverCount()) {
            int difference = getObserverCount() - count;
            for (int i = 0; i < difference; i++) {
                removeObserver();
            }
        }
        // Add new observers if the count is greater than the current count
        else if (count > getObserverCount()) {
            int difference = count - getObserverCount();
            for (int i = 0; i < difference; i++) {
                addBee("Observer");
            }
        }
    }

    public static List<double[]> generateRandomCoordinates(int numberOfCities, int width, int height) {
        List<double[]> coordinatesList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfCities; i++) {
            double x = random.nextDouble() * width;
            double y = random.nextDouble() * height;
            double[] coordinates = { x, y };
            coordinatesList.add(coordinates);
        }

        return coordinatesList;
    }

    public double getLowQualityThreshold() {
        return lowQualityThreshold;
    }

    public double getMediumQualityThreshold() {
        return mediumQualityThreshold;
    }

    public void reactivateScout() {
        List<FoodSource> sourcesToRemove = new ArrayList<>();

        // Check each food source for maximum attempt count
        for (FoodSource source : getFoodSources()) {
            if (source.getAttemptCount() >= MAX_ATTEMPTS) {
                // Remove the source from the environment
                sourcesToRemove.add(source);
            }
        }

        // Remove the sources and reactivate scouts
        for (FoodSource source : sourcesToRemove) {
            grid[source.getPositionX()][source.getPositionY()] = null; // Remove the
            // source from the grid
        }

        // Reactivate scouts to find new sources
        setScoutCount(getScoutCount() + 1);
    }

    public void runABCAlgorithm(int maxIterations) {
        int iteration = 0;
        Tour globalBestTour = null;
        double globalBestDistance = Double.POSITIVE_INFINITY;
        List<double[]> coordinatesList = generateRandomCoordinates(numberOfCities, width, height);

        while (iteration < maxIterations) {
            // Employed Bees Phase
            for (Bee bee : bees) {
                bee.populateCityCoordinates(coordinatesList); // Provide the coordinates list
                // Generate random tour for scout
                Tour tour = bee.generateRandomTour();
                bee.setTour(tour);
                // Apply local search (2-opt) to improve tour
                if (bee.getTour() != null) {
                    localSearch(bee);
                }
            }

            // Onlooker Bees Phase
            for (Bee bee : bees) {
                // Onlooker bee selects a tour
                Tour tour = bee.getTour();
                // Apply local search (2-opt) to improve tour
                if (tour != null) {
                    localSearch(bee);
                }
            }

            // Global Update
            for (Bee bee : bees) {
                Tour tour = bee.getTour();
                if (tour != null) {
                    double distance = bee.calculateTourDistance(tour);
                    if (distance < globalBestDistance) {
                        globalBestTour = tour;
                        globalBestDistance = distance;
                    }
                }
            }

            // Termination condition: You can use a convergence criterion or a maximum
            // number of iterations
            iteration++;
        }

        displayBestTour(globalBestTour);

        // After the algorithm finishes, globalBestTour contains the best tour found
        // Perform any necessary actions with the best tour (e.g., display it)
    }

    public void displayBestTour(Tour bestTour) {

        String BestTour = bestTour.toString();

        System.out.println("displayBestTour : " + BestTour);
        JOptionPane.showMessageDialog(null, bestTour.toString(), "Best Tour:", JOptionPane.INFORMATION_MESSAGE);

        // // Stop the application
        // System.exit(0);
    }

    public void localSearch(Bee bee) {
        Tour tour = bee.getTour();
        for (int i = 0; i < tour.size() - 1; i++) {
            for (int j = i + 1; j < tour.size(); j++) {
                // Apply 2-opt swap: reverse the sub-tour between cities i and j
                Tour newTour = bee.twoOptSwap(tour, i, j);
                double newDistance = bee.calculateTourDistance(newTour);
                double currentDistance = bee.calculateTourDistance(tour);
                if (newDistance < currentDistance) {
                    // If the new tour is better, update the current tour
                    tour = newTour;
                    bee.setTour(newTour);
                }
            }
        }
    }

}
