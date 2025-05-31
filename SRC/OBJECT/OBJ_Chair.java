package SRC.OBJECT;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import SRC.MAIN.GamePanel;
import java.awt.Graphics2D;

public class OBJ_Chair extends SuperObject {
    
    private int chairWidth = 1;  
    private int chairHeight = 1; 
    private int mode = 1;
    public OBJ_Chair(GamePanel gp, int col, int row, int mode) {
        super(gp, col, row);
        setName("chair");
        getPosition().setCollision(true); 
        this.mode = mode;
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/chair_" + mode + ".png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/chair_" + mode + ".png"));
            }
            setImage(image);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("chair image not found for mode " + mode);
        }
    }
    public OBJ_Chair(GamePanel gp, int col, int row) {
        this(gp, col, row, 1);
    }
    public void draw(Graphics2D g2, GamePanel gp) {
        
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        
        if(worldX + (chairWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (chairHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            g2.drawImage(getImage(), screenX, screenY, gp.getTileSize() * chairWidth, gp.getTileSize() * chairHeight, null);
        }
    }
    public int getChairWidth() {
        return chairWidth;
    }
    public int getChairHeight() {
        return chairHeight;
    }
    public int getMode() {
        return mode;
    }
}
