package SRC.ENTITY;

import java.util.Set;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;
import SRC.DATA.GameData;
import SRC.ITEMS.Item;
import java.awt.Graphics2D;

/**
 * MayorTadiEntity - Represents the Mayor Tadi NPC in the game
 */
public class MayorTadiEntity extends NPCEntity {
    private BufferedImage sprite;
    
    /**
     * Constructor for MayorTadiEntity
     * 
     * @param gp GamePanel reference
     * @param worldX X position in the world
     * @param worldY Y position in the world
     */
    public MayorTadiEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Mayor Tadi", "Mayor Tadi's House", 
              "Mayor Tadi adalah wali kota Spakbor Hills.");
        
        setItemPreferences();
        
        loadSprite();
        setSpeed(0);
    }    /**
     * Set up Mayor Tadi's item preferences
     */
    private void setItemPreferences() {
        Set<String> lovedItemNames = Set.of("Legend");
        for (String itemName : lovedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLovedItem(item);
            }
        }
        
        Set<String> likedItemNames = Set.of("Angler", "Crimsonfish", "Glacierfish");
        for (String itemName : likedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLikedItem(item);
            }
        }
        
        Set<String> hatedItemNames = Set.of("Hot Pepper", "Cauliflower", "Coal", "Wood", "Grape", "Wheat");
        for (String itemName : hatedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addHatedItem(item);
            }
        }
    }
    
    /**
     * Load Mayor Tadi's sprite from the file system
     */
    private void loadSprite() {
        String[] pathsToTry = {
            "RES/ENTITY/NPC/mayortadi.png",
            "RES\\ENTITY\\NPC\\mayortadi.png",
            "./RES/ENTITY/NPC/mayortadi.png",
            ".\\RES\\ENTITY\\NPC\\mayortadi.png"
        };
        
        boolean loaded = false;
        
        for (String path : pathsToTry) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    sprite = ImageIO.read(file);
                    System.out.println("Loaded Mayor Tadi sprite from: " + path);
                    loaded = true;
                    break;
                }
            } catch (IOException e) {
                System.out.println("Could not load Mayor Tadi sprite from: " + path);
            }
        }
        if (!loaded) {
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                sprite = ImageIO.read(classLoader.getResourceAsStream("RES/ENTITY/NPC/mayortadi.png"));
                System.out.println("Loaded Mayor Tadi sprite from resource stream");
                loaded = true;
            } catch (Exception e) {
                System.out.println("Could not load Mayor Tadi sprite from resource stream: " + e.getMessage());
            }
        }
        if (!loaded || sprite == null) {
            System.err.println("Failed to load Mayor Tadi sprite from all paths. Creating placeholder.");
            createPlaceholderSprite();
        }
    }
    
    /**
     * Create a placeholder sprite when the image file can't be loaded
     */
    private void createPlaceholderSprite() {
        System.out.println("Creating placeholder sprite for Mayor Tadi");
        
        sprite = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprite.createGraphics();
        g.setColor(java.awt.Color.RED);
        g.fillRect(0, 0, 48, 48);
        g.setColor(java.awt.Color.WHITE);
        g.drawString("MAYOR", 5, 24);
        g.dispose();
    }
    
    /**
     * Update method for Mayor Tadi's behavior
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
     * Custom interaction method for Mayor Tadi
     */
    @Override
    public void interact(Player player) {
        System.out.println("Mayor Tadi says: Have you caught any legendary fish lately?");
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
     * @param item the item being given
     */
    @Override
    public void receiveGift(Item item) {
        super.receiveGift(item);
        updateRelationshipStatus();
    }
    
    /**
     * Get Mayor Tadi's sprite for rendering
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
     * Draw the Mayor Tadi NPC
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
