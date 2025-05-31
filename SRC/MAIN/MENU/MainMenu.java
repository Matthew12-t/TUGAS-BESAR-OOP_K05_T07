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

public class MainMenu {
    private GamePanel gamePanel;
    
    
    public static final int MAIN_MENU = 0;
    public static final int ACTION_SCREEN = 1;
    public static final int CREDITS_SCREEN = 2;
    public static final int HELP_SCREEN = 3;
    public static final int NEW_GAME_MENU = 4;
    public static final int LOAD_GAME_MENU = 5;
    
    private int currentMenuState = MAIN_MENU;
    
    
    private final int BUTTON_ROWS = 2;
    private final int BUTTON_COLS = 3;
    private final int BUTTON_WIDTH = 150;
    private final int BUTTON_HEIGHT = 60;
    private final int BUTTON_SPACING_X = 20;
    private final int BUTTON_SPACING_Y = 20;
    
    
    private int selectedButtonRow = 0;
    private int selectedButtonCol = 0;
    private Rectangle[] buttonBounds;    
    private BufferedImage backgroundImage;
    private BufferedImage actionImage;
    private BufferedImage creditsImage;
    private BufferedImage helpImage;
    
    
    private BufferedImage actionButtonImage;
    private BufferedImage creditsButtonImage;
    private BufferedImage exitButtonImage;
    private BufferedImage helpButtonImage;
    private BufferedImage loadButtonImage;
    private BufferedImage newGameButtonImage;
    
    
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
            backgroundImage = ImageIO.read(new File("RES/MAINMENU/mainmenu.png"));
            actionImage = ImageIO.read(new File("RES/MAINMENU/action1.png"));      
            creditsImage = ImageIO.read(new File("RES/MAINMENU/credits1.png"));
            helpImage = ImageIO.read(new File("RES/MAINMENU/help1.png"));
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
        
        
        int totalGridWidth = (BUTTON_COLS * BUTTON_WIDTH) + ((BUTTON_COLS - 1) * BUTTON_SPACING_X);
        int totalGridHeight = (BUTTON_ROWS * BUTTON_HEIGHT) + ((BUTTON_ROWS - 1) * BUTTON_SPACING_Y);
        
        int startX = (screenWidth - totalGridWidth) / 2;
        int startY = (screenHeight - totalGridHeight) / 2 + 180; 
        
        
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
                
                break;
            case LOAD_GAME_MENU:
                
                break;
        }
    }
      private void drawMainMenu(Graphics2D g2) {
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        } else {
            
            g2.setColor(new Color(34, 139, 34)); 
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }
          
        BufferedImage[] buttonImages = {
            actionButtonImage,    
            creditsButtonImage,   
            exitButtonImage,      
            helpButtonImage,      
            loadButtonImage,      
            newGameButtonImage    
        };
        
        for (int i = 0; i < buttonBounds.length; i++) {
            Rectangle bounds = buttonBounds[i];
            
            
            int row = i / BUTTON_COLS;
            int col = i % BUTTON_COLS;
            boolean isSelected = (row == selectedButtonRow && col == selectedButtonCol);
            
            
            BufferedImage buttonImg = buttonImages[i];
            if (buttonImg != null) {
                
                g2.drawImage(buttonImg, bounds.x, bounds.y, bounds.width, bounds.height, null);
                
                
                if (isSelected) {
                    g2.setColor(new Color(255, 215, 0, 100)); 
                    g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
                    
                    
                    g2.setColor(new Color(255, 215, 0)); 
                    g2.setStroke(new java.awt.BasicStroke(3));
                    g2.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
                    g2.setStroke(new java.awt.BasicStroke(1)); 
                }
            } else {
                
                if (isSelected) {
                    g2.setColor(new Color(255, 215, 0, 200)); 
                } else {
                    g2.setColor(new Color(139, 69, 19, 180)); 
                }
                g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
                
                
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 15, 15);
                
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                String buttonText = buttonNames[i];
                int textWidth = g2.getFontMetrics().stringWidth(buttonText);
                int textX = bounds.x + (bounds.width - textWidth) / 2;
                int textY = bounds.y + bounds.height / 2 + g2.getFontMetrics().getAscent() / 2 - 2;
                g2.drawString(buttonText, textX, textY);
            }
        }
        
        
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
        
        
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        
        if (image != null) {
            
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
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, (screenWidth - titleWidth) / 2, screenHeight / 2);
        }
          
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        String backText = "Press ESC to go back";
        int backWidth = g2.getFontMetrics().stringWidth(backText);
        g2.drawString(backText, (screenWidth - backWidth) / 2, screenHeight - 50);
    }
    

    public void handleKeyPress(KeyEvent e) {
        int code = e.getKeyCode();
        
        if (currentMenuState == MAIN_MENU) {
            
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
                    
                    gamePanel.setGameState(GamePanel.OPTIONS_STATE);
                    break;
            }        } else {
            
            switch (code) {
                case KeyEvent.VK_ESCAPE:
                    goBack();
                    break;
            }
        }
    }
    
    
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
            case 0: 
                currentMenuState = ACTION_SCREEN;
                break;
            case 1: 
                currentMenuState = CREDITS_SCREEN;
                break;
            case 2: 
                System.exit(0);
                break;
            case 3: 
                currentMenuState = HELP_SCREEN;
                break;
            case 4: 
                currentMenuState = LOAD_GAME_MENU;
                
                gamePanel.setGameState(GamePanel.LOAD_GAME_STATE);
                break;
            case 5: 
                currentMenuState = NEW_GAME_MENU;
                
                gamePanel.setGameState(GamePanel.NEW_GAME_STATE);
                break;
        }
    }    public void goBack() {
        if (currentMenuState != MAIN_MENU) {
            currentMenuState = MAIN_MENU;
            
            
        }
    }
    
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
