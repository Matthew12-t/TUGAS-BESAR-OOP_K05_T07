package MAIN;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    
    private GamePanel gamePanel;
    
    public int targetX;
    public int targetY;
    public boolean hasTarget;
    
    public MouseHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.hasTarget = false;
    }
    
    @Override
    public void mousePressed(MouseEvent e) { // Ubah dari mouseClicked ke mousePressed
        // Set target position to mouse click location
        targetX = e.getX();
        targetY = e.getY();
        hasTarget = true;
        
        // Debug info
        System.out.println("Mouse clicked at: " + targetX + ", " + targetY);
    }
}