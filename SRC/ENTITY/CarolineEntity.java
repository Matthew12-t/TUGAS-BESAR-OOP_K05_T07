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


public class CarolineEntity extends NPCEntity {
    private BufferedImage sprite;
    
 
    public CarolineEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Caroline", "Caroline's House", 
              "Caroline adalah seorang tukang kayu lokal di Spakbor Hills.");
        
        setItemPreferences();
        loadSprite();
        setSpeed(0);
    }    

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
            System.out.println("Caroline: Eh, kamu siapa? Jangan-jangan kamu?");
        } else if (getHeartPoints() <= 50) {
            System.out.println("Caroline: Oh, kamu yang sering lewat depan rumah ya? Santai aja, aku nggak gigit kok!");
        } else if (getHeartPoints() <= 75) {
            System.out.println("Caroline: Aku lagi nyari kayu nih, kamu punya? Atau cuma numpang ngobrol doang?");
        } else if (getHeartPoints() <= 100) {
            System.out.println("Caroline: Wah, makin sering ketemu nih. Jangan-jangan kamu ngefans sama aku ya?");
        } else if (getHeartPoints() <= 125) {
            System.out.println("Caroline: Kalau tiap hari ngobrol gini, nanti aku baper lho!");
        } else {
            System.out.println("Caroline: Duh, kamu lucu deh. Gimana kalau kita duet joget TikTok?");
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
            return "Caroline: Wah, akan ku ubah menjadi suatu karya seni. " + baseMsg;
        } else if (baseMsg.contains("likes")) {
            return "Caroline: Terima kasih, aku suka ini! " + baseMsg;
        } else if (baseMsg.contains("hates")) {
            return "Caroline: Hmm... aku tidak suka Makanan PEDASSS!. " + baseMsg;
        } else {
            return "Caroline: Terima kasih, tapi aku lebih suka sesuatu yang lain. " + baseMsg;
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
