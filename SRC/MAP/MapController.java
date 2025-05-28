package SRC.MAP;

import SRC.MAIN.GamePanel;
import SRC.OBJECT.SuperObject;
import SRC.OBJECT.OBJ_House;
import SRC.OBJECT.OBJ_Pond;
import SRC.OBJECT.OBJ_ShippingBin;
import SRC.TILES.Tile;

/**
 * MapController class handles non-GUI logic for maps
 * including collision detection, tile validation, and object bounds calculation
 */
public class MapController {
    
    public MapController(GamePanel gp) {
        // Constructor for future extensibility if needed
    }
    
    /**
     * Check if a position has collision
     * @param map The map to check collision on
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return true if the position has collision
     */
    public boolean hasCollision(Map map, int col, int row) {
        return getCollisionBounds(map, col, row) != null;
    }
    
    /**
     * Get collision bounds at a position if any
     * @param map The map to check collision on
     * @param col Column in the map grid
     * @param row Row in the map grid
     * @return int[] containing [leftBound, rightBound, topBound, bottomBound], null if no collision
     */
    public int[] getCollisionBounds(Map map, int col, int row) {
        // Check if the tile itself has collision (water, wall, edge, or edge map tile)
        if (col >= 0 && col < map.getMaxCol() && row >= 0 && row < map.getMaxRow()) {
            int tileType = map.getMapTileData()[col][row];
            if (tileType == 1 || tileType == 7 || tileType == 13 || tileType == 14 || tileType == 15) { // Water, Wall, Edge, or Edge Map tile
                // For tiles with collision, return its own bounds
                return new int[] { col, col + 1, row, row + 1 };
            }
        }
        
        // Also check for out-of-bounds positions (this prevents player from going outside the map)
        if (col < 0 || col >= map.getMaxCol() || row < 0 || row >= map.getMaxRow()) {
            // Return collision bounds for out-of-bounds positions
            return new int[] { col, col + 1, row, row + 1 };
        }

        // Check if there's an object with collision at this position
        for (SuperObject obj : map.getObjects()) {
            if (obj != null) {
                Tile objPosition = obj.getPosition();
                int objCol = objPosition.getCol();
                int objRow = objPosition.getRow();

                // Get object dimensions and bounds
                int width = 1; // Default width
                int height = 1; // Default height

                // Determine dimensions based on object type
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
                    width = ((SRC.OBJECT.OBJ_Chair) obj).getChairWidth();
                    height = ((SRC.OBJECT.OBJ_Chair) obj).getChairHeight();
                } else if (obj instanceof SRC.OBJECT.OBJ_Stove) {
                    width = ((SRC.OBJECT.OBJ_Stove) obj).getStoveWidth();
                    height = ((SRC.OBJECT.OBJ_Stove) obj).getStoveHeight();
                } else if (obj instanceof SRC.OBJECT.OBJ_Rak) {
                    width = ((SRC.OBJECT.OBJ_Rak) obj).getRakWidth();
                    height = ((SRC.OBJECT.OBJ_Rak) obj).getRakHeight();
                } else if (obj instanceof SRC.OBJECT.OBJ_Tv) {
                    width = ((SRC.OBJECT.OBJ_Tv) obj).getTvWidth();
                    height = ((SRC.OBJECT.OBJ_Tv) obj).getTvHeight();
                }else if (obj instanceof SRC.OBJECT.OBJ_Chimney) {
                    width = ((SRC.OBJECT.OBJ_Chimney) obj).getChimneyWidth();
                    height = ((SRC.OBJECT.OBJ_Chimney) obj).getChimneyHeight();
                } else if (obj instanceof SRC.OBJECT.OBJ_StoreDecoration1) {
                    width = ((SRC.OBJECT.OBJ_StoreDecoration1) obj).getDecorationWidth();
                    height = ((SRC.OBJECT.OBJ_StoreDecoration1) obj).getDecorationHeight();
                } else if (obj instanceof SRC.OBJECT.OBJ_StoreDecoration2) {
                    width = ((SRC.OBJECT.OBJ_StoreDecoration2) obj).getDecorationWidth();
                    height = ((SRC.OBJECT.OBJ_StoreDecoration2) obj).getDecorationHeight();
                } else if (obj instanceof SRC.OBJECT.OBJ_StoreDecoration3) {
                    width = ((SRC.OBJECT.OBJ_StoreDecoration3) obj).getDecorationWidth();
                    height = ((SRC.OBJECT.OBJ_StoreDecoration3) obj).getDecorationHeight();
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
     * Check if a position is valid for object placement
     * @param map The map to check placement on
     * @param col Column position in the grid
     * @param row Row position in the grid
     * @param width Width of the object in tiles (default 1)
     * @param height Height of the object in tiles (default 1)
     * @return true if position is valid for placement
     */
    public boolean isValidPlacement(Map map, int col, int row, int width, int height) {
        // Check if all tiles in the placement area are free
        for (int c = col; c < col + width; c++) {
            for (int r = row; r < row + height; r++) {
                if (hasCollision(map, c, r)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Check if player is on water tile or pond object
     * @param map The current map
     * @param playerCol Player's column position
     * @param playerRow Player's row position
     * @return true if player is on fishable water
     */
    public boolean isOnFishableWater(Map map, int playerCol, int playerRow) {
        // Check if standing on water tiles
        if (playerCol >= 0 && playerCol < map.getMaxCol() && playerRow >= 0 && playerRow < map.getMaxRow()) {
            int tileType = map.getMapTileData()[playerCol][playerRow];
            if (tileType == Tile.TILE_WATER || tileType == Tile.TILE_WATER2 || tileType == Tile.TILE_WATER3) {
                return true;
            }
        }
        
        // Check if standing on a pond object
        for (SuperObject obj : map.getObjects()) {
            if (obj instanceof OBJ_Pond) {
                Tile objPosition = obj.getPosition();
                int objCol = objPosition.getCol();
                int objRow = objPosition.getRow();
                int width = ((OBJ_Pond) obj).getPondWidth();
                int height = ((OBJ_Pond) obj).getPondHeight();
                
                // Check if player is within pond bounds
                if (playerCol >= objCol && playerCol < objCol + width &&
                    playerRow >= objRow && playerRow < objRow + height) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Check if player is adjacent to water tile or pond object
     * @param map The current map
     * @param playerCol Player's column position
     * @param playerRow Player's row position
     * @return true if player is next to fishable water
     */
    public boolean isAdjacentToFishableWater(Map map, int playerCol, int playerRow) {
        // Check adjacent tiles (up, down, left, right)
        int[] dCol = {0, 0, -1, 1};
        int[] dRow = {-1, 1, 0, 0};
        
        for (int i = 0; i < 4; i++) {
            int adjacentCol = playerCol + dCol[i];
            int adjacentRow = playerRow + dRow[i];
            
            if (isOnFishableWater(map, adjacentCol, adjacentRow)) {
                return true;
            }
        }
        
        return false;
    }
}
