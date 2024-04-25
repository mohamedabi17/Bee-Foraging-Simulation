package entities;

// Import the FoodSource class if it's in a different package
import simulation.FoodSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bee {
    protected String type;
    protected int positionX;
    protected int positionY;
    protected String behavior;
    protected FoodSource assignedFoodSource;
    private FoodSource betterFoodSource; // Track the better food source for the bee
    private Tour tour;
    private List<double[]> cityCoordinates = new ArrayList<>();
    private int numberOfCities = 5; // Define this variable

    public Bee(int posX, int posY, String type) {
        this.positionX = posX;
        this.positionY = posY;
        this.type = type;
        this.currentFoodSource = null;
    }

    protected FoodSource currentFoodSource;

    public FoodSource getCurrentFoodSource() {
        return this.currentFoodSource;
    }

    public void setCurrentFoodSource(FoodSource foodSource) {
        this.currentFoodSource = foodSource;
    }

    public Bee(int posX, int posY) {
        this.positionX = posX;
        this.positionY = posY;
    }

    // Constructor with type parameter
    public Bee(String type) {
        this.type = type;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String getType() {
        return type;
    }

    public void setBetterFoodSource(FoodSource source) {
        betterFoodSource = source;
    }

    public FoodSource getBetterFoodSource() {
        return betterFoodSource;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void move(int newX, int newY) {
        this.positionX = newX;
        this.positionY = newY;
    }

    public void setAssignedFoodSource(FoodSource foodSource) {
        this.assignedFoodSource = foodSource;
    }

    public void exploreFoodSource() {
        // Simulate bee exploring food source
        if (assignedFoodSource != null) {
            double quality = assignedFoodSource.evaluateQuality();
            // Assume evaluating the quality of the food source
            System.out.println("Bee of type " + type + " explored food source with quality: " + quality);
        }
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Tour generateRandomTour() {
        List<Integer> cityIndices = new ArrayList<>();
        for (int i = 0; i < numberOfCities; i++) {
            cityIndices.add(i);
        }
        Collections.shuffle(cityIndices); // Randomly shuffle city indices
        return new Tour(cityIndices); // Create a tour from the shuffled city indices
    }

    public void localSearch(Bee bee) {
        Tour tour = bee.getTour();

        if (tour != null && tour.size() > 0) { // Check if the tour is not null and not empty
            for (int i = 0; i < tour.size() - 1; i++) {
                for (int j = i + 1; j < tour.size(); j++) {
                    // Apply 2-opt swap: reverse the sub-tour between cities i and j
                    Tour newTour = twoOptSwap(tour, i, j);
                    double newDistance = calculateTourDistance(newTour);
                    double currentDistance = calculateTourDistance(tour);
                    if (newDistance < currentDistance) {
                        // If the new tour is better, update the current tour
                        tour = newTour;
                        bee.setTour(newTour);
                    }
                }
            }
        } else {
            System.out.println("Tour is null or empty. Cannot perform local search.");
        }
    }

    public Tour twoOptSwap(Tour tour, int i, int j) {
        // Create a new tour by reversing the sub-tour between cities i and j
        List<Integer> newTour = new ArrayList<>(tour.getCityIndices());
        for (int k = i, l = j; k < l; k++, l--) {
            int temp = newTour.get(k);
            newTour.set(k, newTour.get(l));
            newTour.set(l, temp);
        }
        return new Tour(newTour);
    }

    public double calculateTourDistance(Tour tour) {
        List<Integer> cityIndices = tour.getCityIndices();
        double totalDistance = 0;
        for (int i = 0; i < cityIndices.size() - 1; i++) {
            int cityIndex1 = cityIndices.get(i);
            int cityIndex2 = cityIndices.get(i + 1);
            double distance = calculateDistanceBetweenCities(cityIndex1, cityIndex2);
            totalDistance += distance;
        }
        // Add the distance from the last city back to the starting city
        int lastCityIndex = cityIndices.get(cityIndices.size() - 1);
        int startingCityIndex = cityIndices.get(0);
        double distanceToStartingCity = calculateDistanceBetweenCities(lastCityIndex, startingCityIndex);
        totalDistance += distanceToStartingCity;
        return totalDistance;
    }

    private double calculateDistanceBetweenCities(int cityIndex1, int cityIndex2) {
        // Get the coordinates of the two cities
        double[] city1 = cityCoordinates.get(cityIndex1);
        double[] city2 = cityCoordinates.get(cityIndex2);

        // Calculate the Euclidean distance between the two cities
        double distance = Math.sqrt(Math.pow(city2[0] - city1[0], 2) + Math.pow(city2[1] - city1[1], 2));

        return distance;
    }

    public void populateCityCoordinates(List<double[]> coordinatesList) {
        // Clear the existing list to avoid duplicates
        cityCoordinates.clear();

        // Add the coordinates of each city to the cityCoordinates list
        for (double[] coordinates : coordinatesList) {
            cityCoordinates.add(coordinates);
        }
    }

}