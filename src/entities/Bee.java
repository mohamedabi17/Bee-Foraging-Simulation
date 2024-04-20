package entities;

import java.util.ArrayList;
import java.util.List;

public class Bee {
    protected String type;
    protected int positionX;
    protected int positionY;
    protected String behavior;
    protected FoodSource assignedFoodSource;

    public Bee(String type) {
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