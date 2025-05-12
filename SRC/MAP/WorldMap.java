package SRC.MAP;

import SRC.MAIN.GamePanel;
import SRC.OBJECT.OBJ_House;

/**
 * WorldMap class represents the main world map in the game
 */
public class WorldMap extends Map {
    
    /**
     * Constructor for the WorldMap
     * @param gp GamePanel reference
     */
    public WorldMap(GamePanel gp) {
        // Use the game panel's world dimensions, allow up to 20 objects on world map
        super(gp, "World Map", gp.getMaxWorldCol(), gp.getMaxWorldRow(), 20);
    }
      /**
     * Initialize the world map with specific terrain features
     */
    @Override
    protected void initializeMap() {
        // Start with default initialization (all grass)
        super.initializeMap();
        
        // Add some water features (tile type 1)
        // For example: a lake in the middle
        int centerX = maxCol / 2;
        int centerY = maxRow / 2;
        int lakeSize = 5;
        
        for (int col = centerX - lakeSize; col <= centerX + lakeSize; col++) {
            for (int row = centerY - lakeSize; row <= centerY + lakeSize; row++) {
                // Create a circular-ish lake
                if (Math.sqrt(Math.pow(col - centerX, 2) + Math.pow(row - centerY, 2)) <= lakeSize) {
                    setTile(col, row, 1); // Water tile                }
                }
            }
        }
    }
    
    /**
     * Setup initial objects in the world map
     */
    @Override
    public void setupInitialObjects() {
        // Place houses around the map (avoid water) using deployHouse for better encapsulation
        deployHouse(5, 5);
        deployHouse(15, 8);
        deployHouse(25, 20);
        deployHouse(10, 25);
    }
    
    /**
     * Custom method for WorldMap: get distance to nearest house
     * @param col Column position to check from
     * @param row Row position to check from
     * @return Distance to nearest house, or -1 if no houses exist
     */
    public int getDistanceToNearestHouse(int col, int row) {
        int nearestDistance = Integer.MAX_VALUE;
        boolean foundHouse = false;
        
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null && objects[i] instanceof OBJ_House) {
                int objCol = objects[i].getPosition().getCol();
                int objRow = objects[i].getPosition().getRow();
                
                int distance = Math.abs(col - objCol) + Math.abs(row - objRow);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    foundHouse = true;
                }
            }
        }
        
        return foundHouse ? nearestDistance : -1;
    }
}
