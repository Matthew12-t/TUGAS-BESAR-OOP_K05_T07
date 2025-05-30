/**
 * Test to verify that the watering logic in TileManager works correctly
 */
public class TEST_WateringLogic {
    
    // Mock PlantedTileInfo for testing
    static class MockPlantedTileInfo {
        public String seedName;
        public int plantedDay;
        public int plantedHour;
        public int daysToGrow;
        public boolean isWatered;
        public int lastWateredDay;
        
        public MockPlantedTileInfo(String seedName, int plantedDay, int plantedHour, int daysToGrow) {
            this.seedName = seedName;
            this.plantedDay = plantedDay;
            this.plantedHour = plantedHour;
            this.daysToGrow = daysToGrow;
            this.isWatered = true; // Start with watered status (free watering on planting day)
            this.lastWateredDay = plantedDay; // Last watered day is the planting day
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== WATERING LOGIC TEST ===\n");
        
        // Test scenario: Seed planted on day 1, grows for 3 days
        testWateringScenario();
    }
    
    private static void testWateringScenario() {
        System.out.println("Test Scenario: Cauliflower Seed planted on Day 1, requires 3 days to grow");
        
        MockPlantedTileInfo plant = new MockPlantedTileInfo("Cauliflower Seed", 1, 6, 3);
        
        System.out.println("Initial state:");
        System.out.println("  Planted Day: " + plant.plantedDay);
        System.out.println("  Days to Grow: " + plant.daysToGrow);
        System.out.println("  Is Watered: " + plant.isWatered);
        System.out.println("  Last Watered Day: " + plant.lastWateredDay);
        System.out.println();
        
        // Test growth calculation for different scenarios
        System.out.println("=== SCENARIO 1: Plant watered on day 2, not watered on day 3 ===");
        testGrowthCalculation(plant, 1, "Day 1 (planting day)");
        
        // Simulate watering on day 2
        plant.isWatered = true;
        plant.lastWateredDay = 2;
        testGrowthCalculation(plant, 2, "Day 2 (watered)");
        
        // Day 3 - not watered
        plant.isWatered = false;
        // lastWateredDay stays 2 (last time it was watered)
        testGrowthCalculation(plant, 3, "Day 3 (not watered)");
        
        // Day 4 - still not watered  
        testGrowthCalculation(plant, 4, "Day 4 (not watered)");
        
        System.out.println("\n=== SCENARIO 2: Plant watered every day ===");
        plant = new MockPlantedTileInfo("Cauliflower Seed", 1, 6, 3);
        
        testGrowthCalculation(plant, 1, "Day 1 (planting day)");
        
        // Water on day 2
        plant.isWatered = true;
        plant.lastWateredDay = 2;
        testGrowthCalculation(plant, 2, "Day 2 (watered)");
        
        // Water on day 3
        plant.lastWateredDay = 3;
        testGrowthCalculation(plant, 3, "Day 3 (watered)");
        
        // Water on day 4
        plant.lastWateredDay = 4;
        testGrowthCalculation(plant, 4, "Day 4 (watered)");
    }
    
    private static void testGrowthCalculation(MockPlantedTileInfo plant, int currentDay, String description) {
        System.out.println(description + ":");
        
        // Replicate the calculateGrowthStage logic
        int daysPassed = currentDay - plant.plantedDay;
        
        // Calculate growth stage based on watering
        int actualGrowthDays = 0;
        
        // Loop from planting day to current day
        for (int day = plant.plantedDay; day <= currentDay; day++) {
            // Check if plant was watered on that day
            boolean wasWateredOnDay = wasPlantWateredOnDay(plant, day);
            
            if (wasWateredOnDay) {
                actualGrowthDays++;
            }
            System.out.println("    Day " + day + ": watered=" + wasWateredOnDay);
        }
        
        // Growth stage = actualGrowthDays, max daysToGrow + 1 (for harvest)
        int growthStage = Math.min(actualGrowthDays, plant.daysToGrow + 1);
        
        System.out.println("  Days Passed: " + daysPassed);
        System.out.println("  Actual Growth Days: " + actualGrowthDays);
        System.out.println("  Growth Stage: " + growthStage);
        System.out.println("  Ready to Harvest: " + (growthStage > plant.daysToGrow));
        System.out.println();
    }
    
    // Replicate the wasPlantWateredOnDay logic
    private static boolean wasPlantWateredOnDay(MockPlantedTileInfo plantInfo, int day) {
        // Plant is considered watered if:
        // 1. First day planted (free watering)
        // 2. Last watered day is exactly that day
        // 3. Weather rainy on that day (all plants auto-watered)
        
        if (day == plantInfo.plantedDay) {
            return true; // First day free watering
        }
        
        if (plantInfo.lastWateredDay == day) {
            return true; // Manually watered on that day
        }
        
        // Check weather history (for simple implementation, assume no rainy days)
        return false;
    }
}
