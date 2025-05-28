package SRC.ENTITY;

import SRC.MAIN.GamePanel;
import SRC.INVENTORY.Inventory;
import SRC.ITEMS.Item;
import SRC.ENTITY.ACTION.FishingUI;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.GameTime;

/**
 * PlayerAction class handles all player action logic
 * including inventory management, movement, and interactions
 */
public class PlayerAction {
      private GamePanel gamePanel;
    private Player player;
    private Inventory inventory;
    private FishingUI fishingUI; // Add fishing UI
    
    // Energy system - now delegated to Player
    private static final int MAX_ENERGY = 100;
    
    public PlayerAction(GamePanel gamePanel, Player player) {        this.gamePanel = gamePanel;
        this.player = player;
        this.inventory = new Inventory();
        this.fishingUI = new FishingUI(gamePanel); // Initialize fishing UI
        // Energy is now managed by Player class directly
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
    }    /**
     * Handle fishing action when player is on fishable water
     * @return true if fishing action was performed successfully
     */
    public boolean performFishing() {
        System.out.println("DEBUG: performFishing called");
        
        // Stop player movement during fishing
        stopPlayerMovement();
        
        // Check if player has enough energy for fishing (reduced to 5)
        final int FISHING_ENERGY_COST = 5;
        if (!hasEnoughEnergy(FISHING_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy");
            fishingUI.showInsufficientEnergy();
            return false;
        }
        
        // Check if player has fishing rod in inventory
        if (!hasValidFishingRod()) {
            System.out.println("DEBUG: No fishing rod");
            fishingUI.showMissingFishingRod();
            return false;
        }
        
        // Get player's current position in tile coordinates
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        // Check if player is on fishable water
        SRC.MAP.Map currentMap = gamePanel.getCurrentMap();
        if (!currentMap.getMapController().isOnFishableWater(currentMap, playerCol, playerRow) && 
            !currentMap.getMapController().isAdjacentToFishableWater(currentMap, playerCol, playerRow)) {
            System.out.println("DEBUG: Not on fishable water");
            fishingUI.showInvalidLocation();
            return false;
        }
        
        // Get current fishing location
        String fishingLocation = determineFishingLocation(currentMap.getMapName());
        if (fishingLocation == null) {
            System.out.println("DEBUG: Invalid fishing location");
            fishingUI.showInvalidLocation();
            return false;
        }
        
        System.out.println("DEBUG: All checks passed, starting fishing mini-game");
        
        // Show fishing attempt message
        fishingUI.showFishingAttempt();
        
        // Pause game time
        pauseGameTime();
        
        // Consume energy for fishing action
        consumeEnergy(FISHING_ENERGY_COST);
        
        // Perform fishing with mini-game
        String caughtFish = performFishingWithMiniGame(fishingLocation);
        
        // Add 15 minutes to game time
        addGameTime(15);
        
        // Resume game time
        resumeGameTime();
        
        if (caughtFish != null) {
            // Add fish to inventory using FishData system
            SRC.ITEMS.Fish fishItem = SRC.DATA.FishData.getFish(caughtFish);
            if (fishItem != null) {
                inventory.addItem(fishItem, 1);
                fishingUI.showFishingResult(caughtFish, true);
            }
        } else {
            fishingUI.showFishingResult("", false);
        }
        
        return true;
    }    /**
     * Perform fishing with mini-game based on current location and conditions
     * @param fishingLocation The location where fishing is taking place
     * @return name of caught fish or null if nothing caught
     */
    private String performFishingWithMiniGame(String fishingLocation) {
        System.out.println("DEBUG: performFishingWithMiniGame called with location=" + fishingLocation);
        
        // Get current game conditions directly from GamePanel (already enum types)
        Season currentSeason = gamePanel.getSeason();
        Weather currentWeather = gamePanel.getWeather();
        GameTime currentTime = new GameTime(
            gamePanel.getCurrentTime().getHour(), 
            gamePanel.getCurrentTime().getMinute()
        );
        
        System.out.println("DEBUG: Current conditions - Season: " + currentSeason + ", Weather: " + currentWeather + ", Time: " + currentTime.getHour() + ":" + currentTime.getMinute());
        
        // Get catchable fish based on current conditions
        java.util.Map<String, SRC.ITEMS.Fish> catchableFishMap = SRC.DATA.FishData.getCatchableFish(
            fishingLocation, currentTime, currentSeason, currentWeather
        );
        
        System.out.println("DEBUG: Found " + catchableFishMap.size() + " catchable fish");
        
        if (catchableFishMap.isEmpty()) {
            System.out.println("DEBUG: No fish available at this location/time/weather");
            return null; // No fish available at this location/time/weather
        }
          // Convert map to list and select random fish
        java.util.List<SRC.ITEMS.Fish> catchableFish = new java.util.ArrayList<>(catchableFishMap.values());
        java.util.Random random = new java.util.Random();
        SRC.ITEMS.Fish selectedFish = catchableFish.get(random.nextInt(catchableFish.size()));
        
        System.out.println("DEBUG: Selected fish: " + selectedFish.getName() + " (Type: " + selectedFish.getType() + ")");
        
        // Play integrated mini-game based on fish type
        boolean success = playIntegratedMiniGame(selectedFish.getType());
        
        System.out.println("DEBUG: Mini-game result: " + (success ? "SUCCESS" : "FAILED"));
        
        return success ? selectedFish.getName() : null;
    }
      /**
     * Integrated mini-game logic
     * @param fishType Type of fish (Common, Regular, Legendary)
     * @return true if mini-game was successful
     */
    private boolean playIntegratedMiniGame(String fishType) {
        System.out.println("DEBUG: playIntegratedMiniGame called with fishType=" + fishType);
        
        int maxRange;
        int maxAttempts;
        
        // Set parameters based on fish type
        switch (fishType) {
            case "Common":
                maxRange = 10;
                maxAttempts = 10;
                break;
            case "Regular":
                maxRange = 100;
                maxAttempts = 10;
                break;
            case "Legendary":
                maxRange = 500;
                maxAttempts = 7;
                break;
            default:
                maxRange = 50;
                maxAttempts = 8;
        }
        
        // Generate random target number
        int targetNumber = (int) (Math.random() * maxRange) + 1;
        
        System.out.println("DEBUG: About to call fishingUI.playGUIMiniGame with targetNumber=" + targetNumber);
        
        // Use GUI for mini-game
        return fishingUI.playGUIMiniGame(fishType, targetNumber, maxRange, maxAttempts);
    }
    
    /**
     * Determine fishing location based on current map
     * @param mapName The name of the current map
     * @return fishing location string or null if not a fishing location
     */
    private String determineFishingLocation(String mapName) {
        switch (mapName) {
            case "Forest River Map":
                return "Forest River";
            case "Mountain Lake":
                return "Mountain Lake";
            case "Ocean Map":
                return "Ocean";
            case "Farm Map":
                return "Pond"; // Assuming farm has a pond
            default:
                return null;
        }
    }
      /**
     * Stop player movement during actions like fishing
     */
    private void stopPlayerMovement() {
        // Stop keyboard movement by resetting key states
        gamePanel.getKeyHandler().upPressed = false;
        gamePanel.getKeyHandler().downPressed = false;
        gamePanel.getKeyHandler().leftPressed = false;
        gamePanel.getKeyHandler().rightPressed = false;
        
        // Stop mouse movement by clearing target
        gamePanel.getMouseHandler().setHasTarget(false);
        
        System.out.println("DEBUG: Player movement stopped for action");
    }

    /**
     * Check if player has a valid fishing rod in inventory
     * @return true if player has fishing rod
     */
    private boolean hasValidFishingRod() {
        return inventory.hasItem("Fishing Rod") || inventory.hasItem("Fiberglass Rod");
    }
    
    /**
     * Pause the game time during fishing
     */
    private void pauseGameTime() {
        // The time system will be paused during fishing mini-game
        // Implementation depends on GamePanel's time system
        gamePanel.pauseTime();
    }
    
    /**
     * Resume the game time after fishing
     */
    private void resumeGameTime() {
        // Resume the time system after fishing
        gamePanel.resumeTime();
    }
    
    /**
     * Add specific minutes to game time
     * @param minutes Minutes to add
     */
    private void addGameTime(int minutes) {
        // Add specified minutes to the game time
        gamePanel.addGameTime(minutes);
    }
    
    /**
     * Energy management methods
     */
      /**
     * Get current energy level (delegate to Player)
     * @return current energy value
     */
    public int getCurrentEnergy() {
        return player.getEnergy();
    }
    
    /**
     * Get maximum energy level
     * @return maximum energy value
     */
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }    /**
     * Consume energy for actions (delegate to Player)
     * @param amount energy to consume
     * @return true if energy was consumed, false if not enough energy
     */
    public boolean consumeEnergy(int amount) {
        return player.consumeEnergy(amount);
    }      /**
     * Restore energy (delegate to Player)
     * @param amount energy to restore
     */
    public void restoreEnergy(int amount) {
        player.restoreEnergy(amount);
    }
      /**
     * Set energy to specific value (delegate to Player)
     * @param energy new energy value
     */
    public void setEnergy(int energy) {
        player.setEnergy(energy);
    }
      /**
     * Check if player has enough energy for an action (delegate to Player)
     * @param requiredEnergy energy required
     * @return true if player has enough energy
     */
    public boolean hasEnoughEnergy(int requiredEnergy) {
        return player.hasEnoughEnergy(requiredEnergy);
    }
      /**
     * Get energy percentage (0.0 to 1.0) (delegate to Player)
     * @return energy as percentage
     */
    public double getEnergyPercentage() {
        return player.getEnergyPercentage();
    }
    
    /**
     * Update PlayerAction - including fishing UI
     */
    public void update() {
        fishingUI.update();
    }
    
    /**
     * Draw PlayerAction UI elements
     * @param g2 Graphics2D object for drawing
     */
    public void draw(java.awt.Graphics2D g2) {
        fishingUI.draw(g2);
    }
    
    /**
     * Get the fishing UI instance
     * @return FishingUI instance
     */
    public FishingUI getFishingUI() {
        return fishingUI;
    }    /**
     * Find the first edible item in inventory
     * @return slot index of first edible item, or -1 if none found
     */
    private int findFirstEdibleItem() {
        Item[] items = player.getInventoryItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i] instanceof SRC.ITEMS.Edible) {
                return i;
            }
        }
        return -1; // No edible items found
    }

    /**
     * Eat the currently selected item if it's edible
     * Called when player presses 'E' key or clicks "Use" button
     */
    public void eatSelectedItem() {
        // Get the currently selected slot from MouseHandler
        int selectedSlotIndex = gamePanel.getMouseHandler().getSelectedSlotIndex();
        
        // If no slot is selected (e.g., 'E' key press), find first edible item
        if (selectedSlotIndex < 0) {
            selectedSlotIndex = findFirstEdibleItem();
            if (selectedSlotIndex < 0) {
                System.out.println("No edible items found in inventory");
                return;
            }
            System.out.println("No item selected, using first edible item at slot " + selectedSlotIndex);
        }
        Item[] items = player.getInventoryItems();
        int[] quantities = player.getInventoryQuantities();
        
        if (selectedSlotIndex >= items.length || items[selectedSlotIndex] == null) {
            System.out.println("No item in selected slot");
            return;
        }
        
        Item selectedItem = items[selectedSlotIndex];
          // Check if the item is edible
        if (!(selectedItem instanceof SRC.ITEMS.Edible)) {
            System.out.println(selectedItem.getName() + " is not edible!");
            return;
        }
        SRC.ITEMS.Edible edibleItem = (SRC.ITEMS.Edible) selectedItem;
          // Consume the item (restore energy)
        int energyGained = edibleItem.getEnergyValue();
        System.out.println("DEBUG: Item " + selectedItem.getName() + " gives " + energyGained + " energy");
        
        // Get current energy before eating
        int currentEnergy = player.getEnergy();
        System.out.println("DEBUG: Player energy before eating: " + currentEnergy);        // Update Player's energy directly
        int newEnergy = currentEnergy + energyGained;
        if (newEnergy > 100) newEnergy = 100; // Cap at max energy
        player.setEnergy(newEnergy);
        
        System.out.println("DEBUG: Player energy after eating: " + player.getEnergy());
        System.out.println("You consumed " + selectedItem.getName() + " and gained " + energyGained + " energy!");
        
        // Add 5 minutes to game time
        addGameTime(5);
        // Reduce quantity or remove item if quantity becomes 0
        if (quantities[selectedSlotIndex] > 1) {
            quantities[selectedSlotIndex]--;
        } else {
            player.removeItemFromInventory(selectedSlotIndex);
            // Reset selection since item was removed
            gamePanel.getMouseHandler().setSelectedSlotIndex(-1);
        }
    }
}
