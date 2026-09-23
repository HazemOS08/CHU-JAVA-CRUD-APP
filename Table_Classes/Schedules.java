package Table_Classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Schedules {
    private int id;
    private int userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int posId;


    private String positionName;

    public Schedules() {
    }

    public Schedules(int userId, LocalDateTime startTime, LocalDateTime endTime,int posId) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.posId=posId;
    }

    public Schedules(int id, int userId, LocalDateTime startTime, LocalDateTime endTime,int posId) {
        this(userId, startTime, endTime,posId);
        this.id = id;

    }

    @Override
    public String toString() {

    DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");

    String start = startTime.format(time);
    String end = endTime.format(time);


    String text = String.format("#%-3d P#%-3d %s-%s", id,posId, start, end);
    

    
    return String.format("%-22s",text);

}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public String getPositionName() {
        return positionName;
    }
    

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getPosId() {
        return posId;
    }

    public void setPosId(int posId) {
        this.posId = posId;
    }

    
    
}
