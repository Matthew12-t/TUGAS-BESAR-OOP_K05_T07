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


public class MayorTadiEntity extends NPCEntity {
    private BufferedImage sprite;
    

    public MayorTadiEntity(GamePanel gp, int worldX, int worldY) {
        super(gp, worldX, worldY, "Mayor Tadi", "Mayor Tadi's House", 
              "Mayor Tadi adalah wali kota Spakbor Hills.");
        
        setItemPreferences();
        
        loadSprite();
        setSpeed(0);
    }    

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

        Set<String> allItemNames = GameData.getAllItemNames();
        for (String itemName : allItemNames) {
            Item item = GameData.getItem(itemName, 1);
            if (item != null && !lovedItemNames.contains(itemName) && !likedItemNames.contains(itemName)) {
                addHatedItem(item);
            }
        }
    }
    

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
            System.out.println("Mayor Tadi: Siapa lu, gw kenalnya cuma si wowok.");
        } else if (getHeartPoints() <= 50) {
            System.out.println("Mayor Tadi: Kamu adalah seorang warga yang baik.");
        } else if (getHeartPoints() <= 75) {
            System.out.println("Mayor Tadi: Aku punya teman yang bernama Wowok, dia sangat baik.");
        }else if (getHeartPoints() <= 100) {
            System.out.println("Mayor Tadi: we wok detok not onle tok de tok.");
        } else if (getHeartPoints() <= 125) {
            System.out.println("Mayor Tadi: Hubungan kita ini apaa sih?? .");
        } else{
            System.out.println("Mayor Tadi: Pliss nikahin aku dong  :)");
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
            return "Mayor Tadi: Luar biasa! Ini hadiah yang sangat mewah! " + baseMsg;
        } else if (baseMsg.contains("likes")) {
            return "Mayor Tadi: Aku suka jenis ikan ini! " + baseMsg;
        } else if (baseMsg.contains("hates")) {
            return "Mayor Tadi: Cuih, Ini... tidak cocok untukku, dasar rakyat miskin. " + baseMsg;
        } else {
            return "Mayor Tadi: Terima kasih, tapi aku harap lain kali lebih spesial. " + baseMsg;
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
