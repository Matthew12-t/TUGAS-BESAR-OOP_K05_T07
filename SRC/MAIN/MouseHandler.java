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
    }
    
    private boolean isPlacingHouse = false; // Flag to track if we're in house placement mode
    
    /**
     * Enable or disable house placement mode
     * @param placingHouse true to enable house placement mode
     */
    public void setPlacingHouse(boolean placingHouse) {
        this.isPlacingHouse = placingHouse;
    }
    
    /**
     * Check if we're currently in house placement mode
     * @return true if in house placement mode
     */
    public boolean isPlacingHouse() {
        return isPlacingHouse;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        // Get mouse click position (screen coordinates)
        int screenX = e.getX();
        int screenY = e.getY();
        
        // Convert to world coordinates
        int worldX = screenX + gamePanel.getCameraX();
        int worldY = screenY + gamePanel.getCameraY();
        
        // Snap to tile grid
        int tileSize = gamePanel.getTileSize();
        int col = worldX / tileSize;
        int row = worldY / tileSize;
        
        // Calculate centered world coordinates for target
        int centeredWorldX = col * tileSize + (tileSize / 2);
        int centeredWorldY = row * tileSize + (tileSize / 2);
        
        // Save world coordinates
        setTargetX(centeredWorldX);
        setTargetY(centeredWorldY);
        setHasTarget(true);
        
        // Debug info
        System.out.println("Mouse clicked at screen: " + screenX + ", " + screenY + 
                           " | Target tile: " + col + ", " + row +
                           " | Target world position: " + getTargetX() + ", " + getTargetY());
          // Handle object placement if in placement mode
        if (isPlacingHouse) {
            // Get current map and use its deployment methods
            if (gamePanel.getCurrentMap().isValidPlacement(col, row)) {
                boolean success = gamePanel.getCurrentMap().deployHouse(col, row);
                if (success) {
                    System.out.println("House placed successfully at tile: " + col + ", " + row);
                } else {
                    System.out.println("Failed to place house at tile: " + col + ", " + row);
                }
            } else {
                System.out.println("Invalid placement location at tile: " + col + ", " + row);
            }
        }
    }
}