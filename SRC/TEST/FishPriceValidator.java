package SRC.TEST;

import SRC.DATA.FishData;
import SRC.ITEMS.Fish;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.GameTime;

import java.util.*;

/**
 * Driver class to validate if fish selling prices follow the specified formula:
 * Price = 4^(number of seasons) × 2^(number of hours) × 2^(number of weather variations) × 4^(number of locations) × C
 * Where C = 10 for common fish, C = 5 for regular fish, C = 25 for legendary fish
 */
public class FishPriceValidator {
    
    // Maps to track fish occurrences
    private static final Map<String, Set<String>> fishLocations = new HashMap<>();
    private static final Map<String, Set<Season>> fishSeasons = new HashMap<>();
    private static final Map<String, Set<Weather>> fishWeather = new HashMap<>();
    private static final Map<String, Integer> fishHoursPerDay = new HashMap<>();
    private static final Map<String, String> fishTypes = new HashMap<>();
    
    public static void main(String[] args) {
        System.out.println("======== FISH PRICE VALIDATION REPORT ========");
        System.out.println("This program validates if fish selling prices follow the formula:");
        System.out.println("Price = 4^(seasons) × 2^(hours) × 2^(weather) × 4^(locations) × C");
        System.out.println("Where C = 10 for common fish, C = 5 for regular fish, C = 25 for legendary fish");
        System.out.println("================================================\n");
        
        // Get all fish from FishData
        Map<String, Fish> allFish = FishData.getAllFish();
        
        // Process each fish entry to collect data for calculation
        collectFishData(allFish);
        
        // Calculate and validate prices
        validateFishPrices();
    }
    
    /**
     * Collect data about fish for price calculation
     */
    private static void collectFishData(Map<String, Fish> allFish) {
        for (Map.Entry<String, Fish> entry : allFish.entrySet()) {
            Fish fish = entry.getValue();
            String fishName = fish.getName();
            
            // Store fish type
            fishTypes.put(fishName, fish.getType());
            
            // Store location
            if (!fishLocations.containsKey(fishName)) {
                fishLocations.put(fishName, new HashSet<>());
            }
            fishLocations.get(fishName).add(fish.getLocation());
            
            // Store season
            if (!fishSeasons.containsKey(fishName)) {
                fishSeasons.put(fishName, new HashSet<>());
            }
            
            if (fish.getSeason() == Season.ANY) {
                // If ANY, add all seasons
                fishSeasons.get(fishName).add(Season.SPRING);
                fishSeasons.get(fishName).add(Season.SUMMER);
                fishSeasons.get(fishName).add(Season.FALL);
                fishSeasons.get(fishName).add(Season.WINTER);
            } else {
                fishSeasons.get(fishName).add(fish.getSeason());
            }
            
            // Store weather
            if (!fishWeather.containsKey(fishName)) {
                fishWeather.put(fishName, new HashSet<>());
            }
            
            if (fish.getWeather() == Weather.ANY) {
                // If ANY, add all weather types
                fishWeather.get(fishName).add(Weather.SUNNY);
                fishWeather.get(fishName).add(Weather.RAINY);
            } else {
                fishWeather.get(fishName).add(fish.getWeather());
            }
            
            // Calculate hours
            if (!fishHoursPerDay.containsKey(fishName)) {
                fishHoursPerDay.put(fishName, 0);
            }
            
            int startHour = fish.getStartTime().getHour();
            int endHour = fish.getEndTime().getHour();
            int hours;
            
            if (startHour < endHour) {
                hours = endHour - startHour;
                if (fish.getEndTime().getMinute() > 0) {
                    hours++; // Count partial hour
                }
            } else if (startHour > endHour) {
                // Crosses midnight
                hours = (24 - startHour) + endHour;
                if (fish.getEndTime().getMinute() > 0) {
                    hours++; // Count partial hour
                }
            } else {
                // Same hour - check if full 24 hours
                if (fish.getStartTime().getMinute() == 0 && 
                    (fish.getEndTime().getMinute() == 59 || fish.getEndTime().getMinute() == 0)) {
                    hours = 24; // Full day
                } else {
                    hours = 1; // Just one hour
                }
            }
            
            // Update with maximum hours found
            fishHoursPerDay.put(fishName, Math.max(fishHoursPerDay.get(fishName), hours));
        }
    }
    
    /**
     * Calculate and validate fish prices
     */
    private static void validateFishPrices() {
        System.out.printf("%-20s | %-10s | %-8s | %-15s | %-6s | %-9s | %-15s | %-15s | %s%n",
                "Fish Name", "Type", "Seasons", "Hours", "Weather", "Locations", "Expected Price", "Actual Price", "Status");
        System.out.println("-".repeat(115));
        
        // Sort fish alphabetically for better readability
        List<String> fishNames = new ArrayList<>(fishTypes.keySet());
        Collections.sort(fishNames);
        
        for (String fishName : fishNames) {
            String fishType = fishTypes.get(fishName);
            int numSeasons = fishSeasons.get(fishName).size();
            int hours = fishHoursPerDay.get(fishName);
            int numWeather = fishWeather.get(fishName).size();
            int numLocations = fishLocations.get(fishName).size();
            
            int baseValue = getBaseValueForType(fishType);
            
            // Calculate expected price based on formula
            double expectedPrice = Math.pow(4, numSeasons) * 
                                  Math.pow(2, hours) * 
                                  Math.pow(2, numWeather) * 
                                  Math.pow(4, numLocations) * 
                                  baseValue;
              // Get actual price
            Fish fishItem = FishData.getFish(fishName);
            int actualPrice = fishItem != null ? fishItem.getSellPrice() : 0;
            
            // Validate if the price matches the expected price
            String status = Math.abs(expectedPrice - actualPrice) < 0.01 ? "MATCH" : "MISMATCH";
            
            System.out.printf("%-20s | %-10s | %-8d | %-15d | %-6d | %-9d | %-15.0f | %-15d | %s%n",
                    fishName, fishType, numSeasons, hours, numWeather, numLocations, 
                    expectedPrice, actualPrice, status);
        }
    }
    
    /**
     * Get base value constant for fish type
     */
    private static int getBaseValueForType(String fishType) {
        switch (fishType) {
            case "Common": return 10;
            case "Regular": return 5;
            case "Legendary": return 25;
            default: return 0;
        }
    }
}
