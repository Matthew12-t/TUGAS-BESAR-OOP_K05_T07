package SRC.STORE;

import SRC.ITEMS.Item;
import SRC.ITEMS.Seed;
import SRC.ITEMS.Food;
import SRC.ITEMS.Crop;
import SRC.ITEMS.MiscItem;
import SRC.ENTITY.Player;
import SRC.INVENTORY.Inventory;
import java.util.List;

/**
 * Simple Store class that manages store inventory and transactions.
 * Uses StoreManager to get items with valid prices.
 * Generic implementation for type safety.
 */
public class Store<T extends Item> {
    private String storeName;
    private StoreManager<T> storeManager;
    
    public Store(String storeName) {
        this.storeName = storeName;
        this.storeManager = new StoreManager<>();
        System.out.println("Store '" + storeName + "' initialized");
    }
    
    /**
     * Get store manager
     */
    public StoreManager<T> getStoreManager() {
        return storeManager;
    }
    
    /**
     * Get store name
     */
    public String getStoreName() {
        return storeName;
    }
    
    /**
     * Purchase an item from the store
     */
    public boolean purchaseItem(Item item, int quantity, Player player) {
        if (item == null || quantity <= 0) {
            System.out.println("Invalid item or quantity");
            return false;
        }
        
        int totalCost = getBuyPrice(item) * quantity;
        
        // Check if player has enough gold
        if (player.getGold() < totalCost) {
            System.out.println("Not enough gold! Need " + totalCost + ", have " + player.getGold());
            return false;
        }
        
        // Check if item is available in store
        if (!storeManager.hasItem(item.getName())) {
            System.out.println("Item not available in store: " + item.getName());
            return false;
        }
        
        // Process the purchase
        if (player.spendGold(totalCost)) {
            // Add item to player's inventory
            for (int i = 0; i < quantity; i++) {
                player.addItemToInventory(item);
            }
            
            System.out.println("Successfully purchased " + quantity + "x " + item.getName() + 
                             " for " + totalCost + " gold");
            return true;
        }
        
        return false;
    }
    
    /**
     * Get the buy price for an item
     */
    public int getBuyPrice(Item item) {
        if (item instanceof Seed) {
            return ((Seed) item).getBuyPrice();
        } else if (item instanceof Food) {
            return ((Food) item).getBuyPrice();
        } else if (item instanceof Crop) {
            return ((Crop) item).getBuyPrice();
        } else if (item instanceof MiscItem) {
            return ((MiscItem) item).getBuyPrice();
        }
        return 0;
    }
    
    /**
     * Check if player can afford an item
     */
    public boolean canAfford(Item item, int quantity, Player player) {
        int totalCost = getBuyPrice(item) * quantity;
        return player.getGold() >= totalCost;
    }
    
    /**
     * Check if there's enough inventory space
     */
    public boolean hasInventorySpace(Item item, int quantity, Player player) {
        Inventory inventory = player.getPlayerAction().getInventory();
        return inventory.getCurrentSize() < inventory.getMaxSlots();
    }
}
