import SRC.DATA.FishData;
import SRC.ITEMS.Fish;
impo            System.out.println("* ALL FISH SPECIFICATIONS ARE VALID!");
        } else {
            System.out.println("* SOME FISH SPECIFICATIONS ARE INVALID!");SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.GameTime;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Validator to check if current fish data matches the required specifications
 */
public class FishSpecificationValidator {
    
    // Required fish specifications
    private static final String[][] REQUIRED_FISH = {
        {"Largemouth Bass", "Any", "06:00-18:00", "Any", "Mountain Lake"},
        {"Rainbow Trout", "Summer", "06:00-18:00", "Sunny", "Forest River,Mountain Lake"},
        {"Sturgeon", "Summer,Winter", "06:00-18:00", "Any", "Mountain Lake"},
        {"Midnight Carp", "Winter,Fall", "20:00-02:00", "Any", "Mountain Lake,Pond"},
        {"Flounder", "Spring,Summer", "06:00-22:00", "Any", "Ocean"},
        {"Halibut", "Any", "06:00-11:00,19:00-02:00", "Any", "Ocean"},
        {"Octopus", "Summer", "06:00-22:00", "Any", "Ocean"},
        {"Pufferfish", "Summer", "00:00-16:00", "Sunny", "Ocean"},
        {"Sardine", "Any", "06:00-18:00", "Any", "Ocean"},
        {"Super Cucumber", "Summer,Fall,Winter", "18:00-02:00", "Any", "Ocean"},
        {"Catfish", "Spring,Summer,Fall", "06:00-22:00", "Rainy", "Forest River,Pond"},
        {"Salmon", "Fall", "06:00-18:00", "Any", "Forest River"}
    };
    
    public static void main(String[] args) {
        System.out.println("=== FISH SPECIFICATION VALIDATION ===\n");
        
        Map<String, Fish> allFish = FishData.getAllFish();
        boolean allValid = true;
        
        for (String[] spec : REQUIRED_FISH) {
            String fishName = spec[0];
            String[] requiredSeasons = spec[1].split(",");
            String[] requiredTimes = spec[2].split(",");
            String requiredWeather = spec[3];
            String[] requiredLocations = spec[4].split(",");
            
            System.out.println("Checking: " + fishName);
            boolean fishValid = validateFish(allFish, fishName, requiredSeasons, requiredTimes, requiredWeather, requiredLocations);
            
            if (!fishValid) {
                allValid = false;
            }
            System.out.println();
        }
        
        System.out.println("=== SUMMARY ===");
        if (allValid) {
            System.out.println("✅ ALL FISH SPECIFICATIONS ARE VALID!");
        } else {
            System.out.println("❌ SOME FISH SPECIFICATIONS ARE INVALID!");
        }
        
        // Also show what fish we have in the system
        System.out.println("\n=== CURRENT FISH IN SYSTEM ===");
        for (Map.Entry<String, Fish> entry : allFish.entrySet()) {
            Fish fish = entry.getValue();
            System.out.println(String.format("%s: %s | %s | %s-%s | %s | %s", 
                entry.getKey(),
                fish.getName(),
                fish.getLocation(),
                formatTime(fish.getStartTime()),
                formatTime(fish.getEndTime()),
                fish.getSeason(),
                fish.getWeather()
            ));
        }
    }
    
    private static boolean validateFish(Map<String, Fish> allFish, String fishName, 
                                      String[] requiredSeasons, String[] requiredTimes, 
                                      String requiredWeather, String[] requiredLocations) {
        
        // Find all fish variants with this name
        List<Fish> fishVariants = new ArrayList<>();
        for (Map.Entry<String, Fish> entry : allFish.entrySet()) {
            if (entry.getValue().getName().equals(fishName)) {
                fishVariants.add(entry.getValue());
            }
        }
        
        if (fishVariants.isEmpty()) {
            System.out.println("  * Fish not found: " + fishName);
            return false;
        }
        
        System.out.println("  Found " + fishVariants.size() + " variants");
        
        // Check if all required combinations are covered
        boolean allCombinationsCovered = true;
        
        for (String location : requiredLocations) {
            for (String season : requiredSeasons) {
                for (String timeRange : requiredTimes) {
                    boolean combinationFound = false;
                    
                    for (Fish fish : fishVariants) {
                        if (matchesLocation(fish.getLocation(), location) &&
                            matchesSeason(fish.getSeason(), season) &&
                            matchesWeather(fish.getWeather(), requiredWeather) &&
                            matchesTimeRange(fish, timeRange)) {
                            combinationFound = true;
                            break;
                        }
                    }
                    
                    if (!combinationFound) {
                        System.out.println("    * Missing combination: " + location + " + " + season + " + " + timeRange + " + " + requiredWeather);
                        allCombinationsCovered = false;
                    }
                }
            }
        }
        
        if (allCombinationsCovered) {
            System.out.println("  ✅ All required combinations found");
            return true;
        } else {
            return false;
        }
    }
    
    private static boolean matchesLocation(String fishLocation, String requiredLocation) {
        return fishLocation.equalsIgnoreCase(requiredLocation);
    }
    
    private static boolean matchesSeason(Season fishSeason, String requiredSeason) {
        if (requiredSeason.equalsIgnoreCase("Any")) {
            return fishSeason == Season.ANY;
        }
        
        return fishSeason.toString().equalsIgnoreCase(requiredSeason) || fishSeason == Season.ANY;
    }
    
    private static boolean matchesWeather(Weather fishWeather, String requiredWeather) {
        if (requiredWeather.equalsIgnoreCase("Any")) {
            return fishWeather == Weather.ANY;
        }
        
        return fishWeather.toString().equalsIgnoreCase(requiredWeather) || fishWeather == Weather.ANY;
    }
    
    private static boolean matchesTimeRange(Fish fish, String timeRange) {
        String[] times = timeRange.split("-");
        if (times.length != 2) return false;
        
        try {
            String[] startParts = times[0].split(":");
            String[] endParts = times[1].split(":");
            
            int startHour = Integer.parseInt(startParts[0]);
            int startMinute = Integer.parseInt(startParts[1]);
            int endHour = Integer.parseInt(endParts[0]);
            int endMinute = Integer.parseInt(endParts[1]);
            
            GameTime startTime = new GameTime(startHour, startMinute);
            GameTime endTime = new GameTime(endHour, endMinute);
            
            return fish.getStartTime().getHour() == startTime.getHour() &&
                   fish.getStartTime().getMinute() == startTime.getMinute() &&
                   fish.getEndTime().getHour() == endTime.getHour() &&
                   fish.getEndTime().getMinute() == endTime.getMinute();
                   
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static String formatTime(GameTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }
}
