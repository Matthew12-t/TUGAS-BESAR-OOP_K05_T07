package SRC.MAIN;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {
    
    private GamePanel gamePanel;
    
    private int targetX;
    private int targetY;
    private boolean hasTarget;
    
    public MouseHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.hasTarget = false;
    }
    
    // Getters and Setters
    public int getTargetX() {
        return targetX;
    }
    
    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }
    
    public int getTargetY() {
        return targetY;
    }
    
    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
    
    public boolean isHasTarget() {
        return hasTarget;
    }
    
    public void setHasTarget(boolean hasTarget) {
        this.hasTarget = hasTarget;
    }      @Override
    public void mousePressed(MouseEvent e) { // Ubah dari mouseClicked ke mousePressed
        // Ambil posisi klik mouse (screen coordinates)
        int screenX = e.getX();
        int screenY = e.getY();
        
        // Convert to world coordinates
        int worldX = screenX + gamePanel.getCameraX();
        int worldY = screenY + gamePanel.getCameraY();
        
        // Snap to tile grid
        int tileSize = gamePanel.getTileSize();
        worldX = (worldX / tileSize) * tileSize + (tileSize / 2);
        worldY = (worldY / tileSize) * tileSize + (tileSize / 2);
        
        // Simpan koordinat dunia (world coordinates)
        setTargetX(worldX);
        setTargetY(worldY);
        
        setHasTarget(true);
        
        // Debug info
        System.out.println("Mouse clicked at screen: " + screenX + ", " + screenY + 
                          " | Target world position: " + getTargetX() + ", " + getTargetY());
    }
}