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


public class PerryEntity extends NPCEntity {
    private BufferedImage sprite;
    

    public PerryEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Perry", "Perry's House", 
              "Perry adalah seorang penulis yang baru saja menerbitkan buku pertamanya");

        setItemPreferences();
        loadSprite();
        setSpeed(0);
    }    private void setItemPreferences() {
        Set<String> lovedItemNames = Set.of("Cranberry", "Blueberry");
        for (String itemName : lovedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLovedItem(item);
            }
        }
        
        Set<String> likedItemNames = Set.of("Wine");
        for (String itemName : likedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLikedItem(item);
            }
        }
        
        Set<String> hatedItemNames = Set.of("Carp", "Bullhead", "Chub", "Fish Sandwich", "Fish Stew", "Fish n' Chips",
                                       "Pufferfish", "Salmon", "Legend", "Angler", "Crimsonfish",
                                       "Glacierfish", "Catfish", "Sardine", "Flounder", "Halibut", 
                                       "Octopus", "Super Cucumber", "Fugu", "Sashimi");
        for (String itemName : hatedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addHatedItem(item);
            }
        }
    }
    
    private void loadSprite() {
        String[] pathsToTry = {
            "RES/ENTITY/NPC/perry.png",
            "RES\\ENTITY\\NPC\\perry.png",
            "./RES/ENTITY/NPC/perry.png",
            ".\\RES\\ENTITY\\NPC\\perry.png"
        };
        
        boolean loaded = false;
        for (String path : pathsToTry) {
            try {
                File file = new File(path);
                if (file.exists()) {
                    sprite = ImageIO.read(file);
                    System.out.println("Loaded Perry sprite from: " + path);
                    loaded = true;
                    break;
                }
            } catch (IOException e) {
                System.out.println("Could not load Perry sprite from: " + path);
            }
        }
        if (!loaded) {
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                sprite = ImageIO.read(classLoader.getResourceAsStream("RES/ENTITY/NPC/perry.png"));
                System.out.println("Loaded Perry sprite from resource stream");
                loaded = true;
            } catch (Exception e) {
                System.out.println("Could not load Perry sprite from resource stream: " + e.getMessage());
            }
        }
        if (!loaded || sprite == null) {
            System.err.println("Failed to load Perry sprite from all paths. Creating placeholder.");
            createPlaceholderSprite();
        }
    }
    
    private void createPlaceholderSprite() {
        System.out.println("Creating placeholder sprite for Perry");
        

        sprite = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprite.createGraphics();
        g.setColor(java.awt.Color.CYAN);
        g.fillRect(0, 0, 48, 48);
        g.setColor(java.awt.Color.BLACK);
        g.drawString("PERRY", 5, 24);
        g.dispose();
    }
    
    
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
    

    @Override
    public void interact(Player player) {
        super.interact(player);
        if (getHeartPoints() <= 25) {
            System.out.println("Perry: Ada yang bisa kubantu?");
        } else if (getHeartPoints() <= 50) {
            System.out.println("Perry: Kamu mau kutuliskan puisi?");
        } else if (getHeartPoints() <= 75) {
            System.out.println("Perry: Kamu sangat baik!!");
        }else if (getHeartPoints() <= 100) {
            System.out.println("Perry: Love comforteth like sunshine after rain..");
        } else if (getHeartPoints() <= 125) {
            System.out.println("Perry: I kiss thee with a most constant heart.");
        } else{
            System.out.println("Perry: I do love nothing in the world so well as you—is not that strange?");
        }
        updateRelationshipStatus();
    }

    private void updateRelationshipStatus() {
        if (getHeartPoints() < 100) {
            setRelationshipStatus("single");
        }
    }

    @Override
    public String receiveGift(Item item) {
        String baseMsg = super.receiveGift(item);
        updateRelationshipStatus();
        if (baseMsg.contains("loves")) {
            return "Perry: Wah, buah beri segar! Aku suka sekali!! " + baseMsg;
        } else if (baseMsg.contains("likes")) {
            return "Perry: Terima kasih, minuman ini cocok untuk ide menulis! " + baseMsg;
        } else if (baseMsg.contains("hates")) {
            return "Perry: sorry yee, aku ada alergi ikan. " + baseMsg;
        } else {
            return "Perry: Terima kasih, tapi aku lebih suka buah atau minuman. " + baseMsg;
        }
    }
    

    public BufferedImage getSprite() {
        if (sprite == null) {
            createPlaceholderSprite();
        }
        return sprite;
    }
    
 
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
