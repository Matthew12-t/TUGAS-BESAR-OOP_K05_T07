package SRC.MAIN;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles arrow key (up/down) and enter key inputs for navigation and selection.
 * Primarily used for menus and UI navigation.
 */
public class ArrowKeyHandler implements KeyListener {
    
    // Track key states
    private boolean upPressed;
    private boolean downPressed;
    private boolean enterPressed;
    
    /**
     * Constructor for ArrowKeyHandler
     */
    public ArrowKeyHandler() {
        // Empty constructor
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required by KeyListener interface
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        // Handle arrow up
        if (code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        
        // Handle arrow down
        if (code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        
        // Handle enter key
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        // Reset key states when released
        if (code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        
        if (code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
    }
    
    /**
     * Check if up arrow is pressed
     * @return true if up arrow is pressed
     */
    public boolean isUpPressed() {
        return upPressed;
    }
    
    /**
     * Check if down arrow is pressed
     * @return true if down arrow is pressed
     */
    public boolean isDownPressed() {
        return downPressed;
    }
    
    /**
     * Check if enter is pressed
     * @return true if enter is pressed
     */
    public boolean isEnterPressed() {
        return enterPressed;
    }
}