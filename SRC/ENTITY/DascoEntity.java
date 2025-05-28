package SRC.ENTITY;

import java.util.Set;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;
import java.awt.Graphics2D;

/**
 * DascoEntity - Represents the Dasco NPC in the game
 */
public class DascoEntity extends NPCEntity {
    private BufferedImage sprite;    /**
     * Constructor for DascoEntity
     * 
     * @param gp GamePanel reference
     * @param worldX X position in the world
     * @param worldY Y position in the world
     */
    public DascoEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Dasco", "Dasco's House", 
              "A cheerful local who loves fine cuisine and has a passion for cooking.");
        
        // Set item preferences
        setItemPreferences();
        
        // Load sprite
        loadSprite();
        
        // Set movement speed to 0 to make NPC static
        setSpeed(0);
    }
      /**
     * Set up Dasco's item preferences
     */
    private void setItemPreferences() {
        // Define Dasco's item preferences based on the specs provided
        
        // Add loved items
        Set<String> lovedItems = Set.of("The Legends of Spakbor", "Cooked Pig's Head", "Wine", "Fugu", "Spakbor Salad");
        for (String item : lovedItems) {
            addLovedItem(item);
        }
        
        // Add liked items
        Set<String> likedItems = Set.of("Fish Sandwich", "Fish Stew", "Baguette", "Fish n' Chips");
        for (String item : likedItems) {
            addLikedItem(item);
        }
        
        // Add hated items
        Set<String> hatedItems = Set.of("Legend", "Grape", "Cauliflower", "Wheat", "Pufferfish", "Salmon");
        for (String item : hatedItems) {
            addHatedItem(item);
        }
    }    /**
     * Load Dasco's sprite from the file system
     */    private void loadSprite() {
        // Try multiple path formats to handle different environments
        String[] pathsToTry = {
            "RES/ENTITY/NPC/dasco.png",
            "RES\\ENTITY\\NPC\\dasco.png",
            "./RES/ENTITY/NPC/dasco.png",
            ".\\RES\\ENTITY\\NPC\\dasco.png"
        };
        
        boolean loaded = false;
        
        // First try loading with File
        for (String path : pathsToTry) {
            try {
                File spriteFile = new File(path);
                
                if (spriteFile.exists()) {
                    sprite = ImageIO.read(spriteFile);
                    System.out.println("Successfully loaded Dasco sprite from: " + spriteFile.getAbsolutePath());
                    System.out.println("Sprite dimensions: " + sprite.getWidth() + "x" + sprite.getHeight());
                    loaded = true;
                    break;
                }
            } catch (IOException e) {
                System.err.println("Error loading Dasco sprite from " + path + ": " + e.getMessage());
            }
        }
        
        // If file-based loading failed, try with resource stream
        if (!loaded) {
            try {
                // Try with classloader resource stream
                java.io.InputStream is = getClass().getClassLoader().getResourceAsStream("RES/ENTITY/NPC/dasco.png");
                if (is != null) {
                    sprite = ImageIO.read(is);
                    System.out.println("Successfully loaded Dasco sprite from classloader resource");
                    System.out.println("Sprite dimensions: " + sprite.getWidth() + "x" + sprite.getHeight());
                    loaded = true;
                }
            } catch (Exception e) {
                System.err.println("Error loading Dasco sprite from resource stream: " + e.getMessage());
            }
        }
        
        // If all loading attempts failed, create a placeholder sprite
        if (!loaded || sprite == null) {
            System.err.println("Failed to load Dasco sprite from all paths. Creating placeholder.");
            createPlaceholderSprite();
        }
    }
    
    /**
     * Create a placeholder sprite when the image file can't be loaded
     */
    private void createPlaceholderSprite() {
        System.out.println("Creating placeholder sprite for Dasco");
        
        // Create a simple colored square with text as a placeholder
        sprite = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprite.createGraphics();
        g.setColor(java.awt.Color.MAGENTA);
        g.fillRect(0, 0, 48, 48);
        g.setColor(java.awt.Color.BLACK);
        g.drawString("DASCO", 5, 24);
        g.dispose();
    }
      /**
     * Update method for Dasco's behavior
     * This will be called by the game loop
     */
    public void update() {
        incrementSpriteCounter();
        
        if (getSpriteCounter() > 60) {  
            if (getSpriteNum() == 1) {
                setSpriteNum(2);
            } else {
                setSpriteNum(1);
            }
            setSpriteCounter(0);
        }
    }
    
    /**
     * Custom interaction method for Dasco
     */
    @Override
    public void interact(Player player) {
        // Custom interaction logic for Dasco
        System.out.println("Dasco love wowo");
        // Determine relationship status based on heart points
        updateRelationshipStatus();
    }
    
    /**
     * Update relationship status based on heart points
     */
    private void updateRelationshipStatus() {
        if (getHeartPoints() < 100) {
            setRelationshipStatus("single");}
        
    }
      /**
     * Custom method to handle gift giving
     * @param itemName the name of the item being given
     */
    @Override
    public void receiveGift(String itemName) {
        super.receiveGift(itemName);
        updateRelationshipStatus();
    }
      /**
     * Get Dasco's sprite for rendering
     * 
     * @return The sprite image
     */
    public BufferedImage getSprite() {
        if (sprite == null) {
            System.err.println("WARNING: Dasco sprite is null! Attempting to reload...");
            loadSprite();
        }
        return sprite;
    }    /**
     * Draw the Dasco NPC
     * 
     * @param g2 Graphics2D object used for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        // Get the current sprite based on direction and animation frame
        BufferedImage image = getSprite();
        
        // Calculate screen position based on world position and camera
        int screenX = getWorldX() - gp.getCameraX();
        int screenY = getWorldY() - gp.getCameraY();
        int tileSize = gp.getTileSize();
        
        // Only draw if NPC is visible on screen
        if (getWorldX() + tileSize > gp.getCameraX() &&
            getWorldX() - tileSize < gp.getCameraX() + gp.getScreenWidth() &&
            getWorldY() + tileSize > gp.getCameraY() &&
            getWorldY() - tileSize < gp.getCameraY() + gp.getScreenHeight()) {
            
            if (image != null) {
                // Draw the sprite with proper size
                g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
                
            } 
        }
    }
}
