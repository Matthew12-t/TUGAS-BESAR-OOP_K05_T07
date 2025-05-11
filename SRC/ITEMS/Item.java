package SRC.ITEMS;

public abstract class Item {
    private String name;
    private String category;
    private int sellPrice;
    private int buyPrice;

    // Constructor
    public Item(String name, String category, int sellPrice, int buyPrice) {
        this.name = name;
        this.category = category;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
    }
    // Getters
    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public int getSellPrice() {
        return sellPrice;
    }
    public int getBuyPrice() {
        return buyPrice;
    }
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }
    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }    
}
