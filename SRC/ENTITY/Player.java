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
    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseHandler) {
        super(gp, 100, 100); // Panggil konstruktor Entity dengan posisi awal
        this.gp = gp;
        this.keyH = keyH;
        this.mouseHandler = mouseHandler;
        this.energy = 100; 
        this.inventory = new ArrayList<>();
        solidArea = new Rectangle(0, 0, gp.getTileSize(), gp.getTileSize()); // Set solid area size to tile size

        // Hitung ukuran visual player berdasarkan skala
        this.playerVisualWidth = gp.getTileSize() * visualScale; // 48 * 4 = 192 pixels
        this.playerVisualHeight = gp.getTileSize() * visualScale; // 48 * 4 = 192 pixels


        // Inisialisasi array gambar
        up = new BufferedImage[TOTAL_FRAMES];
        down = new BufferedImage[TOTAL_FRAMES];
        left = new BufferedImage[TOTAL_FRAMES];
        right = new BufferedImage[TOTAL_FRAMES];

        setDefaultValues();
        getPlayerImage();
    }    public void setDefaultValues() {
        setWorldX(100); // Starting position in the game world (world coordinates)
        setWorldY(100);
        setSpeed(4); // Movement speed in pixels per frame
        setDirection("down"); // default direction
    }public void getPlayerImage() {
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
        }        // Ensure the player does not move out of bounds
        if (getWorldX() < 0) {
            setWorldX(0);
        } else if (getWorldX() > gp.getMaxWorldWidth() - playerVisualWidth) {
            setWorldX(gp.getMaxWorldWidth() - playerVisualWidth);
        }

        if (getWorldY() < 0) {
            setWorldY(0);
        } else if (getWorldY() > gp.getMaxWorldHeight() - playerVisualHeight) {
            setWorldY(gp.getMaxWorldHeight() - playerVisualHeight);
        }        if (mouseHandler.isHasTarget()) {
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
                    System.out.println("Player reached target tile center at: " + targetWorldX + ", " + targetWorldY);                } else {
                    // Move player towards target in world coordinates
                    double moveRatio = getSpeed() / distance;
                    int newWorldX = getWorldX() + (int)(dx * moveRatio);
                    int newWorldY = getWorldY() + (int)(dy * moveRatio);
                    
                    // Check if new position would be out of bounds
                    boolean hitBoundary = false;
                    
                    // Check X boundaries
                    if (newWorldX < 0) {
                        setWorldX(0);
                        hitBoundary = true;
                    } else if (newWorldX > gp.getMaxWorldWidth() - playerVisualWidth) {
                        setWorldX(gp.getMaxWorldWidth() - playerVisualWidth);
                        hitBoundary = true;
                    } else {
                        setWorldX(newWorldX);
                    }
                    
                    // Check Y boundaries
                    if (newWorldY < 0) {
                        setWorldY(0);
                        hitBoundary = true;
                    } else if (newWorldY > gp.getMaxWorldHeight() - playerVisualHeight) {
                        setWorldY(gp.getMaxWorldHeight() - playerVisualHeight);
                        hitBoundary = true;
                    } else {
                        setWorldY(newWorldY);
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
        }        // Update animation only if moving
        if (moving) {
            incrementSpriteCounter();
            // Change frame every few updates (adjust '5' for animation speed)
            // A higher number makes the animation slower
            if (getSpriteCounter() > 8) { // Increased sprite counter threshold slightly for smoother look
                setSpriteNum((getSpriteNum() + 1) % TOTAL_FRAMES);
                setSpriteCounter(0);
            }
        } else {
             // Optional: Reset animation to the standing frame (e.g., frame 0) when not moving
             // This prevents the animation from continuing when the player is idle.
             // setSpriteNum(0);
             // setSpriteCounter(0); // Reset counter too
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
        }        if (mouseHandler.isHasTarget()) {
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
    }    public List<Item> getInventory() {
        return inventory;
    }
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
    
    // Getter for solidArea
    public Rectangle getSolidArea() {
        return solidArea;
    }
    
    // Getter for player visual dimensions
    public int getPlayerVisualWidth() {
        return playerVisualWidth;
    }
    
    public int getPlayerVisualHeight() {
        return playerVisualHeight;
    }
}