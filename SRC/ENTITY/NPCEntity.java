package SRC.ENTITY;

import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

import SRC.MAIN.GamePanel;
import SRC.ITEMS.Item;

public class NPCEntity extends Entity implements NPC {    // Basic NPC properties
    private String name;
    private String location;
    private String description;
    private int heartPoints;
    private static final int MAX_HEART_POINTS = 150;
    private String relationshipStatus; 
    private int engagementDay; // Track the day when became fiance
    private List<Item> lovedItems;
    private List<Item> likedItems;
    private List<Item> hatedItems;
    
    private Rectangle solidArea;
    private boolean isSolid = true; 
    public NPCEntity(GamePanel gp, int worldX, int worldY, String name, String location, String description) {
        super(gp, worldX, worldY);
        this.name = name;
        this.location = location;
        this.description = description;
          this.heartPoints = 0; 
        this.relationshipStatus = "single";
        this.engagementDay = -1; // -1 means not engaged yet
        this.lovedItems = new ArrayList<>();
        this.likedItems = new ArrayList<>();
        this.hatedItems = new ArrayList<>();
        
        // Initialize collision area - NPC takes up the full tile size
        int tileSize = gp.getTileSize();
        this.solidArea = new Rectangle(0, 0, tileSize, tileSize);
    }
    public void update() {
        incrementSpriteCounter();
        if (getSpriteCounter() > 60) { 
            setSpriteNum((getSpriteNum() + 1) % 2);  
            setSpriteCounter(0);
        }
    }    /**
     * Draw the NPC on screen
     * @param g2 Graphics2D object for drawing
     */    public void draw(Graphics2D g2) {
        // Calculate screen position based on world position and camera
        int screenX = getWorldX() - gp.getCameraX();
        int screenY = getWorldY() - gp.getCameraY();
        
        // Debug - log visibility calculation values
        boolean isVisible = getWorldX() + gp.getTileSize() > gp.getCameraX() &&
                           getWorldX() - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
                           getWorldY() + gp.getTileSize() > gp.getCameraY() &&
                           getWorldY() - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight();
        
        // Print NPC visibility info when entering DascoHouseMap 
        if (name.equals("Dasco") && gp.getCurrentMap().getMapName().equals("Dasco's House")) {
            System.out.println("Dasco NPC draw check - worldX: " + getWorldX() + ", worldY: " + getWorldY() + 
                            ", cameraX: " + gp.getCameraX() + ", cameraY: " + gp.getCameraY() + 
                            ", visible: " + isVisible);
        }
        
        // Only draw if NPC is visible on screen
        if (isVisible) {
            // Default NPC rendering - draw a colored rectangle with name
            g2.setColor(Color.BLUE);
            g2.fillRect(screenX, screenY, gp.getTileSize(), gp.getTileSize());
            
            g2.setColor(Color.WHITE);
            g2.drawString(name.substring(0, Math.min(4, name.length())), 
                         screenX + 5, screenY + gp.getTileSize()/2);
        }
    }    
    @Override
    public String getNPCName() {
        return name;
    }
    
    @Override
    public String getLocation() {
        return location;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
      @Override
    public void interact(Player player) {
        setHeartPoints(getHeartPoints() + 10); 
        player.setEnergy(player.getEnergy() - 10);        
    }
    
    @Override
    public String getRelationshipStatus(Player player) {
        return "Relationship with player: " + relationshipStatus + 
               " (Heart Points: " + heartPoints + "/" + MAX_HEART_POINTS + ")";
    }
      @Override
    public List<Item> getLovedItems() {
        return lovedItems;
    }
    
    @Override
    public List<Item> getLikedItems() {
        return likedItems;
    }
    
    @Override
    public List<Item> getHatedItems() {
        return hatedItems;
    }
    
    @Override
    public void performAction(Player player, String action) {
        switch (action.toLowerCase()) {
            case "chat":
                System.out.println(name + " chats with player");
                break;
            case "gift":
                System.out.println(name + " received a gift from player");
                break;
            case "propose": {
                String message;
                boolean hasRing = false;
                Item[] items = player.getInventoryItems();
                for (Item item : items) {
                    if (item != null && item.getName().equalsIgnoreCase("Proposal Ring")) {
                        hasRing = true;
                        break;
                    }
                }
                if (!hasRing) {
                    message = "Yeuu ga punya cincin kok berani ngelamar anak orang";
                    System.out.println(message);
                    showMessageToUI(message);
                    gp.addHours(1);
                    break;
                }                if (relationshipStatus.equals("single")) {
                    if (heartPoints >= MAX_HEART_POINTS) {
                        relationshipStatus = "fiance";
                        engagementDay = gp.getCurrentDay(); // Record the day of engagement
                        player.setEnergy(player.getEnergy() - 10);
                        message = name + " menerima lamaranmu! Selamat yaaa :).";
                    } else {
                        player.setEnergy(player.getEnergy() - 20);
                        message = name + " menolak lamaranmu. jangan sedih ya broo :(. ";
                    }
                } else if (relationshipStatus.equals("fiance")) {
                    message = name + " sudah menjadi tunanganmu. Mau nikah kah?";
                } else {
                    message = name + " sudah menjadi " + relationshipStatus + ".";
                }
                System.out.println(message);
                showMessageToUI(message);
                gp.addHours(1); // Advance time by 1 hour after proposing
                break;
            }            case "married": {
                String message;
                
                // Check if player is already married to someone else first
                if (player.isMarried() && !name.equals(player.getSpouseName())) {
                    message = "CIEE udah ga setia sama pasangan sendiri, gaboleh poligami yaaa";
                    System.out.println(message);
                    showMessageToUI(message);
                    break;
                }
                
                boolean hasRing = false;
                Item[] items = player.getInventoryItems();
                for (Item item : items) {
                    if (item != null && item.getName().equalsIgnoreCase("Proposal Ring")) {
                        hasRing = true;
                        break;
                    }
                }
                if (!hasRing) {
                    message = "Cihhh, gapunya cincin kok ngajak nikah!";
                    System.out.println(message);
                    showMessageToUI(message);
                    break;
                }if (relationshipStatus.equals("fiance")) {
                    // Check if at least one day has passed since engagement
                    int currentDay = gp.getCurrentDay();
                    if (currentDay <= engagementDay) {
                        message = "Buru-buru amat, baru juga jadi fiance";
                        System.out.println(message);
                        showMessageToUI(message);
                        break; // Don't reduce energy if not enough time has passed
                    }
                    
                    relationshipStatus = "spouse";
                    player.setEnergy(player.getEnergy() - 80);
                    player.setMarried(true, name); // Set marriage status with spouse name
                    message = name + " sekarang menjadi pasanganmu! Selamat yaaa semoga bahagia selalu.";
                } else if (relationshipStatus.equals("spouse")) {
                    // Check if player is already married to this specific NPC
                    if (player.isMarried() && name.equals(player.getSpouseName())) {
                        message = "Aku pasanganmu, bukan suami/istri orang lain!!!!";
                    } else if (player.isMarried() && !name.equals(player.getSpouseName())) {
                        message = "CIEE udah ga setia sama pasangan sendiri, gaboleh poligami yaaa";
                    } else {
                        message = "Aku pasanganmu, bukan suami/istri orang lain!!!!";
                    }
                } else {
                    message = "Minimal dilamar dulu kalii";                }
                System.out.println(message);
                showMessageToUI(message);
                gp.timeskipTo(22, 00, true);
                break;
            }
            default:
                System.out.println("Unknown action: " + action);
                break;
        }
    }

    /**
     * Helper untuk menampilkan pesan ke UI (bisa dihubungkan ke GamePanel/MessagePanel)
     */
    private void showMessageToUI(String message) {
        // Use NPCUi for message panel if available
        if (gp != null && gp.getNPCUi() != null) {
            gp.getNPCUi().showMessagePanel(message);
        } else if (gp != null) {
            gp.showMessagePanel(message);
        }
    }
    
    public int getHeartPoints() {
        return heartPoints;
    }
    
    public void setHeartPoints(int points) {
        this.heartPoints = Math.max(0, Math.min(points, MAX_HEART_POINTS));
    }
    
    public void increaseHeartPoints(int amount) {
        setHeartPoints(heartPoints + amount);
    }
    
    public void decreaseHeartPoints(int amount) {
        setHeartPoints(heartPoints - amount);
    }
    
    public String getRelationshipStatus() {
        return relationshipStatus;
    }
    
    public void setRelationshipStatus(String status) {
        if (status.equals("single") || status.equals("fiance") || status.equals("spouse")) {
            this.relationshipStatus = status;
        } else {
            throw new IllegalArgumentException("Invalid relationship status. Must be 'single', 'fiance', or 'spouse'");
        }
    }
      public void addLovedItem(Item item) {
        lovedItems.add(item);
    }
    
    public void addLikedItem(Item item) {
        likedItems.add(item);
    }
    
    public void addHatedItem(Item item) {
        hatedItems.add(item);
    }
    
    public void removeLovedItem(Item item) {
        lovedItems.remove(item);
    }
    
    public void removeLikedItem(Item item) {
        likedItems.remove(item);
    }
    
    public void removeHatedItem(Item item) {
        hatedItems.remove(item);
    }
    
      /**
     * Player memberikan hadiah ke NPC, return pesan reaksi untuk UI
     */
    public String receiveGift(Item item) {        
        String reactionMsg;
        if (lovedItems.contains(item)) {
            increaseHeartPoints(25);
            reactionMsg = name + " loves this gift!";
        } else if (likedItems.contains(item)) {
            increaseHeartPoints(20);
            reactionMsg = name + " likes this gift.";
        } else if (hatedItems.contains(item)) {
            decreaseHeartPoints(25);
            reactionMsg = name + " hates this gift.";
        } else {
            increaseHeartPoints(0);
            reactionMsg = name + " accepts your gift.";
        }
        System.out.println(reactionMsg);
        return reactionMsg;
    }
    
    /**
     * Get the collision bounds of this NPC
     * @return Rectangle representing the collision area in world coordinates
     */
    public Rectangle getCollisionBounds() {
        return new Rectangle(getWorldX() + solidArea.x, getWorldY() + solidArea.y, 
                           solidArea.width, solidArea.height);
    }
    
    /**
     * Check if this NPC is solid (blocks player movement)
     * @return true if NPC should block player movement
     */
    public boolean isSolid() {
        return isSolid;
    }
    
    /**
     * Set whether this NPC should be solid
     * @param solid true to make NPC block player movement
     */
    public void setSolid(boolean solid) {
        this.isSolid = solid;
    }
    
    /**
     * Get the solid area rectangle of this NPC
     * @return Rectangle representing the relative solid area
     */
    public Rectangle getSolidArea() {
        return solidArea;
    }
}
