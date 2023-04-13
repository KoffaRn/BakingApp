public class RecipeIngredient {
    int ingredientId;
    int nbMeasurement;
    int measurementId;

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getNbMeasurement() {
        return nbMeasurement;
    }

    public void setNbMeasurement(int nbMeasurement) {
        this.nbMeasurement = nbMeasurement;
    }

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }

    public RecipeIngredient(int nbMeasurement, int measurementId, int ingredientId) {
        this.nbMeasurement = nbMeasurement;
        this.measurementId = measurementId;
        this.ingredientId = ingredientId;
    }
}
