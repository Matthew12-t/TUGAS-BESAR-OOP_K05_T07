package SRC.MAP;

import SRC.MAIN.GamePanel;
import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;

/**
 * FarmMap class represents a farming area map in the game
 */
public class FarmMap extends Map {
    // Size of the farm (smaller than the world map)
    public static final int FARM_COLS = 32;
    public static final int FARM_ROWS = 32;    // Farmland tracking
    private boolean[][] tilled; // Tracks which tiles are tilled/farmable
    private boolean mapLoadedSuccessfully = false; // Track if map loaded successfully
    private String currentMapFilePath; // Store the path of the currently loaded map file
    
    // Object deployer for placing objects on the map
    private ObjectDeployer objDeployer;
    
    // Koordinat teleport ke HouseMap
    private int teleportToHouseCol = -2;
    private int teleportToHouseRow = -2;
    // Koordinat depan rumah (untuk spawn setelah keluar dari HouseMap)
    private int depanRumahCol = -1;    private int depanRumahRow = -1;
    private boolean foundHouse = false;

    /**
     * Constructor for the FarmMap with default map file
     * @param gp GamePanel reference
     */
    public FarmMap(GamePanel gp) {
        this(gp, "RES/MAP_TXT/farmmap.txt");
    }
    
    /**
     * Constructor for the FarmMap with specified map file
     * @param gp GamePanel reference
     * @param mapFilePath Path to the map txt file to load
     */    public FarmMap(GamePanel gp, String mapFilePath) {
        super(gp, "Farm Map", FARM_COLS, FARM_ROWS, 10);
        tilled = new boolean[FARM_COLS][FARM_ROWS];
        objDeployer = new ObjectDeployer(gp);
        
        this.currentMapFilePath = mapFilePath; // Store the map file path
        boolean loadSuccess = loadMapFromFile(mapFilePath);
        this.mapLoadedSuccessfully = loadSuccess;
        if (!loadSuccess) {
            System.err.println("Failed to load farm map from: " + mapFilePath);
            // Fallback to default map if loading fails
            this.currentMapFilePath = "RES/MAP_TXT/farmmap.txt";
            loadSuccess = loadMapFromFile("RES/MAP_TXT/farmmap.txt");
            this.mapLoadedSuccessfully = loadSuccess;
        }
    }
    
    /**
     * Initialize the farm map dengan berbagai jenis tile
     */
    @Override
    protected void initializeMap() {
        // Start with default initialization (all grass)
        super.initializeMap();
        // Tambahkan kolam kecil di pojok
        
        // Tambahkan area untuk tillable land (lahan yang siap diolah)
        for (int col = 5; col <= 10; col++) {
            for (int row = 5; row <= 10; row++) {
                setTileInMap(col, row, Tile.TILE_TILLABLE); // Tillable land
            }
        }
    }
    
    /**
     * Set up initial objects in the farm map based on farmmap.txt
     */    @Override
    public void setupInitialObjects() {
        // Jika file berhasil dimuat, gunakan data dari file
        if (mapLoadedSuccessfully) {
            System.out.println("Setting up objects from farm map file...");

            // Cari posisi house (h) di file
            for (int row = 0; row < FARM_ROWS; row++) {
                for (int col = 0; col < FARM_COLS; col++) {
                    int tileValue = getTile(col, row);

                    // Ubah path tile (8) sesuai dengan file
                    if (tileValue == 8) {
                        setTileInMap(col, row, Tile.TILE_PATH);
                    }
                    else if(tileValue ==5){
                        setTileInMap(col, row, Tile.TILE_TELEPORT);
                    }
                    if (tileValue == 0) {
                        setTileInMap(col, row, Tile.TILE_LAND); // Set sebagai land tile
                    }                    // Periksa karakter dari file asli
                    char mapChar = super.getMapFileChar(col, row, currentMapFilePath);

                    // Di dalam metode setupInitialObjects()
                    if (mapChar == 'h' && !foundHouse) {
                        System.out.println("Deploying house at " + col + "," + row);
                        objDeployer.deployHouse(col, row);
                        foundHouse = true; // Flag untuk mencegah rumah di-deploy lebih dari sekali

                        // Cari posisi bawah tengah rumah untuk teleport
                        int houseLeftCol = col;
                        int houseWidth = 6; // Ukuran rumah dari file (6 kolom)
                        int houseHeight = 6; // Ukuran rumah dari file (6 baris)

                        // Hitung posisi tengah rumah dan posisi bawah (untuk depan rumah)
                        int houseCenterCol = houseLeftCol + (houseWidth / 2) - 1; // Tengah rumah
                        int houseBottomRow = row + houseHeight - 1; // Baris terbawah rumah

                        // Set teleport di tengah depan rumah
                        teleportToHouseCol = houseCenterCol;
                        teleportToHouseRow = houseBottomRow + 1; // Satu tile di bawah rumah
                        setTileInMap(teleportToHouseCol, teleportToHouseRow, Tile.TILE_TELEPORT);
                        setTileInMap(teleportToHouseCol+1, teleportToHouseRow, Tile.TILE_TELEPORT);

                        // Set spawn point
                        depanRumahCol = teleportToHouseCol;
                        depanRumahRow = teleportToHouseRow ; // Satu tile di bawah teleport
                    } else if (mapChar == 'h') {
                        objDeployer.deployHouse(col, row);
                    } 
                    else if (mapChar == 's') {
                            objDeployer.deployShippingBin(col, row);
                        
                    } else if (mapChar == 'p') {
                            objDeployer.deployPond(col, row);                    
                    } else if (mapChar == 't') {
                        // Set tile sebagai tillable land
                        setTileInMap(col, row, Tile.TILE_TILLABLE);                    } else if (mapChar == 'o') {
                        // Get the full value to check for tree modes (o1, o2, etc.)
                        String mapValue = super.getMapFileValue(col, row, currentMapFilePath);
                        
                        if (mapValue.length() > 1 && mapValue.charAt(0) == 'o' && Character.isDigit(mapValue.charAt(1))) {
                            // Tree with mode (o1, o2, etc.)
                            int mode = Character.getNumericValue(mapValue.charAt(1));
                            System.out.println("Deploying tree mode " + mode + " at " + col + "," + row);
                            objDeployer.deployPohon(col, row, mode);
                        } else {
                            // Default tree without mode
                            System.out.println("Deploying default tree at " + col + "," + row);
                            objDeployer.deployPohon(col, row);
                        }
                    }
                }
            }
        } else {
            System.out.println("Failed to load map file, no objects deployed");
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
            
            // Bisa mengolah TILE_TILLABLE atau TILE_GRASS
            if (tileType != Tile.TILE_TILLABLE && tileType != Tile.TILE_GRASS) {
                return false;
            }
            
            // Cannot till where objects are placed
            if (hasCollision(col, row)) {
                return false;
            }
            
            // Set tile as tilled land
            setTileInMap(col, row, Tile.TILE_TILLED);
            tilled[col][row] = true;
            return true;
        }
        return false;
    }
   

    public int getTeleportToHouseCol() {
        return teleportToHouseCol;
    }

    public int getTeleportToHouseRow() {
        return teleportToHouseRow;
    }

    public int getDepanRumahCol() {
        return depanRumahCol;
    }    public int getDepanRumahRow() {
        return depanRumahRow;
    }
      /**
     * Find the teleport tile position that leads to world map (should be at x=31)
     * @return int array with [col, row] coordinates, or [-1, -1] if not found
     */
    public int[] getWorldMapTeleportPosition() {
        // Look for teleport tile at column 31 (world map teleport)
        for (int row = 0; row < FARM_ROWS; row++) {
            int tileType = getTile(31, row);
            if (tileType == Tile.TILE_TELEPORT) {
                return new int[]{31, row};
            }
        }
        return new int[]{-1, -1}; // Not found
    }
    
    /**
     * Get the spawn position when coming from world map (left side of world map teleport at x=30)
     * @return int array with [col, row] coordinates for player spawn
     */
    public int[] getWorldMapSpawnPosition() {
        int[] teleportPos = getWorldMapTeleportPosition();
        if (teleportPos[0] != -1 && teleportPos[1] != -1) {
            // Place player to the left of the world map teleport tile (x=30)
            return new int[]{30, teleportPos[1]};
        }
        // Fallback to default position if teleport not found
        return new int[]{30, 10}; // Default position at x=30, y=10
    }
}