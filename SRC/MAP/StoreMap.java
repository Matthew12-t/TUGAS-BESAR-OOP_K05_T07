package SRC.MAP;
import SRC.MAIN.GamePanel;
import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;

public class StoreMap extends Map {    // Size of the store map
    public static final int STORE_COLS = 12;
    public static final int STORE_ROWS = 9;

    // Object deployer for placing objects on the map
    private ObjectDeployer objDeployer;

    /**
     * Constructor for the StoreMap
     * @param gp GamePanel reference
     */
    public StoreMap(GamePanel gp) {
        // Store map allows up to 100 objects
        super(gp, "Store Map", STORE_COLS, STORE_ROWS, 100);

        // Initialize object deployer
        objDeployer = new ObjectDeployer(gp);
    }    /**
     * Initialize the store map with various types of tiles from store.txt
     */
    @Override
    protected void initializeMap() {
        // Don't call super.initializeMap() to avoid grass default
        
        // Initialize map tile data array
        // This is normally done in the parent constructor, but we need to ensure it's clean
        
        // Load map from file
        boolean loadSuccess = loadMapFromFile("RES/MAP_TXT/store.txt");
        
        if (loadSuccess) {
            System.out.println("Setting up tiles from store.txt...");
            
            // Process tiles based on the file content
            for (int row = 0; row < STORE_ROWS; row++) {
                for (int col = 0; col < STORE_COLS; col++) {
                    // Get character from the original file
                    char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/store.txt");                    
                    
                    // Debug print
                    System.out.println("Position (" + col + "," + row + "): char = '" + mapChar + "'");
                    
                    // Process based on character
                    switch (mapChar) {
                        case 'w':
                            setTileInMap(col, row, Tile.TILE_WALL1);
                            System.out.println("Set WALL at (" + col + "," + row + ")");
                            break;
                        case '0':
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            System.out.println("Set FLOOR at (" + col + "," + row + ")");
                            break;
                        case 'c':
                        case 'd':
                        case 's':
                        case 'S':
                            // For object positions, also use floor tile as base
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            System.out.println("Set FLOOR (object position) at (" + col + "," + row + ") for char '" + mapChar + "'");
                            break;
                        default:
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            System.out.println("Set FLOOR (default) at (" + col + "," + row + ") for char '" + mapChar + "'");
                            break;
                    }
                }
            }
        } else {
            System.out.println("Failed to load store.txt, using all floor tiles");
            // Fallback: all floor tiles
            for (int col = 0; col < STORE_COLS; col++) {
                for (int row = 0; row < STORE_ROWS; row++) {
                    setTileInMap(col, row, Tile.TILE_FLOOR);
                }
            }
        }
        
        // Add teleport tile in the middle of the last row
        int mid1 = (STORE_COLS / 2) - 1;
        int mid2 = (STORE_COLS / 2);
        setTileInMap(mid1, STORE_ROWS - 1, Tile.TILE_TELEPORT);
        setTileInMap(mid2, STORE_ROWS - 1, Tile.TILE_TELEPORT);
    }/**
     * Set up initial objects in the store map based on predefined positions
     */
    @Override
    public void setupInitialObjects() {
        // Deploy objects at specific positions based on the store layout
        // These positions correspond to the top-left corner of each object
        
        // StoreDecoration4 (1x1) - 'S' at position (5,1)
        objDeployer.deployStoreDecoration4(5, 1);
        System.out.println("Deployed StoreDecoration4 at (5,1)");
        
        // StoreDecoration3 (3x1) - 's s s' at position (3,2)
        objDeployer.deployStoreDecoration3(3, 2);
        System.out.println("Deployed StoreDecoration3 at (3,2)");
        
        // StoreDecoration2 (1x5) - 'd' column at position (1,3)
        objDeployer.deployStoreDecoration2(1, 3);
        System.out.println("Deployed StoreDecoration2 at (1,3)");
        
        // StoreDecoration1 (3x2) - multiple 'c c c' blocks
        // First block at (7,2)
        objDeployer.deployStoreDecoration1(7, 2);
        System.out.println("Deployed StoreDecoration1 at (7,2)");
        
        // Second block at (7,5)
        objDeployer.deployStoreDecoration1(7, 5);
        System.out.println("Deployed StoreDecoration1 at (7,5)");
    }}
