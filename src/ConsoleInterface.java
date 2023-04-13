import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleInterface {
    boolean exit;
    RecipeHandler rh;
    public ConsoleInterface(RecipeHandler rh) {
        exit = false;
        this.rh = rh;
    }
    public void printMenu() {
        System.out.println("Välkommen till Brödbaksappen");
        System.out.println("""
                1. Lista recept
                2. Visa ett recept
                3. Avsluta""");
        System.out.print("Ange val: ");
        switch (takeIntInput(1,3)) {
            case 1 -> {
                listRecipes();
                waitEnter();
            }
            case 2 -> {
                System.out.println("Ange id på recept du vill visa.");
                int choice = takeIntInput(1, rh.listRecipes().size());
                showRecipe(rh.getRecipe(choice));
                waitEnter();
            }
            case 3 -> {
                exit();
            }
        }
    }
    private void exit() {
        System.out.println("Är du säker? Ja / Nej");
        Scanner sc = new Scanner(System.in);
        String choice = sc.nextLine();
        if(choice.equalsIgnoreCase("ja")) exit = true;
        else if(choice.equalsIgnoreCase("nej")) printMenu();
        else exit();
    }
    private int selectRecipe() {
        System.out.println("Vilket recept vill du visa?");
        return takeIntInput(1, rh.listRecipes().size());
    }
    public void listRecipes() {
        System.out.println("Lista på recept i appen:");
        for(Recipe r : rh.listRecipes()) {
            System.out.println(r.getId() + ". " + r.getName() + ".");
        }
    }
    public void showRecipe(Recipe recipe) {
        System.out.println(recipe.getName());
        System.out.println("Ingredienser: ");
        for(RecipeIngredient ri : recipe.getIngredients()) {
            System.out.print(ri.getNbMeasurement() + rh.getMeasurementName(ri.getMeasurementId()) + ", ");
            System.out.println(rh.getIngredientName(ri.getIngredientId()));
        }
        int index = 1;
        System.out.println("Instruktioner: ");
        for(RecipeStep rs : recipe.getSteps()) {
            System.out.println(index + ": " + rs.getDesc());
            if(rs.getTimer() > 0) {
                System.out.println("Vänta i " + rs.getTimer() + " minuter.");
            }
            index++;
        }
    }
    private int takeIntInput(int min, int max) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        try {
            choice = sc.nextInt();
            if (choice < min || choice > max) {
                return choice;
            }
        } catch (Exception e) {
            System.out.println("Ange en siffra mellan " + min + " och " + max + ".");
            sc.nextLine();
        }
        return choice;
    }
    static void waitEnter() {
        System.out.println("[Tryck Enter för att fortsätta]");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }
}
