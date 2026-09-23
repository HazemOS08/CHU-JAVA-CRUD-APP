package Table_Classes;

public class Positions {
    private int id;
    private int deptId;
    private String name;

    public Positions() {
    }

    public Positions(int deptId, String name) {
        this.deptId = deptId;
        this.name = name;
    }

    public Positions(int id, int deptId, String name) {
        this(deptId,name);
        this.id = id;
    }
    
    @Override 
    public String toString(){
        return String.format("[ ID: %-10d | Departement ID: %-6d | Position: %s",id,deptId,name);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
}
