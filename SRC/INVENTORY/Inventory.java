package SRC.INVENTORY;

import SRC.ITEMS.Item;
import SRC.DATA.GameData;

/**
 * Inventory class manages the player's inventory system
 * Uses a simple slot-based system with items and quantities
 */
public class Inventory {
    
    // Slot-based inventory system
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
    
    /**
     * Initialize starting inventory items using GameData
     */    
    private void initStarterItems() {
        // Add Parsnip Seeds x15 using GameData
        Item seedItem = GameData.addSeed("Parsnip Seed", 15);
        if (seedItem != null) {
            addItem(seedItem, 15);
        }
        
        // Add Hoe using GameData
        Item hoeItem = GameData.addTool("Hoe", 1);
        if (hoeItem != null) {
            addItem(hoeItem, 1);
        }
        
        // Add Watering Can using GameData
        Item wateringCanItem = GameData.addTool("Watering Can", 1);
        if (wateringCanItem != null) {
            addItem(wateringCanItem, 1);
        }
        
        // Add Pickaxe using GameData
        Item pickaxeItem = GameData.addTool("Pickaxe", 1);
        if (pickaxeItem != null) {
            addItem(pickaxeItem, 1);
        }
        
        // Add Fishing Rod using GameData
        Item fishingRodItem = GameData.addTool("Fishing Rod", 1);
        if (fishingRodItem != null) {
            addItem(fishingRodItem, 1);
        }
        Item proposalRingItem = GameData.addTool("Proposal Ring", 2);
        if (proposalRingItem != null) {
            addItem(proposalRingItem, 2);
        }
    }
    
    /**
     * Add item to inventory with specified quantity
     */
    public void addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) return;
        
        // First try to stack with existing item
        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            if (itemSlots[i] != null && itemSlots[i].getName().equals(item.getName())) {
                quantitySlots[i] += quantity;
                return;
            }
        }
        
        // Find empty slot for new item
        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            if (itemSlots[i] == null) {
                itemSlots[i] = item;
                quantitySlots[i] = quantity;
                return;
            }
        }
        
        System.out.println("Inventory is full!");
    }
    
    /**
     * Remove item from specific slot
     */
    public void removeItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < MAX_INVENTORY_SLOTS) {
            if (itemSlots[slotIndex] != null) {
                System.out.println("Removed " + itemSlots[slotIndex].getName() + " x" + quantitySlots[slotIndex]);
                itemSlots[slotIndex] = null;
                quantitySlots[slotIndex] = 0;
            }
        }
    }
    
    /**
     * Remove one item from specific slot (decrement quantity by 1, remove if 0)
     */
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
    
    /**
     * Get item at specific slot
     */
    public Item getItem(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < MAX_INVENTORY_SLOTS) {
            return itemSlots[slotIndex];
        }
        return null;
    }
    
    /**
     * Get quantity at specific slot
     */
    public int getQuantity(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < MAX_INVENTORY_SLOTS) {
            return quantitySlots[slotIndex];
        }
        return 0;
    }
    
    /**
     * Get all non-null items (for compatibility)
     */
    public Item[] getItems() {
        return itemSlots;
    }
    
    /**
     * Get all quantities (for compatibility)
     */
    public int[] getQuantities() {
        return quantitySlots;
    }
    
    /**
     * Get maximum inventory slots
     */
    public int getMaxSlots() {
        return MAX_INVENTORY_SLOTS;
    }
    
    /**
     * Get current number of items (non-empty slots)
     */
    public int getCurrentSize() {
        int count = 0;
        for (Item item : itemSlots) {
            if (item != null) count++;
        }
        return count;
    }
    
    /**
     * Clear all inventory
     */
    public void clear() {
        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            itemSlots[i] = null;
            quantitySlots[i] = 0;
        }
    }
    
    /**
     * Check if inventory has space
     */
    public boolean hasSpace() {
        return getCurrentSize() < MAX_INVENTORY_SLOTS;
    }
    
    /**
     * Get inventory dimensions
     */
    public int getRows() {
        return INVENTORY_ROWS;
    }
    
    public int getCols() {
        return INVENTORY_COLS;
    }
    
    /**
     * Check if inventory contains an item with the specified name
     * @param itemName Name of the item to check for
     * @return true if item exists in inventory
     */
    public boolean hasItem(String itemName) {
        for (int i = 0; i < MAX_INVENTORY_SLOTS; i++) {
            if (itemSlots[i] != null && itemSlots[i].getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
}
