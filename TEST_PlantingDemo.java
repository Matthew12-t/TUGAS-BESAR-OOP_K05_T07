import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple test to demonstrate the planted tile image loading issue
 */
public class TEST_PlantingDemo {
    
    static class MockPlantedTileInfo {
        String seedName;
        int daysToGrow;
        int plantedDay;
        boolean isWatered;
        
        MockPlantedTileInfo(String seedName, int daysToGrow, int plantedDay) {
            this.seedName = seedName;
            this.daysToGrow = daysToGrow;
            this.plantedDay = plantedDay;
            this.isWatered = true;
        }
    }
    
    private static Map<String, BufferedImage> growthImageCache = new HashMap<>();
    
    public static void main(String[] args) {
        System.out.println("=== PLANTED TILE IMAGE LOADING TEST ===");
        
        // Test different seeds and growth stages
        testSeedImageLoading("Cauliflower Seed", 3, 1);
        testSeedImageLoading("Parsnip Seed", 2, 1);
        testSeedImageLoading("Hot Pepper Seed", 1, 1);
        
        // Test different growth stages for cauliflower
        System.out.println("\n=== TESTING DIFFERENT GROWTH STAGES ===");
        for (int stage = 1; stage <= 6; stage++) {
            testGrowthStage("Cauliflower Seed", stage);
        }
    }
    
    private static void testSeedImageLoading(String seedName, int daysToGrow, int currentDay) {
        System.out.println("\n--- Testing: " + seedName + " ---");
        MockPlantedTileInfo plantInfo = new MockPlantedTileInfo(seedName, daysToGrow, currentDay);
        
        // Simulate growth stage calculation
        int daysPassed = currentDay - plantInfo.plantedDay;
        int actualGrowthDays = plantInfo.isWatered ? daysPassed + 1 : daysPassed;
        int growthStage = Math.min(actualGrowthDays, plantInfo.daysToGrow + 1);
        growthStage = Math.max(1, growthStage);
        
        System.out.println("Days to grow: " + daysToGrow);
        System.out.println("Current growth stage: " + growthStage);
        
        // Get image name
        String imageName = getGrowthImageName(plantInfo, growthStage);
        System.out.println("Looking for image: " + imageName);
        
        // Try to load the image
        BufferedImage image = loadGrowthSprite(seedName, imageName);        if (image != null) {
            System.out.println("SUCCESS: Image loaded successfully");
        } else {
            System.out.println("FAILED: Could not load image");
        }
    }
    
    private static void testGrowthStage(String seedName, int stage) {
        String imageName = getGrowthImageNameForStage(seedName, stage);
        System.out.println("Stage " + stage + ": " + imageName);
        
        BufferedImage image = loadGrowthSprite(seedName, imageName);        if (image != null) {
            System.out.println("  EXISTS");
        } else {
            System.out.println("  MISSING");
        }
    }
    
    private static String getGrowthImageName(MockPlantedTileInfo plantInfo, int stage) {
        String cleanSeedName = plantInfo.seedName.replace(" Seed", "").toLowerCase();
        
        if (cleanSeedName.equals("hot pepper")) {
            cleanSeedName = "hot_pepper";
        } else {
            cleanSeedName = cleanSeedName.replace(" ", "");
        }
        
        return cleanSeedName + stage + ".png";
    }
    
    private static String getGrowthImageNameForStage(String seedName, int stage) {
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
            System.out.println("  Trying path: " + imagePath);
            
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.out.println("  File does not exist: " + imagePath);
                return null;
            }
            
            BufferedImage image = ImageIO.read(imageFile);
            growthImageCache.put(cacheKey, image);
            
            System.out.println("  Successfully loaded: " + imagePath);
            return image;
        } catch (Exception e) {
            System.out.println("  Error loading image: " + e.getMessage());
            return null;
        }
    }
}
