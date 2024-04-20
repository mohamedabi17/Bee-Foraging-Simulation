import entities.*;
import simulation.*;
import simulation.Environment;
import java.util.ArrayList;
import java.util.List;
import simulation.FoodSource;

public class BeeForagingSimulation {
    public static void main(String[] args) {
        Hive hive = new Hive(0, 0);
        hive.addBee(new Worker());
        hive.addBee(new Scout());
        hive.addBee(new Observer());

        Environment environment = new Environment(10, 10, 5);
        environment.placeFoodSource(3, 4, 0.8);
        environment.placeFoodSource(7, 8, 0.6);

        // Simulate bee behaviors
        List<FoodSource> foodSources = hive.getFoodSources();
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
