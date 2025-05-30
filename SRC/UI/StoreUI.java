package SRC.UI;

import SRC.MAIN.GamePanel;
import SRC.INVENTORY.Inventory;
import SRC.ITEMS.Item;
import SRC.STORE.StoreManager;
import SRC.STORE.Store;
import SRC.ENTITY.Player;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.List;

public class StoreUI {
    private GamePanel gp;
    private Player player;
    private Inventory inventory;
    
    private Store<Item> store;
    private StoreManager<Item> storeManager;
    
    private final int slotSize = 50;
    private final int storeCols = 4;
    private final int storeRows = 4;
    private final int inventoryCols = 4;
    private final int inventoryRows = 4;
    
    private int currentCategory = 0;
    private String[] categoryNames;
    
    private int selectedStoreSlot = 0;
    private int selectedInventorySlot = 0;
    private boolean selectingStore = true; // true=store, false=inventory
    
    private BufferedImage backgroundImage;
    private Rectangle buyButton;
    private Rectangle exitButton;
    private Rectangle prevCategoryButton;
    private Rectangle nextCategoryButton;
    private final int buttonWidth = 100;
    private final int buttonHeight = 40;
    private final int imageScale = 3;
    
    public StoreUI(GamePanel gp, Player player, Inventory inventory) {
        this.gp = gp;
        this.player = player;
        this.inventory = inventory;
        this.store = new Store<>("General Store");
        this.storeManager = store.getStoreManager();
        this.categoryNames = storeManager.getCategories();
        loadBackgroundImage();
        initializeButtons(); 
    }
    
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("RES/INVENTORY/shippingbin_inventory.png"));
        } catch (Exception e) {
            System.out.println("Could not load store UI background image: " + e.getMessage());
        }
    }
    
    private void initializeButtons() {
        int centerX = gp.getScreenWidth() / 2;
        int bottomY = gp.getScreenHeight() - 80;
        
        buyButton = new Rectangle(centerX - buttonWidth - 10, bottomY, buttonWidth, buttonHeight);
        exitButton = new Rectangle(centerX + 10, bottomY, buttonWidth, buttonHeight);
        
        prevCategoryButton = new Rectangle(centerX - 150, 50, 80, 30);
        nextCategoryButton = new Rectangle(centerX + 70, 50, 80, 30);
    }
    
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());
        
        if (backgroundImage != null) {
            int scaledWidth = backgroundImage.getWidth() * imageScale;
            int scaledHeight = backgroundImage.getHeight() * imageScale;
            int imageX = (gp.getScreenWidth() - scaledWidth) / 2;
            int imageY = (gp.getScreenHeight() - scaledHeight) / 2 - 20;
            
            g2.drawImage(backgroundImage, imageX, imageY, scaledWidth, scaledHeight, null);
            drawStoreSlots(g2, imageX, imageY, scaledWidth, scaledHeight);
            drawInventorySlots(g2, imageX, imageY, scaledWidth, scaledHeight);
        } 
        
        else {
            drawFallbackUI(g2);
        }
        
        drawCategoryNavigation(g2);
        drawButtons(g2);
        drawPlayerGold(g2);
        drawSelectedItemInfo(g2);
    }
    
    private void drawStoreSlots(Graphics2D g2, int baseX, int baseY, int imageWidth, int imageHeight) {
        int storeAreaX = baseX + (int)(imageWidth * 0.05);
        int storeAreaY = baseY + (int)(imageHeight * 0.15);
        int storeAreaWidth = (int)(imageWidth * 0.35);
        int storeAreaHeight = (int)(imageHeight * 0.70);
        
        int slotSpacing = 2;
        int totalSlotsWidth = (storeCols * slotSize) + ((storeCols - 1) * slotSpacing);
        int totalSlotsHeight = (storeRows * slotSize) + ((storeRows - 1) * slotSpacing);
        
        int startX = storeAreaX + (storeAreaWidth - totalSlotsWidth) / 2;
        int startY = storeAreaY + (storeAreaHeight - totalSlotsHeight) / 2;
        
        List<? extends Item> currentItems = getCurrentCategoryItems();
        
        for (int row = 0; row < storeRows; row++) {
            for (int col = 0; col < storeCols; col++) {
                int slotIndex = row * storeCols + col;
                int x = startX + col * (slotSize + slotSpacing);
                int y = startY + row * (slotSize + slotSpacing);
                
                if (selectingStore && selectedStoreSlot == slotIndex) {
                    g2.setColor(new Color(255, 255, 0, 100)); 
                } 
                
                else {
                    g2.setColor(new Color(255, 255, 255, 50));
                }

                g2.fillRect(x, y, slotSize, slotSize);
                g2.setColor(Color.WHITE);
                g2.drawRect(x, y, slotSize, slotSize);
                
                if (slotIndex < currentItems.size()) {
                    Item item = currentItems.get(slotIndex);
                    if (item.getImage() != null) {
                        g2.drawImage(item.getImage(), 
                                   x + 5, y + 5, slotSize - 10, slotSize - 10, null);
                    }
                    
                    int price = store.getBuyPrice(item);
                    g2.setColor(Color.YELLOW);
                    g2.setFont(new Font("Arial", Font.BOLD, 10));
                    g2.drawString("$" + price, x + 2, y + slotSize - 2);
                }
            }
        }
    }
    
    private void drawInventorySlots(Graphics2D g2, int baseX, int baseY, int imageWidth, int imageHeight) {
        int inventoryAreaX = baseX + (int)(imageWidth * 0.60);
        int inventoryAreaY = baseY + (int)(imageHeight * 0.15);
        int inventoryAreaWidth = (int)(imageWidth * 0.35);
        int inventoryAreaHeight = (int)(imageHeight * 0.70);
        
        int slotSpacing = 2;
        int totalSlotsWidth = (inventoryCols * slotSize) + ((inventoryCols - 1) * slotSpacing);
        int totalSlotsHeight = (inventoryRows * slotSize) + ((inventoryRows - 1) * slotSpacing);
        
        int startX = inventoryAreaX + (inventoryAreaWidth - totalSlotsWidth) / 2;
        int startY = inventoryAreaY + (inventoryAreaHeight - totalSlotsHeight) / 2;
        
        Item[] inventoryItems = inventory.getItems();
        
        for (int row = 0; row < inventoryRows; row++) {
            for (int col = 0; col < inventoryCols; col++) {
                int slotIndex = row * inventoryCols + col;
                int x = startX + col * (slotSize + slotSpacing);
                int y = startY + row * (slotSize + slotSpacing);
                
                if (!selectingStore && selectedInventorySlot == slotIndex) {
                    g2.setColor(new Color(255, 255, 0, 100)); 
                } 
                
                else {
                    g2.setColor(new Color(255, 255, 255, 50)); 
                }
                g2.fillRect(x, y, slotSize, slotSize);
                g2.setColor(Color.WHITE);
                g2.drawRect(x, y, slotSize, slotSize);

                if (slotIndex < inventoryItems.length && inventoryItems[slotIndex] != null) {
                    Item item = inventoryItems[slotIndex];
                    if (item.getImage() != null) {
                        g2.drawImage(item.getImage(), 
                                   x + 5, y + 5, slotSize - 10, slotSize - 10, null);
                    }
                }
            }
        }
    }
    
    private void drawFallbackUI(Graphics2D g2) {
        int panelWidth = 600;
        int panelHeight = 400;
        int panelX = (gp.getScreenWidth() - panelWidth) / 2;
        int panelY = (gp.getScreenHeight() - panelHeight) / 2;
        
        g2.setColor(new Color(139, 69, 19, 200)); 
        g2.fillRect(panelX, panelY, panelWidth, panelHeight);
        g2.setColor(Color.WHITE);
        g2.drawRect(panelX, panelY, panelWidth, panelHeight);
        
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("Store", panelX + panelWidth/2 - 30, panelY + 30);
    }
    
    private void drawCategoryNavigation(Graphics2D g2) {

        g2.setColor(new Color(139, 69, 19));
        g2.fillRect(prevCategoryButton.x, prevCategoryButton.y,prevCategoryButton.width, prevCategoryButton.height);
        g2.fillRect(nextCategoryButton.x, nextCategoryButton.y,nextCategoryButton.width, nextCategoryButton.height);
        
        g2.setColor(Color.WHITE);
        g2.drawRect(prevCategoryButton.x, prevCategoryButton.y,prevCategoryButton.width, prevCategoryButton.height);
        g2.drawRect(nextCategoryButton.x, nextCategoryButton.y,nextCategoryButton.width, nextCategoryButton.height);
        
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("< Prev", prevCategoryButton.x + 15, prevCategoryButton.y + 20);
        g2.drawString("Next >", nextCategoryButton.x + 15, nextCategoryButton.y + 20);
        
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        String categoryText = categoryNames[currentCategory];
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(categoryText);
        g2.drawString(categoryText, gp.getScreenWidth()/2 - textWidth/2, 35);
    }
    
    private void drawButtons(Graphics2D g2) {
        // Buy button
        g2.setColor(new Color(34, 139, 34)); // Forest green
        g2.fillRect(buyButton.x, buyButton.y, buyButton.width, buyButton.height);
        g2.setColor(Color.WHITE);
        g2.drawRect(buyButton.x, buyButton.y, buyButton.width, buyButton.height);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Buy", buyButton.x + 35, buyButton.y + 25);
        
        // Exit button
        g2.setColor(new Color(178, 34, 34)); // Fire brick red
        g2.fillRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        g2.setColor(Color.WHITE);
        g2.drawRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        g2.drawString("Exit", exitButton.x + 35, exitButton.y + 25);
    }
    
    private void drawPlayerGold(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Gold: $" + player.getGold(), 20, 30);
    }
    
    private void drawSelectedItemInfo(Graphics2D g2) {
        if (selectingStore) {
            List<? extends Item> currentItems = getCurrentCategoryItems();
            if (selectedStoreSlot < currentItems.size()) {
                Item item = currentItems.get(selectedStoreSlot);
                int price = store.getBuyPrice(item);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.drawString("Selected: " + item.getName() + " - $" + price, 20, gp.getScreenHeight() - 120);
            }
        }
    }
    
    private List<? extends Item> getCurrentCategoryItems() {
        if (currentCategory < categoryNames.length) {
            String categoryName = categoryNames[currentCategory];
            return storeManager.getItemsByCategory(categoryName);
        }
        return storeManager.getAllItems();
    }

    public void moveSelection(String direction) {
        if (selectingStore) {
            moveStoreSelection(direction);
        } else {
            moveInventorySelection(direction);
        }
    }
    
    private void moveStoreSelection(String direction) {
        List<? extends Item> currentItems = getCurrentCategoryItems();
        int maxSlots = Math.min(storeCols * storeRows, currentItems.size());
        
        switch (direction) {
            case "UP":
                if (selectedStoreSlot >= storeCols) {
                    selectedStoreSlot -= storeCols;
                }
                break;
            case "DOWN":
                if (selectedStoreSlot + storeCols < maxSlots) {
                    selectedStoreSlot += storeCols;
                }
                break;
            case "LEFT":
                if (selectedStoreSlot % storeCols > 0) {
                    selectedStoreSlot--;
                }
                break;
            case "RIGHT":
                if (selectedStoreSlot % storeCols < storeCols - 1 && selectedStoreSlot + 1 < maxSlots) {
                    selectedStoreSlot++;
                }
                break;
        }
    }
    
    private void moveInventorySelection(String direction) {
        Item[] inventoryItems = inventory.getItems();
        int maxSlots = Math.min(inventoryCols * inventoryRows, inventoryItems.length);
        
        switch (direction) {
            case "UP":
                if (selectedInventorySlot >= inventoryCols) {
                    selectedInventorySlot -= inventoryCols;
                }
                break;
            case "DOWN":
                if (selectedInventorySlot + inventoryCols < maxSlots) {
                    selectedInventorySlot += inventoryCols;
                }
                break;
            case "LEFT":
                if (selectedInventorySlot % inventoryCols > 0) {
                    selectedInventorySlot--;
                }
                break;
            case "RIGHT":
                if (selectedInventorySlot % inventoryCols < inventoryCols - 1 && selectedInventorySlot + 1 < maxSlots) {
                    selectedInventorySlot++;
                }
                break;
        }
    }
    
    public void switchSelection() {
        selectingStore = !selectingStore;
    }
    
    public void changeCategory(int direction) {
        currentCategory = (currentCategory + direction + categoryNames.length) % categoryNames.length;
        selectedStoreSlot = 0; // Reset selection when changing category
    }
    
    public void buySelectedItem() {
        if (!selectingStore) return;
        
        List<? extends Item> currentItems = getCurrentCategoryItems();
        if (selectedStoreSlot >= currentItems.size()) return;
        
        Item item = currentItems.get(selectedStoreSlot);
        
        // Use the new simplified store system to purchase item
        if (store.purchaseItem(item, 1, player)) {
            System.out.println("Bought " + item.getName() + " for $" + store.getBuyPrice(item));
        } else {
            int price = store.getBuyPrice(item);
            if (player.getGold() < price) {
                System.out.println("Not enough gold! Need $" + price + ", have $" + player.getGold());
            } else {
                System.out.println("Cannot purchase item (inventory full or other issue)");
            }
        }
    }
    
    public void handleMouseClick(int mouseX, int mouseY) {
        if (buyButton.contains(mouseX, mouseY)) {
            buySelectedItem();
        } else if (exitButton.contains(mouseX, mouseY)) {
            gp.setGameState(GamePanel.PLAY_STATE);
        } else if (prevCategoryButton.contains(mouseX, mouseY)) {
            changeCategory(-1);
        } else if (nextCategoryButton.contains(mouseX, mouseY)) {
            changeCategory(1);
        } else {
            // Check if clicking on store slots or inventory slots
            handleStoreSlotClick(mouseX, mouseY);
            handleInventorySlotClick(mouseX, mouseY);
        }
    }
    
    private void handleStoreSlotClick(int mouseX, int mouseY) {
        if (backgroundImage == null) return;
        
        // Calculate positions for scaled image
        int scaledWidth = backgroundImage.getWidth() * imageScale;
        int scaledHeight = backgroundImage.getHeight() * imageScale;
        int baseX = (gp.getScreenWidth() - scaledWidth) / 2;
        int baseY = (gp.getScreenHeight() - scaledHeight) / 2 - 20;
        
        // Calculate store area (left side)
        int storeAreaX = baseX + (int)(scaledWidth * 0.05);
        int storeAreaY = baseY + (int)(scaledHeight * 0.15);
        int storeAreaWidth = (int)(scaledWidth * 0.35);
        int storeAreaHeight = (int)(scaledHeight * 0.70);
        
        // Calculate slot positions
        int slotSpacing = 2;
        int totalSlotsWidth = (storeCols * slotSize) + ((storeCols - 1) * slotSpacing);
        int totalSlotsHeight = (storeRows * slotSize) + ((storeRows - 1) * slotSpacing);
        
        int startX = storeAreaX + (storeAreaWidth - totalSlotsWidth) / 2;
        int startY = storeAreaY + (storeAreaHeight - totalSlotsHeight) / 2;
        
        List<? extends Item> currentItems = getCurrentCategoryItems();
        
        for (int row = 0; row < storeRows; row++) {
            for (int col = 0; col < storeCols; col++) {
                int slotIndex = row * storeCols + col;
                int x = startX + col * (slotSize + slotSpacing);
                int y = startY + row * (slotSize + slotSpacing);
                
                if (mouseX >= x && mouseX <= x + slotSize && 
                    mouseY >= y && mouseY <= y + slotSize &&
                    slotIndex < currentItems.size()) {
                    selectedStoreSlot = slotIndex;
                    selectingStore = true;
                    System.out.println("Selected store slot: " + slotIndex);
                    return;
                }
            }
        }
    }
    
    private void handleInventorySlotClick(int mouseX, int mouseY) {
        if (backgroundImage == null) return;
        
        // Calculate positions for scaled image
        int scaledWidth = backgroundImage.getWidth() * imageScale;
        int scaledHeight = backgroundImage.getHeight() * imageScale;
        int baseX = (gp.getScreenWidth() - scaledWidth) / 2;
        int baseY = (gp.getScreenHeight() - scaledHeight) / 2 - 20;
        
        // Calculate inventory area (right side)
        int inventoryAreaX = baseX + (int)(scaledWidth * 0.60);
        int inventoryAreaY = baseY + (int)(scaledHeight * 0.15);
        int inventoryAreaWidth = (int)(scaledWidth * 0.35);
        int inventoryAreaHeight = (int)(scaledHeight * 0.70);
        
        // Calculate slot positions
        int slotSpacing = 2;
        int totalSlotsWidth = (inventoryCols * slotSize) + ((inventoryCols - 1) * slotSpacing);
        int totalSlotsHeight = (inventoryRows * slotSize) + ((inventoryRows - 1) * slotSpacing);
        
        int startX = inventoryAreaX + (inventoryAreaWidth - totalSlotsWidth) / 2;
        int startY = inventoryAreaY + (inventoryAreaHeight - totalSlotsHeight) / 2;
        
        Item[] inventoryItems = inventory.getItems();
        
        for (int row = 0; row < inventoryRows; row++) {
            for (int col = 0; col < inventoryCols; col++) {
                int slotIndex = row * inventoryCols + col;
                int x = startX + col * (slotSize + slotSpacing);
                int y = startY + row * (slotSize + slotSpacing);
                
                if (mouseX >= x && mouseX <= x + slotSize && 
                    mouseY >= y && mouseY <= y + slotSize &&
                    slotIndex < inventoryItems.length && inventoryItems[slotIndex] != null) {
                    selectedInventorySlot = slotIndex;
                    selectingStore = false;
                    System.out.println("Selected inventory slot: " + slotIndex);
                    return;
                }
            }
        }
    }
}
