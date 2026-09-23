package DAO;
import java.util.List;
import Table_Classes.User;
public interface UserDAO {

    User findById(int id);
    User findByUsername(String name);

    List<User>  findByFirstName(String name);
    List<User>  findByLastName(String name);
    List<User> findAll();

    boolean insert(User user);
    boolean delete( int id);
    boolean update(User user);

    
} 
