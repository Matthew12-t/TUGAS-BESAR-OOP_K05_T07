package SRC.ENTITY;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import SRC.ITEMS.Item;
import SRC.ITEMS.Fish;
import SRC.MAIN.GamePanel;
import SRC.MAIN.KeyHandler;
import SRC.MAIN.MouseHandler;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private GamePanel gp;
    private KeyHandler keyH;
    private MouseHandler mouseHandler;    
    private PlayerAction playerAction;
    private int energy;
    private final int MAX_ENERGY = 100; 
    private int gold; 
    private final int TOTAL_FRAMES = 2;
    private boolean married;
    private String spouseName; 
    private int totalIncome;
    private int totalExpenditure;
    private int daysPlayed;
    private int totalCropsHarvested;
    private int totalFishCaught;
    private List<Fish> caughtFish;
    private String playerName = "Farmer";
    private String farmName = "My Farm";
    private String gender = "BOY"; 
    private String favoriteItem = "Parsnip";
    private Rectangle solidArea;
    private boolean showHitbox = false; 
    private BufferedImage[] up;
    private BufferedImage[] down;
    private BufferedImage[] left;
    private BufferedImage[] right;
    private final int visualScale = 1;
    private int playerVisualWidth;
    private int playerVisualHeight;
    private final float collisionScale = 0.6f; 
    private int collisionWidth;
    private int collisionHeight;
    private int collisionOffsetX;
    private int collisionOffsetY;


    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseHandler) {
        super(gp, 100, 100); 
        this.gp = gp;          this.keyH = keyH;
        this.married = false;
        this.spouseName = null; 
        this.mouseHandler = mouseHandler;
        this.energy = 100; 
        this.gold = 500; 
        this.playerAction = new PlayerAction(gp, this);
        

        this.totalIncome = 0;
        this.totalExpenditure = 0;
        this.daysPlayed = 0;
        this.totalCropsHarvested = 0;
        this.totalFishCaught = 0;
        this.caughtFish = new ArrayList<>();
        

        this.playerVisualWidth = gp.getTileSize() * visualScale; 
        this.playerVisualHeight = gp.getTileSize() * visualScale; 
        this.collisionWidth = (int)(gp.getTileSize() * collisionScale); 
        this.collisionHeight = (int)(gp.getTileSize() * collisionScale); 
        
        this.collisionOffsetX = (playerVisualWidth - collisionWidth) / 2; 
        this.collisionOffsetY = (playerVisualHeight - collisionHeight) / 2; 
        
        solidArea = new Rectangle(collisionOffsetX, collisionOffsetY, collisionWidth, collisionHeight);

        up = new BufferedImage[TOTAL_FRAMES];
        down = new BufferedImage[TOTAL_FRAMES];
        left = new BufferedImage[TOTAL_FRAMES];
        right = new BufferedImage[TOTAL_FRAMES];

        setDefaultValues();
        getPlayerImage();
    }
    
    public int getEnergy() {
        return energy;
    }    public void setEnergy(int energy) {
        if (energy < -30) {
            this.energy = -30; 
        } else if (energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY; 
        } else {
            this.energy = energy;
        }
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        if (gold < 0) {
            this.gold = 0; 
        } else {
            this.gold = gold;
        }
    }

    public void addGold(int amount) {
        if (amount > 0) {
            this.gold += amount;
            this.totalIncome += amount; 
        }
    }

    public boolean spendGold(int amount) {
        if (amount > 0 && this.gold >= amount) {
            this.gold -= amount;
            this.totalExpenditure += amount; 
            return true; 
        }
        return false; 
    }

    public void addItemToInventory(Item item) {
        playerAction.addItemToInventory(item);
    }
    
    public void removeItemFromInventory(int slotIndex) {
        playerAction.removeInventoryItem(slotIndex);
    }
    
    public void removeOneItemFromInventory(int slotIndex) {
        playerAction.removeOneInventoryItem(slotIndex);
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName != null ? playerName : "Farmer";
    }
    
    public String getFarmName() {
        return farmName;
    }
    
    public void setFarmName(String farmName) {
        this.farmName = farmName != null ? farmName : "My Farm";
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender != null && (gender.equals("BOY") || gender.equals("GIRL")) ? gender : "BOY";
    }
    
    public String getFavoriteItem() {
        return favoriteItem;
    }
    
    public void setFavoriteItem(String favoriteItem) {
        this.favoriteItem = favoriteItem != null ? favoriteItem : "Parsnip";
    }
    
    public int getDaysPlayed() {
        return daysPlayed;
    }
    
    public int getTotalIncome() {
        return totalIncome;
    }
    
    public int getTotalExpenditure() {
        return totalExpenditure;
    }
    
    public int getTotalCropsHarvested() {
        return totalCropsHarvested;
    }
    
    public int getTotalFishCaught() {
        return totalFishCaught;
    }
    
    public boolean isMarried() {
        return married;
    }
    
    public Item[] getInventoryItems() {
        return playerAction.getInventoryItems();
    }
    
    public int[] getInventoryQuantities() {
        return playerAction.getInventoryQuantities();
    }
    public PlayerAction getPlayerAction() {
        return playerAction;
    }

    public int getCurrentEnergy() {
        return energy;
    }
    
    public int getMaxEnergy() {
        return MAX_ENERGY;
    }
      public boolean consumeEnergy(int amount) {
        if (energy >= amount) {
            energy -= amount;
            if (energy < -30) energy = -30; 
            return true;
        } else {

            energy -= amount;
            if (energy < -30) energy = -30; 
            return true;
        }
    }
    
    public void restoreEnergy(int amount) {
        energy += amount;
        if (energy > MAX_ENERGY) {
            energy = MAX_ENERGY;
        }
    }      
    
    public boolean hasEnoughEnergy(int requiredEnergy) {

        final int LOWER_ENERGY_BOUND = -20;
        return (energy - requiredEnergy) > LOWER_ENERGY_BOUND;
    }
    
    public int getLowerEnergyBound() {
        return -20;
    }
      public double getEnergyPercentage() {

        double percentage = (double) energy / MAX_ENERGY;
        return Math.max(0.0, percentage); 
    }
    
    public Rectangle getSolidArea() {
        return solidArea;
    }
      
    public void toggleHitbox() {
        showHitbox = !showHitbox;
    }

    public boolean checkCollision() {

        int tileSize = gp.getTileSize();
        

        int hitboxWorldX = getWorldX() + collisionOffsetX;
        int hitboxWorldY = getWorldY() + collisionOffsetY;
        

        int hitboxLeftCol = hitboxWorldX / tileSize;
        int hitboxTopRow = hitboxWorldY / tileSize;
        int hitboxRightCol = (hitboxWorldX + collisionWidth - 1) / tileSize;
        int hitboxBottomRow = (hitboxWorldY + collisionHeight - 1) / tileSize;
        

        SRC.MAP.Map currentMap = gp.getCurrentMap();

        for (int row = hitboxTopRow; row <= hitboxBottomRow; row++) {
            for (int col = hitboxLeftCol; col <= hitboxRightCol; col++) {
                if (currentMap.hasCollision(col, row)) {
                    return true;
                }
            }
        }
        
        if (checkNPCCollision()) {
            return true;
        }
        
        return false;
    }
    
    private boolean checkNPCCollision() {
        SRC.MAP.Map currentMap = gp.getCurrentMap();

        if (currentMap instanceof SRC.MAP.NPC_HOUSE.NPCHouseMap) {
            SRC.MAP.NPC_HOUSE.NPCHouseMap npcHouseMap = (SRC.MAP.NPC_HOUSE.NPCHouseMap) currentMap;
            java.util.ArrayList<NPCEntity> npcs = null;
            if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.AbigailHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.AbigailHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.DascoHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.DascoHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.EmilyHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.EmilyHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.CarolineHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.CarolineHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.PerryHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.PerryHouseMap) npcHouseMap).getNPCs();
            } else if (npcHouseMap instanceof SRC.MAP.NPC_HOUSE.MayorTadiHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.MayorTadiHouseMap) npcHouseMap).getNPCs();
            }
            if (npcs != null) {
                for (NPCEntity npc : npcs) {
                    if (npc.isSolid() && checkCollisionWithNPC(npc)) {
                        return true;
                    }
                }
            }
        }

        else if (currentMap instanceof SRC.MAP.StoreMap) {
            SRC.MAP.StoreMap storeMap = (SRC.MAP.StoreMap) currentMap;
            java.util.ArrayList<NPCEntity> npcs = storeMap.getNPCs();
            if (npcs != null) {
                for (NPCEntity npc : npcs) {
                    if (npc.isSolid() && checkCollisionWithNPC(npc)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean checkCollisionWithNPC(NPCEntity npc) {
        java.awt.Rectangle playerBounds = new java.awt.Rectangle(
            getWorldX() + collisionOffsetX,
            getWorldY() + collisionOffsetY,
            collisionWidth,
            collisionHeight
        );
        java.awt.Rectangle npcBounds = npc.getCollisionBounds();
        return playerBounds.intersects(npcBounds);
    }
    
    public int getPlayerVisualWidth() {
        return playerVisualWidth;
    }
    
    public int getPlayerVisualHeight() {
        return playerVisualHeight;
    }
    
    public void setDefaultValues() {
        setWorldX(100); 
        setWorldY(150);
        setSpeed(4);
        setDirection("down"); 
    }
    
    public void getPlayerImage() {
        try {

            if(gender.equals("BOY")){
                for (int i = 0; i < TOTAL_FRAMES; i++) {
                    down[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/BOY/boy-walk-" + (i+1) + "-100.png"));
                    up[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/BOY/boy-walk-back-" + (i+1) + "-100.png"));
                    left[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/BOY/boy-walk-left-" + (i+1) + "-100.png"));
                    right[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/BOY/boy-walk-right-" + (i+1) + "-100.png"));
                }
            } 
            else {                
                for (int i = 0; i < TOTAL_FRAMES; i++) {
                    down[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/GIRL/girl-walk-" + (i+1) + "-100.png"));
                    up[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/GIRL/girl-walk-back-" + (i+1) + "-100.png"));
                    left[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/GIRL/girl-walk-left-" + (i+1) + "-100.png"));
                    right[i] = ImageIO.read(getClass().getResourceAsStream("/RES/ENTITY/PLAYER/GIRL/girl-walk-right-" + (i+1) + "-100.png"));
                }
            } 
        }     
        catch (IOException e) {
            System.err.println("Error loading player images: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
             System.err.println("Resource not found: Check player image paths in RES folder. Make sure RES is in your classpath.");
             e.printStackTrace();
        }
    }
    
    public void update() {
        boolean moving = false;
        
        int originalX = getWorldX();
        int originalY = getWorldY();

        if (keyH.upPressed) {
            setDirection("up");
            setWorldY(getWorldY() - getSpeed()); 
            moving = true;
        } else if (keyH.downPressed) { 
            setDirection("down");
            setWorldY(getWorldY() + getSpeed()); 
            moving = true;
        } else if (keyH.leftPressed) { 
            setDirection("left");
            setWorldX(getWorldX() - getSpeed()); 
            moving = true;
        } else if (keyH.rightPressed) { 
            setDirection("right");
            setWorldX(getWorldX() + getSpeed()); 
            moving = true;
        }
        

        if (checkCollision()) {

            setWorldX(originalX);
            setWorldY(originalY);
        }
        

        int worldMargin = -70; 
        if (getWorldX() < worldMargin) {
            setWorldX(worldMargin);
        } else if (getWorldX() > gp.getMaxWorldWidth() - playerVisualWidth - worldMargin) {
            setWorldX(gp.getMaxWorldWidth() - playerVisualWidth - worldMargin);
        }
        
        if (getWorldY() < worldMargin) {
            setWorldY(worldMargin);
        } else if (getWorldY() > gp.getMaxWorldHeight() - playerVisualHeight - worldMargin) {
            setWorldY(gp.getMaxWorldHeight() - playerVisualHeight - worldMargin);
        }
        
        if (mouseHandler.isHasTarget()) {
            moving = true; 
            
            
            int targetWorldX = mouseHandler.getTargetX();
            int targetWorldY = mouseHandler.getTargetY();

            
            int playerCenterX = getWorldX() + playerVisualWidth / 2;
            int playerCenterY = getWorldY() + playerVisualHeight / 2;
            int dx = targetWorldX - playerCenterX;
            int dy = targetWorldY - playerCenterY;

            
            double distance = Math.sqrt(dx * dx + dy * dy);

            
            if (distance > 0) { 
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) {
                        setDirection("right");
                    } else {
                        setDirection("left");
                    }
                } else {
                    if (dy > 0) {
                        setDirection("down");
                    } else {
                        setDirection("up");
                    }
                }

                
                if (distance < getSpeed()) {
                    
                    setWorldX(targetWorldX - playerVisualWidth / 2);
                    setWorldY(targetWorldY - playerVisualHeight / 2);
                    mouseHandler.setHasTarget(false); 
                    moving = false; 
                    System.out.println("Player reached target tile center at: " + targetWorldX + ", " + targetWorldY);
                } else {
                    
                    double moveRatio = getSpeed() / distance;
                    int newWorldX = getWorldX() + (int)(dx * moveRatio);
                    int newWorldY = getWorldY() + (int)(dy * moveRatio);
                    
                    
                    int moveOriginalX = getWorldX();
                    int moveOriginalY = getWorldY();
                    
                    
                    setWorldX(newWorldX);
                    setWorldY(newWorldY);                    
                    if (checkCollision()) {
                        
                        setWorldX(moveOriginalX);
                        setWorldY(moveOriginalY);
                        
                        
                        mouseHandler.setHasTarget(false);
                        moving = false;
                        System.out.println("Collision detected, stopped moving to target");
                    }
                    
                    
                    boolean hitBoundary = false;
                    
                    
                    if (getWorldX() < worldMargin) {
                        setWorldX(worldMargin);
                        hitBoundary = true;
                    } else if (getWorldX() > gp.getMaxWorldWidth() - playerVisualWidth - worldMargin) {
                        setWorldX(gp.getMaxWorldWidth() - playerVisualWidth - worldMargin);
                        hitBoundary = true;
                    }
                    
                    
                    if (getWorldY() < worldMargin) {
                        setWorldY(worldMargin);
                        hitBoundary = true;
                    } else if (getWorldY() > gp.getMaxWorldHeight() - playerVisualHeight - worldMargin) {
                        setWorldY(gp.getMaxWorldHeight() - playerVisualHeight - worldMargin);
                        hitBoundary = true;
                    }
                    
                    
                    if (hitBoundary) {
                        mouseHandler.setHasTarget(false);
                        moving = false;
                        System.out.println("Hit boundary, stopped moving to target");
                    }
                }
            } else {
                
                mouseHandler.setHasTarget(false);
                moving = false;
            }
        } 
        
        
        if (moving) {
            incrementSpriteCounter();
            
            
            if (getSpriteCounter() > 8) { 
                setSpriteNum((getSpriteNum() + 1) % TOTAL_FRAMES);
                setSpriteCounter(0);
            }
        } else {
            
            setSpriteNum(0);
            setSpriteCounter(0); 
        }
    }

    
    public void draw(Graphics2D g2, int screenX, int screenY) {
        BufferedImage image = null;

        
        switch (super.getDirection()) {
            case "up":
                image = up[super.getSpriteNum()];
                break;
            case "down":
                image = down[super.getSpriteNum()];
                break;
            case "left":
                image = left[super.getSpriteNum()];
                break;
            case "right":
                image = right[super.getSpriteNum()];
                break;
        }
        
        
        if (image != null) {
            g2.drawImage(image, screenX, screenY, playerVisualWidth, playerVisualHeight, null);
        } else {
            
            g2.setColor(Color.white);
            g2.fillRect(screenX, screenY, playerVisualWidth, playerVisualHeight);
        }
        
        
        if (showHitbox) {
            
            g2.setColor(new Color(0, 0, 255, 50)); 
            g2.drawRect(screenX, screenY, playerVisualWidth, playerVisualHeight);
            
            
            g2.setColor(new Color(255, 0, 0, 100)); 
            g2.fillRect(screenX + collisionOffsetX, screenY + collisionOffsetY, 
                     collisionWidth, collisionHeight);
                     
            
            g2.setColor(Color.RED);
            g2.drawRect(screenX + collisionOffsetX, screenY + collisionOffsetY, 
                     collisionWidth, collisionHeight);
                     
            
            int centerX = screenX + collisionOffsetX + collisionWidth/2;
            int centerY = screenY + collisionOffsetY + collisionHeight/2;
            g2.drawLine(centerX - 5, centerY, centerX + 5, centerY); 
            g2.drawLine(centerX, centerY - 5, centerX, centerY + 5); 
            
            
            g2.setColor(Color.WHITE);
            g2.drawString(collisionWidth + "x" + collisionHeight, 
                      screenX + collisionOffsetX, 
                      screenY + collisionOffsetY - 5);

            
            int tileSize = gp.getTileSize();
            
            
            int hitboxWorldX = getWorldX() + collisionOffsetX;
            int hitboxWorldY = getWorldY() + collisionOffsetY;
            int hitboxLeftCol = hitboxWorldX / tileSize;
            int hitboxTopRow = hitboxWorldY / tileSize;
            int hitboxRightCol = (hitboxWorldX + collisionWidth - 1) / tileSize;            
            int hitboxBottomRow = (hitboxWorldY + collisionHeight - 1) / tileSize;
            
            
            g2.setColor(new Color(0, 255, 0, 100)); 
            
            
            for (int row = hitboxTopRow; row <= hitboxBottomRow; row++) {
                for (int col = hitboxLeftCol; col <= hitboxRightCol; col++) {
                    int tileScreenX = (col * tileSize) - gp.getCameraX();
                    int tileScreenY = (row * tileSize) - gp.getCameraY();
                    g2.drawRect(tileScreenX, tileScreenY, tileSize, tileSize);
                }
            }
        }
        
        if (mouseHandler.isHasTarget()) {
            
            Color targetColor = new Color(255, 255, 255, 100); 
            g2.setColor(targetColor);
            
            
            int targetWorldX = mouseHandler.getTargetX();
            int targetWorldY = mouseHandler.getTargetY();
            
            
            int targetScreenX = targetWorldX - gp.getCameraX();
            int targetScreenY = targetWorldY - gp.getCameraY();
            
            
            int tileSize = gp.getTileSize();
            g2.fillRect(targetScreenX - tileSize/2, targetScreenY - tileSize/2, tileSize, tileSize);            
            g2.setColor(new Color(255, 255, 255, 150));
            g2.drawRect(targetScreenX - tileSize/2, targetScreenY - tileSize/2, tileSize, tileSize);
        }
    }
      
    
    private SRC.ITEMS.Tool currentHoldingTool = null;
    
    private SRC.ITEMS.Seed currentHoldingSeed = null;
    
    public SRC.ITEMS.Tool getCurrentHoldingTool() {
        return currentHoldingTool;
    }

    public void setCurrentHoldingTool(SRC.ITEMS.Tool tool) {
        
        if (tool != null && this.currentHoldingSeed != null) {
            this.currentHoldingSeed = null;
            System.out.println("Dropped held seed to hold tool: " + tool.getName());
        }
        
        this.currentHoldingTool = tool;
        System.out.println("Player is now holding: " + (tool != null ? tool.getName() : "nothing"));
    }
    
    /**
     * Check if player is holding a specific tool
     * @param toolName Name of the tool to check
     * @return true if holding the specified tool
     */
    public boolean isHolding(String toolName) {
        return currentHoldingTool != null && currentHoldingTool.getName().equals(toolName);
    }
    
    /**
     * Check if player is holding any tool
     * @return true if holding any tool
     */
    public boolean isHoldingAnyTool() {
        return currentHoldingTool != null;
    }
    
    
    /**
     * Get currently holding seed
     * @return Current holding seed or null if none
     */
    public SRC.ITEMS.Seed getCurrentHoldingSeed() {
        return currentHoldingSeed;
    }
      /**
     * Set currently holding seed
     * @param seed Seed to hold
     */
    public void setCurrentHoldingSeed(SRC.ITEMS.Seed seed) {
        
        if (seed != null && this.currentHoldingTool != null) {
            this.currentHoldingTool = null;
            System.out.println("Dropped held tool to hold seed: " + seed.getName());
        }
        
        this.currentHoldingSeed = seed;
        System.out.println("Player is now holding seed: " + (seed != null ? seed.getName() : "nothing"));
    }
    
    /**
     * Check if player is holding a specific seed
     * @param seedName Name of the seed to check
     * @return true if holding the specified seed
     */
    public boolean isHoldingSeed(String seedName) {
        return currentHoldingSeed != null && currentHoldingSeed.getName().equals(seedName);
    }
    
    /**
     * Check if player is holding any seed
     * @return true if holding any seed
     */
    public boolean isHoldingAnySeed() {
        return currentHoldingSeed != null;
    }
    
    /**
     * Get the currently held item (for seed detection in planting logic)
     * @return Item currently selected in inventory, or null if none selected
     */
    public Item getCurrentlyHeldItem() {
        int selectedSlotIndex = gp.getMouseHandler().getSelectedSlotIndex();
        if (selectedSlotIndex >= 0) {
            Item[] items = getInventoryItems();
            if (selectedSlotIndex < items.length && items[selectedSlotIndex] != null) {
                return items[selectedSlotIndex];
            }
        }        return null;
    }

    
    public void incrementDaysPlayed() {
        this.daysPlayed++;
    }

    public void incrementCropsHarvested(int amount) {
        this.totalCropsHarvested += amount;
    }

    public void incrementFishCaught(Fish fish) {
        this.totalFishCaught++;
        if (fish != null) {
            this.caughtFish.add(fish);
        }
    }

    public List<Fish> getCaughtFish() {
        return new ArrayList<>(caughtFish); 
    }    public void setMarried(boolean married) {
        this.married = married;
    }
    
    public String getSpouseName() {
        return spouseName;
    }
    
    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }
    
    public void setMarried(boolean married, String spouseName) {
        this.married = married;
        this.spouseName = spouseName;
    }
}