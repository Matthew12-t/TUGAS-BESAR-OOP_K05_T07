import SRC.ITEMS.Fish;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.GameTime;

public class TestCompleteSystem {
    public static void main(String[] args) {
        System.out.println("Testing Complete Fish System...");
        System.out.println("================================");
        
        // Test Halibut with multiple time ranges
        Fish halibut = new Fish("Halibut", "Regular", "Ocean", Season.ANY, Weather.ANY, "6-11,19-2");
        System.out.println("Halibut sell price: " + halibut.getSellPrice() + " gold");
        System.out.println("Halibut energy value: " + halibut.getEnergyValue());
        
        // Test other fish types for energy
        Fish commonFish = new Fish("Sardine", "Common", "Ocean", Season.ANY, Weather.ANY, 
                                   new GameTime(6, 0), new GameTime(19, 0));
        Fish legendaryFish = new Fish("Legend", "Legendary", "Mountain Lake", Season.SPRING, Weather.RAINY, 
                                      new GameTime(6, 0), new GameTime(20, 0));
        
        System.out.println("\nTesting Energy System:");
        System.out.println("Common Fish (Sardine) energy: " + commonFish.getEnergyValue());
        System.out.println("Legendary Fish (Legend) energy: " + legendaryFish.getEnergyValue());
        
        // Test basic fish functionality without Player dependency
        System.out.println("\nTesting Fish Properties:");
        
        // Test multiple fish types for pricing
        Fish[] testFish = {
            new Fish("Carp", "Common", "Forest River", Season.ANY, Weather.ANY, new GameTime(6, 0), new GameTime(19, 0)),
            new Fish("Salmon", "Regular", "Forest River", Season.FALL, Weather.ANY, new GameTime(6, 0), new GameTime(19, 0)),
            new Fish("Tuna", "Regular", "Ocean", Season.SUMMER, Weather.ANY, new GameTime(6, 0), new GameTime(19, 0)),
            halibut,
            commonFish,
            legendaryFish
        };
        
        System.out.println("\nFish Pricing and Energy Summary:");
        for (Fish fish : testFish) {
            System.out.println(fish.getName() + " (" + fish.getRarity() + "): " + 
                             fish.getSellPrice() + " gold, " + fish.getEnergyValue() + " energy");
        }
        
        // Verify all requirements
        boolean halibutPriceCorrect = halibut.getSellPrice() == 40;
        boolean energySystemCorrect = true;
        
        // Check that all fish provide exactly 1 energy
        for (Fish fish : testFish) {
            if (fish.getEnergyValue() != 1) {
                energySystemCorrect = false;
                break;
            }
        }
        
        // Test that Halibut correctly calculates based on multiple time ranges
        System.out.println("\nHalibut Time Range Test:");
        System.out.println("Halibut time ranges: 6-11,19-2");
        System.out.println("Expected total hours: 12 (5 hours + 7 hours)");
        System.out.println("Expected sell price calculation: (4/4) × (24/12) × (2/2) × (4/1) × 5 = 40");
        System.out.println("Actual sell price: " + halibut.getSellPrice());
        
        System.out.println("\n=== TEST RESULTS ===");
        System.out.println("Halibut price (should be 40): " + (halibutPriceCorrect ? "PASS" : "FAIL"));
        System.out.println("Energy system (all fish give 1 energy): " + (energySystemCorrect ? "PASS" : "FAIL"));
        
        boolean allTestsPassed = halibutPriceCorrect && energySystemCorrect;
        System.out.println("Overall test: " + (allTestsPassed ? "PASS" : "FAIL"));
        
        if (allTestsPassed) {
            System.out.println("\n✓ All fish system requirements implemented successfully!");
            System.out.println("✓ Halibut correctly calculates sell price with multiple time ranges");
            System.out.println("✓ All fish provide exactly 1 energy point when consumed");
        }
    }
}
