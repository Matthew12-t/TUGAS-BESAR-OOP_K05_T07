package SRC.MAIN.MENU;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;

import SRC.MAIN.GamePanel;

/**
 * MainMenu handles the main menu system with 6 buttons arranged in 2 rows of 3
 * Buttons: ACTION, CREDITS, EXIT, HELP, LOAD, NEW_GAME
 */
public class MainMenu {
    private GamePanel gamePanel;
    
    // Menu state constants
    public static final int MAIN_MENU = 0;
    public static final int ACTION_SCREEN = 1;
    public static final int CREDITS_SCREEN = 2;
    public static final int HELP_SCREEN = 3;
    public static final int NEW_GAME_MENU = 4;
    public static final int LOAD_GAME_MENU = 5;
    
    private int currentMenuState = MAIN_MENU;
    
    // Button layout - 2 rows x 3 columns
    private final int BUTTON_ROWS = 2;
    private final int BUTTON_COLS = 3;
    private final int BUTTON_WIDTH = 150;
    private final int BUTTON_HEIGHT = 60;
    private final int BUTTON_SPACING_X = 20;
    private final int BUTTON_SPACING_Y = 20;
    
    // Button positions and selection
    private int selectedButtonRow = 0;
    private int selectedButtonCol = 0;
    private Rectangle[] buttonBounds;    // Images
    private BufferedImage backgroundImage;
    private BufferedImage actionImage;
    private BufferedImage creditsImage;
    private BufferedImage helpImage;
    
    // Button images
    private BufferedImage actionButtonImage;
    private BufferedImage creditsButtonImage;
    private BufferedImage exitButtonImage;
    private BufferedImage helpButtonImage;
    private BufferedImage loadButtonImage;
    private BufferedImage newGameButtonImage;
    
    // Button names in order (row by row)
    private final String[] buttonNames = {
        "ACTION", "CREDITS", "EXIT",
        "HELP", "LOAD", "NEW GAME"
    };
    
    public MainMenu(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.buttonBounds = new Rectangle[6];
        loadImages();
        setupButtonBounds();
    }
      private void loadImages() {
        try {
            // Load background image
            backgroundImage = ImageIO.read(new File("RES/MAINMENU/mainmenu.png"));
            
            // Load action screen image
            actionImage = ImageIO.read(new File("RES/MAINMENU/action1.png"));
            
            // Load credits screen image  
            creditsImage = ImageIO.read(new File("RES/MAINMENU/credits1.png"));
            
            // Load help screen image
            helpImage = ImageIO.read(new File("RES/MAINMENU/help1.png"));
            
            // Load button images
            actionButtonImage = ImageIO.read(new File("RES/MAINMENU/action.png"));
            creditsButtonImage = ImageIO.read(new File("RES/MAINMENU/credits.png"));
            exitButtonImage = ImageIO.read(new File("RES/MAINMENU/exit.png"));
            helpButtonImage = ImageIO.read(new File("RES/MAINMENU/help.png"));
            loadButtonImage = ImageIO.read(new File("RES/MAINMENU/load.png"));
            newGameButtonImage = ImageIO.read(new File("RES/MAINMENU/new_game.png"));
            
            System.out.println("Main menu images loaded successfully");
        } catch (IOException e) {
            System.err.println("Error loading main menu images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupButtonBounds() {
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        // Calculate starting position to center the button grid
        int totalGridWidth = (BUTTON_COLS * BUTTON_WIDTH) + ((BUTTON_COLS - 1) * BUTTON_SPACING_X);
        int totalGridHeight = (BUTTON_ROWS * BUTTON_HEIGHT) + ((BUTTON_ROWS - 1) * BUTTON_SPACING_Y);
        
        int startX = (screenWidth - totalGridWidth) / 2;
        int startY = (screenHeight - totalGridHeight) / 2 + 180; // Offset down a bit
        
        // Create button bounds
        for (int row = 0; row < BUTTON_ROWS; row++) {
            for (int col = 0; col < BUTTON_COLS; col++) {
                int index = row * BUTTON_COLS + col;
                int x = startX + col * (BUTTON_WIDTH + BUTTON_SPACING_X);
                int y = startY + row * (BUTTON_HEIGHT + BUTTON_SPACING_Y);
                
                buttonBounds[index] = new Rectangle(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
            }
        }
    }
    
    public void update() {
        // Handle menu state-specific updates if needed
    }
    
    public void draw(Graphics2D g2) {
        switch (currentMenuState) {
            case MAIN_MENU:
                drawMainMenu(g2);
                break;
            case ACTION_SCREEN:
                drawActionScreen(g2);
                break;
            case CREDITS_SCREEN:
                drawCreditsScreen(g2);
                break;
            case HELP_SCREEN:
                drawHelpScreen(g2);
                break;
            case NEW_GAME_MENU:
                // This will be handled by NewGameUI
                break;
            case LOAD_GAME_MENU:
                // This will be handled by LoadGameUI
                break;
        }
    }
      private void drawMainMenu(Graphics2D g2) {
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        // Draw background
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        } else {
            // Fallback background
            g2.setColor(new Color(34, 139, 34)); // Forest green
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }
          // Draw title - commented out to remove white text overlay
        // g2.setColor(Color.WHITE);
        // g2.setFont(new Font("Arial", Font.BOLD, 36));
        // String title = "Spakbor Hills";
        // int titleWidth = g2.getFontMetrics().stringWidth(title);
        // g2.drawString(title, (screenWidth - titleWidth) / 2, 100);
        
        // Draw buttons using images
        BufferedImage[] buttonImages = {
            actionButtonImage,    // ACTION (0)
            creditsButtonImage,   // CREDITS (1)
            exitButtonImage,      // EXIT (2)
            helpButtonImage,      // HELP (3)
            loadButtonImage,      // LOAD (4)
            newGameButtonImage    // NEW GAME (5)
        };
        
        for (int i = 0; i < buttonBounds.length; i++) {
            Rectangle bounds = buttonBounds[i];
            
            // Determine if this button is selected
            int row = i / BUTTON_COLS;
            int col = i % BUTTON_COLS;
            boolean isSelected = (row == selectedButtonRow && col == selectedButtonCol);
            
            // Draw button image if available
            BufferedImage buttonImg = buttonImages[i];
            if (buttonImg != null) {
                // Draw the button image
                g2.drawImage(buttonImg, bounds.x, bounds.y, bounds.width, bounds.height, null);
                
                // Draw selection highlight
                if (isSelected) {
                    g2.setColor(new Color(255, 215, 0, 100)); // Golden yellow with transparency
                    g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
                    
                    // Draw selection border
                    g2.setColor(new Color(255, 215, 0)); // Golden yellow
                    g2.setStroke(new java.awt.BasicStroke(3));
                    g2.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
                    g2.setStroke(new java.awt.BasicStroke(1)); // Reset stroke
                }
            } else {
                // Fallback: draw button background if image not available
                if (isSelected) {
                    g2.setColor(new Color(255, 215, 0, 200)); // Golden yellow with transparency
                } else {
                    g2.setColor(new Color(139, 69, 19, 180)); // Brown with transparency
                }
                g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
                
                // Draw button border
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
                
                // Draw button text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                String buttonText = buttonNames[i];
                int textWidth = g2.getFontMetrics().stringWidth(buttonText);
                int textX = bounds.x + (bounds.width - textWidth) / 2;
                int textY = bounds.y + bounds.height / 2 + g2.getFontMetrics().getAscent() / 2 - 2;
                g2.drawString(buttonText, textX, textY);
            }
        }
        
        // Draw instructions
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        String instruction = "Use ARROW KEYS to navigate, ENTER to select, ESC to exit";
        int instructionWidth = g2.getFontMetrics().stringWidth(instruction);
        g2.drawString(instruction, (screenWidth - instructionWidth) / 2, screenHeight - 30);
    }
    
    private void drawActionScreen(Graphics2D g2) {
        drawInfoScreen(g2, actionImage, "Action Controls");
    }
    
    private void drawCreditsScreen(Graphics2D g2) {
        drawInfoScreen(g2, creditsImage, "Credits");
    }
    
    private void drawHelpScreen(Graphics2D g2) {
        drawInfoScreen(g2, helpImage, "Help");
    }
    
    private void drawInfoScreen(Graphics2D g2, BufferedImage image, String title) {
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        // Draw the info image
        if (image != null) {
            // Scale image to fit screen nicely
            double scale = Math.min(
                (double)(screenWidth * 0.8) / image.getWidth(),
                (double)(screenHeight * 0.8) / image.getHeight()
            );
            int scaledWidth = (int)(image.getWidth() * scale);
            int scaledHeight = (int)(image.getHeight() * scale);
            
            int x = (screenWidth - scaledWidth) / 2;
            int y = (screenHeight - scaledHeight) / 2;
            
            g2.drawImage(image, x, y, scaledWidth, scaledHeight, null);
        } else {
            // Fallback display
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, (screenWidth - titleWidth) / 2, screenHeight / 2);
        }
          // Draw back instruction
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String backText = "Press ESC to go back";
        int backWidth = g2.getFontMetrics().stringWidth(backText);
        g2.drawString(backText, (screenWidth - backWidth) / 2, screenHeight - 50);
    }
    
    /**
     * Handle keyboard input for menu navigation
     */
    public void handleKeyPress(KeyEvent e) {
        int code = e.getKeyCode();
        
        if (currentMenuState == MAIN_MENU) {
            // Handle main menu navigation
            switch (code) {
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
                    selectCurrentButton();
                    break;                case KeyEvent.VK_ESCAPE:
                    // Return to game (OPTIONS_STATE) instead of exiting
                    gamePanel.setGameState(GamePanel.OPTIONS_STATE);
                    break;
            }        } else {
            // Handle sub-screens (ACTION, CREDITS, HELP)
            switch (code) {
                case KeyEvent.VK_ESCAPE:
                    goBack();
                    break;
            }
        }
    }
    
    // Navigation methods
    public void moveSelectionUp() {
        selectedButtonRow = Math.max(0, selectedButtonRow - 1);
    }
    
    public void moveSelectionDown() {
        selectedButtonRow = Math.min(BUTTON_ROWS - 1, selectedButtonRow + 1);
    }
    
    public void moveSelectionLeft() {
        selectedButtonCol = Math.max(0, selectedButtonCol - 1);
    }
    
    public void moveSelectionRight() {
        selectedButtonCol = Math.min(BUTTON_COLS - 1, selectedButtonCol + 1);
    }
    
    public void selectCurrentButton() {
        int selectedIndex = selectedButtonRow * BUTTON_COLS + selectedButtonCol;
        
        switch (selectedIndex) {
            case 0: // ACTION
                currentMenuState = ACTION_SCREEN;
                break;
            case 1: // CREDITS
                currentMenuState = CREDITS_SCREEN;
                break;
            case 2: // EXIT
                System.exit(0);
                break;
            case 3: // HELP
                currentMenuState = HELP_SCREEN;
                break;
            case 4: // LOAD
                currentMenuState = LOAD_GAME_MENU;
                // Switch to LoadGameUI state in GamePanel
                gamePanel.setGameState(GamePanel.LOAD_GAME_STATE);
                break;
            case 5: // NEW GAME
                currentMenuState = NEW_GAME_MENU;
                // Switch to NewGameUI state in GamePanel
                gamePanel.setGameState(GamePanel.NEW_GAME_STATE);
                break;
        }
    }    public void goBack() {
        if (currentMenuState != MAIN_MENU) {
            currentMenuState = MAIN_MENU;
            // No need to change GamePanel state since we're still in the main menu system
            // The GamePanel state should remain MAIN_MENU_STATE throughout all main menu navigation
        }
    }
    
    // Getters
    public int getCurrentMenuState() {
        return currentMenuState;
    }
    
    public void setCurrentMenuState(int state) {
        this.currentMenuState = state;
    }
    
    public boolean isInMainMenu() {
        return currentMenuState == MAIN_MENU;
    }
}
