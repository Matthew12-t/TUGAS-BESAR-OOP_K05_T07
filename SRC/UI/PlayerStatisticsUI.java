package SRC.UI;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;

import SRC.MAIN.GamePanel;
import SRC.ENTITY.Player;

/**
 * PlayerStatisticsUI shows comprehensive player statistics during gameplay
 * Similar to EndGameUI but for current game status
 */
public class PlayerStatisticsUI {
    private GamePanel gamePanel;
    private boolean isDisplaying = false;
    
    
    private static final int POPUP_WIDTH = 700;
    private static final int POPUP_HEIGHT = 600;
    private int popupX;
    private int popupY;
    
    
    private static final Color POPUP_BACKGROUND = new Color(40, 40, 60, 240);
    private static final Color POPUP_BORDER = new Color(255, 215, 0, 255); 
    private static final Color TITLE_BACKGROUND = new Color(50, 50, 80, 255);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 215, 0); 
    private static final Color SUCCESS_COLOR = new Color(50, 205, 50); 
    private static final Color STAT_COLOR = new Color(200, 200, 255); 
    private static final Color SECTION_COLOR = new Color(180, 180, 200); 
    
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font SECTION_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font STAT_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
    
    public PlayerStatisticsUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        
        
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        this.popupX = (screenWidth - POPUP_WIDTH) / 2;
        this.popupY = (screenHeight - POPUP_HEIGHT) / 2;
    }
    
    public void showStatistics() {
        this.isDisplaying = true;
    }
    
    public void hideStatistics() {
        this.isDisplaying = false;
    }
    
    public boolean isDisplaying() {
        return isDisplaying;
    }
    
    public void draw(Graphics2D g2) {
        if (!isDisplaying) return;
        
        Player player = gamePanel.getPlayer();
        if (player == null) return;
        
        
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gamePanel.getScreenWidth(), gamePanel.getScreenHeight());
        
        
        g2.setColor(POPUP_BACKGROUND);
        g2.fillRoundRect(popupX, popupY, POPUP_WIDTH, POPUP_HEIGHT, 20, 20);
        
        
        g2.setColor(POPUP_BORDER);
        g2.setStroke(new java.awt.BasicStroke(3));
        g2.drawRoundRect(popupX, popupY, POPUP_WIDTH, POPUP_HEIGHT, 20, 20);
        g2.setStroke(new java.awt.BasicStroke(1)); 
        
        
        g2.setColor(TITLE_BACKGROUND);
        g2.fillRoundRect(popupX + 10, popupY + 10, POPUP_WIDTH - 20, 50, 15, 15);
        
        
        g2.setColor(ACCENT_COLOR);
        g2.setFont(TITLE_FONT);
        String title = "Player Statistics";
        FontMetrics titleMetrics = g2.getFontMetrics();
        int titleX = popupX + (POPUP_WIDTH - titleMetrics.stringWidth(title)) / 2;
        g2.drawString(title, titleX, popupY + 45);
        
        
        int contentX = popupX + 20;
        int contentY = popupY + 80;
        int contentWidth = POPUP_WIDTH - 40;
        
        
        int currentY = contentY;
        g2.setFont(SECTION_FONT);
        g2.setColor(SECTION_COLOR);
        g2.drawString("PLAYER INFORMATION", contentX, currentY);
        currentY += 25;
        
        g2.setFont(STAT_FONT);
        g2.setColor(TEXT_COLOR);
        g2.drawString("Name: " + player.getPlayerName(), contentX, currentY);
        currentY += 20;
        g2.drawString("Farm: " + player.getFarmName(), contentX, currentY);
        currentY += 20;
        g2.drawString("Gender: " + player.getGender(), contentX, currentY);
        currentY += 20;
        g2.drawString("Favorite Item: " + player.getFavoriteItem(), contentX, currentY);
        currentY += 20;
        g2.setColor(player.isMarried() ? SUCCESS_COLOR : TEXT_COLOR);
        g2.drawString("Marriage Status: " + (player.isMarried() ? "Married ♥" : "Single"), contentX, currentY);
        currentY += 35;
        
        
        int col1X = contentX;
        int col2X = contentX + contentWidth / 3;
        int col3X = contentX + 2 * contentWidth / 3;
        int colWidth = contentWidth / 3 - 10;
        
        
        drawStatsColumn(g2, "FINANCIAL", col1X, currentY, colWidth,
            new String[] {
                "Current Gold:",
                formatGold(player.getGold()) + "g",
                "",
                "Total Income:",
                formatGold(player.getTotalIncome()) + "g",
                "",
                "Total Expenditure:",
                formatGold(player.getTotalExpenditure()) + "g",
                "",
                "Net Worth:",
                formatGold(player.getTotalIncome() - player.getTotalExpenditure()) + "g"
            });
        
        
        drawStatsColumn(g2, "TIME & ACTIVITY", col2X, currentY, colWidth,
            new String[] {
                "Days Played:",
                String.valueOf(player.getDaysPlayed()),
                "",
                "Current Energy:",
                String.valueOf(player.getEnergy()) + "/" + String.valueOf(player.getMaxEnergy()),
                "",
                "Crops Harvested:",
                String.valueOf(player.getTotalCropsHarvested()),
                "",
                "Current Season:",
                gamePanel.getCurrentSeasonName(),
                "",
                "Weather:",
                gamePanel.getWeatherString()
            });
        
        
        drawStatsColumn(g2, "FISHING & PROGRESS", col3X, currentY, colWidth,
            new String[] {
                "Total Fish Caught:",
                String.valueOf(player.getTotalFishCaught()),
                "",
                "Current Location:",
                gamePanel.getCurrentMap().getMapName(),
                "",
                "Position:",
                "X: " + (player.getWorldX() / gamePanel.getTileSize()),
                "Y: " + (player.getWorldY() / gamePanel.getTileSize()),
                "",
                "Game Progress:",
                calculateProgressPercentage() + "%"
            });
        
        
        drawContinueHint(g2);
    }
    
    private void drawStatsColumn(Graphics2D g2, String columnTitle, int x, int y, int width, String[] stats) {
        
        g2.setFont(SECTION_FONT);
        g2.setColor(SECTION_COLOR);
        g2.drawString(columnTitle, x, y);
        
        
        g2.setFont(STAT_FONT);
        int currentY = y + 25;
        
        for (String stat : stats) {
            if (stat.isEmpty()) {
                currentY += 10; 
            } else if (stat.endsWith(":")) {
                
                g2.setColor(TEXT_COLOR);
                g2.drawString(stat, x, currentY);
                currentY += 18;
            } else {
                
                g2.setColor(STAT_COLOR);
                g2.drawString(stat, x + 10, currentY);
                currentY += 18;
            }
        }
    }
    
    private void drawContinueHint(Graphics2D g2) {
        g2.setFont(SMALL_FONT);
        g2.setColor(ACCENT_COLOR);
        String hint = "Press ESC  to close";
        FontMetrics metrics = g2.getFontMetrics();
        int hintWidth = metrics.stringWidth(hint);
        int hintX = popupX + (POPUP_WIDTH - hintWidth) / 2;
        int hintY = popupY + POPUP_HEIGHT - 20;
        g2.drawString(hint, hintX, hintY);
    }
    
    private String formatGold(int gold) {
        if (gold >= 1000000) {
            return String.format("%.1fM", gold / 1000000.0);
        } else if (gold >= 1000) {
            return String.format("%.1fK", gold / 1000.0);
        } else {
            return String.valueOf(gold);
        }
    }
    
    private String calculateProgressPercentage() {
        Player player = gamePanel.getPlayer();
        int totalProgress = 0;
        int maxProgress = 0;
        
        
        
        maxProgress += 20;
        if (player.isMarried()) totalProgress += 20;
        
        
        maxProgress += 30;
        int goldProgress = Math.min(30, (player.getGold() * 30) / 17209);
        totalProgress += goldProgress;
        
        
        maxProgress += 20;
        int daysProgress = Math.min(20, (player.getDaysPlayed() * 20) / 100);
        totalProgress += daysProgress;
        
        
        maxProgress += 15;
        int cropsProgress = Math.min(15, (player.getTotalCropsHarvested() * 15) / 500);
        totalProgress += cropsProgress;
        
        
        maxProgress += 15;
        int fishProgress = Math.min(15, (player.getTotalFishCaught() * 15) / 100);
        totalProgress += fishProgress;
        
        return String.valueOf((totalProgress * 100) / maxProgress);
    }
    
    public void handleKeyPress(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_ENTER) {
            hideStatistics();
        }
    }
}
