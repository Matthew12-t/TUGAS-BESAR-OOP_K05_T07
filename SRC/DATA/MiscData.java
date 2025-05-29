package SRC.DATA;
import SRC.ITEMS.MiscItem;
import java.util.HashMap;
import java.util.Map;
public class MiscData {
    private static Map<String, MiscItem> miscItems = new HashMap<>();
    static {
        initializeMiscItems();
    }
    private static void initializeMiscItems() {
        miscItems.put("Wood", new MiscItem("FireWood", 5, 10, "Buat Masak"));
        miscItems.put("Coal", new MiscItem("Coal",  10, 20, "Buat Masak"));
        miscItems.put("Egg", new MiscItem("Egg", 5, 10, "Telor ayam"));
        miscItems.put("Eggplant", new MiscItem("Eggplant", 15, 30, "Terong"));
        System.out.println("MiscData: Initialized " + miscItems.size() + " miscellaneous items");
    }
    public static MiscItem getMiscItem(String itemName) {
        return miscItems.get(itemName);
    }
    public static MiscItem addMiscItem(String itemName, int quantity) {
        MiscItem item = getMiscItem(itemName);
        if (item != null) {
            return item;
        } else {
            System.err.println("Misc item not found: " + itemName);
            return null;
        }
    }
    public static Map<String, MiscItem> getAllMiscItems() {
        return new HashMap<>(miscItems);
    }
    public static void addMiscItem(MiscItem item) {
        if (item != null && !miscItems.containsKey(item.getName())) {
            miscItems.put(item.getName(), item);
            System.out.println("Added new misc item: " + item.getName());
        } else {
            System.err.println("Item already exists or is null: " + item);
        }
    }
    public static void removeMiscItem(String itemName) {
        if (miscItems.remove(itemName) != null) {
            System.out.println("Removed misc item: " + itemName);
        } else {
            System.err.println("Item not found for removal: " + itemName);
        }
    }
    public static Map<String, MiscItem> getMiscItems() {
        return new HashMap<>(miscItems);
    }
    public static boolean hasMiscItem(String itemName) {
        return miscItems.containsKey(itemName);
    }
}
