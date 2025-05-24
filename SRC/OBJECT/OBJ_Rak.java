package SRC.OBJECT;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import SRC.MAIN.GamePanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class OBJ_Rak extends SuperObject {
    private int rakWidth = 2;
    private int rakHeight = 2;

    public OBJ_Rak(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("rak");
        getPosition().setCollision(true);
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/rak.png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/rak.png"));
            }
            setImage(image);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("rak image not found");
        }
    }
    public void draw(Graphics2D g2, GamePanel gp) {
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();
        int size = gp.getTileSize();
        if(worldX + (rakWidth * size) > gp.getCameraX() &&
           worldX - size < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (rakHeight * size) > gp.getCameraY() &&
           worldY - size < gp.getCameraY() + gp.getScreenHeight()) {
            g2.drawImage(getImage(), screenX, screenY, size * rakWidth, size * rakHeight, null);
        }
    }
    public int getRakWidth() { return rakWidth; }
    public int getRakHeight() { return rakHeight; }
}
