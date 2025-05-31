package SRC.MAIN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import SRC.ENTITY.Player;
import SRC.ENTITY.NPCEntity;
import SRC.ITEMS.Item;
import SRC.MAP.FarmMap;
import SRC.MAP.ForestRiverMap;
import SRC.MAP.Map;
import SRC.MAP.MountainLake;
import SRC.MAP.OceanMap;
import SRC.MAP.StoreMap;
import SRC.MAP.NPC_HOUSE.HouseMap;
import SRC.MAP.NPC_HOUSE.NPCHouseMap;
import SRC.MAP.NPC_HOUSE.AbigailHouseMap;
import SRC.MAP.NPC_HOUSE.CarolineHouseMap;
import SRC.MAP.NPC_HOUSE.DascoHouseMap;
import SRC.MAP.NPC_HOUSE.EmilyHouseMap;
import SRC.MAP.NPC_HOUSE.MayorTadiHouseMap;
import SRC.MAP.NPC_HOUSE.PerryHouseMap;
import SRC.OBJECT.SuperObject;
import SRC.TIME.Time;
import SRC.TIME.GameTime;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.UI.NPCUi;
import SRC.UI.CheatUI; 
import SRC.CHEAT.Cheat;
import SRC.SHIPPINGBIN.ShippingBin;
import SRC.SHIPPINGBIN.ShippingBinUI;
import SRC.UI.StoreUI;
import SRC.UI.CookingUI;
import SRC.UI.ClockUI;
import SRC.UI.DayUI;
import SRC.UI.EnergyUI;
import SRC.UI.HoldingItemUI;
import SRC.TILES.TileManager;
import SRC.MAIN.MusicManager;

public class GamePanel extends JPanel implements Runnable {
    private final int originalTileSize = 16; 
    private final int scale = 3;
    private final int tileSize = originalTileSize * scale; 
    private final int maxScreenCol = 16; 
    private final int maxScreenRow = 14; 
    private final int screenWidth = tileSize * maxScreenCol; 
    private final int screenHeight = tileSize * maxScreenRow; 
    private final int FPS = 60;
        
    public static final int MAIN_MENU_STATE = -1;
    public static final int NEW_GAME_STATE = -2;
    public static final int LOAD_GAME_STATE = -3;
    public static final int OPTIONS_STATE = -4;
    public static final int PLAY_STATE = 0;
    public static final int MAP_MENU_STATE = 1;
    public static final int INVENTORY_STATE = 2;
    public static final int SLEEP_STATE = 3;
    public static final int SHIPPING_STATE = 4;
    public static final int STORE_STATE = 5;
    public static final int COOKING_STATE = 6;
    public static final int TV_STATE = 7;    public static final int CHEAT_STATE = 8;
    public static final int ENDGAME_STATE = 9;
    public static final int PLAYER_STATISTICS_STATE = 10;
    private int gameState = MAIN_MENU_STATE; 

    
    private SRC.MAIN.MENU.MainMenu mainMenu;
    private SRC.UI.NewGameUI newGameUI;
    private SRC.UI.LoadGameUI loadGameUI;
    private SRC.UI.OptionsUI optionsUI;

    
    private BufferedImage inventoryImage;
    
    
    private BufferedImage[] mapMenuImages;
    private int currentMapMenuIndex = 0;
    private final int TOTAL_WORLD_MAPS = 9; 
    
    
    
    private final int maxWorldCol = 32;
    private final int maxWorldRow = 32;    private boolean isInitializedFarmMap = false; 
    private boolean isInitializedHouseMap = false; 
    private boolean isInitializedWorldMap = false; 
    private boolean isInitializedMountainLake = false; 
    private boolean isInitializedOceanMap = false; 
    private boolean isInitializedStoreMap = false; 
    
    
    private boolean isInitializedAbigailHouse = false;
    private boolean isInitializedCarolineHouse = false;
    private boolean isInitializedDascoHouse = false;
    private boolean isInitializedEmilyHouse = false;
    private boolean isInitializedMayorTadiHouse = false;
    private boolean isInitializedPerryHouse = false;    
    private KeyHandler keyHandler = new KeyHandler(this);
    private MouseHandler mouseHandler = new MouseHandler(this);
    private Thread gameThread;

    
    private TileManager tileManager;

    
    private Player player = new Player(this, keyHandler, mouseHandler);    
     
    private Map forestrivermap;   
    private Map mountainLake;     
    private Map oceanMap;         
    private Map farmMap;          
    private Map currentMap;       
    private Map houseMap;         
    private Map storeMap;         
    
    
    private Map abigailHouseMap;   
    private Map carolineHouseMap;  
    private Map dascoHouseMap;     
    private Map emilyHouseMap;     
    private Map mayorTadiHouseMap; 
    private Map perryHouseMap;     
    private int cameraX = 0; 
    private int cameraY = 0;    
    private ClockUI clockUI;
    private DayUI dayUI;
      
    private EnergyUI energyUI;
    
    
    private HoldingItemUI holdingItemUI;
    
    
    private float currentBrightness = 1.0f; 
    
    
    private static final int INVENTORY_ROWS = 4;
    private static final int INVENTORY_COLS = 4;
    
    
    private ShippingBin ShippingBin;
    private ShippingBinUI shippingBinUI;
      
    private StoreUI storeUI;
    
    
    private CookingUI cookingUI;    
    private SRC.UI.CheatUI cheatUI;
    private SRC.CHEAT.Cheat cheat;
    
    
    private MusicManager musicManager;
    private SRC.ENDGAME.EndGame endGame;
    private SRC.UI.EndGameUI endGameUI;    
    private SRC.UI.PlayerStatisticsUI playerStatisticsUI;
    
    
    private boolean milestonesShown = false;

    
    public int getTileSize() {
        return tileSize;
    }
    
    public int getOriginalTileSize() {
        return originalTileSize;
    }
    
    public int getScale() {
        return scale;
    }
    
    public int getMaxScreenCol() {
        return maxScreenCol;
    }
    
    public int getMaxScreenRow() {
        return maxScreenRow;
    }
    
    public int getScreenWidth() {
        return screenWidth;
    }
      public int getScreenHeight() {
        return screenHeight;
    }
    
    public int getFPS() {
        return FPS;
    }
    
    public ShippingBin getShippingBin() {
        return ShippingBin;
    }
      public ShippingBinUI getShippingBinUI() {
        return shippingBinUI;
    }
      public StoreUI getStoreUI() {
        return storeUI;
    }
    
    public CookingUI getCookingUI() {
        return cookingUI;
    }
    
    public SRC.UI.TvUI getTvUI() {
        return player.getPlayerAction().getTvUI();
    }
    
    public void setGameState(int gameState) {
        this.gameState = gameState;
    }
    
    public int getGameState() {
        return gameState;
    }
    
    public int getMaxWorldCol() {
        return maxWorldCol;
    }
    
    public int getMaxWorldRow() {
        return maxWorldRow;
    }    public int getMaxWorldWidth() {
        
        if (currentMap.getMapName().equals("Farm Map")) {
            return tileSize * FarmMap.FARM_COLS;
        } else if (currentMap.getMapName().equals("House Map")) {
            return tileSize * HouseMap.HOUSE_COLS;
        } else if (currentMap.getMapName().equals("Store Map")) {
            return tileSize * StoreMap.STORE_COLS;
        } else {
            return tileSize * maxWorldCol;
        }
    }
    
    public int getMaxWorldHeight() {
        
        if (currentMap.getMapName().equals("Farm Map")) {
            return tileSize * FarmMap.FARM_ROWS;
        } else if (currentMap.getMapName().equals("House Map")) {
            return tileSize * HouseMap.HOUSE_ROWS;
        } else if (currentMap.getMapName().equals("Store Map")) {
            return tileSize * StoreMap.STORE_ROWS;
        } else {
            return tileSize * maxWorldRow;
        }
    }
    
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }
    
    public MouseHandler getMouseHandler() {
        return mouseHandler;
    }
      public Player getPlayer() {
        return player;
    }
    
    public TileManager getTileManager() {
        return tileManager;
    }
    
    public int getCameraX() {
        return cameraX;
    }
    
    public void setCameraX(int cameraX) {
        this.cameraX = cameraX;
    }
      public int getCameraY() {
        return cameraY;
    }
      public void setCameraY(int cameraY) {
        this.cameraY = cameraY;
    }

    public Map getCurrentMap() {
        return currentMap;
    }
      /**
     * Switch to world map
     */    
    public void switchToWorldMap() {
        farmMap.setActive(false);
        houseMap.setActive(false);
        forestrivermap.setActive(true);
        this.currentMap = forestrivermap;
    }
    
    /**
     * Switch to farm map
     */    
    public void switchToFarmMap() {
        forestrivermap.setActive(false);
        houseMap.setActive(false);
        farmMap.setActive(true);
        this.currentMap = farmMap;
    }
      /**
     * Switch to house map
     */
    public void switchToHouseMap() {
        forestrivermap.setActive(false);
        farmMap.setActive(false);
        storeMap.setActive(false);
        houseMap.setActive(true);
        this.currentMap = houseMap;
    }
    
    /**
     * Switch to store map
     */    public void switchToStoreMap() {
        forestrivermap.setActive(false);
        farmMap.setActive(false);
        houseMap.setActive(false);
        storeMap.setActive(true);
        this.currentMap = storeMap;
    }
    
    /**
     * Switch to Abigail's house map
     */
    public void switchToAbigailHouse() {
        deactivateAllMaps();
        abigailHouseMap.setActive(true);
        this.currentMap = abigailHouseMap;
    }
    
    /**
     * Switch to Caroline's house map
     */
    public void switchToCarolineHouse() {
        deactivateAllMaps();
        carolineHouseMap.setActive(true);
        this.currentMap = carolineHouseMap;
    }
    
    /**
     * Switch to Dasco's house map
     */
    public void switchToDascoHouse() {
        deactivateAllMaps();
        dascoHouseMap.setActive(true);
        this.currentMap = dascoHouseMap;
    }
    
    /**
     * Switch to Emily's house map
     */
    public void switchToEmilyHouse() {
        deactivateAllMaps();
        emilyHouseMap.setActive(true);
        this.currentMap = emilyHouseMap;
    }
    
    /**
     * Switch to Mayor Tadi's house map
     */
    public void switchToMayorTadiHouse() {
        deactivateAllMaps();
        mayorTadiHouseMap.setActive(true);
        this.currentMap = mayorTadiHouseMap;
    }
    
    /**
     * Switch to Perry's house map
     */
    public void switchToPerryHouse() {
        deactivateAllMaps();
        perryHouseMap.setActive(true);
        this.currentMap = perryHouseMap;
    }
    
    /**
     * Helper method to deactivate all maps
     */
    private void deactivateAllMaps() {
        forestrivermap.setActive(false);
        farmMap.setActive(false);
        houseMap.setActive(false);
        storeMap.setActive(false);
        abigailHouseMap.setActive(false);
        carolineHouseMap.setActive(false);
        dascoHouseMap.setActive(false);
        emilyHouseMap.setActive(false);
        mayorTadiHouseMap.setActive(false);
        perryHouseMap.setActive(false);
        mountainLake.setActive(false);
        oceanMap.setActive(false);
    }
    public void setIsInitializedFarmMap(boolean isInitialized) {
        this.isInitializedFarmMap = isInitialized;
    }
    public boolean isInitializedFarmMap() {
        return isInitializedFarmMap;
    }
    public void setIsInitializedHouseMap(boolean isInitialized) {
        this.isInitializedHouseMap = isInitialized;
    }
    public boolean isInitializedHouseMap() {
        return isInitializedHouseMap;
    }    public void setIsInitializedWorldMap(boolean isInitialized) {
        this.isInitializedWorldMap = isInitialized;
    }
    public boolean isInitializedWorldMap() {
        return isInitializedWorldMap;
    }
      public void setIsInitializedStoreMap(boolean isInitialized) {
        this.isInitializedStoreMap = isInitialized;
    }
    public boolean isInitializedStoreMap() {
        return isInitializedStoreMap;
    }
    
    
    public void setIsInitializedAbigailHouse(boolean isInitialized) {
        this.isInitializedAbigailHouse = isInitialized;
    }
    public boolean isInitializedAbigailHouse() {
        return isInitializedAbigailHouse;
    }
    
    public void setIsInitializedCarolineHouse(boolean isInitialized) {
        this.isInitializedCarolineHouse = isInitialized;
    }
    public boolean isInitializedCarolineHouse() {
        return isInitializedCarolineHouse;
    }
    
    public void setIsInitializedDascoHouse(boolean isInitialized) {
        this.isInitializedDascoHouse = isInitialized;
    }
    public boolean isInitializedDascoHouse() {
        return isInitializedDascoHouse;
    }
    
    public void setIsInitializedEmilyHouse(boolean isInitialized) {
        this.isInitializedEmilyHouse = isInitialized;
    }
    public boolean isInitializedEmilyHouse() {
        return isInitializedEmilyHouse;
    }
    
    public void setIsInitializedMayorTadiHouse(boolean isInitialized) {
        this.isInitializedMayorTadiHouse = isInitialized;
    }
    public boolean isInitializedMayorTadiHouse() {
        return isInitializedMayorTadiHouse;
    }
    
    public void setIsInitializedPerryHouse(boolean isInitialized) {
        this.isInitializedPerryHouse = isInitialized;
    }
    public boolean isInitializedPerryHouse() {
        return isInitializedPerryHouse;
    }
    
    
    public Map getAbigailHouseMap() {
        return abigailHouseMap;
    }
    
    public Map getCarolineHouseMap() {
        return carolineHouseMap;
    }
    
    public Map getDascoHouseMap() {
        return dascoHouseMap;
    }
    
    public Map getEmilyHouseMap() {
        return emilyHouseMap;
    }
    
    public Map getMayorTadiHouseMap() {
        return mayorTadiHouseMap;
    }
    
    public Map getPerryHouseMap() {
        return perryHouseMap;
    }
      public Map getStoreMap() {
        return storeMap;
    }
    

    public Map getFarmMap() {
        return farmMap;
    }
    

    public String getRandomFarmMapPath() {
        String[] farmMaps = {
            "RES/MAP_TXT/farmmap.txt",
            "RES/MAP_TXT/farmmap1.txt", 
            "RES/MAP_TXT/farmmap2.txt",
            "RES/MAP_TXT/farmmap3.txt",
            "RES/MAP_TXT/farmmap4.txt"
        };
        
        java.util.Random random = new java.util.Random();
        int randomIndex = random.nextInt(farmMaps.length);
        
        System.out.println("Selected farm map: " + farmMaps[randomIndex]);
        return farmMaps[randomIndex];
    }

    public void initializeRandomizedFarmMap(String mapFilePath) {
        this.farmMap = new FarmMap(this, mapFilePath);
        
        this.isInitializedFarmMap = false;
        if (currentMap == farmMap) {
            this.farmMap.setupInitialObjects();
            this.isInitializedFarmMap = true;
        }
    }
    

    public SuperObject[] getCurrentObjects() {
        return currentMap.getObjects();
    }
      public GamePanel() {
        
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); 
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true);

        
        mapMenuImages = new BufferedImage[TOTAL_WORLD_MAPS];
        loadMapMenuImages();        
        this.clockUI = new ClockUI(screenWidth, screenHeight, this);

        
        this.dayUI = new DayUI(screenWidth, screenHeight, clockUI);
        this.energyUI = new EnergyUI(screenWidth, screenHeight);
        this.holdingItemUI = new HoldingItemUI(screenWidth, screenHeight);
        
        
        this.cheatUI = new CheatUI(this);
        this.cheat = new Cheat(player, player.getPlayerAction().getInventory(), clockUI, this);
        this.cheatUI.setCheat(cheat);

        
        loadInventoryImage();
        this.ShippingBin = new ShippingBin();
        this.shippingBinUI = new ShippingBinUI(this, ShippingBin, player.getPlayerAction().getInventory());        
        this.storeUI = new StoreUI(this, player, player.getPlayerAction().getInventory());
        this.cookingUI = new CookingUI(this);
        this.tileManager = new TileManager(this);
        
        
        this.forestrivermap = new ForestRiverMap(this);
        this.mountainLake = new MountainLake(this);
        this.oceanMap = new OceanMap(this);
        this.farmMap = new FarmMap(this);
        this.houseMap = new HouseMap(this);
        this.storeMap = new StoreMap(this);
        
        
        this.abigailHouseMap = new AbigailHouseMap(this);
        this.carolineHouseMap = new CarolineHouseMap(this);
        this.dascoHouseMap = new DascoHouseMap(this);
        this.emilyHouseMap = new EmilyHouseMap(this);
        this.mayorTadiHouseMap = new MayorTadiHouseMap(this);
        this.perryHouseMap = new PerryHouseMap(this);
        
        this.currentMap = houseMap;
        setIsInitializedHouseMap(true);
        if(currentMap == farmMap){
            this.farmMap.setupInitialObjects();
        }
        else if (currentMap == forestrivermap){
            this.forestrivermap.setupInitialObjects();        
        }
        else if (currentMap == mountainLake){
            this.mountainLake.setupInitialObjects();
        }
        else if (currentMap == oceanMap){
            this.oceanMap.setupInitialObjects();
        }
        else if (currentMap == houseMap){
            this.houseMap.setupInitialObjects();
        }
        else if (currentMap == storeMap){
            this.storeMap.setupInitialObjects();
        }
        else{
            this.forestrivermap.setupInitialObjects();
        }        
        this.farmMap.setActive(false);
        this.forestrivermap.setActive(false);
        this.mountainLake.setActive(false);
        this.oceanMap.setActive(false);
        this.storeMap.setActive(false);
        
        
        this.abigailHouseMap.setActive(false);
        this.carolineHouseMap.setActive(false);
        this.dascoHouseMap.setActive(false);
        this.emilyHouseMap.setActive(false);
        this.mayorTadiHouseMap.setActive(false);
        this.perryHouseMap.setActive(false);
        
        this.houseMap.setActive(true);
        this.npcUi = new NPCUi(this);        
        this.endGame = new SRC.ENDGAME.EndGame(player, this);
        this.endGameUI = new SRC.UI.EndGameUI(this);
        
        
        this.playerStatisticsUI = new SRC.UI.PlayerStatisticsUI(this);
          
        this.mainMenu = new SRC.MAIN.MENU.MainMenu(this);
        this.newGameUI = new SRC.UI.NewGameUI(this);
        this.loadGameUI = new SRC.UI.LoadGameUI(this);
        this.optionsUI = new SRC.UI.OptionsUI(this);
          
        this.musicManager = new MusicManager();
        this.musicManager.playMusic("RES/SOUND/music.wav");
    }
    
    /**
     * Load map menu images from resources
     */
    private void loadMapMenuImages() {
        try {
            for (int i = 0; i < TOTAL_WORLD_MAPS; i++) {
                String imagePath = "RES/MAPMENU/worldmap" + (i+1) + ".png";
                mapMenuImages[i] = ImageIO.read(new File(imagePath));
                System.out.println("Loaded map menu image: " + imagePath);
            }        } catch (Exception e) {
            System.err.println("Error loading map menu images: " + e.getMessage());
            e.printStackTrace();
        }
    }
      /**
     * Load the inventory background image
     */
    private void loadInventoryImage() {
        try {
            inventoryImage = ImageIO.read(new File("RES/INVENTORY/inventory.png"));
        } catch (IOException e) {
            System.err.println("Error loading inventory image: " + e.getMessage());
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();    }
      @Override
    public void run(){
        double drawInterval = 1000000000.0 / FPS; 
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            
            clockUI.updateIfNeeded();
            
            if (delta >= 1) {
                update();
                
                
                if (gameState == PLAY_STATE) {
                    player.getPlayerAction().checkAutomaticSleep();
                }
                
                repaint();
                delta--;
                drawCount++;
            }

            
            if(timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }        
        }
    }

    public void centerCameraOnMap(int mapCols, int mapRows) {
        
        int mapWidth = mapCols * tileSize;
        int mapHeight = mapRows * tileSize;
        
        
        if (mapWidth < screenWidth || mapHeight < screenHeight) {
            
            
            int centerX = Math.max(0, (screenWidth - mapWidth) / 2);
            
            
            int centerY = Math.max(0, (screenHeight - mapHeight) / 2);
            
            
            
            cameraX = -centerX;
            cameraY = -centerY;
            
            
        } else {
            
            cameraX = player.getWorldX() - screenWidth / 2 + player.getPlayerVisualWidth() / 2;
            cameraY = player.getWorldY() - screenHeight / 2 + player.getPlayerVisualHeight() / 2;
            
            
            if (cameraX < 0) cameraX = 0;
            if (cameraY < 0) cameraY = 0;
            if (cameraX > mapWidth - screenWidth) cameraX = mapWidth - screenWidth;
            if (cameraY > mapHeight - screenHeight) cameraY = mapHeight - screenHeight;
        }
    }

        
    public void teleportToMap(String targetMapName) {
        if (currentMap.getMapName().equals(targetMapName)) {
            
            return;
        }
        
        
        mouseHandler.setHasTarget(false);
        
        if (targetMapName.equals("World Map")) {
            switchMap(forestrivermap);
            if (!isInitializedWorldMap) {
                forestrivermap.setupInitialObjects();
                isInitializedWorldMap = true;
            }
            
            player.setWorldX(tileSize * 1); 
            
        } else if (targetMapName.equals("Farm Map")) {
            
            if (currentMap.getMapName().equals("House Map")) {
                
                switchMap(farmMap);
                if (!isInitializedFarmMap) {
                    farmMap.setupInitialObjects();
                    isInitializedFarmMap = true;
                }
                FarmMap farmMapRef = (FarmMap) farmMap;
                player.setWorldX(tileSize * (farmMapRef.getDepanRumahCol() +1));
                player.setWorldY(tileSize * (farmMapRef.getDepanRumahRow()+ 1));            } else {
                switchMap(farmMap);
                if (!isInitializedFarmMap) {
                    farmMap.setupInitialObjects();
                    isInitializedFarmMap = true;
                }
                
                FarmMap farmMapRef = (FarmMap) farmMap;
                int[] spawnPos = farmMapRef.getWorldMapSpawnPosition();
                player.setWorldX(tileSize * spawnPos[0]);
                player.setWorldY(tileSize * spawnPos[1]);
            }
        } else if (targetMapName.equals("Forest River Map")) {
            switchMap(forestrivermap);
            if (!isInitializedWorldMap) {
                forestrivermap.setupInitialObjects();
                isInitializedWorldMap = true;
            }
            
            
            
            player.setWorldX(tileSize * 1); 
            player.setWorldY(tileSize * 2); 
            
            
            centerCameraOnMap(ForestRiverMap.FOREST_COLS, ForestRiverMap.FOREST_ROWS);
        } else if (targetMapName.equals("Mountain Lake")) {
            switchMap(mountainLake);
            if (!isInitializedMountainLake) {
                mountainLake.setupInitialObjects();
                isInitializedMountainLake = true;
            }
            
            
            player.setWorldX(tileSize * 1); 
            player.setWorldY(tileSize * 2); 
            
            
            centerCameraOnMap(MountainLake.MOUNTAIN_COLS, MountainLake.MOUNTAIN_ROWS);
        } else if (targetMapName.equals("House Map")) {
            switchMap(houseMap);
            if (!isInitializedHouseMap) {
                houseMap.setupInitialObjects();
                isInitializedHouseMap = true;
            }
            
            int doorStart = (HouseMap.HOUSE_COLS / 2) - 1; 
            player.setWorldX(tileSize * doorStart);
            player.setWorldY(tileSize * (HouseMap.HOUSE_ROWS - 2)); 
              
            centerCameraOnMap(HouseMap.HOUSE_COLS, HouseMap.HOUSE_ROWS);        } 
            else if (targetMapName.equals("Store Map")) {
            switchMap(storeMap);
            if (!isInitializedStoreMap) {
                storeMap.setupInitialObjects();
                isInitializedStoreMap = true;
            }
            
            player.setWorldX(tileSize * ((StoreMap.STORE_COLS / 2) - 2)); 
            player.setWorldY(tileSize * (StoreMap.STORE_ROWS - 4)); 
            
            
            centerCameraOnMap(StoreMap.STORE_COLS, StoreMap.STORE_ROWS);
            ((StoreMap)storeMap).ensureNPCsVisible();
        } else if (targetMapName.equals("Abigail's House")) {
            switchToAbigailHouse();
            if (!isInitializedAbigailHouse) {
                abigailHouseMap.setupInitialObjects();
                isInitializedAbigailHouse = true;
            }
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 4));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((AbigailHouseMap)abigailHouseMap).ensureNPCsVisible();
        } else if (targetMapName.equals("Caroline's House")) {
            switchToCarolineHouse();
            if (!isInitializedCarolineHouse) {
                carolineHouseMap.setupInitialObjects();
                isInitializedCarolineHouse = true;
            }
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 4));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);  
            ((CarolineHouseMap)carolineHouseMap).ensureNPCsVisible();
        } else if (targetMapName.equals("Dasco's House")) {
            switchToDascoHouse();
            if (!isInitializedDascoHouse) {
                dascoHouseMap.setupInitialObjects();
                isInitializedDascoHouse = true;
            }
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 4));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((DascoHouseMap)dascoHouseMap).ensureNPCsVisible();
        } else if (targetMapName.equals("Emily's House")) {
            switchToEmilyHouse();
            if (!isInitializedEmilyHouse) {
                emilyHouseMap.setupInitialObjects();
                isInitializedEmilyHouse = true;
            }
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 4));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((EmilyHouseMap)emilyHouseMap).ensureNPCsVisible();
        } else if (targetMapName.equals("Mayor Tadi's House")) {
            switchToMayorTadiHouse();
            if (!isInitializedMayorTadiHouse) {
                mayorTadiHouseMap.setupInitialObjects();
                isInitializedMayorTadiHouse = true;
            }
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 4));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            
        } else if (targetMapName.equals("Perry's House")) {
            switchToPerryHouse();
            if (!isInitializedPerryHouse) {
                perryHouseMap.setupInitialObjects();
                isInitializedPerryHouse = true;
            }
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 2));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 4));
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((PerryHouseMap)perryHouseMap).ensureNPCsVisible();        }
    }
      public void update() {
        
        if (gameState == MAIN_MENU_STATE) {
            if (mainMenu != null) {
                mainMenu.update();
            }
            return; 
        } else if (gameState == NEW_GAME_STATE || gameState == LOAD_GAME_STATE || gameState == OPTIONS_STATE) {
            
            return;
        }
        
        else if (gameState == PLAY_STATE || gameState == INVENTORY_STATE) {
            
            if (gameState == PLAY_STATE) {
                player.update(); 
            }
              
            player.getPlayerAction().update();
            
            int playerCenterX = player.getWorldX() + player.getPlayerVisualWidth() / 2;
            int playerCenterY = player.getWorldY() + player.getPlayerVisualHeight() / 2;
    
            
            int playerCol = playerCenterX / tileSize;
            int playerRow = playerCenterY / tileSize;
            int tileType = currentMap.getTile(playerCol, playerRow);
            if (tileType == 5) { 
            if (currentMap.getMapName().equals("Farm Map")) {
                FarmMap farmMapRef = (FarmMap) farmMap;
                
                if (playerCol == farmMapRef.getTeleportToHouseCol() && playerRow == farmMapRef.getTeleportToHouseRow() || playerCol == farmMapRef.getTeleportToHouseCol()+1 &&playerRow == farmMapRef.getTeleportToHouseRow()) {
                    teleportToMap("House Map");
                } else if (playerCol == FarmMap.FARM_COLS - 1) {
                    
                    enterMapMenuState();
                }
            } else if (currentMap.getMapName().equals("World Map")) {
                teleportToMap("Farm Map");
            } else if (currentMap.getMapName().equals("House Map")) {
                
                teleportToMap("Farm Map");
            } else if (currentMap.getMapName().equals("Forest River Map")) {
                if (playerCol == 0) { 
                    teleportToMap("Farm Map");
                }
            } else if (currentMap.getMapName().equals("Mountain Lake")) {
                if (playerCol == 0) { 
                    teleportToMap("Farm Map");
                }
            } else if (currentMap.getMapName().equals("Ocean Map")) {
                if (playerCol == 0) { 
                    teleportToMap("Farm Map");
                }
            } else if (currentMap.getMapName().equals("Store Map")) {
                
                    teleportToMap("Farm Map");
                  } else if (currentMap.getMapName().equals("Abigail's House") ||
                       currentMap.getMapName().equals("Caroline's House") ||
                       currentMap.getMapName().equals("Dasco's House") ||
                       currentMap.getMapName().equals("Emily's House") ||
                       currentMap.getMapName().equals("Mayor Tadi's House") ||
                       currentMap.getMapName().equals("Perry's House")) {
                
                teleportToMap("Farm Map");
            }
        }
        }
        
        
        if (currentMap.getMapName().equals("Forest River Map")) {
            
            centerCameraOnMap(ForestRiverMap.FOREST_COLS, ForestRiverMap.FOREST_ROWS);
        } else if (currentMap.getMapName().equals("Mountain Lake")) {
            
            centerCameraOnMap(MountainLake.MOUNTAIN_COLS, MountainLake.MOUNTAIN_ROWS);
        } else if (currentMap.getMapName().equals("Ocean Map")) {
            
            centerCameraOnMap(OceanMap.OCEAN_COLS, OceanMap.OCEAN_ROWS);       
        } else if (currentMap.getMapName().equals("Store Map")) {
            
            centerCameraOnMap(StoreMap.STORE_COLS, StoreMap.STORE_ROWS);
        } else if (currentMap.getMapName().equals("Abigail's House") ||
                   currentMap.getMapName().equals("Caroline's House") ||
                   currentMap.getMapName().equals("Dasco's House") ||
                   currentMap.getMapName().equals("Emily's House") ||
                   currentMap.getMapName().equals("Mayor Tadi's House") ||
                   currentMap.getMapName().equals("Perry's House")) {
            
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
        } else {
            
            cameraX = player.getWorldX() - screenWidth / 2 + player.getPlayerVisualWidth() / 2;
            cameraY = player.getWorldY() - screenHeight / 2 + player.getPlayerVisualHeight() / 2;
            if (cameraX < 0) cameraX = 0;
            if (cameraY < 0) cameraY = 0;
            if (cameraX > getMaxWorldWidth() - screenWidth) cameraX = getMaxWorldWidth() - screenWidth;
            if (cameraY > getMaxWorldHeight() - screenHeight) cameraY = getMaxWorldHeight() - screenHeight;
        }
          
        updateBrightness();        
        if (gameState == PLAY_STATE) {
            
            boolean goldMilestone = player.getGold() >= 17209;
            boolean marriageMilestone = player.isMarried();
            
            if (goldMilestone && marriageMilestone && !milestonesShown) {
                System.out.println("*** BOTH MILESTONES ACHIEVED! ***");
                System.out.println("Player gold: " + player.getGold() + " (Required: 17,209)");
                System.out.println("Marriage status: " + (player.isMarried() ? "MARRIED" : "SINGLE") + " (Required: MARRIED)");
                System.out.println("Creating new EndGame instance with updated data...");
                
                
                this.endGame = new SRC.ENDGAME.EndGame(player, this);
                
                
                endGameUI.showEndGameScreen(endGame);
                
                System.out.println("Switching to EndGame state...");
                gameState = ENDGAME_STATE;
                milestonesShown = true; 
            }
        }
        
        
        if (gameState == TV_STATE) {
            
            player.getPlayerAction().update();
        }
        
        
    }
    
    /**
     * Switch to a different map
     * @param newMap The map to switch to
     */
    public void switchMap(Map newMap) {
        
        currentMap.setActive(false);
        
        
        newMap.setActive(true);
        currentMap = newMap;
        
        System.out.println("Switched to map: " + newMap.getMapName());
    }    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        
        if (gameState == MAIN_MENU_STATE) {
            if (mainMenu != null) {
                mainMenu.draw(g2);
            }
        } else if (gameState == NEW_GAME_STATE) {
            if (newGameUI != null) {
                newGameUI.draw(g2);
            }
        } else if (gameState == LOAD_GAME_STATE) {
            if (loadGameUI != null) {
                loadGameUI.draw(g2);
            }
        } else if (gameState == OPTIONS_STATE) {
            if (optionsUI != null) {
                optionsUI.draw(g2);
            }
        } 
        
        else if (gameState == PLAY_STATE || gameState == CHEAT_STATE) {
            
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            
            currentMap.draw(g2);
            
            
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            
            player.draw(g2, playerScreenX, playerScreenY);
            
            
            applyNightEffect(g2);
              
            clockUI.drawTimeInfo(g2, player);            
            dayUI.drawDayInfo(g2);
              
            energyUI.drawEnergyBar(g2, player);
            
            
            holdingItemUI.drawHeldItems(g2, player);       
        } 
        
        else if (gameState == INVENTORY_STATE) {
            
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            currentMap.draw(g2);
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            player.draw(g2, playerScreenX, playerScreenY);
            
            
            applyNightEffect(g2);
            
            
            drawInventoryScreen(g2);       
        } 
            
        else if (gameState == SLEEP_STATE) {
            
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            currentMap.draw(g2);
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            player.draw(g2, playerScreenX, playerScreenY);
            
            
            applyNightEffect(g2);
            
            
            player.getPlayerAction().getSleepUI().draw(g2);
        } else if (gameState == MAP_MENU_STATE) {
            
            if (mapMenuImages != null && currentMapMenuIndex >= 0 && currentMapMenuIndex < mapMenuImages.length) {
                BufferedImage currentImage = mapMenuImages[currentMapMenuIndex];
                if (currentImage != null) {
                    
                    
                    double scaleFactor = (double)(screenWidth * 0.4) / currentImage.getWidth();
                    int scaledWidth = (int)(currentImage.getWidth() * scaleFactor);
                    int scaledHeight = (int)(currentImage.getHeight() * scaleFactor);
                    
                    int x = (screenWidth - scaledWidth) / 2;
                    int y = (screenHeight - scaledHeight) / 2;
                    
                    
                    g2.drawImage(currentImage, x, y, scaledWidth, scaledHeight, null);
                    
                }                else {
                    g2.setColor(Color.RED);
                    g2.drawString("Error loading map image", screenWidth/2 - 80, screenHeight/2);
                }
            }        
        } 
        else if (gameState == SHIPPING_STATE) {
            
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            currentMap.draw(g2);
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;            
            player.draw(g2, playerScreenX, playerScreenY);
            
            
            applyNightEffect(g2);
              
            if (shippingBinUI != null) {
                shippingBinUI.draw(g2);
            }        } else if (gameState == STORE_STATE) {
            
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            currentMap.draw(g2);
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            player.draw(g2, playerScreenX, playerScreenY);
            
            
            applyNightEffect(g2);
            
            
            if (this.getStoreUI() != null) {
                this.getStoreUI().draw(g2);
            }        
        } 
        
        else if (gameState == COOKING_STATE) {
            
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            currentMap.draw(g2);
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            player.draw(g2, playerScreenX, playerScreenY);

            applyNightEffect(g2);
            
            
            if (cookingUI != null) {
                cookingUI.draw(g2);
            }        
        } 
        
        else if (gameState == TV_STATE) {
            
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            currentMap.draw(g2);
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            player.draw(g2, playerScreenX, playerScreenY);
            
            
            applyNightEffect(g2);
            
            
            if (player.getPlayerAction().getTvUI() != null) {
                player.getPlayerAction().getTvUI().draw(g2);
            }        
        }
         else if (gameState == ENDGAME_STATE) {
            
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            currentMap.draw(g2);
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            player.draw(g2, playerScreenX, playerScreenY);
            
            applyNightEffect(g2);
            
            if (endGameUI != null) {
                endGameUI.draw(g2);
            }
        } else if (gameState == PLAYER_STATISTICS_STATE) {
            
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
            currentMap.draw(g2);
            int playerScreenX = player.getWorldX() - cameraX;
            int playerScreenY = player.getWorldY() - cameraY;
            player.draw(g2, playerScreenX, playerScreenY);
            
            applyNightEffect(g2);
            
            if (playerStatisticsUI != null) {
                playerStatisticsUI.draw(g2);
            }
        }
        
        
        if (gameState == CHEAT_STATE) {
            cheatUI.draw(g2);
        }
        
        npcUi.drawMessagePanel(g2);
        g2.dispose();
    } 
    

    public void enterMapMenuState() {
        System.out.println("Entering map menu state");
        this.gameState = MAP_MENU_STATE;
        this.currentMapMenuIndex = 0;
        addMinutes(15);
        player.consumeEnergy(10);
        System.out.println("Game state is now: " + (gameState == PLAY_STATE ? "PLAY_STATE" : "MAP_MENU_STATE"));
        
        
        repaint();
    }   
    public void exitMapMenuState() {
        System.out.println("Exiting map menu state");

        int selectedMap = currentMapMenuIndex;

        this.gameState = PLAY_STATE;
 
        System.out.println("Selected map: worldmap" + (selectedMap + 1));

        if (selectedMap == 0) {
            
            switchMap(forestrivermap);
            if (!isInitializedWorldMap) {
                forestrivermap.setupInitialObjects();
                isInitializedWorldMap = true;
            }

            player.setWorldX(tileSize * 1); 
            player.setWorldY(tileSize * 2); 
            
            System.out.println("Teleported player to ForestRiverMap at position (1, 2)");
            
            
            centerCameraOnMap(ForestRiverMap.FOREST_COLS, ForestRiverMap.FOREST_ROWS);        }
        else if (selectedMap == 1) {
            
            switchMap(mountainLake);
            if (!isInitializedMountainLake) {
                mountainLake.setupInitialObjects();
                isInitializedMountainLake = true;
            }
            
            
            player.setWorldX(tileSize * 1); 
            player.setWorldY(tileSize * 2); 
            
            System.out.println("Teleported player to Mountain Lake at position (1, 2)");
            
            
            centerCameraOnMap(MountainLake.MOUNTAIN_COLS, MountainLake.MOUNTAIN_ROWS);        }
        else if (selectedMap == 2) {
            
            switchMap(oceanMap);
            if (!isInitializedOceanMap) {
                oceanMap.setupInitialObjects();
                isInitializedOceanMap = true;
            }
            
            
            player.setWorldX(tileSize * 1); 
            player.setWorldY(tileSize * 2); 
            
            System.out.println("Teleported player to Ocean Map at position (1, 2)");
            
            
            centerCameraOnMap(OceanMap.OCEAN_COLS, OceanMap.OCEAN_ROWS);
        }
        else if (selectedMap == 3) {
            
            switchMap(storeMap);
            if (!isInitializedStoreMap) {
                storeMap.setupInitialObjects();
                isInitializedStoreMap = true;
            }
            
            
            player.setWorldX(tileSize * ((StoreMap.STORE_COLS / 2) - 1)); 
            player.setWorldY(tileSize * (StoreMap.STORE_ROWS - 2));

            System.out.println("Teleported player to Store Map at entrance");
              
            centerCameraOnMap(StoreMap.STORE_COLS, StoreMap.STORE_ROWS);
        }
        else if (selectedMap == 9) {
            
            switchToAbigailHouse();
            if (!isInitializedAbigailHouse) {
                abigailHouseMap.setupInitialObjects();
                isInitializedAbigailHouse = true;
            }
            
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 1));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Abigail's House at entrance");
            
            
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((AbigailHouseMap)abigailHouseMap).ensureNPCsVisible();
        }
        else if (selectedMap == 5) {
            
            switchToCarolineHouse();
            if (!isInitializedCarolineHouse) {
                carolineHouseMap.setupInitialObjects();
                isInitializedCarolineHouse = true;
            }
            
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 1));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Caroline's House at entrance");
            
            
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((CarolineHouseMap)carolineHouseMap).ensureNPCsVisible();
        }
        else if (selectedMap == 7) {
            
            switchToDascoHouse();
            if (!isInitializedDascoHouse) {
                dascoHouseMap.setupInitialObjects();
                isInitializedDascoHouse = true;
            }
            
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 1));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Dasco's House at entrance");
            
            
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            
            
            ((DascoHouseMap)dascoHouseMap).ensureNPCsVisible();
        }
        else if (selectedMap == 8) {
            
            switchToEmilyHouse();
            if (!isInitializedEmilyHouse) {
                emilyHouseMap.setupInitialObjects();
                isInitializedEmilyHouse = true;
            }
            
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 1));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Emily's House at entrance");
            
            
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((EmilyHouseMap)emilyHouseMap).ensureNPCsVisible();
        }
        else if (selectedMap == 4) {
            
            switchToMayorTadiHouse();
            if (!isInitializedMayorTadiHouse) {
                mayorTadiHouseMap.setupInitialObjects();
                isInitializedMayorTadiHouse = true;
            }
            
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 1));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Mayor Tadi's House at entrance");
            
            
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((MayorTadiHouseMap)mayorTadiHouseMap).ensureNPCsVisible();

        }
        else if (selectedMap == 6) {
            switchToPerryHouse();
            if (!isInitializedPerryHouse) {
                perryHouseMap.setupInitialObjects();
                isInitializedPerryHouse = true;
            }
            
            
            player.setWorldX(tileSize * ((NPCHouseMap.NPC_HOUSE_COLS / 2) - 1));
            player.setWorldY(tileSize * (NPCHouseMap.NPC_HOUSE_ROWS - 2));
            
            System.out.println("Teleported player to Perry's House at entrance");
            
            
            centerCameraOnMap(NPCHouseMap.NPC_HOUSE_COLS, NPCHouseMap.NPC_HOUSE_ROWS);
            ((PerryHouseMap)perryHouseMap).ensureNPCsVisible();
        }
        else {
            
            switchMap(forestrivermap);
            if (!isInitializedWorldMap) {
                forestrivermap.setupInitialObjects();
                isInitializedWorldMap = true;
            }
            
            
            player.setWorldX(tileSize * 1);
            player.setWorldY(tileSize * 2);
            
            
            centerCameraOnMap(ForestRiverMap.FOREST_COLS, ForestRiverMap.FOREST_ROWS);
        }
        
        
        repaint();
    }
      /**
     * Navigate to the previous map in the menu
     */
    public void selectPreviousMap() {
        currentMapMenuIndex--;
        if (currentMapMenuIndex < 0) {
            currentMapMenuIndex = TOTAL_WORLD_MAPS - 1; 
        }
        System.out.println("Selected map index: " + (currentMapMenuIndex + 1));
        
        repaint();
    }
    
    /**
     * Navigate to the next map in the menu
     */
    public void selectNextMap() {
        currentMapMenuIndex++;
        if (currentMapMenuIndex >= TOTAL_WORLD_MAPS) {
            currentMapMenuIndex = 0; 
        }
        System.out.println("Selected map index: " + (currentMapMenuIndex + 1));
        
        repaint();    }

    
    public Season getSeason() {
        return clockUI.getCurrentSeason();
    }

    public String getCurrentSeasonName() {
        return clockUI.getCurrentSeason().getDisplayName();
    }

    public Weather getWeather() {
        return clockUI.getCurrentWeather();
    }

    public boolean isRainy() {
        return clockUI.getCurrentWeather() == Weather.RAINY;
    }

    public boolean isSunny() {
        return clockUI.getCurrentWeather() == Weather.SUNNY;
    }

    public String getWeatherString() {
        return clockUI.getCurrentWeather().getDisplayName();
    }
    

    private void drawInventoryScreen(Graphics2D g2) {
        
        g2.setColor(new Color(0, 0, 0, 180)); 
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        
        int inventoryWidth = 128 * scale; 
        int inventoryHeight = 128 * scale; 
        int x = (screenWidth - inventoryWidth) / 2;
        int y = (screenHeight - inventoryHeight) / 2;
        
        
        if (inventoryImage != null) {
            g2.drawImage(inventoryImage, x, y, inventoryWidth, inventoryHeight, null);
        } else {
            
            g2.setColor(new Color(139, 69, 19, 200)); 
            g2.fillRoundRect(x, y, inventoryWidth, inventoryHeight, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(x, y, inventoryWidth, inventoryHeight, 15, 15);
        }
        
        
        int slotSize = 32 * scale; 
        int slotSpacing = 0; 
        int slotOffsetX = 0; 
        int slotOffsetY = 0; 
        
        
        int selectedSlotIndex = mouseHandler.getSelectedSlotIndex();
        
        
        for (int row = 0; row < INVENTORY_ROWS; row++) {
            for (int col = 0; col < INVENTORY_COLS; col++) {
                
                int slotX = x + slotOffsetX + col * (slotSize + slotSpacing);
                int slotY = y + slotOffsetY + row * (slotSize + slotSpacing);
                
                
                int slotIndex = row * INVENTORY_COLS + col;
                
                
                if (slotIndex == selectedSlotIndex) {
                    g2.setColor(new Color(255, 255, 100, 180)); 
                    g2.fillRect(slotX, slotY, slotSize, slotSize);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(slotX, slotY, slotSize, slotSize);
                }
                  
                Item[] playerItems = player.getInventoryItems();
                int[] playerQuantities = player.getInventoryQuantities();
                if (slotIndex < playerItems.length && playerItems[slotIndex] != null) {
                    Item item = playerItems[slotIndex];
                    int quantity = playerQuantities[slotIndex];
                    
                    
                    if (item.getImage() != null) {
                        
                        int scaledWidth = (int)(item.getImage().getWidth() * 1.5);
                        int scaledHeight = (int)(item.getImage().getHeight() * 1.5);
                        int itemX = slotX + (slotSize - scaledWidth) / 2;
                        int itemY = slotY + (slotSize - scaledHeight) / 2;
                        g2.drawImage(item.getImage(), itemX, itemY, scaledWidth, scaledHeight, null);
                    }
                    
                    
                    if (quantity > 1) {
                        g2.setColor(Color.WHITE);
                        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12));
                        String quantityText = "x" + quantity;
                        g2.drawString(quantityText, slotX + slotSize - 25, slotY + slotSize - 5);
                    }
                }
            }
        }
        
        
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24));
        String titleText = "Inventory";
        int titleWidth = g2.getFontMetrics().stringWidth(titleText);
        int titleX = x + (inventoryWidth - titleWidth) / 2;
        g2.drawString(titleText, titleX, y - 15);
          
        int removeButtonWidth = 80; 
        int removeButtonHeight = 30;
        int removeButtonX = x + inventoryWidth + 10;
        int removeButtonY = y + 20;
        
        
        g2.setColor(new Color(200, 60, 60)); 
        g2.fillRoundRect(removeButtonX, removeButtonY, removeButtonWidth, removeButtonHeight, 10, 10);
        
        
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(removeButtonX, removeButtonY, removeButtonWidth, removeButtonHeight, 10, 10);
        
        
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14));
        String removeButtonText = "Remove";
        int removeButtonTextWidth = g2.getFontMetrics().stringWidth(removeButtonText);
        int removeButtonTextX = removeButtonX + (removeButtonWidth - removeButtonTextWidth) / 2;
        g2.drawString(removeButtonText, removeButtonTextX, removeButtonY + 20);
        
        
        int useButtonWidth = 80;
        int useButtonHeight = 30;
        int useButtonX = x + inventoryWidth + 10;
        int useButtonY = removeButtonY + removeButtonHeight + 10; 
        
        
        g2.setColor(new Color(60, 200, 60)); 
        g2.fillRoundRect(useButtonX, useButtonY, useButtonWidth, useButtonHeight, 10, 10);
        
        
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(useButtonX, useButtonY, useButtonWidth, useButtonHeight, 10, 10);
        
        
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14));        String useButtonText = "Use";
        int useButtonTextWidth = g2.getFontMetrics().stringWidth(useButtonText);
        int useButtonTextX = useButtonX + (useButtonWidth - useButtonTextWidth) / 2;        g2.drawString(useButtonText, useButtonTextX, useButtonY + 20);
    }

    public void pauseTime() {
        clockUI.pauseTime();
    }
    

    public void resumeTime() {
        clockUI.resumeTime();
    }
    

    public void addGameTime(int minutes) {
        clockUI.addGameTime(minutes);
    }

    public void addMinutes(int minutes) {
        clockUI.addGameTime(minutes);
    }

    public void addHours(int hours) {
        clockUI.addGameTime(hours * 60);
    }


    public void timeskipTo(int hour, int minute, boolean teleportHomeIfMarried) {
        Time t = clockUI.getCurrentTime();
        t.setHour(hour);
        t.setMinute(minute);
        if (teleportHomeIfMarried){
            teleportToMap("House Map");
            System.out.println("harusnya teleport ke rumah");
        }
    }


    public Time getCurrentTime() {
        return clockUI.getCurrentTime();
    }
    
    public int getCurrentDay() {
        return clockUI.getCurrentDay();
    }   

    public void advanceToNextDay() {
        clockUI.advanceToNextDay();
        
        
        int removedPlants = tileManager.removeMismatchedSeasonPlants();
        if (removedPlants > 0) {
            System.out.println("Removed " + removedPlants + " plants due to season change.");
        }
    }
    
    
    
    private NPCUi npcUi;

    
    public void tryGiftToNearbyNPC() {
        npcUi.tryGiftToNearbyNPC();
    }

    public void tryTalkToNearbyNPC() {
        npcUi.tryTalkToNearbyNPC();
    }

    public void confirmGiftFromInventory() {
        npcUi.confirmGiftFromInventory();
    }

    public NPCEntity getGiftingTargetNPC() {
        return npcUi.getGiftingTargetNPC();
    }


    public NPCEntity getNearbyNPC(int distance) {
        return npcUi.getNearbyNPC(distance);
    }

    public NPCUi getNPCUi() {
        return npcUi;
    }

    public void showMessagePanel(String text) {
        npcUi.showMessagePanel(text);
    }    

    public void showNPCInteractionMenu(SRC.ENTITY.NPCEntity npc) {
        
        if (npc != null) {
            npcUi.openNPCInteractionMenu(npc);
        }
        repaint();
    }    
    public void closeNPCInteractionMenu() {
        npcUi.closeNPCInteractionMenu();
        repaint();
    }    
    public boolean isNPCInteractionMenuOpen() {
        return npcUi.isNPCInteractionMenuOpen();
    }

    public SRC.ENTITY.NPCEntity getNPCInteractionTarget() {
               return npcUi.getNPCInteractionTarget();
    }

    public boolean isMessagePanelActive() {
        return npcUi.isMessagePanelActive();
    }   
    private boolean isNightTime() {
        Time currentTime = clockUI.getCurrentTime();
        int hour = currentTime.getHour();
        
        return hour >= 18 || hour < 6;
    }
    

    private boolean isMapAffectedByNight() {
        String mapName = currentMap.getMapName();
        return mapName.equals("Farm Map") || 
               mapName.equals("Ocean Map") || 
               mapName.equals("Mountain Lake") || 
               mapName.equals("Forest River Map");
    }
    

    private void updateBrightness() {
        if (isMapAffectedByNight() && isNightTime()) {
            currentBrightness = 0.5f; 
        } else {
            currentBrightness = 1.0f; 
        }
    }
  
    private void applyNightEffect(Graphics2D g2) {
        if (currentBrightness < 1.0f) {
            
            int alpha = (int)((1.0f - currentBrightness) * 255);
            Color nightOverlay = new Color(0, 0, 0, alpha);
            g2.setColor(nightOverlay);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }
    }

    public CheatUI getCheatUI() {
        return cheatUI;
    }    public void toggleCheatConsole() {
        if (gameState == CHEAT_STATE) {
            gameState = PLAY_STATE;
            cheatUI.toggle();
            
            keyHandler.resetAllKeyStates();
            System.out.println("DEBUG: Cheat console closed, key states reset");
        } else {
            gameState = CHEAT_STATE;
            cheatUI.toggle();
            System.out.println("DEBUG: Cheat console opened");
        }
    }
    private GameTime gameTime;

    public void setSeason(Season season) {
        if (gameTime != null) {
            gameTime.setCurrentSeason(season);
        }
    }

    public void setWeather(Weather weather) {
        if (gameTime != null) {
            gameTime.setCurrentWeather(weather);
        }
    }

    public SRC.ENDGAME.EndGame getEndGame() {
        return endGame;
    }    public SRC.UI.EndGameUI getEndGameUI() {
        return endGameUI;
    }

    public SRC.UI.PlayerStatisticsUI getPlayerStatisticsUI() {
        return playerStatisticsUI;
    }public SRC.MAIN.MENU.MainMenu getMainMenu() {
        return mainMenu;
    }
    
    public SRC.UI.NewGameUI getNewGameUI() {
        return newGameUI;
    }
    
    public SRC.UI.LoadGameUI getLoadGameUI() {
        return loadGameUI;
    }
      public SRC.UI.OptionsUI getOptionsUI() {
        return optionsUI;
    }
    

    public MusicManager getMusicManager() {
        return musicManager;
    }
    

    public void cleanup() {
        if (musicManager != null) {
            musicManager.dispose();
        }
        
        gameThread = null;
    }
}
