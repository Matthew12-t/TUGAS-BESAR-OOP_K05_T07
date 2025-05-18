package SRC.OBJECT;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import SRC.MAIN.GamePanel;
import java.awt.Graphics2D;

public class OBJ_Chair extends SuperObject {
    // Ukuran tempat tidur dalam jumlah tile
    private int chairWidth = 1;  // Lebar kursi (jumlah kolom)
    private int chairHeight = 1; // Tinggi kursi (jumlah baris)
    public OBJ_Chair(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("chair");
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/chair.png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/chair.png"));
            }
            setImage(image);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("chair image not found");
        }
    }
    public void draw(Graphics2D g2, GamePanel gp) {
        // Hitung posisi layar berdasarkan posisi kamera
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        // Hanya render jika objek berada dalam area layar yang terlihat
        if(worldX + (chairWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (chairHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            g2.drawImage(getImage(), screenX, screenY, gp.getTileSize() * chairWidth, gp.getTileSize() * chairHeight, null);
        }
    }
    public int getchairWidth() {
        return chairWidth;
    }
    public int getchairHeight() {
        return chairHeight;
    }
}
