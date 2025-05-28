package SRC.MAP.NPC_HOUSE;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import SRC.MAIN.GamePanel;
import SRC.MAP.Map;
import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;

/**
 * Base class for all NPC House Maps
 * Provides common functionality for 13x9 NPC houses
 */
public abstract class NPCHouseMap extends Map {
    // Size of NPC house maps (13x9)
    public static final int NPC_HOUSE_COLS = 13;
    public static final int NPC_HOUSE_ROWS = 9;    // Set to keep track of which large tiles have already been drawn
    protected Set<String> drawnLargeTiles = new HashSet<>();
    
    // Set to keep track of which large objects have already been deployed
    protected Set<String> deployedLargeObjects = new HashSet<>();

    // Object deployer for placing objects on the map
    protected ObjectDeployer objDeployer;/**
     * Constructor for NPCHouseMap
     * @param gp GamePanel reference
     * @param houseName Name of the house (e.g., "Abigail's House")
     */
    public NPCHouseMap(GamePanel gp, String houseName) {
        // NPC house allows up to 50 objects
        super(gp, houseName, NPC_HOUSE_COLS, NPC_HOUSE_ROWS, 50);

        // Initialize object deployer
        objDeployer = new ObjectDeployer(gp);
    }

    /**
     * Initialize the NPC house map with tiles from the specified txt file
     * @param mapFileName The txt file to load (e.g., "abigailhousemap.txt")
     */
    protected void initializeFromFile(String mapFileName) {
        // Start with default initialization
        super.initializeMap();
        
        // Load map from file
        boolean loadSuccess = loadMapFromFile("RES/MAP_TXT/" + mapFileName);
        
        if (loadSuccess) {
            System.out.println("Setting up tiles from " + mapFileName + "...");
            
            // Process tiles based on the file content
            for (int row = 0; row < NPC_HOUSE_ROWS; row++) {
                for (int col = 0; col < NPC_HOUSE_COLS; col++) {
                    // Get character from the original file
                    char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/" + mapFileName);
                    
                    // Process based on character
                    switch (mapChar) {
                        case '2':
                            setTileInMap(col, row, Tile.TILE_WALL1);
                            break;                        
                        default:
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            break;
                    }
                }
            }
        } else {
            System.out.println("Failed to load " + mapFileName + ", using default floor tiles");
            // fallback: all floor
            for (int col = 0; col < NPC_HOUSE_COLS; col++) {
                for (int row = 0; row < NPC_HOUSE_ROWS; row++) {
                    setTileInMap(col, row, Tile.TILE_FLOOR);
                }
            }
        }
        
        // Add teleport tile in the middle of the last row
        int mid1 = (NPC_HOUSE_COLS / 2) - 1;
        int mid2 = (NPC_HOUSE_COLS / 2);
        setTileInMap(mid1, NPC_HOUSE_ROWS - 1, Tile.TILE_TELEPORT);
        setTileInMap(mid2, NPC_HOUSE_ROWS - 1, Tile.TILE_TELEPORT);
    }    /**
     * Set up initial objects in the NPC house based on the map file
     * @param mapFileName The txt file to read objects from
     */
    protected void setupObjectsFromFile(String mapFileName) {
        // Clear the set of deployed large objects for a fresh start
        deployedLargeObjects.clear();
        
        for (int row = 0; row < NPC_HOUSE_ROWS; row++) {
            for (int col = 0; col < NPC_HOUSE_COLS; col++) {
                // Get value (potentially multi-character) from the original file
                String mapValue = super.getMapFileValue(col, row, "RES/MAP_TXT/" + mapFileName);
                
                if (mapValue.length() == 0) {
                    continue;
                }
                
                // Check for multi-character furniture codes
                if (mapValue.length() > 1) {
                    if (mapValue.charAt(0) == 'c' && mapValue.length() == 2 && Character.isDigit(mapValue.charAt(1))) {
                        // Chair with mode (c1, c2, c3, c4)
                        int mode = Character.getNumericValue(mapValue.charAt(1));
                        System.out.println("Deploying chair mode " + mode + " at " + col + "," + row);
                        objDeployer.deployChair(col, row, mode);
                        continue;
                    } else if (mapValue.charAt(0) == 't' && mapValue.length() == 2 && Character.isDigit(mapValue.charAt(1))) {
                        // Table with mode (t1, t2)
                        int mode = Character.getNumericValue(mapValue.charAt(1));
                        System.out.println("Deploying table mode " + mode + " at " + col + "," + row);
                        objDeployer.deployTable(col, row, mode);
                        continue;
                    }
                }
                  // Handle single-character objects
                char mapChar = mapValue.charAt(0);
                final int finalCol = col;
                final int finalRow = row;
                
                switch (mapChar) {
                    case 'b':
                        // Bed is 2x4 large object - use tracking to prevent duplicates
                        deployLargeObject("bed", finalCol, finalRow, 2, 4, () -> objDeployer.deployBed(finalCol, finalRow));
                        break;
                    case 's':
                        objDeployer.deployStove(finalCol, finalRow);
                        break;
                    case 'r':
                        objDeployer.deployRak(finalCol, finalRow);
                        break;
                    case 't':
                        // Single 't' is a TV
                        objDeployer.deployTv(finalCol, finalRow);
                        break;
                    case 'C':
                        // Chimney is 2x3 large object - use tracking to prevent duplicates
                        deployLargeObject("chimney", finalCol, finalRow, 2, 3, () -> objDeployer.deployChimney(finalCol, finalRow));
                        break;
                }
            }
        }
    }

    /**
     * Helper method to deploy large objects only once using tracking
     * @param objectType Type of object (for tracking key)
     * @param col Column position
     * @param row Row position  
     * @param width Width of the object
     * @param height Height of the object
     * @param deployAction Action to perform the actual deployment
     */
    private void deployLargeObject(String objectType, int col, int row, int width, int height, Runnable deployAction) {
        // Check if this object region has already been deployed
        for (int r = row; r < row + height; r++) {
            for (int c = col; c < col + width; c++) {
                String key = objectType + "_" + c + "_" + r;
                if (deployedLargeObjects.contains(key)) {
                    // This object was already deployed
                    return;
                }
            }
        }
        
        // Mark all tiles of this object as deployed
        for (int r = row; r < row + height; r++) {
            for (int c = col; c < col + width; c++) {
                String key = objectType + "_" + c + "_" + r;
                deployedLargeObjects.add(key);
            }
        }
        
        // Deploy the object
        deployAction.run();
    }

    /**
     * Draw the NPC house map
     * @param g2 Graphics2D object for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        // Clear the set of drawn large tiles
        drawnLargeTiles.clear();
        
        // Draw tiles
        drawTiles(g2);
        
        // Draw objects on top of tiles
        drawObjects(g2);
    }
}
