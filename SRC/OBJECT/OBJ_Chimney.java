package SRC.OBJECT;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import SRC.MAIN.GamePanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class OBJ_Chimney extends SuperObject {
    private int chimneyWidth = 2;
    private int chimneyHeight = 3;

    public OBJ_Chimney(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("chimney");
        getPosition().setCollision(true);
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/chimney.png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/chimney.png"));
            }
            setImage(image);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("chimney image not found");
        }
    }
    
    public void draw(Graphics2D g2, GamePanel gp) {
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();
        int size = gp.getTileSize();
        if(worldX + (chimneyWidth * size) > gp.getCameraX() &&
           worldX - size < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (chimneyHeight * size) > gp.getCameraY() &&
           worldY - size < gp.getCameraY() + gp.getScreenHeight()) {
            g2.drawImage(getImage(), screenX, screenY, size * chimneyWidth, size * chimneyHeight, null);
        }
    }
    
    public int getChimneyWidth() { return chimneyWidth; }
    public int getChimneyHeight() { return chimneyHeight; }
}
