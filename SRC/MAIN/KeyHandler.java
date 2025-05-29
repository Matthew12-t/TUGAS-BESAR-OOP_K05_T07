package SRC.MAIN;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import SRC.ENTITY.NPCEntity;

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
    }    
    
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();        // Only handle these if we have a gamePanel reference
        if (gamePanel != null) {
            // Handle SLEEP_STATE specially - only respond to Enter key
            if (gamePanel.getGameState() == GamePanel.SLEEP_STATE) {
                if (code == KeyEvent.VK_ENTER) {
                    System.out.println("DEBUG: Enter pressed in sleep state");
                    gamePanel.getPlayer().getPlayerAction().getSleepUI().handleEnterPress();
                }
                return; // Don't process other keys during sleep
            }
            
            // Toggle inventory with 'i' key - works in any state
            if(code == KeyEvent.VK_I) {
                if (gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
                    gamePanel.setGameState(GamePanel.PLAY_STATE);
                    System.out.println("Closed inventory");
                } else {
                    gamePanel.setGameState(GamePanel.INVENTORY_STATE);
                    System.out.println("Opened inventory");
                }
            }
            
            // Handle different key inputs depending on game state
            if (gamePanel.getGameState() == GamePanel.PLAY_STATE) {
                // Movement keys - only active in play state
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
                
                // Map switching
                if(code == KeyEvent.VK_1) {
                    gamePanel.switchToWorldMap();
                    System.out.println("Switched to World Map");
                }
                if(code == KeyEvent.VK_2) {
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
                }                  // 'C' key action - prioritize sleep if near bed, otherwise fishing
                if(code == KeyEvent.VK_C) {
                    // Check if player is near a bed for sleep action
                    if (gamePanel.getPlayer().getPlayerAction().isPlayerNearBed()) {
                        System.out.println("DEBUG: 'C' key pressed for sleep (near bed)");
                        gamePanel.getPlayer().getPlayerAction().performSleep();
                    } else {
                        System.out.println("DEBUG: 'C' key pressed for fishing");
                        gamePanel.getPlayer().getPlayerAction().performFishing();
                    }
                }
                
                // --- NPC INTERACTION KEYS ---
                if(code == KeyEvent.VK_G) {
                    // Gifting
                    gamePanel.tryGiftToNearbyNPC();
                }
                if(code == KeyEvent.VK_T) {
                    // Talking
                    gamePanel.tryTalkToNearbyNPC();
                }
                if(code == KeyEvent.VK_P) {
                    // Propose (lamaran)
                    NPCEntity nearbyNPC = gamePanel.getNearbyNPC(1);
                    if (nearbyNPC != null) {
                        nearbyNPC.performAction(gamePanel.getPlayer(), "propose");
                    } 
                }
            }
            // --- INVENTORY STATE GIFTING ---
            else if (gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
                if (code == KeyEvent.VK_G) {
                    // Only confirm gift if giftingTargetNPC is set
                    if (gamePanel.getGiftingTargetNPC() != null) {
                        gamePanel.confirmGiftFromInventory();
                    }
                }
            }            
            
            // Eating action with 'E' key - works in both PLAY_STATE and INVENTORY_STATE
            if(code == KeyEvent.VK_E) {
                if (gamePanel.getGameState() == GamePanel.PLAY_STATE || 
                    gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
                    // Get player from gamePanel and perform eating action
                    gamePanel.getPlayer().getPlayerAction().eatSelectedItem();
                    System.out.println("DEBUG: 'E' key pressed for eating in state: " + 
                                     (gamePanel.getGameState() == GamePanel.PLAY_STATE ? "PLAY" : "INVENTORY"));
                }
            }
            
            else if (gamePanel.getGameState() == GamePanel.MAP_MENU_STATE) {
                // Map menu navigation
                if(code == KeyEvent.VK_UP) {
                    System.out.println("UP arrow key pressed in map menu");
                    gamePanel.selectPreviousMap();
                }
                if(code == KeyEvent.VK_DOWN) {
                    System.out.println("DOWN arrow key pressed in map menu");
                    gamePanel.selectNextMap();
                }
                if(code == KeyEvent.VK_ENTER) {
                    System.out.println("ENTER key pressed in map menu");
                    gamePanel.exitMapMenuState();
                }
                
                // Add WASD key support in map menu as well
                if(code == KeyEvent.VK_W) {
                    System.out.println("W key pressed in map menu");
                    gamePanel.selectPreviousMap();
                }
                if(code == KeyEvent.VK_S) {
                    System.out.println("S key pressed in map menu");
                    gamePanel.selectNextMap();
                }
            }
        }
        else {
            // Fallback if gamePanel is null - just handle movement keys
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
        }
    }    @Override
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
    
    /**
     * Reset all key states to false
     * This method should be called when dialogs are closed to prevent stuck keys
     */
    public void resetAllKeyStates() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        System.out.println("DEBUG: All key states reset");
    }
}
