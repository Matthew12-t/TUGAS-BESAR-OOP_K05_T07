package SRC.ENTITY;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;
import SRC.MAIN.KeyHandler;
import SRC.MAIN.MouseHandler;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    MouseHandler mouseHandler;
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

        // Set the player's collision area (hitbox) relative to its worldX, worldY
        // Ukuran solidArea biasanya tidak langsung mengikuti skala visual 4x.
        // Anda mungkin ingin solidArea lebih kecil dari ukuran visual
        // agar pergerakan terasa lebih luwes, atau sama dengan ukuran tile gp.tileSize.
        // Contoh: hitbox seukuran tile (48x48)
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);

        // Hitung ukuran visual player berdasarkan skala
        this.playerVisualWidth = gp.tileSize * visualScale; // 48 * 4 = 192 pixels
        this.playerVisualHeight = gp.tileSize * visualScale; // 48 * 4 = 192 pixels


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
    }

    public void getPlayerImage() {
        try {
            // Load images for each direction and frame
            // Using getClass().getResourceAsStream is generally better for deploying in a JAR
            for (int i = 0; i < TOTAL_FRAMES; i++) {
                 // Attempt to load from classpath first
                 up[i] = ImageIO.read(getClass().getResourceAsStream("/RES/cw belakang/barcode" + (i+1) + ".png"));
                 down[i] = ImageIO.read(getClass().getResourceAsStream("/RES/cw depan/barcode" + (i+1) + ".png"));
                 left[i] = ImageIO.read(getClass().getResourceAsStream("/RES/cw kiri/barcode" + (i+1) + ".png"));
                 right[i] = ImageIO.read(getClass().getResourceAsStream("/RES/cw kanan/barcode" + (i+1) + ".png"));

                 // Fallback for direct file access if running outside JAR (less recommended)
                 if (up[i] == null) up[i] = ImageIO.read(new File("RES/cw belakang/barcode" + (i+1) + ".png"));
                 if (down[i] == null) down[i] = ImageIO.read(new File("RES/cw depan/barcode" + (i+1) + ".png"));
                 if (left[i] == null) left[i] = ImageIO.read(new File("RES/cw kiri/barcode" + (i+1) + ".png"));
                 if (right[i] == null) right[i] = ImageIO.read(new File("RES/cw kanan/barcode" + (i+1) + ".png"));
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

        // Keyboard movement updates player's world position
        if (keyH.upPressed) {
            direction = "up";
            worldY -= speed; // Update worldY
            moving = true;
        } else if (keyH.downPressed) { // Use else if for mutually exclusive movements
            direction = "down";
            worldY += speed; // Update worldY
            moving = true;
        } else if (keyH.leftPressed) { // Use else if
            direction = "left";
            worldX -= speed; // Update worldX
            moving = true;
        } else if (keyH.rightPressed) { // Use else if
            direction = "right";
            worldX += speed; // Update worldX
            moving = true;
        }        // Ensure the player does not move out of bounds
        if (worldX < 0) {
            worldX = 0;
        } else if (worldX > gp.maxWorldWidth - playerVisualWidth) {
            worldX = gp.maxWorldWidth - playerVisualWidth;
        }

        if (worldY < 0) {
            worldY = 0;
        } else if (worldY > gp.maxWorldHeight - playerVisualHeight) {
            worldY = gp.maxWorldHeight - playerVisualHeight;
        }

        if (mouseHandler.hasTarget) {
            moving = true; 
            int targetWorldX = mouseHandler.targetX + gp.cameraX;
            int targetWorldY = mouseHandler.targetY + gp.cameraY;

            // Calculate vector from player's center to the world target
            // Gunakan solidArea untuk menentukan pusat player untuk perhitungan mouse
            int dx = targetWorldX - (worldX + solidArea.width / 2);
            int dy = targetWorldY - (worldY + solidArea.height / 2);

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
                     // Move the remaining distance to the target
                     worldX = targetWorldX - solidArea.width / 2;
                     worldY = targetWorldY - solidArea.height / 2;
                     mouseHandler.hasTarget = false; // Stop moving
                     moving = false; // Stop animation if not moving anymore
                 } else {
                     // Move player towards target in world coordinates
                     double moveRatio = speed / distance;
                     worldX += dx * moveRatio; // Update worldX
                     worldY += dy * moveRatio; // Update worldY
                 }
            } else {
                 // Player is exactly on the target
                 mouseHandler.hasTarget = false;
                 moving = false;
            }
        }        // Ensure the player does not move out of bounds after mouse movement
        if (worldX < 0) {
            worldX = 0;
        } else if (worldX > gp.maxWorldWidth - playerVisualWidth) {
            worldX = gp.maxWorldWidth - playerVisualWidth;
        }

        if (worldY < 0) {
            worldY = 0;
        } else if (worldY > gp.maxWorldHeight - playerVisualHeight) {
            worldY = gp.maxWorldHeight - playerVisualHeight;
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
        }

        // --- Gambar karakter menggunakan ukuran visual yang diskalakan ---
        if (image != null) {
            g2.drawImage(image, screenX, screenY, playerVisualWidth, playerVisualHeight, null);
        } else {
            // Fallback: Draw a white rectangle if image not found
            g2.setColor(Color.white);
            g2.fillRect(screenX, screenY, playerVisualWidth, playerVisualHeight);
        }
        if (mouseHandler.hasTarget) {
            g2.setColor(Color.red);
            // Draw oval at the mouse target screen position
            g2.drawOval(mouseHandler.targetX - 5, mouseHandler.targetY - 5, 10, 10);
        }
    }
}