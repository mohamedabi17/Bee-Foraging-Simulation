package entities;

import java.util.ArrayList;
import java.util.List;
import simulation.FoodSource;
import java.util.Random;

public class Observer extends Bee {
    public Observer() {
        super("Observer"); // Call the superclass constructor with the type "Observer"
    }

    public void chooseFoodSource(List<FoodSource> foodSources) {
        // Simulate observer choosing food source
        if (!foodSources.isEmpty()) {
            Random random = new Random();
            FoodSource chosenFoodSource = foodSources.get(random.nextInt(foodSources.size()));
            assignedFoodSource = chosenFoodSource;
            System.out.println("Observer chose food source");
        }
    }
}