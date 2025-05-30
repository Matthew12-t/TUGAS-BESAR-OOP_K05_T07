package SRC.COOKING;

import java.util.Map;
import java.util.HashMap;

/**
 * Recipe class untuk menyimpan informasi resep masakan
 */
public class Recipe {
    private String dishName;
    private Map<String, Integer> ingredients; // ingredient name -> quantity required
    private String resultImagePath;
    private int energyGain; // Energy yang didapat setelah makan
    private String description;
    
    public Recipe(String dishName, String description, int energyGain) {
        this.dishName = dishName;
        this.description = description;
        this.energyGain = energyGain;
        this.ingredients = new HashMap<>();
        this.resultImagePath = "RES/FOOD/" + dishName.toLowerCase().replace(" ", "_").replace("'", "") + ".png";
    }
    
    /**
     * Add ingredient to recipe
     * @param ingredientName Name of the ingredient
     * @param quantity Quantity required
     */
    public void addIngredient(String ingredientName, int quantity) {
        ingredients.put(ingredientName, quantity);
    }
    
    /**
     * Check if player has enough ingredients to make this recipe
     * @param playerInventory Player's current inventory
     * @return true if player can make this recipe
     */
    public boolean canMake(Map<String, Integer> playerInventory) {
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            String ingredient = entry.getKey();
            int required = entry.getValue();
            
            // Special case for "Any Fish"
            if (ingredient.equals("Any Fish")) {
                int fishCount = 0;
                // Count all fish in inventory
                String[] fishTypes = {"Salmon", "Carp", "Catfish", "Angler", "Bullhead", 
                                     "Chub", "Flounder", "Halibut", "Largemouth Bass", 
                                     "Midnight Carp", "Rainbow Trout", "Sardine", "Sturgeon",
                                     "Crimsonfish", "Glacierfish", "Octopus", "Super Cucumber"};
                
                for (String fishType : fishTypes) {
                    fishCount += playerInventory.getOrDefault(fishType, 0);
                }
                
                if (fishCount < required) {
                    return false;
                }
            } else {
                // Normal ingredient check
                int available = playerInventory.getOrDefault(ingredient, 0);
                if (available < required) {
                    return false;
                }
            }
        }
        return true;
    }
      // Getters
    public String getDishName() { return dishName; }
    public Map<String, Integer> getIngredients() { return new HashMap<>(ingredients); }
    public String getResultImagePath() { return resultImagePath; }
    public int getEnergyGain() { return energyGain; }
    public int getEnergyValue() { return energyGain; } // Alias for getEnergyGain for UI compatibility
    public String getDescription() { return description; }
}
