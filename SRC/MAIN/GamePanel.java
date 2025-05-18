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
        } else {
            return tileSize * maxWorldCol;
        }
    }
    
    public int getMaxWorldHeight() {
        // Get height based on the active map
        if (currentMap.getMapName().equals("Farm Map")) {
            return tileSize * FarmMap.FARM_ROWS;
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
        worldMap.setActive(true);
        this.currentMap = worldMap;
    }
    
    /**
     * Switch to farm map
     */
    public void switchToFarmMap() {
        worldMap.setActive(false);
        farmMap.setActive(true);
        this.currentMap = farmMap;    }
    
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
        this.currentMap = farmMap;
        
        // Setup initial objects in maps
        this.worldMap.setupInitialObjects();
        this.farmMap.setupInitialObjects();
        
        // Set world map as the default current map
        this.farmMap.setActive(true);
        this.worldMap.setActive(false);
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
     */
    public void teleportToMap(String targetMapName) {
        if (currentMap.getMapName().equals(targetMapName)) {
            // Sudah di map tujuan, tidak perlu teleport
            return;
        }
        if (targetMapName.equals("World Map")) {
            switchMap(worldMap);
            // Pindahkan player ke posisi default masuk dari farm
            player.setWorldX(tileSize * 1); // Kolom ke-2
            // Y tetap
        } else if (targetMapName.equals("Farm Map")) {
            switchMap(farmMap);
            // Pindahkan player ke posisi default masuk dari world
            player.setWorldX(tileSize * (FarmMap.FARM_COLS - 4));
            // Y tetap
        }
    }

    public void update() {
        player.update(); // Update player's world position
        // Check for teleport tile
        int playerCol = player.getWorldX() / tileSize;
        int playerRow = player.getWorldY() / tileSize;
        int tileType = currentMap.getTile(playerCol, playerRow);
        if (tileType == SRC.TILES.Tile.TILE_TELEPORT) {
            if (currentMap.getMapName().equals("Farm Map")) {
                teleportToMap("World Map");
            } else if (currentMap.getMapName().equals("World Map")) {
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