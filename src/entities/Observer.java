package entities;

import java.util.ArrayList;
import java.util.List;
import simulation.FoodSource;
import java.util.Random;
import simulation.Environment;

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

    // In Observer class
    public void observeAndChooseFoodSource(Environment environment) {
        // Observe worker dances
        List<Worker> workers = environment.getWorkers();
        FoodSource bestSource = null;
        double bestQuality = -1;
        for (Worker worker : workers) {
            FoodSource source = worker.getCurrentFoodSource();
            double quality = source.getQuality();
            // Add some error to the perceived quality
            double perceivedQuality = quality * (1 + (new Random().nextDouble() - 0.5) / 10);
            if (perceivedQuality > bestQuality) {
                bestSource = source;
                bestQuality = perceivedQuality;
            }
        }

        // Choose the best source observed
        this.setCurrentFoodSource(bestSource);

        // No need to call observeAndChooseFoodSource again here
    }

}