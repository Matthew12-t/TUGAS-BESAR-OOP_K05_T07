package SRC.DATA;

import SRC.ITEMS.Crop;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

/**
 * Data class for managing all crop items with static initialization
 */
public class CropData {
    private static Map<String, Crop> crops = new HashMap<>();
    
    // Static initializer to load all crops
    static {
        initializeCrops();
    }
    
    /**
     * Initialize all crop items
     */
    private static void initializeCrops() {
        // Spring Crops
        crops.put("Parsnip", new Crop("Parsnip", 35, 25, 1));
        crops.put("Cauliflower", new Crop("Cauliflower", 175, 100, 1));
        crops.put("Potato", new Crop("Potato", 80, 50, 1));
        crops.put("Kale", new Crop("Kale", 110, 70, 1));
        
        // Summer Crops  
        crops.put("Tomato", new Crop("Tomato", 60, 50, 1));
        crops.put("Corn", new Crop("Corn", 50, 150, 1));
        crops.put("Blueberry", new Crop("Blueberry", 50, 80, 3));
        crops.put("Pepper", new Crop("Pepper", 40, 40, 1));
        
        // Fall Crops
        crops.put("Pumpkin", new Crop("Pumpkin", 320, 100, 1));
        crops.put("Eggplant", new Crop("Eggplant", 60, 20, 1));
        crops.put("Cranberry", new Crop("Cranberry", 75, 240, 2));
        crops.put("Beet", new Crop("Beet", 100, 20, 1));
        
        // Special/Rare Crops
        crops.put("Ancient Fruit", new Crop("Ancient Fruit", 550, 1000, 1));
        crops.put("Starfruit", new Crop("Starfruit", 750, 400, 1));
        crops.put("Sweet Gem Berry", new Crop("Sweet Gem Berry", 3000, 1000, 1));
        
        System.out.println("CropData: Initialized " + crops.size() + " crop types");
    }
    
    /**
     * Get a crop by name
     * @param cropName Name of the crop
     * @return Crop object or null if not found
     */
    public static Crop getCrop(String cropName) {
        return crops.get(cropName);
    }
    
    /**
     * Add crop to inventory with specified quantity (returns the crop item)
     * @param cropName Name of the crop to add
     * @param quantity Quantity to add (for compatibility, not used in return)
     * @return Crop item ready to be added to inventory
     */
    public static Crop addCrop(String cropName, int quantity) {
        Crop crop = getCrop(cropName);
        if (crop != null) {
            return crop;
        } else {
            System.err.println("Crop not found: " + cropName);
            return null;
        }
    }
    
    /**
     * Check if crop exists
     * @param cropName Name of the crop
     * @return true if crop exists
     */
    public static boolean hasCrop(String cropName) {
        return crops.containsKey(cropName);
    }
    
    /**
     * Get all crops
     * @return Map of all crops
     */
    public static Map<String, Crop> getAllCrops() {
        return new HashMap<>(crops);
    }
}
