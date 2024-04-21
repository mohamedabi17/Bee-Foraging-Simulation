package entities;

import simulation.FoodSource;
import simulation.Environment;

import java.util.List;

public class Worker extends Bee {

    public Worker() {
        super("Worker");
    }

    public Worker(FoodSource currentFoodSource) {
        super("Worker");
        this.setCurrentFoodSource(currentFoodSource);
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

    public void replaceFoodSource(FoodSource newFoodSource) {
        // Simulate worker replacing food source
        if (assignedFoodSource != null) {
            assignedFoodSource = newFoodSource;
            System.out.println("Worker replaced food source");
            communicateSourceInformation(newFoodSource);
        }
    }

    public void exploreAndChooseFoodSource(Environment environment) {
        // Explore current food source
        FoodSource currentSource = this.getCurrentFoodSource();
        evaluateSourceQuality(currentSource);

        // Explore neighboring food sources
        exploreNeighborhood(environment);
    }
}
