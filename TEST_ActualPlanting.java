import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

/**
 * Test untuk mensimulasikan actual planting scenario dan melihat image mana yang ter-load
 */
public class TEST_ActualPlanting {
    
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
        System.out.println("=== ACTUAL PLANTING SIMULATION TEST ===");
        
        // Simulasi dari SeedData.java yang sudah diperbaiki
        System.out.println("\n--- PARSNIP SEED (3 days configured) ---");
        simulatePlantingDays("Parsnip Seed", 3);
        
        System.out.println("\n--- HOT PEPPER SEED (2 days configured) ---");
        simulatePlantingDays("Hot Pepper Seed", 2);
        
        System.out.println("\n--- CAULIFLOWER SEED (6 days configured) ---");
        simulatePlantingDays("Cauliflower Seed", 6);
    }
    
    private static void simulatePlantingDays(String seedName, int daysToGrow) {
        System.out.println("Seed: " + seedName + " (Days to grow: " + daysToGrow + ")");
        
        // Simulasi dari hari ke-0 sampai beberapa hari setelah harvest
        for (int currentDay = 1; currentDay <= daysToGrow + 3; currentDay++) {
            MockPlantedTileInfo plantInfo = new MockPlantedTileInfo(seedName, daysToGrow, 1); // planted on day 1
            
            // Simulasi logic dari TileManager.calculateGrowthStage()
            int daysPassed = currentDay - plantInfo.plantedDay;
            int actualGrowthDays = plantInfo.isWatered ? daysPassed + 1 : daysPassed;
            int growthStage = Math.min(actualGrowthDays, plantInfo.daysToGrow + 1);
            growthStage = Math.max(1, growthStage);
            
            // Get image name yang akan dicari
            String imageName = getGrowthImageName(plantInfo, growthStage);
            
            // Coba load image
            BufferedImage image = loadGrowthSprite(seedName, imageName);
            String status = (image != null) ? "LOADED" : "FAILED";
            
            String harvestStatus = (growthStage >= daysToGrow + 1) ? " [READY TO HARVEST]" : "";
            
            System.out.println("  Day " + currentDay + ": Stage " + growthStage + " -> " + imageName + " [" + status + "]" + harvestStatus);
            
            // Jika gagal load, coba lihat folder contents
            if (image == null) {
                showAvailableImages(seedName);
                break; // Stop pada error pertama untuk seed ini
            }
        }
    }
    
    private static void showAvailableImages(String seedName) {
        String cleanSeedName = seedName.replace(" Seed", "").replace(" ", "").toUpperCase();
        if (cleanSeedName.equals("HOTPEPPER")) {
            cleanSeedName = "HOTPEPPER";
        }
        
        File folder = new File("RES/SEED/" + cleanSeedName);
        if (folder.exists() && folder.isDirectory()) {
            System.out.println("    Available images in folder:");
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().endsWith(".png") && !f.getName().contains("_seed")) {
                        System.out.println("      " + f.getName());
                    }
                }
            }
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
