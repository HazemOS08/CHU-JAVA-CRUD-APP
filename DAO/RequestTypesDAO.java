package DAO;
import Table_Classes.RequestTypes;
import java.util.List;

public interface RequestTypesDAO {

    RequestTypes findById(int id);
    RequestTypes findByType(String type);
    List<RequestTypes> findAll();
    
    boolean insert(RequestTypes req);
    boolean update(RequestTypes req);
    boolean delete(int id);

}
