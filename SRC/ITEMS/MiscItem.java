package SRC.ITEMS;
import SRC.ENTITY.Player;
public class MiscItem extends Item {
    private String description;

    public MiscItem(String name, int sellPrice, int buyPrice, String description) {
        super(name, "MiscItem", sellPrice, buyPrice);
        this.description = description;
        
        // Load image for this item
        String imagePath = "RES/MISC/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }

    // Getters
    public String getDescription() {
        return description;
    }

    // Setters
    public void setDescription(String description) {
        this.description = description;
    }
    public void use(Player player) {
        System.out.println("Using " + getName() + ": " + description);
    }
    
}
