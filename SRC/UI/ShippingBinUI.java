package SRC.UI;

import SRC.MAIN.GamePanel;
import SRC.DATA.ShippingBinData;
import SRC.INVENTORY.Inventory;
import SRC.ITEMS.Item;
import SRC.ITEMS.Tool;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.List;

public class ShippingBinUI {
    private GamePanel gp;
    private ShippingBinData shippingBinData;
    private Inventory inventory;
    private final int slotSize = 50;
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
    
    // Image scaling
    private final int imageScale = 3;
      public ShippingBinUI(GamePanel gp, ShippingBinData shippingBinData, Inventory inventory) {
        this.gp = gp;
        this.shippingBinData = shippingBinData;
        this.inventory = inventory;
        loadBackgroundImage();
        initializeButtons();
    }
    
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("RES/INVENTORY/shippingbin_inventory.png"));
            System.out.println("Successfully loaded shipping bin inventory image");
        } catch (Exception e) {
            System.out.println("Could not load shipping bin inventory image: " + e.getMessage());
        }
    }
    
    private void initializeButtons() {
        // Calculate button positions (center bottom)
        int centerX = gp.getScreenWidth() / 2;
        int bottomY = gp.getScreenHeight() - 80;
        
        sellButton = new Rectangle(centerX - buttonWidth - 10, bottomY, buttonWidth, buttonHeight);
        exitButton = new Rectangle(centerX + 10, bottomY, buttonWidth, buttonHeight);
    }
    
    public void draw(Graphics2D g2) {
        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());
        
        // Draw background image if available (scaled up)
        if (backgroundImage != null) {
            int scaledWidth = backgroundImage.getWidth() * imageScale; // 160 * 2 = 320
            int scaledHeight = backgroundImage.getHeight() * imageScale; // 75 * 2 = 150
            int imageX = (gp.getScreenWidth() - scaledWidth) / 2;
            int imageY = (gp.getScreenHeight() - scaledHeight) / 2 - 20;
            
            g2.drawImage(backgroundImage, imageX, imageY, scaledWidth, scaledHeight, null);
            
            // Draw inventory slots (left side - 4x4 grid)
            drawInventorySlots(g2, imageX, imageY, scaledWidth, scaledHeight);
            
            // Draw shipping bin slots (right side - 4x4 grid)
            drawShippingBinSlots(g2, imageX, imageY, scaledWidth, scaledHeight);
        } else {
            // Fallback if image doesn't load
            drawFallbackUI(g2);
        }
        
        // Draw buttons
        drawButtons(g2);
        
        // Draw total value
        drawTotalValue(g2);
    }    private void drawInventorySlots(Graphics2D g2, int baseX, int baseY, int imageWidth, int imageHeight) {
        // Calculate inventory area within scaled image - left side
        int inventoryAreaX = baseX + (int)(imageWidth * 0.05); // 5% padding from left
        int inventoryAreaY = baseY + (int)(imageHeight * 0.15); // 25% padding from top
        int inventoryAreaWidth = (int)(imageWidth * 0.35); // 45% of image width
        int inventoryAreaHeight = (int)(imageHeight * 0.70); // 70% of image height
        
        // Calculate slot positions within inventory area
        int slotSpacing = 2;
        int totalSlotsWidth = (inventoryCols * slotSize) + ((inventoryCols - 1) * slotSpacing);
        int totalSlotsHeight = (inventoryRows * slotSize) + ((inventoryRows - 1) * slotSpacing);
        
        int startX = inventoryAreaX + (inventoryAreaWidth - totalSlotsWidth) / 2;
        int startY = inventoryAreaY + (inventoryAreaHeight - totalSlotsHeight) / 2;
        
        for (int row = 0; row < inventoryRows; row++) {
            for (int col = 0; col < inventoryCols; col++) {
                int slotIndex = row * inventoryCols + col;
                int x = startX + col * (slotSize + slotSpacing);
                int y = startY + row * (slotSize + slotSpacing);
                
                // Highlight selected slot
                if (slotIndex == selectedInventorySlot) {
                    g2.setColor(new Color(255, 255, 0, 150)); // Yellow highlight
                    g2.fillRect(x - 2, y - 2, slotSize + 4, slotSize + 4);
                    g2.setColor(Color.YELLOW);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRect(x - 2, y - 2, slotSize + 4, slotSize + 4);
                }
                
                // Draw slot border
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(x, y, slotSize, slotSize);
                
                // Draw item if present
                Item item = inventory.getItem(slotIndex);
                if (item != null) {
                    BufferedImage itemImage = item.getImage();
                    if (itemImage != null) {
                        g2.drawImage(itemImage, x + 2, y + 2, slotSize - 4, slotSize - 4, null);
                        
                        // Draw quantity
                        int quantity = inventory.getQuantity(slotIndex);
                        if (quantity > 1) {
                            g2.setColor(Color.WHITE);
                            g2.setFont(new Font("Arial", Font.BOLD, 10));
                            FontMetrics fm = g2.getFontMetrics();
                            String qtyText = String.valueOf(quantity);
                            g2.fillRect(x + slotSize - fm.stringWidth(qtyText) - 3, y + slotSize - fm.getHeight() + 1, 
                                       fm.stringWidth(qtyText) + 2, fm.getHeight() - 1);
                            g2.setColor(Color.BLACK);
                            g2.drawString(qtyText, x + slotSize - fm.stringWidth(qtyText) - 2, y + slotSize - 2);
                        }
                        
                        // Draw red X if item is a tool (cannot be sold)
                        if (item instanceof Tool) {
                            g2.setColor(new Color(255, 0, 0, 180)); // Semi-transparent red
                            g2.fillRect(x, y, slotSize, slotSize);
                            g2.setColor(Color.WHITE);
                            g2.setStroke(new BasicStroke(3));
                            // Draw X
                            g2.drawLine(x + 4, y + 4, x + slotSize - 4, y + slotSize - 4);
                            g2.drawLine(x + slotSize - 4, y + 4, x + 4, y + slotSize - 4);
                        }
                    }
                }
            }
        }
    }
      private void drawShippingBinSlots(Graphics2D g2, int baseX, int baseY, int imageWidth, int imageHeight) {
        // Calculate shipping bin area within scaled image - right side
        int shippingAreaX = baseX + (int)(imageWidth * 0.50); // Start at 50% of image width
        int shippingAreaY = baseY + (int)(imageHeight * 0.15); // 25% padding from top
        int shippingAreaWidth = (int)(imageWidth * 0.55); // 45% of image width
        int shippingAreaHeight = (int)(imageHeight * 0.70); // 70% of image height
        
        // Calculate slot positions within shipping area
        int slotSpacing = 2;
        int totalSlotsWidth = (shippingBinCols * slotSize) + ((shippingBinCols - 1) * slotSpacing);
        int totalSlotsHeight = (shippingBinRows * slotSize) + ((shippingBinRows - 1) * slotSpacing);
        
        int startX = shippingAreaX + (shippingAreaWidth - totalSlotsWidth) / 2;
        int startY = shippingAreaY + (shippingAreaHeight - totalSlotsHeight) / 2;
        
        List<Item> shippingItems = shippingBinData.getItems();
        List<Integer> quantities = shippingBinData.getQuantities();
        
        for (int row = 0; row < shippingBinRows; row++) {
            for (int col = 0; col < shippingBinCols; col++) {
                int slotIndex = row * shippingBinCols + col;
                int x = startX + col * (slotSize + slotSpacing);
                int y = startY + row * (slotSize + slotSpacing);
                
                // Draw slot border
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1));
                g2.drawRect(x, y, slotSize, slotSize);
                
                // Draw item if present
                if (slotIndex < shippingItems.size()) {
                    Item item = shippingItems.get(slotIndex);
                    if (item != null) {
                        BufferedImage itemImage = item.getImage();
                        if (itemImage != null) {
                            g2.drawImage(itemImage, x + 2, y + 2, slotSize - 4, slotSize - 4, null);
                            
                            // Draw quantity
                            int quantity = quantities.get(slotIndex);
                            if (quantity > 1) {
                                g2.setColor(Color.WHITE);
                                g2.setFont(new Font("Arial", Font.BOLD, 10));
                                FontMetrics fm = g2.getFontMetrics();
                                String qtyText = String.valueOf(quantity);
                                g2.fillRect(x + slotSize - fm.stringWidth(qtyText) - 3, y + slotSize - fm.getHeight() + 1, 
                                           fm.stringWidth(qtyText) + 2, fm.getHeight() - 1);
                                g2.setColor(Color.BLACK);
                                g2.drawString(qtyText, x + slotSize - fm.stringWidth(qtyText) - 2, y + slotSize - 2);
                            }
                        }
                    }
                }
            }
        }
    }      private void drawFallbackUI(Graphics2D g2) {
        // Simple fallback UI if image doesn't load
        int panelWidth = 800;
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
        drawInventorySlots(g2, panelX, panelY, panelWidth, panelHeight);
        drawShippingBinSlots(g2, panelX, panelY, panelWidth, panelHeight);
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
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        
        String valueText = "Total Value: " + totalValue + " Gold";
        FontMetrics metrics = g2.getFontMetrics();
        int textX = (gp.getScreenWidth() - metrics.stringWidth(valueText)) / 2;
        int textY = gp.getScreenHeight() - 120;
        
        // Draw background for text
        g2.setColor(new Color(0, 0, 0, 180));
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
        if (backgroundImage == null) return;
        
        // Calculate positions for scaled image
        int scaledWidth = backgroundImage.getWidth() * imageScale;
        int scaledHeight = backgroundImage.getHeight() * imageScale;
        int baseX = (gp.getScreenWidth() - scaledWidth) / 2;
        int baseY = (gp.getScreenHeight() - scaledHeight) / 2 - 50;
        
        // Calculate inventory area
        int inventoryAreaX = baseX + (int)(scaledWidth * 0.05);
        int inventoryAreaY = baseY + (int)(scaledHeight * 0.20);
        int inventoryAreaWidth = (int)(scaledWidth * 0.30);
        int inventoryAreaHeight = (int)(scaledHeight * 0.70);
        
        // Calculate slot positions
        int slotSpacing = 2;
        int totalSlotsWidth = (inventoryCols * slotSize) + ((inventoryCols - 1) * slotSpacing);
        int totalSlotsHeight = (inventoryRows * slotSize) + ((inventoryRows - 1) * slotSpacing);
        
        int startX = inventoryAreaX + (inventoryAreaWidth - totalSlotsWidth) / 2;
        int startY = inventoryAreaY + (inventoryAreaHeight - totalSlotsHeight) / 2;
        
        for (int row = 0; row < inventoryRows; row++) {
            for (int col = 0; col < inventoryCols; col++) {
                int slotIndex = row * inventoryCols + col;
                int x = startX + col * (slotSize + slotSpacing);
                int y = startY + row * (slotSize + slotSpacing);
                
                if (mouseX >= x && mouseX <= x + slotSize && 
                    mouseY >= y && mouseY <= y + slotSize) {
                    selectedInventorySlot = slotIndex;
                    System.out.println("Selected inventory slot: " + slotIndex);
                    return;
                }
            }
        }
    }
      private void sellSelectedItem() {
        Item selectedItem = inventory.getItem(selectedInventorySlot);
        
        if (selectedItem == null) {
            System.out.println("No item selected to sell");
            return;
        }
        
        // Check if the item is a tool - tools cannot be sold
        if (selectedItem instanceof Tool) {
            System.out.println("Tools cannot be sold!");
            // You could add a visual notification here
            return;
        }
        
        int quantity = inventory.getQuantity(selectedInventorySlot);
        
        if (quantity <= 0) {
            System.out.println("No quantity to sell");
            return;
        }
        
        // Move item to shipping bin (only 1 item at a time for simplicity)
        shippingBinData.addItem(selectedItem, 1);
        
        // Remove from inventory
        if (quantity > 1) {
            inventory.getQuantities()[selectedInventorySlot]--;
        } else {
            inventory.removeItem(selectedInventorySlot);
        }
        
        System.out.println("Moved 1 " + selectedItem.getName() + " to shipping bin");
    }
    
    // Keep this method for backward compatibility but make it do nothing
    public void handleKeyPress(int keyCode) {
        // Key handling disabled - using mouse clicks instead
    }
}
