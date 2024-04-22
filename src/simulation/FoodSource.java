package simulation;

import javax.swing.ImageIcon;

public class FoodSource {
    private int positionX;
    private int positionY;
    private double quality;
    private boolean isHive; // Flag to indicate if it's a hive or not
    ImageIcon Found;

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

    public FoodSource(ImageIcon icon) {
        this.Found = icon;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
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

    public boolean isHive() {
        return isHive;
    }

    public void setHive(boolean isHive) {
        this.isHive = isHive;
    }

    public void incrementAttemptCounter() {
        this.attemptCounter++;
        if (this.attemptCounter >= MAX_ATTEMPTS) {
            // Remove this food source from the environment
            // You'll need to add a method in your Environment class to do this
        }
    }

    public int getAttemptCount() {
        return this.attemptCounter;
    }

    public double evaluateQuality() {
        return this.quality;
    }
}
