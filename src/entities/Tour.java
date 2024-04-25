package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tour {
    private List<Integer> cityIndices;

    public Tour(List<Integer> cities) {
        this.cityIndices = new ArrayList<>(cities);
    }

    public List<Integer> getCities() {
        return cityIndices;
    }

    public void setCities(List<Integer> cities) {
        this.cityIndices = cities;
    }

    public double calculateDistance(double[][] distances) {
        double distance = 0.0;
        for (int i = 0; i < cityIndices.size() - 1; i++) {
            int city1 = cityIndices.get(i);
            int city2 = cityIndices.get(i + 1);
            distance += distances[city1][city2];
        }
        // Add distance from last city back to the starting city
        int lastCity = cityIndices.get(cityIndices.size() - 1);
        int firstCity = cityIndices.get(0);
        distance += distances[lastCity][firstCity];
        return distance;
    }

    public void shuffle() {
        Collections.shuffle(cityIndices.subList(1, cityIndices.size())); // Exclude starting city from shuffling
    }

    public void swap(int i, int j) {
        Collections.swap(cityIndices, i, j);
    }

    @Override
    public String toString() {
        return "Tour: " + cityIndices.toString();
    }

    public List<Integer> getCityIndices() {
        return cityIndices;
    }

    public int size() {
        return cityIndices.size();
    }
}
