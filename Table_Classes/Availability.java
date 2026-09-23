package Table_Classes;

import java.time.LocalDate;

public class Availability {
    private int id;
    private LocalDate lastUpdated;
    private int frequency; // how many days each week
    private String info; // any additional info like : doesn't work on Fridays
    private int userId;
    private boolean isActive;


    public Availability(){}


    public Availability(LocalDate lastUpdated, int frequency, String info, 
        int userId, boolean isActive) {
        this.lastUpdated = lastUpdated;
        this.frequency = frequency;
        this.info = info;
        this.userId = userId;
        this.isActive = isActive;
    }

    public Availability(int id, LocalDate lastUpdated, 
        int frequency, String info, int userId, boolean isActive) {
        this( lastUpdated,  frequency,  info, userId,  isActive) ;
        this.id = id;
    }

    @Override
    public String toString() {
        String active= this.isActive==true ? "Active" : "Inactive";
    return String.format(" [ID=%d | User ID=%d | Days/Week=%d | Status=%s | Info='%s']",
            id, userId, frequency, active, info);
}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    };
    
}
