package SRC.ENTITY;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

import SRC.ITEMS.Item;
import SRC.MAIN.GamePanel;
import SRC.MAIN.KeyHandler;
import SRC.MAIN.MouseHandler;

public class Player extends Entity {
    private GamePanel gp;
    private KeyHandler keyH;
    private MouseHandler mouseHandler;
    private int energy;
    private List<Item> inventory;
    private final int MAX_ENERGY = 100; // Maximum energy value
    private final int TOTAL_FRAMES = 8;

    // --- Variabel Entitas (Deklarasikan jika tidak di Entity) ---
    private Rectangle solidArea; // Collision area for the player
    private boolean showHitbox = true; // Debug toggle for hitbox visibility
    // -----------------------------------------------------------

    // Arrays untuk menyimpan 8 frame animasi untuk setiap arah
    private BufferedImage[] up;
    private BufferedImage[] down;
    private BufferedImage[] left;
    private BufferedImage[] right;

    // --- Tambahkan skala visual player ---
    private final int visualScale = 4;
    private int playerVisualWidth;
    private int playerVisualHeight;
    // -------------------------------------

    // --- Collision hitbox properties ---
    private final float collisionScale = 0.6f; // Hitbox size relative to tile size (60%)
    private int collisionWidth;
    private int collisionHeight;
    private int collisionOffsetX;
    private int collisionOffsetY;
    // ----------------------------------

    //contructor
    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseHandler) {
        super(gp, 100, 100); // Panggil konstruktor Entity dengan posisi awal
        this.gp = gp;
        this.keyH = keyH;
        this.mouseHandler = mouseHandler;
        this.energy = 100; 
        this.inventory = new ArrayList<>();
        
        // Hitung ukuran visual player berdasarkan skala
        this.playerVisualWidth = gp.getTileSize() * visualScale; // 48 * 4 = 192 pixels
        this.playerVisualHeight = gp.getTileSize() * visualScale; // 48 * 4 = 192 pixels

        // Hitung ukuran collision hitbox (lebih kecil dari visual)
        this.collisionWidth = (int)(gp.getTileSize() * collisionScale); // 48 * 0.6 = ~28 pixels
        this.collisionHeight = (int)(gp.getTileSize() * collisionScale); // 48 * 0.6 = ~28 pixels
        
        // Offset agar hitbox berada di tengah sprite (baik horizontal maupun vertikal)
        this.collisionOffsetX = (playerVisualWidth - collisionWidth) / 2; // Tengah secara horizontal
        this.collisionOffsetY = (playerVisualHeight - collisionHeight) / 2; // Tengah secara vertikal
        
        // Set solid area dengan ukuran dan posisi hitbox baru
        solidArea = new Rectangle(collisionOffsetX, collisionOffsetY, collisionWidth, collisionHeight);

        // Inisialisasi array gambar
        up = new BufferedImage[TOTAL_FRAMES];
        down = new BufferedImage[TOTAL_FRAMES];
        left = new BufferedImage[TOTAL_FRAMES];
        right = new BufferedImage[TOTAL_FRAMES];

        setDefaultValues();
        getPlayerImage();
    }
    
    // --- Getter dan Setter untuk atribut baru ---
    public int getEnergy() {
        return energy;
    }   
    public void setEnergy(int energy) {
        if (energy < 0) {
            this.energy = 0; // Prevent negative energy
        } else if (energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY; // Prevent exceeding max energy
        } else {
            this.energy = energy;
        }
    }
    public void addItemToInventory(Item item) {
        inventory.add(item);
    }
    public void removeItemFromInventory(Item item) {
        inventory.remove(item);
    }    
    public List<Item> getInventory() {
        return inventory;
    }
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
    // Getter for solidArea      
    public Rectangle getSolidArea() {
        return solidArea;
    }
      
    /**
     * Toggle hitbox visibility for debugging
     */
    public void toggleHitbox() {
        showHitbox = !showHitbox;
    }
      
    /**
     * Check if player collides with any map objects or tiles
     * @return true if collision detected
     */
    public boolean checkCollision() {
        // Calculate the player's position in tile coordinates with center hitbox
        int tileSize = gp.getTileSize();
        
        // Calculate the world position of the hitbox
        int hitboxWorldX = getWorldX() + collisionOffsetX;
        int hitboxWorldY = getWorldY() + collisionOffsetY;
        
        // Convert hitbox corners to tile coordinates
        int hitboxLeftCol = hitboxWorldX / tileSize;
        int hitboxTopRow = hitboxWorldY / tileSize;
        int hitboxRightCol = (hitboxWorldX + collisionWidth - 1) / tileSize;
        int hitboxBottomRow = (hitboxWorldY + collisionHeight - 1) / tileSize;
        
        // Get the current map
        SRC.MAP.Map currentMap = gp.getCurrentMap();
        
        // Check all tiles that the hitbox overlaps
        for (int row = hitboxTopRow; row <= hitboxBottomRow; row++) {
            for (int col = hitboxLeftCol; col <= hitboxRightCol; col++) {
                if (currentMap.hasCollision(col, row)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    // Getter for player visual dimensions
    public int getPlayerVisualWidth() {
        return playerVisualWidth;
    }
    
    public int getPlayerVisualHeight() {
        return playerVisualHeight;
    }
    
    public void setDefaultValues() {
        setWorldX(100); // Starting position in the game world (world coordinates)
        setWorldY(100);
        setSpeed(4); // Movement speed in pixels per frame
        setDirection("down"); // default direction
    }
    
    public void getPlayerImage() {
        try {
            // Load images for each direction and frame
            // Using getClass().getResourceAsStream is generally better for deploying in a JAR
            for (int i = 0; i < TOTAL_FRAMES; i++) {
                 // Attempt to load from classpath first
                 up[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/cw belakang/barcode" + (i+1) + ".png"));
                 down[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/cw depan/barcode" + (i+1) + ".png"));
                 left[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/cw kiri/barcode" + (i+1) + ".png"));
                 right[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/cw kanan/barcode" + (i+1) + ".png"));

                 // Fallback for direct file access if running outside JAR (less recommended)
                 if (up[i] == null) up[i] = ImageIO.read(new File("RES/ENTITY/PLAYER/cw belakang/barcode" + (i+1) + ".png"));
                 if (down[i] == null) down[i] = ImageIO.read(new File("RES/ENTITY/PLAYER/cw depan/barcode" + (i+1) + ".png"));
                 if (left[i] == null) left[i] = ImageIO.read(new File("RES/ENTITY/PLAYER/cw kiri/barcode" + (i+1) + ".png"));
                 if (right[i] == null) right[i] = ImageIO.read(new File("RES/ENTITY/PLAYER/cw kanan/barcode" + (i+1) + ".png"));
            }

            // Check if any images failed to load and print a warning
            if (up[0] == null || down[0] == null || left[0] == null || right[0] == null) {
                 System.err.println("Warning: Some player images failed to load. Check file paths and if resources are included in classpath.");
            } else {
                System.out.println("All player images successfully loaded!");
            }
        } catch (IOException e) {
            System.err.println("Error loading player images: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
             System.err.println("Resource not found: Check player image paths in RES folder. Make sure RES is in your classpath.");
             e.printStackTrace();
        }
    }
    
    public void update() {
        boolean moving = false;
        
        // Store the player's original position in case a collision happens
        int originalX = getWorldX();
        int originalY = getWorldY();

        // Keyboard movement updates player's world position
        if (keyH.upPressed) {
            setDirection("up");
            setWorldY(getWorldY() - getSpeed()); // Update worldY
            moving = true;
        } else if (keyH.downPressed) { // Use else if for mutually exclusive movements
            setDirection("down");
            setWorldY(getWorldY() + getSpeed()); // Update worldY
            moving = true;
        } else if (keyH.leftPressed) { // Use else if
            setDirection("left");
            setWorldX(getWorldX() - getSpeed()); // Update worldX
            moving = true;
        } else if (keyH.rightPressed) { // Use else if
            setDirection("right");
            setWorldX(getWorldX() + getSpeed()); // Update worldX
            moving = true;
        }
        
        // Check for collision with map objects
        if (checkCollision()) {
            // If collision, revert to original position
            setWorldX(originalX);
            setWorldY(originalY);
        }
        
        // Ensure the player does not move out of bounds
        int worldMargin = -70; // Small margin to prevent getting too close to the edge
        if (getWorldX() < worldMargin) {
            setWorldX(worldMargin);
        } else if (getWorldX() > gp.getMaxWorldWidth() - playerVisualWidth - worldMargin) {
            setWorldX(gp.getMaxWorldWidth() - playerVisualWidth - worldMargin);
        }
        
        if (getWorldY() < worldMargin) {
            setWorldY(worldMargin);
        } else if (getWorldY() > gp.getMaxWorldHeight() - playerVisualHeight - worldMargin) {
            setWorldY(gp.getMaxWorldHeight() - playerVisualHeight - worldMargin);
        }
        
        if (mouseHandler.isHasTarget()) {
            moving = true; 
            
            // mouseHandler.targetX/Y sudah dalam koordinat dunia (world coordinates)
            int targetWorldX = mouseHandler.getTargetX();
            int targetWorldY = mouseHandler.getTargetY();

            // Calculate vector from player's center to the world target
            int playerCenterX = getWorldX() + playerVisualWidth / 2;
            int playerCenterY = getWorldY() + playerVisualHeight / 2;
            int dx = targetWorldX - playerCenterX;
            int dy = targetWorldY - playerCenterY;

            // Calculate distance to target
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Determine direction based on movement vector
            if (distance > 0) { // Avoid division by zero if distance is 0
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) {
                        setDirection("right");
                    } else {
                        setDirection("left");
                    }
                } else {
                    if (dy > 0) {
                        setDirection("down");
                    } else {
                        setDirection("up");
                    }
                }

                // If player is close to target, stop moving
                if (distance < getSpeed()) {
                    // Position player precisely so that its center is at the center of the target tile
                    setWorldX(targetWorldX - playerVisualWidth / 2);
                    setWorldY(targetWorldY - playerVisualHeight / 2);
                    mouseHandler.setHasTarget(false); // Stop moving and remove target indicator
                    moving = false; // Stop animation
                    System.out.println("Player reached target tile center at: " + targetWorldX + ", " + targetWorldY);
                } else {
                    // Move player towards target in world coordinates
                    double moveRatio = getSpeed() / distance;
                    int newWorldX = getWorldX() + (int)(dx * moveRatio);
                    int newWorldY = getWorldY() + (int)(dy * moveRatio);
                    
                    // Save original position
                    int moveOriginalX = getWorldX();
                    int moveOriginalY = getWorldY();
                    
                    // Apply movement
                    setWorldX(newWorldX);
                    setWorldY(newWorldY);
                    
                    // Check for collision after movement
                    if (checkCollision()) {
                        // Try moving on X-axis only
                        setWorldY(moveOriginalY);
                        if (checkCollision()) {
                            // X-only movement caused collision too, try Y-only
                            setWorldX(moveOriginalX);
                            setWorldY(newWorldY);
                            
                            if (checkCollision()) {
                                // Both X and Y cause collision, stay in place
                                setWorldX(moveOriginalX);
                                setWorldY(moveOriginalY);
                                mouseHandler.setHasTarget(false); // Cancel target if stuck
                                moving = false;
                            }
                        }
                    }
                    
                    // Check if new position would be out of bounds
                    boolean hitBoundary = false;
                    
                    // Check X boundaries
                    if (getWorldX() < worldMargin) {
                        setWorldX(worldMargin);
                        hitBoundary = true;
                    } else if (getWorldX() > gp.getMaxWorldWidth() - playerVisualWidth - worldMargin) {
                        setWorldX(gp.getMaxWorldWidth() - playerVisualWidth - worldMargin);
                        hitBoundary = true;
                    }
                    
                    // Check Y boundaries
                    if (getWorldY() < worldMargin) {
                        setWorldY(worldMargin);
                        hitBoundary = true;
                    } else if (getWorldY() > gp.getMaxWorldHeight() - playerVisualHeight - worldMargin) {
                        setWorldY(gp.getMaxWorldHeight() - playerVisualHeight - worldMargin);
                        hitBoundary = true;
                    }
                    
                    // If hit boundary, stop moving towards target
                    if (hitBoundary) {
                        mouseHandler.setHasTarget(false);
                        moving = false;
                        System.out.println("Hit boundary, stopped moving to target");
                    }
                }
            } else {
                // Player is exactly on the target
                mouseHandler.setHasTarget(false);
                moving = false;
            }
        } 
        
        // Update animation only if moving
        if (moving) {
            incrementSpriteCounter();
            // Change frame every few updates (adjust '5' for animation speed)
            // A higher number makes the animation slower
            if (getSpriteCounter() > 8) { // Increased sprite counter threshold slightly for smoother look
                setSpriteNum((getSpriteNum() + 1) % TOTAL_FRAMES);
                setSpriteCounter(0);
            }
        } else {
            // Reset animation to the standing frame when not moving
            setSpriteNum(0);
            setSpriteCounter(0); // Reset counter too
        }
    }

    // Method to draw the player at a specific screen position
    public void draw(Graphics2D g2, int screenX, int screenY) {
        BufferedImage image = null;

        // Select the appropriate image based on direction and animation frame
        switch (super.getDirection()) {
            case "up":
                image = up[super.getSpriteNum()];
                break;
            case "down":
                image = down[super.getSpriteNum()];
                break;
            case "left":
                image = left[super.getSpriteNum()];
                break;
            case "right":
                image = right[super.getSpriteNum()];
                break;
        }
        
        // --- Gambar karakter menggunakan ukuran visual yang diskalakan ---
        if (image != null) {
            g2.drawImage(image, screenX, screenY, playerVisualWidth, playerVisualHeight, null);
        } else {
            // Fallback: Draw a white rectangle if image not found
            g2.setColor(Color.white);
            g2.fillRect(screenX, screenY, playerVisualWidth, playerVisualHeight);
        }
        
        // Draw hitbox untuk debug jika showHitbox diaktifkan
        if (showHitbox) {
            // Outline visual area keseluruhan player
            g2.setColor(new Color(0, 0, 255, 50)); // Biru transparan
            g2.drawRect(screenX, screenY, playerVisualWidth, playerVisualHeight);
            
            // Fill collision hitbox yang berada di tengah (merah transparan)
            g2.setColor(new Color(255, 0, 0, 100)); 
            g2.fillRect(screenX + collisionOffsetX, screenY + collisionOffsetY, 
                     collisionWidth, collisionHeight);
                     
            // Outline hitbox collision dengan warna merah solid
            g2.setColor(Color.RED);
            g2.drawRect(screenX + collisionOffsetX, screenY + collisionOffsetY, 
                     collisionWidth, collisionHeight);
                     
            // Tambahkan cross-hair di tengah hitbox
            int centerX = screenX + collisionOffsetX + collisionWidth/2;
            int centerY = screenY + collisionOffsetY + collisionHeight/2;
            g2.drawLine(centerX - 5, centerY, centerX + 5, centerY); // Horizontal line
            g2.drawLine(centerX, centerY - 5, centerX, centerY + 5); // Vertical line
            
            // Tampilkan informasi hitbox di atas player
            g2.setColor(Color.WHITE);
            g2.drawString(collisionWidth + "x" + collisionHeight, 
                      screenX + collisionOffsetX, 
                      screenY + collisionOffsetY - 5);

            // Visualisasi tile-based collision detection
            int tileSize = gp.getTileSize();
            
            // Calculate hitbox tile coordinates
            int hitboxWorldX = getWorldX() + collisionOffsetX;
            int hitboxWorldY = getWorldY() + collisionOffsetY;
            int hitboxLeftCol = hitboxWorldX / tileSize;
            int hitboxTopRow = hitboxWorldY / tileSize;
            int hitboxRightCol = (hitboxWorldX + collisionWidth - 1) / tileSize;
            int hitboxBottomRow = (hitboxWorldY + collisionHeight - 1) / tileSize;
            
            // Convert tile coordinates back to screen coordinates
            int leftTileScreenX = (hitboxLeftCol * tileSize) - gp.getCameraX();
            int topTileScreenY = (hitboxTopRow * tileSize) - gp.getCameraY();
            int rightTileScreenX = (hitboxRightCol * tileSize) - gp.getCameraX();
            int bottomTileScreenY = (hitboxBottomRow * tileSize) - gp.getCameraY();
            
            // Draw tile-based collision boxes
            g2.setColor(new Color(0, 255, 0, 100)); // Green with transparency
            
            // Draw all tiles that the hitbox overlaps
            for (int row = hitboxTopRow; row <= hitboxBottomRow; row++) {
                for (int col = hitboxLeftCol; col <= hitboxRightCol; col++) {
                    int tileScreenX = (col * tileSize) - gp.getCameraX();
                    int tileScreenY = (row * tileSize) - gp.getCameraY();
                    g2.drawRect(tileScreenX, tileScreenY, tileSize, tileSize);
                }
            }
        }
        
        if (mouseHandler.isHasTarget()) {
            // Set transparent white color
            Color targetColor = new Color(255, 255, 255, 100); // RGBA format with alpha=100 (semitransparent white)
            g2.setColor(targetColor);
            
            // mouseHandler.targetX/Y sekarang dalam koordinat dunia, konversi ke screen coordinates
            int targetWorldX = mouseHandler.getTargetX();
            int targetWorldY = mouseHandler.getTargetY();
            
            // Convert world coordinates to screen coordinates
            int targetScreenX = targetWorldX - gp.getCameraX();
            int targetScreenY = targetWorldY - gp.getCameraY();
            
            // Draw a semi-transparent square centered on the target tile
            int tileSize = gp.getTileSize();
            g2.fillRect(targetScreenX - tileSize/2, targetScreenY - tileSize/2, tileSize, tileSize);
            
            // Add a white border with slightly higher opacity to make it more visible
            g2.setColor(new Color(255, 255, 255, 150));
            g2.drawRect(targetScreenX - tileSize/2, targetScreenY - tileSize/2, tileSize, tileSize);
        }
    }
}