package SRC.OBJECT;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;


public class OBJ_House extends SuperObject {
    
    
    private int houseWidth = 6;  
    private int houseHeight = 6; 
    

    public OBJ_House(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("House");
        
        
        try {
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/house.png"));
            if (image == null) {
                
                image = ImageIO.read(new File("RES/OBJECT/house.png"));
            }
            setImage(image);
            System.out.println("House image loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading house image: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {            
            System.err.println("House image resource not found. Check the path.");
            e.printStackTrace();
        }
    }
    

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        
        if(worldX + (houseWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (houseHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            
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
