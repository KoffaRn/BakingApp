public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        RecipeHandler rh = new RecipeHandler("recipe");
        rh.createTable();
        rh.createStandardMeasurement();
        ConsoleInterface ci = new ConsoleInterface(rh);
        while(!ci.exit) ci.printMenu();
    }
}