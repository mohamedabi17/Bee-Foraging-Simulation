package entities;

import java.util.ArrayList;
import java.util.List;
import simulation.Environment;
import simulation.FoodSource;
import java.util.Random;
import simulation.Environment;

public class Scout extends Bee {
    private int explorationCounter; // Counter to track the number of explored sources
    private int maxExplorationAttempts; // Maximum exploration attempts before reactivation

    public Scout() {
        super("Scout");
        this.explorationCounter = 0;
        this.maxExplorationAttempts = 10; // Adjust this value as needed
    }

    public void chooseRandomFoodSource(Environment environment) {
        // Choose a random food source from the environment
        List<FoodSource> foodSources = environment.getFoodSources();
        int randomIndex = new Random().nextInt(foodSources.size());
        FoodSource chosenSource = foodSources.get(randomIndex);

        // Set the chosen source as the current source for this scout
        this.setCurrentFoodSource(chosenSource);
    }

    public void exploreSource(Environment environment, FoodSource source) {
        // Increment the exploration counter
        this.explorationCounter++;

        // Check if the exploration counter has reached the maximum threshold
        if (this.explorationCounter >= this.maxExplorationAttempts) {
            environment.reactivateScout();

        }
    }

}
