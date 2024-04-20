import entities.*;
import simulation.*;
import simulation.Environment;
import java.util.ArrayList;
import java.util.List;
import simulation.FoodSource;

public class BeeForagingSimulation {
    public static void main(String[] args) {
        // Create food sources
        List<FoodSource> foodSources = new ArrayList<>();
        foodSources.add(new FoodSource(3, 4, 0.8));
        foodSources.add(new FoodSource(7, 8, 0.6));

        // Create hive and add bees
        Hive hive = new Hive(0, 0);
        hive.addBee(new Worker());
        hive.addBee(new Scout());
        hive.addBee(new Observer());

        // Set food sources for the hive
        hive.setFoodSources(foodSources);

        // Simulate bee behaviors
        for (Bee bee : hive.getBees()) {
            bee.exploreFoodSource();
            if (bee instanceof Worker) {
                Worker worker = (Worker) bee;
                worker.replaceFoodSource(foodSources.get(0));
            } else if (bee instanceof Observer) {
                Observer observer = (Observer) bee;
                observer.chooseFoodSource(foodSources);
            }
        }
    }
}
