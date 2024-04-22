package entities;

import java.util.List;
import java.util.Random;
import simulation.Environment;
import simulation.FoodSource;
import java.util.Random;

public class Observer extends Bee {
    private FoodSource assignedFoodSource;
    private Random random = new Random();

    @Override
    public String toString() {
        return "Observer Bee";
    }

    public Observer(int posX, int posY) {
        super(posX, posY, "Scout");
        random = new Random();
    }

    public void chooseFoodSource(List<FoodSource> foodSources) {
        if (!foodSources.isEmpty()) {
            Random random = new Random();
            assignedFoodSource = foodSources.get(random.nextInt(foodSources.size()));
            System.out.println("Observer chose food source");
        }
    }

    public void observeAndChooseFoodSource(Environment environment) {
        double bestQuality = Double.MIN_VALUE;
        FoodSource bestSource = null;

        // Watch the dances of the worker bees
        for (Bee bee : environment.getBees()) {
            if (bee instanceof Worker) {
                FoodSource source = bee.getCurrentFoodSource();
                if (source != null) {
                    // Perceive the estimated quality of each source with a certain degree of error
                    double perceivedQuality = perceiveQuality(source.getQuality());
                    if (perceivedQuality > bestQuality) {
                        bestQuality = perceivedQuality;
                        bestSource = source;
                    }
                }
            }
        }

        // Choose a food source based on the perceived quality
        if (bestSource != null) {
            setCurrentFoodSource(bestSource);
            exploreAndChooseFoodSource(environment); // Behave like a Worker bee
        }
    }

    private double perceiveQuality(double trueQuality) {
        // Implement perception of quality with a certain degree of error
        // For example, you can add some random noise to the true quality
        double error = random.nextDouble() * 0.1; // Adjust the error range as needed
        double perceivedQuality = trueQuality + error;
        return perceivedQuality;
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

    public void exploreAndChooseFoodSource(Environment environment) {
        // Explore the assigned food source
        exploreAssignedFoodSource(environment);

        // Explore the neighborhood for better sources
        exploreNeighborhood(environment);
    }

    private void exploreAssignedFoodSource(Environment environment) {
        // Evaluate the quality of the assigned food source
        double currentQuality = assignedFoodSource.getQuality();

        // Simulate exploring the current food source
        // For example, the observer could stay at the same source or perform some
        // action to assess its quality

        // After exploring, if the perceived quality of the assigned source has changed,
        // update it
        double perceivedQuality = perceiveQuality(currentQuality);
        if (perceivedQuality > currentQuality) {
            assignedFoodSource.setQuality(perceivedQuality);
            System.out.println("Observer perceived a better quality for the assigned food source.");
        }
    }

    private void exploreNeighborhood(Environment environment) {
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

}