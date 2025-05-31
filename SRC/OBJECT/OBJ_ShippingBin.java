package SRC.OBJECT;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;


public class OBJ_ShippingBin extends SuperObject {
    
    
    private int binWidth = 3;   
    private int binHeight = 2;  
    

    public OBJ_ShippingBin(GamePanel gp, int col, int row) {
        super(gp, col, row);
        setName("Shipping Bin");
        
        
        
        try {
            
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/shippingbin.png"));
            if (image == null) {
                
                image = ImageIO.read(new File("RES/OBJECT/shippingbin.png"));
            }
            setImage(image);
            System.out.println("Shipping bin image loaded successfully!");        } catch (IOException e) {
            
            BufferedImage tempImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = tempImage.createGraphics();
            g2.setColor(new Color(139, 69, 19)); 
            g2.fillRect(0, 0, 16, 16);
            g2.setColor(Color.BLACK);
            g2.drawRect(0, 0, 15, 15);
            g2.dispose();
            
            setImage(tempImage);
            System.out.println("Created temporary shipping bin image.");
        }
    }
    

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        
        int worldX = getPosition().getWorldX();
        int worldY = getPosition().getWorldY();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();

        
        if(worldX + (binWidth * gp.getTileSize()) > gp.getCameraX() &&
           worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
           worldY + (binHeight * gp.getTileSize()) > gp.getCameraY() &&
           worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {

            
            g2.drawImage(getImage(), screenX, screenY, 
                        gp.getTileSize() * binWidth, 
                        gp.getTileSize() * binHeight, null);
            
        }
    }
    


    public int getBinWidth() {
        return binWidth;
    }
    

    public int getBinHeight() {
        return binHeight;
    }
}