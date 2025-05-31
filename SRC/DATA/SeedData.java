package SRC.DATA;

import SRC.ITEMS.Seed;
import java.util.HashMap;
import java.util.Map;

/**
 * Data class for managing all seed items with static initialization
 */
public class SeedData {
    private static Map<String, Seed> seeds = new HashMap<>();
    
    // Static initializer to load all seeds
    static {
        initializeSeeds();
    }
    
    /**
     * Initialize all seed items
     */
    private static void initializeSeeds() {
       
        seeds.put("Parsnip Seed", new Seed("Parsnip Seed", 10, 20, "Spring", 1)); 
        seeds.put("Cauliflower Seed", new Seed("Cauliflower Seed", 40, 80, "Spring", 5)); 
        seeds.put("Potato Seed", new Seed("Potato Seed", 25, 50, "Spring", 3));
        seeds.put("Wheat Seed", new Seed("Wheat Seed", 30, 60, "Spring", 1));

        seeds.put("Blueberry Seed", new Seed("Blueberry Seed", 40, 80, "Summer", 7));
        seeds.put("Tomato Seed", new Seed("Tomato Seed", 25, 50, "Summer", 3));
        seeds.put("Hot Pepper Seed", new Seed("Hot Pepper Seed", 20, 40, "Summer", 1));
        seeds.put("Melon Seed", new Seed("Melon Seed", 40, 80, "Summer", 4));

        seeds.put("Cranberry Seed", new Seed("Cranberry Seed", 50, 100, "Fall", 2));
        seeds.put("Pumpkin Seed", new Seed("Pumpkin Seed", 75, 150, "Fall", 7));
        seeds.put("Wheat Seed", new Seed("Wheat Seed", 30, 60, "Fall", 1));
        seeds.put("Grape Seed", new Seed("Grape Seed", 30, 60, "Fall", 3));
    }
    
    /**
     * Get a seed by name
     * @param seedName Name of the seed
     * @return Seed object or null if not found
     */
    public static Seed getSeed(String seedName) {
        return seeds.get(seedName);
    }
    
    /**
     * Add seed to inventory with specified quantity (returns the seed item)
     * @param seedName Name of the seed to add
     * @param quantity Quantity to add (for compatibility, not used in return)
     * @return Seed item ready to be added to inventory
     */
    public static Seed addSeed(String seedName, int quantity) {
        Seed seed = getSeed(seedName);
        if (seed != null) {
            return seed;
        } else {
            System.err.println("Seed not found: " + seedName);
            return null;
        }
    }
    
    /**
     * Check if seed exists
     * @param seedName Name of the seed
     * @return true if seed exists
     */
    public static boolean hasSeed(String seedName) {
        return seeds.containsKey(seedName);
    }
    
    /**
     * Get seeds by season
     * @param season Season to filter by
     * @return Map of seeds for the specified season
     */
    public static Map<String, Seed> getSeedsBySeason(String season) {
        Map<String, Seed> seasonSeeds = new HashMap<>();
        for (Map.Entry<String, Seed> entry : seeds.entrySet()) {
            if (entry.getValue().getSeason().equalsIgnoreCase(season)) {
                seasonSeeds.put(entry.getKey(), entry.getValue());
            }
        }
        return seasonSeeds;
    }
    
    /**
     * Get all seeds
     * @return Map of all seeds
     */
    public static Map<String, Seed> getAllSeeds() {
        return new HashMap<>(seeds);
    }
}
