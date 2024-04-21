package entities;

import java.util.List;
import java.util.Random;
import simulation.Environment;
import simulation.FoodSource;

public class Observer extends Bee {
    private FoodSource assignedFoodSource;

    public Observer() {
        super("Observer");
    }

    public void chooseFoodSource(List<FoodSource> foodSources) {
        if (!foodSources.isEmpty()) {
            Random random = new Random();
            assignedFoodSource = foodSources.get(random.nextInt(foodSources.size()));
            System.out.println("Observer chose food source");
        }
    }

    public void observeAndChooseFoodSource(Environment environment) {
        List<Worker> workers = environment.getWorkers();
        List<FoodSource> sources = environment.getFoodSources();

        // Observe worker dances and estimate source quality
        double[] estimatedQualities = estimateSourceQuality(workers, sources);

        // Choose source probabilistically based on estimated qualities
        chooseSourceProbabilistically(sources, estimatedQualities);
    }

    private double[] estimateSourceQuality(List<Worker> workers, List<FoodSource> sources) {
        double[] estimatedQualities = new double[sources.size()];

        for (int i = 0; i < sources.size(); i++) {
            double sumQuality = 0.0;
            int workerCount = 0;

            for (Worker worker : workers) {
                FoodSource source = worker.getCurrentFoodSource();
                if (source != null && source.equals(sources.get(i))) {
                    sumQuality += source.getQuality();
                    workerCount++;
                }
            }

            estimatedQualities[i] = (workerCount > 0) ? sumQuality / workerCount : 0.0;
        }

        return estimatedQualities;
    }

    private void chooseSourceProbabilistically(List<FoodSource> sources, double[] estimatedQualities) {
        double totalQuality = 0.0;
        for (double quality : estimatedQualities) {
            totalQuality += quality;
        }

        double randomValue = Math.random() * totalQuality;
        double cumulativeProbability = 0.0;

        for (int i = 0; i < sources.size(); i++) {
            cumulativeProbability += estimatedQualities[i];
            if (randomValue <= cumulativeProbability) {
                assignedFoodSource = sources.get(i);
                System.out.println("Observer chose food source");
                break;
            }
        }
    }

    public void exploreNeighborhood(Environment environment) {
        // Get neighboring food sources
        List<FoodSource> neighboringSources = environment.getNeighboringFoodSources(this);

        // Explore each neighboring source to check for better options
        for (FoodSource source : neighboringSources) {
            if (source.getQuality() > assignedFoodSource.getQuality()) {
                assignedFoodSource = source;
                System.out.println("Observer found a better food source in the neighborhood.");
            }
        }
    }

    public void replaceSourceIfBetter(FoodSource newSource) {
        // Replace the current source if the new one is better
        if (newSource.getQuality() > assignedFoodSource.getQuality()) {
            assignedFoodSource = newSource;
            System.out.println("Observer replaced the current food source with a better one.");
        }
    }

    public void communicateSourceInformation(Worker worker) {
        // Perform a dance to communicate information about the source to the worker
        System.out.println("Observer performs a dance to communicate source information to the worker.");
        // You can add more details about the communication process here
    }

}