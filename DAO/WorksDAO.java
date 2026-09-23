package DAO;
import java.util.List;

import Table_Classes.Works;
public interface WorksDAO {

    List<Works> findByUserId(int userId);
    List<Works> findByPosId(int posId);
    List<Works> findByDepartmentId(int id);
    List<Works> findAll();

    Works findByUserIdPosId(int userId,int posId);

    boolean insert(Works work);
    boolean delete(Works work);
    

}
