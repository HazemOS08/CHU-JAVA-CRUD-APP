package Table_Classes;

public class Departments {
    private int id;
    private String name;

    public Departments() {
    }

    public Departments(String name) {
        this.name = name;
    }

    public Departments(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Override 
    public String toString(){
        return String.format("[ ID: %-6d | Department: %-30s]",id,name);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
}
