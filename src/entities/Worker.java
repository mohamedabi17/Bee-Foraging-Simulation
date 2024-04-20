package entities;

import java.util.ArrayList;
import java.util.List;
import simulation.FoodSource;

public class Worker extends Bee {
    public Worker() {
        super("Worker");
    }

    public void replaceFoodSource(FoodSource newFoodSource) {
        // Simulate worker replacing food source
        if (assignedFoodSource != null) {
            assignedFoodSource = newFoodSource;
            System.out.println("Worker replaced food source");
        }
    }
}