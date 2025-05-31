import SRC.DATA.FishData;
import SRC.ITEMS.Fish;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.GameTime;
import java.util.*;

/**
 * Comprehensive Fish Specification Validator
 * Validates current fish data against the provided 19 fish specifications
 */
public class ComprehensiveFishValidator {
    
    // Expected fish specifications from the provided data
    private static final String[][] EXPECTED_FISH_SPECS = {
        {"Bullhead", "Any", "Any", "Any", "Mountain Lake", "common"},
        {"Carp", "Any", "Any", "Any", "Mountain Lake, Pond", "common"},
        {"Chub", "Any", "Any", "Any", "Forest River, Mountain Lake", "common"},
        {"Largemouth Bass", "Any", "06:00-18:00", "Any", "Mountain Lake", "regular"},
        {"Rainbow Trout", "Summer", "06:00-18:00", "Sunny", "Forest River, Mountain Lake", "regular"},
        {"Sturgeon", "Summer, Winter", "06:00-18:00", "Any", "Mountain Lake", "regular"},
        {"Midnight Carp", "Winter, Fall", "20:00-02:00", "Any", "Mountain Lake, Pond", "regular"},
        {"Flounder", "Spring, Summer", "06:00-22:00", "Any", "Ocean", "regular"},
        {"Halibut", "Any", "06:00-11:00, 19:00-02:00", "Any", "Ocean", "regular"},
        {"Octopus", "Summer", "06:00-22:00", "Any", "Ocean", "regular"},
        {"Pufferfish", "Summer", "00:00-16:00", "Sunny", "Ocean", "regular"},
        {"Sardine", "Any", "06:00-18:00", "Any", "Ocean", "regular"},
        {"Super Cucumber", "Summer, Fall, Winter", "18:00-02:00", "Any", "Ocean", "regular"},
        {"Catfish", "Spring, Summer, Fall", "06:00-22:00", "Rainy", "Forest River, Pond", "regular"},
        {"Salmon", "Fall", "06:00-18:00", "Any", "Forest River", "regular"},
        {"Angler", "Fall", "08:00-20:00", "Any", "Pond", "legendary"},
        {"Crimsonfish", "Summer", "08:00-20:00", "Any", "Ocean", "legendary"},
        {"Glacierfish", "Winter", "08:00-20:00", "Any", "Forest River", "legendary"},
        {"Legend", "Spring", "08:00-20:00", "Rainy", "Mountain Lake", "legendary"}
    };
    
    public static void main(String[] args) {
        System.out.println("=== COMPREHENSIVE FISH SPECIFICATION VALIDATOR ===\n");
        
        validateAllFishSpecifications();
        
        System.out.println("\n=== VALIDATION COMPLETE ===");
    }
    
    /**
     * Validates all fish specifications against current data
     */
    private static void validateAllFishSpecifications() {
        int totalFish = EXPECTED_FISH_SPECS.length;
        int validFish = 0;
        int invalidFish = 0;
        
        System.out.println("Validating " + totalFish + " fish specifications...\n");
        
        for (String[] expectedSpec : EXPECTED_FISH_SPECS) {
            String fishName = expectedSpec[0];
            String expectedSeason = expectedSpec[1];
            String expectedTime = expectedSpec[2];
            String expectedWeather = expectedSpec[3];
            String expectedLocations = expectedSpec[4];
            String expectedRarity = expectedSpec[5];
            
            System.out.println("--- VALIDATING: " + fishName + " ---");
            
            boolean isValid = validateFishSpecification(fishName, expectedSeason, expectedTime, 
                                                     expectedWeather, expectedLocations, expectedRarity);
            
            if (isValid) {
                validFish++;
                System.out.println("✅ " + fishName + " - VALID\n");
            } else {
                invalidFish++;
                System.out.println("❌ " + fishName + " - INVALID\n");
            }
        }
        
        // Summary
        System.out.println("=== VALIDATION SUMMARY ===");
        System.out.println("Total Fish: " + totalFish);
        System.out.println("Valid Fish: " + validFish);
        System.out.println("Invalid Fish: " + invalidFish);
        System.out.println("Success Rate: " + String.format("%.1f%%", (validFish * 100.0 / totalFish)));
    }
    
    /**
     * Validates a single fish specification
     */
    private static boolean validateFishSpecification(String fishName, String expectedSeason, 
                                                   String expectedTime, String expectedWeather, 
                                                   String expectedLocations, String expectedRarity) {
        
        boolean isValid = true;
        
        // Check if fish exists
        if (!FishData.hasFish(fishName)) {
            System.out.println("❌ Fish not found: " + fishName);
            return false;
        }
        
        // Get all fish variants for this name
        Map<String, Fish> allFish = FishData.getAllFish();
        List<Fish> fishVariants = new ArrayList<>();
        
        for (Fish fish : allFish.values()) {
            if (fish.getName().equals(fishName)) {
                fishVariants.add(fish);
            }
        }
        
        if (fishVariants.isEmpty()) {
            System.out.println("❌ No fish variants found for: " + fishName);
            return false;
        }
        
        System.out.println("Found " + fishVariants.size() + " variant(s) for " + fishName);
        
        // Validate rarity (should be consistent across all variants)
        String actualRarity = fishVariants.get(0).getType();
        String normalizedExpectedRarity = normalizeRarity(expectedRarity);
        if (!actualRarity.equalsIgnoreCase(normalizedExpectedRarity)) {
            System.out.println("❌ Rarity mismatch - Expected: " + normalizedExpectedRarity + ", Actual: " + actualRarity);
            isValid = false;
        } else {
            System.out.println("✅ Rarity: " + actualRarity);
        }
        
        // Validate locations
        Set<String> actualLocations = new HashSet<>();
        for (Fish fish : fishVariants) {
            actualLocations.add(fish.getLocation());
        }
        
        Set<String> expectedLocationSet = parseLocations(expectedLocations);
        if (!actualLocations.equals(expectedLocationSet)) {
            System.out.println("❌ Location mismatch");
            System.out.println("   Expected: " + expectedLocationSet);
            System.out.println("   Actual: " + actualLocations);
            isValid = false;
        } else {
            System.out.println("✅ Locations: " + actualLocations);
        }
        
        // Validate seasons
        Set<String> actualSeasons = new HashSet<>();
        for (Fish fish : fishVariants) {
            if (fish.getSeason() == Season.ANY) {
                actualSeasons.add("Any");
            } else {
                actualSeasons.add(fish.getSeason().toString());
            }
        }
        
        Set<String> expectedSeasonSet = parseSeasons(expectedSeason);
        if (!validateSeasonMatch(actualSeasons, expectedSeasonSet)) {
            System.out.println("❌ Season mismatch");
            System.out.println("   Expected: " + expectedSeasonSet);
            System.out.println("   Actual: " + actualSeasons);
            isValid = false;
        } else {
            System.out.println("✅ Seasons: " + actualSeasons);
        }
        
        // Validate weather
        Set<String> actualWeathers = new HashSet<>();
        for (Fish fish : fishVariants) {
            if (fish.getWeather() == Weather.ANY) {
                actualWeathers.add("Any");
            } else {
                actualWeathers.add(fish.getWeather().toString());
            }
        }
        
        Set<String> expectedWeatherSet = parseWeather(expectedWeather);
        if (!validateWeatherMatch(actualWeathers, expectedWeatherSet)) {
            System.out.println("❌ Weather mismatch");
            System.out.println("   Expected: " + expectedWeatherSet);
            System.out.println("   Actual: " + actualWeathers);
            isValid = false;
        } else {
            System.out.println("✅ Weather: " + actualWeathers);
        }
        
        // Validate time ranges
        if (!validateTimeRanges(fishVariants, expectedTime)) {
            System.out.println("❌ Time range mismatch");
            System.out.println("   Expected: " + expectedTime);
            isValid = false;
        } else {
            System.out.println("✅ Time: " + expectedTime);
        }
        
        return isValid;
    }
    
    /**
     * Parse comma-separated locations
     */
    private static Set<String> parseLocations(String locations) {
        Set<String> locationSet = new HashSet<>();
        String[] parts = locations.split(",");
        for (String part : parts) {
            locationSet.add(part.trim());
        }
        return locationSet;
    }
    
    /**
     * Parse comma-separated seasons
     */
    private static Set<String> parseSeasons(String seasons) {
        Set<String> seasonSet = new HashSet<>();
        if (seasons.equals("Any")) {
            seasonSet.add("Any");
        } else {
            String[] parts = seasons.split(",");
            for (String part : parts) {
                seasonSet.add(part.trim().toUpperCase());
            }
        }
        return seasonSet;
    }
    
    /**
     * Parse weather string
     */
    private static Set<String> parseWeather(String weather) {
        Set<String> weatherSet = new HashSet<>();
        if (weather.equals("Any")) {
            weatherSet.add("Any");
        } else {
            weatherSet.add(weather.trim().toUpperCase());
        }
        return weatherSet;
    }
    
    /**
     * Normalize rarity string
     */
    private static String normalizeRarity(String rarity) {
        switch (rarity.toLowerCase()) {
            case "common": return "Common";
            case "regular": return "Regular";
            case "legendary": return "Legendary";
            default: return rarity;
        }
    }
    
    /**
     * Validate season match considering ANY season
     */
    private static boolean validateSeasonMatch(Set<String> actual, Set<String> expected) {
        if (expected.contains("Any")) {
            return actual.contains("Any");
        }
        
        if (actual.contains("Any")) {
            return expected.contains("Any");
        }
        
        return actual.equals(expected);
    }
    
    /**
     * Validate weather match considering ANY weather
     */
    private static boolean validateWeatherMatch(Set<String> actual, Set<String> expected) {
        if (expected.contains("Any")) {
            return actual.contains("Any");
        }
        
        if (actual.contains("Any")) {
            return expected.contains("Any");
        }
        
        return actual.equals(expected);
    }
    
    /**
     * Validate time ranges for fish variants
     */
    private static boolean validateTimeRanges(List<Fish> fishVariants, String expectedTime) {
        if (expectedTime.equals("Any")) {
            // Check if all variants cover full day (0:00-23:59)
            for (Fish fish : fishVariants) {
                GameTime startTime = fish.getStartTime();
                GameTime endTime = fish.getEndTime();
                if (!(startTime.getHour() == 0 && startTime.getMinute() == 0 && 
                      endTime.getHour() == 23 && endTime.getMinute() == 59)) {
                    return false;
                }
            }
            return true;
        }
        
        // Parse expected time ranges
        List<TimeRange> expectedRanges = parseTimeRanges(expectedTime);
        
        // Get actual time ranges
        List<TimeRange> actualRanges = new ArrayList<>();
        for (Fish fish : fishVariants) {
            actualRanges.add(new TimeRange(fish.getStartTime(), fish.getEndTime()));
        }
        
        // Check if actual ranges match expected ranges
        return compareTimeRanges(actualRanges, expectedRanges);
    }
    
    /**
     * Parse time range strings like "06:00-18:00" or "06:00-11:00, 19:00-02:00"
     */
    private static List<TimeRange> parseTimeRanges(String timeStr) {
        List<TimeRange> ranges = new ArrayList<>();
        String[] parts = timeStr.split(",");
        
        for (String part : parts) {
            part = part.trim();
            String[] times = part.split("-");
            if (times.length == 2) {
                GameTime start = parseTime(times[0].trim());
                GameTime end = parseTime(times[1].trim());
                ranges.add(new TimeRange(start, end));
            }
        }
        
        return ranges;
    }
    
    /**
     * Parse time string like "06:00"
     */
    private static GameTime parseTime(String timeStr) {
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return new GameTime(hour, minute);
    }
    
    /**
     * Compare actual and expected time ranges
     */
    private static boolean compareTimeRanges(List<TimeRange> actual, List<TimeRange> expected) {
        if (actual.size() != expected.size()) {
            return false;
        }
        
        // Sort both lists for comparison
        actual.sort((a, b) -> a.start.getHour() - b.start.getHour());
        expected.sort((a, b) -> a.start.getHour() - b.start.getHour());
        
        for (int i = 0; i < actual.size(); i++) {
            if (!actual.get(i).equals(expected.get(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Helper class for time ranges
     */
    private static class TimeRange {
        GameTime start;
        GameTime end;
        
        TimeRange(GameTime start, GameTime end) {
            this.start = start;
            this.end = end;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TimeRange)) return false;
            TimeRange other = (TimeRange) obj;
            return this.start.getHour() == other.start.getHour() &&
                   this.start.getMinute() == other.start.getMinute() &&
                   this.end.getHour() == other.end.getHour() &&
                   this.end.getMinute() == other.end.getMinute();
        }
        
        @Override
        public String toString() {
            return String.format("%02d:%02d-%02d:%02d", 
                start.getHour(), start.getMinute(), 
                end.getHour(), end.getMinute());
        }
    }
}
