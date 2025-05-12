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
    GamePanel gp;
    KeyHandler keyH;
    MouseHandler mouseHandler;
    private int energy;
    private List<Item> inventory;
    private final int MAX_ENERGY = 100; // Maximum energy value
    private final int TOTAL_FRAMES = 8;

    // --- Variabel Entitas (Deklarasikan jika tidak di Entity) ---
    public int worldX, worldY; // Player's position in the game world
    public int speed;         // Player's movement speed
    public String direction;  // Player's current direction ("up", "down", "left", "right")
    public Rectangle solidArea; // Collision area for the player
    // -----------------------------------------------------------

    // Arrays untuk menyimpan 8 frame animasi untuk setiap arah
    private BufferedImage[] up;
    private BufferedImage[] down;
    private BufferedImage[] left;
    private BufferedImage[] right;

    // Variabel animasi
    private int spriteCounter = 0;
    private int spriteNum = 0;

    // --- Tambahkan skala visual player ---
    private final int visualScale = 4;
    private int playerVisualWidth;
    private int playerVisualHeight;
    // -------------------------------------
    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseHandler) {
        this.gp = gp;
        this.keyH = keyH;
        this.mouseHandler = mouseHandler;
        this.energy = 100; 
        this.inventory = new ArrayList<>();
        
        // Hitung ukuran visual player berdasarkan skala
        this.playerVisualWidth = gp.tileSize * visualScale; // 48 * 4 = 192 pixels
        this.playerVisualHeight = gp.tileSize * visualScale; // 48 * 4 = 192 pixels
        
        // Create a collision area at the center bottom of the player sprite (like feet position)
        int collisionWidth = gp.tileSize; // Width of collision box
        int collisionHeight = gp.tileSize; 
        int collisionX = (playerVisualWidth - collisionWidth) / 2; 
        int collisionY = ((playerVisualHeight - collisionHeight)/2) ; 
        
        solidArea = new Rectangle(collisionX, collisionY, collisionWidth, collisionHeight);


        // Inisialisasi array gambar
        up = new BufferedImage[TOTAL_FRAMES];
        down = new BufferedImage[TOTAL_FRAMES];
        left = new BufferedImage[TOTAL_FRAMES];
        right = new BufferedImage[TOTAL_FRAMES];

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = 100; // Starting position in the game world (world coordinates)
        worldY = 100;
        speed = 4; // Movement speed in pixels per frame
        direction = "down"; // default direction
    }    public void getPlayerImage() {
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
    }    public void update() {
        boolean moving = false;

        // Store original position to revert to if collision occurs
        int originalWorldX = worldX;
        int originalWorldY = worldY;

        // Keyboard movement updates player's world position
        if (keyH.upPressed) {
            direction = "up";
            worldY -= speed; // Try to move up
            moving = true;
        } else if (keyH.downPressed) { // Use else if for mutually exclusive movements
            direction = "down";
            worldY += speed; // Try to move down
            moving = true;
        } else if (keyH.leftPressed) { // Use else if
            direction = "left";
            worldX -= speed; // Try to move left
            moving = true;
        } else if (keyH.rightPressed) { // Use else if
            direction = "right";
            worldX += speed; // Try to move right
            moving = true;
        }        // Check collision with tiles - if collision, revert to original position
        if (moving && gp.tileManager.checkRectangleCollision(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height)) {
            worldX = originalWorldX;
            worldY = originalWorldY;
            moving = false; // Stop animation since we didn't actually move
        }
        
        // Ensure the player does not move out of bounds
        if (worldX < 0) {
            worldX = 0;
        } else if (worldX > gp.maxWorldWidth - playerVisualWidth) {
            worldX = gp.maxWorldWidth - playerVisualWidth;
        }

        if (worldY < 0) {
            worldY = 0;
        } else if (worldY > gp.maxWorldHeight - playerVisualHeight) {
            worldY = gp.maxWorldHeight - playerVisualHeight;
        }        if (mouseHandler.hasTarget) {
            moving = true; 
            
            // Store original position to revert to if collision occurs
            originalWorldX = worldX;
            originalWorldY = worldY;
            
            // mouseHandler.targetX/Y sudah dalam koordinat dunia (world coordinates)
            int targetWorldX = mouseHandler.targetX;
            int targetWorldY = mouseHandler.targetY;

            // Calculate vector from player's center to the world target
            int playerCenterX = worldX + playerVisualWidth / 2;
            int playerCenterY = worldY + playerVisualHeight / 2;
            int dx = targetWorldX - playerCenterX;
            int dy = targetWorldY - playerCenterY;

            // Calculate distance to target
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Determine direction based on movement vector
            if (distance > 0) { // Avoid division by zero if distance is 0
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) {
                        direction = "right";
                    } else {
                        direction = "left";
                    }
                } else {
                    if (dy > 0) {
                        direction = "down";
                    } else {
                        direction = "up";
                    }
                }

                // If player is close to target, stop moving
                if (distance < speed) {
                    // Calculate the position if player centered at target
                    int newWorldX = targetWorldX - playerVisualWidth / 2;
                    int newWorldY = targetWorldY - playerVisualHeight / 2;
                      // Check if this position would cause a collision
                    if (!gp.tileManager.checkRectangleCollision(newWorldX + solidArea.x, newWorldY + solidArea.y, solidArea.width, solidArea.height)) {
                        // No collision, set position 
                        worldX = newWorldX;
                        worldY = newWorldY;
                    }
                    
                    mouseHandler.hasTarget = false; // Stop moving and remove target indicator
                    moving = false; // Stop animation
                    System.out.println("Player reached target tile center at: " + targetWorldX + ", " + targetWorldY);
                } else {
                    // Move player towards target in world coordinates
                    double moveRatio = speed / distance;
                    int newWorldX = worldX + (int)(dx * moveRatio);
                    int newWorldY = worldY + (int)(dy * moveRatio);
                    
                    // Check if new position would be out of bounds
                    boolean hitObstacle = false;
                    
                    // Check X boundaries
                    if (newWorldX < 0) {
                        worldX = 0;
                        hitObstacle = true;
                    } else if (newWorldX > gp.maxWorldWidth - playerVisualWidth) {
                        worldX = gp.maxWorldWidth - playerVisualWidth;
                        hitObstacle = true;
                    } else {
                        worldX = newWorldX;
                    }
                    
                    // Check Y boundaries
                    if (newWorldY < 0) {
                        worldY = 0;
                        hitObstacle = true;
                    } else if (newWorldY > gp.maxWorldHeight - playerVisualHeight) {
                        worldY = gp.maxWorldHeight - playerVisualHeight;
                        hitObstacle = true;
                    } else {
                        worldY = newWorldY;
                    }                              // Check collision with tiles
                    if (gp.tileManager.checkRectangleCollision(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height)) {
                        worldX = originalWorldX; 
                        worldY = originalWorldY;
                        hitObstacle = true;
                    }
                    
                    // If hit obstacle, stop moving towards target
                    if (hitObstacle) {
                        mouseHandler.hasTarget = false;
                        moving = false;
                        System.out.println("Hit obstacle, stopped moving to target");
                    }
                }
            } else {
                // Player is exactly on the target
                mouseHandler.hasTarget = false;
                moving = false;
            }
        }

        // Update animation only if moving
        if (moving) {
            spriteCounter++;
            // Change frame every few updates (adjust '5' for animation speed)
            // A higher number makes the animation slower
            if (spriteCounter > 8) { // Increased sprite counter threshold slightly for smoother look
                spriteNum = (spriteNum + 1) % TOTAL_FRAMES;
                spriteCounter = 0;
            }
        } else {
             // Optional: Reset animation to the standing frame (e.g., frame 0) when not moving
             // This prevents the animation from continuing when the player is idle.
             // spriteNum = 0;
             // spriteCounter = 0; // Reset counter too
        }
    }

    // Method to draw the player at a specific screen position
    public void draw(Graphics2D g2, int screenX, int screenY) {
        BufferedImage image = null;

        // Select the appropriate image based on direction and animation frame
        switch (direction) {
            case "up":
                image = up[spriteNum];
                break;
            case "down":
                image = down[spriteNum];
                break;
            case "left":
                image = left[spriteNum];
                break;
            case "right":
                image = right[spriteNum];
                break;
        }        // --- Gambar karakter menggunakan ukuran visual yang diskalakan ---
        if (image != null) {
            g2.drawImage(image, screenX, screenY, playerVisualWidth, playerVisualHeight, null);
        } else {
            // Fallback: Draw a white rectangle if image not found
            g2.setColor(Color.white);
            g2.fillRect(screenX, screenY, playerVisualWidth, playerVisualHeight);
        }
        
        // Draw collision area (for debugging)
        // g2.setColor(new Color(255, 0, 0, 128)); // Semi-transparent red
        // g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        if (mouseHandler.hasTarget) {
            // Set transparent white color
            Color targetColor = new Color(255, 255, 255, 100); // RGBA format with alpha=100 (semitransparent white)
            g2.setColor(targetColor);
            
            // mouseHandler.targetX/Y sekarang dalam koordinat dunia, konversi ke screen coordinates
            int targetWorldX = mouseHandler.targetX;
            int targetWorldY = mouseHandler.targetY;
            
            // Convert world coordinates to screen coordinates
            int targetScreenX = targetWorldX - gp.cameraX;
            int targetScreenY = targetWorldY - gp.cameraY;
            
            // Draw a semi-transparent square centered on the target tile
            int tileSize = gp.tileSize;
            g2.fillRect(targetScreenX - tileSize/2, targetScreenY - tileSize/2, tileSize, tileSize);
            
            // Add a white border with slightly higher opacity to make it more visible
            g2.setColor(new Color(255, 255, 255, 150));
            g2.drawRect(targetScreenX - tileSize/2, targetScreenY - tileSize/2, tileSize, tileSize);
        }
    }
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
}