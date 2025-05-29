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
 * AbigailEntity - Represents the Abigail NPC in the game
 */
public class AbigailEntity extends NPCEntity {
    private BufferedImage sprite;
    
    /**
     * Constructor for AbigailEntity
     * 
     * @param gp GamePanel reference
     * @param worldX X position in the world
     * @param worldY Y position in the world
     */
    public AbigailEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Abigail", "Abigail's House", 
              "Abigail adalah seorang perempuan yang outgoing dan senang melakukan eksplorasi alam.");
        
        setItemPreferences();
        loadSprite();
        setSpeed(0);
    }    /**
     * Set up Abigail's item preferences
     */
    private void setItemPreferences() {
        
        Set<String> lovedItemNames = Set.of("Blueberry", "Melon", "Pumpkin", "Grape", "Cranberry");
        for (String itemName : lovedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLovedItem(item);
            }
        }
        
        Set<String> likedItemNames = Set.of("Baguette", "Pumpkin Pie", "Wine");
        for (String itemName : likedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLikedItem(item);
            }
        }
        
        Set<String> hatedItemNames = Set.of("Hot Pepper", "Cauliflower", "Parsnip", "Wheat");
        for (String itemName : hatedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addHatedItem(item);
            }
        }
    }
    
    /**
     * Load Abigail's sprite from the file system
     */
    private void loadSprite() {
        String[] pathsToTry = {
            "RES/ENTITY/NPC/abigail.png",
            "RES\\ENTITY\\NPC\\abigail.png",
            "./RES/ENTITY/NPC/abigail.png",
            ".\\RES\\ENTITY\\NPC\\abigail.png"
        };
        
        boolean loaded = false;
        
        for (String path : pathsToTry) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    sprite = ImageIO.read(file);
                    System.out.println("Loaded Abigail sprite from: " + path);
                    loaded = true;
                    break;
                }
            } catch (IOException e) {
                System.out.println("Could not load Abigail sprite from: " + path);
            }
        }
        if (!loaded) {
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                sprite = ImageIO.read(classLoader.getResourceAsStream("RES/ENTITY/NPC/abigail.png"));
                System.out.println("Loaded Abigail sprite from resource stream");
                loaded = true;
            } catch (Exception e) {
                System.out.println("Could not load Abigail sprite from resource stream: " + e.getMessage());
            }
        }
        if (!loaded || sprite == null) {
            System.err.println("Failed to load Abigail sprite from all paths. Creating placeholder.");
            createPlaceholderSprite();
        }
    }
    
    /**
     * Create a placeholder sprite when the image file can't be loaded
     */
    private void createPlaceholderSprite() {
        System.out.println("Creating placeholder sprite for Abigail");
        sprite = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprite.createGraphics();
        g.setColor(java.awt.Color.PINK);
        g.fillRect(0, 0, 48, 48);
        g.setColor(java.awt.Color.BLACK);
        g.drawString("ABIGAIL", 5, 24);
        g.dispose();
    }
    
    /**
     * Update method for Abigail's behavior
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
     * Custom interaction method for Abigail
     */
    @Override
    public void interact(Player player) {
        System.out.println("Abigail says: I was just thinking about having some pumpkin pie! Do you have any berries to share?");
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
    public String receiveGift(Item item) {
        String baseMsg = super.receiveGift(item);
        updateRelationshipStatus();
        if (baseMsg.contains("loves")) {
            return "Abigail: Wah, aku suka sekali! " + baseMsg;
        } else if (baseMsg.contains("likes")) {
            return "Abigail: Ini cukup menarik, terima kasih! " + baseMsg;
        } else if (baseMsg.contains("hates")) {
            return "Abigail: Uh... aku tidak suka ini... " + baseMsg;
        } else {
            return "Abigail: Terima kasih, tapi aku harap lain kali lebih baik! " + baseMsg;
        }
    }
    
    /**
     * Get Abigail's sprite for rendering
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
     * Draw the Abigail NPC
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
