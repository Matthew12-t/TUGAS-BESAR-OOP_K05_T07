/**
 * Test to simulate the complete farming cycle with hour-based growth
 */
public class TEST_CompleteFarmingCycle {
    
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
        System.out.println("=== COMPLETE FARMING CYCLE TEST ===\n");
        
        // Test scenario: Player plants Cauliflower Seed at 8 AM on Monday (Day 1)
        // Cauliflower requires 3 days to grow
        testCompleteFarmingCycle();
    }
    
    private static void testCompleteFarmingCycle() {
        System.out.println("Scenario: Player plants Cauliflower Seed on Monday at 8 AM");
        System.out.println("Cauliflower requires 3 days to grow (stages 1->2->3->4 where 4 = harvest ready)");
        System.out.println();
        
        MockPlantedTileInfo plant = new MockPlantedTileInfo("Cauliflower Seed", 1, 8, 3);
        
        // Monday
        System.out.println("=== MONDAY (Day 1) ===");
        testTimeProgression(plant, 1, 8, "Monday 8 AM - Just planted");
        testTimeProgression(plant, 1, 12, "Monday 12 PM - 4 hours after planting");
        testTimeProgression(plant, 1, 18, "Monday 6 PM - 10 hours after planting");
        
        // Tuesday
        System.out.println("\n=== TUESDAY (Day 2) ===");
        testTimeProgression(plant, 2, 6, "Tuesday 6 AM - 22 hours after planting");
        testTimeProgression(plant, 2, 8, "Tuesday 8 AM - EXACTLY 24 hours after planting");
        
        // Player waters the plant on Tuesday
        System.out.println(">> Player waters the plant on Tuesday <<");
        plant.isWatered = true;
        plant.lastWateredDay = 2;
        
        testTimeProgression(plant, 2, 12, "Tuesday 12 PM - Watered, 28 hours after planting");
        testTimeProgression(plant, 2, 18, "Tuesday 6 PM - Watered, 34 hours after planting");
        
        // Wednesday  
        System.out.println("\n=== WEDNESDAY (Day 3) ===");
        testTimeProgression(plant, 3, 6, "Wednesday 6 AM - 46 hours after planting");
        testTimeProgression(plant, 3, 8, "Wednesday 8 AM - EXACTLY 48 hours after planting");
        
        // Player waters the plant on Wednesday
        System.out.println(">> Player waters the plant on Wednesday <<");
        plant.isWatered = true;
        plant.lastWateredDay = 3;
        
        testTimeProgression(plant, 3, 12, "Wednesday 12 PM - Watered, 52 hours after planting");
        
        // Thursday
        System.out.println("\n=== THURSDAY (Day 4) ===");
        testTimeProgression(plant, 4, 6, "Thursday 6 AM - 70 hours after planting");
        testTimeProgression(plant, 4, 8, "Thursday 8 AM - EXACTLY 72 hours after planting");
        
        // Player waters the plant on Thursday  
        System.out.println(">> Player waters the plant on Thursday <<");
        plant.isWatered = true;
        plant.lastWateredDay = 4;
        
        testTimeProgression(plant, 4, 12, "Thursday 12 PM - Watered, 76 hours after planting");
        
        // Friday
        System.out.println("\n=== FRIDAY (Day 5) ===");
        testTimeProgression(plant, 5, 8, "Friday 8 AM - EXACTLY 96 hours after planting");
        
        System.out.println("\n=== SUMMARY ===");
        System.out.println("Expected progression for Cauliflower (3 days to grow):");
        System.out.println("- Stage 1: Monday 8 AM - Tuesday 7:59 AM (0-23 hours)");
        System.out.println("- Stage 2: Tuesday 8 AM - Wednesday 7:59 AM (24-47 hours, if watered on Day 1)");  
        System.out.println("- Stage 3: Wednesday 8 AM - Thursday 7:59 AM (48-71 hours, if watered on Day 2)");
        System.out.println("- Stage 4: Thursday 8 AM onwards (72+ hours, if watered on Day 3) - HARVEST READY!");
    }
    
    private static void testTimeProgression(MockPlantedTileInfo plant, int currentDay, int currentHour, String description) {
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
            
            // Stop if we've reached maximum growth stage
            if (growthStage > plant.daysToGrow + 1) {
                break;
            }
        }
        
        // Cap at maximum growth stage
        growthStage = Math.min(growthStage, plant.daysToGrow + 1);
        
        boolean readyToHarvest = (growthStage > plant.daysToGrow);
        String harvestStatus = readyToHarvest ? " 🌾 HARVEST READY!" : "";
        
        System.out.printf("%-40s | Stage %d | %2d hrs%s%n", 
                         description, growthStage, totalHoursPassed, harvestStatus);
    }
    
    // Replicate the wasPlantWateredOnDay logic
    private static boolean wasPlantWateredOnDay(MockPlantedTileInfo plantInfo, int day) {
        if (day == plantInfo.plantedDay) {
            return true; // First day free watering
        }
        
        if (plantInfo.lastWateredDay == day) {
            return true; // Manually watered on that day
        }
        
        return false;
    }
}
