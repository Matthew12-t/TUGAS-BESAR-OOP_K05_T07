package SRC.ENTITY.ACTION;

import SRC.MAIN.GamePanel;
import java.awt.*;

/**
 * FishingUI class handles visual feedback for fishing actions
 */
public class FishingUI {
    private GamePanel gp;
    private boolean showFishingMessage = false;
    private String fishingMessage = "";
    private int messageTimer = 0;
    private final int MESSAGE_DURATION = 180; // 3 seconds at 60 FPS
    private Color messageColor = Color.WHITE;
    
    public FishingUI(GamePanel gp) {
        this.gp = gp;
    }
    
    /**
     * Display fishing result message
     */
    public void showFishingResult(String fishName, boolean success) {
        if (success) {
            fishingMessage = "You caught a " + fishName + "!";
            messageColor = Color.GREEN;
        } else {
            fishingMessage = "No fish bit... Try again!";
            messageColor = Color.ORANGE;
        }
        showFishingMessage = true;
        messageTimer = MESSAGE_DURATION;
    }
    
    /**
     * Show fishing attempt message
     */
    public void showFishingAttempt() {
        fishingMessage = "Casting your line...";
        messageColor = Color.CYAN;
        showFishingMessage = true;
        messageTimer = 90; // 1.5 seconds
    }
    
    /**
     * Show insufficient energy message
     */
    public void showInsufficientEnergy() {
        fishingMessage = "Not enough energy to fish!";
        messageColor = Color.RED;
        showFishingMessage = true;
        messageTimer = MESSAGE_DURATION;
    }
    
    /**
     * Show invalid location message
     */
    public void showInvalidLocation() {
        fishingMessage = "You need to be near water to fish!";
        messageColor = Color.YELLOW;
        showFishingMessage = true;
        messageTimer = MESSAGE_DURATION;
    }
    
    /**
     * Show missing fishing rod message
     */
    public void showMissingFishingRod() {
        fishingMessage = "You need a fishing rod to fish!";
        messageColor = Color.RED;
        showFishingMessage = true;
        messageTimer = MESSAGE_DURATION;
    }
    
    /**
     * Update the fishing UI
     */
    public void update() {
        if (showFishingMessage) {
            messageTimer--;
            if (messageTimer <= 0) {
                showFishingMessage = false;
            }
        }
    }
    
    /**
     * Draw the fishing UI
     */
    public void draw(Graphics2D g2) {
        if (showFishingMessage) {
            // Save original settings
            Font originalFont = g2.getFont();
            Color originalColor = g2.getColor();
            
            // Set message font and color
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.setColor(Color.BLACK);
            
            // Calculate message position (center of screen)
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(fishingMessage);
            int x = (gp.getWidth() - textWidth) / 2;
            int y = gp.getHeight() / 2 - 100;
            
            // Draw shadow
            g2.drawString(fishingMessage, x + 2, y + 2);
            
            // Draw main text
            g2.setColor(messageColor);
            g2.drawString(fishingMessage, x, y);
            
            // Add fade effect for last 30 frames
            if (messageTimer <= 30) {
                float alpha = (float) messageTimer / 30.0f;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            }
            
            // Restore original settings
            g2.setFont(originalFont);
            g2.setColor(originalColor);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
    
    /**
     * Check if fishing message is currently being displayed
     */
    public boolean isShowingMessage() {
        return showFishingMessage;
    }    /**
     * Play GUI-based mini-game using JOptionPane
     * @param fishType Type of fish (Common, Regular, Legendary)
     * @param targetNumber The number player needs to guess
     * @param maxRange Maximum range for guessing
     * @param maxAttempts Maximum attempts allowed
     * @return true if player wins the mini-game
     */
    public boolean playGUIMiniGame(String fishType, int targetNumber, int maxRange, int maxAttempts) {
        System.out.println("DEBUG: playGUIMiniGame called with fishType=" + fishType + ", targetNumber=" + targetNumber + ", maxRange=" + maxRange + ", maxAttempts=" + maxAttempts);
        
        // Simple approach - directly use JOptionPane without threading complications
        try {
            javax.swing.JOptionPane.showMessageDialog(null, 
                "A " + fishType + " fish is on the line!\n" +
                "Guess a number between 1 and " + maxRange + "\n" +
                "You have " + maxAttempts + " attempts!",
                "Fishing Mini-Game", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println("ERROR showing intro dialog: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            String input = javax.swing.JOptionPane.showInputDialog(null,
                "Attempt " + attempt + "/" + maxAttempts + "\n" +
                "Guess a number between 1 and " + maxRange + ":",
                "Fishing Mini-Game",
                javax.swing.JOptionPane.QUESTION_MESSAGE);            // Check if user cancelled
            if (input == null) {
                // Reset key states to prevent stuck movement keys
                gp.getKeyHandler().resetAllKeyStates();
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "The fish got away!", 
                    "Fishing Failed", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            try {
                int guess = Integer.parseInt(input);
                
                if (guess == targetNumber) {
                    javax.swing.JOptionPane.showMessageDialog(null, 
                        "Perfect! You caught the " + fishType + " fish!",
                        "Fishing Success", 
                        javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else if (guess < targetNumber) {
                    if (attempt < maxAttempts) {
                        javax.swing.JOptionPane.showMessageDialog(null, 
                            "Too low! The fish is still fighting!",
                            "Try Again", 
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    if (attempt < maxAttempts) {
                        javax.swing.JOptionPane.showMessageDialog(null, 
                            "Too high! The fish is still fighting!",
                            "Try Again", 
                            javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(null, 
                    "Invalid input! Please enter a number.",
                    "Error", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                attempt--; // Don't count invalid attempts
            }
        }
        
        javax.swing.JOptionPane.showMessageDialog(null, 
            "The fish got away! The number was " + targetNumber,
            "Fishing Failed", 
            javax.swing.JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
