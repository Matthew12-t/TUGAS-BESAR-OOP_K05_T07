package SRC.MAP;

import SRC.MAIN.GamePanel;
import SRC.TILES.Tile;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

public class OceanMap extends Map {
    public static final int OCEAN_COLS = 11;
    public static final int OCEAN_ROWS = 6;
    
    boolean loadSuccess = loadMapFromFile("RES/MAP_TXT/ocean.txt");
    
    // To track which tiles have been drawn as part of large tiles
    private Set<String> drawnLargeTiles = new HashSet<>();
    
    /**
     * Constructor for the OceanMap
     * @param gp GamePanel reference
     */
    public OceanMap(GamePanel gp) {
        super(gp, "Ocean Map", OCEAN_COLS, OCEAN_ROWS, 10);
    }
    
    /**
     * Initialize the ocean map
     */
    @Override
    protected void initializeMap() {
        // Start with default initialization
        super.initializeMap();
    }
  
    
    /**
     * Set up initial objects in the ocean map based on ocean.txt
     */
    @Override
    public void setupInitialObjects() {
        // If file was successfully loaded, use data from the file
        if (loadSuccess) {
            for (int row = 0; row < OCEAN_ROWS; row++) {
                for (int col = 0; col < OCEAN_COLS; col++) {
                    // Check character from original file
                    char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/ocean.txt");
                    
                    // Process based on character from file
                    if (mapChar == 'w') {
                        // Water3 tiles
                        setTileInMap(col, row, Tile.TILE_WATER3);
                    } else if (mapChar == 'i') {
                        // Island tiles (4x4 large tile)
                        setTileInMap(col, row, Tile.TILE_ISLAND);    
                    } else if (mapChar == 'b') {
                        // Bridge tiles (5x2 large tile)
                        setTileInMap(col, row, Tile.TILE_BRIDGE);
                    }else if (mapChar == '5') {
                        // Teleport tile
                        setTileInMap(col, row, Tile.TILE_TELEPORT);
                        System.out.println("Set teleport tile at position (" + col + ", " + row + ")");
                    }
                }
            }
        } else {
            System.out.println("Failed to load ocean map file, no objects deployed");
        }
    }
    
    /**
     * Override drawTiles to handle large tiles properly
     * @param g2 Graphics2D object for drawing
     */
    @Override
    protected void drawTiles(Graphics2D g2) {
        // Clear the tracking set for each drawing cycle
        drawnLargeTiles = new HashSet<>();
        
        // Determine the range of tiles to draw based on camera position
        int startCol = gp.getCameraX() / gp.getTileSize();
        int startRow = gp.getCameraY() / gp.getTileSize();
        
        // Draw one extra tile row/column to cover partially visible tiles at edges
        int endCol = (gp.getCameraX() + gp.getScreenWidth()) / gp.getTileSize() + 1;
        int endRow = (gp.getCameraY() + gp.getScreenHeight()) / gp.getTileSize() + 1;
        
        // Clamp the drawing range to map bounds
        if (endCol > maxCol) {
            endCol = maxCol;
        }
        if (endRow > maxRow) {
            endRow = maxRow;
        }
        
        // Draw visible tiles
        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                if (col >= 0 && row >= 0 && col < maxCol && row < maxRow) {
                    int tileType = mapTileData[col][row];
                      // Special handling for large tiles
                    if (Tile.isLargeTile(tileType)) {
                        // Get the dimensions of this tile type
                        int[] dimensions = Tile.getTileDimensions(tileType);
                        int width = dimensions[0];
                        int height = dimensions[1];
                        
                        // Create a unique key for this tile position
                        String tileKey = tileType + "_" + col + "_" + row;
                        
                        if (!drawnLargeTiles.contains(tileKey)) {
                            // Calculate tile's world and screen position
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
                                    if (col + c < maxCol && row + r < maxRow) {
                                        drawnLargeTiles.add(tileType + "_" + (col + c) + "_" + (row + r));
                                    }
                                }
                            }
                        }
                    } else {
                        // Regular tile drawing
                        drawTile(g2, col, row, tileType);
                    }
                }
            }
        }
    }
    
    /**
     * Override drawTile to handle teleport tiles with appropriate base for ocean maps
     * @param g2 Graphics2D object for drawing
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @param tileType Type of tile to draw
     */
    @Override
    protected void drawTile(Graphics2D g2, int col, int row, int tileType) {
        // Calculate tile's world position
        int worldX = col * gp.getTileSize();
        int worldY = row * gp.getTileSize();
        
        // Calculate tile's screen position (relative to camera view)
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();
        
        // Only draw if the tile is visible on screen
        if (worldX + gp.getTileSize() > gp.getCameraX() &&
            worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
            worldY + gp.getTileSize() > gp.getCameraY() &&
            worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {
                
            // Special handling for teleport tiles in ocean maps
            if (tileType == Tile.TILE_TELEPORT) {
                Tile.makeTeleportTile(g2, screenX, screenY, gp.getTileSize(), "water"); // Use ocean map base (water)
            } else {
                // Use standard drawing for other tile types
                Tile.drawTileByType(g2, screenX, screenY, gp.getTileSize(), tileType);
            }
        }
    }
}
