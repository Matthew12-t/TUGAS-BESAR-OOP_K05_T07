package SRC.UI;

import SRC.MAIN.GamePanel;
import SRC.CHEAT.Cheat;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CheatUI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_30, arial_60B;  
    public boolean isActive = false;
    private String inputText = "";
    private String outputText = "Enter cheat command:";
    private boolean cursorVisible = true;
    private int cursorTimer = 0;
    private Cheat cheat;

    public CheatUI(GamePanel gp) {
        this.gp = gp;
        arial_30 = new Font("Arial", Font.PLAIN, 20);  // Font size for input and regular text
        arial_60B = new Font("Arial", Font.BOLD, 30);  // Font size for title
    }
    
    public void setCheat(Cheat cheat) {
        this.cheat = cheat;
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        
        if (isActive) {
            drawCheatWindow();
        }
    }
    
    private void drawCheatWindow() {
        // Background
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(gp.getTileSize() * 2, gp.getTileSize() * 2, 
                        gp.getScreenWidth() - gp.getTileSize() * 4, 
                        gp.getScreenHeight() - gp.getTileSize() * 4, 35, 35);
        
        // Window border
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(gp.getTileSize() * 2 + 5, gp.getTileSize() * 2 + 5, 
                        gp.getScreenWidth() - gp.getTileSize() * 4 - 10, 
                        gp.getScreenHeight() - gp.getTileSize() * 4 - 10, 25, 25);
        
        // Title
        g2.setFont(arial_60B);
        g2.setColor(Color.WHITE);
        String text = "CHEAT CONSOLE";
        int x = getXforCenteredText(text);
        int y = gp.getTileSize() * 3;
        g2.drawString(text, x, y);
        
        // Instructions
        g2.setFont(arial_30);
        text = "Commands:";
        x = gp.getTileSize() * 3;
        y += gp.getTileSize();
        g2.drawString(text, x, y);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));  // Smaller font for instructions
        String[] instructions = {
            "add item {name} {quantity} - Add items to inventory",
            "add time {minutes} - Add time",
            "add gold {amount} - Add gold",
            "goto season {winter/spring/summer/fall} - Change season",
            "goto weather {sunny/rainy} - Change weather",
            "",
            "Press Ctrl+C to close"
        };
        
        for (String instruction : instructions) {
            y += 30;  // Changed from 35
            g2.drawString(instruction, x, y);
        }
        
        // Input field
        y += gp.getTileSize();
        g2.setFont(arial_30);
        g2.setColor(Color.WHITE);
        text = "Input: ";
        g2.drawString(text, x, y);
        
        // Input box
        int inputBoxX = x + g2.getFontMetrics().stringWidth(text);
        int inputBoxY = y - 35;
        int inputBoxWidth = gp.getScreenWidth() - gp.getTileSize() * 6 - g2.getFontMetrics().stringWidth(text);
        int inputBoxHeight = 40;  // Changed from 45
        
        g2.setColor(new Color(50, 50, 50));
        g2.fillRect(inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight);
        g2.setColor(Color.WHITE);
        g2.drawRect(inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight);
        
        // Input text
        g2.setColor(Color.WHITE);
        String displayText = inputText;
        
        // Cursor
        cursorTimer++;
        if (cursorTimer > 30) {
            cursorVisible = !cursorVisible;
            cursorTimer = 0;
        }
        
        if (cursorVisible) {
            displayText += "|";
        }
        
        g2.drawString(displayText, inputBoxX + 5, y);
        
        // Output area
        y += gp.getTileSize();
        g2.setColor(Color.YELLOW);
        text = "Output:";
        g2.drawString(text, x, y);

        y += 30;  // Reduced spacing
        g2.setFont(new Font("Arial", Font.PLAIN, 16));  // Smaller font for output
        g2.setColor(Color.LIGHT_GRAY);
        
        // Split output text into lines
        String[] outputLines = outputText.split("\n");
        for (String line : outputLines) {
            g2.drawString(line, x, y);
            y += 30;  // Changed from 35
        }
    }
    
    public void handleKeyInput(KeyEvent e) {
        if (!isActive) return;
        
        int keyCode = e.getKeyCode();
        
        if (keyCode == KeyEvent.VK_ENTER) {
            if (!inputText.trim().isEmpty()) {
                executeCommand();
            }
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            if (inputText.length() > 0) {
                inputText = inputText.substring(0, inputText.length() - 1);
            }
        }
    }
    
    public void handleCharInput(char c) {
        if (!isActive) return;
        
        // Accept letters, numbers, spaces and basic punctuation
        if ((c >= 'a' && c <= 'z') || 
            (c >= 'A' && c <= 'Z') ||
            (c >= '0' && c <= '9') ||
            c == ' ' || c == '{' || c == '}' || 
            c == '_' || c == '-' || c == '.') {
            inputText += c;
        }
    }
    
    private void executeCommand() {
        if (cheat != null && !inputText.trim().isEmpty()) {
            outputText = cheat.executeCommand(inputText);
        }
        inputText = "";
    }
    
    public void toggle() {
        isActive = !isActive;
        if (isActive) {
            inputText = "";
            outputText = "Enter cheat command:";
        }
    }
    
    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.getScreenWidth() / 2 - length / 2;
        return x;
    }
}
