package Table_Classes;
import java.time.LocalDate;
public class Requests {
    private int id;
    private int userId;
    private int typeId;
    private String message;
    private boolean status;
    private LocalDate date;

    public Requests() {
    }

    public Requests(int userId, int typeId, String message, boolean status, LocalDate date) {
        this.userId = userId;
        this.typeId = typeId;
        this.message = message;
        this.status = status;
        this.date=date;
    }

    public Requests(int id, int userId, int typeId, String message, boolean status, LocalDate date) {
        this(userId, typeId, message, status,date);
        this.id = id;

    }

    @Override 
    public String toString(){

        return String.format("Request [ ID: %-10d | User ID: %-10d | Status: %-10s | Date: %s | Description: '%s']",
            id, userId, status ? "solved" : "pending", date, message);
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    
}
