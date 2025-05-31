package SRC.MAIN;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import SRC.ITEMS.Item;

public class MouseHandler extends MouseAdapter {
    
    private GamePanel gamePanel;
    private int targetX;
    private int targetY;
    private boolean hasTarget;
    
    
    private int selectedSlotIndex = -1;
    
    public MouseHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.hasTarget = false;
    }
    
    
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
    
    public int getSelectedSlotIndex() {
        return selectedSlotIndex;
    }
    
    public void setSelectedSlotIndex(int selectedSlotIndex) {
        this.selectedSlotIndex = selectedSlotIndex;
    }
    
    private boolean isPlacingHouse = false;
    
    public void setPlacingHouse(boolean placingHouse) {
        this.isPlacingHouse = placingHouse;
    }
    
    public boolean isPlacingHouse() {
        return isPlacingHouse;
    }
      @Override
    public void mousePressed(MouseEvent e) {
        
        int screenX = e.getX();
        int screenY = e.getY();
        
        
        if (gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
            
            handleInventoryClick(screenX, screenY);
            return;
        }
          
        if (gamePanel.getGameState() == GamePanel.SHIPPING_STATE) {
            
            gamePanel.getShippingBinUI().handleMouseClick(screenX, screenY);
            return;
        }
          
        if (gamePanel.getGameState() == GamePanel.STORE_STATE) {            
            gamePanel.getStoreUI().handleMouseClick(screenX, screenY);
            return;
        }
        
        
        if (gamePanel.getGameState() == GamePanel.COOKING_STATE) {
            
            gamePanel.getCookingUI().processMouseClick(screenX, screenY);
            return;
        }
        
        
        if (gamePanel.getGameState() == GamePanel.PLAY_STATE) {
            
            if (gamePanel.isNPCInteractionMenuOpen()) {
                System.out.println("NPC interaction menu is open - ignoring map click");
                return;
            }
            
            
            if (gamePanel.isMessagePanelActive()) {
                System.out.println("NPC message panel is active - ignoring map click");
                return;
            }
            
            
            int worldX = screenX + gamePanel.getCameraX();
            int worldY = screenY + gamePanel.getCameraY();
            
            
            int tileSize = gamePanel.getTileSize();
            int col = worldX / tileSize;
            int row = worldY / tileSize;
            
            
            int centeredWorldX = col * tileSize + (tileSize / 2);
            int centeredWorldY = row * tileSize + (tileSize / 2);
            
            
            setTargetX(centeredWorldX);
            setTargetY(centeredWorldY);
            setHasTarget(true);
            
            
            System.out.println("Mouse clicked at screen: " + screenX + ", " + screenY + 
                            " | Target tile: " + col + ", " + row +
                            " | Target world position: " + getTargetX() + ", " + getTargetY() + 
                            " map: " + gamePanel.getCurrentMap().getMapName());
        }
    }
    
    /**
     * Handle mouse clicks in the inventory screen
     */
    private void handleInventoryClick(int screenX, int screenY) {
        
        int inventoryWidth = 128 * gamePanel.getScale(); 
        int inventoryHeight = 128 * gamePanel.getScale(); 
        int x = (gamePanel.getScreenWidth() - inventoryWidth) / 2;
        int y = (gamePanel.getScreenHeight() - inventoryHeight) / 2;
        
        
        int slotSize = 32 * gamePanel.getScale(); 
        int slotOffsetX = 0; 
        int slotOffsetY = 0; 
        int removeButtonWidth = 80; 
        int removeButtonHeight = 30;
        int removeButtonX = x + inventoryWidth + 10;
        int removeButtonY = y + 20;
        
        
        if (screenX >= removeButtonX && screenX <= removeButtonX + removeButtonWidth &&
            screenY >= removeButtonY && screenY <= removeButtonY + removeButtonHeight) {
            
            if (selectedSlotIndex >= 0) {
                gamePanel.getPlayer().removeItemFromInventory(selectedSlotIndex);
                System.out.println("Removed item from slot " + selectedSlotIndex);
                selectedSlotIndex = -1; 
            } else {
                System.out.println("No item selected to remove");
            }
            return;
        }
        
        
        int useButtonWidth = 80;
        int useButtonHeight = 30;
        int useButtonX = x + inventoryWidth + 10;
        int useButtonY = removeButtonY + removeButtonHeight + 10; 
        
        
        if (screenX >= useButtonX && screenX <= useButtonX + useButtonWidth &&
            screenY >= useButtonY && screenY <= useButtonY + useButtonHeight) {
            
            if (selectedSlotIndex >= 0) {
                useSelectedItem();
                System.out.println("Used item from slot " + selectedSlotIndex);
            } else {
                System.out.println("No item selected to use");
            }
            return;
        }
        
        
        if (screenX >= x && screenX < x + inventoryWidth && screenY >= y && screenY < y + inventoryHeight) {
            
            int col = (screenX - x - slotOffsetX) / slotSize;
            int row = (screenY - y - slotOffsetY) / slotSize;
            
            
            if (col >= 0 && col < 4 && row >= 0 && row < 4) {
                int slotIndex = row * 4 + col;
                  
                Item[] items = gamePanel.getPlayer().getInventoryItems();
                if (slotIndex < items.length && items[slotIndex] != null) {
                    
                    selectedSlotIndex = slotIndex;
                    System.out.println("Selected inventory slot: " + selectedSlotIndex);
                } else {
                    selectedSlotIndex = -1; 
                }
            }
        } else {
            
            selectedSlotIndex = -1;
        }
    }
    
    /**
     * Use the currently selected item
     */    
    private void useSelectedItem() {
        Item[] items = gamePanel.getPlayer().getInventoryItems();
        if (selectedSlotIndex >= 0 && selectedSlotIndex < items.length && items[selectedSlotIndex] != null) {
            
            Item selectedItem = items[selectedSlotIndex];
            
            if (selectedItem != null) {
                
                String itemName = selectedItem.getName();
                String itemCategory = selectedItem.getCategory();
                
                
                gamePanel.getPlayer().getPlayerAction().useItem(itemName, itemCategory);
                
                
                
                if (itemCategory.equals("Tool")) {
                    System.out.println("Equipped tool: " + itemName);
                    
                } else if (itemCategory.equals("Seed")) {
                    System.out.println("Selected seed for planting: " + itemName);
                    
                } else if (itemCategory.equals("Food") || itemCategory.equals("Fish") || itemCategory.equals("Crop")) {
                    
                    gamePanel.getPlayer().getPlayerAction().eatSelectedItem();
                }
            }
        }
    }
}