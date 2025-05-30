package SRC.DATA;

import SRC.ITEMS.Item;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Central data management class that provides unified access to all game items
 */
public class GameData {
      // Static initializer to ensure all data classes are loaded
    static {
        // Force initialization of all data classes
        SeedData.getAllSeeds();
        ToolData.getAllTools();
        CropData.getAllCrops();
        FishData.getAllFish();
        FoodData.getAllFoods();
        MiscData.getAllMiscItems();
        
        System.out.println("GameData: All item data initialized successfully!");
    }
    
    /**
     * Add seed to inventory via SeedData
     * @param seedName Name of the seed
     * @param quantity Quantity to add
     * @return Item ready to be added to inventory
     */
    public static Item addSeed(String seedName, int quantity) {
        return SeedData.addSeed(seedName, quantity);
    }
    
    /**
     * Add tool to inventory via ToolData
     * @param toolName Name of the tool
     * @param quantity Quantity to add
     * @return Item ready to be added to inventory
     */
    public static Item addTool(String toolName, int quantity) {
        return ToolData.addTool(toolName, quantity);
    }
    
    /**
     * Add crop to inventory via CropData
     * @param cropName Name of the crop
     * @param quantity Quantity to add
     * @return Item ready to be added to inventory
     */
    public static Item addCrop(String cropName, int quantity) {
        return CropData.addCrop(cropName, quantity);
    }
    
    /**
     * Add fish to inventory via FishData
     * @param fishName Name of the fish
     * @param quantity Quantity to add
     * @return Item ready to be added to inventory
     */
    public static Item addFish(String fishName, int quantity) {
        return FishData.addFish(fishName, quantity);
    }
    
    /**
     * Add food to inventory via FoodData
     * @param foodName Name of the food
     * @param quantity Quantity to add
     * @return Item ready to be added to inventory
     */
    public static Item addFood(String foodName, int quantity) {
        return FoodData.addFood(foodName, quantity);
    }
    
    /**
     * Add misc item to inventory via MiscData
     * @param miscName Name of the misc item
     * @param quantity Quantity to add
     * @return Item ready to be added to inventory
     */
    public static Item addMisc(String miscName, int quantity) {
        return MiscData.addMiscItem(miscName, quantity);
    }
    
    /**
     * Get an item by name from any category
     * @param itemName Name of the item
     * @param category Category of the item (optional, for faster lookup)
     * @return Item ready to be added to inventory
     */
    public static Item getItem(String itemName, String category) {
        // If category is specified, check that category first
        if (category != null) {
            switch (category.toLowerCase()) {
                case "seed":
                case "seeds":
                    if (SeedData.hasSeed(itemName)) {
                        return SeedData.getSeed(itemName);
                    }
                    break;
                case "tool":
                case "tools":
                    if (ToolData.hasTool(itemName)) {
                        return ToolData.getTool(itemName);
                    }
                    break;
                case "crop":
                case "crops":
                    if (CropData.hasCrop(itemName)) {
                        return CropData.getCrop(itemName);
                    }
                    break;
                case "fish":
                    if (FishData.hasFish(itemName)) {
                        return FishData.getFish(itemName);
                    }
                    break;                case "food":
                    if (FoodData.hasFood(itemName)) {
                        return FoodData.getFood(itemName);
                    }
                    break;
                case "misc":
                case "miscellaneous":
                    if (MiscData.hasMiscItem(itemName)) {
                        return MiscData.getMiscItem(itemName);
                    }
                    break;
            }
        }
        
        // If category not specified or item not found in specified category,
        // search all categories
        return getItem(itemName, 1);
    }
      /**
     * Get an item by name with default quantity of 1
     * @param itemName Name of the item
     * @param quantity Quantity (ignored, kept for compatibility)
     * @return Item ready to be added to inventory
     */
    public static Item getItem(String itemName, int quantity) {
        // Check all categories
        if (SeedData.hasSeed(itemName)) {
            return SeedData.getSeed(itemName);
        }
        if (ToolData.hasTool(itemName)) {
            return ToolData.getTool(itemName);
        }
        if (CropData.hasCrop(itemName)) {
            return CropData.getCrop(itemName);
        }
        if (FishData.hasFish(itemName)) {
            return FishData.getFish(itemName);
        }
        if (FoodData.hasFood(itemName)) {
            return FoodData.getFood(itemName);
        }
        if (MiscData.hasMiscItem(itemName)) {
            return MiscData.getMiscItem(itemName);
        }
        
        System.err.println("Item not found: " + itemName);
        return null;
    }
    
    /**
     * Get an item by name from specific category with quantity
     * @param itemName Name of the item
     * @param category Category of the item
     * @param quantity Quantity (ignored, kept for compatibility)
     * @return Item ready to be added to inventory
     */
    public static Item getItem(String itemName, String category, int quantity) {
        return getItem(itemName, category);
    }
      /**
     * Check if an item exists in any category
     * @param itemName Name of the item to check
     * @return true if item exists
     */
    public static boolean hasItem(String itemName) {
        return SeedData.hasSeed(itemName) || 
               ToolData.hasTool(itemName) || 
               CropData.hasCrop(itemName) || 
               FishData.hasFish(itemName) || 
               FoodData.hasFood(itemName) ||
               MiscData.hasMiscItem(itemName);
    }
      /**
     * Get all items from all categories
     * @return Map of all items with their categories
     */
    public static Map<String, String> getAllItemsWithCategories() {
        Map<String, String> allItems = new HashMap<>();
        
        // Add seeds
        SeedData.getAllSeeds().forEach((name, seed) -> allItems.put(name, "Seed"));
        
        // Add tools
        ToolData.getAllTools().forEach((name, tool) -> allItems.put(name, "Tool"));
        
        // Add crops
        CropData.getAllCrops().forEach((name, crop) -> allItems.put(name, "Crop"));
        
        // Add fish
        FishData.getAllFish().forEach((name, fish) -> allItems.put(name, "Fish"));
        
        // Add food
        FoodData.getAllFoods().forEach((name, food) -> allItems.put(name, "Food"));
        
        // Add misc items
        MiscData.getAllMiscItems().forEach((name, misc) -> allItems.put(name, "Misc"));
        
        return allItems;
    }
    
    /**
     * Get starter items for new players
     * @return Array of Items for starter inventory
     */
    public static Item[] getStarterItems() {
        return new Item[] {
            addSeed("Parsnip Seeds", 15),
            addTool("Hoe", 1),
            addTool("Watering Can", 1),
            addTool("Pickaxe", 1),
            addTool("Fishing Rod", 1),
            addTool("Proposal Ring", 2)
        };
    }
    
    /**
     * Get starter item quantities (parallel to getStarterItems)
     * @return Array of quantities for starter items
     */
    public static int[] getStarterQuantities() {
        return new int[] { 15, 1, 1, 1, 1 };
    }
    public static Set<String> getAllItemNames() {
        return getAllItemsWithCategories().keySet();
    }
}