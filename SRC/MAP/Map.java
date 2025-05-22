package SRC.MAP;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import SRC.MAIN.GamePanel;
import SRC.OBJECT.OBJ_House;
import SRC.OBJECT.OBJ_Pond;
import SRC.OBJECT.OBJ_ShippingBin;
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
                mapTileData[col][row] = 0; // 0 = grass tile (default)
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
            
            // Draw the appropriate tile type using the Tile class utility method
            Tile.drawTileByType(g2, screenX, screenY, gp.getTileSize(), tileType);
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
    }
      /**
     * Get the name of the map
     * @return The map name
     */
    public String getMapName() {
        return mapName;
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
     */

    /**
     * Get collision bounds at a position if any
     * 
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return int[] containing [leftBound, rightBound, topBound, bottomBound], null
     *         if no collision
     */
    public int[] getCollisionBounds(int col, int row) {
        // Check if the tile itself has collision (water tile or wall tile)
        if (col >= 0 && col < maxCol && row >= 0 && row < maxRow) {
            int tileType = mapTileData[col][row];
            if (tileType == 1 || tileType == 7) { // Water or Wall tile
                // For water or wall tiles, return its own bounds
                return new int[] { col, col + 1, row, row + 1 };
            }
        }

        // Check if there's an object with collision at this position
        for (SuperObject obj : objects) {
            if (obj != null) {
                Tile objPosition = obj.getPosition();
                int objCol = objPosition.getCol();
                int objRow = objPosition.getRow();

                // Get object dimensions and bounds
                int width = 1; // Default width
                int height = 1; // Default height

                // Tentukan dimensi berdasarkan tipe objek
                if (obj instanceof OBJ_House) {
                    width = ((OBJ_House) obj).getHouseWidth();
                    height = ((OBJ_House) obj).getHouseHeight();
                } else if (obj instanceof OBJ_Pond) {
                    width = ((OBJ_Pond) obj).getPondWidth();
                    height = ((OBJ_Pond) obj).getPondHeight();
                } else if (obj instanceof OBJ_ShippingBin) {
                    width = ((OBJ_ShippingBin) obj).getBinWidth();
                    height = ((OBJ_ShippingBin) obj).getBinHeight();
                } else if (obj instanceof SRC.OBJECT.OBJ_Table) {
                    width = ((SRC.OBJECT.OBJ_Table) obj).gettableWidth();
                    height = ((SRC.OBJECT.OBJ_Table) obj).gettableHeight();
                } else if (obj instanceof SRC.OBJECT.OBJ_Bed) {
                    width = ((SRC.OBJECT.OBJ_Bed) obj).getBedWidth();
                    height = ((SRC.OBJECT.OBJ_Bed) obj).getBedHeight();
                } else if (obj instanceof SRC.OBJECT.OBJ_Chair) {
                    width = ((SRC.OBJECT.OBJ_Chair) obj).getchairWidth();
                    height = ((SRC.OBJECT.OBJ_Chair) obj).getchairHeight();
                }

                // Calculate bounds
                int leftBound = objCol;
                int rightBound = objCol + width;
                int topBound = objRow;
                int bottomBound = objRow + height;

                // Check if position is within bounds
                if (col >= leftBound && col < rightBound &&
                        row >= topBound && row < bottomBound) {
                    return new int[] { leftBound, rightBound, topBound, bottomBound };
                }
            }
        }

        // No collision found
        return null;
    }

    /**
     * Check if a position has collision (backward compatibility)
     * 
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return true if the position has collision
     */
    public boolean hasCollision(int col, int row) {
        return getCollisionBounds(col, row) != null;
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
    }
      /**
     * Check if a position is valid for object placement
     * @param col Column position in the grid
     * @param row Row position in the grid
     * @param width Width of the object in tiles (default 1)
     * @param height Height of the object in tiles (default 1)
     * @return true if position is valid for placement
     */
    public boolean isValidPlacement(int col, int row, int width, int height) {
        // Periksa apakah seluruh area yang dibutuhkan objek berada dalam batas map
        if (col < 0 || col + width > maxCol || row < 0 || row + height > maxRow) {
            return false;
        }
        
        // Periksa setiap tile apakah sudah ada collision
        for (int c = col; c < col + width; c++) {
            for (int r = row; r < row + height; r++) {
                if (hasCollision(c, r)) {
                    return false;
                }
            }
        }
        
        return true;
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
     * Set up initial objects in the map
     * This should be implemented by subclasses to place their initial objects
     */
    public abstract void setupInitialObjects();
}
