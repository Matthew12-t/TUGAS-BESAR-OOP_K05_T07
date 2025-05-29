import SRC.DATA.ShippingBinData;
import SRC.ITEMS.Item;
import SRC.ITEMS.Crop;
import SRC.DATA.CropData;

/**
 * Test class to verify ShippingBinData functionality
 */
public class TEST_ShippingBin {
    public static void main(String[] args) {
        System.out.println("=== Testing ShippingBinData ===");
        
        // Create shipping bin data instance
        ShippingBinData shippingBin = new ShippingBinData();
        
        // Test 1: Add some items
        System.out.println("\nTest 1: Adding items to shipping bin");
        
        // Create some test items (crops)
        Crop corn = CropData.getCorn();
        Crop tomato = CropData.getTomato();
        Crop potato = CropData.getPotato();
        
        // Add items to shipping bin
        shippingBin.addItem(corn, 5);
        shippingBin.addItem(tomato, 3);
        shippingBin.addItem(potato, 2);
        
        System.out.println("Added: 5 Corn, 3 Tomato, 2 Potato");
        
        // Test 2: Check total value
        System.out.println("\nTest 2: Calculating total value");
        int totalValue = shippingBin.calculateTotalIncome();
        System.out.println("Total value in shipping bin: " + totalValue + " coins");
        
        // Test 3: Get shipping items
        System.out.println("\nTest 3: Listing items in shipping bin");
        var items = shippingBin.getShippingItems();
        for (int i = 0; i < items.size(); i++) {
            var shippingItem = items.get(i);
            System.out.println("- " + shippingItem.item.getName() + " x" + shippingItem.quantity + 
                             " (value: " + shippingBin.getItemSellPrice(shippingItem.item.getName()) + " each)");
        }
        
        // Test 4: Clear shipping bin
        System.out.println("\nTest 4: Clearing shipping bin");
        shippingBin.clearShippingBin();
        System.out.println("Items after clearing: " + shippingBin.getShippingItems().size());
        System.out.println("Total value after clearing: " + shippingBin.calculateTotalIncome() + " coins");
        
        System.out.println("\n=== ShippingBinData Test Complete ===");
    }
}
