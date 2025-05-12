package SRC.MAIN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import SRC.ENTITY.Player;
import SRC.TILE.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    final int originalTileSize = 16; // 16x16 tile from source image
    final int scale = 3;
    final public int tileSize = originalTileSize * scale; // 48x48 tile displayed on screen
    final int maxScreenCol = 16; // Number of tiles horizontally on screen
    final int maxScreenRow = 12; // Number of tiles vertically on screen
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    final int FPS = 60;

    // WORLD SETTINGS
    public final int maxWorldCol = 32;
    public final int maxWorldRow = 32;
    public final int maxWorldWidth = tileSize * maxWorldCol; // 32 * 48 = 1536 pixels
    public final int maxWorldHeight = tileSize * maxWorldRow; // 32 * 48 = 1536 pixels

    // SYSTEM
    KeyHandler keyHandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler(this);
    Thread gameThread;
      // TILE
    public TileManager tileManager = new TileManager(this);

    // ENTITY
    Player player = new Player(this, keyHandler, mouseHandler);

    // CAMERA
    public int cameraX = 0; 
    public int cameraY = 0;

    public GamePanel() {
        // Set the size of the game panel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); 
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true); 
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

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

            // Optional: Display FPS
            if(timer >= 1000000000) {
                // System.out.println("FPS: " + drawCount); // Uncomment to see FPS in console
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update(); // Update player's world position

        // Update camera position to follow the player
        // Center the camera on the player's center
        cameraX = player.worldX - screenWidth / 2 + player.solidArea.width / 2;
        cameraY = player.worldY - screenHeight / 2 + player.solidArea.height / 2;

        // Clamp camera within world bounds
        if (cameraX < 0) {
            cameraX = 0;
        }
        if (cameraY < 0) {
            cameraY = 0;
        }
        if (cameraX > maxWorldWidth - screenWidth) {
            cameraX = maxWorldWidth - screenWidth;
        }
        if (cameraY > maxWorldHeight - screenHeight) {
            cameraY = maxWorldHeight - screenHeight;
        }
    }    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw the map using our TileManager
        tileManager.draw(g2, this);
        
        // --- Draw Player ---
        // Calculate player's screen position (relative to camera)
        int playerScreenX = player.worldX - cameraX;
        int playerScreenY = player.worldY - cameraY;

        // Draw the player at their screen position
        player.draw(g2, playerScreenX, playerScreenY); 
        
        g2.dispose(); // Release system resources
    }
    public int getscreenWidth() {
        return screenWidth;
    }
    public int getscreenHeight() {
        return screenHeight;
    }
}