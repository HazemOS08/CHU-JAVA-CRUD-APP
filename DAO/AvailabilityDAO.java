package DAO;
import Table_Classes.Availability;
import java.util.List;
public interface AvailabilityDAO {
    Availability findById(int id);

    Availability findByUserId(int userId);
    List<Availability> findByDepartmentId(int id);
    
    boolean insert(Availability availability);
    boolean update(Availability availability);
    boolean delete(int id);
}
