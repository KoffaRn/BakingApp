import com.sun.source.tree.StatementTree;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;

public class RecipeHandler {
    Connection conn;
    String dbName;

    public RecipeHandler(String dbName) {
        this.dbName = dbName;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbName + ".db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createTable() {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS ingredient (id INTEGER PRIMARY KEY,name VARCHAR(50),dairy INTEGER DEFAULT 0,gluten INTEGER DEFAULT 0)");
            statement.execute("CREATE TABLE IF NOT EXISTS measurement (id INTEGER PRIMARY KEY, name VARCHAR(50))");
            statement.execute("CREATE TABLE IF NOT EXISTS recipe (id INTEGER PRIMARY KEY, name VARCHAR(50))");
            statement.execute("CREATE TABLE IF NOT EXISTS recipe_ingredient (id INTEGER PRIMARY KEY, measurement_number INTEGER, measurement_id INTEGER, recipe_id INTEGER, ingredient_id INTEGER, FOREIGN KEY(measurement_id) REFERENCES measurement(id), FOREIGN KEY(recipe_id) REFERENCES recipe(id), FOREIGN KEY(ingredient_id) REFERENCES ingredient(id))");
            statement.execute("CREATE TABLE IF NOT EXISTS recipe_step (id INTEGER PRIMARY KEY,recipe_id INTEGER,ord INTEGER,instruction TEXT, timer INTEGER,FOREIGN KEY(recipe_id) REFERENCES recipe(id))");
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean createIngredient(String name, boolean isGluten, boolean isDairy) {
        int diary = 0;
        int gluten = 0;
        if (isDairy == true) diary = 1;
        if (isGluten == true) gluten = 1;
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ingredient (name, dairy, gluten) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setInt(2, diary);
            ps.setInt(3, gluten);
            int r = ps.executeUpdate();
            if (r > 0) return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return false;
    }
    public ArrayList<Recipe> listRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id, name FROM recipe");

            while(rs.next()) {
                recipes.add(getRecipe(rs.getInt(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return recipes;
    }
    /**
     *
     * @param recipeId id of recipe
     * @param ord order of step, leave at 0 for next step
     * @param instruction for user to follow
     * @param timer for timer, 0 for no timer
     * @return true if successful
     */
    public boolean createRecipeStep(int recipeId, int ord, String instruction, int timer) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO recipe_step (recipe_id, ord, instruction, timer) VALUES (?, ?, ?, ?)");
            ps.setInt(1, recipeId);
            if(ord == 0) {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT MAX(ord) FROM recipe_step WHERE recipe_id = " + recipeId + "");
                if(rs.next()) ord = rs.getInt(1) + 1;
                else ord = 1;
            }
            ps.setInt(2, ord);
            ps.setString(3, instruction);
            ps.setInt(4, timer);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public boolean createRecipeIngredient(int measurementNumber, int measurementId, int recipeId, int ingredientId) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO recipe_ingredient (measurement_number, measurement_id, recipe_id, ingredient_id) VALUES (?, ?, ?, ?)");
            ps.setInt(1, measurementNumber);
            ps.setInt(2, measurementId);
            ps.setInt(3, recipeId);
            ps.setInt(4, ingredientId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public String getIngredientName(int id) {
        String name = "";
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT name FROM ingredient WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) name = rs.getString("name");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return name;
    }
    public boolean createMeasurement(String name) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO measurement (name) VALUES (?)");
            ps.setString(1, name);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public boolean createStandardMeasurement() {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM measurement");
            if(!rs.next()) {
                statement.execute("INSERT INTO measurement (name) VALUES ('l')");
                statement.execute("INSERT INTO measurement (name) VALUES ('dl')");
                statement.execute("INSERT INTO measurement (name) VALUES ('cl')");
                statement.execute("INSERT INTO measurement (name) VALUES ('ml')");
                statement.execute("INSERT INTO measurement (name) VALUES ('g')");
            }
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean createRecipe(String name) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("INSERT INTO recipe (name) VALUES (?)");
            ps.setString(1, name);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    public Recipe getRecipe(int id) {
        ArrayList<RecipeStep> steps = getSteps(id);
        ArrayList<RecipeIngredient> ingredients = getIngredients(id);
        String name = getRecipeName(id);
        return new Recipe(id, steps, ingredients, name);
    }
    public String getRecipeName(int id) {
        PreparedStatement ps = null;
        String name = "";
        try {
            ps = conn.prepareStatement("SELECT name FROM recipe WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) name = rs.getString("name");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return name;
    }
    public String getMeasurementName(int id) {
        String name = "";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT name FROM measurement WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) name = rs.getString("name");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }


        return name;
    }
    public ArrayList<RecipeIngredient> getIngredients(int recipeId) {
        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT measurement_number, measurement_id, ingredient_id FROM recipe_ingredient WHERE recipe_id = ?");
            ps.setInt(1, recipeId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                ingredients.add(new RecipeIngredient(rs.getInt("measurement_number"), rs.getInt("measurement_id"), rs.getInt("ingredient_id")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return ingredients;
    }
    public ArrayList<RecipeStep> getSteps(int recipeId) {
        ArrayList<RecipeStep> steps = new ArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT instruction, timer FROM recipe_step WHERE recipe_id = ? ORDER BY ord");
            ps.setInt(1, recipeId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                steps.add(new RecipeStep(rs.getString("instruction"), rs.getInt("timer")));
            }
        } catch (SQLException e) {

            System.err.println(e.getMessage());
        }
        return steps;
    }

    @Override
    protected void finalize() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
