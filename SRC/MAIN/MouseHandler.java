package SRC.MAIN;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import SRC.ITEMS.Item;

public class MouseHandler extends MouseAdapter {
    
    private GamePanel gamePanel;
    private int targetX;
    private int targetY;
    private boolean hasTarget;
    
    // Selected inventory slot (for removing items)
    private int selectedSlotIndex = -1;
    
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
        // Get mouse click position (screen coordinates)
        int screenX = e.getX();
        int screenY = e.getY();
        
        // Check if we're in inventory state
        if (gamePanel.getGameState() == GamePanel.INVENTORY_STATE) {
            // Handle inventory clicks
            handleInventoryClick(screenX, screenY);
            return;
        }
          // Check if we're in shipping bin state
        if (gamePanel.getGameState() == GamePanel.SHIPPING_STATE) {
            // Handle shipping bin clicks
            gamePanel.getShippingBinUI().handleMouseClick(screenX, screenY);
            return;
        }
          // Check if we're in store state
        if (gamePanel.getGameState() == GamePanel.STORE_STATE) {            // Handle store clicks
            gamePanel.getStoreUI().handleMouseClick(screenX, screenY);
            return;
        }
        
        // Check if we're in cooking state
        if (gamePanel.getGameState() == GamePanel.COOKING_STATE) {
            // Handle cooking UI clicks
            gamePanel.getCookingUI().processMouseClick(screenX, screenY);
            return;
        }
        
        // Only handle map clicks in PLAY_STATE
        if (gamePanel.getGameState() == GamePanel.PLAY_STATE) {
            // Check if NPC interaction menu is open - if so, ignore map clicks
            if (gamePanel.isNPCInteractionMenuOpen()) {
                System.out.println("NPC interaction menu is open - ignoring map click");
                return;
            }
            
            // Check if message panel is active - if so, ignore map clicks
            if (gamePanel.isMessagePanelActive()) {
                System.out.println("NPC message panel is active - ignoring map click");
                return;
            }
            
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
                            " | Target world position: " + getTargetX() + ", " + getTargetY() + 
                            " map: " + gamePanel.getCurrentMap().getMapName());
        }
    }
    
    /**
     * Handle mouse clicks in the inventory screen
     */
    private void handleInventoryClick(int screenX, int screenY) {
        // Calculate inventory position (same calculation as in drawInventoryScreen)
        int inventoryWidth = 128 * gamePanel.getScale(); // 128 pixels * scale
        int inventoryHeight = 128 * gamePanel.getScale(); // 128 pixels * scale
        int x = (gamePanel.getScreenWidth() - inventoryWidth) / 2;
        int y = (gamePanel.getScreenHeight() - inventoryHeight) / 2;
        
        // Calculate slot size and parameters
        int slotSize = 32 * gamePanel.getScale(); // 32x32 pixel slots * scale
        int slotOffsetX = 0; // Offset from left edge of inventory
        int slotOffsetY = 0; // Offset from top edge of inventory        // Calculate remove button position (right side of inventory)
        int removeButtonWidth = 80; // Updated to match GamePanel
        int removeButtonHeight = 30;
        int removeButtonX = x + inventoryWidth + 10;
        int removeButtonY = y + 20;
        
        // Check if clicked on remove button
        if (screenX >= removeButtonX && screenX <= removeButtonX + removeButtonWidth &&
            screenY >= removeButtonY && screenY <= removeButtonY + removeButtonHeight) {
            // If a slot is selected, remove the item
            if (selectedSlotIndex >= 0) {
                gamePanel.getPlayer().removeItemFromInventory(selectedSlotIndex);
                System.out.println("Removed item from slot " + selectedSlotIndex);
                selectedSlotIndex = -1; // Reset selection
            } else {
                System.out.println("No item selected to remove");
            }
            return;
        }
        
        // Calculate use button position (below remove button)
        int useButtonWidth = 80;
        int useButtonHeight = 30;
        int useButtonX = x + inventoryWidth + 10;
        int useButtonY = removeButtonY + removeButtonHeight + 10; // 10px gap below remove button
        
        // Check if clicked on use button
        if (screenX >= useButtonX && screenX <= useButtonX + useButtonWidth &&
            screenY >= useButtonY && screenY <= useButtonY + useButtonHeight) {
            // If a slot is selected, use the item
            if (selectedSlotIndex >= 0) {
                useSelectedItem();
                System.out.println("Used item from slot " + selectedSlotIndex);
            } else {
                System.out.println("No item selected to use");
            }
            return;
        }
        
        // Check if clicked within inventory grid
        if (screenX >= x && screenX < x + inventoryWidth && screenY >= y && screenY < y + inventoryHeight) {
            // Calculate which slot was clicked
            int col = (screenX - x - slotOffsetX) / slotSize;
            int row = (screenY - y - slotOffsetY) / slotSize;
            
            // Ensure valid slot bounds
            if (col >= 0 && col < 4 && row >= 0 && row < 4) {
                int slotIndex = row * 4 + col;
                  // Make sure this slot has an item
                Item[] items = gamePanel.getPlayer().getInventoryItems();
                if (slotIndex < items.length && items[slotIndex] != null) {
                    // Set as selected slot
                    selectedSlotIndex = slotIndex;
                    System.out.println("Selected inventory slot: " + selectedSlotIndex);
                } else {
                    selectedSlotIndex = -1; // No item in this slot
                }
            }
        } else {
            // Clicked outside inventory, cancel selection
            selectedSlotIndex = -1;
        }
    }
    
    /**
     * Use the currently selected item
     */    
    private void useSelectedItem() {
        Item[] items = gamePanel.getPlayer().getInventoryItems();
        if (selectedSlotIndex >= 0 && selectedSlotIndex < items.length && items[selectedSlotIndex] != null) {
            // Get the selected item
            Item selectedItem = items[selectedSlotIndex];
            
            if (selectedItem != null) {
                // Use the item through PlayerAction
                String itemName = selectedItem.getName();
                String itemCategory = selectedItem.getCategory();
                
                // Call PlayerAction to use the item
                gamePanel.getPlayer().getPlayerAction().useItem(itemName, itemCategory);
                
                // For tools, we don't consume them, just activate them
                // For consumables (food, seeds), we might want to consume energy/quantity
                if (itemCategory.equals("Tool")) {
                    System.out.println("Equipped tool: " + itemName);
                    // Don't remove tools from inventory, just set as active
                } else if (itemCategory.equals("Seed")) {
                    System.out.println("Selected seed for planting: " + itemName);
                    // Set seed as active for planting                
                } else if (itemCategory.equals("Food") || itemCategory.equals("Fish") || itemCategory.equals("Crop")) {
                    // Use the PlayerAction eating system for all edible items
                    gamePanel.getPlayer().getPlayerAction().eatSelectedItem();
                }
            }
        }
    }
}