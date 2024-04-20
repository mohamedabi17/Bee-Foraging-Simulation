package simulation;

import entities.Bee;
import entities.FoodSource;
import java.util.List;

public class Environment {
    private FoodSource[][] grid;
    private List<Bee> bees;
    private int width;
    private int height;

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new FoodSource[width][height];
        this.bees = new ArrayList<>();
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
        // Implement simulation logic here
        // For example, move bees, update food sources, etc.
        // This will depend on your specific simulation requirements
    }

    private boolean isValidPosition(int posX, int posY) {
        return posX >= 0 && posX < width && posY >= 0 && posY < height;
    }

    public FoodSource[][] getGrid() {
        return grid;
    }

    public List<Bee> getBees() {
        return bees;
    }
}


