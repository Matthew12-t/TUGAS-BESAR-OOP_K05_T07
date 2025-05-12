package SRC.MAP;

import java.awt.Graphics2D;
import SRC.MAIN.GamePanel;
import SRC.OBJECT.OBJ_House;
import SRC.TILES.Tile;

/**
 * FarmMap class represents a farming area map in the game
 */
public class FarmMap extends Map {    // Size of the farm (smaller than the world map)
    public static final int FARM_COLS = 16;
    public static final int FARM_ROWS = 16;
    
    // Farmland tracking
    private boolean[][] tilled; // Tracks which tiles are tilled/farmable
    
    /**
     * Constructor for the FarmMap
     * @param gp GamePanel reference
     */
    public FarmMap(GamePanel gp) {
        // Farm map is smaller than world map, allows up to 10 objects
        super(gp, "Farm Map", FARM_COLS, FARM_ROWS, 10);
        
        // Initialize tilled land tracker
        tilled = new boolean[FARM_COLS][FARM_ROWS];
    }    /**
     * Initialize the farm map dengan berbagai jenis tile
     */
    @Override
    protected void initializeMap() {
        // Start with default initialization (all grass)
        super.initializeMap();
        
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
    }
    /**
     * Method khusus untuk menempatkan farmhouse
     * Di farm map hanya diizinkan satu rumah
     * @param col Kolom untuk menempatkan rumah
     * @param row Baris untuk menempatkan rumah
     * @return true jika berhasil ditempatkan
     */
    public boolean deployFarmHouse(int col, int row) {
        // Cek apakah sudah ada rumah di farm
        boolean houseExists = false;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null && objects[i] instanceof OBJ_House) {
                houseExists = true;
                break;
            }
        }
        
        // Jika sudah ada rumah, hapus rumah yang lama
        if (houseExists) {
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] != null && objects[i] instanceof OBJ_House) {
                    objects[i] = null;
                    System.out.println("Existing house removed from Farm Map");
                    break;
                }
            }
        }
        
        // Gunakan metode dari parent class untuk menempatkan rumah baru
        return super.deployHouse(col, row);
    }
    
    /**
     * Set up initial objects in the farm map
     */    @Override
    public void setupInitialObjects() {
        // Add the initial farm house using proper deployment
        objects[0] = new OBJ_House(gp, FARM_COLS - 3, FARM_ROWS - 3);
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
