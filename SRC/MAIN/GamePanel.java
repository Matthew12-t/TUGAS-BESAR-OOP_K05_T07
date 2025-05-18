package SRC.MAIN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import SRC.ENTITY.Player;
import SRC.MAP.FarmMap;
import SRC.MAP.Map;
import SRC.MAP.WorldMap;
import SRC.MAP.HouseMap;
import SRC.OBJECT.SuperObject;

public class GamePanel extends JPanel implements Runnable {
    
    private final int originalTileSize = 16; // 16x16 tile from source image
    private final int scale = 3;
    private final int tileSize = originalTileSize * scale; // 48x48 tile displayed on screen
    private final int maxScreenCol = 16; // Number of tiles horizontally on screen
    private final int maxScreenRow = 12; // Number of tiles vertically on screen
    private final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    private final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    private final int FPS = 60;    // WORLD SETTINGS
    // Default world dimensions (for WorldMap)
    private final int maxWorldCol = 32;
    private final int maxWorldRow = 32;
    private boolean isInitializedFarmMap = false; // Flag to check if farm map is initialized
    private boolean isInitializedHouseMap = false; // Flag to check if house map is initialized
    private boolean isInitializedWorldMap = false; // Flag to check if world map is initialized
    // SYSTEM
    private KeyHandler keyHandler = new KeyHandler(this);
    private MouseHandler mouseHandler = new MouseHandler(this);
    private Thread gameThread;

    // ENTITY
    private Player player = new Player(this, keyHandler, mouseHandler);
      // MAP
     // Current active map
    private Map worldMap;   // World map instance
    private Map farmMap;    // Farm map instance
    private Map currentMap; // Default to farm map
    private Map houseMap;   // House map instance

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
     */    public void switchToWorldMap() {
        farmMap.setActive(false);
        houseMap.setActive(false);
        worldMap.setActive(true);
        this.currentMap = worldMap;
    }
    
    /**
     * Switch to farm map
     */    public void switchToFarmMap() {
        worldMap.setActive(false);
        houseMap.setActive(false);
        farmMap.setActive(true);
        this.currentMap = farmMap;
    }
    
    /**
     * Switch to house map
     */
    public void switchToHouseMap() {
        worldMap.setActive(false);
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

          // Initialize maps
        this.worldMap = new WorldMap(this);
        this.farmMap = new FarmMap(this);
        this.houseMap = new HouseMap(this);
        this.currentMap = houseMap;
        setIsInitializedHouseMap(true);
        
        // Setup initial objects in maps
        if(currentMap == farmMap){
            this.farmMap.setupInitialObjects();
        }
        else if (currentMap == worldMap){
            this.worldMap.setupInitialObjects();
        }
        else if (currentMap == houseMap){
            this.houseMap.setupInitialObjects();
        }
        else{
            this.worldMap.setupInitialObjects();
        }
          // Set house map as the active map (since it's the current map)
        this.farmMap.setActive(false);
        this.worldMap.setActive(false);
        this.houseMap.setActive(true);
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
     * Teleport player to another map by name, keeping logic transition as before
     * @param targetMapName Nama map tujuan ("Farm Map" atau "World Map")
     */    public void teleportToMap(String targetMapName) {
        if (currentMap.getMapName().equals(targetMapName)) {
            // Sudah di map tujuan, tidak perlu teleport
            return;
        }
        if (targetMapName.equals("World Map")) {
            switchMap(worldMap);
            if (!isInitializedWorldMap) {
                worldMap.setupInitialObjects();
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
        }
    }

    public void update() {
        player.update(); // Update player's world position
        // Hitung posisi tengah player
        int playerCenterX = player.getWorldX() + player.getPlayerVisualWidth() / 2;
        int playerCenterY = player.getWorldY() + player.getPlayerVisualHeight() / 2;

        // Check for teleport tile berdasarkan posisi tengah player
        int playerCol = playerCenterX / tileSize;
        int playerRow = playerCenterY / tileSize;
        int tileType = currentMap.getTile(playerCol, playerRow);
        if (tileType == SRC.TILES.Tile.TILE_TELEPORT) {
            if (currentMap.getMapName().equals("Farm Map")) {
                FarmMap farmMapRef = (FarmMap) farmMap;
                // Teleport ke HouseMap jika di bawah rumah
                if (playerCol == farmMapRef.getTeleportToHouseCol() && playerRow == farmMapRef.getTeleportToHouseRow()) {
                    teleportToMap("House Map");
                } else if (playerCol == FarmMap.FARM_COLS - 1) {
                    teleportToMap("World Map");
                }
            } else if (currentMap.getMapName().equals("World Map")) {
                teleportToMap("Farm Map");
            } else if (currentMap.getMapName().equals("House Map")) {
                // When stepping on teleport tile in house, go to farm map
                teleportToMap("Farm Map");
            }
        }
        // Update camera position to follow the player
        cameraX = player.getWorldX() - screenWidth / 2 + player.getPlayerVisualWidth() / 2;
        cameraY = player.getWorldY() - screenHeight / 2 + player.getPlayerVisualHeight() / 2;
        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > getMaxWorldWidth() - screenWidth) cameraX = getMaxWorldWidth() - screenWidth;
        if (cameraY > getMaxWorldHeight() - screenHeight) cameraY = getMaxWorldHeight() - screenHeight;
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

        // Draw the current map (tiles and objects)
        currentMap.draw(g2);

        // --- Draw Player ---
        // Calculate player's screen position (relative to camera)
        int playerScreenX = player.getWorldX() - cameraX;
        int playerScreenY = player.getWorldY() - cameraY;

        // Draw the player at their screen position
        player.draw(g2, playerScreenX, playerScreenY); 
        
        g2.dispose(); // Release system resources
    }
}