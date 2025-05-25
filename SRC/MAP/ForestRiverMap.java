package SRC.MAP;

import SRC.MAIN.GamePanel;
import SRC.TILES.Tile;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

public class ForestRiverMap extends Map {
    public static final int FOREST_COLS = 8;
    public static final int FOREST_ROWS = 8;
    
    boolean loadSuccess = loadMapFromFile("RES/MAP_TXT/forestrivermap.txt");
    
    // To track which tiles have been drawn as part of large tiles
    private Set<String> drawnLargeTiles = new HashSet<>();
    
    /**
     * Constructor for the ForestRiverMap
     * @param gp GamePanel reference
     */
    public ForestRiverMap(GamePanel gp) {
        super(gp, "Forest River Map", FOREST_COLS, FOREST_ROWS, 10);

    }    /**
     * Initialize the forest map
     */    @Override
    protected void initializeMap() {
        // Start with default initialization
        super.initializeMap();
        
    }
    

  /**
     * Set up initial objects in the forest map based on forestrivermap.txt
     */    @Override
    public void setupInitialObjects() {
        // Jika file berhasil dimuat, gunakan data dari file
        if (loadSuccess) {

            for (int row = 0; row < FOREST_ROWS; row++) {
                for (int col = 0; col < FOREST_COLS; col++) {
                    // Periksa karakter dari file asli
                    char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/forestrivermap.txt");
                    
                    // Proses berdasarkan karakter dari file
                    if (mapChar == 'd') {
                        setTileInMap(col, row, Tile.TILE_FOREST_GRASS1);
                    } else if (mapChar == 'D') {
                        // Forest Grass 2 is a regular 1x1 tile
                        setTileInMap(col, row, Tile.TILE_FOREST_GRASS2);    
                    } else if (mapChar == 'a') {
                        // Use TILE_EDGE (visible 'a' characters) for the actual edges shown in the map file
                        setTileInMap(col, row, Tile.TILE_EDGE);
                    } else if (mapChar == 'b') {
                        // For Platform, set the tile type for all platform tiles
                        setTileInMap(col, row, Tile.TILE_PLATFORM);
                    } else if (mapChar == '1') {
                        // Set water tile and explicitly print collision status for debugging
                        setTileInMap(col, row, Tile.TILE_WATER);
                    } else if(mapChar == '5'){
                        setTileInMap(col, row, Tile.TILE_TELEPORT);
                        System.out.println("Set teleport tile at position (" + col + ", " + row + ")");
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
}