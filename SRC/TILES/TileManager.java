package SRC.TILES;

import SRC.MAIN.GamePanel;
import SRC.MAP.Map;
import SRC.TIME.Time;
import java.util.HashMap;
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
        
        public PlantedTileInfo(String seedName, int plantedDay, int plantedHour, int daysToGrow) {
            this.seedName = seedName;
            this.plantedDay = plantedDay;
            this.plantedHour = plantedHour;
            this.daysToGrow = daysToGrow;
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
        return true;
    }
    
    // ===== PLANTED TILE GROWTH LOGIC =====
      /**
     * Hitung growth stage berdasarkan current time
     * @param plantInfo info tanaman
     * @return growth stage (0 = baru tanam, daysToGrow+1 = siap harvest)
     */
    private int calculateGrowthStage(PlantedTileInfo plantInfo) {
        // Get current game time
        int currentDay = gamePanel.getCurrentDay();
        Time currentTime = gamePanel.getCurrentTime();
        int currentHour = (currentTime != null) ? currentTime.getHour() : 6; // Default 6 AM
        
        // Hitung berapa hari sudah berlalu sejak ditanam
        int daysPassed = currentDay - plantInfo.plantedDay;
        
        // Jika belum 24 jam sejak ditanam, masih stage 0
        if (daysPassed == 0 && currentHour < plantInfo.plantedHour) {
            return 0;
        }
        
        // Jika sudah lewat jam tanam di hari yang sama, atau sudah hari berikutnya
        if (daysPassed == 0 && currentHour >= plantInfo.plantedHour) {
            return 1; // Stage pertama growth
        }
        
        // Untuk hari-hari berikutnya, growth stage bertambah
        return Math.min(daysPassed + 1, plantInfo.daysToGrow + 1);
    }
    
    /**
     * Check apakah tanaman sudah siap dipanen
     */
    private boolean isPlantReadyToHarvestInternal(PlantedTileInfo plantInfo) {
        return calculateGrowthStage(plantInfo) > plantInfo.daysToGrow;
    }
    
    /**
     * Get image file name berdasarkan growth stage
     */
    private String getGrowthImageName(PlantedTileInfo plantInfo) {
        int stage = calculateGrowthStage(plantInfo);
        
        // Nama file: namaseed1.png, namaseed2.png, dst
        String cleanSeedName = plantInfo.seedName.replace(" Seeds", "").replace(" ", "").toLowerCase();
        return cleanSeedName + stage + ".png";
    }
    
    // ===== TILE ACTIONS =====
    
    /**
     * Plant seed pada tilled tile
     */
    public boolean plantSeed(int col, int row, String seedName, int daysToGrow) {
        // Validasi: hanya bisa plant di Farm Map
        if (!gamePanel.getCurrentMap().getMapName().equals("Farm Map")) {
            System.out.println("DEBUG: Can only plant seeds in Farm Map!");
            return false;
        }
        
        // Validasi: tile harus tilled dan belum ada tanaman
        int tileType = gamePanel.getCurrentMap().getTile(col, row);
        if (tileType != Tile.TILE_TILLED) {
            System.out.println("DEBUG: Can only plant on tilled soil!");
            return false;
        }
        
        String tileKey = col + "," + row;
        if (plantedTiles.containsKey(tileKey)) {
            System.out.println("DEBUG: Tile already has a plant!");
            return false;
        }
        
        // Validasi: tidak ada collision
        if (gamePanel.getCurrentMap().getMapController().hasCollision(gamePanel.getCurrentMap(), col, row)) {
            System.out.println("DEBUG: Cannot plant - collision detected");
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
        
        System.out.println("DEBUG: Planted " + seedName + " at (" + col + ", " + row + ") on day " + 
                          currentDay + " at " + currentHour + ":00");
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
        
        // Get crop name dari seed name
        String cropName = plantInfo.seedName.replace(" Seeds", "");
        
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
    }
    
    /**
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
            String cleanSeedName = seedName.replace(" Seeds", "").replace(" ", "").toLowerCase();
            String imagePath = "RES/SEED/" + cleanSeedName + "/" + imageName;
            
            BufferedImage image = ImageIO.read(new File(imagePath));
            
            // Cache image untuk performance
            growthImageCache.put(cacheKey, image);
            
            return image;
        } catch (Exception e) {
            System.out.println("DEBUG: Could not load growth sprite: " + imageName + " for seed: " + seedName);
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
}
