package SRC.TEST;

import SRC.MAIN.GamePanel;
import SRC.UI.StoreUI;
import SRC.DATA.GameData;

/**
 * Test class to verify Store UI integration with GamePanel
 */
public class StoreUIIntegrationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Store UI Integration Test ===");
        
        try {
            // Test 1: GamePanel initialization with StoreUI
            testGamePanelStoreUIInitialization();
            
            // Test 2: Verify StoreUI getter method
            testStoreUIGetterMethod();
            
            // Test 3: Verify GamePanel state constants
            testGameStateConstants();
              System.out.println("\n[SUCCESS] All Store UI integration tests passed!");
            
        } catch (Exception e) {
            System.err.println("[FAILURE] Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testGamePanelStoreUIInitialization() {
        System.out.println("\n1. Testing GamePanel StoreUI initialization...");
        
        GamePanel gamePanel = new GamePanel();
        
        // Verify StoreUI is not null
        StoreUI storeUI = gamePanel.getStoreUI();
        if (storeUI == null) {
            throw new RuntimeException("StoreUI is null - initialization failed!");
        }
        
        System.out.println("   [PASS] StoreUI successfully initialized in GamePanel");
    }
    
    private static void testStoreUIGetterMethod() {
        System.out.println("\n2. Testing StoreUI getter method...");
        
        GamePanel gamePanel = new GamePanel();
        StoreUI storeUI = gamePanel.getStoreUI();
        
        // Verify getter returns the same instance
        StoreUI storeUI2 = gamePanel.getStoreUI();
        if (storeUI != storeUI2) {
            throw new RuntimeException("StoreUI getter returns different instances!");
        }
        
        System.out.println("   [PASS] StoreUI getter method works correctly");
    }
    
    private static void testGameStateConstants() {
        System.out.println("\n3. Testing GamePanel state constants...");
        
        // Verify STORE_STATE constant exists and has correct value
        int storeState = GamePanel.STORE_STATE;
        if (storeState != 5) {
            throw new RuntimeException("STORE_STATE constant has unexpected value: " + storeState);
        }
        
        // Test state setting
        GamePanel gamePanel = new GamePanel();
        gamePanel.setGameState(GamePanel.STORE_STATE);
        
        if (gamePanel.getGameState() != GamePanel.STORE_STATE) {
            throw new RuntimeException("Failed to set game state to STORE_STATE");
        }
        
        System.out.println("   [PASS] STORE_STATE constant and state management working correctly");
    }
}
