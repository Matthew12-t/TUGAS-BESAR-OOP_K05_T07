package SRC.OBJECT;

import SRC.MAIN.GamePanel;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Graphics2D;

public class OBJ_StoreDecoration4 extends SuperObject {
    private int decorationWidth = 1;  // 1x1 size
    private int decorationHeight = 1;
    
    public OBJ_StoreDecoration4(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("store_decoration4");
        getPosition().setCollision(true); // Store decoration has collision
        
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/storedecoration4.png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/storedecoration4.png"));
            }
            setImage(image);
            System.out.println("Store Decoration 4 image loaded successfully");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load store decoration 4 image: " + e.getMessage());
            // Create a default colored rectangle if image fails to load
            setImage(new BufferedImage(gp.getTileSize(), gp.getTileSize(), BufferedImage.TYPE_INT_RGB));
        }
    }
    
    public void draw(Graphics2D g2, GamePanel gp) {
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();
        int size = gp.getTileSize();
        
        if(worldX + (decorationWidth * size) > gp.getCameraX() &&
           worldX - size < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (decorationHeight * size) > gp.getCameraY() &&
           worldY - size < gp.getCameraY() + gp.getScreenHeight()) {
            g2.drawImage(getImage(), screenX, screenY, size * decorationWidth, size * decorationHeight, null);
        }
    }
    
    public int getDecorationWidth() { return decorationWidth; }
    public int getDecorationHeight() { return decorationHeight; }
}
