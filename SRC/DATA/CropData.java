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
        crops.put("Parsnip", new Crop("Parsnip", 20, 50, 1));
        crops.put("Cauliflower", new Crop("Cauliflower", 150, 200, 1));
        crops.put("Potato", new Crop("Potato", 80, 0, 1)); 
        crops.put("Wheat", new Crop("Wheat", 30, 50, 3)); 
        crops.put("Tomato", new Crop("Tomato", 60, 90, 1)); 
        crops.put("Blueberry", new Crop("Blueberry", 40, 150, 3)); 
        crops.put("Hot Pepper", new Crop("Hot Pepper", 40, 0, 1)); 
        crops.put("Pumpkin", new Crop("Pumpkin", 250, 300, 1));
        crops.put("Cranberry", new Crop("Cranberry", 25, 0, 10)); 
        crops.put("Grape", new Crop("Grape", 10, 100, 20)); 
        crops.put("Melon", new Crop("Melon", 250, 0, 1));
        
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
