package SRC.MAIN;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import SRC.ENTITY.NPCEntity;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    
    
    private GamePanel gamePanel;
    
    
    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
      
    public KeyHandler() {
        
    }    
      @Override
    public void keyTyped(KeyEvent e) {
        
        if (gamePanel != null && gamePanel.getGameState() == GamePanel.CHEAT_STATE) {
            gamePanel.getCheatUI().handleCharInput(e.getKeyChar());
        }
    }@Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        
        if (gamePanel != null) {
            
            if (gamePanel.getGameState() == GamePanel.MAIN_MENU_STATE) {
                if (gamePanel.getMainMenu() != null) {
                    gamePanel.getMainMenu().handleKeyPress(e);
                }
                return;
            }
            
            
            if (gamePanel.getGameState() == GamePanel.NEW_GAME_STATE) {
                if (gamePanel.getNewGameUI() != null) {
                    gamePanel.getNewGameUI().handleKeyPress(e);
                }
                return;
            }
            
            
            if (gamePanel.getGameState() == GamePanel.LOAD_GAME_STATE) {
                if (gamePanel.getLoadGameUI() != null) {
                    gamePanel.getLoadGameUI().handleKeyPress(e);
                }
                return;
            }
              
            if (gamePanel.getGameState() == GamePanel.OPTIONS_STATE) {
                if (gamePanel.getOptionsUI() != null) {
                    gamePanel.getOptionsUI().handleKeyPress(e);
                }
                return;
            }
            
            
            if (gamePanel.getGameState() == GamePanel.PLAYER_STATISTICS_STATE) {
                if (code == KeyEvent.VK_ESCAPE) {
                    gamePanel.setGameState(GamePanel.OPTIONS_STATE);
                    System.out.println("ESC key pressed - returning from player statistics to options");
                }
                return;
            }
            
            
            if (gamePanel.getGameState() == GamePanel.PLAY_STATE && code == KeyEvent.VK_ESCAPE) {
                gamePanel.setGameState(GamePanel.OPTIONS_STATE);
                return;
            }
        }
        
        
        if (gamePanel != null && e.isControlDown() && code == KeyEvent.VK_C) {
            gamePanel.toggleCheatConsole();
            return; 
        }
        
        
        if (gamePanel != null && gamePanel.getGameState() == GamePanel.CHEAT_STATE) {
            gamePanel.getCheatUI().handleKeyInput(e);
            return; 
        }
          
        if (gamePanel != null) {
            
            if (gamePanel.getGameState() == GamePanel.SLEEP_STATE) {
                if (code == KeyEvent.VK_ENTER) {
                    System.out.println("DEBUG: Enter pressed in sleep state");
                    gamePanel.getPlayer().getPlayerAction().getSleepUI().handleEnterPress();
                }
                return; 
            }
              
            if (gamePanel.getGameState() == GamePanel.ENDGAME_STATE) {
                if (code == KeyEvent.VK_ENTER) {
                    System.out.println("DEBUG: Enter pressed in endgame state");
                    gamePanel.getEndGameUI().handleEnterPress();
                }
                return; 
            }
            
            
            if(code == KeyEvent.VK_I) {
                if (gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
                    gamePanel.setGameState(GamePanel.PLAY_STATE);
                    System.out.println("Closed inventory");
                } else {
                    gamePanel.setGameState(GamePanel.INVENTORY_STATE);
                    System.out.println("Opened inventory");
                }
            }            
            if (gamePanel.getGameState() == GamePanel.PLAY_STATE) {
                
                if (gamePanel.isNPCInteractionMenuOpen()) {
                    
                    if (code == KeyEvent.VK_T) {
                        gamePanel.getNPCUi().handleTalkKey();
                    } else if (code == KeyEvent.VK_G) {
                        gamePanel.getNPCUi().handleGiftKey();
                    } else if (code == KeyEvent.VK_P) {
                        gamePanel.getNPCUi().handleProposeKey();
                    } else if (code == KeyEvent.VK_ENTER) {
                        gamePanel.getNPCUi().handleEnterKey();
                    } else if (code == KeyEvent.VK_Q) {
                        gamePanel.closeNPCInteractionMenu();
                    }
                    return; 
                }                
                if (gamePanel.isMessagePanelActive()) {
                    if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                        
                        gamePanel.getNPCUi().clearMessagePanel();
                        System.out.println("Message panel dismissed");
                    }
                    return; 
                }
                
                
                if (gamePanel.getPlayer().getPlayerAction().getTvUI().isWatching()) {
                    if (code == KeyEvent.VK_ESCAPE) {
                        gamePanel.getPlayer().getPlayerAction().getTvUI().handleEscapePress();
                        System.out.println("DEBUG: Escape pressed - stopped watching TV");
                    }
                    return; 
                }
                
                
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

                if(code == KeyEvent.VK_P) {
                    
                    if (gamePanel.getPlayer().getPlayerAction().getHeldSeedName() != null) {
                        System.out.println("DEBUG: 'P' key pressed for planting (holding seed)");
                        gamePanel.getPlayer().getPlayerAction().performPlanting();
                    } else {
                        
                        boolean isPlacing = gamePanel.getMouseHandler().isPlacingHouse();
                        gamePanel.getMouseHandler().setPlacingHouse(!isPlacing);
                        System.out.println("House placement mode: " + (!isPlacing ? "ON" : "OFF"));
                    }
                }
  
                  
                if(code == KeyEvent.VK_DELETE || code == KeyEvent.VK_BACK_SPACE) {
                    if (gamePanel.getMouseHandler().isHasTarget()) {
                        int worldX = gamePanel.getMouseHandler().getTargetX();
                        int worldY = gamePanel.getMouseHandler().getTargetY();
                        int tileCol = worldX / gamePanel.getTileSize();
                        int tileRow = worldY / gamePanel.getTileSize();
                        
                        gamePanel.getCurrentMap().removeObject(tileCol, tileRow);
                    }
                }                
                if(code == KeyEvent.VK_C) {
                    
                    if (gamePanel.getPlayer().getPlayerAction().isPlayerNearBed()) {
                        System.out.println("DEBUG: 'C' key pressed for sleep (near bed)");
                        gamePanel.getPlayer().getPlayerAction().performSleep();
                    } else if (gamePanel.getPlayer().getPlayerAction().isPlayerNearStove()) {
                        System.out.println("DEBUG: 'C' key pressed for cooking (near stove)");
                        gamePanel.getPlayer().getPlayerAction().performCooking();
                    } else if (gamePanel.getCurrentMap().getMapName().equals("Store Map")) {
                        System.out.println("DEBUG: 'C' key pressed for store (in Store Map)");
                        gamePanel.setGameState(GamePanel.STORE_STATE);
                    } else if (gamePanel.getPlayer().getPlayerAction().isPlayerNearShippingBin()) {
                        System.out.println("DEBUG: 'C' key pressed for shipping bin");
                        gamePanel.setGameState(GamePanel.SHIPPING_STATE);                    } else if (gamePanel.getPlayer().getPlayerAction().isPlayerNearTV()) {
                        System.out.println("DEBUG: 'C' key pressed for TV watching (near TV)");
                        gamePanel.getPlayer().getPlayerAction().performWatchTV();
                    } else if (gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
                        
                        
                        int tileSize = gamePanel.getTileSize();
                        int playerCol = (gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getPlayerVisualWidth() / 2) / tileSize;
                        int playerRow = (gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getPlayerVisualHeight() / 2) / tileSize;
                        
                        if (gamePanel.getTileManager().hasPlantAt(playerCol, playerRow) && 
                            gamePanel.getTileManager().isPlantReadyToHarvest(playerCol, playerRow)) {
                            System.out.println("DEBUG: 'C' key pressed for harvesting (ready crop at player position)");
                            gamePanel.getPlayer().getPlayerAction().performHarvesting();
                        } else if (gamePanel.getPlayer().isHolding("Watering Can")) {
                            System.out.println("DEBUG: 'C' key pressed for watering (holding watering can in farm map)");
                            gamePanel.getPlayer().getPlayerAction().performWatering();
                        } else if (gamePanel.getPlayer().isHoldingAnySeed()) {
                            System.out.println("DEBUG: 'C' key pressed for planting (holding seeds in farm map)");
                            gamePanel.getPlayer().getPlayerAction().performPlanting();
                        } else if (gamePanel.getPlayer().isHolding("Hoe")) {
                            System.out.println("DEBUG: 'C' key pressed for tilling (holding hoe in farm map)");
                            gamePanel.getPlayer().getPlayerAction().performTilling();
                        } else if (gamePanel.getPlayer().isHolding("Pickaxe")) {
                            System.out.println("DEBUG: 'C' key pressed for land recovery (holding pickaxe in farm map)");
                            gamePanel.getPlayer().getPlayerAction().performLandRecovery();
                        } else {
                            System.out.println("DEBUG: 'C' key pressed for fishing (no specific farm action available)");
                            gamePanel.getPlayer().getPlayerAction().performFishing();
                        };
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
            
            else if (gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
                if (code == KeyEvent.VK_G) {
                    
                    if (gamePanel.getGiftingTargetNPC() != null) {
                        gamePanel.confirmGiftFromInventory();
                    }
                }
            }            
              
              if(code == KeyEvent.VK_E) {
                if (gamePanel.getGameState() == GamePanel.PLAY_STATE || gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
                    
                    gamePanel.getPlayer().getPlayerAction().eatSelectedItem();
                    System.out.println("DEBUG: 'E' key pressed for eating in state: " + 
                                     (gamePanel.getGameState() == GamePanel.PLAY_STATE ? "PLAY" : "INVENTORY"));
                }
            }

            
            else if (gamePanel.getGameState() == GamePanel.MAP_MENU_STATE) {
                
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
                
                
                if(code == KeyEvent.VK_W) {
                    System.out.println("W key pressed in map menu");
                    gamePanel.selectPreviousMap();
                }                if(code == KeyEvent.VK_S) {
                    System.out.println("S key pressed in map menu");
                    gamePanel.selectNextMap();
                }            }            else if (gamePanel.getGameState() == GamePanel.SHIPPING_STATE) {
                
                if(code == KeyEvent.VK_ESCAPE) {
                    gamePanel.setGameState(GamePanel.PLAY_STATE);
                    System.out.println("ESC key pressed - exiting shipping bin state");
                }
            }            else if (gamePanel.getGameState() == GamePanel.STORE_STATE) {
                
                if(code == KeyEvent.VK_ESCAPE) {
                    gamePanel.setGameState(GamePanel.PLAY_STATE);
                    System.out.println("ESC key pressed - exiting store state");
                }            }
            else if (gamePanel.getGameState() == GamePanel.COOKING_STATE) {
                
                if(code == KeyEvent.VK_ESCAPE) {
                    gamePanel.setGameState(GamePanel.PLAY_STATE);
                    System.out.println("ESC key pressed - exiting cooking state");
                } else {
                    
                    gamePanel.getCookingUI().processInput(code);
                }
            }
            
            else if (gamePanel.isNPCInteractionMenuOpen()) {
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
                return;
            }
        }
        else {
            
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
    

    public void resetAllKeyStates() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        System.out.println("DEBUG: All key states reset");
    }
}
