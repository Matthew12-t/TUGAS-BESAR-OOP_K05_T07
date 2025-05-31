package SRC.TILES;

import SRC.MAIN.GamePanel;
import SRC.MAP.Map;
import SRC.TIME.Time;
import SRC.DATA.SeedData;
import SRC.ITEMS.Seed;
import SRC.SEASON.Season;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Color;

/**
 * TileManager handles tile operations including tilling and planted tile growth
 */
public class TileManager {
    private GamePanel gamePanel;
      // ===== PLANTED TILE MANAGEMENT =====
      // Simple planted tile data structure
    public static class PlantedTileInfo {
        public String seedName;        // Nama seed yang ditanam
        public int plantedDay;         // Hari game saat ditanam  
        public int plantedHour;        // Jam game saat ditanam
        public int daysToGrow;         // Total hari untuk harvest
        public boolean isWatered;      // Apakah sudah disiram hari ini
        public int lastWateredDay;     // Hari terakhir disiram
        public HashSet<Integer> wateredDays; // Track all days when plant was watered
          public PlantedTileInfo(String seedName, int plantedDay, int plantedHour, int daysToGrow) {
            this.seedName = seedName;
            this.plantedDay = plantedDay;
            this.plantedHour = plantedHour;
            this.daysToGrow = daysToGrow;
            this.isWatered = true; // Start with watered status (free watering on planting day)
            this.lastWateredDay = plantedDay; // Last watered day is the planting day
            this.wateredDays = new HashSet<>();
            this.wateredDays.add(plantedDay); // Add planting day as watered (free watering)
        }
    }
    
    // Storage untuk planted tiles - key: "col,row"
    private HashMap<String, PlantedTileInfo> plantedTiles;
    
    // Cache untuk growth images
    private HashMap<String, BufferedImage> growthImageCache;
    
    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.plantedTiles = new HashMap<>();
        this.growthImageCache = new HashMap<>();
    }
    
    /**
     * Till a land tile to convert it to tilled soil
     * @param col Column position
     * @param row Row position
     * @return true if tilling was successful
     */
    public boolean tillTile(int col, int row) {
        Map currentMap = gamePanel.getCurrentMap();
        
        // Check if we're in Farm Map
        if (!currentMap.getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Tilling only available in Farm Map");
            return false;
        }
        
        // Check if tile is valid land tile
        int currentTileType = currentMap.getTile(col, row);
        if (currentTileType != Tile.TILE_LAND) {
            System.out.println("DEBUG: Can only till land tiles (type " + Tile.TILE_LAND + "), current type: " + currentTileType);
            return false;
        }
        
        // Check for collisions with shipping bin and pond objects
        if (gamePanel.getCurrentMap().getMapController().hasCollision(gamePanel.getCurrentMap(), col, row)) {
            System.out.println("DEBUG: Cannot till - collision with object detected");
            return false;
        }
        
        // Perform tilling - convert land to tilled soil
        currentMap.setTileInMap(col, row, Tile.TILE_TILLED);
        System.out.println("DEBUG: Successfully tilled tile at (" + col + ", " + row + ")");
        return true;
    }
    
    /**
     * Recover tilled soil back to land using Pickaxe
     * @param col Column position
     * @param row Row position
     * @return true if land recovery was successful
     */
    public boolean recoverLand(int col, int row) {
        Map currentMap = gamePanel.getCurrentMap();
        
        // Check if we're in Farm Map
        if (!currentMap.getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Land recovery only available in Farm Map");
            return false;
        }
        
        // Check if tile is valid tilled tile
        int currentTileType = currentMap.getTile(col, row);
        if (currentTileType != Tile.TILE_TILLED) {
            System.out.println("DEBUG: Can only recover tilled soil (type " + Tile.TILE_TILLED + "), current type: " + currentTileType);
            return false;
        }
        
        // Check for collisions with shipping bin and pond objects
        if (gamePanel.getCurrentMap().getMapController().hasCollision(gamePanel.getCurrentMap(), col, row)) {
            System.out.println("DEBUG: Cannot recover land - collision with object detected");
            return false;
        }
        
        // Perform land recovery - convert tilled soil back to land
        currentMap.setTileInMap(col, row, Tile.TILE_LAND);
        System.out.println("DEBUG: Successfully recovered land at (" + col + ", " + row + ")");
        return true;    }
    
    // ===== PLANTED TILE GROWTH LOGIC =====
    
    /**
     * Calculate growth stage based on current day and watering status
     * Growth happens when day changes (if watered on the previous day)
     * @param plantInfo plant information
     * @return growth stage (1 = newly planted, daysToGrow+1 = ready to harvest)
     */
    private int calculateGrowthStage(PlantedTileInfo plantInfo) {
        // Get current game time
        int currentDay = gamePanel.getCurrentDay();
        
        // Update watering status berdasarkan weather
        updateWateringFromWeather(plantInfo, currentDay);
        
        // Calculate how many days have passed since planting
        int daysPassed = currentDay - plantInfo.plantedDay;
        
        // If negative days (should not happen), return stage 1
        if (daysPassed < 0) {
            return 1;
        }
          // Growth stage calculation (MODIFIED: plants need watering every 2 days):
        // - Stage 1: Day of planting (day 0)
        // - Stage 2: Day 2 after planting (if watered on day 0 or day 1)
        // - Stage 3: Day 4 after planting (if watered on day 2 or day 3)
        // - etc.
        
        int growthStage = 1; // Start at stage 1 (newly planted)
        
        // For each 2-day period that has passed, check if plant was watered at least once
        // and advance growth stage if it was
        for (int period = 0; period < (daysPassed + 1) / 2; period++) {
            // Calculate which days to check for watering (2-day periods)
            int firstDayToCheck = plantInfo.plantedDay + (period * 2);
            int secondDayToCheck = firstDayToCheck + 1;
            
            // Check if plant was watered during either day in this 2-day period
            boolean wateredInPeriod = wasPlantWateredOnDay(plantInfo, firstDayToCheck) || 
                                      wasPlantWateredOnDay(plantInfo, secondDayToCheck);
            
            if (wateredInPeriod) {
                growthStage++;
            }
            // If not watered in this 2-day period, growth stage doesn't advance
            
            // Stop if we've reached maximum growth stage
            if (growthStage > plantInfo.daysToGrow + 1) {
                break;
            }
        }
        
        // Cap at maximum growth stage (daysToGrow + 1 for harvest stage)
        return Math.min(growthStage, plantInfo.daysToGrow + 1);
    }    /**
     * Cek apakah tanaman disiram pada hari tertentu
     */
    private boolean wasPlantWateredOnDay(PlantedTileInfo plantInfo, int day) {
        // Tanaman dianggap disiram jika:
        // 1. Hari tersebut ada dalam Set wateredDays (manual watering atau rainy day)
        // 2. Hari pertama ditanam sudah otomatis included di wateredDays
        
        return plantInfo.wateredDays.contains(day);
    }
      /**
     * Update watering status berdasarkan weather saat ini
     */
    private void updateWateringFromWeather(PlantedTileInfo plantInfo, int currentDay) {
        // Jika hari ini rainy, semua tanaman auto-watered
        if (gamePanel.getWeather().name().equals("RAINY") && 
            plantInfo.lastWateredDay < currentDay) {
            plantInfo.isWatered = true;
            plantInfo.lastWateredDay = currentDay;
            // Add rainy day to watered days Set
            plantInfo.wateredDays.add(currentDay);
        }
        
        // Reset isWatered jika sudah ganti hari
        if (plantInfo.lastWateredDay < currentDay) {
            plantInfo.isWatered = false;
        }
    }
    
    /**
     * Check apakah tanaman sudah siap dipanen
     */
    private boolean isPlantReadyToHarvestInternal(PlantedTileInfo plantInfo) {
        return calculateGrowthStage(plantInfo) > plantInfo.daysToGrow;
    }    /**
     * Get image file name berdasarkan growth stage
     */
    private String getGrowthImageName(PlantedTileInfo plantInfo) {
        int stage = calculateGrowthStage(plantInfo);
        
        // Pastikan minimum stage = 1 untuk ada image
        stage = Math.max(1, stage);
        
        // Nama file: namaseed1.png, namaseed2.png, dst
        // Contoh: "Cauliflower Seed" -> "cauliflower1.png"
        // Special case untuk "Hot Pepper Seed" -> "hot_pepper1.png"
        String cleanSeedName = plantInfo.seedName.replace(" Seed", "").toLowerCase();
        
        // Handle special cases untuk nama dengan spasi
        if (cleanSeedName.equals("hot pepper")) {
            cleanSeedName = "hot_pepper"; // Gunakan underscore
        } else {
            cleanSeedName = cleanSeedName.replace(" ", ""); // Remove spasi untuk yang lain
        }
        
        String imageName = cleanSeedName + stage + ".png";
        
        // Debug output
        System.out.println("DEBUG: Growth stage for " + plantInfo.seedName + " = " + stage);
        System.out.println("DEBUG: Looking for image: " + imageName);
        
        return imageName;
    }
    
    // ===== TILE ACTIONS =====
    
    /**
     * Plant seed pada tilled tile
     */
    public boolean plantSeed(int col, int row, String seedName, int daysToGrow) {
        // Validasi: hanya bisa plant di Farm Map
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            return false;
        }
        
        // Validasi: tile harus tilled dan belum ada tanaman
        int tileType = gamePanel.getCurrentMap().getTile(col, row);
        if (tileType != Tile.TILE_TILLED) {
            return false;
        }
        
        String tileKey = col + "," + row;
        if (plantedTiles.containsKey(tileKey)) {
            return false;
        }
        
        // Validasi: tidak ada collision
        if (gamePanel.getCurrentMap().getMapController().hasCollision(gamePanel.getCurrentMap(), col, row)) {
            return false;
        }

          // Get current game time
        int currentDay = gamePanel.getCurrentDay();
        Time currentTime = gamePanel.getCurrentTime();
        int currentHour = (currentTime != null) ? currentTime.getHour() : 6; // Default 6 AM
        
        // Create planted tile info
        PlantedTileInfo plantInfo = new PlantedTileInfo(seedName, currentDay, currentHour, daysToGrow);
        
        // Store planted tile data
        plantedTiles.put(tileKey, plantInfo);
        
        // Update tile type to PLANTED
        gamePanel.getCurrentMap().setTileInMap(col, row, Tile.TILE_PLANTED);
        
        System.out.println("DEBUG: Planted " + seedName + " at (" + col + ", " + row + ") on day " + currentDay + " at " + currentHour + ":00");
        return true;
    }
    
    /**
     * Harvest crop dari planted tile
     */
    public String harvestCrop(int col, int row) {
        String tileKey = col + "," + row;
        PlantedTileInfo plantInfo = plantedTiles.get(tileKey);
        
        if (plantInfo == null) {
            System.out.println("DEBUG: No plant at this tile!");
            return null;
        }
          // Check apakah siap harvest
        if (!isPlantReadyToHarvestInternal(plantInfo)) {
            System.out.println("DEBUG: Plant is not ready to harvest yet!");
            return null;
        }
        
        // Get crop name dari seed name (fix: use " Seed" not " Seeds")
        String cropName = plantInfo.seedName.replace(" Seed", "");
        
        // Remove planted tile data
        plantedTiles.remove(tileKey);
        
        // Reset tile back to tilled
        gamePanel.getCurrentMap().setTileInMap(col, row, Tile.TILE_TILLED);
        
        System.out.println("DEBUG: Harvested " + cropName + " from (" + col + ", " + row + ")");
        return cropName;
    }
    
    // ===== RENDERING =====
    
    /**
     * Draw planted tile dengan growth stage
     */
    public void drawPlantedTileGrowth(Graphics2D g2, int screenX, int screenY, int col, int row) {
        String tileKey = col + "," + row;
        PlantedTileInfo plantInfo = plantedTiles.get(tileKey);
        
        if (plantInfo == null) {
            // Fallback ke tilled tile jika tidak ada plant info
            drawBaseTilledTile(g2, screenX, screenY);
            return;
        }
        
        // Draw base tilled soil
        drawBaseTilledTile(g2, screenX, screenY);
        
        // Get growth image name
        String growthImageName = getGrowthImageName(plantInfo);
        
        // Load and draw growth sprite
        BufferedImage growthSprite = loadGrowthSprite(plantInfo.seedName, growthImageName);
        
        if (growthSprite != null) {
            // Draw plant sprite di atas soil
            g2.drawImage(growthSprite, screenX, screenY, gamePanel.getTileSize(), gamePanel.getTileSize(), null);
        } else {
            // Fallback jika sprite tidak ditemukan
            g2.setColor(Color.GREEN);
            g2.fillOval(screenX + 10, screenY + 10, 20, 20); // Green circle placeholder
        }
        
        // Debug info
        int growthStage = calculateGrowthStage(plantInfo);
        if (isPlantReadyToHarvestInternal(plantInfo)) {
            g2.setColor(Color.YELLOW);
            g2.drawString("HARVEST!", screenX + 5, screenY + 15);
        } else {
            g2.setColor(Color.WHITE);
            g2.drawString("Stage " + growthStage, screenX + 5, screenY + 15);
        }
    }    /**
     * Load growth sprite dengan caching
     */
    private BufferedImage loadGrowthSprite(String seedName, String imageName) {
        // Check cache dulu
        String cacheKey = seedName + "_" + imageName;
        if (growthImageCache.containsKey(cacheKey)) {
            return growthImageCache.get(cacheKey);
        }
        
        try {
            // Path structure: RES/SEED/[SEED_NAME]/[imageName]
            // Contoh: "Cauliflower Seed" -> "RES/SEED/CAULIFLOWER/cauliflower1.png"
            String cleanSeedName = seedName.replace(" Seed", "").replace(" ", "").toUpperCase();
            
            // Special mapping untuk nama folder yang berbeda
            if (cleanSeedName.equals("HOTPEPPER")) {
                cleanSeedName = "HOTPEPPER"; // Folder tetap HOTPEPPER
            }
            
            String imagePath = "RES/SEED/" + cleanSeedName + "/" + imageName;
            
            // Debug output
            System.out.println("DEBUG: Trying to load image from path: " + imagePath);
            
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.out.println("DEBUG: Image file does not exist: " + imagePath);
                return null;
            }
            
            BufferedImage image = ImageIO.read(imageFile);
            
            // Cache image untuk performance
            growthImageCache.put(cacheKey, image);
            
            System.out.println("DEBUG: Successfully loaded image: " + imagePath);
            return image;
        } catch (Exception e) {
            System.out.println("DEBUG: Could not load growth sprite: " + imageName + " for seed: " + seedName);
            System.out.println("DEBUG: Full path attempted: RES/SEED/" + seedName.replace(" Seed", "").replace(" ", "").toUpperCase() + "/" + imageName);
            System.out.println("DEBUG: Error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Draw base tilled tile
     */
    private void drawBaseTilledTile(Graphics2D g2, int screenX, int screenY) {
        // Draw brown tilled soil
        g2.setColor(new Color(101, 67, 33)); // Brown color for tilled soil
        g2.fillRect(screenX, screenY, gamePanel.getTileSize(), gamePanel.getTileSize());
        
        // Add tilled texture lines
        g2.setColor(new Color(80, 50, 25)); // Darker brown for lines
        for (int i = 0; i < 3; i++) {
            g2.drawLine(screenX, screenY + (i * gamePanel.getTileSize() / 3), 
                       screenX + gamePanel.getTileSize(), screenY + (i * gamePanel.getTileSize() / 3));
        }
    }
    
    // ===== PUBLIC QUERY METHODS =====
    
    /**
     * Check apakah tile memiliki tanaman
     */
    public boolean hasPlantAt(int col, int row) {
        String tileKey = col + "," + row;
        return plantedTiles.containsKey(tileKey);
    }
    
    /**
     * Check apakah tanaman siap dipanen
     */
    public boolean isPlantReadyToHarvest(int col, int row) {
        String tileKey = col + "," + row;
        PlantedTileInfo plantInfo = plantedTiles.get(tileKey);
        
        if (plantInfo == null) return false;
        
        return isPlantReadyToHarvestInternal(plantInfo);
    }
    
    /**
     * Check if plant is already watered today
     */
    public boolean isPlantWateredToday(int col, int row) {
        String tileKey = col + "," + row;
        PlantedTileInfo plantInfo = plantedTiles.get(tileKey);
        
        if (plantInfo == null) return false;
        
        int currentDay = gamePanel.getCurrentDay();
        return plantInfo.isWatered && plantInfo.lastWateredDay == currentDay;
    }    /**
     * Water a plant manually
     */
    public boolean waterPlant(int col, int row) {
        String tileKey = col + "," + row;
        PlantedTileInfo plantInfo = plantedTiles.get(tileKey);
        
        if (plantInfo == null) {
            System.out.println("DEBUG: No plant found at (" + col + ", " + row + ") to water");
            return false;
        }
        
        int currentDay = gamePanel.getCurrentDay();
        
        // Update watering status
        plantInfo.isWatered = true;
        plantInfo.lastWateredDay = currentDay;
        
        // Add current day to watered days Set (this fixes the bug!)
        plantInfo.wateredDays.add(currentDay);
        
        System.out.println("DEBUG: Plant watered at (" + col + ", " + row + ") on day " + currentDay);
        System.out.println("DEBUG: Plant watered days: " + plantInfo.wateredDays);
        return true;
    }
    
    /**
     * Get planted tile info untuk debugging
     */
    public PlantedTileInfo getPlantedTileInfo(int col, int row) {
        String tileKey = col + "," + row;
        return plantedTiles.get(tileKey);
    }
    
    /**
     * Get total planted tiles count
     */
    public int getTotalPlantedTiles() {
        return plantedTiles.size();
    }
    
    /**
     * Find all plant coordinates where the seed season doesn't match the current season
     * @return List of coordinate strings in format "col,row" for plants with mismatched seasons
     */
    public List<String> getMismatchedSeasonPlants() {
        List<String> mismatchedCoordinates = new ArrayList<>();
        
        // Get current season from GamePanel
        Season currentSeason = gamePanel.getSeason();
        String currentSeasonName = currentSeason.getDisplayName();
        
        // Check each planted tile
        for (String coordinate : plantedTiles.keySet()) {
            PlantedTileInfo plantInfo = plantedTiles.get(coordinate);
            
            // Get seed data to check season requirement
            Seed seedData = SeedData.getSeed(plantInfo.seedName);
            
            if (seedData != null) {
                String requiredSeason = seedData.getSeason();
                
                // Compare seed's required season with current season
                // Skip if seed season is "Any" or matches current season
                if (!requiredSeason.equalsIgnoreCase("Any") && 
                    !requiredSeason.equalsIgnoreCase(currentSeasonName)) {
                    
                    mismatchedCoordinates.add(coordinate);
                    
                    // Debug output
                    System.out.println("DEBUG: Mismatched season plant found at " + coordinate + 
                                     " - Seed: " + plantInfo.seedName + 
                                     " (requires " + requiredSeason + 
                                     ") but current season is " + currentSeasonName);
                }
            }
        }
        
        System.out.println("DEBUG: Found " + mismatchedCoordinates.size() + 
                          " plants with mismatched seasons");
        
        return mismatchedCoordinates;
    }
    
    /**
     * Get detailed information about plants with mismatched seasons
     * @return List of strings with detailed info about each mismatched plant
     */
    public List<String> getMismatchedSeasonPlantsDetails() {
        List<String> detailsList = new ArrayList<>();
        
        // Get current season from GamePanel
        Season currentSeason = gamePanel.getSeason();
        String currentSeasonName = currentSeason.getDisplayName();
        
        // Check each planted tile
        for (String coordinate : plantedTiles.keySet()) {
            PlantedTileInfo plantInfo = plantedTiles.get(coordinate);
            
            // Get seed data to check season requirement
            Seed seedData = SeedData.getSeed(plantInfo.seedName);
            
            if (seedData != null) {
                String requiredSeason = seedData.getSeason();
                
                // Compare seed's required season with current season
                // Skip if seed season is "Any" or matches current season
                if (!requiredSeason.equalsIgnoreCase("Any") && 
                    !requiredSeason.equalsIgnoreCase(currentSeasonName)) {
                    
                    String[] coords = coordinate.split(",");
                    String details = String.format("Plant at (%s, %s): %s (needs %s season, current: %s)", 
                                                  coords[0], coords[1], 
                                                  plantInfo.seedName, 
                                                  requiredSeason, 
                                                  currentSeasonName);
                    detailsList.add(details);
                }
            }
        }
        
        return detailsList;
    }
    
    /**
     * Remove all plants with mismatched seasons and reset their tiles to tilled soil
     * @return Number of plants removed
     */
    public int removeMismatchedSeasonPlants() {
        List<String> mismatchedCoordinates = getMismatchedSeasonPlants();
        int removedCount = 0;
        
        // Remove each mismatched plant
        for (String coordinate : mismatchedCoordinates) {
            String[] coords = coordinate.split(",");
            int col = Integer.parseInt(coords[0]);
            int row = Integer.parseInt(coords[1]);
            
            // Get plant info before removing for logging
            PlantedTileInfo plantInfo = plantedTiles.get(coordinate);
            
            // Remove plant data
            plantedTiles.remove(coordinate);
              // Reset tile back to tilled soil (use direct farm map reference)
            gamePanel.getFarmMap().setTileInMap(col, row, Tile.TILE_TILLED);
            
            removedCount++;
            
            // Log the removal
            if (plantInfo != null) {
                System.out.println("DEBUG: Removed mismatched season plant at (" + col + ", " + row + 
                                 "): " + plantInfo.seedName);
            }
        }
        
        if (removedCount > 0) {
            System.out.println("DEBUG: Successfully removed " + removedCount + 
                             " plants with mismatched seasons");
        } else {
            System.out.println("DEBUG: No mismatched season plants found to remove");
        }
        
        return removedCount;
    }
}
