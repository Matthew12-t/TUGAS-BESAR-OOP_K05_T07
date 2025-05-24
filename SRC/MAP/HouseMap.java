package SRC.MAP;
import java.awt.Graphics2D;
import SRC.MAIN.GamePanel;
import SRC.OBJECT.OBJ_House;
import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;
public class HouseMap extends Map {
    // Size of the house map
    public static final int HOUSE_COLS = 24;
    public static final int HOUSE_ROWS = 24;

    // Object deployer for placing objects on the map
    private ObjectDeployer objDeployer;

    /**
     * Constructor for the HouseMap
     * @param gp GamePanel reference
     */
    public HouseMap(GamePanel gp) {
        // House map is smaller than world map, allows up to 10 objects
        super(gp, "House Map", HOUSE_COLS, HOUSE_ROWS, 10);

        // Initialize object deployer
        objDeployer = new ObjectDeployer(gp);
    }

    /**
     * Initialize the house map with various types of tiles and objects from housemap.txt
     */
    @Override
    protected void initializeMap() {
        // Read housemap.txt and set tiles accordingly
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader("RES/MAP_TXT/housemap.txt"));
            loadMapFromFile("RES/MAP_TXT/housemap.txt");
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < HOUSE_ROWS) {
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }
                String[] values = line.trim().split("\\s+");
                for (int col = 0; col < values.length && col < HOUSE_COLS; col++) {
                    char ch = values[col].charAt(0);
                    switch (ch) {
                        case '2':
                            setTileInMap(col, row, SRC.TILES.Tile.TILE_WALL);
                            break;
                        case 'b':
                        case 'c':
                        case 't':
                            setTileInMap(col, row, SRC.TILES.Tile.TILE_FLOOR);
                            break;
                        default:
                            setTileInMap(col, row, SRC.TILES.Tile.TILE_FLOOR);
                            break;
                    }
                }
                row++;
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Error loading housemap.txt: " + e.getMessage());
            // fallback: all floor
            for (int col = 0; col < HOUSE_COLS; col++) {
                for (int row = 0; row < HOUSE_ROWS; row++) {
                    setTileInMap(col, row, SRC.TILES.Tile.TILE_FLOOR);
                }
            }
        }
        // Add teleport tile in the middle of the last row
        int mid1 = (HOUSE_COLS / 2) - 1;
        int mid2 = (HOUSE_COLS / 2);
        setTileInMap(mid1, HOUSE_ROWS - 1, SRC.TILES.Tile.TILE_TELEPORT);
        setTileInMap(mid2, HOUSE_ROWS - 1, SRC.TILES.Tile.TILE_TELEPORT);
    }

    /**
     * Draw the house map
     * @param g2 Graphics2D object for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }
    
    /**
     * Set up initial objects in the house map (bed, table, chair) from housemap.txt
     */
    @Override
    public void setupInitialObjects() {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader("RES/MAP_TXT/housemap.txt"));
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < HOUSE_ROWS) {
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }
                String[] values = line.trim().split("\\s+");
                for (int col = 0; col < values.length && col < HOUSE_COLS; col++) {
                    String val = values[col];
                    if (val.length() == 2 && val.charAt(0) == 'c' && Character.isDigit(val.charAt(1))) {
                        int mode = Character.getNumericValue(val.charAt(1));
                        objDeployer.deployChair(col, row, mode);
                    } else if (val.length() == 2 && val.charAt(0) == 't' && Character.isDigit(val.charAt(1))) {
                        int mode = Character.getNumericValue(val.charAt(1));
                        objDeployer.deployTable(col, row, mode);
                    } else if (val.equals("b")) {
                        objDeployer.deployBed(col, row);
                    }
                    else if (val.equals("s")) {
                        objDeployer.deployStove(col, row);
                    }
                    // ignore default/other
                }
                row++;
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Error deploying objects from housemap.txt: " + e.getMessage());
        }
    }
}

