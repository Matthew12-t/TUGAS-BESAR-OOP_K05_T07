package SRC.COOKING;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Cooking mengelola semua resep dan logika cooking
 */
public class Cooking {
    private static List<Recipe> recipes = new ArrayList<>();
    
    static {
        initializeRecipes();
    }
    
    /**
     * Initialize all cooking recipes
     */
    private static void initializeRecipes() {
        // Fish n' Chips
        Recipe fishChips = new Recipe("Fish n' Chips", "Crispy fish with golden fries", 50);
        fishChips.addIngredient("Any Fish", 2);
        fishChips.addIngredient("Wheat", 1);
        fishChips.addIngredient("Potato", 1);
        recipes.add(fishChips);
        
        // Baguette
        Recipe baguette = new Recipe("Baguette", "Fresh French bread", 30);
        baguette.addIngredient("Wheat", 3);
        recipes.add(baguette);
        
        // Sashimi
        Recipe sashimi = new Recipe("Sashimi", "Fresh raw salmon slices", 40);
        sashimi.addIngredient("Salmon", 3);
        recipes.add(sashimi);
        
        // Fugu
        Recipe fugu = new Recipe("Fugu", "Dangerous but delicious pufferfish", 60);
        fugu.addIngredient("Pufferfish", 1);
        recipes.add(fugu);
        
        // Wine
        Recipe wine = new Recipe("Wine", "Fermented grape beverage", 35);
        wine.addIngredient("Grape", 2);
        recipes.add(wine);
        
        // Pumpkin Pie
        Recipe pumpkinPie = new Recipe("Pumpkin Pie", "Sweet autumn dessert", 45);
        pumpkinPie.addIngredient("Egg", 1);
        pumpkinPie.addIngredient("Wheat", 1);
        pumpkinPie.addIngredient("Pumpkin", 1);
        recipes.add(pumpkinPie);
        
        // Veggie Soup
        Recipe veggieSoup = new Recipe("Veggie Soup", "Healthy vegetable soup", 40);
        veggieSoup.addIngredient("Cauliflower", 1);
        veggieSoup.addIngredient("Parsnip", 1);
        veggieSoup.addIngredient("Potato", 1);
        veggieSoup.addIngredient("Tomato", 1);
        recipes.add(veggieSoup);
        
        // Fish Stew
        Recipe fishStew = new Recipe("Fish Stew", "Hearty fish stew with vegetables", 55);
        fishStew.addIngredient("Any Fish", 2);
        fishStew.addIngredient("Hot Pepper", 1);
        fishStew.addIngredient("Cauliflower", 2);
        recipes.add(fishStew);
        
        // Spakbor Salad
        Recipe spakborSalad = new Recipe("Spakbor Salad", "Fresh fruit and vegetable salad", 35);
        spakborSalad.addIngredient("Melon", 1);
        spakborSalad.addIngredient("Cranberry", 1);
        spakborSalad.addIngredient("Blueberry", 1);
        spakborSalad.addIngredient("Tomato", 1);
        recipes.add(spakborSalad);
        
        // Fish Sandwich
        Recipe fishSandwich = new Recipe("Fish Sandwich", "Tasty fish sandwich with vegetables", 45);
        fishSandwich.addIngredient("Any Fish", 1);
        fishSandwich.addIngredient("Wheat", 2);
        fishSandwich.addIngredient("Tomato", 1);
        fishSandwich.addIngredient("Hot Pepper", 1);
        recipes.add(fishSandwich);
        
        // The Legends of Spakbor
        Recipe legendsSpakbor = new Recipe("The Legends of Spakbor", "Legendary dish made with the rarest fish", 100);
        legendsSpakbor.addIngredient("Legend", 1); // Legend Fish
        legendsSpakbor.addIngredient("Potato", 2);
        legendsSpakbor.addIngredient("Parsnip", 1);
        legendsSpakbor.addIngredient("Tomato", 1);
        legendsSpakbor.addIngredient("Eggplant", 1);
        recipes.add(legendsSpakbor);
    }
    
    /**
     * Get all available recipes
     * @return List of all recipes
     */
    public static List<Recipe> getAllRecipes() {
        return new ArrayList<>(recipes);
    }
    
    /**
     * Get recipe by dish name
     * @param dishName Name of the dish
     * @return Recipe object or null if not found
     */
    public static Recipe getRecipe(String dishName) {
        for (Recipe recipe : recipes) {
            if (recipe.getDishName().equals(dishName)) {
                return recipe;
            }
        }
        return null;
    }
    
    /**
     * Check if player has enough fuel to cook
     * @param playerInventory Player's inventory
     * @return true if player has fuel
     */
    public static boolean hasFuel(Map<String, Integer> playerInventory) {
        int firewood = playerInventory.getOrDefault("Wood", 0);
        int coal = playerInventory.getOrDefault("Coal", 0);
        
        return (firewood > 0 || coal > 0);
    }
    
    /**
     * Get total cooking capacity based on fuel
     * @param playerInventory Player's inventory
     * @return Number of dishes that can be cooked
     */
    public static int getCookingCapacity(Map<String, Integer> playerInventory) {
        int firewood = playerInventory.getOrDefault("Wood", 0);
        int coal = playerInventory.getOrDefault("Coal", 0);
        
        // 1 Firewood = 1 dish, 1 Coal = 2 dishes
        return firewood + (coal * 2);
    }
    
    /**
     * Consume fuel for cooking one dish
     * @param playerInventory Player's inventory (will be modified)
     * @return true if fuel was consumed successfully
     */
    public static boolean consumeFuel(Map<String, Integer> playerInventory) {
        // Priority: Use coal first (more efficient), then firewood
        int coal = playerInventory.getOrDefault("Coal", 0);
        int firewood = playerInventory.getOrDefault("Wood", 0);
        
        if (coal > 0) {
            // Use coal - but we need to track partial usage
            // For simplicity, let's consume 1 coal per cooking action
            playerInventory.put("Coal", coal - 1);
            return true;
        } else if (firewood > 0) {
            // Use firewood
            playerInventory.put("Wood", firewood - 1);
            return true;
        }
        
        return false; // No fuel available
    }
}
