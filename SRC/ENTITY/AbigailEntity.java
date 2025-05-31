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


public class AbigailEntity extends NPCEntity {
    private BufferedImage sprite;
    

    public AbigailEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Abigail", "Abigail's House", 
              "Abigail adalah seorang perempuan yang outgoing dan senang melakukan eksplorasi alam.");
        
        setItemPreferences();
        loadSprite();
        setSpeed(0);
    }    

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
            System.out.println("Abigail: Eh, kamu siapa? Jangan-jangan kamu alien ya?");
        } else if (getHeartPoints() <= 50) {
            System.out.println("Abigail: Halo! Aku lagi cari makanan, kamu punya?");
        } else if (getHeartPoints() <= 75) {
            System.out.println("Abigail: Kadang aku suka main ke gunung, kamu mau ikut nggak?");
        } else if (getHeartPoints() <= 100) {
            System.out.println("Abigail: Kamu tau nggak, aku suka kamu... eh, bercanda ya!");
        } else if (getHeartPoints() <= 125) {
            System.out.println("Abigail: Kalau kita balapan, siapa yang menang ya? Kamu sih, kan juara di hatiku!");
        } else {
            System.out.println("Abigail: Duh, ngobrol sama kamu tuh seru banget. Kapan-kapan kita nikah yuk!");
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
            return "Abigail: Wah, aku suka sekali! " + baseMsg;
        } else if (baseMsg.contains("likes")) {
            return "Abigail: Ini cukup menarik, terima kasih! " + baseMsg;
        } else if (baseMsg.contains("hates")) {
            return "Abigail: Uh... aku tidak suka ini... " + baseMsg;
        } else {
            return "Abigail: Terima kasih, tapi aku harap lain kali lebih baik! " + baseMsg;
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
