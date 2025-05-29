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
 * CarolineEntity - Represents the Caroline NPC in the game
 */
public class CarolineEntity extends NPCEntity {
    private BufferedImage sprite;
    
    /**
     * Constructor for CarolineEntity
     * 
     * @param gp GamePanel reference
     * @param worldX X position in the world
     * @param worldY Y position in the world
     */
    public CarolineEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Caroline", "Caroline's House", 
              "Caroline adalah seorang tukang kayu lokal di Spakbor Hills.");
        
        setItemPreferences();
        loadSprite();
        setSpeed(0);
    }    /**
     * Set up Caroline's item preferences
     */
    private void setItemPreferences() {
        Set<String> lovedItemNames = Set.of("Wood", "Coal");
        for (String itemName : lovedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLovedItem(item);
            }
        }
        
        Set<String> likedItemNames = Set.of("Potato", "Wheat");
        for (String itemName : likedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLikedItem(item);
            }
        }
        
        Set<String> hatedItemNames = Set.of("Hot Pepper");
        for (String itemName : hatedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addHatedItem(item);
            }
        }
    }
    
    /**
     * Load Caroline's sprite from the file system
     */
    private void loadSprite() {
        String[] pathsToTry = {
            "RES/ENTITY/NPC/caroline.png",
            "RES\\ENTITY\\NPC\\caroline.png",
            "./RES/ENTITY/NPC/caroline.png",
            ".\\RES\\ENTITY\\NPC\\caroline.png"
        };
        
        boolean loaded = false;
        for (String path : pathsToTry) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    sprite = ImageIO.read(file);
                    System.out.println("Loaded Caroline sprite from: " + path);
                    loaded = true;
                    break;
                }
            } catch (IOException e) {
                System.out.println("Could not load Caroline sprite from: " + path);
            }
        }

        if (!loaded) {
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                sprite = ImageIO.read(classLoader.getResourceAsStream("RES/ENTITY/NPC/caroline.png"));
                System.out.println("Loaded Caroline sprite from resource stream");
                loaded = true;
            } catch (Exception e) {
                System.out.println("Could not load Caroline sprite from resource stream: " + e.getMessage());
            }
        }
        if (!loaded || sprite == null) {
            System.err.println("Failed to load Caroline sprite from all paths. Creating placeholder.");
            createPlaceholderSprite();
        }
    }
    
    /**
     * Create a placeholder sprite when the image file can't be loaded
     */
    private void createPlaceholderSprite() {
        System.out.println("Creating placeholder sprite for Caroline");
        sprite = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprite.createGraphics();
        g.setColor(java.awt.Color.GREEN);
        g.fillRect(0, 0, 48, 48);
        g.setColor(java.awt.Color.BLACK);
        g.drawString("CAROLIN", 5, 24);
        g.dispose();
    }
    
    /**
     * Update method for Caroline's behavior
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
     * Custom interaction method for Caroline
     */
    @Override
    public void interact(Player player) {
        System.out.println("Caroline says: I've been collecting firewood all day!");
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
     * Get Caroline's sprite for rendering
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
     * Draw the Caroline NPC
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
