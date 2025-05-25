package SRC.MAIN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import SRC.ENTITY.Player;
import SRC.MAP.FarmMap;
import SRC.MAP.ForestRiverMap;
import SRC.MAP.Map;
import SRC.MAP.MountainLake;
import SRC.MAP.WorldMap;
import SRC.MAP.HouseMap;
import SRC.OBJECT.SuperObject;

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
    private int gameState = PLAY_STATE;
    
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
    // SYSTEM
    private KeyHandler keyHandler = new KeyHandler(this);
    private MouseHandler mouseHandler = new MouseHandler(this);
    private Thread gameThread;

    // ENTITY
    private Player player = new Player(this, keyHandler, mouseHandler);
    // MAP
     // Current active map
    private Map forestrivermap;   // Forest river map instance
    private Map mountainLake;     // Mountain lake map instance
    private Map farmMap;          // Farm map instance
    private Map currentMap;       // Default to farm map
    private Map houseMap;         // House map instance

    // CAMERA
    private int cameraX = 0; 
    private int cameraY = 0;
    
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
    }
    public int getMaxWorldWidth() {
        // Get width based on the active map
        if (currentMap.getMapName().equals("Farm Map")) {
            return tileSize * FarmMap.FARM_COLS;
        } else if (currentMap.getMapName().equals("House Map")) {
            return tileSize * HouseMap.HOUSE_COLS;
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
        houseMap.setActive(true);
        this.currentMap = houseMap;
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
    }
    public void setIsInitializedWorldMap(boolean isInitialized) {
        this.isInitializedWorldMap = isInitialized;
    }
    public boolean isInitializedWorldMap() {
        return isInitializedWorldMap;
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
        this.setFocusable(true);

        // Initialize map menu images
        mapMenuImages = new BufferedImage[TOTAL_WORLD_MAPS];
        loadMapMenuImages();        // Initialize maps
        this.forestrivermap = new ForestRiverMap(this);
        this.mountainLake = new MountainLake(this);
        this.farmMap = new FarmMap(this);
        this.houseMap = new HouseMap(this);
        this.currentMap = houseMap;
        setIsInitializedHouseMap(true);
          // Setup initial objects in maps
        if(currentMap == farmMap){
            this.farmMap.setupInitialObjects();
        }
        else if (currentMap == forestrivermap){
            this.forestrivermap.setupInitialObjects();
        }
        else if (currentMap == mountainLake){
            this.mountainLake.setupInitialObjects();
        }
        else if (currentMap == houseMap){
            this.houseMap.setupInitialObjects();
        }
        else{
            this.forestrivermap.setupInitialObjects();
        }
          // Set house map as the active map (since it's the current map)
        this.farmMap.setActive(false);
        this.forestrivermap.setActive(false);
        this.mountainLake.setActive(false);
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
            }
        } catch (Exception e) {
            System.err.println("Error loading map menu images: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();    }
    
    @Override
    public void run(){
        double drawInterval = 1000000000.0 / FPS; // Time in nanoseconds per frame
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

            if (delta >= 1) {
                // 1. UPDATE: update information such as character positions
                update();
                // 2. DRAW: draw the screen with the updated information
                repaint(); // Calls paintComponent method
                delta--;
                drawCount++;
            }

            // Display FPS
            if(timer >= 1000000000) {
                System.out.println("FPS: " + drawCount); // Show FPS in console
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
     * @param targetMapName Nama map tujuan ("Farm Map" atau "World Map")    */          public void teleportToMap(String targetMapName) {
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
            centerCameraOnMap(HouseMap.HOUSE_COLS, HouseMap.HOUSE_ROWS);
        }
    }    
    
    public void update() {
        // Handle different game states
        if (gameState == PLAY_STATE) {
            // Only update player in PLAY_STATE
            player.update(); // Update player's world position
            // Hitung posisi tengah player
            int playerCenterX = player.getWorldX() + player.getPlayerVisualWidth() / 2;
            int playerCenterY = player.getWorldY() + player.getPlayerVisualHeight() / 2;
    
            // Check for teleport tile berdasarkan posisi tengah player
            int playerCol = playerCenterX / tileSize;
            int playerRow = playerCenterY / tileSize;
            int tileType = currentMap.getTile(playerCol, playerRow);if (tileType == SRC.TILES.Tile.TILE_TELEPORT) {
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
        }else if (gameState == MAP_MENU_STATE) {
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
            centerCameraOnMap(ForestRiverMap.FOREST_COLS, ForestRiverMap.FOREST_ROWS);
        }
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
            centerCameraOnMap(MountainLake.MOUNTAIN_COLS, MountainLake.MOUNTAIN_ROWS);
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
    }
}