package Table_Classes;

public class RequestTypes {
    private int id;
    private String type;

    public RequestTypes() {
    }

    public RequestTypes(String type) {
        this.type = type;
    }

    public RequestTypes(int id, String type) {
        this.id = id;
        this.type = type;
    }
    
    @Override 
    public String toString(){
        return String.format("[ID: %-6d | type: %s",id,type);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    
}
