package SRC.DATA;

import SRC.ITEMS.Food;
import java.util.HashMap;
import java.util.Map;

/**
 * Data class for managing all food items with static initialization
 */
public class FoodData {
    private static Map<String, Food> foods = new HashMap<>();
    
    // Static initializer to load all foods
    static {
        initializeFoods();
    }
    
    /**
     * Initialize all food items
     */    private static void initializeFoods() {
        // Cooked Foods - format: name, sellPrice, buyPrice, energy
        foods.put("Fried Egg", new Food("Fried Egg", 35, 50, 25));
        foods.put("Omelet", new Food("Omelet", 125, 100, 50));
        foods.put("Pancakes", new Food("Pancakes", 80, 200, 40));
        foods.put("Bread", new Food("Bread", 60, 125, 30));
        foods.put("Cookies", new Food("Cookies", 140, 200, 70));
        foods.put("Chocolate Cake", new Food("Chocolate Cake", 200, 300, 100));
        foods.put("Blueberry Tart", new Food("Blueberry Tart", 150, 200, 75));
        foods.put("Cheese Cauliflower", new Food("Cheese Cauliflower", 300, 400, 150));
        foods.put("Parsnip Soup", new Food("Parsnip Soup", 120, 200, 60));
        foods.put("Fish Taco", new Food("Fish Taco", 500, 400, 250));
        foods.put("Salmon Dinner", new Food("Salmon Dinner", 300, 400, 150));
        foods.put("Sashimi", new Food("Sashimi", 75, 150, 37));
        foods.put("Maki Roll", new Food("Maki Roll", 220, 300, 110));
        foods.put("Baked Fish", new Food("Baked Fish", 100, 200, 50));
        foods.put("Salad", new Food("Salad", 110, 200, 55));
        
        // Beverages
        foods.put("Coffee", new Food("Coffee", 150, 200, 75));
        foods.put("Beer", new Food("Beer", 200, 300, 100));
        foods.put("Wine", new Food("Wine", 400, 500, 200));
        foods.put("Juice", new Food("Juice", 75, 150, 37));
        foods.put("Ice Cream", new Food("Ice Cream", 120, 200, 60));
        
        // Energy Items/Elixirs
        foods.put("Energy Tonic", new Food("Energy Tonic", 500, 1000, 500));
        foods.put("Life Elixir", new Food("Life Elixir", 500, 2000, 1000));
        foods.put("Oil of Garlic", new Food("Oil of Garlic", 1000, 3000, 1500));
        foods.put("Muscle Remedy", new Food("Muscle Remedy", 500, 1500, 750));
        
        System.out.println("FoodData: Initialized " + foods.size() + " food types");
    }
    
    /**
     * Get a food by name
     * @param foodName Name of the food
     * @return Food object or null if not found
     */
    public static Food getFood(String foodName) {
        return foods.get(foodName);
    }
    
    /**
     * Add food to inventory with specified quantity (returns the food item)
     * @param foodName Name of the food to add
     * @param quantity Quantity to add (for compatibility, not used in return)
     * @return Food item ready to be added to inventory
     */
    public static Food addFood(String foodName, int quantity) {
        Food food = getFood(foodName);
        if (food != null) {
            return food;
        } else {
            System.err.println("Food not found: " + foodName);
            return null;
        }
    }
    
    /**
     * Check if food exists
     * @param foodName Name of the food
     * @return true if food exists
     */
    public static boolean hasFood(String foodName) {
        return foods.containsKey(foodName);
    }
    
    /**
     * Get all foods
     * @return Map of all foods
     */
    public static Map<String, Food> getAllFoods() {
        return new HashMap<>(foods);
    }
}
