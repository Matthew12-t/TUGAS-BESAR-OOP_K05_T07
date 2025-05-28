package SRC.ITEMS;

import SRC.ENTITY.Player;

public class Fish extends Item implements Edible {
    private String type;
    private String location;
    private String season;
    private String weather;    private Time time;
    private int energy;

    // Constructor
    public Fish(String name, int sellPrice, int buyPrice, String type, String location, String season, String weather, Time time) {
        super(name, "Fish", sellPrice, buyPrice);
        this.type = type;
        this.location = location;
        this.season = season;
        this.weather = weather;
        this.time = time;
        this.energy = 1;
        
        // Load image untuk fish ini
        String imagePath = "RES/FISH/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }
    // Getters
    public String getType() {
        return type;
    }
    public String getLocation() {
        return location;
    }
    public String getSeason() {
        return season;
    }
    public String getWeather() {
        return weather;
    }
    public Time getTime() {
        return time;
    }
    // Setters
    public void setType(String type) {
        this.type = type;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setSeason(String season) {
        this.season = season;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }
    public void setTime(Time time) {
        this.time = time;
    }
    @Override
    public int getEnergyValue() {
        return this.energy;
    }
    @Override
    public void consume(Player player) {
        // Logic to consume the fish and increase player's energy
        player.setEnergy(player.getEnergy() + getEnergyValue());
        System.out.println("You consumed " + this.getName() + " and gained " + getEnergyValue() + " energy!");
    }
}
    