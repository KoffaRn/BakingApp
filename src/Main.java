public class Main {
    public static void main(String[] args) {
        RecipeHandler rh = new RecipeHandler("recipe");
        rh.createTable();
        rh.createStandardMeasurement();
        ConsoleInterface ci = new ConsoleInterface(rh);
        while(!ci.exit) ci.printMenu();
    }
}