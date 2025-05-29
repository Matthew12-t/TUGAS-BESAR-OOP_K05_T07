package SRC.UI;

import SRC.MAIN.GamePanel;
import SRC.DATA.ShippingBinData;
import SRC.INVENTORY.Inventory;
import SRC.ITEMS.Item;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.List;

public class ShippingBinUI {
    private GamePanel gp;
    private ShippingBinData shippingBinData;
    private Inventory inventory;
    private final int slotSize = 48;
    private final int inventoryCols = 4;
    private final int inventoryRows = 4;
    private final int shippingBinCols = 4;
    private final int shippingBinRows = 4;
    private int selectedInventorySlot = 0;
    private BufferedImage backgroundImage;
    
    // Button properties
    private Rectangle sellButton;
    private Rectangle exitButton;
    private final int buttonWidth = 100;
    private final int buttonHeight = 40;
    
    public ShippingBinUI_New(GamePanel gp, ShippingBinData shippingBinData, Inventory inventory) {
        this.gp = gp;
        this.shippingBinData = shippingBinData;
        this.inventory = inventory;
        loadBackgroundImage();
        initializeButtons();
    }
    
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("RES/INVENTORY/shippingbin_inventory.png"));
        } catch (Exception e) {
            System.out.println("Could not load shipping bin inventory image: " + e.getMessage());
        }
    }
    
    private void initializeButtons() {
        // Calculate button positions (center bottom)
        int centerX = gp.getScreenWidth() / 2;
        int bottomY = gp.getScreenHeight() - 100;
        
        sellButton = new Rectangle(centerX - buttonWidth - 10, bottomY, buttonWidth, buttonHeight);
        exitButton = new Rectangle(centerX + 10, bottomY, buttonWidth, buttonHeight);
    }
    
    public void draw(Graphics2D g2) {
        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());
        
        // Draw background image if available
        if (backgroundImage != null) {
            int imageX = (gp.getScreenWidth() - backgroundImage.getWidth()) / 2;
            int imageY = (gp.getScreenHeight() - backgroundImage.getHeight()) / 2 - 50; // Offset up for buttons
            g2.drawImage(backgroundImage, imageX, imageY, null);
            
            // Draw inventory slots
            drawInventorySlots(g2, imageX, imageY);
            
            // Draw shipping bin slots
            drawShippingBinSlots(g2, imageX, imageY);
        } else {
            // Fallback if image doesn't load
            drawFallbackUI(g2);
        }
        
        // Draw buttons
        drawButtons(g2);
        
        // Draw total value
        drawTotalValue(g2);
    }
    
    private void drawInventorySlots(Graphics2D g2, int baseX, int baseY) {
        // Inventory slots are on the left side of the image (4x4 grid)
        int startX = baseX + 50; // Adjust based on image layout
        int startY = baseY + 50; // Adjust based on image layout
        
        for (int row = 0; row < inventoryRows; row++) {
            for (int col = 0; col < inventoryCols; col++) {
                int slotIndex = row * inventoryCols + col;
                int x = startX + col * (slotSize + 4);
                int y = startY + row * (slotSize + 4);
                
                // Highlight selected slot
                if (slotIndex == selectedInventorySlot) {
                    g2.setColor(new Color(255, 255, 0, 100)); // Yellow highlight
                    g2.fillRect(x - 2, y - 2, slotSize + 4, slotSize + 4);
                }
                
                // Draw item if present
                Item item = inventory.getItem(slotIndex);
                if (item != null) {
                    BufferedImage itemImage = item.getImage();
                    if (itemImage != null) {
                        g2.drawImage(itemImage, x, y, slotSize, slotSize, null);
                        
                        // Draw quantity
                        int quantity = inventory.getQuantity(slotIndex);
                        if (quantity > 1) {
                            g2.setColor(Color.WHITE);
                            g2.setFont(new Font("Arial", Font.BOLD, 12));
                            g2.drawString(String.valueOf(quantity), x + slotSize - 15, y + slotSize - 2);
                        }
                    }
                }
            }
        }
    }
    
    private void drawShippingBinSlots(Graphics2D g2, int baseX, int baseY) {
        // Shipping bin slots are on the right side of the image (4x4 grid)
        int startX = baseX + 300; // Adjust based on image layout
        int startY = baseY + 50;  // Adjust based on image layout
        
        List<Item> shippingItems = shippingBinData.getItems();
        List<Integer> quantities = shippingBinData.getQuantities();
        
        for (int row = 0; row < shippingBinRows; row++) {
            for (int col = 0; col < shippingBinCols; col++) {
                int slotIndex = row * shippingBinCols + col;
                int x = startX + col * (slotSize + 4);
                int y = startY + row * (slotSize + 4);
                
                // Draw item if present
                if (slotIndex < shippingItems.size()) {
                    Item item = shippingItems.get(slotIndex);
                    if (item != null) {
                        BufferedImage itemImage = item.getImage();
                        if (itemImage != null) {
                            g2.drawImage(itemImage, x, y, slotSize, slotSize, null);
                            
                            // Draw quantity
                            int quantity = quantities.get(slotIndex);
                            if (quantity > 1) {
                                g2.setColor(Color.WHITE);
                                g2.setFont(new Font("Arial", Font.BOLD, 12));
                                g2.drawString(String.valueOf(quantity), x + slotSize - 15, y + slotSize - 2);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void drawFallbackUI(Graphics2D g2) {
        // Simple fallback UI if image doesn't load
        int panelWidth = 600;
        int panelHeight = 400;
        int panelX = (gp.getScreenWidth() - panelWidth) / 2;
        int panelY = (gp.getScreenHeight() - panelHeight) / 2 - 50;
        
        g2.setColor(new Color(139, 69, 19));
        g2.fillRect(panelX, panelY, panelWidth, panelHeight);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(panelX, panelY, panelWidth, panelHeight);
        
        // Draw title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String title = "Shipping Bin";
        FontMetrics titleMetrics = g2.getFontMetrics();
        int titleX = panelX + (panelWidth - titleMetrics.stringWidth(title)) / 2;
        g2.drawString(title, titleX, panelY + 30);
        
        // Draw inventory and shipping bin areas
        drawInventorySlots(g2, panelX, panelY);
        drawShippingBinSlots(g2, panelX, panelY);
    }
    
    private void drawButtons(Graphics2D g2) {
        // Draw Sell button
        g2.setColor(new Color(34, 139, 34)); // Forest green
        g2.fillRect(sellButton.x, sellButton.y, sellButton.width, sellButton.height);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(sellButton.x, sellButton.y, sellButton.width, sellButton.height);
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics metrics = g2.getFontMetrics();
        String sellText = "SELL";
        int sellTextX = sellButton.x + (sellButton.width - metrics.stringWidth(sellText)) / 2;
        int sellTextY = sellButton.y + (sellButton.height + metrics.getAscent()) / 2;
        g2.drawString(sellText, sellTextX, sellTextY);
        
        // Draw Exit button
        g2.setColor(new Color(178, 34, 34)); // Dark red
        g2.fillRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        g2.setColor(Color.BLACK);
        g2.drawRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        
        g2.setColor(Color.WHITE);
        String exitText = "EXIT";
        int exitTextX = exitButton.x + (exitButton.width - metrics.stringWidth(exitText)) / 2;
        int exitTextY = exitButton.y + (exitButton.height + metrics.getAscent()) / 2;
        g2.drawString(exitText, exitTextX, exitTextY);
    }
    
    private void drawTotalValue(Graphics2D g2) {
        int totalValue = shippingBinData.calculateTotalValue();
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        
        String valueText = "Total Value: " + totalValue + " Gold";
        FontMetrics metrics = g2.getFontMetrics();
        int textX = (gp.getScreenWidth() - metrics.stringWidth(valueText)) / 2;
        int textY = gp.getScreenHeight() - 150;
        
        // Draw background for text
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(textX - 10, textY - metrics.getAscent() - 5, 
                   metrics.stringWidth(valueText) + 20, metrics.getHeight() + 10);
        
        g2.setColor(Color.YELLOW);
        g2.drawString(valueText, textX, textY);
    }
    
    // Handle mouse clicks
    public void handleMouseClick(int mouseX, int mouseY) {
        if (sellButton.contains(mouseX, mouseY)) {
            sellSelectedItem();
        } else if (exitButton.contains(mouseX, mouseY)) {
            gp.setGameState(GamePanel.PLAY_STATE);
        } else {
            // Check if clicking on inventory slots
            handleInventoryClick(mouseX, mouseY);
        }
    }
    
    private void handleInventoryClick(int mouseX, int mouseY) {
        // Calculate which inventory slot was clicked
        int baseX = (gp.getScreenWidth() - (backgroundImage != null ? backgroundImage.getWidth() : 600)) / 2;
        int baseY = (gp.getScreenHeight() - (backgroundImage != null ? backgroundImage.getHeight() : 400)) / 2 - 50;
        
        int startX = baseX + 50;
        int startY = baseY + 50;
        
        for (int row = 0; row < inventoryRows; row++) {
            for (int col = 0; col < inventoryCols; col++) {
                int slotIndex = row * inventoryCols + col;
                int x = startX + col * (slotSize + 4);
                int y = startY + row * (slotSize + 4);
                
                if (mouseX >= x && mouseX <= x + slotSize && 
                    mouseY >= y && mouseY <= y + slotSize) {
                    selectedInventorySlot = slotIndex;
                    break;
                }
            }
        }
    }
    
    private void sellSelectedItem() {
        Item selectedItem = inventory.getItem(selectedInventorySlot);
        if (selectedItem != null) {
            int quantity = inventory.getQuantity(selectedInventorySlot);
            
            // Move 1 item at a time to shipping bin
            shippingBinData.addItem(selectedItem, 1);
            
            // Remove from inventory
            if (quantity > 1) {
                inventory.getQuantities()[selectedInventorySlot]--;
            } else {
                inventory.removeItem(selectedInventorySlot);
            }
            
            System.out.println("Moved " + selectedItem.getName() + " to shipping bin");
        }
    }
    
    // Handle arrow key navigation
    public void handleKeyPress(int keyCode) {
        if (keyCode == 38) { // UP arrow
            if (selectedInventorySlot >= inventoryCols) {
                selectedInventorySlot -= inventoryCols;
            }
        } else if (keyCode == 40) { // DOWN arrow
            if (selectedInventorySlot < (inventoryRows - 1) * inventoryCols) {
                selectedInventorySlot += inventoryCols;
            }
        } else if (keyCode == 37) { // LEFT arrow
            if (selectedInventorySlot % inventoryCols > 0) {
                selectedInventorySlot--;
            }
        } else if (keyCode == 39) { // RIGHT arrow
            if (selectedInventorySlot % inventoryCols < inventoryCols - 1) {
                selectedInventorySlot++;
            }
        }
    }
}
