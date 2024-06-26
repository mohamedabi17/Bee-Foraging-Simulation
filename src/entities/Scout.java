package entities;

import java.util.ArrayList;
import java.util.List;
import simulation.Environment;
import simulation.FoodSource;
import java.util.Random;
import simulation.Environment;
import java.util.Random;

public class Scout extends Bee {

    private Random random;

    private int explorationCounter; // Counter to track the number of explored sources
    private int maxExplorationAttempts; // Maximum exploration attempts before reactivation

    public Scout() {
        super("Scout");
        random = new Random();

        this.explorationCounter = 0;
        this.maxExplorationAttempts = 10; // Adjust this value as needed
    }

    @Override
    public String toString() {
        return "Scout Bee";
    }

    public Scout(int posX, int posY) {
        super(posX, posY, "Scout");
        random = new Random();
        this.explorationCounter = 0;
        this.maxExplorationAttempts = 10; // Adjust this value as needed
    }

    public void chooseRandomFoodSource(Environment environment) {
        // Keep moving randomly until a food source is found
        while (currentFoodSource == null) {
            int newX = random.nextInt(environment.getWidth());
            int newY = random.nextInt(environment.getHeight());
            move(newX, newY);

            // Check if a food source exists at the new position
            FoodSource newFoodSource = environment.getGrid()[newX][newY];
            if (newFoodSource != null) {
                currentFoodSource = newFoodSource;
                break;
            }
        }
    }

    public void exploreSource(Environment environment, FoodSource source) {
        // Increment the exploration counter
        this.explorationCounter++;

        // Check if the exploration counter has reached the maximum threshold
        if (this.explorationCounter >= this.maxExplorationAttempts) {
            environment.reactivateScout();

        }
    }

    public void communicateSourceInformation(FoodSource source) {
        System.out.println("Scout Found New Food Source: " + source);
    }

    public void replaceFoodSource(FoodSource newFoodSource) {
        // Simulate Scout replacing food source
        if (assignedFoodSource != null) {
            assignedFoodSource = newFoodSource;
            System.out.println("Scout replaced food source");
            communicateSourceInformation(newFoodSource);
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

}
