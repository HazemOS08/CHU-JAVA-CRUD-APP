package DAO;
import java.util.List;
import Table_Classes.Departments;

public interface DepartmentsDAO {
    
      Departments findById(int id);
      Departments findByName(String s);

      List<Departments> findAll();

      boolean insert(Departments depart);
      boolean delete(int id);
      boolean update(Departments depart);
}
