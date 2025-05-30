package SRC.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import SRC.ENTITY.Player;

/**
 * EnergyUI handles the display of the player's energy bar
 */
public class EnergyUI {
    private int screenWidth;
    private int screenHeight;
      // Energy bar properties
    private static final int BAR_WIDTH = 200;
    private static final int BAR_HEIGHT = 20;
    private static final int BAR_X = 10; // Top-left corner
    private static final int BAR_Y = 30; // Moved down slightly for better positioning
    
    // Colors
    private static final Color BACKGROUND_COLOR = new Color(50, 50, 50, 150); // Dark gray semi-transparent
    private static final Color BORDER_COLOR = Color.WHITE;
    private static final Color HIGH_ENERGY_COLOR = new Color(76, 175, 80); // Green
    private static final Color MEDIUM_ENERGY_COLOR = new Color(255, 193, 7); // Yellow
    private static final Color LOW_ENERGY_COLOR = new Color(244, 67, 54); // Red
    
    /**
     * Constructor for EnergyUI
     * @param screenWidth Width of the screen
     * @param screenHeight Height of the screen
     */
    public EnergyUI(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
    
    /**
     * Draw the energy bar on the screen
     * @param g2 Graphics2D object to draw with
     * @param player Player object to get energy information from
     */
    public void drawEnergyBar(Graphics2D g2, Player player) {
        // Draw background (empty bar)
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(BAR_X, BAR_Y, BAR_WIDTH, BAR_HEIGHT);
        
        // Draw border
        g2.setColor(BORDER_COLOR);
        g2.drawRect(BAR_X, BAR_Y, BAR_WIDTH, BAR_HEIGHT);
        
        // Calculate energy fill width
        double energyPercentage = player.getEnergyPercentage();
        int fillWidth = (int) (BAR_WIDTH * energyPercentage);
        
        // Draw energy fill with color based on energy level
        if (energyPercentage > 0.6) {
            g2.setColor(HIGH_ENERGY_COLOR);
        } else if (energyPercentage > 0.3) {
            g2.setColor(MEDIUM_ENERGY_COLOR);
        } else {
            g2.setColor(LOW_ENERGY_COLOR);
        }
        
        g2.fillRect(BAR_X + 1, BAR_Y + 1, fillWidth - 2, BAR_HEIGHT - 2);
        
        // Draw energy text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String energyText = player.getCurrentEnergy() + "/" + player.getMaxEnergy();
        FontMetrics fm = g2.getFontMetrics();
        int textX = BAR_X + (BAR_WIDTH - fm.stringWidth(energyText)) / 2;
        int textY = BAR_Y + (BAR_HEIGHT + fm.getAscent()) / 2 - 2;
        g2.drawString(energyText, textX, textY);
        
        // Draw "Energy" label above the bar
        g2.setFont(new Font("Arial", Font.BOLD, 10));
        g2.drawString("Energy", BAR_X, BAR_Y - 2);
    }
    
    /**
     * Get the energy bar's X position
     * @return X position of the energy bar
     */
    public int getBarX() {
        return BAR_X;
    }
    
    /**
     * Get the energy bar's Y position
     * @return Y position of the energy bar
     */
    public int getBarY() {
        return BAR_Y;
    }
    
    /**
     * Get the energy bar's width
     * @return Width of the energy bar
     */
    public int getBarWidth() {
        return BAR_WIDTH;
    }
    
    /**
     * Get the energy bar's height
     * @return Height of the energy bar
     */
    public int getBarHeight() {
        return BAR_HEIGHT;
    }
    
    /**
     * Get the bottom Y position of the energy bar (for positioning other UI elements)
     * @return Bottom Y position of the energy bar
     */
    public int getBottomY() {
        return BAR_Y + BAR_HEIGHT;
    }
}
