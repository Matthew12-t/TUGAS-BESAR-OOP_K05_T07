import SRC.STORE.Store;
import SRC.STORE.StoreManager;
import SRC.ITEMS.Item;

/**
 * Test class to verify the Store and StoreManager integration
 */
public class TEST_StoreIntegration {
    public static void main(String[] args) {
        System.out.println("=== Store Integration Test ===");
        
        try {
            // Test Store initialization
            Store<Item> store = new Store<>("Test Store");
            System.out.println("✓ Store created successfully: " + store.getStoreName());
            
            // Test StoreManager initialization
            StoreManager<Item> storeManager = store.getStoreManager();
            System.out.println("✓ StoreManager created successfully");
            
            // Test category retrieval
            String[] categories = storeManager.getCategories();
            System.out.println("✓ Available categories: " + categories.length);
            for (String category : categories) {
                System.out.println("  - " + category);
            }
            
            // Test items in each category
            for (String category : categories) {
                var items = storeManager.getItemsByCategory(category);
                System.out.println("✓ " + category + " has " + items.size() + " items");
                
                // Test first item in category if available
                if (!items.isEmpty()) {
                    Item firstItem = (Item) items.get(0);
                    int price = store.getBuyPrice(firstItem);
                    System.out.println("  - First item: " + firstItem.getName() + " (Price: " + price + " gold)");
                }
            }
            
            System.out.println("\n=== Integration Test PASSED ===");
            
        } catch (Exception e) {
            System.err.println("=== Integration Test FAILED ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
