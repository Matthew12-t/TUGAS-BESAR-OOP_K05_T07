import SRC.DATA.SeedData;
import SRC.ITEMS.Seed;
import SRC.TILES.TileManager;
import java.io.File;
import java.awt.image.BufferedImage;

/**
 * Final verification that planting system works correctly
 * with current SeedData configuration
 */
public class TEST_FinalPlantingVerification {
    public static void main(String[] args) {
        System.out.println("=== FINAL PLANTING SYSTEM VERIFICATION ===\n");
        
        // Test seeds that we know have images
        String[] testSeeds = {"Parsnip Seed", "Hot Pepper Seed", "Cauliflower Seed"};
        
        for (String seedName : testSeeds) {
            System.out.println("Testing: " + seedName);
            
            Seed seed = SeedData.getSeed(seedName);
            if (seed == null) {
                System.out.println("  ERROR: Seed not found in SeedData!");
                continue;
            }
            
            int daysToGrow = seed.getDaysToGrow();
            int expectedImages = daysToGrow + 1; // N days = N+1 images
            
            System.out.println("  Days to grow: " + daysToGrow);
            System.out.println("  Expected images: " + expectedImages);
            
            // Check if we can load all expected images
            boolean allImagesAvailable = true;
            String folderName = seedName.replace(" Seed", "").toUpperCase();
            String baseName = seedName.replace(" Seed", "").toLowerCase().replace(" ", "_");
            
            for (int stage = 1; stage <= expectedImages; stage++) {
                String imagePath = "RES/SEED/" + folderName + "/" + baseName + stage + ".png";
                File imageFile = new File(imagePath);
                
                if (imageFile.exists()) {
                    System.out.println("    Stage " + stage + ": " + imagePath + " ✓");
                } else {
                    System.out.println("    Stage " + stage + ": " + imagePath + " ✗ MISSING");
                    allImagesAvailable = false;
                }
            }
            
            // Test image loading through TileManager
            System.out.println("  Testing TileManager loading:");
            try {
                TileManager tileManager = new TileManager(null);
                
                for (int stage = 1; stage <= expectedImages; stage++) {
                    String imagePath = "RES/SEED/" + folderName + "/" + baseName + stage + ".png";
                    BufferedImage image = tileManager.loadImage(imagePath);
                    
                    if (image != null) {
                        System.out.println("    TileManager loaded stage " + stage + ": " + image.getWidth() + "x" + image.getHeight() + " ✓");
                    } else {
                        System.out.println("    TileManager failed to load stage " + stage + " ✗");
                        allImagesAvailable = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("    TileManager error: " + e.getMessage());
                allImagesAvailable = false;
            }
            
            System.out.println("  Result: " + (allImagesAvailable ? "✓ ALL WORKING" : "✗ ISSUES FOUND"));
            System.out.println();
        }
        
        System.out.println("=== CONFIGURATION SUMMARY ===");
        System.out.println("Rule: N days to grow = N+1 image files");
        System.out.println("- Parsnip: 1 day = 2 images (parsnip1.png, parsnip2.png)");
        System.out.println("- Hot Pepper: 1 day = 2 images (hot_pepper1.png, hot_pepper2.png)");
        System.out.println("- Cauliflower: 5 days = 6 images (cauliflower1.png...cauliflower6.png)");
        System.out.println("\nIf all tests show ✓, the planting system is working correctly!");
    }
}
