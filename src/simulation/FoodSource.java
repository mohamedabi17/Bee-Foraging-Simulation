package simulation;

import entities.Bee;
import java.util.List;

public class FoodSource {
    protected int positionX;
    protected int positionY;
    protected double quality;
    private boolean isHive; // Flag to indicate if it's a hive or not

    private int attemptCounter = 0;
    private static final int MAX_ATTEMPTS = 10; // Set this to your desired threshold

    public FoodSource(int posX, int posY, double quality) {
        this.positionX = posX;
        this.positionY = posY;
        this.quality = quality;
    }

    // Constructor
    public FoodSource(double quality, boolean isHive) {
        this.quality = quality;
        this.isHive = isHive;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public double evaluateQuality() {
        // Evaluate the quality of the food source based on criteria
        return quality;
    }

    public boolean isHive() {
        return isHive;
    }

    public void setHive(boolean isHive) {
        this.isHive = isHive;
    }

    public void incrementAttemptCounter() {
        this.attemptCounter++;
        if (this.attemptCounter >= MAX_ATTEMPTS) {
            // remove this food source from environment
            // You'll need to add a method in your Environment class to do this
        }
    }
}
