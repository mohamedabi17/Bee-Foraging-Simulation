package entities;

import java.util.ArrayList;
import java.util.List;
import simulation.FoodSource;

public class Hive {
    private int positionX;
    private int positionY;
    private List<Bee> bees;
    private List<FoodSource> foodSources;

    public Hive(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.bees = new ArrayList<>();
        this.foodSources = new ArrayList<>();
    }

    public void addBee(Bee bee) {
        bees.add(bee);
    }

    public void addFoodSource(FoodSource foodSource) {
        foodSources.add(foodSource);
    }

    public List<FoodSource> getFoodSources() {
        return foodSources;
    }

    public void setFoodSources(List<FoodSource> foodSources) {
        this.foodSources = foodSources;
    }

    public List<Bee> getBees() {
        return bees;
    }
}
