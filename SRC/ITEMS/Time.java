package SRC.ITEMS;

public class Time {
    private int hour;
    private int minute;
    
    // Constructor
    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    // Getters
    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

 
    // Setters
    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

}
