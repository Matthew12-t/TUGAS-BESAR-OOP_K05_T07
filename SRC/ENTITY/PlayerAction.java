package SRC.ENTITY;

import SRC.MAIN.GamePanel;
import SRC.INVENTORY.Inventory;
import SRC.ITEMS.Item;

/**
 * PlayerAction class handles all player action logic
 * including inventory management, movement, and interactions
 */
public class PlayerAction {
    
    private GamePanel gamePanel;
    private Player player;
    private Inventory inventory;
    
    // Energy system
    private static final int MAX_ENERGY = 100;
    private int currentEnergy;
    
    public PlayerAction(GamePanel gamePanel, Player player) {
        this.gamePanel = gamePanel;
        this.player = player;
        this.inventory = new Inventory();
        this.currentEnergy = MAX_ENERGY; // Start with full energy
    }
    
    /**
     * Handle inventory-related actions
     */
    public void openInventory() {
        gamePanel.setGameState(GamePanel.INVENTORY_STATE);
        System.out.println("Opened inventory");
    }
    
    public void closeInventory() {
        gamePanel.setGameState(GamePanel.PLAY_STATE);
        System.out.println("Closed inventory");
    }
    
    public void toggleInventory() {
        if (gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
            closeInventory();
        } else {
            openInventory();
        }
    }
    
    /**
     * Handle item removal from inventory
     */
    public void removeInventoryItem(int slotIndex) {
        inventory.removeItem(slotIndex);
        System.out.println("Removed item from slot " + slotIndex);
    }
      /**
     * Get inventory items for display
     */
    public Item[] getInventoryItems() {
        return inventory.getItems();
    }
    
    /**
     * Get inventory quantities for display
     */
    public int[] getInventoryQuantities() {
        return inventory.getQuantities();
    }
    
    /**
     * Add item to inventory
     */
    public void addItemToInventory(Item item) {
        inventory.addItem(item, 1);
    }
    
    /**
     * Get the inventory instance
     */
    public Inventory getInventory() {
        return inventory;
    }
    
    /**
     * Handle player movement actions
     */
    public void handleMovement() {
        // Movement logic will be implemented here
        // This can include keyboard movement and mouse target movement
    }
    
    /**
     * Handle player interactions with objects/NPCs
     */
    public void handleInteraction(int worldX, int worldY) {
        // Interaction logic will be implemented here
        System.out.println("Player interaction at: " + worldX + ", " + worldY);
    }
    
    /**
     * Handle tool usage
     */
    public void useTool(String toolName) {
        // Tool usage logic will be implemented here
        System.out.println("Using tool: " + toolName);
    }
    
    /**
     * Handle item usage
     */
    public void useItem(String itemName, String itemCategory) {
        System.out.println("Using item: " + itemName + " (Category: " + itemCategory + ")");
        
        // Different behavior based on item category
        switch (itemCategory.toLowerCase()) {
            case "tool":
                useTool(itemName);
                // Consume energy for tool usage
                consumeEnergy(5); // Tools consume 5 energy
                break;
            case "seed":
                useSeeds(itemName);
                // Consume energy for planting
                consumeEnergy(3); // Planting consumes 3 energy
                break;
            case "food":
                // Food restores energy, handled in MouseHandler
                System.out.println("Eating " + itemName);
                break;
            default:
                System.out.println("Cannot use item of category: " + itemCategory);
                break;
        }
    }
    
    /**
     * Handle seed usage for planting
     */
    private void useSeeds(String seedName) {
        System.out.println("Preparing to plant: " + seedName);
        // TODO: Implement planting logic
        // This could set a "planting mode" where the player can click on tillable soil
    }
    
    /**
     * Energy management methods
     */
    
    /**
     * Get current energy level
     * @return current energy value
     */
    public int getCurrentEnergy() {
        return currentEnergy;
    }
    
    /**
     * Get maximum energy level
     * @return maximum energy value
     */
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }
    
    /**
     * Consume energy for actions
     * @param amount energy to consume
     * @return true if energy was consumed, false if not enough energy
     */
    public boolean consumeEnergy(int amount) {
        if (currentEnergy >= amount) {
            currentEnergy -= amount;
            if (currentEnergy < 0) currentEnergy = 0;
            System.out.println("Consumed " + amount + " energy. Current: " + currentEnergy);
            return true;
        } else {
            System.out.println("Not enough energy! Current: " + currentEnergy + ", Required: " + amount);
            return false;
        }
    }
    
    /**
     * Restore energy
     * @param amount energy to restore
     */
    public void restoreEnergy(int amount) {
        currentEnergy += amount;
        if (currentEnergy > MAX_ENERGY) {
            currentEnergy = MAX_ENERGY;
        }
        System.out.println("Restored " + amount + " energy. Current: " + currentEnergy);
    }
    
    /**
     * Set energy to specific value
     * @param energy new energy value
     */
    public void setEnergy(int energy) {
        if (energy < 0) energy = 0;
        if (energy > MAX_ENERGY) energy = MAX_ENERGY;
        this.currentEnergy = energy;
    }
    
    /**
     * Check if player has enough energy for an action
     * @param requiredEnergy energy required
     * @return true if player has enough energy
     */
    public boolean hasEnoughEnergy(int requiredEnergy) {
        return currentEnergy >= requiredEnergy;
    }
    
    /**
     * Get energy percentage (0.0 to 1.0)
     * @return energy as percentage
     */
    public double getEnergyPercentage() {
        return (double) currentEnergy / MAX_ENERGY;
    }
}
