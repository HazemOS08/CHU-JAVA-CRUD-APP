package DAO;
import Table_Classes.Positions;
import java.util.List;

public interface PositionsDAO {
    
    Positions findById(int id);

    List<Positions> findByDeptId(int id);
    List<Positions> findByName(String s);
    List<Positions> findAll();
    boolean insert(Positions pos);
    boolean delete(int id);
    boolean  update(Positions pos);

}
