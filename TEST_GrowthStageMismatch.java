import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

/**
 * Test to demonstrate the growth stage vs available images mismatch issue
 */
public class TEST_GrowthStageMismatch {
    
    private static Map<String, BufferedImage> growthImageCache = new HashMap<>();
    
    public static void main(String[] args) {
        System.out.println("=== GROWTH STAGE VS AVAILABLE IMAGES MISMATCH TEST ===");
        
        // Test parsnip (4 days growth but only 2 images)
        System.out.println("\n--- PARSNIP SEED (4 days growth, but only 2 images) ---");
        testGrowthProgression("Parsnip Seed", 4);
        
        // Test hot pepper (1 day growth but 2 images)
        System.out.println("\n--- HOT PEPPER SEED (1 day growth, but 2 images) ---");
        testGrowthProgression("Hot Pepper Seed", 1);
        
        // Test cauliflower (5 days growth and 6 images)
        System.out.println("\n--- CAULIFLOWER SEED (5 days growth, 6 images) ---");
        testGrowthProgression("Cauliflower Seed", 5);
    }
    
    private static void testGrowthProgression(String seedName, int daysToGrow) {
        System.out.println("Seed: " + seedName);
        System.out.println("Configured days to grow: " + daysToGrow);
        
        // Test all possible growth stages (1 to daysToGrow + 1 for harvest stage)
        for (int day = 0; day <= daysToGrow + 2; day++) {
            int growthStage = Math.min(day + 1, daysToGrow + 1); // +1 for harvest
            growthStage = Math.max(1, growthStage); // Minimum stage 1
            
            String imageName = getGrowthImageName(seedName, growthStage);
            BufferedImage image = loadGrowthSprite(seedName, imageName);
            
            String status = (image != null) ? "EXISTS" : "MISSING";
            System.out.println("  Day " + day + " -> Stage " + growthStage + " -> " + imageName + " [" + status + "]");
              if (image == null && growthStage <= daysToGrow) {
                System.out.println("    WARNING: Missing image for active growth stage!");
            }
        }
    }
    
    private static String getGrowthImageName(String seedName, int stage) {
        String cleanSeedName = seedName.replace(" Seed", "").toLowerCase();
        
        if (cleanSeedName.equals("hot pepper")) {
            cleanSeedName = "hot_pepper";
        } else {
            cleanSeedName = cleanSeedName.replace(" ", "");
        }
        
        return cleanSeedName + stage + ".png";
    }
    
    private static BufferedImage loadGrowthSprite(String seedName, String imageName) {
        String cacheKey = seedName + "_" + imageName;
        if (growthImageCache.containsKey(cacheKey)) {
            return growthImageCache.get(cacheKey);
        }
        
        try {
            String cleanSeedName = seedName.replace(" Seed", "").replace(" ", "").toUpperCase();
            
            if (cleanSeedName.equals("HOTPEPPER")) {
                cleanSeedName = "HOTPEPPER";
            }
            
            String imagePath = "RES/SEED/" + cleanSeedName + "/" + imageName;
            
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                return null;
            }
            
            BufferedImage image = ImageIO.read(imageFile);
            growthImageCache.put(cacheKey, image);
            return image;
        } catch (Exception e) {
            return null;
        }
    }
}
