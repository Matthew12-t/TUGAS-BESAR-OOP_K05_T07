package SRC.OBJECT;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;

/**
 * OBJ_House class represents a house object in the game.
 * This replaces the previous Door and Key objects.
 */
public class OBJ_House extends SuperObject {
    
    // Ukuran rumah dalam jumlah tile
    private int houseWidth = 6;  // Lebar rumah (jumlah kolom)
    private int houseHeight = 6; // Tinggi rumah (jumlah baris)
    
    /**
     * Constructor for the House object
     * @param gp GamePanel reference
     * @param col Column position in the map grid
     * @param row Row position in the map grid
     */
    public OBJ_House(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("House");
        
        // Default: Houses have collision
        setCollision(true);
        
        // Load the house image from resource folder
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/house.png"));
            if (image == null) {
                // Try with File if resource stream doesn't work
                image = ImageIO.read(new File("RES/OBJECT/house.png"));
            }
            setImage(image);
            System.out.println("House image loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading house image: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {            System.err.println("House image resource not found. Check the path.");
            e.printStackTrace();
        }
    }
    
    /**
     * Override metode draw dari SuperObject untuk menggambar rumah dengan ukuran yg lebih besar
     */
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        // Hitung posisi layar berdasarkan posisi kamera
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        // Hanya render jika objek berada dalam area layar yang terlihat
        if(worldX + (houseWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (houseHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            // Gambar rumah dengan ukuran sesuai dengan jumlah tile
            g2.drawImage(getImage(), screenX, screenY, 
                        gp.getTileSize() * houseWidth, 
                        gp.getTileSize() * houseHeight, null);
        }
    }
    
    /**
     * Getter untuk lebar rumah
     * @return lebar rumah dalam jumlah tile
     */
    public int getHouseWidth() {
        return houseWidth;
    }
    
    /**
     * Getter untuk tinggi rumah
     * @return tinggi rumah dalam jumlah tile
     */
    public int getHouseHeight() {
        return houseHeight;
    }
}
