package entities;

// Import the FoodSource class if it's in a different package
import simulation.FoodSource;
import java.util.ArrayList;
import java.util.List;

public class Bee {
    protected String type;
    protected int positionX;
    protected int positionY;
    protected String behavior;
    protected FoodSource assignedFoodSource;

    public Bee(int posX, int posY, String type) {
        this.positionX = posX;
        this.positionY = posY;
        this.type = type;
    }

    public Bee(int posX, int posY) {
        this.positionX = posX;
        this.positionY = posY;
    }

    // Constructor with type parameter
    public Bee(String type) {
        this.type = type;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void move(int newX, int newY) {
        this.positionX = newX;
        this.positionY = newY;
    }

    public void setAssignedFoodSource(FoodSource foodSource) {
        this.assignedFoodSource = foodSource;
    }

    public void exploreFoodSource() {
        // Simulate bee exploring food source
        if (assignedFoodSource != null) {
            double quality = assignedFoodSource.evaluateQuality();
            // Assume evaluating the quality of the food source
            System.out.println("Bee of type " + type + " explored food source with quality: " + quality);
        }
    }
}