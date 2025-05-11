package SRC.MAIN;

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
        // Ambil posisi klik mouse (screen coordinates)
        int screenX = e.getX();
        int screenY = e.getY();
        
        // Convert to world coordinates
        int worldX = screenX + gamePanel.cameraX;
        int worldY = screenY + gamePanel.cameraY;
        
        // Snap to tile grid
        int tileSize = gamePanel.tileSize;
        worldX = (worldX / tileSize) * tileSize + (tileSize / 2);
        worldY = (worldY / tileSize) * tileSize + (tileSize / 2);
        
        // Simpan koordinat dunia (world coordinates)
        targetX = worldX;
        targetY = worldY;
        
        hasTarget = true;
        
        // Debug info
        System.out.println("Mouse clicked at screen: " + screenX + ", " + screenY + 
                          " | Target world position: " + targetX + ", " + targetY);
    }
}