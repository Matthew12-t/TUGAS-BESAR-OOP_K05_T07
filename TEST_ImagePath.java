import java.io.File;

public class TEST_ImagePath {
    public static void main(String[] args) {
        // Test path generation for different seeds
        String[] seedNames = {"Parsnip Seed", "Cauliflower Seed", "Hot Pepper Seed"};
        
        for (String seedName : seedNames) {
            System.out.println("\n=== Testing: " + seedName + " ===");
            
            // Current logic from TileManager
            String cleanSeedNameForFile = seedName.replace(" Seed", "").toLowerCase();
            if (cleanSeedNameForFile.equals("hot pepper")) {
                cleanSeedNameForFile = "hot_pepper";
            } else {
                cleanSeedNameForFile = cleanSeedNameForFile.replace(" ", "");
            }
            
            String cleanSeedNameForFolder = seedName.replace(" Seed", "").replace(" ", "").toUpperCase();
            if (cleanSeedNameForFolder.equals("HOTPEPPER")) {
                cleanSeedNameForFolder = "HOTPEPPER";
            }
            
            for (int stage = 1; stage <= 3; stage++) {
                String imageName = cleanSeedNameForFile + stage + ".png";
                String imagePath = "RES/SEED/" + cleanSeedNameForFolder + "/" + imageName;
                
                File imageFile = new File(imagePath);
                boolean exists = imageFile.exists();
                
                System.out.println("Stage " + stage + ": " + imagePath + " -> " + (exists ? "EXISTS" : "NOT FOUND"));
                
                if (!exists) {
                    // Check if file exists with different naming
                    File folder = new File("RES/SEED/" + cleanSeedNameForFolder);
                    if (folder.exists() && folder.isDirectory()) {
                        System.out.println("  Folder contents:");
                        File[] files = folder.listFiles();
                        if (files != null) {
                            for (File f : files) {
                                if (f.getName().endsWith(".png")) {
                                    System.out.println("    " + f.getName());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
