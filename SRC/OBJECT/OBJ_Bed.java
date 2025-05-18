package SRC.OBJECT;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import SRC.MAIN.GamePanel;
import java.awt.Graphics2D;

public class OBJ_Bed extends SuperObject {
    // Ukuran tempat tidur dalam jumlah tile
    private int bedWidth = 2;  // Lebar tempat tidur (jumlah kolom)
    private int bedHeight = 4; // Tinggi tempat tidur (jumlah baris)
    public OBJ_Bed(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("Bed");
        getPosition().setCollision(true); // Bed cannot be passed through
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/bed.png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/bed.png"));
            }
            setImage(image);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Bed image not found");
        }
    }
    public void draw(Graphics2D g2, GamePanel gp) {
        // Hitung posisi layar berdasarkan posisi kamera
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        // Hanya render jika objek berada dalam area layar yang terlihat
        if(worldX + (bedWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (bedHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            g2.drawImage(getImage(), screenX, screenY, gp.getTileSize() * bedWidth, gp.getTileSize() * bedHeight, null);
        }
    }
    public int getBedWidth() {
        return bedWidth;
    }
    public int getBedHeight() {
        return bedHeight;
    }
}
