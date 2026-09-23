package DAO;
import Table_Classes.Schedules;

import java.time.LocalDateTime;
import java.util.List;

public interface SchedulesDAO {
    
    Schedules findById(int id);
     
    //find the monthly schedule of a user 
    List<Schedules> findByUserIdAndRange(LocalDateTime start, LocalDateTime end, int userId);

    //find the monthly schedule of an entire departement
    List<Schedules> findByDepartmentIdAndRange(LocalDateTime start, LocalDateTime end, int deptId);

    boolean insert(Schedules schedule);
    boolean update(Schedules schedule);
    boolean delete(int id);

}
