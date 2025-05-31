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

public class PlayerAction {
    private GamePanel gamePanel;
    private Player player;    private Inventory inventory;
    private FishingUI fishingUI; 
    private SleepUI sleepUI; 
    private SRC.UI.TvUI tvUI; 
    
    
    private static final int MAX_ENERGY = 100;
      
    private static final int LOW_ENERGY_THRESHOLD = -20;
    
    
    private String lastBedMapName = "House Map"; 
    private int lastBedX = 5; 
    private int lastBedY = 3; 
      
    private Store<Item> store;
      public PlayerAction(GamePanel gamePanel, Player player) {
        this.gamePanel = gamePanel;
        this.player = player;
        this.inventory = new Inventory();
        this.fishingUI = new FishingUI(gamePanel); 
        this.sleepUI = new SleepUI(gamePanel); 
        this.tvUI = new SRC.UI.TvUI(gamePanel); 
        
          
        initializeStore();
    }    private void initializeStore() {
        store = new Store<>("General Store");
        System.out.println("Store system initialized");
    }
    
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
    
        public void removeOneInventoryItem(int slotIndex) {
        inventory.removeOneItem(slotIndex);
        System.out.println("Removed one item from slot " + slotIndex);
    }
    
        public void removeInventoryItem(int slotIndex) {
        inventory.removeItem(slotIndex);
        System.out.println("Removed item from slot " + slotIndex);
    }
          public Item[] getInventoryItems() {
        return inventory.getItems();
    }
    
        public int[] getInventoryQuantities() {
        return inventory.getQuantities();
    }
    
        public void addItemToInventory(Item item) {
        inventory.addItem(item, 1);
    }
    
        public Inventory getInventory() {
        return inventory;
    }
        public void handleMovement() {
         
    }
    
        public void handleInteraction(int worldX, int worldY) {
        
        System.out.println("Player interaction at: " + worldX + ", " + worldY);
    }
          public void holdTool(String toolName) {
        
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

        public void holdSeed(String seedName) {
        
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
    
        public boolean useHeldTool() {
        if (!player.isHoldingAnyTool()) {
            System.out.println("No tool is currently held");
            return false;
        }
        
        SRC.ITEMS.Tool heldTool = player.getCurrentHoldingTool();
        String toolName = heldTool.getName();
        
        
        int energyCost = getToolEnergyCost(toolName);
        if (!hasEnoughEnergy(energyCost)) {
            System.out.println("Not enough energy to use " + toolName);
            return false;
        }
        
        
        consumeEnergy(energyCost);
        System.out.println("Used " + toolName + " (Energy cost: " + energyCost + ")");
        return true;
    }

        private int getToolEnergyCost(String toolName) {
        switch (toolName) {
            case "Fishing Rod":
                return 5; 
            case "Hoe":
                return 3; 
            case "Watering Can":
                return 2; 
            case "Scythe":
                return 5; 
            case "Pickaxe":
                return 6; 
            default:
        return 3; 
        }
    }

        public void dropHeldTool() {
        if (player.isHoldingAnyTool()) {
            SRC.ITEMS.Tool droppedTool = player.getCurrentHoldingTool();
            player.setCurrentHoldingTool(null);
            System.out.println("Dropped: " + droppedTool.getName());
        } else {
            System.out.println("No tool is currently held");
        }
    }

        public void dropHeldSeed() {
        if (player.isHoldingAnySeed()) {
            SRC.ITEMS.Seed droppedSeed = player.getCurrentHoldingSeed();
            player.setCurrentHoldingSeed(null);
            System.out.println("Dropped seed: " + droppedSeed.getName());
        } else {
            System.out.println("No seed is currently held");
        }
    }        public void useItem(String itemName, String itemCategory) {
        System.out.println("Using item: " + itemName + " (Category: " + itemCategory + ")");
        
        
        switch (itemCategory.toLowerCase()) {
            case "tool":
                
                holdTool(itemName);
                break;
            case "seed":
                
                holdSeed(itemName);
                break;
            case "food":
                
                System.out.println("Eating " + itemName);
                break;
            default:
                System.out.println("Cannot use item of category: " + itemCategory);
                break;
        }
    }
          private void useSeeds(String seedName) {
        System.out.println("Preparing to plant: " + seedName);
        
        
    }

        public boolean performTilling() {
        System.out.println("DEBUG: performTilling called");
        
        
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Tilling only available in Farm Map");
            return false;
        }
        
        
        if (!player.isHolding("Hoe")) {
            System.out.println("DEBUG: Player must be holding a Hoe to till land");
            return false;
        }
        
        
        final int TILLING_ENERGY_COST = 5;
        if (!hasEnoughEnergy(TILLING_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy for tilling");
            return false;
        }
        
        
        int tileSize = gamePanel.getTileSize();        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
          
        if (tileManager.tillTile(playerCol, playerRow)) {
            
            consumeEnergy(TILLING_ENERGY_COST);
            addGameTime(5); 
            System.out.println("DEBUG: Successfully tilled land at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to till land at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }

        public boolean performLandRecovery() {
        System.out.println("DEBUG: performLandRecovery called");
        
        
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Land recovery only available in Farm Map");
            return false;
        }
        
        
        if (!player.isHolding("Pickaxe")) {
            System.out.println("DEBUG: Player must be holding a Pickaxe to recover land");
            return false;
        }
        
        
        final int LAND_RECOVERY_ENERGY_COST = 5;
        if (!hasEnoughEnergy(LAND_RECOVERY_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy for land recovery");
            return false;
        }
        
        
        int tileSize = gamePanel.getTileSize();        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
          
        if (tileManager.recoverLand(playerCol, playerRow)) {
            
            consumeEnergy(LAND_RECOVERY_ENERGY_COST);
            addGameTime(5); 
            System.out.println("DEBUG: Successfully recovered land at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to recover land at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }        public boolean performFishing() {
        System.out.println("DEBUG: performFishing called");
        
        
        stopPlayerMovement();
        
        
        final int FISHING_ENERGY_COST = 5;
        if (!hasEnoughEnergy(FISHING_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy");
            fishingUI.showInsufficientEnergy();
            return false;
        }
        
        
        if (!hasValidFishingRod()) {
            System.out.println("DEBUG: No fishing rod");
            fishingUI.showMissingFishingRod();
            return false;
        }
        
        
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        
        SRC.MAP.Map currentMap = gamePanel.getCurrentMap();
        if (!currentMap.getMapController().isOnFishableWater(currentMap, playerCol, playerRow) && 
            !currentMap.getMapController().isAdjacentToFishableWater(currentMap, playerCol, playerRow)) {
            System.out.println("DEBUG: Not on fishable water");
            fishingUI.showInvalidLocation();
            return false;
        }
        
        
        String fishingLocation = determineFishingLocation(currentMap.getMapName());
        if (fishingLocation == null) {
            System.out.println("DEBUG: Invalid fishing location");
            fishingUI.showInvalidLocation();
            return false;
        }
          System.out.println("DEBUG: All checks passed, starting fishing mini-game");
        
        
        fishingUI.showFishingAttempt();
        
        
        pauseGameTime();
        
        
        
        
        
        String caughtFish = performFishingWithMiniGame(fishingLocation);
        
        
        addGameTime(15);
        
        
        resumeGameTime();
          if (caughtFish != null) {
            
            SRC.ITEMS.Fish fishItem = SRC.DATA.FishData.getFish(caughtFish);
            if (fishItem != null) {
                inventory.addItem(fishItem, 1);
                fishingUI.showFishingResult(caughtFish, true);
                
                
                player.incrementFishCaught(fishItem);
            }
        } else {
            fishingUI.showFishingResult("", false);
        }
        
        return true;
    }    private String performFishingWithMiniGame(String fishingLocation) {
        System.out.println("DEBUG: performFishingWithMiniGame called with location=" + fishingLocation);        
        Season currentSeason = gamePanel.getSeason();
        Weather currentWeather = gamePanel.getWeather();
        GameTime currentTime = new GameTime(
            gamePanel.getCurrentTime().getHour(), 
            gamePanel.getCurrentTime().getMinute()
        );
        
        System.out.println("DEBUG: Current conditions - Season: " + currentSeason + ", Weather: " + currentWeather + ", Time: " + currentTime.getHour() + ":" + currentTime.getMinute());
        
        
        java.util.Map<String, SRC.ITEMS.Fish> catchableFishMap = SRC.DATA.FishData.getCatchableFish(
            fishingLocation, currentTime, currentSeason, currentWeather
        );
        
        System.out.println("DEBUG: Found " + catchableFishMap.size() + " catchable fish");
        
        if (catchableFishMap.isEmpty()) {
            System.out.println("DEBUG: No fish available at this location/time/weather");
            return null; 
        }
          
        java.util.List<SRC.ITEMS.Fish> catchableFish = new java.util.ArrayList<>(catchableFishMap.values());
        java.util.Random random = new java.util.Random();
        SRC.ITEMS.Fish selectedFish = catchableFish.get(random.nextInt(catchableFish.size()));
        
        System.out.println("DEBUG: Selected fish: " + selectedFish.getName() + " (Type: " + selectedFish.getType() + ")");
        
        
        boolean success = playIntegratedMiniGame(selectedFish.getType());
        
        System.out.println("DEBUG: Mini-game result: " + (success ? "SUCCESS" : "FAILED"));
        
        return success ? selectedFish.getName() : null;
    }
          private boolean playIntegratedMiniGame(String fishType) {
        System.out.println("DEBUG: playIntegratedMiniGame called with fishType=" + fishType);
        
        int maxRange;
        int maxAttempts;
        
        
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
        
        
        int targetNumber = (int) (Math.random() * maxRange) + 1;
        
        System.out.println("DEBUG: About to call fishingUI.playGUIMiniGame with targetNumber=" + targetNumber);
        
        
        return fishingUI.playGUIMiniGame(fishType, targetNumber, maxRange, maxAttempts);
    }
    
        private String determineFishingLocation(String mapName) {
        switch (mapName) {
            case "Forest River Map":
                return "Forest River";
            case "Mountain Lake":
                return "Mountain Lake";
            case "Ocean Map":
                return "Ocean";
            case "Farm Map":
                return "Pond"; 
            default:
                return null;
        }
    }
          private void stopPlayerMovement() {
        
        gamePanel.getKeyHandler().upPressed = false;
        gamePanel.getKeyHandler().downPressed = false;
        gamePanel.getKeyHandler().leftPressed = false;
        gamePanel.getKeyHandler().rightPressed = false;
        
        
        gamePanel.getMouseHandler().setHasTarget(false);
        
        System.out.println("DEBUG: Player movement stopped for action");
    }        private boolean hasValidFishingRod() {
        return player.isHolding("Fishing Rod") || player.isHolding("Fiberglass Rod");
    }
    
        private void pauseGameTime() {
        
        
        gamePanel.pauseTime();
    }
    
        private void resumeGameTime() {
        
        gamePanel.resumeTime();
    }
    
        private void addGameTime(int minutes) {
        
        gamePanel.addGameTime(minutes);
    }
    
              public int getCurrentEnergy() {
        return player.getEnergy();
    }
    
        public int getMaxEnergy() {
        return MAX_ENERGY;
    }        public boolean consumeEnergy(int amount) {
        return player.consumeEnergy(amount);
    }          public void restoreEnergy(int amount) {
        player.restoreEnergy(amount);
    }
          public void setEnergy(int energy) {
        player.setEnergy(energy);
    }        public boolean hasEnoughEnergy(int requiredEnergy) {
        return hasEnoughEnergyForAction(requiredEnergy);
    }
    
        private boolean hasEnoughEnergyForAction(int energyCost) {
        int currentEnergy = player.getCurrentEnergy();
        int lowerBound = player.getLowerEnergyBound(); 
        
        
        boolean canPerformAction = (currentEnergy - energyCost) > -21;
        
        System.out.println("DEBUG: Energy Check - Current: " + currentEnergy + 
                          ", Cost: " + energyCost + 
                          ", After Action: " + (currentEnergy - energyCost) + 
                          ", Lower Bound: " + lowerBound + 
                          ", Can Perform: " + canPerformAction);
        
        return canPerformAction;
    }
          public double getEnergyPercentage() {
        return player.getEnergyPercentage();
    }
          public void update() {
        fishingUI.update();
        sleepUI.update();
        tvUI.update();
    }

        public void draw(java.awt.Graphics2D g2) {
        fishingUI.draw(g2);
        sleepUI.draw(g2);
        tvUI.draw(g2);
    }
    
        public FishingUI getFishingUI() {
        return fishingUI;
    }        private int findFirstEdibleItem() {
        Item[] items = player.getInventoryItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i] instanceof SRC.ITEMS.Edible) {
                return i;
            }
        }
        return -1; 
    }

        public void eatSelectedItem() {
        
        int selectedSlotIndex = gamePanel.getMouseHandler().getSelectedSlotIndex();
        
        
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
          
        if (!(selectedItem instanceof SRC.ITEMS.Edible)) {
            System.out.println(selectedItem.getName() + " is not edible!");
            return;
        }
        SRC.ITEMS.Edible edibleItem = (SRC.ITEMS.Edible) selectedItem;
          
        int energyGained = edibleItem.getEnergyValue();
        System.out.println("DEBUG: Item " + selectedItem.getName() + " gives " + energyGained + " energy");
        
        
        int currentEnergy = player.getEnergy();
        System.out.println("DEBUG: Player energy before eating: " + currentEnergy);        
        int newEnergy = currentEnergy + energyGained;
        if (newEnergy > 100) newEnergy = 100; 
        player.setEnergy(newEnergy);
        
        System.out.println("DEBUG: Player energy after eating: " + player.getEnergy());
        System.out.println("You consumed " + selectedItem.getName() + " and gained " + energyGained + " energy!");
        
        
        addGameTime(5);
        
        if (quantities[selectedSlotIndex] > 1) {
            quantities[selectedSlotIndex]--;
        } else {
            player.removeItemFromInventory(selectedSlotIndex);
            
            gamePanel.getMouseHandler().setSelectedSlotIndex(-1);
        }
    }
    
        public boolean performSleep() {
        System.out.println("DEBUG: performSleep called");
        
        
        if (!isPlayerNearBed()) {
            System.out.println("DEBUG: Player not near bed");
            return false;
        }
        
        
        executeSleep(SleepUI.SleepTrigger.MANUAL);
        return true;
    }
    
        public void checkAutomaticSleep() {
        
        if (player.getEnergy() <= LOW_ENERGY_THRESHOLD) {
            System.out.println("DEBUG: Auto sleep triggered - Low energy");
            executeSleep(SleepUI.SleepTrigger.LOW_ENERGY);
            return;
        }
        Time currentTime = gamePanel.getCurrentTime();
        int hour = currentTime.getHour();
        if (hour >= 2 && hour <= 5) {
            System.out.println("DEBUG: Auto sleep triggered - Late night (Hour: " + hour + ")");
            executeSleep(SleepUI.SleepTrigger.LATE_TIME);
            return;
        }
   
    }        private void executeSleep(SleepUI.SleepTrigger trigger) {
        System.out.println("DEBUG: Executing sleep with trigger: " + trigger);
        
        
        stopPlayerMovement();
        
        
        transportPlayerToHouseBed();
        
        
        int shippingBinValue = gamePanel.getShippingBin().calculateTotalValue();
        
        
        int currentDay = gamePanel.getCurrentDay();
        Season currentSeason = gamePanel.getSeason();
        Weather currentWeather = gamePanel.getWeather();
        
        
        SleepUI.SleepResult sleepResult;
        if (shippingBinValue > 0) {
            sleepResult = SleepUI.createSleepResultWithShipping(
                trigger, currentDay, currentSeason, currentWeather, shippingBinValue);
        } else {
            sleepResult = SleepUI.createSleepResult(trigger, currentDay, currentSeason, currentWeather);
        }
        
        
        performSleepEffects(trigger);
        
        
        sleepUI.showSleepResult(sleepResult);
        
        
        gamePanel.setGameState(GamePanel.SLEEP_STATE);
    }        public SleepUI getSleepUI() {
        return this.sleepUI;
    }    public boolean isPlayerNearBed() {
        SRC.MAP.Map currentMap = gamePanel.getCurrentMap();
        
        
        if (currentMap != null && 
            !(currentMap instanceof HouseMap)) {
            System.out.println("DEBUG: Player is not in a house map, cannot sleep");
            return false; 
        }
        
        
        int tileSize = gamePanel.getTileSize();        
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        
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
    
        public boolean isPlayerNearTV(){
        SRC.MAP.Map currentMap = gamePanel.getCurrentMap();
        
        
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        
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
    }        private void performSleepEffects(SleepUI.SleepTrigger trigger) {
        
        int currentEnergy = player.getCurrentEnergy();
        int newEnergy;
        
        if (currentEnergy <= 0) {
            
            newEnergy = 10;
        } else if (currentEnergy < (MAX_ENERGY * 0.1)) {
            
            newEnergy = MAX_ENERGY / 2;
        } else {
            
            newEnergy = MAX_ENERGY;
        }
        
        player.setEnergy(newEnergy);
        System.out.println("DEBUG: Energy restored from " + currentEnergy + " to " + newEnergy);
        
        
        Time currentTime = gamePanel.getCurrentTime();
        currentTime.setHour(10);  
        currentTime.setMinute(0); 
        
        
        int shippingBinValue = gamePanel.getShippingBin().calculateTotalValue();
        if (shippingBinValue > 0) {
            
            player.addGold(shippingBinValue);
            
            
            gamePanel.getShippingBin().clearAllItems();
            
            System.out.println("DEBUG: Shipping bin processed - Value: " + shippingBinValue + " gold added");
        } else {
            
            System.out.println("DEBUG: No shipping bin items - no income received");
        }
        
        
        gamePanel.advanceToNextDay();
        
        currentTime.setHour(10);
        currentTime.setMinute(0);
        
        System.out.println("DEBUG: Sleep effects applied - Energy restored, time set to 10:00 AM, advanced to next day");
    }    private void transportPlayerToHouseBed() {
        
        if (!gamePanel.getCurrentMap().getMapName().equals(lastBedMapName)) {
            
            if (lastBedMapName.equals("House Map")) {
                gamePanel.switchToHouseMap();
            } else {
                
                
                
                
                    System.out.println("DEBUG: Could not switch to " + lastBedMapName + ", falling back to House Map");
                    gamePanel.switchToHouseMap();
                    lastBedMapName = "House Map";
                    lastBedX = 5;
                    lastBedY = 3;
                
            }
        }
        
          
        player.setWorldX(player.getWorldX()); 
        player.setWorldY(player.getWorldY() );
    }
    
        public boolean isPlayerNearShippingBin() {
        int playerCol = (player.getWorldX() + player.getSolidArea().x) / gamePanel.getTileSize();
        int playerRow = (player.getWorldY() + player.getSolidArea().y) / gamePanel.getTileSize();
        
        
        for (int checkRow = playerRow - 1; checkRow <= playerRow + 1; checkRow++) {
            for (int checkCol = playerCol - 1; checkCol <= playerCol + 1; checkCol++) {
                if (checkRow >= 0 && checkRow < gamePanel.getMaxWorldRow() && 
                    checkCol >= 0 && checkCol < gamePanel.getMaxWorldCol()) {
                    
                    
                    SuperObject[] objects = gamePanel.getCurrentObjects();
                    for (int i = 0; i < objects.length; i++) {
                        if (objects[i] != null && 
                            objects[i].getName().equals("Shipping Bin")) {
                            
                            int objCol = objects[i].getWorldX() / gamePanel.getTileSize();
                            int objRow = objects[i].getWorldY() / gamePanel.getTileSize();
                            
                            
                            if (objRow <= checkRow && checkRow < objRow + 2 && 
                                objCol <= checkCol && checkCol < objCol + 3) { 
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
        public void openShippingBin() {
        if (isPlayerNearShippingBin()) {
            gamePanel.setGameState(GamePanel.SHIPPING_STATE);
            System.out.println("Opened shipping bin");
        } else {
            System.out.println("No shipping bin nearby");
        }
    }
          
        public void openStore() {
        gamePanel.setGameState(GamePanel.STORE_STATE);
        System.out.println("Opened store");
    }
    
        public void closeStore() {
        gamePanel.setGameState(GamePanel.PLAY_STATE);
        System.out.println("Closed store");
    }
    
        public Store<Item> getStore() {
        return store;
    }
    
        public boolean isPlayerNearStore() {
        
        return gamePanel.getCurrentMap().getMapName().equals("Store Map");
    }
    
        public boolean performPlanting() {
        System.out.println("DEBUG: performPlanting called");
        
        
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Planting only available in Farm Map");
            return false;
        }
        
        
        String heldSeedName = getHeldSeedName();
        if (heldSeedName == null) {
            System.out.println("DEBUG: Player must be holding a seed to plant");
            return false;
        }
        
        
        final int PLANTING_ENERGY_COST = 5;
        if (!hasEnoughEnergy(PLANTING_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy for planting");
            return false;
        }
        
        
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        
        int daysToGrow = getSeedGrowthDays(heldSeedName);        if (daysToGrow == -1) {
            System.out.println("DEBUG: Invalid seed data for " + heldSeedName);
            return false;
        }
        
        
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
          
        if (tileManager.plantSeed(playerCol, playerRow, heldSeedName, daysToGrow)) {
            
            consumeEnergy(PLANTING_ENERGY_COST);
            addGameTime(5); 
            
            
            removeSeedFromInventory(heldSeedName);
            
            System.out.println("DEBUG: Successfully planted " + heldSeedName + " at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to plant " + heldSeedName + " at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }        public String getHeldSeedName() {
        
        if (player.isHoldingAnySeed()) {
            return player.getCurrentHoldingSeed().getName();
        }
        
        
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
          
        return null; 
    }
    
        private int getSeedGrowthDays(String seedName) {
        try {
            
            SRC.ITEMS.Seed seed = SRC.DATA.SeedData.getSeed(seedName);
            if (seed != null) {
                return seed.getDaysToHarvest();
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Error accessing seed data for " + seedName + ": " + e.getMessage());
        }
        return -1; 
    }
          private void removeSeedFromInventory(String seedName) {
        Item[] items = player.getInventoryItems();
        int[] quantities = player.getInventoryQuantities();
        
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getName().equals(seedName)) {
                if (quantities[i] > 1) {
                    quantities[i]--; 
                } else {
                    player.removeItemFromInventory(i); 
                    
                    if (player.isHoldingSeed(seedName)) {
                        player.setCurrentHoldingSeed(null);
                    }
                    
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

        public boolean performHarvesting() {
        System.out.println("DEBUG: performHarvesting called");
        
        
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Harvesting only available in Farm Map");
            return false;
        }
        
        
        int tileSize = gamePanel.getTileSize();        
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
        
        
        if (!tileManager.hasPlantAt(playerCol, playerRow)) {
            System.out.println("DEBUG: No plant at this location");
            return false;
        }
          if (!tileManager.isPlantReadyToHarvest(playerCol, playerRow)) {
            System.out.println("DEBUG: Plant is not ready to harvest yet");
            return false;
        }
          
        String cropName = tileManager.harvestCrop(playerCol, playerRow);
        System.out.println("DEBUG: TileManager.harvestCrop() returned: '" + cropName + "'");
        
        if (cropName != null) {
            
            try {                System.out.println("DEBUG: Attempting to get crop data for: '" + cropName + "'");
                SRC.ITEMS.Crop crop = SRC.DATA.CropData.getCrop(cropName);
                
                if (crop != null) {
                    int harvestQuantity = crop.getCropPerHarvest();
                    int totalEnergyCost = harvestQuantity * 5; 
                    
                    System.out.println("DEBUG: Crop data found - Name: '" + crop.getName() + 
                                     "', Harvest Quantity: " + harvestQuantity + 
                                     ", Total Energy Cost: " + totalEnergyCost);
                    
                    
                    if (!hasEnoughEnergy(totalEnergyCost)) {
                        System.out.println("DEBUG: Not enough energy to harvest " + harvestQuantity + 
                                         " crops (need " + totalEnergyCost + " energy)");
                        return false;                    }
                    
                    
                    System.out.println("DEBUG: Inventory before harvest:");
                    printInventoryDebug();
                    
                    
                    for (int i = 0; i < harvestQuantity; i++) {
                        
                        inventory.addItem(crop, 1);
                        System.out.println("DEBUG: Added crop #" + (i+1) + " to inventory directly");
                    }
                    
                    
                    System.out.println("DEBUG: Inventory after harvest:");
                    printInventoryDebug();
                    
                    
                    player.incrementCropsHarvested(harvestQuantity);
                    
                    
                    consumeEnergy(totalEnergyCost);
                    
                    System.out.println("DEBUG: Successfully harvested " + harvestQuantity + "x " + cropName + 
                                     " and consumed " + totalEnergyCost + " energy");
                } else {
                    System.out.println("DEBUG: ERROR - Could not find crop data for '" + cropName + "'");
                    System.out.println("DEBUG: Available crops in CropData:");
                    
                    try {
                        java.util.Map<String, SRC.ITEMS.Crop> allCrops = SRC.DATA.CropData.getAllCrops();
                        for (String key : allCrops.keySet()) {
                            System.out.println("DEBUG:   - '" + key + "'");
                        }
                    } catch (Exception e2) {
                        System.out.println("DEBUG: Error accessing CropData: " + e2.getMessage());
                    }                    return false;
                }            } catch (Exception e) {
                System.out.println("DEBUG: Exception in harvest processing: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
            
            addGameTime(5); 
            
            System.out.println("DEBUG: Successfully harvested " + cropName + " at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to harvest crop at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }
    
        private void printInventoryDebug() {
        Item[] items = inventory.getItems();
        int[] quantities = inventory.getQuantities();
        
        System.out.println("=== INVENTORY DEBUG ===");
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                System.out.println("Slot " + i + ": " + items[i].getName() + " x" + quantities[i]);
            }
        }
        System.out.println("=====================");
    }
    
        public boolean performWatering() {
        System.out.println("DEBUG: performWatering called");
        
        
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Watering only available in Farm Map");
            return false;
        }
        
        
        if (!player.isHolding("Watering Can")) {
            System.out.println("DEBUG: Player must be holding a Watering Can to water plants");
            return false;
        }
          
        if (!hasEnoughEnergy(5)) {
            System.out.println("DEBUG: Not enough energy to water plants");
            return false;
        }
        
        
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
          
        SRC.TILES.TileManager tileManager = gamePanel.getTileManager();
        
        
        if (!tileManager.hasPlantAt(playerCol, playerRow)) {
            System.out.println("DEBUG: No plant at this location to water");
            return false;
        }
        
        
        if (tileManager.isPlantWateredToday(playerCol, playerRow)) {
            System.out.println("DEBUG: Plant is already watered today");
            return false;
        }
        
        
        boolean success = tileManager.waterPlant(playerCol, playerRow);        if (success) {
            
            consumeEnergy(5);
            
            
            addGameTime(5);
            
            System.out.println("DEBUG: Successfully watered plant at (" + playerCol + ", " + playerRow + ")");
            return true;
        } else {
            System.out.println("DEBUG: Failed to water plant at (" + playerCol + ", " + playerRow + ")");
            return false;
        }
    }
    
        public boolean isPlayerNearStove() {
        int playerCol = (player.getWorldX() + player.getSolidArea().x) / gamePanel.getTileSize();
        int playerRow = (player.getWorldY() + player.getSolidArea().y) / gamePanel.getTileSize();
        
        
        for (int checkRow = playerRow - 1; checkRow <= playerRow + 1; checkRow++) {
            for (int checkCol = playerCol - 1; checkCol <= playerCol + 1; checkCol++) {
                if (checkRow >= 0 && checkRow < gamePanel.getMaxWorldRow() && 
                    checkCol >= 0 && checkCol < gamePanel.getMaxWorldCol()) {
                    
                    
                    SuperObject[] objects = gamePanel.getCurrentObjects();
                    for (int i = 0; i < objects.length; i++) {
                        if (objects[i] != null && 
                            objects[i].getName().equals("stove")) {
                            
                            int objCol = objects[i].getWorldX() / gamePanel.getTileSize();
                            int objRow = objects[i].getWorldY() / gamePanel.getTileSize();
                            
                            
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
    
        public boolean performCooking() {
        System.out.println("DEBUG: performCooking called");
        
        
        if (!isPlayerNearStove()) {
            System.out.println("DEBUG: Player not near stove");
            return false;
        }
        
        
        gamePanel.setGameState(GamePanel.COOKING_STATE);
        System.out.println("DEBUG: Opened cooking interface");
        return true;
    }
    
        public boolean performWatchTV() {
        System.out.println("DEBUG: performWatchTV called");
        
        
        if (!isPlayerNearTV()) {
            System.out.println("DEBUG: Player not near TV");
            return false;
        }
        
        
        final int WATCHING_ENERGY_COST = 5;
        if (!hasEnoughEnergy(WATCHING_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy for watching TV");
            return false;
        }
          
        consumeEnergy(WATCHING_ENERGY_COST);        
        tvUI.startWatchingTV();
        
        
        gamePanel.setGameState(GamePanel.TV_STATE);
        
        System.out.println("DEBUG: Started watching TV - consumed " + WATCHING_ENERGY_COST + " energy");
        return true;
    }
    
        public SRC.UI.TvUI getTvUI() {
        return tvUI;
    }
    
        public boolean performMarriage() {
        System.out.println("DEBUG: performMarriage called");
        
        
        if (!player.isHolding("Proposal Ring")) {
            System.out.println("DEBUG: Player must be holding a Proposal Ring to propose");
            return false;
        }
        
        
        final int MARRIAGE_ENERGY_COST = 80;
        if (!hasEnoughEnergy(MARRIAGE_ENERGY_COST)) {
            System.out.println("DEBUG: Not enough energy for marriage proposal");
            System.out.println("DEBUG: Marriage requires " + MARRIAGE_ENERGY_COST + " energy, current energy: " + player.getEnergy());
            return false;
        }
        
        
        if (!isPlayerNearEligibleNPC()) {
            System.out.println("DEBUG: No eligible NPC nearby for marriage");
            return false;
        }
        
        
        consumeEnergy(MARRIAGE_ENERGY_COST);
        addGameTime(30); 
        
        
        removeProposalRingFromInventory();
        
        System.out.println("DEBUG: Successfully performed marriage - consumed " + MARRIAGE_ENERGY_COST + " energy");
        System.out.println("DEBUG: Congratulations on your marriage!");
        return true;
    }
    
        private boolean isPlayerNearEligibleNPC() {
        
        int tileSize = gamePanel.getTileSize();
        int playerCol = (player.getWorldX() + player.getPlayerVisualWidth() / 2) / tileSize;
        int playerRow = (player.getWorldY() + player.getPlayerVisualHeight() / 2) / tileSize;
        
        
        
        SRC.MAP.Map currentMap = gamePanel.getCurrentMap();
        if (currentMap != null && currentMap.getMapName().contains("House")) {
            System.out.println("DEBUG: Player is in a house map, eligible for marriage proposal");
            return true;
        }
        
        return false;
    }
    
        private void removeProposalRingFromInventory() {
        Item[] items = player.getInventoryItems();
        int[] quantities = player.getInventoryQuantities();
        
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getName().equals("Proposal Ring")) {
                if (quantities[i] > 1) {
                    quantities[i]--; 
                } else {
                    player.removeItemFromInventory(i); 
                    
                    if (player.isHolding("Proposal Ring")) {
                        player.setCurrentHoldingTool(null);
                    }
                    
                    if (gamePanel.getMouseHandler().getSelectedSlotIndex() == i) {
                        gamePanel.getMouseHandler().setSelectedSlotIndex(-1);
                    }
                }
                System.out.println("DEBUG: Removed Proposal Ring from inventory");
                return;
            }
        }
        System.out.println("DEBUG: Could not find Proposal Ring in inventory to remove");
    }
}
