package SRC.MAP;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import SRC.MAIN.GamePanel;
import SRC.OBJECT.SuperObject;
import SRC.TILES.Tile;

/**
 * Abstract base class for all maps in the game
 */
public abstract class Map {
    protected GamePanel gp;
    protected int[][] mapTileData;
    protected SuperObject[] objects;
    protected int maxCol;
    protected int maxRow;
    protected String mapName;
    protected boolean isActive; // Tracks if this map is currently active
    protected MapController mapController; // Controller for non-GUI logic
    
    /**
     * Constructor for the Map class
     * @param gp GamePanel reference
     * @param mapName Name of the map
     * @param maxCol Maximum number of columns in the map
     * @param maxRow Maximum number of rows in the map
     * @param maxObjects Maximum number of objects allowed on this map
     */    
    
     public Map(GamePanel gp, String mapName, int maxCol, int maxRow, int maxObjects) {
        this.gp = gp;
        this.mapName = mapName;
        this.maxCol = maxCol;
        this.maxRow = maxRow;
        this.mapTileData = new int[maxCol][maxRow];
        this.objects = new SuperObject[maxObjects];
        this.mapController = new MapController(gp);
        
        // Initialize the map with default tiles
        initializeMap();
    }
    
    /**
     * Initialize the map with default tiles
     * Subclasses can override this to set up specific map layouts
     */
    protected void initializeMap() {
        // Default implementation: all grass tiles
        for (int col = 0; col < maxCol; col++) {
            for (int row = 0; row < maxRow; row++) {
                mapTileData[col][row] = 0; 
            }
        }
    }
    
    /**
     * Draw the map (tiles and objects)
     * @param g2 Graphics2D object for drawing
     */
    public void draw(Graphics2D g2) {
        // Draw the map tiles
        drawTiles(g2);
        
        // Draw the objects on the map
        drawObjects(g2);
    }
    
    /**
     * Draw the map tiles
     * @param g2 Graphics2D object for drawing
     */
    protected void drawTiles(Graphics2D g2) {
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
                    drawTile(g2, col, row, tileType);
                }
            }
        }
    }
      /**
     * Draw a specific tile at the given position
     * @param g2 Graphics2D object for drawing
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @param tileType Type of tile to draw
     */
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
              // Special handling for planted tiles to use dynamic growth system
            if (tileType == Tile.TILE_PLANTED) {
                // Use GamePanel's shared TileManager instance for growth rendering
                SRC.TILES.TileManager tileManager = gp.getTileManager();
                
                // Use delegation method for dynamic growth rendering
                Tile.drawPlantedTileGrowth(g2, screenX, screenY, gp.getTileSize(), tileManager, col, row);
            } else {
                // Draw other tile types using the standard method
                Tile.drawTileByType(g2, screenX, screenY, gp.getTileSize(), tileType);
            }
        }
    }
    
    /**
     * Draw the objects on the map
     * @param g2 Graphics2D object for drawing
     */
    protected void drawObjects(Graphics2D g2) {
        for (SuperObject obj : objects) {
            if (obj != null) {
                obj.draw(g2, gp);
            }
        }
    }
    
    /**
     * Set a specific tile type at a position
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @param tileType Type of tile to set
     */
    public void setTileInMap(int col, int row, int tileType) {
        if (col >= 0 && col < maxCol && row >= 0 && row < maxRow) {
            mapTileData[col][row] = tileType;
        }
    }
    
    /**
     * Get the tile type at a position
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return The tile type at the specified position
     */
    public int getTile(int col, int row) {
        if (col >= 0 && col < maxCol && row >= 0 && row < maxRow) {
            return mapTileData[col][row];
        }
        return -1; // Invalid position
    }
    
    /**
     * Get the objects array
     * @return The array of objects on this map
     */
    public SuperObject[] getObjects() {
        return objects;
    }    /**
     * Get the name of the map
     * @return The map name
     */
    public String getMapName() {
        return mapName;
    }
      /**
     * Get the MapController instance
     * @return The MapController for this map
     */
    public MapController getMapController() {
        return mapController;
    }
    
    /**
     * Get the maximum number of columns
     * @return Maximum columns in the map
     */
    public int getMaxCol() {
        return maxCol;
    }
    
    /**
     * Get the maximum number of rows
     * @return Maximum rows in the map
     */
    public int getMaxRow() {
        return maxRow;
    }
    
    /**
     * Get the map tile data array
     * @return The 2D array containing tile data
     */
    public int[][] getMapTileData() {
        return mapTileData;
    }
    
    /**
     * Set this map as active or inactive
     * @param active True to set this map as the active map
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    /**
     * Check if this map is currently active
     * @return True if this map is the active map
     */
    public boolean isActive() {
        return isActive;
    }    

    /**
     * /**
     * Get collision bounds at a position if any
     * 
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return int[] containing [leftBound, rightBound, topBound, bottomBound], null
     *         if no collision
     */    /**
     * Get collision bounds at a position if any
     * 
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return int[] containing [leftBound, rightBound, topBound, bottomBound], null
     *         if no collision
     */      public int[] getCollisionBounds(int col, int row) {
        return mapController.getCollisionBounds(this, col, row);
    }/**
     * Check if a position has collision (backward compatibility)
     * 
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return true if the position has collision
     */
    public boolean hasCollision(int col, int row) {
        return mapController.hasCollision(this, col, row);
    }
    
    /**
     * Remove an object at a specific position in this map
     * @param col Column position in the grid
     * @param row Row position in the grid
     * @return true if an object was removed, false otherwise
     */
    public boolean removeObject(int col, int row) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                Tile position = objects[i].getPosition();
                if (position.getCol() == col && position.getRow() == row) {
                    objects[i] = null;
                    System.out.println("Object removed from " + col + "," + row);
                    return true;
                }
            }
        }
        
        System.out.println("No object found at " + col + "," + row);
        return false;
    }    /**
     * Check if a position is valid for object placement
     * @param col Column position in the grid
     * @param row Row position in the grid
     * @param width Width of the object in tiles (default 1)
     * @param height Height of the object in tiles (default 1)
     * @return true if position is valid for placement
     */
    public boolean isValidPlacement(int col, int row, int width, int height) {
        return mapController.isValidPlacement(this, col, row, width, height);
    }
    
    /**
     * Overloaded method for backward compatibility
     * @param col Column position in the grid
     * @param row Row position in the grid
     * @return true if position is valid for placement
     */
    public boolean isValidPlacement(int col, int row) {
        return isValidPlacement(col, row, 1, 1);
    }

    /**
     * Load map data from a text file
     * Format file map.txt adalah matriks angka yang dipisahkan oleh spasi, di mana:
     * 0 = Grass/Rumput (default)
     * 1 = Water/Air
     * 2 = House Area/Area Rumah
     * Dan bisa ditambahkan kode tile lain sesuai kebutuhan game
     * 
     * @param filePath Path to the map file
     * @return true if map was loaded successfully, false otherwise
     */
    public boolean loadMapFromFile(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            int row = 0;
            
            while ((line = br.readLine()) != null && row < maxRow) {
                // Skip comments or empty lines
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }
                
                // Split the line by spaces
                String[] values = line.trim().split("\\s+");
                
                // Read values for each column
                for (int col = 0; col < maxCol && col < values.length; col++) {
                    try {
                        int tileValue = Integer.parseInt(values[col]);
                        mapTileData[col][row] = tileValue;
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid tile value at row " + row + ", col " + col + ": " + values[col]);
                    }
                }
                row++;
            }
            
            br.close();
            System.out.println("Map loaded successfully from " + filePath);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error loading map from " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Mendapatkan karakter asli dari file map pada posisi tertentu
     */
    public char getMapFileChar(int col, int row, String path) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(path));

            String line;
            int currentRow = 0;

            // Skip to the target row
            while ((line = reader.readLine()) != null && currentRow < row) {
                if (!line.trim().startsWith("//") && !line.trim().isEmpty()) {
                    currentRow++;
                }
            }

            // If we found the row
            if (line != null && !line.trim().startsWith("//")) {
                String[] values = line.trim().split("\\s+");
                if (col < values.length) {
                    reader.close();
                    return values[col].charAt(0);
                }
            }

            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading map file character: " + e.getMessage());
        }

        return '0'; // Default to grass (0)
    }
    
    /**
     * Get the entire value (potentially multi-character) from the map file at a specific position
     */
    public String getMapFileValue(int col, int row, String path) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(path));

            String line;
            int currentRow = 0;

            // Skip to the target row
            while ((line = reader.readLine()) != null && currentRow < row) {
                if (!line.trim().startsWith("//") && !line.trim().isEmpty()) {
                    currentRow++;
                }
            }

            // If we found the row
            if (line != null && !line.trim().startsWith("//")) {
                String[] values = line.trim().split("\\s+");
                if (col < values.length) {
                    reader.close();
                    return values[col];
                }
            }

            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading map file value: " + e.getMessage());
        }        return "0"; // Default to grass (0)
    }

    /**
     * Check if there is an object at the specified coordinates
     * @param col Column position
     * @param row Row position
     * @return true if there is an object at the position, false otherwise
     */
    public boolean hasObjectAt(int col, int row) {
        for (SuperObject obj : objects) {
            if (obj != null) {
                int objCol = obj.getWorldX() / gp.getTileSize();
                int objRow = obj.getWorldY() / gp.getTileSize();
                if (objCol == col && objRow == row) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Get the object at the specified coordinates
     * @param col Column position
     * @param row Row position
     * @return SuperObject at the position, or null if none exists
     */
    public SuperObject getObjectAt(int col, int row) {
        for (SuperObject obj : objects) {
            if (obj != null) {
                int objCol = obj.getWorldX() / gp.getTileSize();
                int objRow = obj.getWorldY() / gp.getTileSize();
                if (objCol == col && objRow == row) {
                    return obj;
                }
            }
        }
        return null;
    }

    /**
     * Set up initial objects in the map
     * This should be implemented by subclasses to place their initial objects
     */
    public abstract void setupInitialObjects();
}
