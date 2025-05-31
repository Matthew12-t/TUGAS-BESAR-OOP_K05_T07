package SRC.ENTITY;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import SRC.ITEMS.Item;
import SRC.ITEMS.Fish;
import SRC.MAIN.GamePanel;
import SRC.MAIN.KeyHandler;
import SRC.MAIN.MouseHandler;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private GamePanel gp;
    private KeyHandler keyH;
    private MouseHandler mouseHandler;    private PlayerAction playerAction;
    private int energy;
    private final int MAX_ENERGY = 100; // Maximum energy value
    private int gold; // Player's gold amount
    private final int TOTAL_FRAMES = 8;
    private boolean married;

    // Statistics tracking for EndGame system
    private int totalIncome;
    private int totalExpenditure;
    private int daysPlayed;
    private int totalCropsHarvested;
    private int totalFishCaught;
    private List<Fish> caughtFish;

    // --- Variabel Entitas (Deklarasikan jika tidak di Entity) ---
    private Rectangle solidArea; // Collision area for the player
    private boolean showHitbox = false; // Debug toggle for hitbox visibility
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
    // ----------------------------------    //contructor
    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseHandler) {
        super(gp, 100, 100); // Panggil konstruktor Entity dengan posisi awal
        this.gp = gp;        
        this.keyH = keyH;
        this.married = false;
        this.mouseHandler = mouseHandler;
        this.energy = 100; 
        this.gold = 500; // Initialize gold with 500
        this.playerAction = new PlayerAction(gp, this);
        
        // Initialize statistics tracking
        this.totalIncome = 0;
        this.totalExpenditure = 0;
        this.daysPlayed = 0;
        this.totalCropsHarvested = 0;
        this.totalFishCaught = 0;
        this.caughtFish = new ArrayList<>();
        
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
    }    public void setEnergy(int energy) {
        if (energy < -30) {
            this.energy = -30; // Allow negative energy but limit to -30
        } else if (energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY; // Prevent exceeding max energy
        } else {
            this.energy = energy;
        }
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        if (gold < 0) {
            this.gold = 0; // Prevent negative gold
        } else {
            this.gold = gold;
        }
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
            this.totalIncome += amount; // Track income for statistics
        }
    }

    public boolean spendGold(int amount) {
        if (amount > 0 && this.gold >= amount) {
            this.gold -= amount;
            this.totalExpenditure += amount; // Track expenditure for statistics
            return true; // Successfully spent gold
        }
        return false; // Not enough gold
    }

    public void addItemToInventory(Item item) {
        playerAction.addItemToInventory(item);
    }
    
    public void removeItemFromInventory(int slotIndex) {
        playerAction.removeInventoryItem(slotIndex);
    }
    
    public void removeOneItemFromInventory(int slotIndex) {
        playerAction.removeOneInventoryItem(slotIndex);
    }
    
    public Item[] getInventoryItems() {
        return playerAction.getInventoryItems();
    }
    
    public int[] getInventoryQuantities() {
        return playerAction.getInventoryQuantities();
    }
    public PlayerAction getPlayerAction() {
        return playerAction;
    }
      /**
     * Energy system methods (implemented directly in Player)
     */
    public int getCurrentEnergy() {
        return energy;
    }
    
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }
      public boolean consumeEnergy(int amount) {
        if (energy >= amount) {
            energy -= amount;
            if (energy < -30) energy = -30; // Allow negative energy but limit to -30
            return true;
        } else {
            // Allow forced energy consumption even if not enough energy
            // This ensures actions can still consume energy and go into negative
            energy -= amount;
            if (energy < -30) energy = -30; // Limit to -30
            return true; // Changed to always return true to allow negative energy
        }
    }
    
    public void restoreEnergy(int amount) {
        energy += amount;
        if (energy > MAX_ENERGY) {
            energy = MAX_ENERGY;
        }
    }
      public boolean hasEnoughEnergy(int requiredEnergy) {
        return energy >= requiredEnergy;
    }
      public double getEnergyPercentage() {
        // Return percentage based on energy, but cap at 0 for UI display
        double percentage = (double) energy / MAX_ENERGY;
        return Math.max(0.0, percentage); // Don't show negative percentage in UI
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
     * Check if player collides with any map objects, tiles, or NPCs
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
        
        if (checkNPCCollision()) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if player collides with any NPCs on the current map
     * @return true if collision with an NPC is detected
     */
    private boolean checkNPCCollision() {
        SRC.MAP.Map currentMap = gp.getCurrentMap();
        // Check for NPC collision in NPCHouseMap subclasses
        if (currentMap instanceof SRC.MAP.NPC_HOUSE.NPCHouseMap) {
            SRC.MAP.NPC_HOUSE.NPCHouseMap npcHouseMap = (SRC.MAP.NPC_HOUSE.NPCHouseMap) currentMap;
            java.util.ArrayList<NPCEntity> npcs = null;
            if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.AbigailHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.AbigailHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.DascoHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.DascoHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.EmilyHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.EmilyHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.CarolineHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.CarolineHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.PerryHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.PerryHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.MayorTadiHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.MayorTadiHouseMap) npcHouseMap).getNPCs();
            }
            if (npcs != null) {
                for (NPCEntity npc : npcs) {
                    if (npc.isSolid() && checkCollisionWithNPC(npc)) {
                        return true;
                    }
                }
            }
        }
        // Check for NPC collision in StoreMap
        else if (currentMap instanceof SRC.MAP.StoreMap) {
            SRC.MAP.StoreMap storeMap = (SRC.MAP.StoreMap) currentMap;
            java.util.ArrayList<NPCEntity> npcs = storeMap.getNPCs();
            if (npcs != null) {
                for (NPCEntity npc : npcs) {
                    if (npc.isSolid() && checkCollisionWithNPC(npc)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Check if player collides with a specific NPC
     * @param npc The NPC to check collision with
     * @return true if collision is detected
     */
    private boolean checkCollisionWithNPC(NPCEntity npc) {
        java.awt.Rectangle playerBounds = new java.awt.Rectangle(
            getWorldX() + collisionOffsetX,
            getWorldY() + collisionOffsetY,
            collisionWidth,
            collisionHeight
        );
        java.awt.Rectangle npcBounds = npc.getCollisionBounds();
        return playerBounds.intersects(npcBounds);
    }
    
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
                    setWorldY(newWorldY);                    // Check for collision after movement
                    if (checkCollision()) {
                        // Revert to original position
                        setWorldX(moveOriginalX);
                        setWorldY(moveOriginalY);
                        
                        // Cancel mouse target immediately when collision is detected
                        mouseHandler.setHasTarget(false);
                        moving = false;
                        System.out.println("Collision detected, stopped moving to target");
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
            int hitboxRightCol = (hitboxWorldX + collisionWidth - 1) / tileSize;            int hitboxBottomRow = (hitboxWorldY + collisionHeight - 1) / tileSize;
            
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
            g2.fillRect(targetScreenX - tileSize/2, targetScreenY - tileSize/2, tileSize, tileSize);            // Add a white border with slightly higher opacity to make it more visible
            g2.setColor(new Color(255, 255, 255, 150));
            g2.drawRect(targetScreenX - tileSize/2, targetScreenY - tileSize/2, tileSize, tileSize);
        }
    }
      
    // Add holding tool system
    private SRC.ITEMS.Tool currentHoldingTool = null;
    // Add holding seed system
    private SRC.ITEMS.Seed currentHoldingSeed = null;
    
    /**
     * Get currently holding tool
     * @return Current holding tool or null if none
     */
    public SRC.ITEMS.Tool getCurrentHoldingTool() {
        return currentHoldingTool;
    }
      /**
     * Set currently holding tool
     * @param tool Tool to hold
     */
    public void setCurrentHoldingTool(SRC.ITEMS.Tool tool) {
        // Clear any held seed to ensure only one item is held at a time
        if (tool != null && this.currentHoldingSeed != null) {
            this.currentHoldingSeed = null;
            System.out.println("Dropped held seed to hold tool: " + tool.getName());
        }
        
        this.currentHoldingTool = tool;
        System.out.println("Player is now holding: " + (tool != null ? tool.getName() : "nothing"));
    }
    
    /**
     * Check if player is holding a specific tool
     * @param toolName Name of the tool to check
     * @return true if holding the specified tool
     */
    public boolean isHolding(String toolName) {
        return currentHoldingTool != null && currentHoldingTool.getName().equals(toolName);
    }
    
    /**
     * Check if player is holding any tool
     * @return true if holding any tool
     */
    public boolean isHoldingAnyTool() {
        return currentHoldingTool != null;
    }
    
    // Seed holding system methods
    /**
     * Get currently holding seed
     * @return Current holding seed or null if none
     */
    public SRC.ITEMS.Seed getCurrentHoldingSeed() {
        return currentHoldingSeed;
    }
      /**
     * Set currently holding seed
     * @param seed Seed to hold
     */
    public void setCurrentHoldingSeed(SRC.ITEMS.Seed seed) {
        // Clear any held tool to ensure only one item is held at a time
        if (seed != null && this.currentHoldingTool != null) {
            this.currentHoldingTool = null;
            System.out.println("Dropped held tool to hold seed: " + seed.getName());
        }
        
        this.currentHoldingSeed = seed;
        System.out.println("Player is now holding seed: " + (seed != null ? seed.getName() : "nothing"));
    }
    
    /**
     * Check if player is holding a specific seed
     * @param seedName Name of the seed to check
     * @return true if holding the specified seed
     */
    public boolean isHoldingSeed(String seedName) {
        return currentHoldingSeed != null && currentHoldingSeed.getName().equals(seedName);
    }
    
    /**
     * Check if player is holding any seed
     * @return true if holding any seed
     */
    public boolean isHoldingAnySeed() {
        return currentHoldingSeed != null;
    }
    
    /**
     * Get the currently held item (for seed detection in planting logic)
     * @return Item currently selected in inventory, or null if none selected
     */
    public Item getCurrentlyHeldItem() {
        int selectedSlotIndex = gp.getMouseHandler().getSelectedSlotIndex();
        if (selectedSlotIndex >= 0) {
            Item[] items = getInventoryItems();
            if (selectedSlotIndex < items.length && items[selectedSlotIndex] != null) {
                return items[selectedSlotIndex];
            }
        }
        return null;
    }
    
    public boolean isMarried() {
        return married;
    }

    public void setMarried(boolean married) {
        this.married = married;
    }

    // Statistics tracking methods for EndGame system
    public int getTotalIncome() {
        return totalIncome;
    }

    public int getTotalExpenditure() {
        return totalExpenditure;
    }

    public int getDaysPlayed() {
        return daysPlayed;
    }

    public void incrementDaysPlayed() {
        this.daysPlayed++;
    }

    public int getTotalCropsHarvested() {
        return totalCropsHarvested;
    }

    public void incrementCropsHarvested(int amount) {
        this.totalCropsHarvested += amount;
    }

    public int getTotalFishCaught() {
        return totalFishCaught;
    }

    public void incrementFishCaught(Fish fish) {
        this.totalFishCaught++;
        if (fish != null) {
            this.caughtFish.add(fish);
        }
    }

    public List<Fish> getCaughtFish() {
        return new ArrayList<>(caughtFish); // Return copy to prevent external modification
    }
}