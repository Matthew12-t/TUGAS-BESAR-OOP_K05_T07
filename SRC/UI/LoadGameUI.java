package SRC.UI;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import SRC.MAIN.GamePanel;

/**
 * LoadGameUI handles the load game interface
 * Shows available saved farms and allows selection
 */
public class LoadGameUI {
    private GamePanel gamePanel;
    
    // Save game data structure
    public static class SaveGameData {
        public String playerName;
        public String farmName;
        public String gender;
        public String favoriteItem;
        public String saveDate;
        public int daysPlayed;
        public int gold;
        public String fileName;
        
        public SaveGameData(String fileName) {
            this.fileName = fileName;
            // Parse save file or set defaults
            this.playerName = "Unknown Farmer";
            this.farmName = "Unknown Farm";
            this.gender = "BOY";
            this.favoriteItem = "Parsnip";
            this.saveDate = "Unknown";
            this.daysPlayed = 0;
            this.gold = 500;
        }
    }
      private List<SaveGameData> saveFiles;
    private int selectedSaveIndex = 0;    private int selectedButton = 0; // 0=load, 1=delete
    
    // UI properties
    private final int PANEL_WIDTH = 600;
    private final int PANEL_HEIGHT = 500;
    private final int SAVE_SLOT_HEIGHT = 80;
    private final int MAX_VISIBLE_SAVES = 5;
    private int scrollOffset = 0;
    
    public LoadGameUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.saveFiles = new ArrayList<>();
        loadSaveFiles();
    }
    
    private void loadSaveFiles() {
        saveFiles.clear();
        
        // Create saves directory if it doesn't exist
        File savesDir = new File("saves");
        if (!savesDir.exists()) {
            savesDir.mkdirs();
        }
        
        // Load save files
        File[] files = savesDir.listFiles((dir, name) -> name.endsWith(".save"));
        if (files != null) {
            for (File file : files) {
                SaveGameData saveData = new SaveGameData(file.getName());
                // TODO: Parse actual save file data
                loadSaveFileData(saveData, file);
                saveFiles.add(saveData);
            }
        }
        
        // Add some example saves if no saves exist (for testing)
        if (saveFiles.isEmpty()) {
            SaveGameData example1 = new SaveGameData("farm1.save");
            example1.playerName = "John";
            example1.farmName = "Sunset Farm";
            example1.gender = "BOY";
            example1.daysPlayed = 15;
            example1.gold = 2500;
            example1.saveDate = "2024-01-15";
            saveFiles.add(example1);
            
            SaveGameData example2 = new SaveGameData("farm2.save");
            example2.playerName = "Emily";
            example2.farmName = "Moonlight Ranch";
            example2.gender = "GIRL";
            example2.daysPlayed = 8;
            example2.gold = 1200;
            example2.saveDate = "2024-01-10";
            saveFiles.add(example2);
        }
    }
      private void loadSaveFileData(SaveGameData saveData, File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentSection = "";
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Check for section headers
                if (line.startsWith("[") && line.endsWith("]")) {
                    currentSection = line.substring(1, line.length() - 1);
                    continue;
                }
                
                // Parse key-value pairs
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        
                        // Parse save file metadata
                        if (key.equals("saveDate")) {
                            saveData.saveDate = value;
                        }
                        
                        // Parse player data
                        if (currentSection.equals("PLAYER")) {
                            switch (key) {
                                case "name":
                                    saveData.playerName = value;
                                    break;
                                case "farmName":
                                    saveData.farmName = value;
                                    break;
                                case "gender":
                                    saveData.gender = value;
                                    break;
                                case "favoriteItem":
                                    saveData.favoriteItem = value;
                                    break;
                                case "gold":
                                    try {
                                        saveData.gold = Integer.parseInt(value);
                                    } catch (NumberFormatException e) {
                                        saveData.gold = 500; // Default value
                                    }
                                    break;
                            }
                        }
                        
                        // Parse stats data
                        if (currentSection.equals("STATS")) {
                            if (key.equals("daysPlayed")) {
                                try {
                                    saveData.daysPlayed = Integer.parseInt(value);
                                } catch (NumberFormatException e) {
                                    saveData.daysPlayed = 0; // Default value
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading save file: " + file.getName());
            e.printStackTrace();
            // Set default values if file can't be read
            saveData.saveDate = "Unknown";
            saveData.playerName = "Unknown Farmer";
            saveData.farmName = "Unknown Farm";
            saveData.gender = "BOY";
            saveData.favoriteItem = "Parsnip";
            saveData.daysPlayed = 0;
            saveData.gold = 500;
        }
    }
    
    public void draw(Graphics2D g2) {
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        // Draw main panel
        int panelX = (screenWidth - PANEL_WIDTH) / 2;
        int panelY = (screenHeight - PANEL_HEIGHT) / 2;
        
        g2.setColor(new Color(139, 69, 19, 220)); // Brown background
        g2.fillRoundRect(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 20, 20);
        
        // Draw title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        String title = "Load Game";
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2.drawString(title, panelX + (PANEL_WIDTH - titleWidth) / 2, panelY + 40);
        
        // Draw save slots
        if (saveFiles.isEmpty()) {
            drawNoSavesMessage(g2, panelX, panelY);
        } else {
            drawSaveSlots(g2, panelX, panelY);
        }
        
        // Draw action buttons
        drawActionButtons(g2, panelX, panelY);
        
        // Draw instructions
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        String instruction = "Use ARROW KEYS to navigate, ENTER to select, ESC to go back";
        int instrWidth = g2.getFontMetrics().stringWidth(instruction);
        g2.drawString(instruction, (screenWidth - instrWidth) / 2, screenHeight - 30);
    }
    
    private void drawNoSavesMessage(Graphics2D g2, int panelX, int panelY) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        String message = "No saved games found";
        FontMetrics fm = g2.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        g2.drawString(message, panelX + (PANEL_WIDTH - messageWidth) / 2, panelY + 200);
        
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String hint = "Create a new game to start playing!";
        int hintWidth = g2.getFontMetrics().stringWidth(hint);
        g2.drawString(hint, panelX + (PANEL_WIDTH - hintWidth) / 2, panelY + 230);
    }
    
    private void drawSaveSlots(Graphics2D g2, int panelX, int panelY) {
        int slotsStartY = panelY + 70;
        int slotsEndY = slotsStartY + (MAX_VISIBLE_SAVES * SAVE_SLOT_HEIGHT);
        
        // Calculate which saves to show based on scroll offset
        int startIndex = scrollOffset;
        int endIndex = Math.min(startIndex + MAX_VISIBLE_SAVES, saveFiles.size());
        
        for (int i = startIndex; i < endIndex; i++) {
            SaveGameData saveData = saveFiles.get(i);
            int slotY = slotsStartY + ((i - startIndex) * SAVE_SLOT_HEIGHT);
            
            // Determine if this slot is selected
            boolean isSelected = (i == selectedSaveIndex && selectedButton == 0);
            
            // Draw slot background
            Color slotColor = isSelected ? 
                new Color(255, 215, 0, 100) : new Color(255, 255, 255, 30);
            g2.setColor(slotColor);
            g2.fillRoundRect(panelX + 20, slotY, PANEL_WIDTH - 40, SAVE_SLOT_HEIGHT - 10, 10, 10);
            
            // Draw slot border
            g2.setColor(isSelected ? Color.YELLOW : Color.WHITE);
            g2.drawRoundRect(panelX + 20, slotY, PANEL_WIDTH - 40, SAVE_SLOT_HEIGHT - 10, 10, 10);
            
            // Draw save data
            drawSaveSlotData(g2, saveData, panelX + 30, slotY + 15);
        }
        
        // Draw scroll indicators if needed
        if (saveFiles.size() > MAX_VISIBLE_SAVES) {
            drawScrollIndicators(g2, panelX, slotsStartY, slotsEndY);
        }
    }
    
    private void drawSaveSlotData(Graphics2D g2, SaveGameData saveData, int x, int y) {
        g2.setColor(Color.WHITE);
        
        // Draw farm name (title)
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString(saveData.farmName, x, y + 15);
        
        // Draw player info
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("Player: " + saveData.playerName + " (" + saveData.gender + ")", x, y + 35);
        
        // Draw stats
        g2.drawString("Days: " + saveData.daysPlayed + " | Gold: " + saveData.gold + "g", x, y + 50);
        
        // Draw save date on the right
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        String dateText = "Saved: " + saveData.saveDate;
        int dateWidth = g2.getFontMetrics().stringWidth(dateText);
        g2.drawString(dateText, x + PANEL_WIDTH - 80 - dateWidth, y + 15);
    }
    
    private void drawScrollIndicators(Graphics2D g2, int panelX, int startY, int endY) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        
        if (scrollOffset > 0) {
            g2.drawString("▲ More above", panelX + PANEL_WIDTH - 100, startY - 5);
        }
        
        if (scrollOffset + MAX_VISIBLE_SAVES < saveFiles.size()) {
            g2.drawString("▼ More below", panelX + PANEL_WIDTH - 100, endY + 15);
        }
    }
      private void drawActionButtons(Graphics2D g2, int panelX, int panelY) {
        int buttonY = panelY + PANEL_HEIGHT - 80;
        int buttonWidth = 100;
        int buttonHeight = 35;
        int buttonSpacing = 20;
        
        String[] buttonTexts = {"LOAD", "DELETE"};
        int totalButtonsWidth = (buttonTexts.length * buttonWidth) + ((buttonTexts.length - 1) * buttonSpacing);
        int startX = panelX + (PANEL_WIDTH - totalButtonsWidth) / 2;
        
        for (int i = 0; i < buttonTexts.length; i++) {
            int buttonX = startX + i * (buttonWidth + buttonSpacing);
            boolean isSelected = (selectedButton == i + 1); // +1 because button 0 is save selection
            
            // Don't allow load/delete if no saves available
            boolean isEnabled = !(saveFiles.isEmpty() && (i == 0 || i == 1));
            
            // Draw button background
            Color bgColor;
            if (!isEnabled) {
                bgColor = new Color(100, 100, 100, 100); // Disabled
            } else if (isSelected) {
                bgColor = new Color(255, 215, 0, 150); // Selected
            } else {
                bgColor = new Color(100, 100, 100, 100); // Normal
            }
            
            g2.setColor(bgColor);
            g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 10, 10);
            
            // Draw button border
            g2.setColor(isSelected ? Color.YELLOW : Color.WHITE);
            g2.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 10, 10);
            
            // Draw button text
            g2.setColor(isEnabled ? Color.WHITE : Color.GRAY);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            String text = buttonTexts[i];
            int textWidth = fm.stringWidth(text);
            int textX = buttonX + (buttonWidth - textWidth) / 2;
            int textY = buttonY + (buttonHeight + fm.getAscent()) / 2 - 2;
            g2.drawString(text, textX, textY);
        }
    }
    
    /**
     * Handle keyboard input for load game UI navigation
     * @param e KeyEvent from KeyHandler
     */
    public void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
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
                goBack();
                break;
            case KeyEvent.VK_DELETE:
                if (selectedButton == 0 && !saveFiles.isEmpty()) {
                    deleteSelectedGame();
                }
                break;
        }
    }
    
    private void moveSelectionUp() {
        if (selectedButton == 0) {
            // Navigate through save files
            if (!saveFiles.isEmpty()) {
                selectedSaveIndex = Math.max(0, selectedSaveIndex - 1);
                adjustScrollOffset();
            }
        } else {
            // Move to save files from buttons
            selectedButton = 0;
            if (saveFiles.isEmpty()) {
                selectedSaveIndex = 0;
            }
        }
    }
      private void moveSelectionDown() {
        if (selectedButton == 0) {
            if (!saveFiles.isEmpty()) {
                if (selectedSaveIndex < saveFiles.size() - 1) {
                    selectedSaveIndex++;
                    adjustScrollOffset();
                } else {
                    // Move to buttons
                    selectedButton = 1; // Load button
                }
            } else {
                // No saves, move to buttons  
                selectedButton = 1; // Load button
            }
        }
    }
    
    private void moveSelectionLeft() {
        if (selectedButton > 0) {
            selectedButton = Math.max(1, selectedButton - 1);
        }
    }
      private void moveSelectionRight() {
        if (selectedButton > 0) {
            selectedButton = Math.min(2, selectedButton + 1);
        }
    }
      private void handleEnterPress() {
        if (selectedButton == 0) {
            // Load selected game
            loadSelectedGame();
        } else if (selectedButton == 1) {
            // Load button
            loadSelectedGame();
        } else if (selectedButton == 2) {
            // Delete button (only if save is selected)
            if (!saveFiles.isEmpty()) {
                deleteSelectedGame();
            }
        }
    }
      // Input handling methods (removed duplicates)
    
    private void adjustScrollOffset() {
        if (selectedSaveIndex < scrollOffset) {
            scrollOffset = selectedSaveIndex;
        } else if (selectedSaveIndex >= scrollOffset + MAX_VISIBLE_SAVES) {
            scrollOffset = selectedSaveIndex - MAX_VISIBLE_SAVES + 1;
        }
    }
    
    private void loadSelectedGame() {
        if (saveFiles.isEmpty() || selectedSaveIndex >= saveFiles.size()) {
            return;
        }
        
        SaveGameData saveData = saveFiles.get(selectedSaveIndex);
        
        // Load the save data into the player
        gamePanel.getPlayer().setPlayerName(saveData.playerName);
        gamePanel.getPlayer().setFarmName(saveData.farmName);
        gamePanel.getPlayer().setGender(saveData.gender);
        gamePanel.getPlayer().setFavoriteItem(saveData.favoriteItem);
        gamePanel.getPlayer().setGold(saveData.gold);
        
        // TODO: Load additional game state from save file
        
        // Start the game
        gamePanel.setGameState(GamePanel.PLAY_STATE);
        
        System.out.println("Loaded game: " + saveData.farmName + " (" + saveData.playerName + ")");
    }
    
    private void deleteSelectedGame() {
        if (saveFiles.isEmpty() || selectedSaveIndex >= saveFiles.size()) {
            return;
        }
        
        SaveGameData saveData = saveFiles.get(selectedSaveIndex);
        
        // Delete the save file
        File saveFile = new File("saves/" + saveData.fileName);
        if (saveFile.exists()) {
            saveFile.delete();
        }
        
        // Remove from list
        saveFiles.remove(selectedSaveIndex);
        
        // Adjust selection
        if (selectedSaveIndex >= saveFiles.size() && !saveFiles.isEmpty()) {
            selectedSaveIndex = saveFiles.size() - 1;
        }
        
        adjustScrollOffset();
        
        System.out.println("Deleted save: " + saveData.farmName);
    }
    
    private void goBack() {
        gamePanel.setGameState(GamePanel.MAIN_MENU_STATE);
    }
    
    public void refreshSaveFiles() {
        loadSaveFiles();
        selectedSaveIndex = 0;
        selectedButton = 0;
        scrollOffset = 0;
    }
}
