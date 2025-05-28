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
    }
      /**
     * Initialize all fish items according to new specifications
     */
    private static void initializeFish() {        // Common Fish (6 types - including pond fish)
        fish.put("Bullhead", new Fish("Bullhead", "Common", "Forest River", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(23, 59)));
        fish.put("Carp", new Fish("Carp", "Common", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(23, 59)));
        fish.put("Chub", new Fish("Chub", "Common", "Forest River", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(23, 59)));
        // Pond-specific common fish
        fish.put("Bluegill", new Fish("Bluegill", "Common", "Pond", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(23, 59)));
        fish.put("Perch", new Fish("Perch", "Common", "Pond", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(23, 59)));
        fish.put("Sunfish", new Fish("Sunfish", "Common", "Pond", Season.ANY, Weather.SUNNY, 
            new GameTime(6, 0), new GameTime(19, 0)));
          // Regular Fish (15 types - including pond fish)
        fish.put("Catfish", new Fish("Catfish", "Regular", "Forest River", Season.SPRING, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(23, 59)));
        fish.put("Largemouth Bass", new Fish("Largemouth Bass", "Regular", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(19, 0)));
        fish.put("Pike", new Fish("Pike", "Regular", "Forest River", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(23, 59)));
        fish.put("Rainbow Trout", new Fish("Rainbow Trout", "Regular", "Forest River", Season.SUMMER, Weather.SUNNY, 
            new GameTime(6, 0), new GameTime(19, 0)));
        fish.put("Salmon", new Fish("Salmon", "Regular", "Forest River", Season.FALL, Weather.ANY, 
            new GameTime(6, 0), new GameTime(19, 0)));
        fish.put("Shad", new Fish("Shad", "Regular", "Forest River", Season.SPRING, Weather.RAINY, 
            new GameTime(9, 0), new GameTime(14, 0)));
        fish.put("Smallmouth Bass", new Fish("Smallmouth Bass", "Regular", "Forest River", Season.FALL, Weather.ANY, 
            new GameTime(6, 0), new GameTime(23, 59)));
        fish.put("Walleye", new Fish("Walleye", "Regular", "Forest River", Season.FALL, Weather.RAINY, 
            new GameTime(12, 0), new GameTime(14, 0)));
        fish.put("Albacore", new Fish("Albacore", "Regular", "Ocean", Season.FALL, Weather.ANY, 
            new GameTime(6, 0), new GameTime(11, 0)));
        fish.put("Anchovy", new Fish("Anchovy", "Regular", "Ocean", Season.SPRING, Weather.ANY, 
            new GameTime(6, 0), new GameTime(14, 0)));
        fish.put("Red Snapper", new Fish("Red Snapper", "Regular", "Ocean", Season.SUMMER, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(19, 0)));
        fish.put("Tuna", new Fish("Tuna", "Regular", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(19, 0)));
        // Pond-specific regular fish
        fish.put("Channel Catfish", new Fish("Channel Catfish", "Regular", "Pond", Season.SPRING, Weather.ANY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Pond Bass", new Fish("Pond Bass", "Regular", "Pond", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(20, 0)));
        fish.put("Golden Carp", new Fish("Golden Carp", "Regular", "Pond", Season.FALL, Weather.SUNNY, 
            new GameTime(8, 0), new GameTime(18, 0)));
          // Legendary Fish (5 types - including pond fish)
        fish.put("Angler", new Fish("Angler", "Legendary", "Forest River", Season.FALL, Weather.ANY, 
            new GameTime(6, 0), new GameTime(20, 0)));
        fish.put("Crimsonfish", new Fish("Crimsonfish", "Legendary", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(20, 0)));
        fish.put("Glacierfish", new Fish("Glacierfish", "Legendary", "Forest River", Season.WINTER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(20, 0)));
        fish.put("Legend", new Fish("Legend", "Legendary", "Mountain Lake", Season.SPRING, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(20, 0)));
        // Pond-specific legendary fish
        fish.put("Mystic Koi", new Fish("Mystic Koi", "Legendary", "Pond", Season.SPRING, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(20, 0)));
        
        System.out.println("FishData: Initialized " + fish.size() + " fish types including pond fish");
    }
    
    /**
     * Get a fish by name
     * @param fishName Name of the fish
     * @return Fish object or null if not found
     */
    public static Fish getFish(String fishName) {
        return fish.get(fishName);
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
            System.err.println("Fish not found: " + fishName);
            return null;
        }
    }
    
    /**
     * Check if fish exists
     * @param fishName Name of the fish
     * @return true if fish exists
     */
    public static boolean hasFish(String fishName) {
        return fish.containsKey(fishName);
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
