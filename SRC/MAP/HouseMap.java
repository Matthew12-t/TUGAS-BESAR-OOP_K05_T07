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
     * Initialize the house map with various types of tiles
     */
    @Override
    protected void initializeMap() {
        // Set all tiles to lantai (floor)
        for (int col = 0; col < HOUSE_COLS; col++) {
            for (int row = 0; row < HOUSE_ROWS; row++) {
                setTile(col, row, SRC.TILES.Tile.TILE_FLOOR);
            }
        }
        // Set border (tepi) to tembok (wall)
        for (int i = 0; i < HOUSE_COLS; i++) {
            setTile(i, 0, SRC.TILES.Tile.TILE_WALL); // atas
            setTile(i, HOUSE_ROWS-1, SRC.TILES.Tile.TILE_WALL); // bawah
        }
        for (int j = 0; j < HOUSE_ROWS; j++) {
            setTile(0, j, SRC.TILES.Tile.TILE_WALL); // kiri
            setTile(HOUSE_COLS-1, j, SRC.TILES.Tile.TILE_WALL); // kanan
        }        // Buat 4 tile pintu di tengah bawah
        int doorStart = (HOUSE_COLS / 2) - 2; // 10 jika 24 kolom
        int doorEnd = doorStart + 4; // 14 (exclusive)
        for (int i = doorStart; i < doorEnd; i++) {
            setTile(i, HOUSE_ROWS-1, SRC.TILES.Tile.TILE_TELEPORT); // Makes door tiles teleport tiles
        }
        
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
     * Set up initial objects in the house map (bed and table)
     */
    @Override
    public void setupInitialObjects() {
        // Deploy bed di pojok kiri atas (misal 2,2)
        objDeployer.deployBed(21, 19);
        // Deploy table di tengah ruangan (misal 11, 8)
        objDeployer.deployTable(18, 21);
        // Deploy house di tengah ruangan (misal 11, 8)
        objDeployer.deployChair(20, 22);

    }
}

