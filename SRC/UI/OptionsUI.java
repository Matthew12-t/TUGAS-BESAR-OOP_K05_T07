package SRC.UI;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import SRC.MAIN.GamePanel;

/**
 * OptionsUI handles the in-game options menu accessible via ESC
 * Provides resume/save/exit options during gameplay
 */
public class OptionsUI {
    private GamePanel gamePanel;
      // Menu options
    private final String[] options = {"RESUME", "SAVE GAME", "PLAYER STATISTICS", "EXIT", "EXIT TO MAIN MENU"};
    private int selectedOption = 0;
    
    // UI properties
    private final int PANEL_WIDTH = 300;
    private final int PANEL_HEIGHT = 400; // Increased from 350 to 450 to fit all buttons
    private final int BUTTON_HEIGHT = 50;
    private final int BUTTON_SPACING = 10;
    
    // Save functionality
    private boolean saveInProgress = false;
    private String saveMessage = "";
    private long saveMessageTimer = 0;
    
    public OptionsUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    public void draw(Graphics2D g2) {
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        // Draw main panel
        int panelX = (screenWidth - PANEL_WIDTH) / 2;
        int panelY = (screenHeight - PANEL_HEIGHT) / 2;
        
        g2.setColor(new Color(139, 69, 19, 230)); // Brown background
        g2.fillRoundRect(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, 20, 20);
        
        // Draw title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String title = "Game Menu";
        FontMetrics fm = g2.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2.drawString(title, panelX + (PANEL_WIDTH - titleWidth) / 2, panelY + 35);
        
        // Draw options
        drawOptions(g2, panelX, panelY);
        
        // Draw save message if active
        if (saveInProgress || (System.currentTimeMillis() - saveMessageTimer < 2000)) {
            drawSaveMessage(g2, panelX, panelY);
        }
        
        // Draw instructions
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        String instruction = "Use ARROW KEYS to navigate, ENTER to select, ESC to resume";
        int instrWidth = g2.getFontMetrics().stringWidth(instruction);
        g2.drawString(instruction, (screenWidth - instrWidth) / 2, screenHeight - 30);
    }
    
    private void drawOptions(Graphics2D g2, int panelX, int panelY) {
        int optionsStartY = panelY + 70;
        
        for (int i = 0; i < options.length; i++) {
            int optionY = optionsStartY + i * (BUTTON_HEIGHT + BUTTON_SPACING);
            boolean isSelected = (i == selectedOption);
            
            // Draw button background
            Color bgColor = isSelected ? 
                new Color(255, 215, 0, 150) : new Color(100, 100, 100, 100);
            g2.setColor(bgColor);
            g2.fillRoundRect(panelX + 20, optionY, PANEL_WIDTH - 40, BUTTON_HEIGHT, 10, 10);
            
            // Draw button border
            g2.setColor(isSelected ? Color.YELLOW : Color.WHITE);
            g2.drawRoundRect(panelX + 20, optionY, PANEL_WIDTH - 40, BUTTON_HEIGHT, 10, 10);
            
            // Draw button text
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            String text = options[i];
            int textWidth = fm.stringWidth(text);
            int textX = panelX + (PANEL_WIDTH - textWidth) / 2;
            int textY = optionY + (BUTTON_HEIGHT + fm.getAscent()) / 2 - 2;
            g2.drawString(text, textX, textY);
        }
    }
    
    private void drawSaveMessage(Graphics2D g2, int panelX, int panelY) {
        // Draw message background
        int messageY = panelY + PANEL_HEIGHT + 10;
        int messageWidth = PANEL_WIDTH;
        int messageHeight = 40;
        
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(panelX, messageY, messageWidth, messageHeight, 10, 10);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(panelX, messageY, messageWidth, messageHeight, 10, 10);
        
        // Draw message text
        g2.setColor(saveInProgress ? Color.YELLOW : Color.GREEN);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(saveMessage);
        int textX = panelX + (messageWidth - textWidth) / 2;
        int textY = messageY + (messageHeight + fm.getAscent()) / 2 - 2;
        g2.drawString(saveMessage, textX, textY);
    }
    
    // Input handling methods
    public void moveSelectionUp() {
        selectedOption = Math.max(0, selectedOption - 1);
    }
    
    public void moveSelectionDown() {
        selectedOption = Math.min(options.length - 1, selectedOption + 1);
    }
      public void handleEnterPress() {
        switch (selectedOption) {
            case 0: // Resume
                resumeGame();
                break;
            case 1: // Save Game
                saveGame();
                break;
            case 2: // Player Statistics
                showPlayerStatistics();
                break;
            case 3: // Exit (close game)
                exitGame();
                break;
            case 4: // Exit to Main Menu
                exitToMainMenu();
                break;
        }
    }
    
    public void handleEscapePress() {
        resumeGame();
    }
      private void resumeGame() {
        gamePanel.setGameState(GamePanel.PLAY_STATE);
    }
    
    private void showPlayerStatistics() {
        gamePanel.setGameState(GamePanel.PLAYER_STATISTICS_STATE);
        gamePanel.getPlayerStatisticsUI().showStatistics();
    }
    
    private void exitGame() {
        System.exit(0);
    }
    
    private void saveGame() {
        if (saveInProgress) {
            return; // Already saving
        }
        
        saveInProgress = true;
        saveMessage = "Saving game...";
        
        // Perform save operation in a separate thread to avoid blocking UI
        new Thread(() -> {
            try {
                boolean success = performSave();
                
                // Update UI on main thread
                javax.swing.SwingUtilities.invokeLater(() -> {
                    saveInProgress = false;
                    if (success) {
                        saveMessage = "Game saved successfully!";
                    } else {
                        saveMessage = "Save failed!";
                    }
                    saveMessageTimer = System.currentTimeMillis();
                });
                
            } catch (Exception e) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    saveInProgress = false;
                    saveMessage = "Save error: " + e.getMessage();
                    saveMessageTimer = System.currentTimeMillis();
                });
            }
        }).start();
    }
    
    private boolean performSave() {
        try {
            // Create saves directory if it doesn't exist
            File savesDir = new File("saves");
            if (!savesDir.exists()) {
                savesDir.mkdirs();
            }
            
            // Generate save filename based on farm name and current date
            String farmName = gamePanel.getPlayer().getFarmName();
            if (farmName == null || farmName.isEmpty()) {
                farmName = "Farm";
            }
            
            // Clean farm name for filename
            String cleanFarmName = farmName.replaceAll("[^a-zA-Z0-9]", "_");
            String timestamp = String.valueOf(System.currentTimeMillis());
            String filename = "saves/" + cleanFarmName + "_" + timestamp + ".save";
            
            // Create save file
            File saveFile = new File(filename);
            try (FileWriter writer = new FileWriter(saveFile)) {
                // Write save data
                writeSaveData(writer);
            }
            
            System.out.println("Game saved to: " + filename);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void writeSaveData(FileWriter writer) throws IOException {
        // Write player data
        writer.write("# Spakbor Hills Save File\n");
        writer.write("version=1.0\n");
        writer.write("saveDate=" + new java.util.Date().toString() + "\n");
        writer.write("\n[PLAYER]\n");
        writer.write("name=" + gamePanel.getPlayer().getPlayerName() + "\n");
        writer.write("farmName=" + gamePanel.getPlayer().getFarmName() + "\n");
        writer.write("gender=" + gamePanel.getPlayer().getGender() + "\n");
        writer.write("favoriteItem=" + gamePanel.getPlayer().getFavoriteItem() + "\n");
        writer.write("gold=" + gamePanel.getPlayer().getGold() + "\n");
        writer.write("energy=" + gamePanel.getPlayer().getEnergy() + "\n");
        writer.write("worldX=" + gamePanel.getPlayer().getWorldX() + "\n");
        writer.write("worldY=" + gamePanel.getPlayer().getWorldY() + "\n");
        writer.write("married=" + gamePanel.getPlayer().isMarried() + "\n");
          // Write game time data
        writer.write("\n[TIME]\n");
        writer.write("season=" + gamePanel.getSeason().name() + "\n");
        writer.write("weather=" + gamePanel.getWeather().name() + "\n");
        // TODO: Add time data when available
        // writer.write("day=" + gamePanel.getClockUI().getDay() + "\n");
        // writer.write("month=" + gamePanel.getClockUI().getMonth() + "\n");
        // writer.write("hour=" + gamePanel.getClockUI().getHour() + "\n");
        // writer.write("minute=" + gamePanel.getClockUI().getMinute() + "\n");
        
        // Write current map
        writer.write("\n[WORLD]\n");
        writer.write("currentMap=" + gamePanel.getCurrentMap().getMapName() + "\n");
        
        // Write statistics
        writer.write("\n[STATS]\n");
        writer.write("daysPlayed=" + gamePanel.getPlayer().getDaysPlayed() + "\n");
        writer.write("totalIncome=" + gamePanel.getPlayer().getTotalIncome() + "\n");
        writer.write("totalExpenditure=" + gamePanel.getPlayer().getTotalExpenditure() + "\n");
        writer.write("totalCropsHarvested=" + gamePanel.getPlayer().getTotalCropsHarvested() + "\n");
        writer.write("totalFishCaught=" + gamePanel.getPlayer().getTotalFishCaught() + "\n");
        
        // TODO: Write inventory data, farm objects, NPC relationships, etc.
        writer.write("\n[INVENTORY]\n");
        // This would require accessing the player's inventory and serializing items
        
        writer.write("\n# End of save file\n");
    }
    
    private void exitToMainMenu() {
        // Reset game state and return to main menu
        gamePanel.setGameState(GamePanel.MAIN_MENU_STATE);
        selectedOption = 0; // Reset selection for next time
        
        // Optionally, you might want to reset some game state here
        // or prompt for save before exiting
        System.out.println("Exited to main menu");
    }
    
    // Getters
    public int getSelectedOption() {
        return selectedOption;
    }
    
    public boolean isSaveInProgress() {
        return saveInProgress;
    }

    /**
     * Handle keyboard input for options UI navigation
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
            case KeyEvent.VK_ENTER:
                handleEnterPress();
                break;
            case KeyEvent.VK_ESCAPE:
                handleEscapePress();
                break;
        }
    }
}
