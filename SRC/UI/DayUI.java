package SRC.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * DayUI handles the display of the current day counter.
 * Shows "Day: X" below the clock interface.
 */
public class DayUI {
    private int screenWidth;
    private int screenHeight;
    private ClockUI clockUI;
    
    
    private static final Color DAY_BACKGROUND_COLOR = new Color(206, 156, 95); 
    private static final Color DAY_BORDER_COLOR = new Color(51, 34, 17);
    private static final Color DAY_TEXT_COLOR = Color.WHITE;
    private static final Font DAY_FONT = new Font("Arial", Font.BOLD, 16);
    
    /**
     * Constructor for DayUI
     * @param screenWidth The screen width for positioning
     * @param screenHeight The screen height for positioning
     * @param clockUI Reference to ClockUI to get current day
     */
    public DayUI(int screenWidth, int screenHeight, ClockUI clockUI) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.clockUI = clockUI;
    }
    
    /**
     * Draw the day information below the clock
     * @param g2 Graphics2D object to draw with
     */
    public void drawDayInfo(Graphics2D g2) {
        
        int currentDay = clockUI.getCurrentDay();
        String dayText = "Day: " + currentDay;
        
        
        g2.setFont(DAY_FONT);
        FontMetrics fm = g2.getFontMetrics();
        
        
        int textWidth = fm.stringWidth(dayText);
        int textHeight = fm.getHeight();
        
        
        int clockWidth = 128;
        int clockX = screenWidth - clockWidth - 10;
        int clockY = 10;
        int clockHeight = 128;
        
        
        int dayBoxWidth = textWidth + 20; 
        int dayBoxHeight = textHeight + 10; 
        int dayBoxX = clockX + (clockWidth - dayBoxWidth) / 2; 
        int dayBoxY = clockY + clockHeight -10; 
        
        
        g2.setColor(DAY_BACKGROUND_COLOR);
        g2.fillRoundRect(dayBoxX, dayBoxY, dayBoxWidth, dayBoxHeight, 8, 8);
        
        
        g2.setColor(DAY_BORDER_COLOR);
        g2.drawRoundRect(dayBoxX, dayBoxY, dayBoxWidth, dayBoxHeight, 8, 8);
        
        
        g2.setColor(DAY_TEXT_COLOR);
        int textX = dayBoxX + (dayBoxWidth - textWidth) / 2;
        int textY = dayBoxY + (dayBoxHeight + fm.getAscent()) / 2 - 2;
        g2.drawString(dayText, textX, textY);
    }
    
    /**
     * Update screen dimensions if needed
     * @param screenWidth New screen width
     * @param screenHeight New screen height
     */
    public void updateScreenDimensions(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
}
