package SRC.OBJECT;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;

/**
 * OBJ_Pond class represents a fishing pond object in the game.
 */
public class OBJ_Pond extends SuperObject {
    
    // Ukuran kolam dalam jumlah tile
    private int pondWidth = 4;   // Lebar kolam (jumlah kolom)
    private int pondHeight = 3;  // Tinggi kolam (jumlah baris)
    
    /**
     * Constructor untuk Pond object dengan ukuran 4x3
     * @param gp GamePanel reference
     * @param col Column position in the map grid
     * @param row Row position in the map grid
     */
    public OBJ_Pond(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("Pond");
        
        // Kolam memiliki collision
        setCollision(true);
        
        // Load the pond image from resource folder
        try {
            // Menggunakan gambar water.png yang sudah ada di RES/TILE/
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/TILE/water.png"));
            if (image == null) {
                // Try with File if resource stream doesn't work
                image = ImageIO.read(new File("RES/TILE/water.png"));
            }
            setImage(image);
            System.out.println("Pond image loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading pond image: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Pond image resource not found. Check the path.");
            e.printStackTrace();
        }
    }
    
    /**
     * Override metode draw dari SuperObject untuk menggambar kolam dengan ukuran 4x3
     */
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        // Hitung posisi layar berdasarkan posisi kamera
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        // Hanya render jika objek berada dalam area layar yang terlihat
        if(worldX + (pondWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (pondHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            // Gambar kolam dengan ukuran sesuai dengan jumlah tile
            g2.drawImage(getImage(), screenX, screenY, 
                        gp.getTileSize() * pondWidth, 
                        gp.getTileSize() * pondHeight, null);
        }
    }
    
    /**
     * Getter untuk lebar kolam
     * @return lebar kolam dalam jumlah tile
     */
    public int getPondWidth() {
        return pondWidth;
    }
    
    /**
     * Getter untuk tinggi kolam
     * @return tinggi kolam dalam jumlah tile
     */
    public int getPondHeight() {
        return pondHeight;
    }
}
