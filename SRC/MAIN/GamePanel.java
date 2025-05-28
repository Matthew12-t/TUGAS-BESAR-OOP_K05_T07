package SRC.MAIN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import SRC.ENTITY.Player;
import SRC.ITEMS.Item;
import SRC.MAP.FarmMap;
import SRC.MAP.ForestRiverMap;
import SRC.MAP.Map;
import SRC.MAP.MountainLake;
import SRC.MAP.OceanMap;
import SRC.MAP.StoreMap;
import SRC.MAP.NPC_HOUSE.HouseMap;
import SRC.MAP.NPC_HOUSE.NPCHouseMap;
import SRC.MAP.NPC_HOUSE.AbigailHouseMap;
import SRC.MAP.NPC_HOUSE.CarolineHouseMap;
import SRC.MAP.NPC_HOUSE.DascoHouseMap;
import SRC.MAP.NPC_HOUSE.EmilyHouseMap;
import SRC.MAP.NPC_HOUSE.MayorTadiHouseMap;
import SRC.MAP.NPC_HOUSE.PerryHouseMap;
import SRC.OBJECT.SuperObject;
import SRC.TIME.Time;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;

public class GamePanel extends JPanel implements Runnable {
    
    private final int originalTileSize = 16; // 16x16 tile from source image
    private final int scale = 3;
    private final int tileSize = originalTileSize * scale; // 48x48 tile displayed on screen
    private final int maxScreenCol = 16; // Number of tiles horizontally on screen
    private final int maxScreenRow = 14; // Number of tiles vertically on screen
    private final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    private final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    private final int FPS = 60;
      // Game states
    public static final int PLAY_STATE = 0;
    public static final int MAP_MENU_STATE = 1;
    public static final int INVENTORY_STATE = 2;
    private int gameState = PLAY_STATE;
      // Inventory UI properties
    private BufferedImage inventoryImage;
    
    // Map menu
    private BufferedImage[] mapMenuImages;
    private int currentMapMenuIndex = 0;
    private final int TOTAL_WORLD_MAPS = 10; // Total world map options in menu
    
    // WORLD SETTINGS
    // Default world dimensions (for WorldMap)
    private final int maxWorldCol = 32;
    private final int maxWorldRow = 32;    private boolean isInitializedFarmMap = false; // Flag to check if farm map is initialized
    private boolean isInitializedHouseMap = false; // Flag to check if house map is initialized
    private boolean isInitializedWorldMap = false; // Flag to check if world map is initialized
    private boolean isInitializedMountainLake = false; // Flag to check if mountain lake map is initialized
    private boolean isInitializedOceanMap = false; // Flag to check if ocean map is initialized
    private boolean isInitializedStoreMap = false; // Flag to check if store map is initialized
    
    // NPC House initialization flags
    private boolean isInitializedAbigailHouse = false;
    private boolean isInitializedCarolineHouse = false;
    private boolean isInitializedDascoHouse = false;
    private boolean isInitializedEmilyHouse = false;
    private boolean isInitializedMayorTadiHouse = false;
    private boolean isInitializedPerryHouse = false;
    // SYSTEM
    private KeyHandler keyHandler = new KeyHandler(this);
    private MouseHandler mouseHandler = new MouseHandler(this);
    private Thread gameThread;

    // ENTITY
    private Player player = new Player(this, keyHandler, mouseHandler);    // MAP
     // Current active map
    private Map forestrivermap;   // Forest river map instance
    private Map mountainLake;     // Mountain lake map instance
    private Map oceanMap;         // Ocean map instance
    private Map farmMap;          // Farm map instance
    private Map currentMap;       // Default to farm map
    private Map houseMap;         // House map instance
    private Map storeMap;         // Store map instance
    
    // NPC House Maps
    private Map abigailHouseMap;   // Abigail's house instance
    private Map carolineHouseMap;  // Caroline's house instance
    private Map dascoHouseMap;     // Dasco's house instance
    private Map emilyHouseMap;     // Emily's house instance
    private Map mayorTadiHouseMap; // Mayor Tadi's house instance
    private Map perryHouseMap;     // Perry's house instance
    
    // CAMERA
    private int cameraX = 0; 
    private int cameraY = 0;      // TIME SYSTEM
    private Time time = new Time(6, 0); // Mulai jam 6:00 pagi
    private int day = 1;
    private int month = 1;
    private String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private int dayOfWeek = 0;    
    private long lastTimeUpdate = System.currentTimeMillis();
    private String[] seasons = {"Spring", "Summer", "Fall", "Winter"};
    private int currentSeasonIndex = 0;
    private BufferedImage clockImage; // Image for clock display    // Weather and Season objects
    private Weather currentWeather = Weather.SUNNY;
    private Season currentSeason = Season.SPRING;
      // Inventory constants
    private static final int INVENTORY_ROWS = 4;
    private static final int INVENTORY_COLS = 4;

    // Getters and setters
    public int getTileSize() {
        return tileSize;
    }
    
    public int getOriginalTileSize() {
        return originalTileSize;
    }
    
    public int getScale() {
        return scale;
    }
    
    public int getMaxScreenCol() {
        return maxScreenCol;
    }
    
    public int getMaxScreenRow() {
        return maxScreenRow;
    }
    
    public int getScreenWidth() {
        return screenWidth;
    }
    
    public int getScreenHeight() {
        return screenHeight;
    }
    
    public int getFPS() {
        return FPS;
    }
    
    public int getMaxWorldCol() {
        return maxWorldCol;
    }
    
    public int getMaxWorldRow() {
        return maxWorldRow;
    }    public int getMaxWorldWidth() {
        // Get width based on the active map
        if (currentMap.getMapName().equals("Farm Map")) {
            return tileSize * FarmMap.FARM_COLS;
        } else if (currentMap.getMapName().equals("House Map")) {
            return tileSize * HouseMap.HOUSE_COLS;
        } else if (currentMap.getMapName().equals("Store Map")) {
            return tileSize * StoreMap.STORE_COLS;
        } else {
            return tileSize * maxWorldCol;
        }
    }
    
    public int getMaxWorldHeight() {
        // Get height based on the active map
        if (currentMap.getMapName().equals("Farm Map")) {
            return tileSize * FarmMap.FARM_ROWS;
        } else if (currentMap.getMapName().equals("House Map")) {
            return tileSize * HouseMap.HOUSE_ROWS;
        } else if (currentMap.getMapName().equals("Store Map")) {
            return tileSize * StoreMap.STORE_ROWS;
        } else {
            return tileSize * maxWorldRow;
        }
    }
    
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }
    
    public MouseHandler getMouseHandler() {
        return mouseHandler;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public int getCameraX() {
        return cameraX;
    }
    
    public void setCameraX(int cameraX) {
        this.cameraX = cameraX;
    }
      public int getCameraY() {
        return cameraY;
    }
      public void setCameraY(int cameraY) {
        this.cameraY = cameraY;
    }
    
    /**
     * Get the current active map
     * @return The currently active map
     */
    public Map getCurrentMap() {
        return currentMap;
    }
      /**
     * Switch to world map
     */    
    public void switchToWorldMap() {
        farmMap.setActive(false);
        houseMap.setActive(false);
        forestrivermap.setActive(true);
        this.currentMap = forestrivermap;
    }
    
    /**
     * Switch to farm map
     */    
    public void switchToFarmMap() {
        forestrivermap.setActive(false);
        houseMap.setActive(false);
        farmMap.setActive(true);
        this.currentMap = farmMap;
    }
      /**
     * Switch to house map
     */
    public void switchToHouseMap() {
        forestrivermap.setActive(false);
        farmMap.setActive(false);
        storeMap.setActive(false);
        houseMap.setActive(true);
        this.currentMap = houseMap;
    }
    
    /**
     * Switch to store map
     */    public void switchToStoreMap() {
        forestrivermap.setActive(false);
        farmMap.setActive(false);
        houseMap.setActive(false);
        storeMap.setActive(true);
        this.currentMap = storeMap;
    }
    
    /**
     * Switch to Abigail's house map
     */
    public void switchToAbigailHouse() {
        deactivateAllMaps();
        abigailHouseMap.setActive(true);
        this.currentMap = abigailHouseMap;
    }
    
    /**
     * Switch to Caroline's house map
     */
    public void switchToCarolineHouse() {
        deactivateAllMaps();
        carolineHouseMap.setActive(true);
        this.currentMap = carolineHouseMap;
    }
    
    /**
     * Switch to Dasco's house map
     */
    public void switchToDascoHouse() {
        deactivateAllMaps();
        dascoHouseMap.setActive(true);
        this.currentMap = dascoHouseMap;
    }
    
    /**
     * Switch to Emily's house map
     */
    public void switchToEmilyHouse() {
        deactivateAllMaps();
        emilyHouseMap.setActive(true);
        this.currentMap = emilyHouseMap;
    }
    
    /**
     * Switch to Mayor Tadi's house map
     */
    public void switchToMayorTadiHouse() {
        deactivateAllMaps();
        mayorTadiHouseMap.setActive(true);
        this.currentMap = mayorTadiHouseMap;
    }
    
    /**
     * Switch to Perry's house map
     */
    public void switchToPerryHouse() {
        deactivateAllMaps();
        perryHouseMap.setActive(true);
        this.currentMap = perryHouseMap;
    }
    
    /**
     * Helper method to deactivate all maps
     */
    private void deactivateAllMaps() {
        forestrivermap.setActive(false);
        farmMap.setActive(false);
        houseMap.setActive(false);
        storeMap.setActive(false);
        abigailHouseMap.setActive(false);
        carolineHouseMap.setActive(false);
        dascoHouseMap.setActive(false);
        emilyHouseMap.setActive(false);
        mayorTadiHouseMap.setActive(false);
        perryHouseMap.setActive(false);
        mountainLake.setActive(false);
        oceanMap.setActive(false);
    }
    public void setIsInitializedFarmMap(boolean isInitialized) {
        this.isInitializedFarmMap = isInitialized;
    }
    public boolean isInitializedFarmMap() {
        return isInitializedFarmMap;
    }
    public void setIsInitializedHouseMap(boolean isInitialized) {
        this.isInitializedHouseMap = isInitialized;
    }
    public boolean isInitializedHouseMap() {
        return isInitializedHouseMap;
    }    public void setIsInitializedWorldMap(boolean isInitialized) {
        this.isInitializedWorldMap = isInitialized;
    }
    public boolean isInitializedWorldMap() {
        return isInitializedWorldMap;
    }
      public void setIsInitializedStoreMap(boolean isInitialized) {
        this.isInitializedStoreMap = isInitialized;
    }
    public boolean isInitializedStoreMap() {
        return isInitializedStoreMap;
    }
    
    // NPC House initialization getters and setters
    public void setIsInitializedAbigailHouse(boolean isInitialized) {
        this.isInitializedAbigailHouse = isInitialized;
    }
    public boolean isInitializedAbigailHouse() {
        return isInitializedAbigailHouse;
    }
    
    public void setIsInitializedCarolineHouse(boolean isInitialized) {
        this.isInitializedCarolineHouse = isInitialized;
    }
    public boolean isInitializedCarolineHouse() {
        return isInitializedCarolineHouse;
    }
    
    public void setIsInitializedDascoHouse(boolean isInitialized) {
        this.isInitializedDascoHouse = isInitialized;
    }
    public boolean isInitializedDascoHouse() {
        return isInitializedDascoHouse;
    }
    
    public void setIsInitializedEmilyHouse(boolean isInitialized) {
        this.isInitializedEmilyHouse = isInitialized;
    }
    public boolean isInitializedEmilyHouse() {
        return isInitializedEmilyHouse;
    }
    
    public void setIsInitializedMayorTadiHouse(boolean isInitialized) {
        this.isInitializedMayorTadiHouse = isInitialized;
    }
    public boolean isInitializedMayorTadiHouse() {
        return isInitializedMayorTadiHouse;
    }
    
    public void setIsInitializedPerryHouse(boolean isInitialized) {
        this.isInitializedPerryHouse = isInitialized;
    }
    public boolean isInitializedPerryHouse() {
        return isInitializedPerryHouse;
    }
    /**
     * Get objects from the current map
     * @return Array of objects on the current map
     */
    public SuperObject[] getCurrentObjects() {
        return currentMap.getObjects();
    }
      public GamePanel() {
        // Set the size of the game panel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); 
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true);        // Initialize map menu images
        mapMenuImages = new BufferedImage[TOTAL_WORLD_MAPS];
        loadMapMenuImages();
        
        // Load clock image
        loadClockImage();
          // Load inventory image
        loadInventoryImage();
        
        // Initialize maps
        this.forestrivermap = new ForestRiverMap(this);
        this.mountainLake = new MountainLake(this);
        this.oceanMap = new OceanMap(this);
        this.farmMap = new FarmMap(this);
        this.houseMap = new HouseMap(this);
        this.storeMap = new StoreMap(this);
        
        // Initialize NPC house maps
        this.abigailHouseMap = new AbigailHouseMap(this);
        this.carolineHouseMap = new CarolineHouseMap(this);
        this.dascoHouseMap = new DascoHouseMap(this);
        this.emilyHouseMap = new EmilyHouseMap(this);
        this.mayorTadiHouseMap = new MayorTadiHouseMap(this);
        this.perryHouseMap = new PerryHouseMap(this);
        
        this.currentMap = houseMap;
        setIsInitializedHouseMap(true);// Setup initial objects in maps
        if(currentMap == farmMap){
            this.farmMap.setupInitialObjects();
        }
        else if (currentMap == forestrivermap){
            this.forestrivermap.setupInitialObjects();        }
        else if (currentMap == mountainLake){
            this.mountainLake.setupInitialObjects();
        }
        else if (currentMap == oceanMap){
            this.oceanMap.setupInitialObjects();
        }
        else if (currentMap == houseMap){
            this.houseMap.setupInitialObjects();
        }
        else if (currentMap == storeMap){
            this.storeMap.setupInitialObjects();
        }
        else{
            this.forestrivermap.setupInitialObjects();
        }        // Set house map as the active map (since it's the current map)
        this.farmMap.setActive(false);
        this.forestrivermap.setActive(false);
        this.mountainLake.setActive(false);
        this.oceanMap.setActive(false);
        this.storeMap.setActive(false);
        
        // Set all NPC houses inactive
        this.abigailHouseMap.setActive(false);
        this.carolineHouseMap.setActive(false);
        this.dascoHouseMap.setActive(false);
        this.emilyHouseMap.setActive(false);
        this.mayorTadiHouseMap.setActive(false);
        this.perryHouseMap.setActive(false);
        
        this.houseMap.setActive(true);
    }
    
    /**
     * Load map menu images from resources
     */
    private void loadMapMenuImages() {
        try {
            for (int i = 0; i < TOTAL_WORLD_MAPS; i++) {
                String imagePath = "RES/MAPMENU/worldmap" + (i+1) + ".png";
                mapMenuImages[i] = ImageIO.read(new File(imagePath));
                System.out.println("Loaded map menu image: " + imagePath);
            }        } catch (Exception e) {
            System.err.println("Error loading map menu images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load clock image from resources
     */
    private void loadClockImage() {
        try {
            String imagePath = "RES/OBJECT/clock.png";
            clockImage = ImageIO.read(new File(imagePath));
            System.out.println("Loaded clock image: " + imagePath);
        } catch (Exception e) {
            System.err.println("Error loading clock image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Load the inventory background image
     */
    private void loadInventoryImage() {
        try {
            inventoryImage = ImageIO.read(new File("RES/INVENTORY/inventory.png"));
        } catch (IOException e) {
            System.err.println("Error loading inventory image: " + e.getMessage());
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();    }
    
    @Override
    public void run(){
        double drawInterval = 1000000000.0 / FPS; 
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            // Update time every 1 second (real world)
            if (System.currentTimeMillis() - lastTimeUpdate >= 1000) {
                advanceGameTime();
                lastTimeUpdate = System.currentTimeMillis();
            }

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            // Display FPS
            if(timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }        }
    }
      /**
     * Center the camera on a map with specific dimensions
     * This ensures the map is centered on screen with a black background visible around it
     * @param mapCols Number of columns in the map
     * @param mapRows Number of rows in the map
     */
    public void centerCameraOnMap(int mapCols, int mapRows) {
        // Calculate the total width and height of the map in pixels
        int mapWidth = mapCols * tileSize;
        int mapHeight = mapRows * tileSize;
        
        // Check if the map is smaller than the screen
        if (mapWidth < screenWidth || mapHeight < screenHeight) {
            // Calculate the camera position to center the map on screen
            // For X: (screenWidth - mapWidth) / 2 is the offset needed to center horizontally
            int centerX = Math.max(0, (screenWidth - mapWidth) / 2);
            
            // For Y: (screenHeight - mapHeight) / 2 is the offset needed to center vertically
            int centerY = Math.max(0, (screenHeight - mapHeight) / 2);
            
            // Set camera position
            // We subtract the offset from zero because camera coordinates are negative of the offset
            cameraX = -centerX;
            cameraY = -centerY;
            
            System.out.println("Map centered with camera at: " + cameraX + ", " + cameraY);
        } else {
            // If map is larger than the screen, use default camera centering on player
            cameraX = player.getWorldX() - screenWidth / 2 + player.getPlayerVisualWidth() / 2;
            cameraY = player.getWorldY() - screenHeight / 2 + player.getPlayerVisualHeight() / 2;
            
            // Clamp to map boundaries
            if (cameraX < 0) cameraX = 0;
            if (cameraY < 0) cameraY = 0;
            if (cameraX > mapWidth - screenWidth) cameraX = mapWidth - screenWidth;
            if (cameraY > mapHeight - screenHeight) cameraY = mapHeight - screenHeight;
        }
    }

    /**
     * Teleport player to another map by name, keeping logic transition as before
     * @param targetMapName Nama map tujuan ("Farm Map" atau "World Map")    */          
    public void teleportToMap(String targetMapName) {
        if (currentMap.getMapName().equals(targetMapName)) {
            // Sudah di map tujuan, tidak perlu teleport
            return;
        }
        
        // Reset mouse target to prevent continued walking after teleporting
        mouseHandler.setHasTarget(false);
        
        if (targetMapName.equals("World Map")) {
            switchMap(forestrivermap);
            if (!isInitializedWorldMap) {
                forestrivermap.setupInitialObjects();
                isInitializedWorldMap = true;
            }
            // Pindahkan player ke posisi default masuk dari farm
            player.setWorldX(tileSize * 1); // Kolom ke-2
            // Y tetap
        } else if (targetMapName.equals("Farm Map")) {
            // Jika teleport dari HouseMap, posisikan player di depan rumah
            if (currentMap.getMapName().equals("House Map")) {
                //farmMap.setupInitialObjects();
                switchMap(farmMap);
                if (!isInitializedFarmMap) {
                    farmMap.setupInitialObjects();
                    isInitializedFarmMap = true;
                }
                FarmMap farmMapRef = (FarmMap) farmMap;
                player.setWorldX(tileSize * farmMapRef.getDepanRumahCol());
                player.setWorldY(tileSize * farmMapRef.getDepanRumahRow());
            } else {
                switchMap(farmMap);
                if (!isInitializedFarmMap) {
                    farmMap.setupInitialObjects();
                    isInitializedFarmMap = true;
                }
                // Pindahkan player ke posisi default masuk dari world
                player.setWorldX(tileSize * (FarmMap.FARM_COLS - 4));
                // Y tetap
            }          
        } else if (targetMapName.equals("Forest River Map")) {
            switchMap(forestrivermap);
            if (!isInitializedWorldMap) {
                forestrivermap.setupInitialObjects();
                isInitializedWorldMap = true;
            }
            
            // Position player next to the teleport tile at position (0,2)
            // Teleport tile is at column 0, row 2, so we place player at column 1, row 2
            player.setWorldX(tileSize * 1); // Column 1 (right after teleport column)
            player.setWorldY(tileSize * 2); // Row 2 (same as teleport tile)
            
            // Center camera on map
            centerCameraOnMap(ForestRiverMap.FOREST_COLS, ForestRiverMap.FOREST_ROWS);
        } else if (targetMapName.equals("Mountain Lake")) {
            switchMap(mountainLake);
            if (!isInitializedMountainLake) {
                mountainLake.setupInitialObjects();
                isInitializedMountainLake = true;
            }
            
            // Position player next to the teleport tile at position (0,2)
            player.setWorldX(tileSize * 1); // Column 1 (right after teleport column)
            player.setWorldY(tileSize * 2); // Row 2 (same as teleport tile)
            
            // Center camera on map
            centerCameraOnMap(MountainLake.MOUNTAIN_COLS, MountainLake.MOUNTAIN_ROWS);
        } else if (targetMapName.equals("House Map")) {
            switchMap(houseMap);
            if (!isInitializedHouseMap) {
                houseMap.setupInitialObjects();
                isInitializedHouseMap = true;
            }
            // Pindahkan player ke posisi default di tengah pintu masuk
            int doorStart = (HouseMap.HOUSE_COLS / 2) - 1; // Center of the door
            player.setWorldX(tileSize * doorStart);
            player.setWorldY(tileSize * (HouseMap.HOUSE_ROWS - 5)); // One tile above the door
              // For consistency, also center the camera on the house map
            centerCameraOnMap(HouseMap.HOUSE_COLS, HouseMap.HOUSE_ROWS);        } else if (targetMapName.equals("Store Map")) {
            switchMap(storeMap);
            if (!isInitializedStoreMap) {
                storeMap.setupInitialObjects();
                isInitializedStoreMap = true;
            }
            // Position player at the entrance of the store
            player.setWorldX(tileSize * (StoreMap.STORE_COLS / 2));
            player.setWorldY(tileSize * (StoreMap.STORE_ROWS - 2)); // Near the bottom entrance
            
            // Center camera on the store map
            centerCameraOnMap(StoreMap.STORE_COLS, StoreMap.STORE_ROWS);
        } else if (targetMapName.equals("Abigail's House")) {
            switchToAbigailHouse();
            if (!isInitializedAbigailHouse) {
                abigailHouseMap.setupInitialObjects();
                isInitializedAbigailHouse = true;
            }
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
        } else if (targetMapName.equals("Caroline's House")) {
            switchToCarolineHouse();
            if (!isInitializedCarolineHouse) {
                carolineHouseMap.setupInitialObjects();
                isInitializedCarolineHouse = true;
            }
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);        } 
        else if (targetMapName.equals("Dasco's House")) {
            switchToDascoHouse();
            if (!isInitializedDascoHouse) {
                dascoHouseMap.setupInitialObjects();
                isInitializedDascoHouse = true;
            }
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((DascoHouseMap)dascoHouseMap).ensureNPCsVisible();
        } else if (targetMapName.equals("Emily's House")) {
            switchToEmilyHouse();
            if (!isInitializedEmilyHouse) {
                emilyHouseMap.setupInitialObjects();
                isInitializedEmilyHouse = true;
            }
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
        } else if (targetMapName.equals("Mayor Tadi's House")) {
            switchToMayorTadiHouse();
            if (!isInitializedMayorTadiHouse) {
                mayorTadiHouseMap.setupInitialObjects();
                isInitializedMayorTadiHouse = true;
            }
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
        } else if (targetMapName.equals("Perry's House")) {
            switchToPerryHouse();
            if (!isInitializedPerryHouse) {
                perryHouseMap.setupInitialObjects();
                isInitializedPerryHouse = true;
            }
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((PerryHouseMap)perryHouseMap).ensureNPCsVisible();
        }
    }    
      public void update() {
        // Handle different game states
        if (gameState == PLAY_STATE || gameState == INVENTORY_STATE) {
            // Update player in both PLAY_STATE and INVENTORY_STATE (background still visible)
            if (gameState == PLAY_STATE) {
                player.update(); // Only update position in PLAY_STATE
            }
            // Hitung posisi tengah player
            int playerCenterX = player.getWorldX() + player.getPlayerVisualWidth() / 2;
            int playerCenterY = player.getWorldY() + player.getPlayerVisualHeight() / 2;
    
            // Check for teleport tile berdasarkan posisi tengah player
            int playerCol = playerCenterX / tileSize;
            int playerRow = playerCenterY / tileSize;
            int tileType = currentMap.getTile(playerCol, playerRow);
            if (tileType == 5) { // TILE_TELEPORT value is 5
            if (currentMap.getMapName().equals("Farm Map")) {
                FarmMap farmMapRef = (FarmMap) farmMap;
                // Teleport ke HouseMap jika di bawah rumah
                if (playerCol == farmMapRef.getTeleportToHouseCol() && playerRow == farmMapRef.getTeleportToHouseRow() || playerCol == farmMapRef.getTeleportToHouseCol()+1 &&playerRow == farmMapRef.getTeleportToHouseRow()) {
                    teleportToMap("House Map");
                } else if (playerCol == FarmMap.FARM_COLS - 1) {
                    // Instead of teleporting directly, enter the map menu state
                    enterMapMenuState();
                }
            } else if (currentMap.getMapName().equals("World Map")) {
                teleportToMap("Farm Map");
            } else if (currentMap.getMapName().equals("House Map")) {
                // When stepping on teleport tile in house, go to farm map
                teleportToMap("Farm Map");
            } else if (currentMap.getMapName().equals("Forest River Map")) {
                if (playerCol == 0) { // Jika di kolom paling kiri
                    teleportToMap("Farm Map");
                }
            }
        }        // Update camera position based on map type
        if (currentMap.getMapName().equals("Forest River Map")) {
            // For the ForestRiverMap, maintain the centered position
            centerCameraOnMap(ForestRiverMap.FOREST_COLS, ForestRiverMap.FOREST_ROWS);
        } else if (currentMap.getMapName().equals("Mountain Lake")) {
            // For the MountainLake, maintain the centered position
            centerCameraOnMap(MountainLake.MOUNTAIN_COLS, MountainLake.MOUNTAIN_ROWS);
        } else if (currentMap.getMapName().equals("Ocean Map")) {
            // For the OceanMap, maintain the centered position
            centerCameraOnMap(OceanMap.OCEAN_COLS, OceanMap.OCEAN_ROWS);        } else if (currentMap.getMapName().equals("Store Map")) {
            // For the StoreMap, maintain the centered position
            centerCameraOnMap(StoreMap.STORE_COLS, StoreMap.STORE_ROWS);
        } else if (currentMap.getMapName().equals("Abigail's House") ||
                   currentMap.getMapName().equals("Caroline's House") ||
                   currentMap.getMapName().equals("Dasco's House") ||
                   currentMap.getMapName().equals("Emily's House") ||
                   currentMap.getMapName().equals("Mayor Tadi's House") ||
                   currentMap.getMapName().equals("Perry's House")) {
            // For NPC houses, maintain the centered position
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
        } else {
            // For other maps, camera follows the player
            cameraX = player.getWorldX() - screenWidth / 2 + player.getPlayerVisualWidth() / 2;
            cameraY = player.getWorldY() - screenHeight / 2 + player.getPlayerVisualHeight() / 2;
            if (cameraX < 0) cameraX = 0;
            if (cameraY < 0) cameraY = 0;
            if (cameraX > getMaxWorldWidth() - screenWidth) cameraX = getMaxWorldWidth() - screenWidth;
            if (cameraY > getMaxWorldHeight() - screenHeight) cameraY = getMaxWorldHeight() - screenHeight;
        }
        }
        // MAP_MENU_STATE doesn't need update logic as it's controlled by key inputs
    }
    
    /**
     * Switch to a different map
     * @param newMap The map to switch to
     */
    public void switchMap(Map newMap) {
        // Deactivate current map
        currentMap.setActive(false);
        
        // Set new map as active and current
        newMap.setActive(true);
        currentMap = newMap;
        
        System.out.println("Switched to map: " + newMap.getMapName());
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (gameState == PLAY_STATE) {
            // Set the background to black (will be visible around small maps)
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            // Draw the current map (tiles and objects)
            currentMap.draw(g2);
            // --- Draw Player ---
            // Calculate player's screen position (relative to camera)
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            // Draw the player at their screen position
            player.draw(g2, playerScreenX, playerScreenY);
            // --- Draw Time Info ---
            drawTimeInfo(g2);
        } else if (gameState == INVENTORY_STATE) {
            // First draw the game world in the background
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            currentMap.draw(g2);
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            player.draw(g2, playerScreenX, playerScreenY);
            
            // Then draw inventory overlay
            drawInventoryScreen(g2);
        } else if (gameState == MAP_MENU_STATE) {
            // Draw map selection menu
            if (mapMenuImages != null && currentMapMenuIndex >= 0 && currentMapMenuIndex < mapMenuImages.length) {
                BufferedImage currentImage = mapMenuImages[currentMapMenuIndex];
                if (currentImage != null) {
                    // Center the map image on the screen with scaling
                    // Scale factor - make the image 80% of screen width
                    double scaleFactor = (double)(screenWidth * 0.4) / currentImage.getWidth();
                    int scaledWidth = (int)(currentImage.getWidth() * scaleFactor);
                    int scaledHeight = (int)(currentImage.getHeight() * scaleFactor);
                    
                    int x = (screenWidth - scaledWidth) / 2;
                    int y = (screenHeight - scaledHeight) / 2;
                    
                    // Draw larger scaled image
                    g2.drawImage(currentImage, x, y, scaledWidth, scaledHeight, null);
                    
                }
                else {
                    g2.setColor(Color.RED);
                    g2.drawString("Error loading map image", screenWidth/2 - 80, screenHeight/2);
                }
            }
        }
        
        g2.dispose(); // Release system resources
    }
      /**
     * Enter map menu mode when player teleports from Farm Map
     */
    public void enterMapMenuState() {
        System.out.println("Entering map menu state");
        this.gameState = MAP_MENU_STATE;
        this.currentMapMenuIndex = 0;
        
        // Debug info
        System.out.println("Game state is now: " + (gameState == PLAY_STATE ? "PLAY_STATE" : "MAP_MENU_STATE"));
        
        // Force an immediate repaint to show the menu
        repaint();
    }    /**
     * Exit map menu mode and enter the selected map
     */    public void exitMapMenuState() {
        System.out.println("Exiting map menu state");
        
        // Save the selected map index before changing state
        int selectedMap = currentMapMenuIndex;
        
        // Change state back to play
        this.gameState = PLAY_STATE;
        
        // Load the selected map (worldmap1, worldmap2, etc)
        System.out.println("Selected map: worldmap" + (selectedMap + 1));
        
        // Choose map based on the selection
        if (selectedMap == 0) {
            // Switch to forest map (first option)
            switchMap(forestrivermap);
            if (!isInitializedWorldMap) {
                forestrivermap.setupInitialObjects();
                isInitializedWorldMap = true;
            }
            
            // Position player next to the teleport tile at position (0,2)
            player.setWorldX(tileSize * 1); // Column 1 (right after teleport column)
            player.setWorldY(tileSize * 2); // Row 2 (next to teleport tile)
            
            System.out.println("Teleported player to ForestRiverMap at position (1, 2)");
            
            // Center camera on map
            centerCameraOnMap(ForestRiverMap.FOREST_COLS, ForestRiverMap.FOREST_ROWS);        }
        else if (selectedMap == 1) {
            // Switch to mountain lake map (second option)
            switchMap(mountainLake);
            if (!isInitializedMountainLake) {
                mountainLake.setupInitialObjects();
                isInitializedMountainLake = true;
            }
            
            // Position player next to the teleport tile at position (0,2)
            player.setWorldX(tileSize * 1); // Column 1 (right after teleport column)
            player.setWorldY(tileSize * 2); // Row 2 (next to teleport tile)
            
            System.out.println("Teleported player to Mountain Lake at position (1, 2)");
            
            // Center camera on map
            centerCameraOnMap(MountainLake.MOUNTAIN_COLS, MountainLake.MOUNTAIN_ROWS);        }
        else if (selectedMap == 2) {
            // Switch to ocean map (third option)
            switchMap(oceanMap);
            if (!isInitializedOceanMap) {
                oceanMap.setupInitialObjects();
                isInitializedOceanMap = true;
            }
            
            // Position player next to the teleport tile at position (0,2)
            player.setWorldX(tileSize * 1); // Column 1 (right after teleport column)
            player.setWorldY(tileSize * 2); // Row 2 (next to teleport tile)
            
            System.out.println("Teleported player to Ocean Map at position (1, 2)");
            
            // Center camera on map
            centerCameraOnMap(OceanMap.OCEAN_COLS, OceanMap.OCEAN_ROWS);
        }
        else if (selectedMap == 3) {
            // Switch to store map (fourth option)
            switchMap(storeMap);
            if (!isInitializedStoreMap) {
                storeMap.setupInitialObjects();
                isInitializedStoreMap = true;
            }
            
            // Position player at the entrance of the store
            player.setWorldX(tileSize * (StoreMap.STORE_COLS / 2)); // Center horizontally
            player.setWorldY(tileSize * (StoreMap.STORE_ROWS - 2)); // Near the bottom entrance
            
            System.out.println("Teleported player to Store Map at entrance");
              // Center camera on map
            centerCameraOnMap(StoreMap.STORE_COLS, StoreMap.STORE_ROWS);
        }
        else if (selectedMap == 4) {
            // Switch to Abigail's house (fifth option)
            switchToAbigailHouse();
            if (!isInitializedAbigailHouse) {
                abigailHouseMap.setupInitialObjects();
                isInitializedAbigailHouse = true;
            }
            
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Abigail's House at entrance");
            
            // Center camera on map
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
        }
        else if (selectedMap == 5) {
            // Switch to Caroline's house (sixth option)
            switchToCarolineHouse();
            if (!isInitializedCarolineHouse) {
                carolineHouseMap.setupInitialObjects();
                isInitializedCarolineHouse = true;
            }
            
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Caroline's House at entrance");
            
            // Center camera on map
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
        }
        else if (selectedMap == 6) {
            // Switch to Dasco's house (seventh option)
            switchToDascoHouse();
            if (!isInitializedDascoHouse) {
                dascoHouseMap.setupInitialObjects();
                isInitializedDascoHouse = true;
            }
            
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Dasco's House at entrance");
            
            // Center camera after the map and NPCs are initialized
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            
            // Ensure NPCs are visible after camera centering
            ((DascoHouseMap)dascoHouseMap).ensureNPCsVisible();
        }
        else if (selectedMap == 7) {
            // Switch to Emily's house (eighth option)
            switchToEmilyHouse();
            if (!isInitializedEmilyHouse) {
                emilyHouseMap.setupInitialObjects();
                isInitializedEmilyHouse = true;
            }
            
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Emily's House at entrance");
            
            // Center camera on map
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
        }
        else if (selectedMap == 8) {
            // Switch to Mayor Tadi's house (ninth option)
            switchToMayorTadiHouse();
            if (!isInitializedMayorTadiHouse) {
                mayorTadiHouseMap.setupInitialObjects();
                isInitializedMayorTadiHouse = true;
            }
            
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Mayor Tadi's House at entrance");
            
            // Center camera on map
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);

        }
        else if (selectedMap == 9) {
            // Switch to Perry's house (tenth option)
            switchToPerryHouse();
            if (!isInitializedPerryHouse) {
                perryHouseMap.setupInitialObjects();
                isInitializedPerryHouse = true;
            }
            
            // Position player at the entrance
            player.setWorldX(tileSize * (NPCHouseMap.NPC_HOUSE_COLS / 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Perry's House at entrance");
            
            // Center camera on map
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((PerryHouseMap)perryHouseMap).ensureNPCsVisible();
        }
        else {
            // Default to forest map for other options
            switchMap(forestrivermap);
            if (!isInitializedWorldMap) {
                forestrivermap.setupInitialObjects();
                isInitializedWorldMap = true;
            }
            
            // Position player next to the teleport tile
            player.setWorldX(tileSize * 1);
            player.setWorldY(tileSize * 2);
            
            // Center camera on map
            centerCameraOnMap(ForestRiverMap.FOREST_COLS, ForestRiverMap.FOREST_ROWS);
        }
        
        // Force repaint to update the screen immediately
        repaint();
    }
      /**
     * Navigate to the previous map in the menu
     */
    public void selectPreviousMap() {
        currentMapMenuIndex--;
        if (currentMapMenuIndex < 0) {
            currentMapMenuIndex = TOTAL_WORLD_MAPS - 1; // Wrap around to the last map
        }
        System.out.println("Selected map index: " + (currentMapMenuIndex + 1));
        // Force repaint to show new selection immediately
        repaint();
    }
    
    /**
     * Navigate to the next map in the menu
     */
    public void selectNextMap() {
        currentMapMenuIndex++;
        if (currentMapMenuIndex >= TOTAL_WORLD_MAPS) {
            currentMapMenuIndex = 0; // Wrap around to the first map
        }
        System.out.println("Selected map index: " + (currentMapMenuIndex + 1));
        // Force repaint to show new selection immediately
        repaint();
    }
      /**
     * Get the current game state
     * @return Current game state (PLAY_STATE or MAP_MENU_STATE)
     */
    public int getGameState() {
        return gameState;
    }    /**
     * Set the current game state
     * @param gameState The game state to set
     */
    public void setGameState(int gameState) {
        this.gameState = gameState;
    }
      // Tambahkan method advanceGameTime
    private void advanceGameTime() {
        // Don't advance time if it's paused (during fishing)
        if (isTimePaused) {
            return;
        }
        
        // Tambah 5 menit setiap detik
        int minute = time.getMinute() + 5;
        int hour = time.getHour();
        if (minute >= 60) {
            minute -= 60;
            hour++;
        }        if (hour >= 24) {
            hour = 0;
            minute = 0;
            day++;
            dayOfWeek = (dayOfWeek + 1) % 7;
            
            // Update weather randomly each day
            currentWeather = (Math.random() < 0.3) ? Weather.RAINY : Weather.SUNNY;
            
            if ((day - 1) % 10 == 0 && day != 1) {
                currentSeasonIndex = (currentSeasonIndex + 1) % seasons.length;
                // Update current season based on index
                Season[] seasonValues = Season.values();
                if (currentSeasonIndex < seasonValues.length) {
                    currentSeason = seasonValues[currentSeasonIndex];
                } else {
                    currentSeasonIndex = 0;
                    currentSeason = Season.SPRING;
                }
            }
            
            if (day > 30) {
                day = 1;
                month++;
                if (month > 12) {
                    month = 1;
                    // Year progression could be tracked here if needed
                }
                currentSeason = Season.SPRING;
                currentSeasonIndex = 0;
                currentWeather = Weather.SUNNY;
            }
        }
        time.setHour(hour);
        time.setMinute(minute);
    }    private void drawTimeInfo(Graphics2D g2) {
        String hari = dayNames[dayOfWeek];
        String tanggal = String.format("%02d", day); 
        String musim = currentSeason.getDisplayName(); 
        String cuaca = currentWeather.getDisplayName(); // Get current weather string
        int hour24 = time.getHour();
        int hour12 = hour24 % 12;
        if (hour12 == 0) hour12 = 12;
        String ampm = (hour24 < 12 || hour24 == 24) ? "AM" : "PM";
        String jam = String.format("%02d:%02d %s", hour12, time.getMinute(), ampm);
        
        if (clockImage != null) {
            int clockWidth = 128;
            int clockHeight = 128;
            int clockX = screenWidth - clockWidth - 10; 
            int clockY = 10; 
            g2.drawImage(clockImage, clockX, clockY, clockWidth, clockHeight, null);
            
            // Draw time text centered on the clock
            int textX = clockX + (clockWidth / 2) - 15;
            int textY = clockY + (clockHeight / 2) + 5;
            g2.setColor(Color.BLACK);
            java.awt.Font originalFont = g2.getFont();
            java.awt.Font boldFont = originalFont.deriveFont(java.awt.Font.BOLD, originalFont.getSize() + 2);
            g2.setFont(boldFont);
            g2.drawString(hari + ", " + tanggal, textX, textY - 43); 
            g2.drawString(jam, textX, textY);
            g2.setFont(originalFont);        
        } else {
            // Fallback to original method if image failed to load
            g2.setColor(new Color(0,0,0,180));
            g2.fillRoundRect(screenWidth-120, 10, 110, 100, 15, 15); // Made taller to fit season and weather
            g2.setColor(Color.BLACK);
            g2.drawString(hari + ", " + tanggal, screenWidth-110, 30);
            g2.drawString(jam, screenWidth-110, 55); 
            g2.drawString(musim, screenWidth-110, 80); // Added season display
            g2.drawString(cuaca, screenWidth-110, 105); // Added weather display
        }    }    public Season getSeason() {
        return currentSeason;
    }

    public String getCurrentSeasonName() {
        return currentSeason.getDisplayName();
    }

    public Weather getWeather() {
        return currentWeather;
    }

    public boolean isRainy() {
        return currentWeather == Weather.RAINY;
    }
    

    public boolean isSunny() {
        return currentWeather == Weather.SUNNY;
    }

    public String getWeatherString() {
        return currentWeather.getDisplayName();
    }
    
    /**
     * Draws the inventory screen
     * @param g2 Graphics2D object to draw with
     */
    private void drawInventoryScreen(Graphics2D g2) {
        // Draw semi-transparent black overlay across entire screen
        g2.setColor(new Color(0, 0, 0, 180)); // Black with 70% opacity
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        // Calculate inventory position to center it on screen
        int inventoryWidth = 128 * scale; // 128 pixels * 3 = 384 pixels
        int inventoryHeight = 128 * scale; // 128 pixels * 3 = 384 pixels
        int x = (screenWidth - inventoryWidth) / 2;
        int y = (screenHeight - inventoryHeight) / 2;
        
        // Draw inventory background
        if (inventoryImage != null) {
            g2.drawImage(inventoryImage, x, y, inventoryWidth, inventoryHeight, null);
        } else {
            // Fallback if image fails to load
            g2.setColor(new Color(139, 69, 19, 200)); // Brown with transparency
            g2.fillRoundRect(x, y, inventoryWidth, inventoryHeight, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(x, y, inventoryWidth, inventoryHeight, 15, 15);
        }
        
        // Draw grid for item slots (4x4 grid)
        int slotSize = 32 * scale; // 32x32 pixel slots * scale
        int slotSpacing = 0; // No spacing between slots
        int slotOffsetX = 0; // Offset from left edge of inventory
        int slotOffsetY = 0; // Offset from top edge of inventory
        
        // Get selected slot index from mouse handler
        int selectedSlotIndex = mouseHandler.getSelectedSlotIndex();
        
        // Draw item slots and items
        for (int row = 0; row < INVENTORY_ROWS; row++) {
            for (int col = 0; col < INVENTORY_COLS; col++) {
                // Calculate slot position
                int slotX = x + slotOffsetX + col * (slotSize + slotSpacing);
                int slotY = y + slotOffsetY + row * (slotSize + slotSpacing);
                
                // Calculate slot index (0-15)
                int slotIndex = row * INVENTORY_COLS + col;
                
                // Only draw slot background if selected (remove black grid)
                if (slotIndex == selectedSlotIndex) {
                    g2.setColor(new Color(255, 255, 100, 180)); // Yellow highlight for selected slot
                    g2.fillRect(slotX, slotY, slotSize, slotSize);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(slotX, slotY, slotSize, slotSize);
                }
                  // Draw item in slot if it exists
                Item[] playerItems = player.getInventoryItems();
                int[] playerQuantities = player.getInventoryQuantities();
                if (slotIndex < playerItems.length && playerItems[slotIndex] != null) {
                    Item item = playerItems[slotIndex];
                    int quantity = playerQuantities[slotIndex];
                    
                    // Draw item image centered in slot with bigger scale
                    if (item.getImage() != null) {
                        // Scale item images to be bigger (1.5x the original size)
                        int scaledWidth = (int)(item.getImage().getWidth() * 1.5);
                        int scaledHeight = (int)(item.getImage().getHeight() * 1.5);
                        int itemX = slotX + (slotSize - scaledWidth) / 2;
                        int itemY = slotY + (slotSize - scaledHeight) / 2;
                        g2.drawImage(item.getImage(), itemX, itemY, scaledWidth, scaledHeight, null);
                    }
                    
                    // Draw item quantity
                    if (quantity > 1) {
                        g2.setColor(Color.WHITE);
                        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12));
                        String quantityText = "x" + quantity;
                        g2.drawString(quantityText, slotX + slotSize - 25, slotY + slotSize - 5);
                    }
                }
            }
        }
        
        // Draw inventory title centered above the inventory
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24));
        String titleText = "Inventory";
        int titleWidth = g2.getFontMetrics().stringWidth(titleText);
        int titleX = x + (inventoryWidth - titleWidth) / 2;
        g2.drawString(titleText, titleX, y - 15);
          // Draw remove button on the right side of inventory (wider button)
        int removeButtonWidth = 80; // Increased width from 60 to 80
        int removeButtonHeight = 30;
        int removeButtonX = x + inventoryWidth + 10;
        int removeButtonY = y + 20;
        
        // Draw remove button background
        g2.setColor(new Color(200, 60, 60)); // Red button
        g2.fillRoundRect(removeButtonX, removeButtonY, removeButtonWidth, removeButtonHeight, 10, 10);
        
        // Draw remove button border
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(removeButtonX, removeButtonY, removeButtonWidth, removeButtonHeight, 10, 10);
        
        // Draw remove button text centered in button
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14));
        String removeButtonText = "Remove";
        int removeButtonTextWidth = g2.getFontMetrics().stringWidth(removeButtonText);
        int removeButtonTextX = removeButtonX + (removeButtonWidth - removeButtonTextWidth) / 2;
        g2.drawString(removeButtonText, removeButtonTextX, removeButtonY + 20);
        
        // Draw use button below remove button
        int useButtonWidth = 80;
        int useButtonHeight = 30;
        int useButtonX = x + inventoryWidth + 10;
        int useButtonY = removeButtonY + removeButtonHeight + 10; // 10px gap below remove button
        
        // Draw use button background
        g2.setColor(new Color(60, 200, 60)); // Green button
        g2.fillRoundRect(useButtonX, useButtonY, useButtonWidth, useButtonHeight, 10, 10);
        
        // Draw use button border
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(useButtonX, useButtonY, useButtonWidth, useButtonHeight, 10, 10);
        
        // Draw use button text centered in button
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14));        String useButtonText = "Use";
        int useButtonTextWidth = g2.getFontMetrics().stringWidth(useButtonText);
        int useButtonTextX = useButtonX + (useButtonWidth - useButtonTextWidth) / 2;        g2.drawString(useButtonText, useButtonTextX, useButtonY + 20);
    }
    
    // Time management for fishing system
    private boolean isTimePaused = false;
    
    /**
     * Pause the game time during fishing
     */
    public void pauseTime() {
        isTimePaused = true;
    }
    
    /**
     * Resume the game time after fishing
     */
    public void resumeTime() {
        isTimePaused = false;
    }
    
    /**
     * Add specific minutes to the game time
     * @param minutes Minutes to add
     */
    public void addGameTime(int minutes) {
        int currentMinute = time.getMinute() + minutes;
        int currentHour = time.getHour();
        
        if (currentMinute >= 60) {
            currentHour += currentMinute / 60;
            currentMinute = currentMinute % 60;
        }
        
        if (currentHour >= 24) {
            currentHour = currentHour % 24;
            // Could also advance day here if needed
        }
        
        time.setHour(currentHour);
        time.setMinute(currentMinute);
    }
    
    /**
     * Get current time object
     * @return Current Time object
     */
    public Time getCurrentTime() {
        return time;
    }
}