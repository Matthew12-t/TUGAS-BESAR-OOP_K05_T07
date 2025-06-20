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


public class EmilyEntity extends NPCEntity {
    private BufferedImage sprite;
    

    public EmilyEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Emily", "Emily's House", 
              "Emily adalah seorang koki yang juga bekerja dan tinggal di toko lokal Spakbor Hills.");

        setItemPreferences();
        loadSprite();
        setSpeed(0);
    }      
    
    private void setItemPreferences() {
        Set<String> lovedItemNames = Set.of(
            "Parsnip Seed", "Cauliflower Seed", "Potato Seed", "Wheat Seed",
            "Blueberry Seed", "Tomato Seed", "Hot Pepper Seed", "Melon Seed",
            "Cranberry Seed", "Pumpkin Seed", "Grape Seed"
        );
        for (String itemName : lovedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLovedItem(item);
            }
        }
        
        Set<String> likedItemNames = Set.of("Catfish", "Salmon", "Sardine");
        for (String itemName : likedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addLikedItem(item);
            }
        }
        
        Set<String> hatedItemNames = Set.of("Coal", "Wood");
        for (String itemName : hatedItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null) {
                addHatedItem(item);
            }
        }
    }
    

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
            System.out.println("Emily: Mau kupukul pakai kayu?");
        } else if (getHeartPoints() <= 50) {
            System.out.println("Emily: Kamu pelanggan toko yang baik.");
        } else if (getHeartPoints() <= 75) {
            System.out.println("Emily: Mau kubuatkan KUE?");
        }else if (getHeartPoints() <= 100) {
            System.out.println("Emily: kamu adalah orang yang baik, aku sayang kamu, EH.");
        } else if (getHeartPoints() <= 125) {
            System.out.println("Emily: Hubungan kita ini apaa sih?? .");
        } else{
            System.out.println("Emily: Pliss nikahin aku dong  :)");
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
            return "Emily: Ini bahan masakan favoritku! " + baseMsg;
        } else if (baseMsg.contains("likes")) {
            return "Emily: Terima kasih, aku bisa memasak dengan ini! " + baseMsg;
        } else if (baseMsg.contains("hates")) {
            return "Emily: Oh, aku tidak suka ini... " + baseMsg;
        } else if (baseMsg.contains("accepts")) {
            return "Emily: Terima kasih, tapi aku harap lain kali lebih enak! " + baseMsg;
        } else {return baseMsg; }
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
