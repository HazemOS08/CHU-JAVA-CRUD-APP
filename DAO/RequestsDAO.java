package DAO;
import java.time.LocalDate;
import java.util.List;
import Table_Classes.Requests;

public interface RequestsDAO {
       
    Requests findById(int id);

    List<Requests> findByUserId(int id);
    List<Requests> findByTypeId(int id);
    List<Requests> findByStatus(String s);
    List<Requests> findByDate(LocalDate date);
    List<Requests> findByDepartmentId(int id);

    boolean insert(Requests req);
    boolean delete(int id);
    boolean update(Requests req);

}