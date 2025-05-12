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
     * @return true jika penempatan berhasil, false jika gagal
     */
    public boolean deployHouse(int col, int row) {
        // Validasi apakah posisi valid untuk penempatan
        if (!gp.getCurrentMap().isValidPlacement(col, row)) {
            return false;
        }
        
        // Cari slot kosong di array objek
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                // Tempatkan rumah baru pada posisi ini
                objects[i] = new OBJ_House(gp, col, row);
                System.out.println("House deployed at " + col + "," + row);
                return true;
            }
        }
        
        // Tidak ada slot kosong ditemukan
        System.out.println("Failed to deploy house: no free object slots");
        return false;
    }
    
    /**
     * Menghapus objek pada posisi tertentu pada map aktif
     * @param col Posisi kolom pada grid map
     * @param row Posisi baris pada grid map
     * @return true jika objek berhasil dihapus, false jika tidak ada objek ditemukan
     */
    public boolean removeObject(int col, int row) {
        SuperObject[] objects = gp.getCurrentMap().getObjects();
        
        // Periksa setiap objek untuk melihat apakah ada di posisi yang diberikan
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                Tile pos = objects[i].getPosition();
                if (pos.getCol() == col && pos.getRow() == row) {
                    objects[i] = null; // Hapus objek
                    System.out.println("Object removed from " + col + "," + row);
                    return true;
                }
            }
        }
        
        System.out.println("No object found at " + col + "," + row);
        return false;
    }
}
