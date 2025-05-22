package SRC.ENTITY;

import SRC.TILES.Tile;
import SRC.MAIN.GamePanel;

public abstract class Entity {
    private Tile position;
    private int speed;
    
    // Direction
    private String direction;
    
    // Animation variables
    private int spriteCounter = 0;
    private int spriteNum = 0;
    
    // Constructor
    public Entity(GamePanel gp, int worldX, int worldY) {
        this.position = new Tile(gp, worldX, worldY);
        this.speed = 0;
        this.direction = "down";
    }
    
    // Abstract method that must be implemented by subclasses
    public abstract void update(); //semua pergerakan yang dilakukan oleh entity ada di sini

      // Getters and Setters
    public int getWorldX() {
        return position.getWorldX();
    }
    
    public void setWorldX(int x) {
        this.position.setWorldX(x);
    }
    
    public int getWorldY() {
        return position.getWorldY();
    }
    
    public void setWorldY(int y) {
        this.position.setWorldY(y);
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public int getSpriteCounter() {
        return spriteCounter;
    }
    
    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }
    
    public int getSpriteNum() {
        return spriteNum;
    }
    
    public void setSpriteNum(int spriteNum) {
        this.spriteNum = spriteNum;
    }
    
    public void incrementSpriteCounter() {
        this.spriteCounter++;
    }
    
    public Tile getPosition() {
        return position;
    }
    
    public void setPosition(Tile position) {
        this.position = position;
    }
}
