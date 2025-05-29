package SRC.ENTITY;

import SRC.MAIN.GamePanel;
import SRC.INVENTORY.Inventory;
import SRC.ITEMS.Item;
import SRC.ENTITY.ACTION.FishingUI;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.Time;
import SRC.TIME.GameTime;
import SRC.DATA.SleepData;
import SRC.UI.SleepUI;
import SRC.OBJECT.SuperObject;

/**
 * PlayerAction class handles all player action logic
 * including inventory management, movement, and interactions
 */
public class PlayerAction {
    private GamePanel gamePanel;
    private Player player;
    private Inventory inventory;
    private FishingUI fishingUI; // Add fishing UI
    private SleepUI sleepUI; // Add sleep UI
    
    // Energy system - now delegated to Player
    private static final int MAX_ENERGY = 100;
    
    // Sleep system constants
    private static final int LOW_ENERGY_THRESHOLD = 20;
    private static final int LATE_NIGHT_HOUR = 2; // 02:00
    
    public PlayerAction(GamePanel gamePanel, Player player) {        this.gamePanel = gamePanel;
        this.player = player;
        this.inventory = new Inventory();
        this.fishingUI = new FishingUI(gamePanel); // Initialize fishing UI
        this.sleepUI = new SleepUI(gamePanel); // Initialize sleep UI
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
     * Handle tool holding (not immediate usage)
     */
    public void holdTool(String toolName) {
        // Get the tool from inventory and set it as currently holding
        Item[] items = player.getInventoryItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i] instanceof SRC.ITEMS.Tool && 
                items[i].getName().equals(toolName)) {
                SRC.ITEMS.Tool tool = (SRC.ITEMS.Tool) items[i];
                player.setCurrentHoldingTool(tool);
                System.out.println("Now holding: " + toolName);
                return;
            }
        }
        System.out.println("Tool not found in inventory: " + toolName);
    }

    /**
     * Use the currently held tool and consume energy
     */
    public boolean useHeldTool() {
        if (!player.isHoldingAnyTool()) {
            System.out.println("No tool is currently held");
            return false;
        }
        
        SRC.ITEMS.Tool heldTool = player.getCurrentHoldingTool();
        String toolName = heldTool.getName();
        
        // Check energy requirements based on tool type
        int energyCost = getToolEnergyCost(toolName);
        if (!hasEnoughEnergy(energyCost)) {
            System.out.println("Not enough energy to use " + toolName);
            return false;
        }
        
        // Consume energy for tool usage
        consumeEnergy(energyCost);
        System.out.println("Used " + toolName + " (Energy cost: " + energyCost + ")");
        return true;
    }

    /**
     * Get energy cost for different tools
     */
    private int getToolEnergyCost(String toolName) {
        switch (toolName) {
            case "Fishing Rod":
                return 5; // Fishing energy cost
            case "Hoe":
                return 3; // Tilling energy cost
            case "Watering Can":
                return 2; // Watering energy cost
            case "Scythe":
                return 4; // Harvesting energy cost
            case "Pickaxe":
                return 6; // Mining energy cost
            default:
        return 3; // Default energy cost
        }
    }

    /**
     * Drop the currently held tool
     */
    public void dropHeldTool() {
        if (player.isHoldingAnyTool()) {
            SRC.ITEMS.Tool droppedTool = player.getCurrentHoldingTool();
            player.setCurrentHoldingTool(null);
            System.out.println("Dropped: " + droppedTool.getName());
        } else {
            System.out.println("No tool is currently held");
        }
    }

    /**
     * Handle item usage - updated for holding system
     */
    public void useItem(String itemName, String itemCategory) {
        System.out.println("Using item: " + itemName + " (Category: " + itemCategory + ")");
        
        // Different behavior based on item category
        switch (itemCategory.toLowerCase()) {
            case "tool":
                // Tools are now held instead of immediately used
                holdTool(itemName);
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
        
        // Use held fishing rod tool (this will consume energy)
        if (!useHeldTool()) {
            resumeGameTime();
            return false;
        }
        
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
        System.out.println("DEBUG: performFishingWithMiniGame called with location=" + fishingLocation);        // Get current game conditions directly from GamePanel (already enum types)
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
    }    /**
     * Check if player is holding a valid fishing rod
     * @return true if player is holding a fishing rod
     */
    private boolean hasValidFishingRod() {
        return player.isHolding("Fishing Rod") || player.isHolding("Fiberglass Rod");
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
    
    /**
     * Handle sleep action when player is near bed and presses sleep key
     * @return true if sleep action was performed successfully
     */
    public boolean performSleep() {
        System.out.println("DEBUG: performSleep called");
        
        // Check if player is near a bed
        if (!isPlayerNearBed()) {
            System.out.println("DEBUG: Player not near bed");
            return false;
        }
        
        // Perform manual sleep
        executeSleep(SleepData.SleepTrigger.MANUAL);
        return true;
    }
    
    /**
     * Check if player needs automatic sleep (low energy or late time)
     * This should be called regularly in the game loop
     */
    public void checkAutomaticSleep() {
        // Check low energy sleep
        if (player.getEnergy() <= LOW_ENERGY_THRESHOLD) {
            System.out.println("DEBUG: Auto sleep triggered - Low energy");
            executeSleep(SleepData.SleepTrigger.LOW_ENERGY);
            return;
        }
          // Check late night sleep
        Time currentTime = gamePanel.getCurrentTime();
        if (currentTime.getHour() == LATE_NIGHT_HOUR && currentTime.getMinute() == 0) {
            System.out.println("DEBUG: Auto sleep triggered - Late night");
            executeSleep(SleepData.SleepTrigger.LATE_TIME);
            return;
        }
    }
      /**
     * Execute sleep sequence (immediate spawn and effects)
     */
    private void executeSleep(SleepData.SleepTrigger trigger) {
        System.out.println("DEBUG: Executing sleep with trigger: " + trigger);
        
        // Stop player movement
        stopPlayerMovement();
        
        // LANGSUNG TRANSPORT PLAYER KE HOUSE BED
        transportPlayerToHouseBed();
        
        // LANGSUNG PERFORM SLEEP EFFECTS (restore energy, set time to 10:00)
        performSleepEffects(trigger);
        
        // Create sleep result SETELAH effects applied
        SleepData.SleepResult sleepResult = createSleepResult(trigger);
        
        // Show sleep screen
        sleepUI.showSleepResult(sleepResult);
        
        // Set game state to sleep mode
        gamePanel.setGameState(GamePanel.SLEEP_STATE);
    }    /**
     * Create sleep result based on current game state
     */
    private SleepData.SleepResult createSleepResult(SleepData.SleepTrigger trigger) {
        int currentDay = gamePanel.getCurrentDay();
        Season currentSeason = gamePanel.getSeason();
        Weather currentWeather = gamePanel.getWeather();
        
        return SleepData.createSleepResult(trigger, currentDay, currentSeason, currentWeather);
    }/**
     * Get access to the sleep UI
     */
    public SleepUI getSleepUI() {
        return this.sleepUI;
    }
    
    /**
     * Check if player is near a bed (within collision distance)
     */
    public boolean isPlayerNearBed() {
        SRC.MAP.Map currentMap = gamePanel.getCurrentMap();
        
        // Get player position in tiles
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        // Check surrounding tiles for bed objects
        for (int row = playerRow - 2; row <= playerRow + 2; row++) {
            for (int col = playerCol - 2; col <= playerCol + 2; col++) {
                if (currentMap.hasObjectAt(col, row)) {
                    SuperObject obj = currentMap.getObjectAt(col, row);
                    if (obj instanceof SRC.OBJECT.OBJ_Bed) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }    /**
     * Perform sleep effects (restore energy, set time to 10:00 AM, process shipping bin)
     */
    private void performSleepEffects(SleepData.SleepTrigger trigger) {
        // Restore full energy
        player.setEnergy(MAX_ENERGY);
        
        // Set time to 10:00 AM instead of 6:00 AM
        Time currentTime = gamePanel.getCurrentTime();
        currentTime.setHour(10);  // Set ke jam 10
        currentTime.setMinute(0); // Set ke menit 0
        
        // Process shipping bin income
        int shippingBinValue = gamePanel.getShippingBinData().calculateTotalValue();
        if (shippingBinValue > 0) {
            // Get current day and season for income calculation
            int currentDay = gamePanel.getCurrentDay();
            Season currentSeason = gamePanel.getSeason();
            Weather currentWeather = gamePanel.getWeather();
            
            // Create sleep result with shipping bin income
            SleepData.SleepResult sleepResult = SleepData.createSleepResultWithShipping(
                trigger, currentDay, currentSeason, currentWeather, shippingBinValue);
            
            // Add total income to player's gold
            player.addGold(sleepResult.getIncome());
            
            // Clear shipping bin after processing
            gamePanel.getShippingBinData().clearAllItems();
            
            System.out.println("DEBUG: Shipping bin processed - Value: " + shippingBinValue + 
                             ", Total income: " + sleepResult.getIncome() + " gold added");
        } else {
            // No shipping bin income, just add regular daily income
            int currentDay = gamePanel.getCurrentDay();
            Season currentSeason = gamePanel.getSeason();
            
            int dailyIncome = SleepData.calculateDailyIncome(currentDay, currentSeason);
            player.addGold(dailyIncome);
            
            System.out.println("DEBUG: Daily income: " + dailyIncome + " gold added (no shipping bin items)");
        }
        
        // Advance to next day if trigger is automatic (not manual)
        if (trigger == SleepData.SleepTrigger.LOW_ENERGY || 
            trigger == SleepData.SleepTrigger.LATE_TIME) {
            gamePanel.advanceToNextDay();
            // Reset time to 10:00 again after day advancement
            currentTime.setHour(10);
            currentTime.setMinute(0);
        }
        
        System.out.println("DEBUG: Sleep effects applied - Energy restored, time set to 10:00 AM");
    }
      /**
     * Transport player to house bed location (spawn beside the bed, not on it)
     */
    private void transportPlayerToHouseBed() {
        // Switch to house map if not already there
        if (!gamePanel.getCurrentMap().getMapName().equals("House Map")) {
            gamePanel.switchToHouseMap();
        }
        
        // Position player beside the bed in house map
        // Bed coordinates in house map
        int tileSize = gamePanel.getTileSize();
        int bedX = 5; // Bed position in house map
        int bedY = 3; // Bed position in house map
        
        // Position player to the RIGHT of bed (not on top)
        player.setWorldX(tileSize * (bedX + 2));
        player.setWorldY(tileSize * bedY);
        
        System.out.println("DEBUG: Player transported to house bed at position beside bed");
    }    /**
     * Check if player is near a shipping bin
     */
    public boolean isPlayerNearShippingBin() {
        int playerCol = (player.getWorldX() + player.getSolidArea().x) / gamePanel.getTileSize();
        int playerRow = (player.getWorldY() + player.getSolidArea().y) / gamePanel.getTileSize();
        
        // Check surrounding tiles for shipping bin
        for (int checkRow = playerRow - 1; checkRow <= playerRow + 1; checkRow++) {
            for (int checkCol = playerCol - 1; checkCol <= playerCol + 1; checkCol++) {
                if (checkRow >= 0 && checkRow < gamePanel.getMaxWorldRow() && 
                    checkCol >= 0 && checkCol < gamePanel.getMaxWorldCol()) {
                    
                    // Check for shipping bin object
                    SuperObject[] objects = gamePanel.getCurrentObjects();
                    for (int i = 0; i < objects.length; i++) {
                        if (objects[i] != null && 
                            objects[i].getName().equals("Shipping Bin")) {
                            
                            int objCol = objects[i].getWorldX() / gamePanel.getTileSize();
                            int objRow = objects[i].getWorldY() / gamePanel.getTileSize();
                            
                            // Check if object position matches check position
                            if (objRow <= checkRow && checkRow < objRow + 2 && // 2 rows high
                                objCol <= checkCol && checkCol < objCol + 3) { // 3 cols wide
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Open shipping bin interface
     */
    public void openShippingBin() {
        if (isPlayerNearShippingBin()) {
            gamePanel.setGameState(GamePanel.SHIPPING_STATE);
            System.out.println("Opened shipping bin");
        } else {
            System.out.println("No shipping bin nearby");
        }
    }
}
