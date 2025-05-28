package SRC.ENTITY;
import java.util.List;
import SRC.ENTITY.Player;
public interface NPC {
    // Nama NPC yang memiliki rumah
    String getNPCName();

    // Lokasi rumah NPC di peta (misalnya koordinat atau nama lokasi)
    String getLocation();

    // Deskripsi dari rumah NPC (misalnya ukuran, jenis bangunan, dll.)
    String getDescription();

    // Metode untuk berinteraksi dengan rumah NPC (misalnya berbicara atau memberikan item)
    void interact(Player player);

    // Menampilkan status hubungan antara Player dan NPC yang tinggal di rumah ini
    String getRelationshipStatus(Player player);

    // Mendapatkan daftar item yang dicintai NPC (bisa digunakan untuk memberi hadiah)
    List<String> getLovedItems();

    // Mendapatkan daftar item yang disukai NPC
    List<String> getLikedItems();

    // Mendapatkan daftar item yang dibenci NPC
    List<String> getHatedItems();

    // Metode untuk melakukan aksi spesifik, misalnya memberikan hadiah atau mengobrol dengan NPC
    void performAction(Player player, String action);
}
