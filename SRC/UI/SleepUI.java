package SRC.UI;

import SRC.MAIN.GamePanel;
import SRC.DATA.SleepData;
import SRC.DATA.SleepData.SleepResult;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * SleepUI handles the display of sleep result screens
 */
public class SleepUI {    private GamePanel gamePanel;
    private SleepResult currentSleepResult;
    private boolean isDisplaying;
    private BufferedImage backgroundImage;
    private long displayStartTime;
    
    // UI styling
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 150);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 215, 0); // Gold color
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font MESSAGE_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final Font INFO_FONT = new Font("Arial", Font.PLAIN, 16);
    
    public SleepUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.isDisplaying = false;
    }
    
    /**
     * Show sleep result screen
     */
    public void showSleepResult(SleepResult sleepResult) {
        this.currentSleepResult = sleepResult;
        this.isDisplaying = true;
        this.displayStartTime = System.currentTimeMillis();
        
        // Load background image based on sleep trigger
        loadBackgroundImage(sleepResult.getBackgroundImage());
        
        System.out.println("Displaying sleep result: " + sleepResult.getTrigger());
    }
      /**
     * Load background image for sleep screen
     */
    private void loadBackgroundImage(String imagePath) {
        try {
            // Prioritas menggunakan path langsung sebagai file
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                backgroundImage = ImageIO.read(imageFile);
                System.out.println("DEBUG: Sleep background image loaded from file: " + imagePath);
            } else {
                // Coba sebagai resource
                backgroundImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
                if (backgroundImage == null) {
                    // Fallback untuk backward compatibility
                    backgroundImage = ImageIO.read(new File("RES/SLEEP/sleeping_Background.png"));
                    System.out.println("DEBUG: Using fallback sleep background: RES/SLEEP/sleeping_Background.png");
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load sleep background image: " + imagePath + " - Error: " + e.getMessage());
            // Create a simple colored background as fallback
            backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = backgroundImage.createGraphics();
            g.setColor(new Color(25, 25, 50)); // Dark blue background
            g.fillRect(0, 0, 800, 600);
            g.dispose();
        }
    }
      /**
     * Update sleep UI (no auto-close, wait for Enter key)
     */
    public void update() {
        // SleepUI sekarang tidak auto-close, menunggu Enter key
        // Auto-close logic dihapus
    }
    
    /**
     * Handle Enter key press to close sleep screen
     */
    public void handleEnterPress() {
        if (isDisplaying) {
            isDisplaying = false;
            currentSleepResult = null;
            // Resume normal game flow
            gamePanel.setGameState(GamePanel.PLAY_STATE);
            System.out.println("Sleep screen closed by Enter key");
        }
    }
    
    /**
     * Draw sleep result screen
     */
    public void draw(Graphics2D g2) {
        if (!isDisplaying || currentSleepResult == null) {
            return;
        }
        
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        // Draw background image (scaled to screen)
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        }
        
        // Draw overlay for better text readability
        g2.setColor(OVERLAY_COLOR);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        // Calculate text areas
        int centerX = screenWidth / 2;
        int startY = screenHeight / 4;
        int lineHeight = 30;
        
        // Draw sleep trigger-specific title
        g2.setFont(TITLE_FONT);
        g2.setColor(ACCENT_COLOR);
        FontMetrics titleMetrics = g2.getFontMetrics();
        
        String title = getTitleForTrigger(currentSleepResult.getTrigger());
        int titleX = centerX - titleMetrics.stringWidth(title) / 2;
        g2.drawString(title, titleX, startY);
        
        // Draw main sleep message
        g2.setFont(MESSAGE_FONT);
        g2.setColor(TEXT_COLOR);
        FontMetrics messageMetrics = g2.getFontMetrics();
        
        String message = currentSleepResult.getMessage();
        String[] messageLines = wrapText(message, messageMetrics, screenWidth - 100);
        
        int messageY = startY + 60;
        for (String line : messageLines) {
            int lineX = centerX - messageMetrics.stringWidth(line) / 2;
            g2.drawString(line, lineX, messageY);
            messageY += lineHeight;
        }
          // Draw game information box
        drawInfoBox(g2, centerX, messageY + 40);
        
        // Draw "Press Enter to continue" hint instead of progress bar
        drawEnterHint(g2, screenWidth, screenHeight);
    }
    
    /**
     * Get title text based on sleep trigger
     */
    private String getTitleForTrigger(SleepData.SleepTrigger trigger) {
        switch (trigger) {
            case MANUAL:
                return "Good Night's Sleep";
            case LOW_ENERGY:
                return "Exhaustion Sleep";
            case LATE_TIME:
                return "Late Night Sleep";
            default:
                return "Sleep";
        }
    }
    
    /**
     * Draw information box with game stats
     */
    private void drawInfoBox(Graphics2D g2, int centerX, int startY) {
        int boxWidth = 400;
        int boxHeight = 120;
        int boxX = centerX - boxWidth / 2;
        
        // Draw box background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(boxX, startY, boxWidth, boxHeight, 10, 10);
        
        // Draw box border
        g2.setColor(ACCENT_COLOR);
        g2.drawRoundRect(boxX, startY, boxWidth, boxHeight, 10, 10);
        
        // Draw information text
        g2.setFont(INFO_FONT);
        g2.setColor(TEXT_COLOR);
        
        int textX = boxX + 20;
        int textY = startY + 25;
        int lineSpacing = 20;
        
        g2.drawString("Day: " + currentSleepResult.getDay(), textX, textY);
        g2.drawString("Season: " + currentSleepResult.getSeason().toString(), textX, textY + lineSpacing);
        g2.drawString("Weather: " + currentSleepResult.getWeather().toString(), textX, textY + lineSpacing * 2);
        
        // Draw income with special formatting
        g2.setColor(ACCENT_COLOR);
        g2.drawString("Daily Income: " + currentSleepResult.getIncome() + " coins", textX, textY + lineSpacing * 3);
    }
      /**
     * Draw Enter key hint
     */
    private void drawEnterHint(Graphics2D g2, int screenWidth, int screenHeight) {
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(ACCENT_COLOR);
        String hint = "Press Enter to continue...";
        FontMetrics hintMetrics = g2.getFontMetrics();
        int hintX = (screenWidth - hintMetrics.stringWidth(hint)) / 2;
        int hintY = screenHeight - 30;
        g2.drawString(hint, hintX, hintY);
    }
    
    /**
     * Wrap text to fit within specified width
     */
    private String[] wrapText(String text, FontMetrics metrics, int maxWidth) {
        String[] words = text.split(" ");
        java.util.List<String> lines = new java.util.ArrayList<>();
        String currentLine = "";
        
        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            if (metrics.stringWidth(testLine) <= maxWidth) {
                currentLine = testLine;
            } else {
                if (!currentLine.isEmpty()) {
                    lines.add(currentLine);
                }
                currentLine = word;
            }
        }
        
        if (!currentLine.isEmpty()) {
            lines.add(currentLine);
        }
        
        return lines.toArray(new String[0]);
    }
    
    /**
     * Check if sleep UI is currently displaying
     */
    public boolean isDisplaying() {
        return isDisplaying;
    }
    
    /**
     * Force close sleep UI
     */
    public void close() {
        isDisplaying = false;
        currentSleepResult = null;
    }
}
