package SRC.ENTITY;

import SRC.MAIN.GamePanel;
import SRC.MAP.NPC_HOUSE.HouseMap;
import SRC.INVENTORY.Inventory;
import SRC.ITEMS.Item;
import SRC.ENTITY.ACTION.FishingUI;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.Time;
import SRC.TIME.GameTime;
import SRC.UI.SleepUI;
import SRC.OBJECT.SuperObject;
import SRC.STORE.Store;

/**
 * PlayerAction class handles all player action logic
 * including inventory management, movement, and interactions
 */
public class PlayerAction {
    private GamePanel gamePanel;
    private Player player;    private Inventory inventory;
    private FishingUI fishingUI; // Add fishing UI
    private SleepUI sleepUI; // Add sleep UI
    private SRC.UI.TvUI tvUI; // Add TV UI
    
    // Energy system - now delegated to Player
    private static final int MAX_ENERGY = 100;
      // Sleep system constants
    private static final int LOW_ENERGY_THRESHOLD = -20;
    private static final int LATE_NIGHT_HOUR = 2; // 02:00
    
    // Last bed location tracking
    private String lastBedMapName = "House Map"; // Default to House Map
    private int lastBedX = 5; // Default bed X position in House Map
    private int lastBedY = 3; // Default bed Y position in House Map
      // Store and buying system
    private Store<Item> store;
      public PlayerAction(GamePanel gamePanel, Player player) {
        this.gamePanel = gamePanel;
        this.player = player;
        this.inventory = new Inventory();
        this.fishingUI = new FishingUI(gamePanel); // Initialize fishing UI
        this.sleepUI = new SleepUI(gamePanel); // Initialize sleep UI
        this.tvUI = new SRC.UI.TvUI(gamePanel); // Initialize TV UI
        // Energy is now managed by Player class directly
          // Initialize store system
        initializeStore();
    }/**
     * Initialize the store system with all available items
     */
    private void initializeStore() {
        store = new Store<>("General Store");
        System.out.println("Store system initialized");
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
     * Remove one item (decrement quantity by 1, remove if 0) from inventory
     */
    public void removeOneInventoryItem(int slotIndex) {
        inventory.removeOneItem(slotIndex);
        System.out.println("Removed one item from slot " + slotIndex);
    }
    
    /**
     * Handle item removal from inventory (removes entire stack)
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
     * Handle seed holding (not immediate usage)
     */
    public void holdSeed(String seedName) {
        // Get the seed from inventory and set it as currently holding
        Item[] items = player.getInventoryItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i] instanceof SRC.ITEMS.Seed && 
                items[i].getName().equals(seedName)) {
                SRC.ITEMS.Seed seed = (SRC.ITEMS.Seed) items[i];
                player.setCurrentHoldingSeed(seed);
                System.out.println("Now holding seed: " + seedName);
                return;
            }
        }
        System.out.println("Seed not found in inventory: " + seedName);
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
     * Drop the currently held seed
     */
    public void dropHeldSeed() {
        if (player.isHoldingAnySeed()) {
            SRC.ITEMS.Seed droppedSeed = player.getCurrentHoldingSeed();
            player.setCurrentHoldingSeed(null);
            System.out.println("Dropped seed: " + droppedSeed.getName());
        } else {
            System.out.println("No seed is currently held");
        }
    }    /**
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
                // Seeds are now held instead of immediately used
                holdSeed(itemName);
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
     * Perform tilling action when player presses 'C' key and holds a Hoe
     * @return true if tilling action was performed successfully
     */
    public boolean performTilling() {
        System.out.println("DEBUG: performTilling called");
        
        // Check if we're in Farm Map
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Tilling only available in Farm Map");
            return false;
        }
        
        // Check if player is holding a Hoe
        if (!player.isHolding("Hoe")) {
            System.out.println("DEBUG: Player must be holding a Hoe to till land");
            return false;
        }
        
        // Check if player has enough energy for tilling (5 energy)
        final int TILLING_ENERGY_COST = 5;
        if (!hasEnoughEnergy(TILLING_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy for tilling");
            return false;
        }
        
        // Get player's current position in tile coordinates
        int tileSize = gamePanel.getTileSize();        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        // Initialize TileManager if needed
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
        
        // Attempt to till the tile at player's position
        if (tileManager.tillTile(playerCol, playerRow)) {
            // Consume energy and time for successful tilling
            consumeEnergy(TILLING_ENERGY_COST);
            addGameTime(5); // Add 5 minutes to game time
            System.out.println("DEBUG: Successfully tilled land at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to till land at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }

    /**
     * Perform land recovery action when player holds a Pickaxe
     * @return true if land recovery action was performed successfully
     */
    public boolean performLandRecovery() {
        System.out.println("DEBUG: performLandRecovery called");
        
        // Check if we're in Farm Map
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Land recovery only available in Farm Map");
            return false;
        }
        
        // Check if player is holding a Pickaxe
        if (!player.isHolding("Pickaxe")) {
            System.out.println("DEBUG: Player must be holding a Pickaxe to recover land");
            return false;
        }
        
        // Check if player has enough energy for land recovery (5 energy)
        final int LAND_RECOVERY_ENERGY_COST = 5;
        if (!hasEnoughEnergy(LAND_RECOVERY_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy for land recovery");
            return false;
        }
        
        // Get player's current position in tile coordinates
        int tileSize = gamePanel.getTileSize();        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        // Initialize TileManager if needed
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
        
        // Attempt to recover the land at player's position
        if (tileManager.recoverLand(playerCol, playerRow)) {
            // Consume energy and time for successful land recovery
            consumeEnergy(LAND_RECOVERY_ENERGY_COST);
            addGameTime(5); // Add 5 minutes to game time
            System.out.println("DEBUG: Successfully recovered land at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to recover land at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }    /**
     * Handle fishing action when player is on fishable water
     * @return true if fishing action was performed successfully
     */
    public boolean performFishing() {
        System.out.println("DEBUG: performFishing called");
        
        // Stop player movement during fishing
        stopPlayerMovement();
        
        // Check if player has enough energy for at least one fishing attempt (5 energy)
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
        
        // NOTE: Energy consumption is now handled per attempt in the mini-game
        // No need to consume energy here as it will be consumed for each attempt
        
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
    }/**
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
     * Update PlayerAction - including fishing UI, sleep UI, and TV UI
     */
    public void update() {
        fishingUI.update();
        sleepUI.update();
        tvUI.update();
    }

    /**
     * Draw PlayerAction UI elements
     * @param g2 Graphics2D object for drawing
     */
    public void draw(java.awt.Graphics2D g2) {
        fishingUI.draw(g2);
        sleepUI.draw(g2);
        tvUI.draw(g2);
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
        executeSleep(SleepUI.SleepTrigger.MANUAL);
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
            executeSleep(SleepUI.SleepTrigger.LOW_ENERGY);
            return;
        }
          // Check late night sleep
        Time currentTime = gamePanel.getCurrentTime();
        if (currentTime.getHour() == LATE_NIGHT_HOUR && currentTime.getMinute() == 0) {
            System.out.println("DEBUG: Auto sleep triggered - Late night");
            executeSleep(SleepUI.SleepTrigger.LATE_TIME);
            return;
        }
    }
      /**
     * Execute sleep sequence (immediate spawn and effects)
     */
    private void executeSleep(SleepUI.SleepTrigger trigger) {
        System.out.println("DEBUG: Executing sleep with trigger: " + trigger);
        
        // Stop player movement
        stopPlayerMovement();
        
        // LANGSUNG TRANSPORT PLAYER KE HOUSE BED
        transportPlayerToHouseBed();
        
        // LANGSUNG PERFORM SLEEP EFFECTS (restore energy, set time to 10:00)
        performSleepEffects(trigger);
        
        // Create sleep result SETELAH effects applied
        SleepUI.SleepResult sleepResult = createSleepResult(trigger);
        
        // Show sleep screen
        sleepUI.showSleepResult(sleepResult);
        
        // Set game state to sleep mode
        gamePanel.setGameState(GamePanel.SLEEP_STATE);
    }    /**
     * Create sleep result based on current game state
     */
    private SleepUI.SleepResult createSleepResult(SleepUI.SleepTrigger trigger) {
        int currentDay = gamePanel.getCurrentDay();
        Season currentSeason = gamePanel.getSeason();
        Weather currentWeather = gamePanel.getWeather();
        
        return SleepUI.createSleepResult(trigger, currentDay, currentSeason, currentWeather);
    }/**
     * Get access to the sleep UI
     */
    public SleepUI getSleepUI() {
        return this.sleepUI;
    }    /**
     * Check if player is near a bed (within collision distance)
     * Improved logic to account for bed size (2x4 tiles)
     * Now supports beds in all house maps (HouseMap and NPCHouseMap)
     */
    public boolean isPlayerNearBed() {
        SRC.MAP.Map currentMap = gamePanel.getCurrentMap();
        
        // Check if player is in any house map (HouseMap or NPCHouseMap)
        if (currentMap != null && 
            !(currentMap instanceof HouseMap)) {
            System.out.println("DEBUG: Player is not in a house map, cannot sleep");
            return false; // Only check beds in house maps
        }
        
        // Get player position in tiles
        int tileSize = gamePanel.getTileSize();        
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        // Check all objects on the map for beds
        SuperObject[] objects = currentMap.getObjects();
        for (SuperObject obj : objects) {            
            if (obj instanceof SRC.OBJECT.OBJ_Bed) {
                SRC.OBJECT.OBJ_Bed bed = (SRC.OBJECT.OBJ_Bed) obj;
                
                int bedCol = obj.getPosition().getWorldX() / tileSize;
                int bedRow = obj.getPosition().getWorldY() / tileSize;
                int bedWidth = bed.getBedWidth(); 
                int bedHeight = bed.getBedHeight(); 
                
                boolean nearLeftSide = (playerCol == bedCol - 1) && 
                                     (playerRow >= bedRow - 1) && (playerRow <= bedRow + bedHeight);
                boolean nearRightSide = (playerCol == bedCol + bedWidth) && 
                                      (playerRow >= bedRow - 1) && (playerRow <= bedRow + bedHeight);
                boolean nearTopSide = (playerRow == bedRow - 1) && 
                                    (playerCol >= bedCol - 1) && (playerCol <= bedCol + bedWidth);
                boolean nearBottomSide = (playerRow == bedRow + bedHeight) && 
                                       (playerCol >= bedCol - 1) && (playerCol <= bedCol + bedWidth);
                  if (nearLeftSide || nearRightSide || nearTopSide || nearBottomSide) {
                    System.out.println("DEBUG: Player near bed at (" + bedCol + "," + bedRow + ") - " +
                                     "Size: " + bedWidth + "x" + bedHeight + " - Player at (" + playerCol + "," + playerRow + ")");
                    
                    // Track this bed as the last bed location
                    lastBedMapName = currentMap.getMapName();
                    lastBedX = bedCol;
                    lastBedY = bedRow;
                    System.out.println("DEBUG: Updated last bed location to map: " + lastBedMapName + 
                                     " at (" + lastBedX + "," + lastBedY + ")");
                    
                    return true;
                }
            }
        }
          return false;
    }
    
    /**
     * Check if player is near a TV (within interaction distance)
     * TV is 1x1 tile, so check adjacent tiles around it
     */
    public boolean isPlayerNearTV(){
        SRC.MAP.Map currentMap = gamePanel.getCurrentMap();
        
        // Get player position in tiles
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        // Check all objects on the map for TV
        SuperObject[] objects = currentMap.getObjects();
        for (SuperObject obj : objects) {
            if (obj instanceof SRC.OBJECT.OBJ_Tv) {
                SRC.OBJECT.OBJ_Tv tv = (SRC.OBJECT.OBJ_Tv) obj;
                
                int tvCol = obj.getPosition().getWorldX() / tileSize;
                int tvRow = obj.getPosition().getWorldY() / tileSize;
                int tvWidth = tv.getTvWidth(); 
                int tvHeight = tv.getTvHeight(); 
                boolean nearLeftSide = (playerCol == tvCol - 1) && 
                                     (playerRow >= tvRow - 1) && (playerRow <= tvRow + tvHeight);
                boolean nearRightSide = (playerCol == tvCol + tvWidth) && 
                                      (playerRow >= tvRow - 1) && (playerRow <= tvRow + tvHeight);
                boolean nearTopSide = (playerRow == tvRow - 1) && 
                                    (playerCol >= tvCol - 1) && (playerCol <= tvCol + tvWidth);
                boolean nearBottomSide = (playerRow == tvRow + tvHeight) && 
                                       (playerCol >= tvCol - 1) && (playerCol <= tvCol + tvWidth);
                
                if (nearLeftSide || nearRightSide || nearTopSide || nearBottomSide) {
                    System.out.println("DEBUG: Player near TV at (" + tvCol + "," + tvRow + ") - " +
                                     "Size: " + tvWidth + "x" + tvHeight + " - Player at (" + playerCol + "," + playerRow + ")");
                    return true;
                }
            }
        }
        
        return false;
    }
    /**
     * Perform sleep effects (restore energy, set time to 10:00 AM, process shipping bin)
     */
    private void performSleepEffects(SleepUI.SleepTrigger trigger) {
        // Restore full energy
        player.setEnergy(MAX_ENERGY);
        
        // Set time to 10:00 AM instead of 6:00 AM
        Time currentTime = gamePanel.getCurrentTime();
        currentTime.setHour(10);  // Set ke jam 10
        currentTime.setMinute(0); // Set ke menit 0
        
        // Process shipping bin income
        int shippingBinValue = gamePanel.getShippingBin().calculateTotalValue();
        if (shippingBinValue > 0) {
            // Get current day and season for income calculation
            int currentDay = gamePanel.getCurrentDay();
            Season currentSeason = gamePanel.getSeason();
            Weather currentWeather = gamePanel.getWeather();
            
            // Create sleep result with shipping bin income
            SleepUI.SleepResult sleepResult = SleepUI.createSleepResultWithShipping(
                trigger, currentDay, currentSeason, currentWeather, shippingBinValue);
            
            // Add total income to player's gold
            player.addGold(sleepResult.getIncome());
            
            // Clear shipping bin after processing
            gamePanel.getShippingBin().clearAllItems();
            
            System.out.println("DEBUG: Shipping bin processed - Value: " + shippingBinValue + 
                             ", Total income: " + sleepResult.getIncome() + " gold added");
        } else {
            // No shipping bin income, just add regular daily income
            int currentDay = gamePanel.getCurrentDay();
            Season currentSeason = gamePanel.getSeason();
            
            int dailyIncome = SleepUI.calculateDailyIncome(currentDay, currentSeason);
            player.addGold(dailyIncome);
            
            System.out.println("DEBUG: Daily income: " + dailyIncome + " gold added (no shipping bin items)");        }
        
        // Always advance to next day when sleeping (manual or automatic)
        gamePanel.advanceToNextDay();
        // Reset time to 10:00 after day advancement
        currentTime.setHour(10);
        currentTime.setMinute(0);
        
        System.out.println("DEBUG: Sleep effects applied - Energy restored, time set to 10:00 AM, advanced to next day");
    }    /**
     * Transport player to last bed location (spawn beside the bed, not on it)
     */
    private void transportPlayerToHouseBed() {
        // Switch to the map where the last bed was located
        if (!gamePanel.getCurrentMap().getMapName().equals(lastBedMapName)) {
            // Switch to the appropriate map
            if (lastBedMapName.equals("House Map")) {
                gamePanel.switchToHouseMap();
            } else {
                // For NPC house maps, we need to switch to the appropriate map
                // This assumes there are methods to switch to specific NPC house maps
                // If not available, we'll fall back to the house map
                
                    System.out.println("DEBUG: Could not switch to " + lastBedMapName + ", falling back to House Map");
                    gamePanel.switchToHouseMap();
                    lastBedMapName = "House Map";
                    lastBedX = 5;
                    lastBedY = 3;
                
            }
        }
        
        
        // Position player to the RIGHT of bed (not on top)
        player.setWorldX(player.getWorldX()); // Move 1 tile to the right
        player.setWorldY(player.getWorldY() ); // Center vertically beside the bed
        
        System.out.println("DEBUG: Player transported to last bed at " + lastBedMapName + 
                         " position (" + lastBedX + "," + lastBedY + ") - Player positioned beside bed");
    }
    
    /**
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
      /**
     * =====================
     * STORE AND BUYING SYSTEM METHODS
     * =====================
     */
    
    /**
     * Open the store interface
     */
    public void openStore() {
        gamePanel.setGameState(GamePanel.STORE_STATE);
        System.out.println("Opened store");
    }
    
    /**
     * Close the store interface
     */
    public void closeStore() {
        gamePanel.setGameState(GamePanel.PLAY_STATE);
        System.out.println("Closed store");
    }
    
    /**
     * Get the store instance
     * @return The store
     */
    public Store<Item> getStore() {
        return store;
    }
    
    /**
     * Check if player is near a store (for opening store interface)
     * @return true if near store
     */
    public boolean isPlayerNearStore() {
        // Check if player is in store map or near store object
        return gamePanel.getCurrentMap().getMapName().equals("Store Map");
    }
    
    /**
     * Perform planting action when player presses 'P' key and holds a Seed
     * @return true if planting action was performed successfully
     */
    public boolean performPlanting() {
        System.out.println("DEBUG: performPlanting called");
        
        // Check if we're in Farm Map
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Planting only available in Farm Map");
            return false;
        }
        
        // Check if player is holding a seed
        String heldSeedName = getHeldSeedName();
        if (heldSeedName == null) {
            System.out.println("DEBUG: Player must be holding a seed to plant");
            return false;
        }
        
        // Check if player has enough energy for planting (5 energy)
        final int PLANTING_ENERGY_COST = 5;
        if (!hasEnoughEnergy(PLANTING_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy for planting");
            return false;
        }
        
        // Get player's current position in tile coordinates
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        // Get seed growth days from SeedData
        int daysToGrow = getSeedGrowthDays(heldSeedName);        if (daysToGrow == -1) {
            System.out.println("DEBUG: Invalid seed data for " + heldSeedName);
            return false;
        }
        
        // Initialize TileManager if needed
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
        
        // Attempt to plant the seed at player's position
        if (tileManager.plantSeed(playerCol, playerRow, heldSeedName, daysToGrow)) {
            // Consume energy and time for successful planting
            consumeEnergy(PLANTING_ENERGY_COST);
            addGameTime(5); // Add 5 minutes to game time
            
            // Remove one seed from inventory
            removeSeedFromInventory(heldSeedName);
            
            System.out.println("DEBUG: Successfully planted " + heldSeedName + " at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to plant " + heldSeedName + " at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }    /**
     * Get the name of the seed currently being held by the player
     * @return seed name if holding a seed, null otherwise
     */
    public String getHeldSeedName() {
        // First check the new holding system
        if (player.isHoldingAnySeed()) {
            return player.getCurrentHoldingSeed().getName();
        }
        
        // Fallback to old inventory-based system for backward compatibility
        int selectedSlotIndex = gamePanel.getMouseHandler().getSelectedSlotIndex();
        if (selectedSlotIndex >= 0) {
            Item[] items = player.getInventoryItems();
            if (selectedSlotIndex < items.length && items[selectedSlotIndex] != null) {
                Item selectedItem = items[selectedSlotIndex];
                if (selectedItem.getCategory().equals("Seed")) {
                    return selectedItem.getName();
                }
            }
        }
          
        return null; // Not holding any seed
    }
    
    /**
     * Get growth days for a specific seed from SeedData
     * @param seedName name of the seed
     * @return number of days to grow, or -1 if not found
     */
    private int getSeedGrowthDays(String seedName) {
        try {
            // Access SeedData to get growth information
            SRC.ITEMS.Seed seed = SRC.DATA.SeedData.getSeed(seedName);
            if (seed != null) {
                return seed.getDaysToHarvest();
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Error accessing seed data for " + seedName + ": " + e.getMessage());
        }
        return -1; // Default or error case
    }
      /**
     * Remove one seed from inventory after planting
     * @param seedName name of the seed to remove
     */
    private void removeSeedFromInventory(String seedName) {
        Item[] items = player.getInventoryItems();
        int[] quantities = player.getInventoryQuantities();
        
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getName().equals(seedName)) {
                if (quantities[i] > 1) {
                    quantities[i]--; // Reduce quantity
                } else {
                    player.removeItemFromInventory(i); // Remove item completely
                    // Clear held seed if this seed was being held
                    if (player.isHoldingSeed(seedName)) {
                        player.setCurrentHoldingSeed(null);
                    }
                    // Reset selection if this was the selected item
                    if (gamePanel.getMouseHandler().getSelectedSlotIndex() == i) {
                        gamePanel.getMouseHandler().setSelectedSlotIndex(-1);
                    }
                }
                System.out.println("DEBUG: Removed 1x " + seedName + " from inventory");
                return;
            }
        }
        System.out.println("DEBUG: Could not find " + seedName + " in inventory to remove");
    }

    /**
     * Perform harvesting action when player presses 'H' key on a ready crop
     * @return true if harvesting action was performed successfully
     */
    public boolean performHarvesting() {
        System.out.println("DEBUG: performHarvesting called");
        
        // Check if we're in Farm Map
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Harvesting only available in Farm Map");
            return false;
        }
        
        // Get player's current position in tile coordinates
        int tileSize = gamePanel.getTileSize();        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        // Initialize TileManager if needed
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
        
        // Check if there's a plant at this position that's ready to harvest
        if (!tileManager.hasPlantAt(playerCol, playerRow)) {
            System.out.println("DEBUG: No plant at this location");
            return false;
        }
        
        if (!tileManager.isPlantReadyToHarvest(playerCol, playerRow)) {
            System.out.println("DEBUG: Plant is not ready to harvest yet");
            return false;
        }
        
        // Attempt to harvest the crop
        String cropName = tileManager.harvestCrop(playerCol, playerRow);
        if (cropName != null) {
            // Add crop to inventory
            try {                
                SRC.ITEMS.Crop crop = SRC.DATA.CropData.getCrop(cropName);
                if (crop != null) {
                    // Add multiple crops to inventory
                    int harvestQuantity = crop.getCropPerHarvest();
                    for (int i = 0; i < harvestQuantity; i++) {
                        player.addItemToInventory(crop);
                    }
                    System.out.println("DEBUG: Harvested " + harvestQuantity + "x " + cropName);
                } else {
                    System.out.println("DEBUG: Could not find crop data for " + cropName);
                }
            } catch (Exception e) {
                System.out.println("DEBUG: Error adding crop to inventory: " + e.getMessage());
            }
            addGameTime(2);
            
            System.out.println("DEBUG: Successfully harvested " + cropName + " at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to harvest crop at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }
    
    /**
     * Perform watering action when player presses 'C' key and holds a Watering Can
     * @return true if watering action was performed successfully
     */
    public boolean performWatering() {
        System.out.println("DEBUG: performWatering called");
        
        // Check if we're in Farm Map
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Watering only available in Farm Map");
            return false;
        }
        
        // Check if player is holding a Watering Can
        if (!player.isHolding("Watering Can")) {
            System.out.println("DEBUG: Player must be holding a Watering Can to water plants");
            return false;
        }
        
        // Check energy requirements (2 energy for watering)
        if (!hasEnoughEnergy(2)) {
            System.out.println("DEBUG: Not enough energy to water plants");
            return false;
        }
        
        // Get player's current position in tile coordinates
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
          // Initialize TileManager if needed
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
        
        // Check if there's a plant at this position
        if (!tileManager.hasPlantAt(playerCol, playerRow)) {
            System.out.println("DEBUG: No plant at this location to water");
            return false;
        }
        
        // Check if plant is already watered today
        if (tileManager.isPlantWateredToday(playerCol, playerRow)) {
            System.out.println("DEBUG: Plant is already watered today");
            return false;
        }
        
        // Perform watering
        boolean success = tileManager.waterPlant(playerCol, playerRow);
        if (success) {
            // Consume energy for watering
            consumeEnergy(2);
            
            // Add time for watering (1 minute)
            addGameTime(1);
            
            System.out.println("DEBUG: Successfully watered plant at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to water plant at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }
    
    /**
     * Check if player is near a stove for cooking
     */
    public boolean isPlayerNearStove() {
        int playerCol = (player.getWorldX() + player.getSolidArea().x) / gamePanel.getTileSize();
        int playerRow = (player.getWorldY() + player.getSolidArea().y) / gamePanel.getTileSize();
        
        // Check surrounding tiles for stove
        for (int checkRow = playerRow - 1; checkRow <= playerRow + 1; checkRow++) {
            for (int checkCol = playerCol - 1; checkCol <= playerCol + 1; checkCol++) {
                if (checkRow >= 0 && checkRow < gamePanel.getMaxWorldRow() && 
                    checkCol >= 0 && checkCol < gamePanel.getMaxWorldCol()) {
                    
                    // Check for stove object
                    SuperObject[] objects = gamePanel.getCurrentObjects();
                    for (int i = 0; i < objects.length; i++) {
                        if (objects[i] != null && 
                            objects[i].getName().equals("stove")) {
                            
                            int objCol = objects[i].getWorldX() / gamePanel.getTileSize();
                            int objRow = objects[i].getWorldY() / gamePanel.getTileSize();
                            
                            // Check if object position matches check position (stove is 1x1)
                            if (objRow == checkRow && objCol == checkCol) {
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
     * Perform cooking action when player presses 'C' key near a stove
     * @return true if cooking action was performed successfully
     */
    public boolean performCooking() {
        System.out.println("DEBUG: performCooking called");
        
        // Check if player is near a stove
        if (!isPlayerNearStove()) {
            System.out.println("DEBUG: Player not near stove");
            return false;
        }
        
        // Open cooking UI
        gamePanel.setGameState(GamePanel.COOKING_STATE);
        System.out.println("DEBUG: Opened cooking interface");
        return true;
    }
    
    /**
     * Perform TV watching action when player is near TV and presses 'C' key
     * @return true if TV watching action was performed successfully
     */
    public boolean performWatchTV() {
        System.out.println("DEBUG: performWatchTV called");
        
        // Check if player is near a TV
        if (!isPlayerNearTV()) {
            System.out.println("DEBUG: Player not near TV");
            return false;
        }
        
        // Check if player has enough energy for watching TV (5 energy)
        final int WATCHING_ENERGY_COST = 5;
        if (!hasEnoughEnergy(WATCHING_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy for watching TV");
            return false;
        }
          // Consume energy for watching TV
        consumeEnergy(WATCHING_ENERGY_COST);        // Start watching TV
        tvUI.startWatchingTV();
        
        // Switch game state to TV watching state
        gamePanel.setGameState(GamePanel.TV_STATE);
        
        System.out.println("DEBUG: Started watching TV - consumed " + WATCHING_ENERGY_COST + " energy");
        return true;
    }
    
    /**
     * Get the TV UI instance
     * @return TvUI instance
     */
    public SRC.UI.TvUI getTvUI() {
        return tvUI;
    }
}
