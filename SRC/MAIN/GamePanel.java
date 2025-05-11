package SRC.MAIN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import SRC.ENTITY.Player;

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

    // ENTITY
    Player player = new Player(this, keyHandler, mouseHandler);

    // CAMERA
    public int cameraX = 0; 
    public int cameraY = 0;

    // RESOURCES 
    Image grassTile;

    public GamePanel() {
        // Set the size of the game panel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); 
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true); 

        // Load resources        
        try {
             grassTile = ImageIO.read(getClass().getResourceAsStream("/RES/TILE/grass.png"));
                if (grassTile == null) {
                 grassTile = ImageIO.read(new File("RES/TILE/grass.png"));
             }
        } catch (IOException e) {
             System.err.println("Error loading grass tile image: " + e.getMessage());
             e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Resource not found: /RES/TILE/grass.png. Check the path and if it's included in classpath.");
            e.printStackTrace();
        }

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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // --- Draw Map (Tiles) ---
        // Determine the range of tiles to draw based on camera position
        int startCol = cameraX / tileSize;
        int startRow = cameraY / tileSize;
        // Draw one extra tile row/column to cover partially visible tiles at edges
        int endCol = (cameraX + screenWidth) / tileSize + 1;
        int endRow = (cameraY + screenHeight) / tileSize + 1;

        // Clamp drawing range to world bounds
        if (endCol > maxWorldCol) {
            endCol = maxWorldCol;
        }
        if (endRow > maxWorldRow) {
            endRow = maxWorldRow;
        }

        // Draw visible tiles
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                // Calculate tile's world position
                int worldX = col * tileSize;
                int worldY = row * tileSize;

                // Calculate tile's screen position (relative to camera view)
                int screenX = worldX - cameraX;
                int screenY = worldY - cameraY;

                // Draw the tile if it's within the camera's view (optional, loop already handles this)
                // if (screenX >= -tileSize && screenX < screenWidth && screenY >= -tileSize && screenY < screenHeight) {
                     if (grassTile != null) {
                         g2.drawImage(grassTile, screenX, screenY, tileSize, tileSize, null);
                     } else {
                         // Draw a fallback color if image not loaded
                         g2.setColor(Color.GREEN);
                         g2.fillRect(screenX, screenY, tileSize, tileSize);
                     }
                // }
            }
        }

        // --- Draw Player ---
        // Calculate player's screen position (relative to camera)
        int playerScreenX = player.worldX - cameraX;
        int playerScreenY = player.worldY - cameraY;

        // Draw the player at their screen position
        player.draw(g2, playerScreenX, playerScreenY); 
        g2.dispose(); // Release system resources
    }
}