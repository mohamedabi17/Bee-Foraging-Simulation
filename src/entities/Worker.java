package entities;

import java.util.ArrayList;
import java.util.List;
import simulation.FoodSource;
import simulation.Environment;

public class Worker extends Bee {
    public Worker() {
        super("Worker");
    }

    public Worker(FoodSource currentFoodSource) {
        super("Worker");
        this.setCurrentFoodSource(currentFoodSource);
    }

    public void replaceFoodSource(FoodSource newFoodSource) {
        // Simulate worker replacing food source
        if (assignedFoodSource != null) {
            assignedFoodSource = newFoodSource;
            System.out.println("Worker replaced food source");
        }
    }

    public void exploreAndChooseFoodSource(Environment environment) {
        // Explore current food source
        FoodSource currentSource = this.getCurrentFoodSource();
        double currentQuality = 0.0;

        if (currentSource != null) {
            currentQuality = currentSource.getQuality();
        } else {
            // If the worker doesn't have a current food source, assign a new one
            List<FoodSource> allSources = environment.getFoodSources();
            if (!allSources.isEmpty()) {
                this.setCurrentFoodSource(allSources.get(0));
                currentSource = this.getCurrentFoodSource();
                currentQuality = currentSource.getQuality();
            }
        }

        // Explore neighboring food sources
        List<FoodSource> neighboringSources = environment.getNeighboringFoodSources(this);
        for (FoodSource source : neighboringSources) {
            double quality = source.getQuality();
            if (quality > currentQuality) {
                // If a better source is found, replace current source
                this.setCurrentFoodSource(source);
                currentQuality = quality;
            }
        }
    }

    public void evaluateNeighboringSources(Environment environment) {
        // Get the neighboring food sources
        List<FoodSource> neighboringSources = environment.getNeighboringFoodSources(this);

        // Evaluate each neighboring source
        for (FoodSource source : neighboringSources) {
            double quality = source.getQuality();

            // If the quality of the neighboring source is better than the current source,
            // replace it
            if (quality > this.getCurrentFoodSource().getQuality()) {
                this.replaceFoodSource(source);
            }
        }
    }

}