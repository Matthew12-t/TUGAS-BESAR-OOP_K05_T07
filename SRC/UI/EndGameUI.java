package SRC.UI;

import SRC.MAIN.GamePanel;
import SRC.ENDGAME.EndGame;
import SRC.ENDGAME.EndGame.NPCRelationshipData;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * EndGameUI handles the display of milestone achievement screens
 * Shows comprehensive statistics when milestones are reached as a popup window
 */
public class EndGameUI {
    
    private GamePanel gamePanel;
    private EndGame endGame;
    private boolean isDisplaying;
    private BufferedImage backgroundImage;
    private long displayStartTime;
      // Popup dimensions and position
    private static final int POPUP_WIDTH = 700;
    private static final int POPUP_HEIGHT = 600;
    private int popupX;
    private int popupY;
    
    // UI styling
    private static final Color POPUP_BACKGROUND = new Color(40, 40, 60, 240);
    private static final Color POPUP_BORDER = new Color(255, 215, 0, 255); // Gold border
    private static final Color TITLE_BACKGROUND = new Color(50, 50, 80, 255);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 215, 0); // Gold color
    private static final Color SUCCESS_COLOR = new Color(50, 205, 50); // Lime green
    private static final Color STAT_COLOR = new Color(200, 200, 255); // Light blue
    private static final Color SECTION_COLOR = new Color(180, 180, 200); // Light gray
    
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font SECTION_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font STAT_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Font HINT_FONT = new Font("Arial", Font.ITALIC, 12);
    
    // Scroll offset for NPC section
    private int npcScrollOffset = 0;
    private static final int MAX_VISIBLE_NPCS = 8;
      public EndGameUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.isDisplaying = false;
        
        // Calculate popup position (centered)
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        this.popupX = (screenWidth - POPUP_WIDTH) / 2;
        this.popupY = (screenHeight - POPUP_HEIGHT) / 2;
        
        loadBackgroundImage();
    }
    
    /**
     * Show end game milestone screen
     */
    public void showEndGameScreen(EndGame endGame) {
        this.endGame = endGame;
        this.isDisplaying = true;
        this.displayStartTime = System.currentTimeMillis();
        
        System.out.println("Displaying EndGame screen: " + endGame.getMilestoneReached());
    }
    
    /**
     * Load background image for end game screen
     */
    private void loadBackgroundImage() {
        try {
            // Try to load a celebration background, fallback to sleep background
            File imageFile = new File("RES/SLEEP/sleeping_Background.png");
            if (imageFile.exists()) {
                backgroundImage = ImageIO.read(imageFile);
                System.out.println("DEBUG: EndGame background image loaded");
            } 
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load EndGame background image: " + e.getMessage());
            backgroundImage = null;
        }
    }    /**
     * Update the EndGame UI
     */
    public void update() {
        // EndGame screen doesn't need update logic, handled by KeyHandler calling handleEnterPress()
    }
    
    /**
     * Handle Enter key press to close EndGame screen
     */
    public void handleEnterPress() {
        if (isDisplaying) {
            isDisplaying = false;
            // Resume normal game flow
            gamePanel.setGameState(GamePanel.PLAY_STATE);
            System.out.println("EndGame screen closed by Enter key");
        }
    }
      /**
     * Draw the EndGame popup window
     */
    public void draw(Graphics2D g2) {
        if (!isDisplaying || endGame == null) {
            return;
        }
        
        // Draw semi-transparent overlay
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());
        
        // Draw popup window background
        g2.setColor(POPUP_BACKGROUND);
        g2.fillRoundRect(popupX, popupY, POPUP_WIDTH, POPUP_HEIGHT, 20, 20);
        
        // Draw popup border
        g2.setColor(POPUP_BORDER);
        g2.setStroke(new java.awt.BasicStroke(3));
        g2.drawRoundRect(popupX, popupY, POPUP_WIDTH, POPUP_HEIGHT, 20, 20);
        
        // Draw title bar
        g2.setColor(TITLE_BACKGROUND);
        g2.fillRoundRect(popupX, popupY, POPUP_WIDTH, 50, 20, 20);
        g2.fillRect(popupX, popupY + 30, POPUP_WIDTH, 20);
        
        // Draw title
        g2.setFont(TITLE_FONT);
        g2.setColor(SUCCESS_COLOR);
        FontMetrics titleMetrics = g2.getFontMetrics();
        String title = " !MILESTONE ACHIEVED! ";
        int titleX = popupX + (POPUP_WIDTH - titleMetrics.stringWidth(title)) / 2;
        g2.drawString(title, titleX, popupY + 35);
        
        // Content area
        int contentX = popupX + 20;
        int contentY = popupY + 70;
        int contentWidth = POPUP_WIDTH - 40;
        int currentY = contentY;
        
        // Draw milestone description
        g2.setFont(SECTION_FONT);
        g2.setColor(ACCENT_COLOR);
        String milestone = endGame.getMilestoneReached();
        FontMetrics sectionMetrics = g2.getFontMetrics();
        int milestoneX = popupX + (POPUP_WIDTH - sectionMetrics.stringWidth(milestone)) / 2;
        g2.drawString(milestone, milestoneX, currentY);
        currentY += 30;
        
        // Create three columns for stats
        int col1X = contentX;
        int col2X = contentX + contentWidth / 3;
        int col3X = contentX + 2 * contentWidth / 3;
        int colWidth = contentWidth / 3 - 10;
        
        // Column 1: Financial Stats
        currentY = drawStatsColumn(g2, "FINANCIAL", col1X, contentY + 40, colWidth,
            new String[] {
                "Total Income:",
                formatGold(endGame.getTotalIncome()) + "g",
                "",
                "Total Expenditure:",
                formatGold(endGame.getTotalExpenditure()) + "g",
                "",
                "Net Worth:",
                formatGold(endGame.getTotalIncome() - endGame.getTotalExpenditure()) + "g",
                "",
                "Avg Income/Season:",
                formatGold((int)endGame.getAvgSeasonIncome()) + "g",
                "",
                "Avg Expenditure/Season:",
                formatGold((int)endGame.getAvgSeasonExpenditure()) + "g"
            });
        
        // Column 2: Time & Activity Stats  
        drawStatsColumn(g2, "TIME & ACTIVITY", col2X, contentY + 40, colWidth,
            new String[] {
                "Days Played:",
                String.valueOf(endGame.getTotalDaysPlayed()),
                "",
                "Total Seasons:",
                String.valueOf((endGame.getTotalDaysPlayed() / 28) + 1),
                "",
                "Current Gold:",
                formatGold(endGame.getPlayer().getGold()) + "g",
                "",
                "Crops Harvested:",
                String.valueOf(endGame.getCropsHarvested()),
                "",
                "Marriage Status:",
                endGame.getPlayer().isMarried() ? "Married ♥" : "Single"
            });
        
        // Column 3: Fishing & NPC Stats
        List<NPCRelationshipData> npcData = endGame.getNpcRelationships();
        int totalNPCs = npcData != null ? npcData.size() : 0;
        
        drawStatsColumn(g2, "FISHING & NPCS", col3X, contentY + 40, colWidth,
            new String[] {
                "Total Fish Caught:",
                String.valueOf(endGame.getFishCaught()),
                "",
                "Common Fish:",
                String.valueOf(endGame.getCommonFishCaught()),
                "",
                "Regular Fish:",
                String.valueOf(endGame.getRegularFishCaught()),
                "",
                "Legendary Fish:",
                String.valueOf(endGame.getLegendaryFishCaught()),
                "",
                "NPCs Met:",
                String.valueOf(totalNPCs)
            });
        
        // Draw NPC relationships section at bottom
        if (npcData != null && !npcData.isEmpty()) {
            int npcSectionY = popupY + POPUP_HEIGHT - 180;
            drawNPCSection(g2, contentX, npcSectionY, contentWidth, npcData);
        }
        
        // Draw continue hint
        drawContinueHint(g2);
    }
      /**
     * Draw a statistics column
     */
    private int drawStatsColumn(Graphics2D g2, String columnTitle, int x, int y, int width, String[] stats) {
        // Draw column title
        g2.setFont(SECTION_FONT);
        g2.setColor(SECTION_COLOR);
        g2.drawString(columnTitle, x, y);
        
        // Draw statistics
        g2.setFont(STAT_FONT);
        int currentY = y + 25;
        
        for (String stat : stats) {
            if (stat.isEmpty()) {
                currentY += 10; // Small spacing
            } else if (stat.endsWith(":")) {
                // Label
                g2.setColor(TEXT_COLOR);
                g2.drawString(stat, x, currentY);
                currentY += 16;
            } else {
                // Value
                g2.setColor(STAT_COLOR);
                g2.drawString(stat, x + 10, currentY);
                currentY += 16;
            }
        }
        
        return currentY;
    }
    
    /**
     * Draw NPC relationships section
     */
    private void drawNPCSection(Graphics2D g2, int x, int y, int width, List<NPCRelationshipData> npcData) {
        // Section title
        g2.setFont(SECTION_FONT);
        g2.setColor(SECTION_COLOR);
        g2.drawString("NPC RELATIONSHIPS", x, y);
        
        // Draw border around NPC section
        g2.setColor(POPUP_BORDER);
        g2.drawRect(x, y + 10, width, 120);
        
        // Column headers
        g2.setFont(SMALL_FONT);
        g2.setColor(TEXT_COLOR);
        int headerY = y + 30;
        g2.drawString("Name", x + 10, headerY);
        g2.drawString("Hearts", x + 120, headerY);
        g2.drawString("Status", x + 200, headerY);
        g2.drawString("Location", x + 350, headerY);
        
        // Draw separator line
        g2.drawLine(x + 5, headerY + 5, x + width - 5, headerY + 5);
        
        // Draw NPC data
        g2.setFont(SMALL_FONT);
        int startIndex = npcScrollOffset;
        int endIndex = Math.min(startIndex + MAX_VISIBLE_NPCS, npcData.size());
        
        for (int i = startIndex; i < endIndex; i++) {
            NPCRelationshipData npc = npcData.get(i);
            int npcY = headerY + 20 + (i - startIndex) * 15;
            
            // Color code by relationship level
            Color npcColor = getNPCColor(npc.getHeartPoints());
            g2.setColor(npcColor);
            
            g2.drawString(npc.getNpcName(), x + 10, npcY);
            g2.drawString(String.valueOf(npc.getHeartPoints()), x + 120, npcY);
            g2.drawString(npc.getRelationshipStatus(), x + 200, npcY);
            g2.drawString(npc.getLocation(), x + 350, npcY);
        }
        
        // Show scroll indicator if needed
        if (npcData.size() > MAX_VISIBLE_NPCS) {
            g2.setColor(TEXT_COLOR);
            String scrollText = String.format("Showing %d-%d of %d NPCs", 
                startIndex + 1, endIndex, npcData.size());
            g2.drawString(scrollText, x + width - 150, y + 140);
        }
    }
    
    /**
     * Get color for NPC based on heart points
     */
    private Color getNPCColor(int heartPoints) {
        if (heartPoints >= 150) return new Color(255, 105, 180); // Hot pink (spouse)
        if (heartPoints >= 120) return new Color(255, 20, 147);  // Deep pink (fiance)
        if (heartPoints >= 100) return new Color(50, 205, 50);   // Lime green (close friend)
        if (heartPoints >= 60) return new Color(144, 238, 144);  // Light green (good friend)
        if (heartPoints >= 30) return new Color(255, 255, 0);    // Yellow (friend)
        if (heartPoints >= 10) return new Color(255, 165, 0);    // Orange (acquaintance)
        return new Color(169, 169, 169); // Gray (stranger)
    }
    
    /**
     * Draw continue hint at bottom of popup
     */
    private void drawContinueHint(Graphics2D g2) {
        g2.setFont(HINT_FONT);
        g2.setColor(TEXT_COLOR);
        FontMetrics hintMetrics = g2.getFontMetrics();
        
        String hint = "Press ENTER to continue your adventure...";
        int hintX = popupX + (POPUP_WIDTH - hintMetrics.stringWidth(hint)) / 2;
        int hintY = popupY + POPUP_HEIGHT - 15;
        
        // Add blinking effect
        long elapsed = System.currentTimeMillis() - displayStartTime;
        if ((elapsed / 500) % 2 == 0) {  // Blink every 500ms
            g2.drawString(hint, hintX, hintY);
        }
    }
    
    /**
     * Format gold amount with commas for better readability
     */
    private String formatGold(int amount) {
        return String.format("%,d", amount);
    }
    
    /**
     * Check if EndGame screen is currently displaying
     */
    public boolean isDisplaying() {
        return isDisplaying;
    }
    
    /**
     * Hide the EndGame screen
     */
    public void hide() {
        isDisplaying = false;
    }
}