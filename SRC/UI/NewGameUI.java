package SRC.UI;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;

import SRC.MAIN.GamePanel;

/**
 * NewGameUI handles the new game creation interface
 * Allows player to set name, farm name, gender, and favorite item
 */
public class NewGameUI {
    private GamePanel gamePanel;
    
    // UI fields
    private String playerName = "";
    private String farmName = "";
    private String gender = "BOY"; // "BOY" or "GIRL"
    private String favoriteItem = "";
      // Input field selection
    private int selectedField = 0; // 0=playerName, 1=farmName, 2=gender, 3=favoriteItem, 4=confirm
    private final int TOTAL_FIELDS = 5;
    
    // Gender options
    private final String[] genderOptions = {"BOY", "GIRL"};
    private int selectedGenderIndex = 0;
    
    // Common favorite items
    private final String[] favoriteItems = {
        "Parsnip", "Carrot", "Potato", "Cauliflower", "Kale", "Tulip",
        "Daffodil", "Jazz", "Poppy", "Summer Spangle", "Spice Berry",
        "Wild Horseradish", "Leek", "Dandelion", "Wood", "Stone"
    };
    private int selectedFavoriteIndex = 0;
    
    // UI properties
    private final int FIELD_WIDTH = 300;
    private final int FIELD_HEIGHT = 40;
    private final int BUTTON_WIDTH = 120;
    private final int BUTTON_HEIGHT = 40;
    
    // Input mode
    private boolean isTyping = false;
    private int typingField = -1;
    
    public NewGameUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    public void draw(Graphics2D g2) {
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        // Draw main background panel
        int panelWidth = 500;
        int panelHeight = 450;
        int panelX = (screenWidth - panelWidth) / 2;
        int panelY = (screenHeight - panelHeight) / 2;
        
        g2.setColor(new Color(139, 69, 19, 220)); // Brown background
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);
        
        // Draw title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        String title = "Create New Farm";
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2.drawString(title, panelX + (panelWidth - titleWidth) / 2, panelY + 40);
        
        // Field positions
        int fieldStartY = panelY + 80;
        int fieldSpacing = 70;
        
        // Draw Player Name field
        drawInputField(g2, panelX + 50, fieldStartY, "Player Name:", playerName, 0);
        
        // Draw Farm Name field
        drawInputField(g2, panelX + 50, fieldStartY + fieldSpacing, "Farm Name:", farmName, 1);
        
        // Draw Gender selection
        drawGenderField(g2, panelX + 50, fieldStartY + fieldSpacing * 2, 2);
        
        // Draw Favorite Item selection
        drawFavoriteItemField(g2, panelX + 50, fieldStartY + fieldSpacing * 3, 3);
          // Draw action buttons
        int buttonY = fieldStartY + fieldSpacing * 4;
        drawButton(g2, panelX + 160, buttonY, "START GAME", 4); // Center the button
        
        // Draw instructions
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        String instruction = isTyping ? "Type your input, press ENTER to confirm, ESC to cancel" 
                                    : "Use ARROW KEYS to navigate, ENTER to select/edit, ESC to go back";
        int instrWidth = g2.getFontMetrics().stringWidth(instruction);
        g2.drawString(instruction, (screenWidth - instrWidth) / 2, screenHeight - 30);
    }
    
    private void drawInputField(Graphics2D g2, int x, int y, String label, String value, int fieldIndex) {
        // Draw label
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString(label, x, y);
        
        // Draw input field
        int fieldY = y + 3;
        Color fieldColor = (selectedField == fieldIndex) ? 
            new Color(255, 215, 0, 100) : new Color(255, 255, 255, 50);
        g2.setColor(fieldColor);
        g2.fillRoundRect(x + 120, fieldY, FIELD_WIDTH, FIELD_HEIGHT, 5, 5);
        
        // Draw field border
        g2.setColor((selectedField == fieldIndex) ? Color.YELLOW : Color.WHITE);
        g2.drawRoundRect(x + 120, fieldY, FIELD_WIDTH, FIELD_HEIGHT, 5, 5);
        
        // Draw value text
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String displayValue = value.isEmpty() ? "Enter " + label.toLowerCase().replace(":", "") : value;
        if (value.isEmpty()) {
            g2.setColor(Color.GRAY);
        }
        g2.drawString(displayValue, x + 130, fieldY + 25);
        
        // Draw cursor if typing
        if (isTyping && typingField == fieldIndex) {
            int cursorX = x + 130 + g2.getFontMetrics().stringWidth(value);
            g2.setColor(Color.BLACK);
            g2.drawLine(cursorX, fieldY + 8, cursorX, fieldY + 32);
        }
    }
    
    private void drawGenderField(Graphics2D g2, int x, int y, int fieldIndex) {
        // Draw label
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Gender:", x, y);
        
        // Draw gender options
        int optionX = x + 120;
        for (int i = 0; i < genderOptions.length; i++) {
            boolean isSelectedOption = (selectedGenderIndex == i);
            boolean isSelectedField = (selectedField == fieldIndex);
            
            Color bgColor;
            if (isSelectedField && isSelectedOption) {
                bgColor = new Color(255, 215, 0, 150); // Golden
            } else if (isSelectedOption) {
                bgColor = new Color(0, 255, 0, 100); // Green for selected gender
            } else {
                bgColor = new Color(255, 255, 255, 50); // Default
            }
            
            g2.setColor(bgColor);
            g2.fillRoundRect(optionX, y + 5, 80, 30, 5, 5);
            
            // Draw border
            g2.setColor(isSelectedField && isSelectedOption ? Color.YELLOW : Color.WHITE);
            g2.drawRoundRect(optionX, y + 5, 80, 30, 5, 5);
            
            // Draw text
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String text = genderOptions[i];
            int textWidth = g2.getFontMetrics().stringWidth(text);
            g2.drawString(text, optionX + (80 - textWidth) / 2, y + 23);
            
            optionX += 100;
        }
    }
    
    private void drawFavoriteItemField(Graphics2D g2, int x, int y, int fieldIndex) {
        // Draw label
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Favorite Thing:", x, y);
        
        // Draw selection field
        int fieldY = y + 5;
        Color fieldColor = (selectedField == fieldIndex) ? 
            new Color(255, 215, 0, 100) : new Color(255, 255, 255, 50);
        g2.setColor(fieldColor);
        g2.fillRoundRect(x + 120, fieldY, FIELD_WIDTH, FIELD_HEIGHT, 5, 5);
        
        // Draw field border
        g2.setColor((selectedField == fieldIndex) ? Color.YELLOW : Color.WHITE);
        g2.drawRoundRect(x + 120, fieldY, FIELD_WIDTH, FIELD_HEIGHT, 5, 5);
        
        // Draw current selection
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String currentItem = favoriteItems[selectedFavoriteIndex];
        g2.drawString(currentItem, x + 130, fieldY + 25);
        
        // Draw navigation arrows if selected
        if (selectedField == fieldIndex) {
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("<", x + 100, fieldY + 25);
            g2.drawString(">", x + 430, fieldY + 25);
        }
    }
    
    private void drawButton(Graphics2D g2, int x, int y, String text, int buttonIndex) {
        // Draw button background
        Color bgColor = (selectedField == buttonIndex) ? 
            new Color(255, 215, 0, 150) : new Color(100, 100, 100, 100);
        g2.setColor(bgColor);
        g2.fillRoundRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, 10, 10);
        
        // Draw button border
        g2.setColor((selectedField == buttonIndex) ? Color.YELLOW : Color.WHITE);
        g2.drawRoundRect(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, 10, 10);
        
        // Draw button text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textX = x + (BUTTON_WIDTH - textWidth) / 2;
        int textY = y + (BUTTON_HEIGHT + fm.getAscent()) / 2 - 2;
        g2.drawString(text, textX, textY);
    }
    
    // Input handling methods
    public void moveSelectionUp() {
        if (!isTyping) {
            selectedField = Math.max(0, selectedField - 1);
        }
    }
    
    public void moveSelectionDown() {
        if (!isTyping) {
            selectedField = Math.min(TOTAL_FIELDS - 1, selectedField + 1);
        }
    }
    
    public void moveSelectionLeft() {
        if (!isTyping) {
            if (selectedField == 2) { // Gender field
                selectedGenderIndex = Math.max(0, selectedGenderIndex - 1);
                gender = genderOptions[selectedGenderIndex];
            } else if (selectedField == 3) { // Favorite item field
                selectedFavoriteIndex = Math.max(0, selectedFavoriteIndex - 1);
                favoriteItem = favoriteItems[selectedFavoriteIndex];
            }
        }
    }
    
    public void moveSelectionRight() {
        if (!isTyping) {
            if (selectedField == 2) { // Gender field
                selectedGenderIndex = Math.min(genderOptions.length - 1, selectedGenderIndex + 1);
                gender = genderOptions[selectedGenderIndex];
            } else if (selectedField == 3) { // Favorite item field
                selectedFavoriteIndex = Math.min(favoriteItems.length - 1, selectedFavoriteIndex + 1);
                favoriteItem = favoriteItems[selectedFavoriteIndex];
            }
        }
    }
    
    public void handleEnterPress() {
        if (isTyping) {
            // Confirm typing
            isTyping = false;
            typingField = -1;
        } else {
            switch (selectedField) {
                case 0: // Player name
                    startTyping(0);
                    break;
                case 1: // Farm name
                    startTyping(1);
                    break;
                case 2: // Gender - just toggle
                    selectedGenderIndex = (selectedGenderIndex + 1) % genderOptions.length;
                    gender = genderOptions[selectedGenderIndex];
                    break;
                case 3: // Favorite item - cycle through
                    selectedFavoriteIndex = (selectedFavoriteIndex + 1) % favoriteItems.length;
                    favoriteItem = favoriteItems[selectedFavoriteIndex];
                    break;                case 4: // Start game
                    startGame();
                    break;
            }
        }
    }
    
    public void handleEscapePress() {
        if (isTyping) {
            // Cancel typing
            isTyping = false;
            typingField = -1;
        } else {
            goBack();
        }
    }
    
    public void handleCharInput(char c) {
        if (isTyping) {
            if (Character.isLetterOrDigit(c) || Character.isSpaceChar(c)) {
                if (typingField == 0) { // Player name
                    if (playerName.length() < 20) {
                        playerName += c;
                    }
                } else if (typingField == 1) { // Farm name
                    if (farmName.length() < 20) {
                        farmName += c;
                    }
                }
            }
        }
    }
    
    public void handleBackspace() {
        if (isTyping) {
            if (typingField == 0 && !playerName.isEmpty()) {
                playerName = playerName.substring(0, playerName.length() - 1);
            } else if (typingField == 1 && !farmName.isEmpty()) {
                farmName = farmName.substring(0, farmName.length() - 1);
            }
        }
    }
    
    private void startTyping(int field) {
        isTyping = true;
        typingField = field;
    }
    
    private void startGame() {
        // Validate inputs
        if (playerName.trim().isEmpty()) {
            playerName = "Farmer";
        }
        if (farmName.trim().isEmpty()) {
            farmName = playerName + "'s Farm";
        }
        
        // Set player data and start game
        gamePanel.getPlayer().setPlayerName(playerName);
        gamePanel.getPlayer().setFarmName(farmName);
        gamePanel.getPlayer().setGender(gender);
        gamePanel.getPlayer().setFavoriteItem(favoriteItem);
        
        // Reset to initial state and start game
        resetFields();
        gamePanel.setGameState(GamePanel.PLAY_STATE);
        
        System.out.println("Starting new game:");
        System.out.println("Player: " + playerName);
        System.out.println("Farm: " + farmName);
        System.out.println("Gender: " + gender);
        System.out.println("Favorite: " + favoriteItem);
    }
    
    private void goBack() {
        resetFields();
        gamePanel.setGameState(GamePanel.MAIN_MENU_STATE);
    }
    
    private void resetFields() {
        playerName = "";
        farmName = "";
        gender = "BOY";
        favoriteItem = favoriteItems[0];
        selectedField = 0;
        selectedGenderIndex = 0;
        selectedFavoriteIndex = 0;
        isTyping = false;
        typingField = -1;
    }
    
    // Getters
    public boolean isTyping() {
        return isTyping;
    }
    
    /**
     * Handle keyboard input for new game UI navigation
     * @param e KeyEvent from KeyHandler
     */
    public void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (isTyping) {
            // Handle text input mode
            switch (keyCode) {
                case KeyEvent.VK_ENTER:
                    handleEnterPress();
                    break;
                case KeyEvent.VK_ESCAPE:
                    handleEscapePress();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    handleBackspace();
                    break;
                default:
                    // Handle character input
                    char keyChar = e.getKeyChar();
                    if (keyChar != KeyEvent.CHAR_UNDEFINED) {
                        handleCharInput(keyChar);
                    }
                    break;
            }
        } else {
            // Handle navigation mode
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    moveSelectionUp();
                    break;
                case KeyEvent.VK_DOWN:
                    moveSelectionDown();
                    break;
                case KeyEvent.VK_LEFT:
                    moveSelectionLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    moveSelectionRight();
                    break;
                case KeyEvent.VK_ENTER:
                    handleEnterPress();
                    break;
                case KeyEvent.VK_ESCAPE:
                    handleEscapePress();
                    break;
            }
        }
    }
}
