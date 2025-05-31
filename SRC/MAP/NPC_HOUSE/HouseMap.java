package SRC.MAP.NPC_HOUSE;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import SRC.MAIN.GamePanel;
import SRC.MAP.Map;
import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;
public class HouseMap extends Map {
    
    public static final int HOUSE_COLS = 24;
    public static final int HOUSE_ROWS = 24;

    
    private Set<String> drawnLargeTiles = new HashSet<>();

    
    private ObjectDeployer objDeployer;


    public HouseMap(GamePanel gp) {
        
        super(gp, "House Map", HOUSE_COLS, HOUSE_ROWS, 100);

        
        objDeployer = new ObjectDeployer(gp);
    }

    @Override    
    protected void initializeMap() {
        
        super.initializeMap();
        
        
        boolean loadSuccess = loadMapFromFile("RES/MAP_TXT/housemap.txt");
        
        if (loadSuccess) {
            System.out.println("Setting up tiles from housemap.txt...");
            
            
            for (int row = 0; row < HOUSE_ROWS; row++) {
                for (int col = 0; col < HOUSE_COLS; col++) {
                    
                    char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/housemap.txt");
                    
                    
                    switch (mapChar) {
                        case '2':
                            setTileInMap(col, row, Tile.TILE_WALL1);
                            break;                        
                            case 'a':
                            
                            setTileInMap(col, row, Tile.TILE_WALL2);
                            break;
                        case 'z':
                            
                            setTileInMap(col, row, Tile.TILE_WALL3);
                            break;
                        default:
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            break;
                    }
                }
            }
        } else {
            System.out.println("Failed to load housemap.txt, using default floor tiles");
            
            for (int col = 0; col < HOUSE_COLS; col++) {
                for (int row = 0; row < HOUSE_ROWS; row++) {
                    setTileInMap(col, row, Tile.TILE_FLOOR);
                }
            }
        }
        
        int mid1 = (HOUSE_COLS / 2) - 1;
        int mid2 = (HOUSE_COLS / 2);
        setTileInMap(mid1, HOUSE_ROWS - 1, SRC.TILES.Tile.TILE_TELEPORT);
        setTileInMap(mid2, HOUSE_ROWS - 1, SRC.TILES.Tile.TILE_TELEPORT);
    }    

    @Override
    protected void drawTile(Graphics2D g2, int col, int row, int tileType) {
        
        int worldX = col * gp.getTileSize();
        int worldY = row * gp.getTileSize();
        
        
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();
        
        
        if (worldX + gp.getTileSize() > gp.getCameraX() &&
            worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
            worldY + gp.getTileSize() > gp.getCameraY() &&
            worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {
                  
            if (tileType == Tile.TILE_TELEPORT) {
                Tile.makeTeleportTile(g2, screenX, screenY, gp.getTileSize(), "house"); 
            } else {
                
                Tile.drawTileByType(g2, screenX, screenY, gp.getTileSize(), tileType);
            }
        }
    }


    @Override
    public void draw(Graphics2D g2) {
        
        drawnLargeTiles.clear();
        
        
        for (int row = 0; row < HOUSE_ROWS; row++) {
            for (int col = 0; col < HOUSE_COLS; col++) {                
                int tileType = mapTileData[col][row];
                
                
                if (drawnLargeTiles.contains(tileType + "_" + col + "_" + row)) {
                    continue;
                }
                
                
                if (tileType == Tile.TILE_WALL2 || tileType == Tile.TILE_WALL3) {
                    
                    int[] dimensions = Tile.getTileDimensions(tileType);
                    int width = dimensions[0];
                    int height = dimensions[1];
                    
                    
                    int worldX = col * gp.getTileSize();
                    int worldY = row * gp.getTileSize();
                    int screenX = worldX - gp.getCameraX();
                    int screenY = worldY - gp.getCameraY();
                    
                    
                    if (worldX + gp.getTileSize() * width > gp.getCameraX() &&
                        worldX < gp.getCameraX() + gp.getScreenWidth() &&
                        worldY + gp.getTileSize() * height > gp.getCameraY() &&
                        worldY < gp.getCameraY() + gp.getScreenHeight()) {
                        
                        
                        Tile.drawLargeTileByType(g2, screenX, screenY, gp.getTileSize(), tileType, width, height);
                    }
                    
                    
                    for (int r = 0; r < height; r++) {
                        for (int c = 0; c < width; c++) {
                            if (col + c < HOUSE_COLS && row + r < HOUSE_ROWS) {
                                drawnLargeTiles.add(tileType + "_" + (col + c) + "_" + (row + r));
                            }
                        }
                    }                } else {
                    
                    drawTile(g2, col, row, tileType);
                }
            }
        }
        
        
        drawObjects(g2);
    }
    
    
    @Override
    public void setupInitialObjects() {

        for (int row = 0; row < HOUSE_ROWS; row++) {
            for (int col = 0; col < HOUSE_COLS; col++) {
                
                String mapValue = super.getMapFileValue(col, row, "RES/MAP_TXT/housemap.txt");
                
                if (mapValue.length() == 0) {
                    continue;
                }
                
                
                if (mapValue.length() > 1) {
                    if (mapValue.charAt(0) == 'c' && mapValue.length() == 2 && Character.isDigit(mapValue.charAt(1))) {
                        
                        int mode = Character.getNumericValue(mapValue.charAt(1));
                        System.out.println("Deploying chair mode " + mode + " at " + col + "," + row);
                        objDeployer.deployChair(col, row, mode);
                        continue;
                    } else if (mapValue.charAt(0) == 't' && mapValue.length() == 2 && Character.isDigit(mapValue.charAt(1))) {
                        
                        int mode = Character.getNumericValue(mapValue.charAt(1));
                        System.out.println("Deploying table mode " + mode + " at " + col + "," + row);
                        objDeployer.deployTable(col, row, mode);
                        continue;
                    }
                }
                
                
                char mapChar = mapValue.charAt(0);
                switch (mapChar) {
                    case 'b':
                        objDeployer.deployBed(col, row);
                        break;
                    case 's':
                        objDeployer.deployStove(col, row);
                        break;
                    case 'r':
                        objDeployer.deployRak(col, row);
                        break;
                    case 't':
                        
                        objDeployer.deployTv(col, row);
                        break;
                }
            }
        }
    }
}

