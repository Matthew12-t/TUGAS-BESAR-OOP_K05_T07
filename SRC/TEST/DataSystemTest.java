package SRC.TEST;

import SRC.DATA.GameData;
import SRC.ITEMS.Item;

/**
 * Simple test class to verify the DATA system functionality
 */
public class DataSystemTest {
    public static void main(String[] args) {
        System.out.println("=== Testing DATA System ===");
          // Test getting items from each category
        System.out.println("\n--- Testing Seeds ---");
        Item parsnipSeeds = GameData.addSeed("Parsnip Seeds", 15);
        if (parsnipSeeds != null) {
            System.out.println("✓ Successfully got: " + parsnipSeeds.getName());
        } else {
            System.out.println("✗ Failed to get Parsnip Seeds");
        }
        
        System.out.println("\n--- Testing Tools ---");
        Item hoe = GameData.addTool("Hoe", 1);
        if (hoe != null) {
            System.out.println("✓ Successfully got: " + hoe.getName());
        } else {
            System.out.println("✗ Failed to get Hoe");
        }
        
        System.out.println("\n--- Testing Fish ---");
        Item carp = GameData.addFish("Carp", 3);
        if (carp != null) {
            System.out.println("✓ Successfully got: " + carp.getName());
        } else {
            System.out.println("✗ Failed to get Carp");
        }
        
        System.out.println("\n--- Testing Crops ---");
        Item parsnip = GameData.addCrop("Parsnip", 5);
        if (parsnip != null) {
            System.out.println("✓ Successfully got: " + parsnip.getName());
        } else {
            System.out.println("✗ Failed to get Parsnip");
        }
          System.out.println("\n--- Testing Food ---");
        Item bread = GameData.addFood("Bread", 10);
        if (bread != null) {
            System.out.println("✓ Successfully got: " + bread.getName());
        } else {
            System.out.println("✗ Failed to get Bread");
        }
          // Test starter items (if available)
        System.out.println("\n--- Testing Item Retrieval ---");
        Item retrievedSeed = GameData.getItem("Parsnip Seeds", "Seed");
        if (retrievedSeed != null) {
            System.out.println("✓ Successfully retrieved: " + retrievedSeed.getName());
        } else {
            System.out.println("✗ Failed to retrieve Parsnip Seeds");
        }
        
        // Test item existence checking
        System.out.println("\n--- Testing Item Existence ---");
        System.out.println("Has 'Potato Seeds': " + GameData.hasItem("Potato Seeds"));
        System.out.println("Has 'Nonexistent Item': " + GameData.hasItem("Nonexistent Item"));
        
        System.out.println("\n=== DATA System Test Complete ===");
    }
}
