import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

/**
 * Test the fixed configuration with proper days to grow matching available images
 */
public class TEST_FixedConfiguration {
    
    static class SeedConfig {
        String name;
        int daysToGrow;
        int expectedImages;
        
        SeedConfig(String name, int daysToGrow) {
            this.name = name;
            this.daysToGrow = daysToGrow;
            this.expectedImages = daysToGrow - 1; // days - 1 = number of growth images
        }
    }
    
    private static Map<String, BufferedImage> growthImageCache = new HashMap<>();
    
    public static void main(String[] args) {
        System.out.println("=== TESTING FIXED SEED CONFIGURATION ===");
        System.out.println("Logic: Days to grow = Number of growth images + 1 (final harvest stage)");
        
        // Test the fixed configurations
        SeedConfig[] configs = {
            new SeedConfig("Parsnip Seed", 3),        // Should have 2 images
            new SeedConfig("Cauliflower Seed", 7),    // Should have 6 images
            new SeedConfig("Hot Pepper Seed", 3)      // Should have 2 images
        };
        
        for (SeedConfig config : configs) {
            testSeedConfiguration(config);
        }
    }
    
    private static void testSeedConfiguration(SeedConfig config) {
        System.out.println("\n--- " + config.name + " ---");
        System.out.println("Configured days to grow: " + config.daysToGrow);
        System.out.println("Expected growth images: " + config.expectedImages);
        
        // Count actual available images
        String folderName = config.name.replace(" Seed", "").replace(" ", "").toUpperCase();
        if (folderName.equals("HOTPEPPER")) {
            folderName = "HOTPEPPER";
        }
        
        File seedDir = new File("RES/SEED/" + folderName);
        int actualImages = 0;
        
        if (seedDir.exists() && seedDir.isDirectory()) {
            File[] files = seedDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".png") && !file.getName().contains("_seed")) {
                        actualImages++;
                    }
                }
            }
        }
        
        System.out.println("Actual images available: " + actualImages);
        
        if (actualImages == config.expectedImages) {
            System.out.println("STATUS: PERFECT MATCH!");
        } else {
            System.out.println("STATUS: MISMATCH - Expected " + config.expectedImages + ", Found " + actualImages);
        }
        
        // Test growth progression
        System.out.println("Growth progression test:");
        for (int day = 0; day <= config.daysToGrow + 1; day++) {
            int growthStage = Math.min(day + 1, config.daysToGrow + 1);
            growthStage = Math.max(1, growthStage);
            
            String imageName = getGrowthImageName(config.name, growthStage);
            BufferedImage image = loadGrowthSprite(config.name, imageName);
            
            String status = (image != null) ? "EXISTS" : "MISSING";
            String warning = "";
            
            if (image == null && growthStage <= config.daysToGrow) {
                warning = " [ERROR!]";
            } else if (image == null && growthStage > config.daysToGrow) {
                warning = " [OK - Beyond growth period]";
            }
            
            System.out.println("  Day " + day + " -> Stage " + growthStage + " -> " + imageName + " [" + status + "]" + warning);
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
