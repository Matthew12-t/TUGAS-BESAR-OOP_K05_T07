package SRC.OBJECT;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import SRC.MAIN.GamePanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class OBJ_Tv extends SuperObject {
    private int tvWidth = 1;
    private int tvHeight = 1;

    public OBJ_Tv(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("tv");
        getPosition().setCollision(true);
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/tv.png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/tv.png"));
            }
            setImage(image);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("tv image not found");
        }
    }
    public void draw(Graphics2D g2, GamePanel gp) {
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();
        int size = gp.getTileSize();
        if(worldX + (tvWidth * size) > gp.getCameraX() &&
           worldX - size < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (tvHeight * size) > gp.getCameraY() &&
           worldY - size < gp.getCameraY() + gp.getScreenHeight()) {
            g2.drawImage(getImage(), screenX, screenY, size * tvWidth, size * tvHeight, null);
        }
    }
    public int getTvWidth() { return tvWidth; }
    public int getTvHeight() { return tvHeight; }
}
