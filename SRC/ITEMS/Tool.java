package SRC.ITEMS;

/**
 * Represents a tool item that can be used by the player
 */
public class Tool extends Item {
    private String description;
    
    /**
     * Creates a new tool
     * 
     * @param name The name of the tool
     * @param sellPrice The price when selling this tool
     * @param buyPrice The price when buying this tool
     * @param description The description of what this tool does
     */    
    public Tool(String name, int sellPrice, int buyPrice, String description) {
        super(name, "Tool", sellPrice, buyPrice);
        this.description = description;
        
        // Load image untuk tool ini
        String imagePath = "RES/TOOLS/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }
    
    /**
     * Get the tool description
     * @return tool description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Set the tool description
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
