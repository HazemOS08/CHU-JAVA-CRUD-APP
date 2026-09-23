package Table_Classes;

public class Works {
    private int userId;
    private int posId;


    public Works() {}


    public Works(int userId, int posId) {
        this.userId = userId;
        this.posId = posId;
    }

    @Override
    public String toString() {
    return String.format("Works [User ID=%d | Pos ID=%d]", userId, posId);
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPosId() {
        return posId;
    }

    public void setPosId(int posId) {
        this.posId = posId;
    };

    
    
}
