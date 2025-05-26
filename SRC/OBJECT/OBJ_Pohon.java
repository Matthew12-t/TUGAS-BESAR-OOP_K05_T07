package SRC.OBJECT;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import SRC.MAIN.GamePanel;
import java.awt.Graphics2D;

public class OBJ_Pohon extends SuperObject {
    // Ukuran tempat tidur dalam jumlah tile
    private int pohonWidth = 3;  // Lebar kursi (jumlah kolom)
    private int pohonHeight = 4; // Tinggi kursi (jumlah baris)
    private int mode = 1;
    public OBJ_Pohon(GamePanel gp, int col, int row, int mode) {
        super(gp, col, row);
        setName("pohon");
        getPosition().setCollision(true); // pohon cannot be passed through
        
        this.mode = mode;
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/tree_" + mode + ".png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/tree_" + mode + ".png"));
            }
            setImage(image);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("pohon image not found for mode " + mode);
        }
    }
    public OBJ_Pohon(GamePanel gp, int col, int row) {
        this(gp, col, row, 1);
    }
    public void draw(Graphics2D g2, GamePanel gp) {
        // Hitung posisi layar berdasarkan posisi kamera
        int worldX = getPosition().getWorldX() - 1 * gp.getTileSize();
        int worldY = getPosition().getWorldY() - 3 * gp.getTileSize();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        // Hanya render jika objek berada dalam area layar yang terlihat
        if(worldX + (pohonWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (pohonHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            g2.drawImage(getImage(), screenX , screenY, gp.getTileSize() * pohonWidth, gp.getTileSize() * pohonHeight, null);
        }
    }
    public int getpohonWidth() {
        return pohonWidth;
    }
    public int getpohonHeight() {
        return pohonHeight;
    }
    public int getMode() {
        return mode;
    }
}
