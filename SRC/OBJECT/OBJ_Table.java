package SRC.OBJECT;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import SRC.MAIN.GamePanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

public class OBJ_Table extends SuperObject {
    // Ukuran meja dalam jumlah tile
    private int tableWidth = 2;  // Lebar meja (jumlah kolom)
    private int tableHeight = 2; // Tinggi meja (jumlah baris)
    private int mode = 1;

    public OBJ_Table(GamePanel gp, int col, int row, int mode) {
        super(gp, col, row);
        setName("table");
        getPosition().setCollision(true); // Table cannot be passed through
        this.mode = mode;
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/table_" + mode + ".png"));
            if (image == null) {
                image = ImageIO.read(new File("RES/OBJECT/table_" + mode + ".png"));
            }
            setImage(image);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("table image not found for mode " + mode);
        }
    }

    // Backward-compatible constructor
    public OBJ_Table(GamePanel gp, int col, int row) {
        this(gp, col, row, 1);
    }

    public void draw(Graphics2D g2, GamePanel gp) {
        // Hitung posisi layar berdasarkan posisi kamera
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        // Hanya render jika objek berada dalam area layar yang terlihat
        if(worldX + (tableWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (tableHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            g2.drawImage(getImage(), screenX, screenY, gp.getTileSize() * tableWidth, gp.getTileSize() * tableHeight, null);
        }
    }
    public int gettableWidth() {
        return tableWidth;
    }
    public int gettableHeight() {
        return tableHeight;
    }
    public int getMode() {
        return mode;
    }
}