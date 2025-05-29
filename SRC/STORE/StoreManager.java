package SRC.STORE;

import SRC.ITEMS.Item;
import SRC.ITEMS.Seed;
import SRC.ITEMS.Food;
import SRC.ITEMS.Crop;
import SRC.ITEMS.MiscItem;
import SRC.DATA.SeedData;
import SRC.DATA.FoodData;
import SRC.DATA.CropData;
import SRC.DATA.MiscData;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * StoreManager calls initialization functions from DATA folder and filters items 
 * that have non-null sell prices for the simplified store system.
 * Uses generics for type safety and flexible item management.
 */
public class StoreManager<T extends Item> {
    private List<Seed> availableSeeds;
    private List<Food> availableFoods;
    private List<Crop> availableCrops;
    private List<MiscItem> availableMiscItems;
    
    private Map<String, List<? extends Item>> categorizedItems;
    
    public StoreManager() {
        initializeData();
        filterAvailableItems();
        categorizeItems();
    }
    
    /**
     * Initialize all data from DATA classes
     */
    private void initializeData() {
        // Force initialization of all DATA classes
        SeedData.getAllSeeds();
        FoodData.getAllFoods();
        CropData.getAllCrops();
        MiscData.getAllMiscItems();
        
        System.out.println("StoreManager: All DATA classes initialized");
    }
    
    /**
     * Filter items that have valid sell prices (buyPrice > 0)
     */
    private void filterAvailableItems() {
        availableSeeds = new ArrayList<>();
        availableFoods = new ArrayList<>();
        availableCrops = new ArrayList<>();
        availableMiscItems = new ArrayList<>();
        
        // Filter seeds with valid buy prices
        for (Seed seed : SeedData.getAllSeeds().values()) {
            if (seed.getBuyPrice() > 0) {
                availableSeeds.add(seed);
            }
        }
        
        // Filter foods with valid buy prices
        for (Food food : FoodData.getAllFoods().values()) {
            if (food.getBuyPrice() > 0) {
                availableFoods.add(food);
            }
        }
        
        // Filter crops with valid buy prices
        for (Crop crop : CropData.getAllCrops().values()) {
            if (crop.getBuyPrice() > 0) {
                availableCrops.add(crop);
            }
        }
        
        // Filter misc items with valid buy prices
        for (MiscItem misc : MiscData.getAllMiscItems().values()) {
            if (misc.getBuyPrice() > 0) {
                availableMiscItems.add(misc);
            }
        }
        
        System.out.println("StoreManager: Filtered " + availableSeeds.size() + " seeds, " + 
                          availableFoods.size() + " foods, " + availableCrops.size() + " crops, " +
                          availableMiscItems.size() + " misc items");
    }
      /**
     * Categorize items for store display
     */
    private void categorizeItems() {
        categorizedItems = new HashMap<>();
        
        // Store typed lists directly
        categorizedItems.put("Seeds", availableSeeds);
        categorizedItems.put("Food", availableFoods);
        categorizedItems.put("Crops", availableCrops);
        categorizedItems.put("Misc", availableMiscItems);
    }
    
    /**
     * Get all available seeds
     */
    public List<Seed> getAvailableSeeds() {
        return new ArrayList<>(availableSeeds);
    }
    
    /**
     * Get all available foods
     */
    public List<Food> getAvailableFoods() {
        return new ArrayList<>(availableFoods);
    }
    
    /**
     * Get all available crops
     */
    public List<Crop> getAvailableCrops() {
        return new ArrayList<>(availableCrops);
    }
    
    /**
     * Get all available misc items
     */
    public List<MiscItem> getAvailableMiscItems() {
        return new ArrayList<>(availableMiscItems);
    }
      /**
     * Get items by category
     */
    public List<? extends Item> getItemsByCategory(String category) {
        return categorizedItems.getOrDefault(category, new ArrayList<>());
    }
    
    /**
     * Get all available categories
     */
    public String[] getCategories() {
        return categorizedItems.keySet().toArray(new String[0]);
    }
    
    /**
     * Get all items across all categories
     */
    public List<Item> getAllItems() {
        List<Item> allItems = new ArrayList<>();
        for (List<? extends Item> categoryItems : categorizedItems.values()) {
            allItems.addAll(categoryItems);
        }
        return allItems;
    }
    
    /**
     * Check if an item is available in the store
     */
    public boolean hasItem(String itemName) {
        for (List<? extends Item> categoryItems : categorizedItems.values()) {
            for (Item item : categoryItems) {
                if (item.getName().equals(itemName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get an item by name from store inventory
     */
    public Item getItem(String itemName) {
        for (List<? extends Item> categoryItems : categorizedItems.values()) {
            for (Item item : categoryItems) {
                if (item.getName().equals(itemName)) {
                    return item;
                }
            }
        }
        return null;
    }
}
