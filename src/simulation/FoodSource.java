public class FoodSource {
    protected int positionX;
    protected int positionY;
    protected double quality;

    public FoodSource(int posX, int posY, double quality) {
        this.positionX = posX;
        this.positionY = posY;
        this.quality = quality;
    }

    public double evaluateQuality() {
        // Evaluate the quality of the food source based on criteria
        return quality;
    }
}
