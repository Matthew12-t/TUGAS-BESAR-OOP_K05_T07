package SRC.OBJECT;

import SRC.MAIN.GamePanel;
import SRC.MAP.Map;

/**
 * Utility class for placing initial objects in maps
 */
public class AssetSetter {

    /**
     * Set up initial objects in the given map
     * @param map The map to place objects on
     */
    public static void setupObjects(Map map) {
        // Let the map handle its own object placement
        map.setupInitialObjects();
    }
}