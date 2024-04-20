package simulation;

import entities.Bee;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Environment {
    private FoodSource[][] grid;
    private List<Bee> bees;
    private int width;
    private int height;
    private Random random;

    public Environment(int width, int height, int numBees) {
        this.width = width;
        this.height = height;
        this.grid = new FoodSource[width][height];
        this.bees = new ArrayList<>();
        this.random = new Random();

        // Add bees to the environment
        for (int i = 0; i < numBees; i++) {
            addBee(new Bee(random.nextInt(width), random.nextInt(height)));
        }
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
}
