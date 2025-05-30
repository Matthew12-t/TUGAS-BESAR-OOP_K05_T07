package SRC.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import SRC.ENTITY.Player;

/**
 * HoldingItemUI handles the display of currently held items (tools and seeds).
 * This component shows what the player is currently holding in their hands.
 */
public class HoldingItemUI {
    // UI styling constants
    private static final Color BACKGROUND_COLOR = new Color(139, 69, 19, 180); // Brown semi-transparent
    private static final Color BORDER_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font MAIN_FONT = new Font("Arial", Font.BOLD, 12);
    
    /**
     * Constructor for HoldingItemUI
     * @param screenWidth The screen width for positioning
     * @param screenHeight The screen height for positioning
     */
    public HoldingItemUI(int screenWidth, int screenHeight) {
        // Constructor parameters kept for compatibility but not stored as they're not used
    }/**
     * Draw the held item information (ONLY ONE item can be held at a time)
     * @param g2 Graphics2D object to draw with
     * @param player Player object to get held item from
     */
    public void drawHeldItems(Graphics2D g2, Player player) {
        int baseX = 10; // Left margin
        int currentY = 70; // Start below energy bar (assuming energy bar is at y=10 with height=20)
        
        // Player can only hold ONE item at a time - either tool OR seed, never both
        SRC.ITEMS.Tool heldTool = player.getCurrentHoldingTool();
        SRC.ITEMS.Seed heldSeed = player.getCurrentHoldingSeed();
        
        if (heldTool != null && heldSeed != null) {
            // ERROR: Player is holding both tool and seed - this should not happen!
            // Clear one of them to fix the inconsistent state (prioritize tool)
            player.setCurrentHoldingSeed(null);
            System.out.println("WARNING: Player was holding both tool and seed. Dropped seed to maintain consistency.");
        }
        
        // Draw the single held item
        if (heldTool != null) {
            // Player is holding a tool
            drawHeldTool(g2, heldTool, baseX, currentY);
        } else if (heldSeed != null) {
            // Player is holding a seed
            drawHeldSeed(g2, heldSeed, baseX, currentY);
        }
    }
    
    /**
     * Draw held tool information
     * @param g2 Graphics2D object to draw with
     * @param heldTool The tool being held
     * @param x X position to draw at
     * @param y Y position to draw at
     * @return New Y position after drawing
     */
    private int drawHeldTool(Graphics2D g2, SRC.ITEMS.Tool heldTool, int x, int y) {
        // Draw tool info background
        int infoWidth = 200;
        int infoHeight = 30;
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRoundRect(x, y, infoWidth, infoHeight, 10, 10);
        
        // Draw tool info border
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(x, y, infoWidth, infoHeight, 10, 10);
        
        // Draw tool icon if available
        if (heldTool.getImage() != null) {
            int iconSize = 24;
            int iconX = x + 5;
            int iconY = y + (infoHeight - iconSize) / 2;
            g2.drawImage(heldTool.getImage(), iconX, iconY, iconSize, iconSize, null);
        }
        
        // Draw tool name
        g2.setColor(TEXT_COLOR);
        g2.setFont(MAIN_FONT);
        FontMetrics fm = g2.getFontMetrics();
        String toolText = "Holding: " + heldTool.getName();
        int toolTextX = x + (heldTool.getImage() != null ? 35 : 10);
        int toolTextY = y + (infoHeight + fm.getAscent()) / 2 - 2;
        g2.drawString(toolText, toolTextX, toolTextY);
        
        return y + infoHeight;
    }
    
    /**
     * Draw held seed information
     * @param g2 Graphics2D object to draw with
     * @param heldSeed The seed being held
     * @param x X position to draw at
     * @param y Y position to draw at
     * @return New Y position after drawing
     */
    private int drawHeldSeed(Graphics2D g2, SRC.ITEMS.Seed heldSeed, int x, int y) {
        // Draw seed info background
        int infoWidth = 200;
        int infoHeight = 30;
        g2.setColor(new Color(34, 139, 34, 180)); // Green semi-transparent for seeds
        g2.fillRoundRect(x, y, infoWidth, infoHeight, 10, 10);
        
        // Draw seed info border
        g2.setColor(BORDER_COLOR);
        g2.drawRoundRect(x, y, infoWidth, infoHeight, 10, 10);
        
        // Draw seed icon if available
        if (heldSeed.getImage() != null) {
            int iconSize = 24;
            int iconX = x + 5;
            int iconY = y + (infoHeight - iconSize) / 2;
            g2.drawImage(heldSeed.getImage(), iconX, iconY, iconSize, iconSize, null);
        }
        
        // Draw seed name
        g2.setColor(TEXT_COLOR);
        g2.setFont(MAIN_FONT);
        FontMetrics fm = g2.getFontMetrics();
        String seedText = "Holding: " + heldSeed.getName();
        int seedTextX = x + (heldSeed.getImage() != null ? 35 : 10);
        int seedTextY = y + (infoHeight + fm.getAscent()) / 2 - 2;
        g2.drawString(seedText, seedTextX, seedTextY);
        
        return y + infoHeight;
    }
      /**
     * Update screen dimensions if needed (method kept for compatibility)
     * @param screenWidth New screen width
     * @param screenHeight New screen height
     */
    public void updateScreenDimensions(int screenWidth, int screenHeight) {
        // Method kept for compatibility but does nothing since fields are not used
    }
}
