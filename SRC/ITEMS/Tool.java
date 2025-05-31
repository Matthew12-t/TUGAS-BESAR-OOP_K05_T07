package SRC.ITEMS;


public class Tool extends Item {
    private String description;
    

    public Tool(String name, int sellPrice, int buyPrice, String description) {
        super(name, "Tool", sellPrice, buyPrice);
        this.description = description;
        

        String imagePath = "RES/TOOLS/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }
    

    public String getDescription() {
        return description;
    }
    
 
    public void setDescription(String description) {
        this.description = description;
    }
}
