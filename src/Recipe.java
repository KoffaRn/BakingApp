import java.util.ArrayList;

public class Recipe {
    int id;
    String name;
    ArrayList<RecipeStep> steps;
    ArrayList<RecipeIngredient> ingredients;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(this.id==0)this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<RecipeStep> getSteps() {
        return steps;
    }
    public void setSteps(ArrayList<RecipeStep> steps) {
        this.steps = steps;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Recipe(int id, ArrayList<RecipeStep> steps, ArrayList<RecipeIngredient> ingredients, String name) {
        this.id = id;
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
    }
}
