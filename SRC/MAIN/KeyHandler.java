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
            }            // Handle different key inputs depending on game state
            if (gamePanel.getGameState() == GamePanel.PLAY_STATE) {
                // Check if NPC interaction menu is open - if so, only allow NPC interaction keys
                if (gamePanel.isNPCInteractionMenuOpen()) {
                    // Only handle NPC interaction keys when menu is open
                    if (code == KeyEvent.VK_T) {
                        gamePanel.getNPCUi().handleTalkKey();
                    } else if (code == KeyEvent.VK_G) {
                        gamePanel.getNPCUi().handleGiftKey();
                    } else if (code == KeyEvent.VK_P) {
                        gamePanel.getNPCUi().handleProposeKey();
                    } else if (code == KeyEvent.VK_ENTER) {
                        gamePanel.getNPCUi().handleEnterKey();
                    } else if (code == KeyEvent.VK_ESCAPE) {
                        gamePanel.closeNPCInteractionMenu();
                    }
                    return; // Don't process any other keys when NPC menu is open
                }
                  // Check if message panel is active - if so, ignore all input except Enter to dismiss
                if (gamePanel.isMessagePanelActive()) {
                    if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                        // Dismiss message panel by clearing it
                        gamePanel.getNPCUi().clearMessagePanel();
                        System.out.println("Message panel dismissed");
                    }
                    return; // Don't process any other keys when message panel is active
                }
                
                // Movement keys - only active in play state and when NPC menu is closed
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
                }                  // Object placement mode vs Planting mode
                if(code == KeyEvent.VK_P) {
                    // Check if player has a seed selected for planting
                    if (gamePanel.getPlayer().getPlayerAction().getHeldSeedName() != null) {
                        System.out.println("DEBUG: 'P' key pressed for planting (holding seed)");
                        gamePanel.getPlayer().getPlayerAction().performPlanting();
                    } else {
                        // Default behavior - house placement mode
                        boolean isPlacing = gamePanel.getMouseHandler().isPlacingHouse();
                        gamePanel.getMouseHandler().setPlacingHouse(!isPlacing);
                        System.out.println("House placement mode: " + (!isPlacing ? "ON" : "OFF"));
                    }
                }
                
                // Harvesting with 'H' key
                if(code == KeyEvent.VK_H) {
                    System.out.println("DEBUG: 'H' key pressed for harvesting");
                    gamePanel.getPlayer().getPlayerAction().performHarvesting();
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
                }                // 'C' key action - prioritize sleep if near bed, store if in store map, shipping bin if near shipping bin, watering/planting/tilling/land recovery if in farm map, otherwise fishing
                if(code == KeyEvent.VK_C) {
                    // Check if player is near a bed for sleep action
                    if (gamePanel.getPlayer().getPlayerAction().isPlayerNearBed()) {
                        System.out.println("DEBUG: 'C' key pressed for sleep (near bed)");
                        gamePanel.getPlayer().getPlayerAction().performSleep();
                    } else if (gamePanel.getCurrentMap().getMapName().equals("Store Map")) {
                        System.out.println("DEBUG: 'C' key pressed for store (in Store Map)");
                        gamePanel.setGameState(GamePanel.STORE_STATE);
                    } else if (gamePanel.getPlayer().getPlayerAction().isPlayerNearShippingBin()) {
                        System.out.println("DEBUG: 'C' key pressed for shipping bin");
                        gamePanel.setGameState(GamePanel.SHIPPING_STATE);
                      } else if (gamePanel.getCurrentMap().getMapName().equals("Farm Map") && 
                               gamePanel.getPlayer().isHolding("Watering Can")) {
                        System.out.println("DEBUG: 'C' key pressed for watering (holding watering can in farm map)");
                        gamePanel.getPlayer().getPlayerAction().performWatering();
                    } else if (gamePanel.getCurrentMap().getMapName().equals("Farm Map") && 
                               gamePanel.getPlayer().isHoldingAnySeed()) {
                        System.out.println("DEBUG: 'C' key pressed for planting (holding seeds in farm map)");
                        gamePanel.getPlayer().getPlayerAction().performPlanting();
                    } else if (gamePanel.getCurrentMap().getMapName().equals("Farm Map") && 
                               gamePanel.getPlayer().isHolding("Hoe")) {
                        System.out.println("DEBUG: 'C' key pressed for tilling (holding hoe in farm map)");
                        gamePanel.getPlayer().getPlayerAction().performTilling();
                    } else if (gamePanel.getCurrentMap().getMapName().equals("Farm Map") && 
                               gamePanel.getPlayer().isHolding("Pickaxe")) {
                        System.out.println("DEBUG: 'C' key pressed for land recovery (holding pickaxe in farm map)");
                        gamePanel.getPlayer().getPlayerAction().performLandRecovery();
                    } else {
                        System.out.println("DEBUG: 'C' key pressed for fishing");
                        gamePanel.getPlayer().getPlayerAction().performFishing();
                    }
                }
                
               
                if(code == KeyEvent.VK_Q) {
                    NPCEntity nearbyNPC = gamePanel.getNearbyNPC(1);
                    if (nearbyNPC != null) {
                        gamePanel.showNPCInteractionMenu(nearbyNPC);
                        return;
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
                if (gamePanel.getGameState() == GamePanel.PLAY_STATE || gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
                    // Get player from gamePanel and perform eating action
                    gamePanel.getPlayer().getPlayerAction().eatSelectedItem();
                    System.out.println("DEBUG: 'E' key pressed for eating in state: " + 
                                     (gamePanel.getGameState() == GamePanel.PLAY_STATE ? "PLAY" : "INVENTORY"));
                }
            }
              // Drop held tool or seed with 'Q' key - works in both PLAY_STATE and INVENTORY_STATE
            if(code == KeyEvent.VK_Q) {
                if (gamePanel.getGameState() == GamePanel.PLAY_STATE || 
                    gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
                    // Drop the currently held tool or seed
                    if (gamePanel.getPlayer().isHoldingAnyTool()) {
                        gamePanel.getPlayer().getPlayerAction().dropHeldTool();
                        System.out.println("DEBUG: 'Q' key pressed to drop held tool");
                    } else if (gamePanel.getPlayer().isHoldingAnySeed()) {
                        gamePanel.getPlayer().getPlayerAction().dropHeldSeed();
                        System.out.println("DEBUG: 'Q' key pressed to drop held seed");
                    } else {
                        System.out.println("DEBUG: 'Q' key pressed but nothing to drop");
                    }
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
                }                if(code == KeyEvent.VK_S) {
                    System.out.println("S key pressed in map menu");
                    gamePanel.selectNextMap();
                }            }            else if (gamePanel.getGameState() == GamePanel.SHIPPING_STATE) {
                // Only handle ESC key to exit shipping bin - all other controls are mouse-based
                if(code == KeyEvent.VK_ESCAPE) {
                    gamePanel.setGameState(GamePanel.PLAY_STATE);
                    System.out.println("ESC key pressed - exiting shipping bin state");
                }
            }            else if (gamePanel.getGameState() == GamePanel.STORE_STATE) {
                // Only handle ESC key to exit store - all other controls are mouse-based
                if(code == KeyEvent.VK_ESCAPE) {
                    gamePanel.setGameState(GamePanel.PLAY_STATE);
                    System.out.println("ESC key pressed - exiting store state");
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
