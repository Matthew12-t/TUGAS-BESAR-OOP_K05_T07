/**
 * Test to verify that the hour-based watering logic works correctly
 * Growth should happen after 24 hours from planting (if watered)
 */
public class TEST_HourBasedGrowth {
    
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
        System.out.println("=== HOUR-BASED GROWTH LOGIC TEST ===\n");
        
        // Test scenario: Seed planted on day 1 at 6 AM, grows for 3 days
        testHourBasedGrowth();
    }
    
    private static void testHourBasedGrowth() {
        System.out.println("Test Scenario: Cauliflower Seed planted on Day 1 at 6 AM, requires 3 days to grow");
        
        MockPlantedTileInfo plant = new MockPlantedTileInfo("Cauliflower Seed", 1, 6, 3);
        
        System.out.println("Initial state:");
        System.out.println("  Planted Day: " + plant.plantedDay + " at " + plant.plantedHour + ":00");
        System.out.println("  Days to Grow: " + plant.daysToGrow);
        System.out.println("  Is Watered: " + plant.isWatered);
        System.out.println("  Last Watered Day: " + plant.lastWateredDay);
        System.out.println();
        
        // Test different time scenarios
        System.out.println("=== SCENARIO 1: Plant watered on planting day, check growth over time ===");
        
        // Day 1, 6 AM (just planted)
        testGrowthAtTime(plant, 1, 6, "Day 1, 6 AM (just planted)");
        
        // Day 1, 12 PM (6 hours after planting)
        testGrowthAtTime(plant, 1, 12, "Day 1, 12 PM (6 hours after planting)");
        
        // Day 1, 6 PM (12 hours after planting)
        testGrowthAtTime(plant, 1, 18, "Day 1, 6 PM (12 hours after planting)");
        
        // Day 2, 5 AM (23 hours after planting)
        testGrowthAtTime(plant, 2, 5, "Day 2, 5 AM (23 hours after planting)");
        
        // Day 2, 6 AM (exactly 24 hours after planting)
        testGrowthAtTime(plant, 2, 6, "Day 2, 6 AM (exactly 24 hours after planting)");
        
        // Day 2, 7 AM (25 hours after planting)
        testGrowthAtTime(plant, 2, 7, "Day 2, 7 AM (25 hours after planting)");
        
        System.out.println("\n=== SCENARIO 2: Plant watered on day 2, check second growth stage ===");
        
        // Water the plant on day 2
        plant.isWatered = true;
        plant.lastWateredDay = 2;
        
        // Day 3, 6 AM (exactly 48 hours after planting, watered on day 2)
        testGrowthAtTime(plant, 3, 6, "Day 3, 6 AM (48 hours after planting, watered on day 2)");
        
        System.out.println("\n=== SCENARIO 3: Plant NOT watered on day 2, should not advance ===");
        
        // Reset and test without watering on day 2
        plant = new MockPlantedTileInfo("Cauliflower Seed", 1, 6, 3);
        // Don't water on day 2 (lastWateredDay stays 1)
        
        // Day 3, 6 AM (48 hours after planting, NOT watered on day 2)
        testGrowthAtTime(plant, 3, 6, "Day 3, 6 AM (48 hours after planting, NOT watered on day 2)");
    }
    
    private static void testGrowthAtTime(MockPlantedTileInfo plant, int currentDay, int currentHour, String description) {
        System.out.println(description + ":");
        
        // Calculate total hours passed since planting
        int totalHoursPassed = ((currentDay - plant.plantedDay) * 24) + (currentHour - plant.plantedHour);
        
        // Calculate how many 24-hour periods have passed
        int full24HourPeriods = totalHoursPassed / 24;
        
        // Growth stage calculation (replicate the new logic)
        int growthStage = 1; // Start at stage 1 (newly planted)
        
        // For each completed 24-hour period, check if plant was watered
        for (int period = 0; period < full24HourPeriods; period++) {
            // Calculate which day this 24-hour period corresponds to
            int dayToCheck = plant.plantedDay + period;
            
            // Check if plant was watered during this period
            boolean wasWateredOnDay = wasPlantWateredOnDay(plant, dayToCheck);
            
            if (wasWateredOnDay) {
                growthStage++;
            }
            
            System.out.println("    Period " + period + " (Day " + dayToCheck + "): watered=" + wasWateredOnDay + 
                             ", growth stage after period: " + growthStage);
            
            // Stop if we've reached maximum growth stage
            if (growthStage > plant.daysToGrow + 1) {
                break;
            }
        }
        
        // Cap at maximum growth stage
        growthStage = Math.min(growthStage, plant.daysToGrow + 1);
        
        System.out.println("  Total Hours Passed: " + totalHoursPassed);
        System.out.println("  Full 24-Hour Periods: " + full24HourPeriods);
        System.out.println("  Final Growth Stage: " + growthStage);
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
