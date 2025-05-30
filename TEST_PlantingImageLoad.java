/**
 * Test to verify image loading during actual planting process
 */
import SRC.TILES.TileManager;
import java.io.File;

public class TEST_PlantingImageLoad {
    public static void main(String[] args) {
        System.out.println("=== TESTING PLANTED TILE IMAGE LOADING ===");
        
        // Test what happens when TileManager tries to load growth images
        testImageLoadingLogic();
        
        System.out.println("\n=== VERIFYING IMAGE PATHS ===");
        verifyImagePaths();
    }
    
    private static void testImageLoadingLogic() {
        System.out.println("\n--- Testing TileManager Image Loading Logic ---");
        
        // Simulate the logic from TileManager.getGrowthImageName()
        String[] testSeeds = {"Cauliflower Seed", "Parsnip Seed", "Hot Pepper Seed"};
        
        for (String seedName : testSeeds) {
            System.out.println("\nTesting seed: " + seedName);
            
            // Simulate different growth stages (1-5)
            for (int stage = 1; stage <= 5; stage++) {
                String imageName = getImageName(seedName, stage);
                String folderName = getFolderName(seedName);
                String fullPath = "RES/SEED/" + folderName + "/" + imageName;
                
                System.out.println("  Stage " + stage + ": " + fullPath);
                
                // Check if file exists
                File imageFile = new File(fullPath);                if (imageFile.exists()) {
                    System.out.println("    [OK] File exists");
                } else {
                    System.out.println("    [X] File NOT found");
                }
            }
        }
    }
    
    // Replicate the logic from TileManager.getGrowthImageName()
    private static String getImageName(String seedName, int stage) {
        // Nama file: namaseed1.png, namaseed2.png, dst
        // Contoh: "Cauliflower Seed" -> "cauliflower1.png"
        // Special case untuk "Hot Pepper Seed" -> "hot_pepper1.png"
        String cleanSeedName = seedName.replace(" Seed", "").toLowerCase();
        
        // Handle special cases untuk nama dengan spasi
        if (cleanSeedName.equals("hot pepper")) {
            cleanSeedName = "hot_pepper"; // Gunakan underscore
        } else {
            cleanSeedName = cleanSeedName.replace(" ", ""); // Remove spasi untuk yang lain
        }
        
        return cleanSeedName + stage + ".png";
    }
    
    // Replicate the logic from TileManager.loadGrowthSprite()
    private static String getFolderName(String seedName) {
        String cleanSeedName = seedName.replace(" Seed", "").replace(" ", "").toUpperCase();
        
        // Special mapping untuk nama folder yang berbeda
        if (cleanSeedName.equals("HOTPEPPER")) {
            cleanSeedName = "HOTPEPPER"; // Folder tetap HOTPEPPER
        }
        
        return cleanSeedName;
    }
    
    private static void verifyImagePaths() {
        System.out.println("\n--- Verifying Actual Image Files ---");
        
        // Check the actual RES/SEED structure
        File seedDir = new File("RES/SEED");        if (!seedDir.exists()) {
            System.out.println("[X] RES/SEED directory does not exist");
            return;
        }
        
        System.out.println("[OK] RES/SEED directory exists");
        
        // List all folders in RES/SEED
        File[] folders = seedDir.listFiles(File::isDirectory);        if (folders != null) {
            for (File folder : folders) {
                System.out.println("\n[FOLDER] " + folder.getName());
                
                // List all PNG files in each folder
                File[] pngFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
                if (pngFiles != null) {
                    for (File pngFile : pngFiles) {
                        System.out.println("  [IMG] " + pngFile.getName());
                    }
                    
                    if (pngFiles.length == 0) {
                        System.out.println("  [WARNING] No PNG files found");
                    }
                } else {
                    System.out.println("  [X] Cannot read folder contents");
                }
            }
        }
    }
}
