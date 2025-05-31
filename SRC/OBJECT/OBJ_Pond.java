package SRC.OBJECT;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;


public class OBJ_Pond extends SuperObject {
    
    
    private int pondWidth = 4;   
    private int pondHeight = 3;  
    

    public OBJ_Pond(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("Pond");
        
        
        
        try {
            
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/pond.png"));
            if (image == null) {
                
                image = ImageIO.read(new File("RES/OBJECT/pond.png"));
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
        
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        
        if(worldX + (pondWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (pondHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            
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
