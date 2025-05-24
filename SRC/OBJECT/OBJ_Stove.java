package SRC.OBJECT;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import SRC.MAIN.GamePanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class OBJ_Stove extends SuperObject {
    private int stoveWidth = 1;
    private int stoveHeight = 1;

    public OBJ_Stove(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("stove");
        getPosition().setCollision(true);
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/stove.png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/stove.png"));
            }
            setImage(image);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("stove image not found");
        }
    }
    public void draw(Graphics2D g2, GamePanel gp) {
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();
        int size = gp.getTileSize();
        if(worldX + (stoveWidth * size) > gp.getCameraX() &&
           worldX - size < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (stoveHeight * size) > gp.getCameraY() &&
           worldY - size < gp.getCameraY() + gp.getScreenHeight()) {
            g2.drawImage(getImage(), screenX, screenY, size * stoveWidth, size * stoveHeight, null);
        }
    }
    public int getStoveWidth() { return stoveWidth; }
    public int getStoveHeight() { return stoveHeight; }
}
