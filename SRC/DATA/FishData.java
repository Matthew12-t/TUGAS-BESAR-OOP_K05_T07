package SRC.DATA;

import SRC.ITEMS.Fish;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.GameTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Data class for managing all fish items with static initialization
 * Updated to match new fishing system specifications
 */
public class FishData {
    private static Map<String, Fish> fish = new HashMap<>();
    
    // Static initializer to load all fish
    static {
        initializeFish();
    }    /**
     * Initialize all fish items according to new specifications
     */    private static void initializeFish() {
        // Common Fish (3 types) with multiple locations
        fish.put("Bullhead", new Fish("Bullhead", "Common", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        
        // Carp - Mountain Lake and Pond
        fish.put("Carp", new Fish("Carp", "Common", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        fish.put("Carp", new Fish("Carp", "Common", "Pond", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        
        // Chub - Forest River and Mountain Lake
        fish.put("Chub_ForestRiver", new Fish("Chub", "Common", "Forest River", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        fish.put("Chub_MountainLake", new Fish("Chub", "Common", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        
        // Regular Fish (12 types) with multiple locations
        fish.put("Largemouth Bass", new Fish("Largemouth Bass", "Regular", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Rainbow Trout - Forest River and Mountain Lake
        fish.put("Rainbow Trout_ForestRiver", new Fish("Rainbow Trout", "Regular", "Forest River", Season.SUMMER, Weather.SUNNY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        fish.put("Rainbow Trout_MountainLake", new Fish("Rainbow Trout", "Regular", "Mountain Lake", Season.SUMMER, Weather.SUNNY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Sturgeon - Summer and Winter seasons
        fish.put("Sturgeon_Summer", new Fish("Sturgeon", "Regular", "Mountain Lake", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        fish.put("Sturgeon_Winter", new Fish("Sturgeon", "Regular", "Mountain Lake", Season.WINTER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Midnight Carp - Mountain Lake and Pond, Winter and Fall
        fish.put("Midnight Carp_MountainLake_Winter", new Fish("Midnight Carp", "Regular", "Mountain Lake", Season.WINTER, Weather.ANY, 
            new GameTime(20, 0), new GameTime(2, 0)));
        fish.put("Midnight Carp_MountainLake_Fall", new Fish("Midnight Carp", "Regular", "Mountain Lake", Season.FALL, Weather.ANY, 
            new GameTime(20, 0), new GameTime(2, 0)));
        fish.put("Midnight Carp_Pond_Winter", new Fish("Midnight Carp", "Regular", "Pond", Season.WINTER, Weather.ANY, 
            new GameTime(20, 0), new GameTime(2, 0)));
        fish.put("Midnight Carp_Pond_Fall", new Fish("Midnight Carp", "Regular", "Pond", Season.FALL, Weather.ANY, 
            new GameTime(20, 0), new GameTime(2, 0)));
        
        // Ocean Fish - Spring and Summer for Flounder
        fish.put("Flounder_Spring", new Fish("Flounder", "Regular", "Ocean", Season.SPRING, Weather.ANY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Flounder_Summer", new Fish("Flounder", "Regular", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        
        // Halibut - two time periods
        fish.put("Halibut_Morning", new Fish("Halibut", "Regular", "Ocean", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(11, 0)));
        fish.put("Halibut_Evening", new Fish("Halibut", "Regular", "Ocean", Season.ANY, Weather.ANY, 
            new GameTime(19, 0), new GameTime(2, 0)));
        
        fish.put("Octopus", new Fish("Octopus", "Regular", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Pufferfish", new Fish("Pufferfish", "Regular", "Ocean", Season.SUMMER, Weather.SUNNY, 
            new GameTime(0, 0), new GameTime(16, 0)));
        fish.put("Sardine", new Fish("Sardine", "Regular", "Ocean", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Super Cucumber - Summer, Fall, Winter
        fish.put("Super Cucumber_Summer", new Fish("Super Cucumber", "Regular", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(18, 0), new GameTime(2, 0)));
        fish.put("Super Cucumber_Fall", new Fish("Super Cucumber", "Regular", "Ocean", Season.FALL, Weather.ANY, 
            new GameTime(18, 0), new GameTime(2, 0)));
        fish.put("Super Cucumber_Winter", new Fish("Super Cucumber", "Regular", "Ocean", Season.WINTER, Weather.ANY, 
            new GameTime(18, 0), new GameTime(2, 0)));
        
        // Catfish - Forest River and Pond, Spring, Summer, Fall
        fish.put("Catfish_ForestRiver_Spring", new Fish("Catfish", "Regular", "Forest River", Season.SPRING, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_ForestRiver_Summer", new Fish("Catfish", "Regular", "Forest River", Season.SUMMER, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_ForestRiver_Fall", new Fish("Catfish", "Regular", "Forest River", Season.FALL, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_Pond_Spring", new Fish("Catfish", "Regular", "Pond", Season.SPRING, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_Pond_Summer", new Fish("Catfish", "Regular", "Pond", Season.SUMMER, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_Pond_Fall", new Fish("Catfish", "Regular", "Pond", Season.FALL, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        
        fish.put("Salmon", new Fish("Salmon", "Regular", "Forest River", Season.FALL, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Legendary Fish (4 types)
        fish.put("Angler", new Fish("Angler", "Legendary", "Pond", Season.FALL, Weather.ANY, 
            new GameTime(8, 0), new GameTime(20, 0)));
        fish.put("Crimsonfish", new Fish("Crimsonfish", "Legendary", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(8, 0), new GameTime(20, 0)));
        fish.put("Glacierfish", new Fish("Glacierfish", "Legendary", "Forest River", Season.WINTER, Weather.ANY, 
            new GameTime(8, 0), new GameTime(20, 0)));
        fish.put("Legend", new Fish("Legend", "Legendary", "Mountain Lake", Season.SPRING, Weather.RAINY, 
            new GameTime(8, 0), new GameTime(20, 0)));
        
    }
    
    /**
     * Get a fish by name (backwards compatibility - returns first match)
     * @param fishName Name of the fish
     * @return Fish object or null if not found
     */
    public static Fish getFish(String fishName) {
        // Try exact match first
        Fish exactMatch = fish.get(fishName);
        if (exactMatch != null) {
            return exactMatch;
        }
        
        // If no exact match, find first fish with matching name
        for (Map.Entry<String, Fish> entry : fish.entrySet()) {
            if (entry.getValue().getName().equals(fishName)) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    /**
     * Get fish by exact key (including location suffix)
     * @param fishKey Exact key of the fish in the map
     * @return Fish object or null if not found
     */
    public static Fish getFishByKey(String fishKey) {
        return fish.get(fishKey);
    }

    /**
     * Add fish to inventory with specified quantity (returns the fish item)
     * @param fishName Name of the fish to add
     * @param quantity Quantity to add (for compatibility, not used in return)
     * @return Fish item ready to be added to inventory
     */
    public static Fish addFish(String fishName, int quantity) {
        Fish fishItem = getFish(fishName);
        if (fishItem != null) {
            return fishItem;
        } else {
            return null;
        }
    }
      /**
     * Check if fish exists
     * @param fishName Name of the fish
     * @return true if fish exists
     */
    public static boolean hasFish(String fishName) {
        // Try exact key match first
        if (fish.containsKey(fishName)) {
            return true;
        }
        
        // If no exact match, check if any fish has this name
        for (Fish fishItem : fish.values()) {
            if (fishItem.getName().equals(fishName)) {
                return true;
            }
        }
        
        return false;
    }
      /**
     * Get fish by location
     * @param location Location to filter by
     * @return Map of fish for the specified location
     */
    public static Map<String, Fish> getFishByLocation(String location) {
        Map<String, Fish> locationFish = new HashMap<>();
        for (Map.Entry<String, Fish> entry : fish.entrySet()) {
            if (entry.getValue().getLocation().equalsIgnoreCase(location)) {
                locationFish.put(entry.getKey(), entry.getValue());
            }
        }
        return locationFish;
    }
    
    /**
     * Get fish by type (Common, Regular, Legendary)
     * @param type Type to filter by
     * @return Map of fish for the specified type
     */
    public static Map<String, Fish> getFishByType(String type) {
        Map<String, Fish> typeFish = new HashMap<>();
        for (Map.Entry<String, Fish> entry : fish.entrySet()) {
            if (entry.getValue().getType().equalsIgnoreCase(type)) {
                typeFish.put(entry.getKey(), entry.getValue());
            }
        }
        return typeFish;
    }
    
    /**
     * Get fish that can be caught at specified conditions
     * @param location Fishing location
     * @param currentTime Current game time
     * @param currentSeason Current season
     * @param currentWeather Current weather
     * @return Map of catchable fish
     */
    public static Map<String, Fish> getCatchableFish(String location, GameTime currentTime, Season currentSeason, Weather currentWeather) {
        Map<String, Fish> catchableFish = new HashMap<>();
        for (Map.Entry<String, Fish> entry : fish.entrySet()) {
            Fish fishItem = entry.getValue();
            if (fishItem.getLocation().equalsIgnoreCase(location) && 
                fishItem.canBeCaughtAt(currentTime, currentSeason, currentWeather)) {
                catchableFish.put(entry.getKey(), fishItem);
            }
        }
        return catchableFish;
    }
    
    /**
     * Get all fish
     * @return Map of all fish
     */
    public static Map<String, Fish> getAllFish() {
        return new HashMap<>(fish);
    }
}
