package SRC.OBJECT;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;

/**
 * OBJ_ShippingBin class represents a shipping bin for selling items in the game.
 */
public class OBJ_ShippingBin extends SuperObject {
    
    // Ukuran shipping bin dalam jumlah tile
    private int binWidth = 3;   // Lebar shipping bin (jumlah kolom)
    private int binHeight = 2;  // Tinggi shipping bin (jumlah baris)
    
    /**
     * Constructor untuk Shipping Bin dengan ukuran 3x2
     * @param gp GamePanel reference
     * @param col Column position in the map grid
     * @param row Row position in the map grid
     */
    public OBJ_ShippingBin(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("Shipping Bin");
        
        
        // Load the shipping bin image or create a temporary one
        try {
            // Coba gunakan house image jika ada
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/shippingbin.png"));
            if (image == null) {
                // Try with File if resource stream doesn't work
                image = ImageIO.read(new File("RES/OBJECT/shippingbin.png"));
            }
            setImage(image);
            System.out.println("Shipping bin image loaded successfully!");        } catch (IOException e) {
            // Jika tidak ada gambar khusus, buat gambar sementara
            BufferedImage tempImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = tempImage.createGraphics();
            g2.setColor(new Color(139, 69, 19)); // Warna coklat menggunakan RGB
            g2.fillRect(0, 0, 16, 16);
            g2.setColor(Color.BLACK);
            g2.drawRect(0, 0, 15, 15);
            g2.dispose();
            
            setImage(tempImage);
            System.out.println("Created temporary shipping bin image.");
        }
    }
    
    /**
     * Override metode draw dari SuperObject untuk menggambar shipping bin dengan ukuran 3x2
     */
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        // Hitung posisi layar berdasarkan posisi kamera
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        // Hanya render jika objek berada dalam area layar yang terlihat
        if(worldX + (binWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (binHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            // Gambar shipping bin dengan ukuran sesuai dengan jumlah tile
            g2.drawImage(getImage(), screenX, screenY, 
                        gp.getTileSize() * binWidth, 
                        gp.getTileSize() * binHeight, null);
            
        }
    }
    
    /**
     * Getter untuk lebar shipping bin
     * @return lebar shipping bin dalam jumlah tile
     */
    public int getBinWidth() {
        return binWidth;
    }
    
    /**
     * Getter untuk tinggi shipping bin
     * @return tinggi shipping bin dalam jumlah tile
     */
    public int getBinHeight() {
        return binHeight;
    }
}