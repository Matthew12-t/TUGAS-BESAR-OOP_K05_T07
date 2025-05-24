package SRC.OBJECT;

import SRC.MAIN.GamePanel;
import SRC.TILES.Tile;

/**
 * Kelas ObjectDeployer digunakan untuk memanajemen penempatan objek di map game
 */
public class ObjectDeployer {
    
    private GamePanel gp;
    
    /**
     * Konstruktor untuk ObjectDeployer
     * @param gp Game panel yang digunakan
     */
    public ObjectDeployer(GamePanel gp) {
        this.gp = gp;
    }
    
    /**
     * Menempatkan rumah pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     */
    public void deployHouse(int col, int row) {
        // Validasi apakah posisi valid untuk penempatan
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            System.out.println("Cannot place house at " + col + "," + row + " - invalid placement");
            return;
        }
        
        // Cari slot kosong di array objek
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                // Tempatkan rumah baru pada posisi ini
                objects[i] = new OBJ_House(gp, col, row);
                System.out.println("House deployed at " + col + "," + row);
                return;
            }
        }
        
        // Tidak ada slot kosong ditemukan
        System.out.println("Failed to deploy house: no free object slots");
    }
    
    /**
     * Menempatkan kolam pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     */
    public void deployPond(int col, int row) {
        // Validasi apakah posisi valid untuk penempatan
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            System.out.println("Cannot place pond at " + col + "," + row + " - invalid placement");
            return;
        }
        
        // Cari slot kosong di array objek
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                // Tempatkan kolam baru pada posisi ini
                objects[i] = new OBJ_Pond(gp, col, row);
                System.out.println("Pond deployed at " + col + "," + row);
                return;
            }
        }
        
        // Tidak ada slot kosong ditemukan
        System.out.println("Failed to deploy pond: no free object slots");
    }
    
    /**
     * Menempatkan shipping bin pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     */
    public void deployShippingBin(int col, int row) {
        // Validasi apakah posisi valid untuk penempatan
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            System.out.println("Cannot place shipping bin at " + col + "," + row + " - invalid placement");
            return;
        }
        
        // Cari slot kosong di array objek
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                // Tempatkan shipping bin baru pada posisi ini
                objects[i] = new OBJ_ShippingBin(gp, col, row);
                System.out.println("Shipping bin deployed at " + col + "," + row);
                return;
            }
        }
        
        // Tidak ada slot kosong ditemukan
        System.out.println("Failed to deploy shipping bin: no free object slots");
    }
    
    /**
     * Menempatkan bed pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     */
    public void deployBed(int col, int row) {
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            System.out.println("Cannot place bed at " + col + "," + row + " - invalid placement");
            return;
        }
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                objects[i] = new OBJ_Bed(gp, col, row);
                System.out.println("Bed deployed at " + col + "," + row);
                return;
            }
        }
        System.out.println("Failed to deploy bed: no free object slots");
    }
    public void deployStove(int col, int row) {
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            System.out.println("Cannot place stove at " + col + "," + row + " - invalid placement");
            return;
        }
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                objects[i] = new OBJ_Stove(gp, col, row);
                ;
                System.out.println("Stove deployed at " + col + "," + row);
                return;
            }
        }
        System.out.println("Failed to deploy stove: no free object slots");
    }
    /**
     * Menempatkan table pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     */
    public void deployTable(int col, int row) {
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            System.out.println("Cannot place table at " + col + "," + row + " - invalid placement");
            return;
        }
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                objects[i] = new OBJ_Table(gp, col, row);
                System.out.println("Table deployed at " + col + "," + row);
                return;
            }
        }
        System.out.println("Failed to deploy table: no free object slots");
    }
     /**
     * Menempatkan bed pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     */
    public void deployChair(int col, int row) {
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            System.out.println("Cannot place chair at " + col + "," + row + " - invalid placement");
            return;
        }
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                objects[i] = new OBJ_Chair(gp, col, row);
                System.out.println("Chair deployed at " + col + "," + row);
                return;
            }
        }
        System.out.println("Failed to deploy chair: no free object slots");
    }
    /**
     * Menempatkan bed pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     */
    public void deployChair(int col, int row, int mode) {
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            System.out.println("Cannot place chair at " + col + "," + row + " - invalid placement");
            return;
        }
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                objects[i] = new OBJ_Chair(gp, col, row, mode);
                System.out.println("Chair (mode " + mode + ") deployed at " + col + "," + row);
                return;
            }
        }
        System.out.println("Failed to deploy chair: no free object slots");
    }
    /**
     * Menempatkan table pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     */
    public void deployTable(int col, int row, int mode) {
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            System.out.println("Cannot place table at " + col + "," + row + " - invalid placement");
            return;
        }
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                objects[i] = new OBJ_Table(gp, col, row, mode);
                System.out.println("Table (mode " + mode + ") deployed at " + col + "," + row);
                return;
            }
        }
        System.out.println("Failed to deploy table: no free object slots");
    }
    /**
     * Menghapus objek pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     */
    public void removeObject(int col, int row) {
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        
        // Periksa setiap objek untuk melihat apakah ada di posisi yang diberikan
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                Tile pos = objects[i].getPosition();
                if (pos.getCol() == col && pos.getRow() == row) {
                    objects[i] = null; // Hapus objek
                    System.out.println("Object removed from " + col + "," + row);
                    return;
                }
            }
        }
        
        System.out.println("No object found at " + col + "," + row);
    }
}