package SRC.DATA;

import SRC.ITEMS.Item;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class GameData {

    static {

        SeedData.getAllSeeds();
        ToolData.getAllTools();
        CropData.getAllCrops();
        FishData.getAllFish();
        FoodData.getAllFoods();
        MiscData.getAllMiscItems();
        
        System.out.println("GameData: All item data initialized successfully!");
    }
    

    public static Item addSeed(String seedName, int quantity) {
        return SeedData.addSeed(seedName, quantity);
    }
    

    public static Item addTool(String toolName, int quantity) {
        return ToolData.addTool(toolName, quantity);
    }
    

    public static Item addCrop(String cropName, int quantity) {
        return CropData.addCrop(cropName, quantity);
    }
    
 
    public static Item addFish(String fishName, int quantity) {
        return FishData.addFish(fishName, quantity);
    }
    

    public static Item addFood(String foodName, int quantity) {
        return FoodData.addFood(foodName, quantity);
    }
    

    public static Item addMisc(String miscName, int quantity) {
        return MiscData.addMiscItem(miscName, quantity);
    }
    

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
        

        return getItem(itemName, 1);
    }

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
    
 
    public static Item getItem(String itemName, String category, int quantity) {
        return getItem(itemName, category);
    }
  
    public static boolean hasItem(String itemName) {
        return SeedData.hasSeed(itemName) || 
               ToolData.hasTool(itemName) || 
               CropData.hasCrop(itemName) || 
               FishData.hasFish(itemName) || 
               FoodData.hasFood(itemName) ||
               MiscData.hasMiscItem(itemName);
    }

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
    
   public static Item[] getStarterItems() {
        return new Item[] {
            addSeed("Parsnip Seed", 15),
            addTool("Hoe", 1),
            addTool("Watering Can", 1),
            addTool("Pickaxe", 1),
            addTool("Fishing Rod", 1),
            addTool("Proposal Ring", 2)
        };
    }
    

    public static int[] getStarterQuantities() {
        return new int[] { 15, 1, 1, 1, 1 };
    }
    public static Set<String> getAllItemNames() {
        return getAllItemsWithCategories().keySet();
    }
}