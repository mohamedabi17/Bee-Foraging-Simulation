package entities;

import simulation.FoodSource;
import simulation.Environment;
import java.util.Random;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Worker extends Bee {

    private Random random;
    public boolean isReturningToHive = false; // Track if the worker is returning to the hive

    public Worker(int posX, int posY) {
        super(posX, posY, "Worker");
    }

    @Override
    public String toString() {
        return "Worker Bee";
    }

    public Worker(FoodSource currentFoodSource) {
        super("Worker");
        this.setCurrentFoodSource(currentFoodSource);
    }

    public void exploreAndChooseFoodSource(Environment environment) {
        // Your implementation here...
    }

    private double evaluateQuality(Environment environment, FoodSource foodSource) {
        // Your implementation here...
        return 0.0; // Placeholder return value
    }

    public void returnToHive(int hiveX, int hiveY) {
        isReturningToHive = true;
        moveTo(hiveX, hiveY); // Move to the center of the grid where the hive is located
    }

    public void moveTo(int x, int y) {
        setPositionX(x);
        setPositionY(y);
    }

    public void evaluateSourceQuality(FoodSource source) {
        // Evaluate the quality of the assigned source
        if (this.getCurrentFoodSource() != null) {
            double currentQuality = this.getCurrentFoodSource().getQuality();
            double newQuality = source.getQuality();
            if (newQuality > currentQuality) {
                this.replaceFoodSource(source);
            }
        }
    }

    public void exploreNeighborhood(Environment environment) {
        // Explore the neighborhood for better sources
        List<FoodSource> neighboringSources = environment.getNeighboringFoodSources(this);
        for (FoodSource source : neighboringSources) {
            replaceSourceIfBetter(this.getCurrentFoodSource(), source);
        }
    }

    public void replaceSourceIfBetter(FoodSource currentSource, FoodSource newSource) {
        // Replace the current source if a better one is found
        if (currentSource != null && newSource != null) {
            double currentQuality = currentSource.getQuality();
            double newQuality = newSource.getQuality();
            if (newQuality > currentQuality) {
                this.replaceFoodSource(newSource);
            }
        }
    }

    public void communicateSourceInformation(FoodSource source) {
        // Perform a dance to communicate information about the source
        System.out.println("Worker performing a dance to communicate information about the source: " + source);
        // You can extend this method to perform additional actions if needed
    }

    public void setReturingToHive(boolean isReturningToHive) {
        this.isReturningToHive = isReturningToHive;
    }

    public boolean getReturingToHive() {
        return (this.isReturningToHive);
    }

    public void replaceFoodSource(FoodSource newFoodSource) {
        // Simulate worker replacing food source
        if (assignedFoodSource != null) {
            assignedFoodSource = newFoodSource;
            System.out.println("Worker replaced food source");
            communicateSourceInformation(newFoodSource);
        }
    }

}
