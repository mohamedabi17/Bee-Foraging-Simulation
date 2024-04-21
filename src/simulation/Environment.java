package simulation;

import entities.Bee;
import entities.Observer;
import entities.Hive;
import entities.Scout;
import entities.Worker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {
    private FoodSource[][] grid;
    private List<Bee> bees;
    private int width;
    private int height;
    private Random random;
    private Hive hive;
    private double lowQualityThreshold;
    private double mediumQualityThreshold;
    private double highQualityThreshold;
    private static final double BEST_SOURCE_QUALITY_THRESHOLD = 0.9;
    private static int MAX_ATTEMPTS = 10;

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new FoodSource[width][height];
        this.bees = new ArrayList<>();
        this.random = new Random();

        lowQualityThreshold = 0.4;
        mediumQualityThreshold = 0.6;
        highQualityThreshold = 0.8;

        // Initialize the environment with food sources and add hive
        initializeFoodSources();
        addHive();
    }

    public void placeFoodSource(int posX, int posY, double quality) {
        if (isValidPosition(posX, posY)) {
            grid[posX][posY] = new FoodSource(posX, posY, quality);
        } else {
            System.out.println("Invalid position for food source");
        }
    }

    public void addBee(Bee bee) {
        bees.add(bee);
    }

    public void addHive() {
        hive = new Hive(width / 2, height / 2); // Place hive at the center of the environment
    }

    public void simulate() {
        boolean bestSourceFound = false;

        // Move bees randomly
        for (Bee bee : bees) {
            int newX = random.nextInt(width);
            int newY = random.nextInt(height);
            bee.move(newX, newY);
        }

        // Update food source qualities
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (grid[i][j] != null) {
                    // Update food source quality based on some criteria
                    double newQuality = calculateNewQuality(grid[i][j]);
                    grid[i][j].setQuality(newQuality);
                }
            }
        }

        // Perform actions based on bee types
        for (Bee bee : bees) {
            if (bee instanceof Worker) {
                ((Worker) bee).exploreAndChooseFoodSource(this);
            } else if (bee instanceof Scout) {
                ((Scout) bee).chooseRandomFoodSource(this);
            } else if (bee instanceof Observer) {
                ((Observer) bee).observeAndChooseFoodSource(this);
            }

            // Check if the best source is found
            if (bee.getCurrentFoodSource().getQuality() >= BEST_SOURCE_QUALITY_THRESHOLD) {
                bestSourceFound = true;
                break;
            }
        }

        // Terminate simulation if the best source is found or other termination
        // conditions are met
        if (bestSourceFound || otherTerminationConditionsMet()) {
            // Perform necessary termination actions
            System.out.println("Simulation terminated.");
            // Additional termination actions can be added here
        }
    }

    private boolean otherTerminationConditionsMet() {
        // Additional termination conditions logic can be implemented here
        return false;
    }

    private double calculateNewQuality(FoodSource foodSource) {
        // Example: Simulate decrease in quality over time
        return foodSource.getQuality() * 0.9;
    }

    private boolean isValidPosition(int posX, int posY) {
        return posX >= 0 && posX < width && posY >= 0 && posY < height;
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
                addBee(new Worker());
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
                addBee(new Scout());
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
                addBee(new Observer());
            }
        }
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

}
