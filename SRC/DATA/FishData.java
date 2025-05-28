package SRC.DATA;

import SRC.ITEMS.Fish;
import SRC.ITEMS.Time;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

/**
 * Data class for managing all fish items with static initialization
 */
public class FishData {
    private static Map<String, Fish> fish = new HashMap<>();
    
    // Static initializer to load all fish
    static {
        initializeFish();
    }
    
    /**
     * Initialize all fish items
     */
    private static void initializeFish() {
        // River Fish
        fish.put("Carp", new Fish("Carp", 30, 15, "Common", "River", "All", "Any", new Time(6, 0)));
        fish.put("Catfish", new Fish("Catfish", 200, 75, "Rare", "River", "Spring/Fall", "Rain", new Time(6, 0)));
        fish.put("Rainbow Trout", new Fish("Rainbow Trout", 65, 35, "Uncommon", "River", "Summer", "Sun", new Time(6, 19)));
        fish.put("Pike", new Fish("Pike", 100, 50, "Uncommon", "River", "Winter", "Any", new Time(6, 0)));
        fish.put("Walleye", new Fish("Walleye", 105, 45, "Uncommon", "River", "Fall", "Rain", new Time(12, 2)));
        
        // Ocean Fish
        fish.put("Sardine", new Fish("Sardine", 40, 20, "Common", "Ocean", "All", "Any", new Time(6, 19)));
        fish.put("Tuna", new Fish("Tuna", 100, 70, "Rare", "Ocean", "Summer/Winter", "Any", new Time(6, 19)));
        fish.put("Red Snapper", new Fish("Red Snapper", 50, 25, "Uncommon", "Ocean", "Summer/Fall", "Rain", new Time(6, 19)));
        fish.put("Flounder", new Fish("Flounder", 100, 50, "Uncommon", "Ocean", "Spring/Summer", "Any", new Time(6, 20)));
        fish.put("Salmon", new Fish("Salmon", 75, 40, "Uncommon", "Ocean", "Fall", "Any", new Time(6, 19)));
        
        // Lake Fish
        fish.put("Largemouth Bass", new Fish("Largemouth Bass", 100, 50, "Uncommon", "Lake", "All", "Any", new Time(6, 19)));
        fish.put("Smallmouth Bass", new Fish("Smallmouth Bass", 50, 25, "Common", "Lake", "All", "Any", new Time(6, 19)));
        
        // Legendary Fish
        fish.put("Legend", new Fish("Legend", 5000, 200, "Legendary", "Lake", "Spring", "Rain", new Time(6, 20)));
        fish.put("Crimsonfish", new Fish("Crimsonfish", 1500, 150, "Legendary", "Ocean", "Summer", "Any", new Time(6, 20)));
        fish.put("Angler", new Fish("Angler", 900, 100, "Legendary", "River", "Fall", "Any", new Time(6, 20)));
        
        System.out.println("FishData: Initialized " + fish.size() + " fish types");
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
     * Get all fish
     * @return Map of all fish
     */
    public static Map<String, Fish> getAllFish() {
        return new HashMap<>(fish);
    }
}
