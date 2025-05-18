package SRC.MAP;

import java.awt.Graphics2D;
import SRC.MAIN.GamePanel;
import SRC.OBJECT.OBJ_House;
import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;

/**
 * FarmMap class represents a farming area map in the game
 */
public class FarmMap extends Map {
    // Size of the farm (smaller than the world map)
    public static final int FARM_COLS = 32;
    public static final int FARM_ROWS = 32;
    
    // Farmland tracking
    private boolean[][] tilled; // Tracks which tiles are tilled/farmable
    
    // Object deployer for placing objects on the map
    private ObjectDeployer objDeployer;
    
    /**
     * Constructor for the FarmMap
     * @param gp GamePanel reference
     */
    public FarmMap(GamePanel gp) {
        // Farm map is smaller than world map, allows up to 10 objects
        super(gp, "Farm Map", FARM_COLS, FARM_ROWS, 10);
        
        // Initialize tilled land tracker
        tilled = new boolean[FARM_COLS][FARM_ROWS];
        
        // Initialize object deployer
        objDeployer = new ObjectDeployer(gp);
    }
    
    /**
     * Initialize the farm map dengan berbagai jenis tile
     */
    @Override
    protected void initializeMap() {
        // Start with default initialization (all grass)
        super.initializeMap();
        boolean loadSuccess = loadMapFromFile("RES/map.txt");
        // Tambahkan kolam kecil di pojok
        for (int col = 1; col <= 3; col++) {
            for (int row = 1; row <= 3; row++) {
                setTile(col, row, Tile.TILE_WATER); // Water tile
            }
        }
        // Tambahkan area untuk tillable land (lahan yang siap diolah)
        for (int col = 5; col <= 10; col++) {
            for (int row = 5; row <= 10; row++) {
                setTile(col, row, Tile.TILE_TILLABLE); // Tillable land
            }
        }
        // Tambahkan contoh tilled land (lahan yang sudah diolah)
        setTile(5, 5, Tile.TILE_TILLED);
        setTile(6, 5, Tile.TILE_TILLED);
        // Tambahkan contoh planted land (lahan yang sudah ditanami)
        setTile(5, 6, Tile.TILE_PLANTED);
        // Tambahkan tile teleport di ujung kanan untuk ke WorldMap
        for (int row = 0; row < FARM_ROWS; row++) {
            setTile(FARM_COLS - 1, row, Tile.TILE_TELEPORT);
        }
    }
    
    /**
     * Set up initial objects in the farm map
     */
    @Override
    public void setupInitialObjects() {
        // Add 1 house at a random valid position
        int attempts = 0;
        boolean houseDeployed = false;

        while (!houseDeployed && attempts < 100) {
            // Random position for house, avoiding edges
            int houseCol = 5 + (int) (Math.random() * (FARM_COLS - 15)); // Leave space for house (6x6) and shipping bin
            int houseRow = 5 + (int) (Math.random() * (FARM_ROWS - 10));

            // Try to place house
            if (isValidPlacement(houseCol, houseRow)) {
                objDeployer.deployHouse(houseCol, houseRow);
                houseDeployed = true;

                // Place shipping bin 2 tiles to the right of the house
                // House is 6x6, so move 8 tiles right (6 + 2 gap)
                objDeployer.deployShippingBin(houseCol + 8, houseRow);
            }

            attempts++;
        }

        // Add 2-3 ponds at random valid positions
        boolean pondDeployed = false;
        attempts = 0; // Reset attempts counter for pond placement

        while (!pondDeployed && attempts < 100) { // Add maximum attempts limit
            // Random position for pond
            int pondCol = 2 + (int) (Math.random() * (FARM_COLS - 6)); // Leave space for pond (4x3)
            int pondRow = 2 + (int) (Math.random() * (FARM_ROWS - 5));

            // Try to place pond
            if (isValidPlacement(pondCol, pondRow)) {
                objDeployer.deployPond(pondCol, pondRow);
                pondDeployed = true; // Set to true after successful deployment
            }

            attempts++;
        }
    }
    
    /**
     * Tills a specific tile to make it farmable
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return true if the tile was successfully tilled
     */
    public boolean tillSoil(int col, int row) {
        if (col >= 0 && col < FARM_COLS && row >= 0 && row < FARM_ROWS) {
            int tileType = getTile(col, row);
            
            // Hanya bisa mengolah TILE_TILLABLE
            if (tileType != Tile.TILE_TILLABLE) {
                return false;
            }
            
            // Cannot till where objects are placed
            if (hasCollision(col, row)) {
                return false;
            }
            
            // Set tile as tilled land
            setTile(col, row, Tile.TILE_TILLED);
            tilled[col][row] = true;
            return true;
        }
        return false;
    }
    
    /**
     * Check if a tile is tilled/farmable
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return true if the tile is tilled
     */
    public boolean isTilled(int col, int row) {
        if (col >= 0 && col < FARM_COLS && row >= 0 && row < FARM_ROWS) {
            return tilled[col][row];
        }
        return false;
    }
    
    /**
     * Menanam seed di tilled land
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return true if the seed was successfully planted
     */
    public boolean plantSeed(int col, int row) {
        if (col >= 0 && col < FARM_COLS && row >= 0 && row < FARM_ROWS) {
            int tileType = getTile(col, row);
            
            // Hanya bisa menanam di TILE_TILLED
            if (tileType != Tile.TILE_TILLED) {
                return false;
            }
            
            // Set tile sebagai planted land
            setTile(col, row, Tile.TILE_PLANTED);
            return true;
        }
        return false;
    }
    
    /**
     * Draw custom tiles for the farm map
     * Overrides the parent method to also draw tilled soil
     */
    @Override
    protected void drawTile(Graphics2D g2, int col, int row, int tileType) {
        // Draw the tile using drawTileByType method
        int screenX = col * gp.getTileSize() - gp.getCameraX();
        int screenY = row * gp.getTileSize() - gp.getCameraY();
        
        // Gunakan metode dari Tile untuk menggambar tile sesuai tipenya
        Tile.drawTileByType(g2, screenX, screenY, gp.getTileSize(), tileType);
    }
}