package SRC.STORE;

import SRC.ITEMS.Item;
import SRC.ITEMS.Seed;
import SRC.ITEMS.Food;
import SRC.ITEMS.Crop;
import SRC.ITEMS.Tool;
import SRC.ITEMS.MiscItem;
import SRC.ENTITY.Player;
import SRC.INVENTORY.Inventory;

public class Store<T extends Item> {
    private String storeName;
    private StoreManager<T> storeManager;
    
    public Store(String storeName) {
        this.storeName = storeName;
        this.storeManager = new StoreManager<>();
    }
    
    public StoreManager<T> getStoreManager() {
        return storeManager;
    }
    
    public String getStoreName() {
        return storeName;
    }
    
    public boolean purchaseItem(Item item, int quantity, Player player) {
        if (item == null || quantity <= 0) {
            return false;
        }
        
        int totalCost = getBuyPrice(item) * quantity;
        
        if (player.getGold() < totalCost) {
            return false;
        }

        if (!storeManager.hasItem(item.getName())) {
            return false;
        }
        
        if (player.spendGold(totalCost)) {
            for (int i = 0; i < quantity; i++) {
                player.addItemToInventory(item);
            }
            
            return true;
        }
        
        return false;
    }

    public int getBuyPrice(Item item) {
        if(item.getBuyPrice() == 0){
            return 0; 
        }
        if (item instanceof Seed) {
            return ((Seed) item).getBuyPrice();
        } 
        else if (item instanceof Food) {
            return ((Food) item).getBuyPrice();
        } 
        else if (item instanceof Crop) {
            return ((Crop) item).getBuyPrice();
        } 
        else if (item instanceof MiscItem) {
            return ((MiscItem) item).getBuyPrice();
        }
        else if (item instanceof Tool) { 
            return ((Tool) item).getBuyPrice();
        }
        return 0;
    }
    
    public boolean canAfford(Item item, int quantity, Player player) {
        int totalCost = getBuyPrice(item) * quantity;
        return player.getGold() >= totalCost;
    }
    
    public boolean hasInventorySpace(Item item, int quantity, Player player) {
        Inventory inventory = player.getPlayerAction().getInventory();
        return inventory.getCurrentSize() < inventory.getMaxSlots();
    }
}
