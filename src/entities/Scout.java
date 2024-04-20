package entities;

import java.util.ArrayList;
import java.util.List;
import simulation.Environment;
import simulation.FoodSource;
import java.util.Random;

public class Scout extends Bee {
    public Scout() {
        super("Scout");
    }

    public void chooseRandomFoodSource(Environment environment) {
        // Choose a random food source from the environment
        List<FoodSource> foodSources = environment.getFoodSources();
        int randomIndex = new Random().nextInt(foodSources.size());
        FoodSource chosenSource = foodSources.get(randomIndex);

        // Set the chosen source as the current source for this scout
        this.setCurrentFoodSource(chosenSource);
    }

}