package SRC.OBJECT;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;
import SRC.TILES.Tile;

public class SuperObject {    
    private BufferedImage image;
    private String name;
    private Tile position;
    
    
    public SuperObject(GamePanel gp, int col, int row) {
        this.position = new Tile(gp, col, row);
    }
    
    public void draw(Graphics2D g2, GamePanel gp) {
        
        int worldX = position.getWorldX();
        int worldY = position.getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        
        if(worldX + gp.getTileSize() > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + gp.getTileSize() > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
        }
    }

    
    public BufferedImage getImage() {
        return image;
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getWorldX() {
        return position.getWorldX();
    }
    
    public void setWorldX(int worldX) {
        position.setWorldX(worldX);
    }
    
    public int getWorldY() {
        return position.getWorldY();
    }
    
    public void setWorldY(int worldY) {
        position.setWorldY(worldY);
    }
    
    public Tile getPosition() {
        return position;
    }
    
    public void setPosition(Tile position) {
        this.position = position;
    }
}