package simulation;

import entities.Bee;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import entities.Worker;
import entities.Observer;
import entities.Scout;

public class Environment {
    private FoodSource[][] grid;
    private List<Bee> bees;
    private int width;
    private int height;
    private Random random;

    private int attemptCounter = 0;
    private static final int MAX_ATTEMPTS = 10; // Set this to your desired threshold

    public Environment(int width, int height, int numBees) {
        this.width = width;
        this.height = height;
        this.grid = new FoodSource[width][height];
        this.bees = new ArrayList<>();
        this.random = new Random();

        // Place food sources in the environment
        placeFoodSource(2, 3, 0.8);
        placeFoodSource(7, 8, 0.6);

        // Add bees to the environment
        for (int i = 0; i < numBees; i++) {
            FoodSource foodSource = getFoodSources().get(random.nextInt(getFoodSources().size()));
            addBee(foodSource);
        }
    }

    public void placeFoodSource(int posX, int posY, double quality) {
        if (isValidPosition(posX, posY)) {
            grid[posX][posY] = new FoodSource(posX, posY, quality);
        } else {
            System.out.println("Invalid position for food source");
        }
    }

    public void addBee(FoodSource foodSource) {
        Worker worker = new Worker();
        worker.setCurrentFoodSource(foodSource);
        bees.add(worker);
    }

    public void addBee(Bee bee) {
        bees.add(bee);
    }

    public void simulate() {
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

        for (Bee bee : bees) {
            if (bee instanceof Worker) {
                ((Worker) bee).exploreAndChooseFoodSource(this);
            } else if (bee instanceof Scout) {
                ((Scout) bee).chooseRandomFoodSource(this);
            } else if (bee instanceof Observer) {
                ((Observer) bee).observeAndChooseFoodSource(this);
            }
        }
    }

    private double calculateNewQuality(FoodSource foodSource) {
        // Example: Simulate decrease in quality over time
        return foodSource.getQuality() * 0.9;
    }

    private boolean isValidPosition(int posX, int posY) {
        return posX >= 0 && posX < width && posY >= 0 && posY < height;
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
        // Here is a simple implementation that considers all food sources in the grid
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

}
