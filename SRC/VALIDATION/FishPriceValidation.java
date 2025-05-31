package SRC.VALIDATION;

import SRC.DATA.FishData;
import SRC.ITEMS.Fish;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import java.util.Map;

/**
 * Driver file to validate fish selling prices according to the formula:
 * Fish Price = 4^(seasons) × 2^(hours) × 2^(weather) × 4^(locations) × C
 * Where C = 10 for common, 5 for regular, 25 for legendary fish
 */
public class FishPriceValidation {
    
    // Base constants for price calculation
    private static final int COMMON_MULTIPLIER = 10;
    private static final int REGULAR_MULTIPLIER = 5;
    private static final int LEGENDARY_MULTIPLIER = 25;
    
    // Season values (4^seasons)
    private static final int SPRING_VALUE = 1;  // 4^0
    private static final int SUMMER_VALUE = 4;  // 4^1
    private static final int FALL_VALUE = 16;   // 4^2
    private static final int WINTER_VALUE = 64; // 4^3
    private static final int ANY_SEASON_VALUE = 1; // Default for ANY season
    
    // Weather values (2^weather)
    private static final int SUNNY_VALUE = 1;   // 2^0
    private static final int RAINY_VALUE = 2;   // 2^1
    private static final int ANY_WEATHER_VALUE = 1; // Default for ANY weather
    
    // Time values (2^hours) - simplified to morning/evening
    private static final int MORNING_VALUE = 1; // 2^0
    private static final int EVENING_VALUE = 2; // 2^1
    private static final int ALLDAY_VALUE = 1;  // Default for all day
    
    // Location values (4^locations)
    private static final int POND_VALUE = 1;         // 4^0
    private static final int FOREST_RIVER_VALUE = 4; // 4^1
    private static final int MOUNTAIN_LAKE_VALUE = 16; // 4^2
    private static final int OCEAN_VALUE = 64;       // 4^3
    
    public static void main(String[] args) {
        System.out.println("=== FISH PRICE VALIDATION ===");
        System.out.println("Formula: 4^(seasons) × 2^(hours) × 2^(weather) × 4^(locations) × C");
        System.out.println("C values: Common=10, Regular=5, Legendary=25");
        System.out.println();
        
        validateAllFishPrices();
    }
    
    /**
     * Validate prices for all fish in the game
     */
    public static void validateAllFishPrices() {
        Map<String, Fish> allFish = FishData.getAllFish();
        int totalFish = 0;
        int correctPrices = 0;
        int incorrectPrices = 0;
        
        System.out.println("Fish Name\t\tType\t\tLocation\t\tExpected\tActual\t\tStatus");
        System.out.println("-----------------------------------------------------------------------------------------");
        
        for (Map.Entry<String, Fish> entry : allFish.entrySet()) {
            String fishKey = entry.getKey();
            Fish fish = entry.getValue();
            
            int expectedPrice = calculateExpectedPrice(fish);
            int actualPrice = fish.getValue();
            boolean isCorrect = (expectedPrice == actualPrice);
            
            if (isCorrect) {
                correctPrices++;
            } else {
                incorrectPrices++;
            }
            totalFish++;
            
            String status = isCorrect ? "✓ CORRECT" : "✗ INCORRECT";
            System.out.printf("%-20s\t%-10s\t%-15s\t%d\t\t%d\t\t%s%n", 
                fish.getName(), fish.getType(), fish.getLocation(), 
                expectedPrice, actualPrice, status);
        }
        
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("Summary: %d total fish, %d correct prices, %d incorrect prices%n", 
            totalFish, correctPrices, incorrectPrices);
        System.out.printf("Accuracy: %.1f%%%n", (correctPrices * 100.0) / totalFish);
    }
    
    /**
     * Calculate expected price based on fish properties
     */
    private static int calculateExpectedPrice(Fish fish) {
        int seasonValue = getSeasonValue(fish.getSeason());
        int timeValue = getTimeValue(fish);
        int weatherValue = getWeatherValue(fish.getWeather());
        int locationValue = getLocationValue(fish.getLocation());
        int typeMultiplier = getTypeMultiplier(fish.getType());
        
        return seasonValue * timeValue * weatherValue * locationValue * typeMultiplier;
    }
    
    /**
     * Get season multiplier value
     */
    private static int getSeasonValue(Season season) {
        if (season == null) return ANY_SEASON_VALUE;
        
        switch (season) {
            case SPRING: return SPRING_VALUE;
            case SUMMER: return SUMMER_VALUE;
            case FALL: return FALL_VALUE;
            case WINTER: return WINTER_VALUE;
            case ANY: return ANY_SEASON_VALUE;
            default: return ANY_SEASON_VALUE;
        }
    }
    
    /**
     * Get time multiplier value based on fish availability
     */
    private static int getTimeValue(Fish fish) {
        // This is simplified - in reality you'd check the actual time range
        // For now, we'll use the fish name or key to determine if it's morning/evening specific
        
        // Check if this is a time-specific variant
        if (fish.getName().contains("Morning") || 
            (fish.getStartTime() != null && fish.getStartTime().getHour() >= 6 && fish.getStartTime().getHour() < 12)) {
            return MORNING_VALUE;
        } else if (fish.getName().contains("Evening") || 
                  (fish.getStartTime() != null && fish.getStartTime().getHour() >= 18)) {
            return EVENING_VALUE;
        } else {
            return ALLDAY_VALUE;
        }
    }
    
    /**
     * Get weather multiplier value
     */
    private static int getWeatherValue(Weather weather) {
        if (weather == null) return ANY_WEATHER_VALUE;
        
        switch (weather) {
            case SUNNY: return SUNNY_VALUE;
            case RAINY: return RAINY_VALUE;
            case ANY: return ANY_WEATHER_VALUE;
            default: return ANY_WEATHER_VALUE;
        }
    }
    
    /**
     * Get location multiplier value
     */
    private static int getLocationValue(String location) {
        if (location == null) return POND_VALUE;
        
        switch (location.toLowerCase()) {
            case "pond": return POND_VALUE;
            case "forest river": return FOREST_RIVER_VALUE;
            case "mountain lake": return MOUNTAIN_LAKE_VALUE;
            case "ocean": return OCEAN_VALUE;
            default: return POND_VALUE;
        }
    }
    
    /**
     * Get type multiplier (C value)
     */
    private static int getTypeMultiplier(String type) {
        if (type == null) return COMMON_MULTIPLIER;
        
        switch (type.toLowerCase()) {
            case "common": return COMMON_MULTIPLIER;
            case "regular": return REGULAR_MULTIPLIER;
            case "legendary": return LEGENDARY_MULTIPLIER;
            default: return COMMON_MULTIPLIER;
        }
    }
    
    /**
     * Validate a specific fish price
     */
    public static void validateFishPrice(String fishName) {
        Fish fish = FishData.getFish(fishName);
        if (fish == null) {
            System.out.println("Fish not found: " + fishName);
            return;
        }
        
        int expectedPrice = calculateExpectedPrice(fish);
        int actualPrice = fish.getValue();
        
        System.out.println("=== FISH PRICE VALIDATION: " + fishName + " ===");
        System.out.println("Type: " + fish.getType());
        System.out.println("Location: " + fish.getLocation());
        System.out.println("Season: " + fish.getSeason());
        System.out.println("Weather: " + fish.getWeather());
        System.out.println("Time: " + fish.getStartTime() + " - " + fish.getEndTime());
        System.out.println();
        System.out.println("Expected Price: " + expectedPrice);
        System.out.println("Actual Price: " + actualPrice);
        System.out.println("Status: " + (expectedPrice == actualPrice ? "✓ CORRECT" : "✗ INCORRECT"));
        
        if (expectedPrice != actualPrice) {
            System.out.println("Difference: " + (actualPrice - expectedPrice));
        }
    }
}
