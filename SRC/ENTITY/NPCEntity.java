package SRC.ENTITY;

import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;

import SRC.MAIN.GamePanel;
import SRC.ITEMS.Item;

public class NPCEntity extends Entity implements NPC {
    // Basic NPC properties
    private String name;
    private String location;
    private String description;
      private int heartPoints;
    private static final int MAX_HEART_POINTS = 150;
    private String relationshipStatus; 
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
        // Basic interaction logic - can be overridden by specific NPC types
        System.out.println(name + " says: Hello, player!");
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
        switch (action.toLowerCase()) {            case "chat":
                System.out.println(name + " chats with player");
                break;
            case "gift":
                System.out.println(name + " received a gift from player");
                break;
            default:
                System.out.println("Unknown action: " + action);
                break;
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
    
      public void receiveGift(Item item) {        
        if (lovedItems.contains(item)) {
            increaseHeartPoints(25);
            System.out.println(name + " loves this gift!");
        } else if (likedItems.contains(item)) {
            increaseHeartPoints(20);
            System.out.println(name + " likes this gift.");
        } else if (hatedItems.contains(item)) {
            decreaseHeartPoints(25);
            System.out.println(name + " hates this gift...");
        } else {
            increaseHeartPoints(0);
            System.out.println(name + " accepts your gift.");
        }
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
