package SRC.ENTITY;

import java.util.Set;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;
import java.awt.Graphics2D;

/**
 * EmilyEntity - Represents the Emily NPC in the game
 */
public class EmilyEntity extends NPCEntity {
    private BufferedImage sprite;
    
    /**
     * Constructor for EmilyEntity
     * 
     * @param gp GamePanel reference
     * @param worldX X position in the world
     * @param worldY Y position in the world
     */
    public EmilyEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Emily", "Emily's House", 
              "Emily adalah seorang koki yang juga bekerja dan tinggal di toko lokal Spakbor Hills.");

        setItemPreferences();
        loadSprite();
        setSpeed(0);
    }

    /**
     * Set up Emily's item preferences
     */
    private void setItemPreferences() {
        Set<String> lovedItems = Set.of("Seeds");
        for (String item : lovedItems) {
            addLovedItem(item);
        }
        Set<String> likedItems = Set.of("Catfish", "Salmon", "Sardine");
        for (String item : likedItems) {
            addLikedItem(item);
        }
        
        Set<String> hatedItems = Set.of("Coal", "Wood");
        for (String item : hatedItems) {
            addHatedItem(item);
        }
    }
    
    /**
     * Load Emily's sprite from the file system
     */
    private void loadSprite() {
        String[] pathsToTry = {
            "RES/ENTITY/NPC/emily.png",
            "RES\\ENTITY\\NPC\\emily.png",
            "./RES/ENTITY/NPC/emily.png",
            ".\\RES\\ENTITY\\NPC\\emily.png"
        };
        
        boolean loaded = false;
        for (String path : pathsToTry) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    sprite = ImageIO.read(file);
                    System.out.println("Loaded Emily sprite from: " + path);
                    loaded = true;
                    break;
                }
            } catch (IOException e) {
                System.out.println("Could not load Emily sprite from: " + path);
            }
        }
        
        if (!loaded) {
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                sprite = ImageIO.read(classLoader.getResourceAsStream("RES/ENTITY/NPC/emily.png"));
                System.out.println("Loaded Emily sprite from resource stream");
                loaded = true;
            } catch (Exception e) {
                System.out.println("Could not load Emily sprite from resource stream: " + e.getMessage());
            }
        }
        if (!loaded || sprite == null) {
            System.err.println("Failed to load Emily sprite from all paths. Creating placeholder.");
            createPlaceholderSprite();
        }
    }
    
    /**
     * Create a placeholder sprite when the image file can't be loaded
     */
    private void createPlaceholderSprite() {
        System.out.println("Creating placeholder sprite for Emily");
        
        sprite = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprite.createGraphics();
        g.setColor(java.awt.Color.BLUE);
        g.fillRect(0, 0, 48, 48);
        g.setColor(java.awt.Color.YELLOW);
        g.drawString("EMILY", 5, 24);
        g.dispose();
    }
    
    /**
     * Update method for Emily's behavior
     * This will be called by the game loop
     */
    public void update() {
        incrementSpriteCounter();
        
        if (getSpriteCounter() > 60) {  
            if (getSpriteNum() == 1) {
                setSpriteNum(0);
            } else {
                setSpriteNum(1);
            }
            setSpriteCounter(0);
        }
    }
    
    /**
     * Custom interaction method for Emily
     */
    @Override
    public void interact(Player player) {
        System.out.println("Emily says: I just planted some new seeds in my garden! Have you caught any interesting fish lately?");
 
        updateRelationshipStatus();
    }
    
    /**
     * Update relationship status based on heart points
     */
    private void updateRelationshipStatus() {
        if (getHeartPoints() < 100) {
            setRelationshipStatus("single");
        }
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
     * Get Emily's sprite for rendering
     * 
     * @return The sprite image
     */
    public BufferedImage getSprite() {
        if (sprite == null) {
            createPlaceholderSprite();
        }
        return sprite;
    }
    
    /**
     * Draw the Emily NPC
     * 
     * @param g2 Graphics2D object used for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image = getSprite();
        
        int screenX = getWorldX() - gp.getCameraX();
        int screenY = getWorldY() - gp.getCameraY();
        int tileSize = gp.getTileSize();
        if (getWorldX() + tileSize > gp.getCameraX() &&
            getWorldX() - tileSize < gp.getCameraX() + gp.getScreenWidth() &&
            getWorldY() + tileSize > gp.getCameraY() &&
            getWorldY() - tileSize < gp.getCameraY() + gp.getScreenHeight()) {
            
            g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
        }
    }
}
