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
    private Map currentMap; // Current active map
    private Map worldMap;   // World map instance
    private Map farmMap;    // Farm map instance

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
        
        // Setup initial objects in maps
        this.worldMap.setupInitialObjects();
        this.farmMap.setupInitialObjects();
        
        // Set world map as the default current map
        this.worldMap.setActive(true);
        this.farmMap.setActive(false);
        this.currentMap = worldMap;
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
    
    public void update() {        player.update(); // Update player's world position
        
        // Check for map transitions
        checkMapTransition();
        
        // Update camera position to follow the player
        // Center the camera on the player's center
        cameraX = player.getWorldX() - screenWidth / 2 + player.getPlayerVisualWidth() / 2;
        cameraY = player.getWorldY() - screenHeight / 2 + player.getPlayerVisualHeight() / 2;
        
        // Clamp camera within world bounds
        if (cameraX < 0) {
            cameraX = 0;
        }
        if (cameraY < 0) {
            cameraY = 0;
        }
        if (cameraX > getMaxWorldWidth() - screenWidth) {
            cameraX = getMaxWorldWidth() - screenWidth;
        }
        if (cameraY > getMaxWorldHeight() - screenHeight) {
            cameraY = getMaxWorldHeight() - screenHeight;
        }
    }
    
    /**
     * Check if player has reached a map transition point
     */    private void checkMapTransition() {
        // Get player's position in tile coordinates
        int playerCol = player.getWorldX() / tileSize;
        // int playerRow = player.getWorldY() / tileSize; // Commented out as currently not used
        
        // If in FarmMap and at the rightmost column
        if (currentMap.getMapName().equals("Farm Map") && playerCol >= FarmMap.FARM_COLS - 1) {
            switchMap(worldMap);
            // Position player at the leftmost side of WorldMap
            player.setWorldX(tileSize * 1); // Position in the second column
            player.setWorldY(player.getWorldY()); // Keep same row position
        }
        // If in WorldMap, check for farm map entrance (can be placed at specific positions)
        else if (currentMap.getMapName().equals("World Map")) {
            // For example, transition to farm if player is at the leftmost column
            if (playerCol == 0) {
                switchMap(farmMap);
                // Position player at the rightmost side of FarmMap minus 2 (to avoid triggering transition immediately)
                player.setWorldX(tileSize * (FarmMap.FARM_COLS - 2));
                player.setWorldY(player.getWorldY()); // Keep same row position
            }
        }
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