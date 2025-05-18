package SRC.MAIN;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    
    // Reference to the game panel
    private GamePanel gamePanel;
    
    // Constructor with GamePanel reference
    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    // Default constructor (for backward compatibility)
    public KeyHandler() {
        // No GamePanel reference
    }    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Movement keys
        if(code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = true;
        }  
        if(code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        
        // Only handle these if we have a gamePanel reference
        if (gamePanel != null) {
            // Map switching
            if(code == KeyEvent.VK_1) {
                gamePanel.switchToWorldMap();
                System.out.println("Switched to World Map");
            }            if(code == KeyEvent.VK_2) {
                gamePanel.switchToFarmMap();
                System.out.println("Switched to Farm Map");
            }
            if(code == KeyEvent.VK_3) {
                gamePanel.switchToHouseMap();
                System.out.println("Switched to House Map");
            }
            
            // Object placement mode
            if(code == KeyEvent.VK_P) {
                boolean isPlacing = gamePanel.getMouseHandler().isPlacingHouse();
                gamePanel.getMouseHandler().setPlacingHouse(!isPlacing);
                System.out.println("House placement mode: " + (!isPlacing ? "ON" : "OFF"));
            }
              // Remove object under cursor
            if(code == KeyEvent.VK_DELETE || code == KeyEvent.VK_BACK_SPACE) {
                if (gamePanel.getMouseHandler().isHasTarget()) {
                    int worldX = gamePanel.getMouseHandler().getTargetX();
                    int worldY = gamePanel.getMouseHandler().getTargetY();
                    int tileCol = worldX / gamePanel.getTileSize();
                    int tileRow = worldY / gamePanel.getTileSize();
                    
                    gamePanel.getCurrentMap().removeObject(tileCol, tileRow);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}
