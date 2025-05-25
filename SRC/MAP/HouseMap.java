package SRC.MAP;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import SRC.MAIN.GamePanel;

import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;
public class HouseMap extends Map {
    // Size of the house map
    public static final int HOUSE_COLS = 24;
    public static final int HOUSE_ROWS = 24;

    // Set to keep track of which large tiles have already been drawn
    private Set<String> drawnLargeTiles = new HashSet<>();

    // Object deployer for placing objects on the map
    private ObjectDeployer objDeployer;

    /**
     * Constructor for the HouseMap
     * @param gp GamePanel reference
     */
    public HouseMap(GamePanel gp) {
        // House map is smaller than world map, allows up to 10 objects
        super(gp, "House Map", HOUSE_COLS, HOUSE_ROWS, 100);

        // Initialize object deployer
        objDeployer = new ObjectDeployer(gp);
    }

    /**
     * Initialize the house map with various types of tiles and objects from housemap.txt
     */    @Override    protected void initializeMap() {
        // Start with default initialization
        super.initializeMap();
        
        // Load map from file
        boolean loadSuccess = loadMapFromFile("RES/MAP_TXT/housemap.txt");
        
        if (loadSuccess) {
            System.out.println("Setting up tiles from housemap.txt...");
            
            // Process tiles based on the file content
            for (int row = 0; row < HOUSE_ROWS; row++) {
                for (int col = 0; col < HOUSE_COLS; col++) {
                    // Get character from the original file
                    char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/housemap.txt");
                    
                    // Process based on character
                    switch (mapChar) {
                        case '2':
                            setTileInMap(col, row, Tile.TILE_WALL1);
                            break;                        
                            case 'a':
                            // For large tile WALL2, just set the tile type
                            setTileInMap(col, row, Tile.TILE_WALL2);
                            break;
                        case 'z':
                            // For large tile WALL3, just set the tile type
                            setTileInMap(col, row, Tile.TILE_WALL3);
                            break;
                        default:
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            break;
                    }
                }
            }
        } else {
            System.out.println("Failed to load housemap.txt, using default floor tiles");
            // fallback: all floor
            for (int col = 0; col < HOUSE_COLS; col++) {
                for (int row = 0; row < HOUSE_ROWS; row++) {
                    setTileInMap(col, row, Tile.TILE_FLOOR);
                }
            }
        }
        // Add teleport tile in the middle of the last row
        int mid1 = (HOUSE_COLS / 2) - 1;
        int mid2 = (HOUSE_COLS / 2);
        setTileInMap(mid1, HOUSE_ROWS - 1, SRC.TILES.Tile.TILE_TELEPORT);
        setTileInMap(mid2, HOUSE_ROWS - 1, SRC.TILES.Tile.TILE_TELEPORT);
    }    /**
     * Draw the house map
     * @param g2 Graphics2D object for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        // Clear the set of drawn large tiles
        drawnLargeTiles.clear();
        
        // Process large tiles first
        for (int row = 0; row < HOUSE_ROWS; row++) {
            for (int col = 0; col < HOUSE_COLS; col++) {                // Get tile type at this position
                int tileType = mapTileData[col][row];
                
                // Skip if already part of a drawn large tile
                if (drawnLargeTiles.contains(tileType + "_" + col + "_" + row)) {
                    continue;
                }
                
                // Handle large tiles (WALL2, WALL3)
                if (tileType == Tile.TILE_WALL2 || tileType == Tile.TILE_WALL3) {
                    // Get dimensions for the large tile
                    int[] dimensions = Tile.getTileDimensions(tileType);
                    int width = dimensions[0];
                    int height = dimensions[1];
                    
                    // Calculate world coordinates
                    int worldX = col * gp.getTileSize();
                    int worldY = row * gp.getTileSize();
                    int screenX = worldX - gp.getCameraX();
                    int screenY = worldY - gp.getCameraY();
                    
                    // Only draw if the tile is at least partially visible on screen
                    if (worldX + gp.getTileSize() * width > gp.getCameraX() &&
                        worldX < gp.getCameraX() + gp.getScreenWidth() &&
                        worldY + gp.getTileSize() * height > gp.getCameraY() &&
                        worldY < gp.getCameraY() + gp.getScreenHeight()) {
                        
                        // Draw the large tile using the central method in Tile class
                        Tile.drawLargeTileByType(g2, screenX, screenY, gp.getTileSize(), tileType, width, height);
                    }
                    
                    // Mark all tiles covered by this large tile as drawn
                    for (int r = 0; r < height; r++) {
                        for (int c = 0; c < width; c++) {
                            if (col + c < HOUSE_COLS && row + r < HOUSE_ROWS) {
                                drawnLargeTiles.add(tileType + "_" + (col + c) + "_" + (row + r));
                            }
                        }
                    }                } else {
                    // Draw regular tiles immediately
                    drawTile(g2, col, row, tileType);
                }
            }
        }
        
        // Draw objects on the map (but not the tiles)
        drawObjects(g2);
    }
    
    /**
     * Set up initial objects in the house map (bed, table, chair) from housemap.txt
     */      @Override
    public void setupInitialObjects() {

        for (int row = 0; row < HOUSE_ROWS; row++) {
            for (int col = 0; col < HOUSE_COLS; col++) {
                // Get value (potentially multi-character) from the original file
                String mapValue = super.getMapFileValue(col, row, "RES/MAP_TXT/housemap.txt");
                
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
                switch (mapChar) {
                    case 'b':
                        objDeployer.deployBed(col, row);
                        break;
                    case 's':
                        objDeployer.deployStove(col, row);
                        break;
                    case 'r':
                        objDeployer.deployRak(col, row);
                        break;
                    case 't':
                        // Single 't' is a TV
                        objDeployer.deployTv(col, row);
                        break;
                }
            }
        }
    }
}

