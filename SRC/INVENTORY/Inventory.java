package SRC.INVENTORY;

import SRC.ITEMS.Item;
import SRC.DATA.GameData;


public class Inventory {
    

    private Item[] itemSlots;
    private int[] quantitySlots;
    private final int MAX_INVENTORY_SLOTS = 16;
    private final int INVENTORY_ROWS = 4;
    private final int INVENTORY_COLS = 4;
    
    public Inventory() {
        this.itemSlots = new Item[MAX_INVENTORY_SLOTS];
        this.quantitySlots = new int[MAX_INVENTORY_SLOTS];
        initStarterItems();
    }
    
  
    private void initStarterItems() {

        Item seedItem = GameData.addSeed("Parsnip Seed", 15);
        if (seedItem != null) {
            addItem(seedItem, 15);
        }
        Item hoeItem = GameData.addTool("Hoe", 1);
        if (hoeItem != null) {
            addItem(hoeItem, 1);
        }
        Item wateringCanItem = GameData.addTool("Watering Can", 1);
        if (wateringCanItem != null) {
            addItem(wateringCanItem, 1);
        }
        Item pickaxeItem = GameData.addTool("Pickaxe", 1);
        if (pickaxeItem != null) {
            addItem(pickaxeItem, 1);
        }
        
        Item fishingRodItem = GameData.addTool("Fishing Rod", 1);
        if (fishingRodItem != null) {
            addItem(fishingRodItem, 1);
        }
    }

    public void addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) return;
        
        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            if (itemSlots[i] != null && itemSlots[i].getName().equals(item.getName())) {
                quantitySlots[i] += quantity;
                return;
            }
        }
        
        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            if (itemSlots[i] == null) {
                itemSlots[i] = item;
                quantitySlots[i] = quantity;
                return;
            }
        }
        
        System.out.println("Inventory is full!");
    }
    

    public void removeItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < MAX_INVENTORY_SLOTS) {
            if (itemSlots[slotIndex] != null) {
                System.out.println("Removed " + itemSlots[slotIndex].getName() + " x" + quantitySlots[slotIndex]);
                itemSlots[slotIndex] = null;
                quantitySlots[slotIndex] = 0;
            }
        }
    }
    

    public void removeOneItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < MAX_INVENTORY_SLOTS) {
            if (itemSlots[slotIndex] != null) {
                if (quantitySlots[slotIndex] > 1) {
                    quantitySlots[slotIndex]--;
                } else {
                    itemSlots[slotIndex] = null;
                    quantitySlots[slotIndex] = 0;
                }
            }
        }
    }
    

    public Item getItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < MAX_INVENTORY_SLOTS) {
            return itemSlots[slotIndex];
        }
        return null;
    }
    

    public int getQuantity(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < MAX_INVENTORY_SLOTS) {
            return quantitySlots[slotIndex];
        }
        return 0;
    }
    

    public Item[] getItems() {
        return itemSlots;
    }
    

    public int[] getQuantities() {
        return quantitySlots;
    }
    

    public int getMaxSlots() {
        return MAX_INVENTORY_SLOTS;
    }
    

    public int getCurrentSize() {
        int count = 0;
        for (Item item : itemSlots) {
            if (item != null) count++;
        }
        return count;
    }
    

    public void clear() {
        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            itemSlots[i] = null;
            quantitySlots[i] = 0;
        }
    }
    
  
    public boolean hasSpace() {
        return getCurrentSize() < MAX_INVENTORY_SLOTS;
    }
    
 
    public int getRows() {
        return INVENTORY_ROWS;
    }
    
    public int getCols() {
        return INVENTORY_COLS;
    }
    

    public boolean hasItem(String itemName) {
        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            if (itemSlots[i] != null && itemSlots[i].getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
    

    public int getItemCount(String itemName) {
        int totalCount = 0;
        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            if (itemSlots[i] != null && itemSlots[i].getName().equals(itemName)) {
                totalCount += quantitySlots[i];
            }
        }
        return totalCount;
    }
}
