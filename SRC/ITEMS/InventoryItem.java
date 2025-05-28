package SRC.ITEMS;

import java.awt.Image;

/**
 * InventoryItem represents an item in the player's inventory
 * It wraps an Item with a quantity and display image
 */
public class InventoryItem {
    private Item item;
    private int quantity;
    private Image image;
    
    /**
     * Constructor for InventoryItem
     * @param item The base item
     * @param quantity The quantity of this item
     */
    public InventoryItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
        this.image = null; // Can be set later
    }
    
    /**
     * Constructor for InventoryItem with image
     * @param item The base item
     * @param quantity The quantity of this item
     * @param image The display image for this item
     */
    public InventoryItem(Item item, int quantity, Image image) {
        this.item = item;
        this.quantity = quantity;
        this.image = image;
    }
    
    // Getters
    public Item getItem() {
        return item;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public Image getImage() {
        return image;
    }
    
    // Setters
    public void setItem(Item item) {
        this.item = item;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    /**
     * Increase quantity by specified amount
     * @param amount Amount to add
     */
    public void addQuantity(int amount) {
        this.quantity += amount;
    }
    
    /**
     * Decrease quantity by specified amount
     * @param amount Amount to subtract
     * @return true if successful, false if not enough quantity
     */
    public boolean removeQuantity(int amount) {
        if (this.quantity >= amount) {
            this.quantity -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * Check if this inventory item has enough quantity
     * @param amount Amount to check
     * @return true if enough quantity available
     */
    public boolean hasEnoughQuantity(int amount) {
        return this.quantity >= amount;
    }
    
    /**
     * Get display name with quantity
     * @return String representation of item with quantity
     */
    public String getDisplayName() {
        return item.getName() + " x" + quantity;
    }
    
    @Override
    public String toString() {
        return "InventoryItem{" +
                "item=" + item.getName() +
                ", quantity=" + quantity +
                '}';
    }
}
