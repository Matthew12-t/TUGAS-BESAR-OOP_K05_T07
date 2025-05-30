import java.io.File;

public class TEST_SeedImageCount {
    public static void main(String[] args) {
        System.out.println("=== COUNTING AVAILABLE GROWTH IMAGES FOR EACH SEED ===");
        
        // Check each seed folder
        String[] seedFolders = {"CAULIFLOWER", "PARSNIP", "HOTPEPPER"};
        
        for (String folder : seedFolders) {
            System.out.println("\n--- " + folder + " ---");
            File seedDir = new File("RES/SEED/" + folder);
            
            if (seedDir.exists() && seedDir.isDirectory()) {
                File[] files = seedDir.listFiles();
                int growthImageCount = 0;
                
                if (files != null) {
                    System.out.println("Files found:");
                    for (File file : files) {
                        if (file.getName().endsWith(".png") && !file.getName().contains("_seed")) {
                            System.out.println("  " + file.getName());
                            growthImageCount++;
                        }
                    }
                }
                
                System.out.println("Growth images available: " + growthImageCount);
                System.out.println("Recommended days to grow: " + (growthImageCount + 1));
                System.out.println("Logic: " + growthImageCount + " growth stages + 1 final harvest stage");
            } else {
                System.out.println("Folder not found: " + seedDir.getAbsolutePath());
            }
        }
    }
}
