package SRC.ENTITY;
import java.util.List;
import SRC.ITEMS.Item;

public interface NPC {
    String getNPCName();
    String getLocation();
    String getDescription();
    void interact(Player player);
    String getRelationshipStatus(Player player);
    List<Item> getLovedItems();
    List<Item> getLikedItems();
    List<Item> getHatedItems();
    void performAction(Player player, String action);
}
