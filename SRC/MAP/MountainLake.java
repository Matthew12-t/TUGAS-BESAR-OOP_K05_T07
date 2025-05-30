package SRC.MAP;

import SRC.MAIN.GamePanel;
import SRC.TILES.Tile;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

/**
 * MountainLake Map class represents the mountain lake area in the game
 */
public class MountainLake extends Map {
    public static final int MOUNTAIN_COLS = 8;
    public static final int MOUNTAIN_ROWS = 8;
    
    boolean loadSuccess = loadMapFromFile("RES/MAP_TXT/mountainlake.txt");
    
    // To track which tiles have been drawn as part of large tiles
    private Set<String> drawnLargeTiles = new HashSet<>();
    
    /**
     * Constructor for the MountainLake
     * @param gp GamePanel reference
     */
    public MountainLake(GamePanel gp) {
        super(gp, "Mountain Lake", MOUNTAIN_COLS, MOUNTAIN_ROWS, 10);
    }
    
    /**
     * Initialize the mountain lake map
     */
    @Override
    protected void initializeMap() {
        // Start with default initialization
        super.initializeMap();
        
        // We don't need explicit edge tiles thanks to out-of-bounds collision detection
        // in the Map.java's getCollisionBounds method
    }
    
    
    /**
     * Set up initial objects in the mountain lake map based on mountainlake.txt
     */
    @Override
    public void setupInitialObjects() {
        // If file was successfully loaded, use data from the file
        if (loadSuccess) {

            for (int row = 0; row < MOUNTAIN_ROWS; row++) {
                for (int col = 0; col < MOUNTAIN_COLS; col++) {
                    // Check character from original file
                    char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/mountainlake.txt");
                    
                    // Process based on character from file
                    if (mapChar == 'd') {
                        setTileInMap(col, row, Tile.TILE_GRASS);
                    } else if (mapChar == 'D') {
                        // Forest Grass 2 is a regular 1x1 tile
                        setTileInMap(col, row, Tile.TILE_GRASSEDGE);    
                    } else if (mapChar == 'a') {
                        // Use TILE_EDGE for visible edge tiles
                        setTileInMap(col, row, Tile.TILE_EDGE2);
                    } else if (mapChar == 'b') {
                        // For Platform tiles
                        setTileInMap(col, row, Tile.TILE_PLATFORM);
                    } else if (mapChar == '1') {
                        // Water tiles
                        setTileInMap(col, row, Tile.TILE_WATER2);
                    } else if (mapChar == '5') {
                        // Teleport tile
                        setTileInMap(col, row, Tile.TILE_TELEPORT);
                        System.out.println("Set teleport tile at position (" + col + ", " + row + ")");
                    } else if (mapChar == 'p') {
                        // Path tiles (new in mountain lake map)
                        setTileInMap(col, row, Tile.TILE_PATH2);
                    }
                }
            }
        } else {
            System.out.println("Failed to load map file, no objects deployed");
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
     * Override drawTile to handle teleport tiles with mountain-specific base
     * @param g2 Graphics2D object for drawing
     * @param col Column position
     * @param row Row position
     * @param tileType Type of tile to draw
     */
    @Override
    public void drawTile(Graphics2D g2, int col, int row, int tileType) {
        // Calculate screen position
        int worldX = col * gp.getTileSize();
        int worldY = row * gp.getTileSize();
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();
        
        // Special handling for teleport tiles in mountain maps
        if (tileType == Tile.TILE_TELEPORT) {
            Tile.makeTeleportTile(g2, screenX, screenY, gp.getTileSize(), "mountain"); // Use mountain map base
        } else {
            // Use standard drawing for other tile types
            Tile.drawTileByType(g2, screenX, screenY, gp.getTileSize(), tileType);
        }
    }

}
