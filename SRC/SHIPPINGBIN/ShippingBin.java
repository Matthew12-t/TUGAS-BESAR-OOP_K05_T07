package SRC.SHIPPINGBIN;

import SRC.ITEMS.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ShippingBin {
    private List<ShippingItem> shippingBinItems;
    private Map<String, Integer> itemSellPrices;
    

    private static class ShippingItem {
        public Item item;
        public int quantity;
        
        public ShippingItem(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }
    }
    
    public ShippingBin() {
        this.shippingBinItems = new ArrayList<>();
        this.itemSellPrices = new HashMap<>();
        initializeSellPrices();
    }
    
    private void initializeSellPrices() {
        // Crop sell prices
        itemSellPrices.put("Parsnip", 35);
        itemSellPrices.put("Green Bean", 40);
        itemSellPrices.put("Cauliflower", 175);
        itemSellPrices.put("Potato", 80);
        itemSellPrices.put("Tulip Bulb", 30);
        itemSellPrices.put("Kale", 110);
        itemSellPrices.put("Garlic", 60);
        itemSellPrices.put("Blue Jazz", 50);
        itemSellPrices.put("Coffee Bean", 15);
        itemSellPrices.put("Rhubarb", 220);
        itemSellPrices.put("Strawberry", 120);
        itemSellPrices.put("Melon", 250);
        itemSellPrices.put("Tomato", 60);
        itemSellPrices.put("Blueberry", 50);
        itemSellPrices.put("Hot Pepper", 40);
        itemSellPrices.put("Radish", 90);
        itemSellPrices.put("Red Cabbage", 260);
        itemSellPrices.put("Starfruit", 750);
        itemSellPrices.put("Corn", 50);
        itemSellPrices.put("Eggplant", 60);
        itemSellPrices.put("Pumpkin", 320);
        itemSellPrices.put("Bok Choy", 80);
        itemSellPrices.put("Yam", 160);
        itemSellPrices.put("Beet", 100);
        itemSellPrices.put("Cranberries", 75);
        itemSellPrices.put("Sunflower", 90);
        itemSellPrices.put("Sweet Gem Berry", 3000);
        
        // Fish sell prices
        itemSellPrices.put("Sardine", 40);
        itemSellPrices.put("Carp", 30);
        itemSellPrices.put("Largemouth Bass", 100);
        itemSellPrices.put("Bullhead", 75);
        itemSellPrices.put("Rainbow Trout", 65);
        itemSellPrices.put("Salmon", 75);
        itemSellPrices.put("Catfish", 200);
        itemSellPrices.put("Sturgeon", 200);
        itemSellPrices.put("Halibut", 80);
        itemSellPrices.put("Angler", 900);
        itemSellPrices.put("Crimsonfish", 1500);
        itemSellPrices.put("Glacierfish", 1000);
        itemSellPrices.put("Legend", 5000);
        itemSellPrices.put("Midnight Carp", 150);
        itemSellPrices.put("Pufferfish", 200);
        itemSellPrices.put("Octopus", 150);
        itemSellPrices.put("Super Cucumber", 250);
        itemSellPrices.put("Flounder", 100);
        itemSellPrices.put("Chub", 50);
        
        // Food sell prices
        itemSellPrices.put("Fried Egg", 35);
        itemSellPrices.put("Omelet", 125);
        itemSellPrices.put("Salmonberries", 25);
        itemSellPrices.put("Cactus Fruit", 75);
        itemSellPrices.put("Sashimi", 75);
        itemSellPrices.put("Maki Roll", 220);
        itemSellPrices.put("Survival Burger", 180);
        itemSellPrices.put("Bean Hotpot", 100);
        itemSellPrices.put("Glazed Yams", 200);
        itemSellPrices.put("Carp Surprise", 150);
    }
    
    public boolean addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) return false;
        
        // Don't allow tools in shipping bin
        if (item instanceof SRC.ITEMS.Tool) {
            System.out.println("Cannot add tools to shipping bin: " + item.getName());
            return false;
        }
        
        // Check if item already exists in shipping bin
        for (ShippingItem shippingItem : shippingBinItems) {
            if (shippingItem.item.getName().equals(item.getName())) {
                shippingItem.quantity += quantity;
                System.out.println("Added " + quantity + " " + item.getName() + " to shipping bin (existing stack)");
                return true;
            }
        }
        
        // Create new shipping item with specified quantity
        shippingBinItems.add(new ShippingItem(item, quantity));
        System.out.println("Added " + quantity + " " + item.getName() + " to shipping bin (new item)");
        return true;
    }
    
    public void removeItem(Item item, int quantity) {
        if (item == null || quantity <= 0) return;
        
        for (int i = 0; i < shippingBinItems.size(); i++) {
            ShippingItem shippingItem = shippingBinItems.get(i);
            if (shippingItem.item.getName().equals(item.getName())) {
                shippingItem.quantity -= quantity;
                if (shippingItem.quantity <= 0) {
                    shippingBinItems.remove(i);
                }
                return;
            }
        }
    }
      public int calculateTotalValue() {
        int totalValue = 0;
        for (ShippingItem shippingItem : shippingBinItems) {
            int sellPrice = shippingItem.item.getSellPrice();
            totalValue += sellPrice * shippingItem.quantity;
        }
        return totalValue;
    }
    
    public void clearAllItems() {
        shippingBinItems.clear();
    }
    
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (ShippingItem shippingItem : shippingBinItems) {
            items.add(shippingItem.item);
        }
        return items;
    }
    
    public List<Integer> getQuantities() {
        List<Integer> quantities = new ArrayList<>();
        for (ShippingItem shippingItem : shippingBinItems) {
            quantities.add(shippingItem.quantity);
        }
        return quantities;
    }
    
    public boolean isEmpty() {
        return shippingBinItems.isEmpty();
    }
    
    public int getItemCount(String itemName) {
        for (ShippingItem shippingItem : shippingBinItems) {
            if (shippingItem.item.getName().equals(itemName)) {
                return shippingItem.quantity;
            }
        }
        return 0;
    }
    
    public int getTotalItemCount() {
        return shippingBinItems.size();
    }
}
