package SRC.STORE;

import SRC.ITEMS.Tool;
import SRC.DATA.ToolData; 
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


public class StoreManager<T extends Item> {
    private List<Seed> availableSeeds;
    private List<Food> availableFoods;
    private List<Crop> availableCrops;
    private List<MiscItem> availableMiscItems;
    private List<Tool> availableTools;
    
    private Map<String, List<? extends Item>> categorizedItems;
    
    public StoreManager() {
        initializeData();
        filterAvailableItems();
        categorizeItems();
    }
    
    private void initializeData() {
        SeedData.getAllSeeds();
        FoodData.getAllFoods();
        CropData.getAllCrops();
        MiscData.getAllMiscItems();
    }
    

    private void filterAvailableItems() {
        availableSeeds = new ArrayList<>();
        availableFoods = new ArrayList<>();
        availableCrops = new ArrayList<>();
        availableMiscItems = new ArrayList<>();
        availableTools = new ArrayList<>();
        
        for (Seed seed : SeedData.getAllSeeds().values()) {
            if (seed.getBuyPrice() > 0) {
                availableSeeds.add(seed);
            }
        }
        
        for (Food food : FoodData.getAllFoods().values()) {
            if (food.getBuyPrice() > 0) {
                availableFoods.add(food);
            }
        }
        
        for (Crop crop : CropData.getAllCrops().values()) {
            if (crop.getBuyPrice() > 0) {
                availableCrops.add(crop);
            }
        }
        
        for (MiscItem misc : MiscData.getAllMiscItems().values()) {
            if (misc.getBuyPrice() > 0) {
                availableMiscItems.add(misc);
            }
        }

        for (Tool tool : ToolData.getAllTools().values()) {
            if (tool.getBuyPrice() > 0) {
                availableTools.add(tool);
            }
        }
    }

    private void categorizeItems() {
        categorizedItems = new HashMap<>();
        
        categorizedItems.put("Seeds", availableSeeds);
        categorizedItems.put("Food", availableFoods);
        categorizedItems.put("Crops", availableCrops);
        categorizedItems.put("Misc", availableMiscItems);
        categorizedItems.put("Tools", availableTools);
    }
    

    public List<Seed> getAvailableSeeds() {
        return new ArrayList<>(availableSeeds);
    }
    
    public List<Food> getAvailableFoods() {
        return new ArrayList<>(availableFoods);
    }
    

    public List<Crop> getAvailableCrops() {
        return new ArrayList<>(availableCrops);
    }
    
    public List<MiscItem> getAvailableMiscItems() {
        return new ArrayList<>(availableMiscItems);
    }
    
    public List<Tool> getAvailableTools() {
        return new ArrayList<>(availableTools);
    }

    public List<? extends Item> getItemsByCategory(String category) {
        return categorizedItems.getOrDefault(category, new ArrayList<>());
    }
    

    public String[] getCategories() {
        return categorizedItems.keySet().toArray(new String[0]);
    }
    
    public List<Item> getAllItems() {
        List<Item> allItems = new ArrayList<>();
        for (List<? extends Item> categoryItems : categorizedItems.values()) {
            allItems.addAll(categoryItems);
        }
        return allItems;
    }
    

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
