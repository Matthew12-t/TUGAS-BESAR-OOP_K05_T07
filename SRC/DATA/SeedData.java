package SRC.DATA;

import SRC.ITEMS.Seed;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
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
        // Spring Seeds
        seeds.put("Parsnip Seeds", new Seed("Parsnip Seeds", 20, 25, "Spring", 4));
        seeds.put("Cauliflower Seeds", new Seed("Cauliflower Seeds", 80, 100, "Spring", 12));
        seeds.put("Potato Seeds", new Seed("Potato Seeds", 80, 50, "Spring", 6));
        seeds.put("Kale Seeds", new Seed("Kale Seeds", 110, 70, "Spring", 6));
        
        // Summer Seeds
        seeds.put("Tomato Seeds", new Seed("Tomato Seeds", 60, 50, "Summer", 11));
        seeds.put("Corn Seeds", new Seed("Corn Seeds", 50, 150, "Summer", 14));
        seeds.put("Blueberry Seeds", new Seed("Blueberry Seeds", 50, 80, "Summer", 13));
        seeds.put("Pepper Seeds", new Seed("Pepper Seeds", 40, 40, "Summer", 5));
        
        // Fall Seeds
        seeds.put("Pumpkin Seeds", new Seed("Pumpkin Seeds", 320, 100, "Fall", 13));
        seeds.put("Eggplant Seeds", new Seed("Eggplant Seeds", 60, 20, "Fall", 5));
        seeds.put("Cranberry Seeds", new Seed("Cranberry Seeds", 75, 240, "Fall", 7));
        seeds.put("Beet Seeds", new Seed("Beet Seeds", 100, 20, "Fall", 6));
        
        System.out.println("SeedData: Initialized " + seeds.size() + " seed types");
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
