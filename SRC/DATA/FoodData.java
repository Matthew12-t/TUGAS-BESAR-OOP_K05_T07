package SRC.DATA;

import SRC.ITEMS.Food;
import java.util.HashMap;
import java.util.Map;


public class FoodData {
    private static Map<String, Food> foods = new HashMap<>();
    

    static {
        initializeFoods();
    }
    
 
    private static void initializeFoods() {
        
        foods.put("Fish n' Chips", new Food("Fish n' Chips", 135, 150, 50)); 
        foods.put("Baguette", new Food("Baguette", 80, 100, 25));
        foods.put("Sashimi", new Food("Sashimi", 275, 300, 70));
        foods.put("Fugu", new Food("Fugu", 135, 0, 50)); 
        foods.put("Wine", new Food("Wine", 90, 100, 20)); 
        foods.put("Pumpkin Pie", new Food("Pumpkin Pie", 100, 120, 35)); 
        foods.put("Veggie Soup", new Food("Veggie Soup", 120, 140, 40));
        foods.put("Fish Stew", new Food("Fish Stew", 260, 280, 70)); 
        foods.put("Spakbor Salad", new Food("Spakbor Salad", 250, 0, 70)); 
        foods.put("Fish Sandwich", new Food("Fish Sandwich", 180, 200, 50));
        foods.put("The Legends of Spakbor", new Food("The Legends of Spakbor", 2000, 0, 100));
        foods.put("Cooked Pig's Head", new Food("Cooked Pig's Head", 0, 1000, 100));
        System.out.println("FoodData: Initialized " + foods.size() + " food types");
    }
    

    public static Food getFood(String foodName) {
        return foods.get(foodName);
    }
    

    public static Food addFood(String foodName, int quantity) {
        Food food = getFood(foodName);
        if (food != null) {
            return food;
        } else {
            System.err.println("Food not found: " + foodName);
            return null;
        }
    }
    
    public static boolean hasFood(String foodName) {
        return foods.containsKey(foodName);
    }

    public static Map<String, Food> getAllFoods() {
        return new HashMap<>(foods);
    }
}
