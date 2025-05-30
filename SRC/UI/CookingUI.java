package SRC.UI;

import SRC.COOKING.Cooking;
import SRC.COOKING.Recipe;
import SRC.ITEMS.Item;
import SRC.MAIN.GamePanel;
import SRC.INVENTORY.Inventory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class CookingUI {
    private GamePanel gp;
    private BufferedImage backgroundImage;
    
    // UI dimensions
    private final int slotSize = 50;
    private final int inventoryCols = 4;
    private final int inventoryRows = 4;
    private final int recipeCols = 4;
    private final int recipeRows = 4;
    
    // Recipe navigation
    private int currentRecipeIndex = 0;
    
    // Selection
    private int selectedInventorySlot = 0;
    
    // UI elements
    private Rectangle cookButton;
    private Rectangle exitButton;
    private Rectangle prevRecipeButton;
    private Rectangle nextRecipeButton;
    private final int buttonWidth = 100;
    private final int buttonHeight = 40;
    private final int imageScale = 3;
    
    public CookingUI(GamePanel gp) {
        this.gp = gp;
        loadBackgroundImage();
        initializeButtons();
    }
    
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("RES/INVENTORY/shippingbin_inventory.png"));
            System.out.println("Successfully loaded cooking UI background image");
        } catch (Exception e) {
            System.out.println("Could not load cooking UI background image: " + e.getMessage());
            // Create a fallback background if needed
            createFallbackBackground();
        }
    }
    
    private void createFallbackBackground() {
        backgroundImage = new BufferedImage(256, 192, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = backgroundImage.createGraphics();
        
        // Create a simple cooking-themed background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(139, 115, 85),  // Brown top
            0, 192, new Color(101, 84, 63)  // Darker brown bottom
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, 256, 192);
        
        g2.dispose();
    }
    
    private void initializeButtons() {
        int centerX = gp.getScreenWidth() / 2;
        int bottomY = gp.getScreenHeight() - 80;
        
        cookButton = new Rectangle(centerX - buttonWidth - 10, bottomY, buttonWidth, buttonHeight);
        exitButton = new Rectangle(centerX + 10, bottomY, buttonWidth, buttonHeight);
        
        // Recipe navigation buttons
        prevRecipeButton = new Rectangle(centerX - 150, 50, 80, 30);
        nextRecipeButton = new Rectangle(centerX + 70, 50, 80, 30);
    }
    
    public void draw(Graphics2D g2) {
        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());
        
        // Draw background image if available
        if (backgroundImage != null) {
            int scaledWidth = backgroundImage.getWidth() * imageScale;
            int scaledHeight = backgroundImage.getHeight() * imageScale;
            int imageX = (gp.getScreenWidth() - scaledWidth) / 2;
            int imageY = (gp.getScreenHeight() - scaledHeight) / 2 - 20;
            
            g2.drawImage(backgroundImage, imageX, imageY, scaledWidth, scaledHeight, null);
            
            // Draw inventory slots (left side - normal display)
            drawInventorySlots(g2, imageX, imageY, scaledWidth, scaledHeight);
            
            // Draw recipe slots (right side - transparent display)
            drawRecipeSlots(g2, imageX, imageY, scaledWidth, scaledHeight);
        }
        
        // Draw recipe navigation
        drawRecipeNavigation(g2);
        
        // Draw buttons
        drawButtons(g2);
        
        // Draw fuel status
        drawFuelStatus(g2);
        
        // Draw current recipe info
        drawRecipeInfo(g2);
    }
    
    private void drawInventorySlots(Graphics2D g2, int baseX, int baseY, int imageWidth, int imageHeight) {
        // Calculate inventory area - left side
        int inventoryAreaX = baseX + (int)(imageWidth * 0.05);
        int inventoryAreaY = baseY + (int)(imageHeight * 0.15);
        int inventoryAreaWidth = (int)(imageWidth * 0.35);
        int inventoryAreaHeight = (int)(imageHeight * 0.70);
        
        // Calculate slot positions
        int slotSpacing = 2;
        int totalSlotsWidth = (inventoryCols * slotSize) + ((inventoryCols - 1) * slotSpacing);
        int totalSlotsHeight = (inventoryRows * slotSize) + ((inventoryRows - 1) * slotSpacing);
        
        int startX = inventoryAreaX + (inventoryAreaWidth - totalSlotsWidth) / 2;
        int startY = inventoryAreaY + (inventoryAreaHeight - totalSlotsHeight) / 2;
        
        Item[] inventoryItems = gp.getPlayer().getInventoryItems();
        int[] quantities = gp.getPlayer().getInventoryQuantities();
        
        for (int row = 0; row < inventoryRows; row++) {
            for (int col = 0; col < inventoryCols; col++) {
                int slotIndex = row * inventoryCols + col;
                int x = startX + col * (slotSize + slotSpacing);
                int y = startY + row * (slotSize + slotSpacing);
                
                // Draw slot background
                if (selectedInventorySlot == slotIndex) {
                    g2.setColor(new Color(255, 255, 0, 100)); // Yellow highlight
                } else {
                    g2.setColor(new Color(255, 255, 255, 50)); // White background
                }
                g2.fillRect(x, y, slotSize, slotSize);
                g2.setColor(Color.WHITE);
                g2.drawRect(x, y, slotSize, slotSize);
                
                // Draw item if available (NORMAL OPACITY)
                if (slotIndex < inventoryItems.length && inventoryItems[slotIndex] != null) {
                    Item item = inventoryItems[slotIndex];
                    if (item.getImage() != null) {
                        g2.drawImage(item.getImage(), 
                                   x + 5, y + 5, slotSize - 10, slotSize - 10, null);
                    }
                    
                    // Draw quantity
                    if (slotIndex < quantities.length && quantities[slotIndex] > 1) {
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Arial", Font.BOLD, 10));
                        g2.drawString(String.valueOf(quantities[slotIndex]), x + 2, y + slotSize - 2);
                    }
                }
            }
        }
          // Draw "INVENTORY" label - MOVED TO TOP
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String inventoryLabel = "INVENTORY";
        int labelX = inventoryAreaX + (inventoryAreaWidth - g2.getFontMetrics().stringWidth(inventoryLabel)) / 2;
        g2.drawString(inventoryLabel, labelX, baseY - 10); // Moved to top
    }
    
    private void drawRecipeSlots(Graphics2D g2, int baseX, int baseY, int imageWidth, int imageHeight) {
        // Calculate recipe area - right side
        int recipeAreaX = baseX + (int)(imageWidth * 0.60);
        int recipeAreaY = baseY + (int)(imageHeight * 0.15);
        int recipeAreaWidth = (int)(imageWidth * 0.35);
        int recipeAreaHeight = (int)(imageHeight * 0.70);
        
        // Calculate slot positions
        int slotSpacing = 2;
        int totalSlotsWidth = (recipeCols * slotSize) + ((recipeCols - 1) * slotSpacing);
        int totalSlotsHeight = (recipeRows * slotSize) + ((recipeRows - 1) * slotSpacing);
        
        int startX = recipeAreaX + (recipeAreaWidth - totalSlotsWidth) / 2;
        int startY = recipeAreaY + (recipeAreaHeight - totalSlotsHeight) / 2;
        
        // Get current recipe
        List<Recipe> recipes = Cooking.getAllRecipes();
        if (recipes.isEmpty()) return;
        
        Recipe currentRecipe = recipes.get(currentRecipeIndex);
        
        // Draw recipe ingredient slots
        int slotIndex = 0;
        for (String ingredientName : currentRecipe.getIngredients().keySet()) {
            if (slotIndex >= recipeCols * recipeRows) break;
            
            int row = slotIndex / recipeCols;
            int col = slotIndex % recipeCols;
            int x = startX + col * (slotSize + slotSpacing);
            int y = startY + row * (slotSize + slotSpacing);
              // Draw slot background
            g2.setColor(new Color(255, 255, 255, 30)); // Semi-transparent white
            g2.fillRect(x, y, slotSize, slotSize);
            g2.setColor(Color.WHITE);
            g2.drawRect(x, y, slotSize, slotSize);
              // Display ALL required ingredients (with transparency if not in inventory)
            int available = gp.getPlayer().getPlayerAction().getInventory().getItemCount(ingredientName);
            boolean hasIngredient = available > 0;
            
            // Get the ingredient item to display
            Item ingredientItem = null;
            String displayIngredientName = ingredientName;
            
            // Handle special case for "Any Fish" - show sardine image
            if (ingredientName.equals("Any Fish")) {
                ingredientItem = SRC.DATA.GameData.getItem("Sardine", 1);
                displayIngredientName = "Any Fish";
            } else {
                // Try to get from player inventory first
                if (hasIngredient) {
                    Item[] inventoryItems = gp.getPlayer().getInventoryItems();
                    for (Item item : inventoryItems) {
                        if (item != null && item.getName().equals(ingredientName)) {
                            ingredientItem = item;
                            break;
                        }
                    }
                }
                
                // If not found in inventory, get from GameData
                if (ingredientItem == null) {
                    ingredientItem = SRC.DATA.GameData.getItem(ingredientName, 1);
                }
            }
              // Draw the ingredient image (normal opacity for available items, reduced for missing)
            if (ingredientItem != null && ingredientItem.getImage() != null) {
                if (hasIngredient) {
                    // Normal opacity for items in inventory
                    g2.drawImage(ingredientItem.getImage(), 
                               x + 5, y + 5, slotSize - 15, slotSize - 15, null);
                } else {
                    // Reduced opacity only for missing ingredients
                    Composite originalComposite = g2.getComposite();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                    g2.drawImage(ingredientItem.getImage(), 
                               x + 5, y + 5, slotSize - 15, slotSize - 15, null);
                    g2.setComposite(originalComposite);
                }
            }
              // Draw ingredient name below the image (turunkan sedikit lagi)
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 8));
            FontMetrics fm = g2.getFontMetrics();
            String displayName = displayIngredientName;
            
            // Truncate long names to fit in slot
            if (fm.stringWidth(displayName) > slotSize - 4) {
                while (fm.stringWidth(displayName + "...") > slotSize - 4 && displayName.length() > 3) {
                    displayName = displayName.substring(0, displayName.length() - 1);
                }
                displayName += "...";
            }
            
            int nameX = x + (slotSize - fm.stringWidth(displayName)) / 2;
            g2.drawString(displayName, nameX, y + slotSize - 12); // Turunkan dari -15 ke -12
              
            // Draw required quantity
            int required = currentRecipe.getIngredients().get(ingredientName);
            // available already declared above
            
            // Color code based on availability
            if (available >= required) {
                g2.setColor(Color.GREEN);
            } else {
                g2.setColor(Color.RED);
            }
            
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString(available + "/" + required, x + 2, y + slotSize - 2);
            
            slotIndex++;
        }
          // Draw "RECIPE" label at top
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String recipeLabel = "RECIPE";
        int labelX = recipeAreaX + (recipeAreaWidth - g2.getFontMetrics().stringWidth(recipeLabel)) / 2;
        g2.drawString(recipeLabel, labelX, baseY - 10);
    }
    
    private void drawRecipeNavigation(Graphics2D g2) {
        List<Recipe> recipes = Cooking.getAllRecipes();
        if (recipes.isEmpty()) return;
        
        Recipe currentRecipe = recipes.get(currentRecipeIndex);
          // Draw recipe navigation buttons - more visible colors
        g2.setColor(new Color(70, 130, 180)); // Steel blue for better visibility
        g2.fillRect(prevRecipeButton.x, prevRecipeButton.y, prevRecipeButton.width, prevRecipeButton.height);
        g2.fillRect(nextRecipeButton.x, nextRecipeButton.y, nextRecipeButton.width, nextRecipeButton.height);
        
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(prevRecipeButton.x, prevRecipeButton.y, prevRecipeButton.width, prevRecipeButton.height);
        g2.drawRect(nextRecipeButton.x, nextRecipeButton.y, nextRecipeButton.width, nextRecipeButton.height);
        
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("< PREV", prevRecipeButton.x + 10, prevRecipeButton.y + 20);
        g2.drawString("NEXT >", nextRecipeButton.x + 10, nextRecipeButton.y + 20);
        
        // Draw current recipe name and navigation info
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String recipeName = currentRecipe.getDishName();
        int nameX = (gp.getScreenWidth() - g2.getFontMetrics().stringWidth(recipeName)) / 2;
        g2.drawString(recipeName, nameX, 100);
        
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        String navInfo = "(" + (currentRecipeIndex + 1) + "/" + recipes.size() + ")";
        int navX = (gp.getScreenWidth() - g2.getFontMetrics().stringWidth(navInfo)) / 2;
        g2.drawString(navInfo, navX, 115);
    }
      private void drawButtons(Graphics2D g2) {
        // Cook button - bright green, more visible
        g2.setColor(new Color(0, 180, 0)); // Bright green
        g2.fillRect(cookButton.x, cookButton.y, cookButton.width, cookButton.height);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(cookButton.x, cookButton.y, cookButton.width, cookButton.height);
        
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String cookText = "COOK";
        int cookTextX = cookButton.x + (cookButton.width - g2.getFontMetrics().stringWidth(cookText)) / 2;
        g2.drawString(cookText, cookTextX, cookButton.y + 25);
        
        // Exit button - bright red, more visible
        g2.setColor(new Color(200, 50, 50)); // Bright red
        g2.fillRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(exitButton.x, exitButton.y, exitButton.width, exitButton.height);
        
        String exitText = "EXIT";
        int exitTextX = exitButton.x + (exitButton.width - g2.getFontMetrics().stringWidth(exitText)) / 2;
        g2.drawString(exitText, exitTextX, exitButton.y + 25);
    }
    
    private void drawFuelStatus(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.YELLOW);
        
        int firewood = gp.getPlayer().getPlayerAction().getInventory().getItemCount("Firewood");
        int coal = gp.getPlayer().getPlayerAction().getInventory().getItemCount("Coal");
        
        String fuelText = "Fuel: Firewood(" + firewood + ") Coal(" + coal + ")";
        g2.drawString(fuelText, 20, gp.getScreenHeight() - 100);
    }
      private void drawRecipeInfo(Graphics2D g2) {
        List<Recipe> recipes = Cooking.getAllRecipes();
        if (recipes.isEmpty()) return;
        
        Recipe currentRecipe = recipes.get(currentRecipeIndex);
        
        // Draw recipe description in the left corner
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(Color.LIGHT_GRAY);
        String description = currentRecipe.getDescription();
        g2.drawString(description, 20, 140);
        
        // Draw energy value centered below the food name
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.YELLOW);
        String energyText = "Energy: +" + currentRecipe.getEnergyValue();
        int energyX = (gp.getScreenWidth() - g2.getFontMetrics().stringWidth(energyText)) / 2;
        g2.drawString(energyText, energyX, 130); // Position below recipe name (which is at y=100)
    }
    
    public void update() {
        // Update logic if needed
    }
    
    public void processInput(int keyCode) {
        switch (keyCode) {
            case 37: // LEFT - Move inventory selection
                selectedInventorySlot--;
                if (selectedInventorySlot < 0) selectedInventorySlot = (inventoryCols * inventoryRows) - 1;
                break;
            case 39: // RIGHT - Move inventory selection
                selectedInventorySlot++;
                if (selectedInventorySlot >= inventoryCols * inventoryRows) selectedInventorySlot = 0;
                break;
            case 38: // UP - Move inventory selection
                selectedInventorySlot -= inventoryCols;
                if (selectedInventorySlot < 0) selectedInventorySlot += inventoryCols * inventoryRows;
                break;
            case 40: // DOWN - Move inventory selection
                selectedInventorySlot += inventoryCols;
                if (selectedInventorySlot >= inventoryCols * inventoryRows) selectedInventorySlot -= inventoryCols * inventoryRows;
                break;
            case 65: // A - Previous recipe
                currentRecipeIndex--;
                if (currentRecipeIndex < 0) currentRecipeIndex = Cooking.getAllRecipes().size() - 1;
                break;
            case 68: // D - Next recipe
                currentRecipeIndex++;
                if (currentRecipeIndex >= Cooking.getAllRecipes().size()) currentRecipeIndex = 0;
                break;
            case 10: // ENTER - Cook recipe
                cookCurrentRecipe();
                break;
            case 27: // ESCAPE - Exit
                gp.setGameState(GamePanel.PLAY_STATE);
                break;
        }
    }
    
    public void processMouseClick(int mouseX, int mouseY) {
        // Check cook button
        if (cookButton.contains(mouseX, mouseY)) {
            cookCurrentRecipe();
        }
        
        // Check exit button
        if (exitButton.contains(mouseX, mouseY)) {
            gp.setGameState(GamePanel.PLAY_STATE);
        }
        
        // Check recipe navigation buttons
        if (prevRecipeButton.contains(mouseX, mouseY)) {
            currentRecipeIndex--;
            if (currentRecipeIndex < 0) currentRecipeIndex = Cooking.getAllRecipes().size() - 1;
        }
        
        if (nextRecipeButton.contains(mouseX, mouseY)) {
            currentRecipeIndex++;
            if (currentRecipeIndex >= Cooking.getAllRecipes().size()) currentRecipeIndex = 0;
        }
    }
    
    private void cookCurrentRecipe() {
        List<Recipe> recipes = Cooking.getAllRecipes();
        if (recipes.isEmpty()) return;
        
        Recipe currentRecipe = recipes.get(currentRecipeIndex);
        
        // Prepare inventory map for cooking system
        java.util.Map<String, Integer> inventoryMap = new java.util.HashMap<>();
        Item[] items = gp.getPlayer().getInventoryItems();
        int[] quantities = gp.getPlayer().getInventoryQuantities();
        for (int i = 0; i < items.length && i < quantities.length; i++) {
            if (items[i] != null) {
                inventoryMap.put(items[i].getName(), quantities[i]);
            }
        }
        
        // Check if we have fuel and can cook
        boolean hasFuel = Cooking.hasFuel(inventoryMap);
        
        if (hasFuel) {
            // Check if we have all ingredients
            boolean hasAllIngredients = true;
            for (String ingredient : currentRecipe.getIngredients().keySet()) {
                int required = currentRecipe.getIngredients().get(ingredient);
                int available = gp.getPlayer().getPlayerAction().getInventory().getItemCount(ingredient);
                if (available < required) {
                    hasAllIngredients = false;
                    break;
                }
            }
              if (hasAllIngredients) {
                // Try to consume fuel and cook the recipe
                boolean fuelConsumed = Cooking.consumeFuel(inventoryMap);
                if (fuelConsumed) {
                    // Consume ingredients from player inventory
                    Inventory playerInventory = gp.getPlayer().getPlayerAction().getInventory();
                    boolean ingredientsConsumed = true;
                    
                    for (String ingredient : currentRecipe.getIngredients().keySet()) {
                        int required = currentRecipe.getIngredients().get(ingredient);
                        
                        // Handle special case for "Any Fish"
                        if (ingredient.equals("Any Fish")) {
                            int fishConsumed = 0;
                            String[] fishTypes = {"Salmon", "Carp", "Catfish", "Angler", "Bullhead", 
                                                 "Chub", "Flounder", "Halibut", "Largemouth Bass", 
                                                 "Midnight Carp", "Rainbow Trout", "Sardine", "Sturgeon",
                                                 "Crimsonfish", "Glacierfish", "Octopus", "Super Cucumber"};
                            
                            for (String fishType : fishTypes) {
                                if (fishConsumed >= required) break;
                                
                                while (fishConsumed < required && playerInventory.hasItem(fishType)) {
                                    if (!consumeItemFromInventory(fishType, 1)) {
                                        ingredientsConsumed = false;
                                        break;
                                    }
                                    fishConsumed++;
                                }
                            }
                            
                            if (fishConsumed < required) {
                                ingredientsConsumed = false;
                            }
                        } else {
                            // Normal ingredient consumption
                            if (!consumeItemFromInventory(ingredient, required)) {
                                ingredientsConsumed = false;
                                break;
                            }
                        }
                    }
                    
                    if (ingredientsConsumed) {
                        // Create cooked dish and add to inventory
                        SRC.ITEMS.Food cookedDish = SRC.DATA.FoodData.getFood(currentRecipe.getDishName());
                        if (cookedDish != null) {
                            playerInventory.addItem(cookedDish, 1);
                            gp.getNPCUi().showMessagePanel("Successfully cooked " + currentRecipe.getDishName() + "!");
                              // Add game time for cooking (10 minutes)
                            gp.addGameTime(10);
                            
                            // Reduce player energy for cooking (5 energy)
                            int currentEnergy = gp.getPlayer().getEnergy();
                            gp.getPlayer().setEnergy(Math.max(0, currentEnergy - 5));
                        } else {
                            gp.getNPCUi().showMessagePanel("Recipe result not found: " + currentRecipe.getDishName());
                        }
                    } else {
                        gp.getNPCUi().showMessagePanel("Failed to consume ingredients!");
                    }
                } else {
                    gp.getNPCUi().showMessagePanel("Failed to consume fuel!");
                }
            } else {
                gp.getNPCUi().showMessagePanel("Not enough ingredients!");
            }
        } else {            gp.getNPCUi().showMessagePanel("No fuel available!");
        }
    }
    
    /**
     * Helper method to consume items from player inventory
     * @param itemName Name of the item to consume
     * @param quantity Quantity to consume
     * @return true if consumption was successful
     */
    private boolean consumeItemFromInventory(String itemName, int quantity) {
        Inventory playerInventory = gp.getPlayer().getPlayerAction().getInventory();
        Item[] items = playerInventory.getItems();
        int[] quantities = playerInventory.getQuantities();
        
        int remainingToConsume = quantity;
        
        // Find and consume the required quantity
        for (int i = 0; i < items.length && remainingToConsume > 0; i++) {
            if (items[i] != null && items[i].getName().equals(itemName)) {
                int available = quantities[i];
                int toConsume = Math.min(available, remainingToConsume);
                
                if (available > toConsume) {
                    quantities[i] -= toConsume;
                } else {
                    playerInventory.removeItem(i);
                }
                
                remainingToConsume -= toConsume;
            }
        }
        
        return remainingToConsume == 0;
    }
}
