package DAO.DAO_Implementation;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import DAO.UserDAO;
import Table_Classes.User;
import db.DbHelper;
public class UserDAOImp implements UserDAO{
    @Override
    public User findById(int id){
        String s="SELECT * FROM users WHERE id= ? ORDER BY role DESC";
       try(Connection con = DbHelper.getConnection();
         PreparedStatement st = con.prepareStatement(s) ){
          

          st.setInt(1,id);

            try (ResultSet r = st.executeQuery()){
                
                if(r.next()){


                    return setToUser(r);
                }
            

       }
    }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
    return null;
    } 

    @Override
    public User findByUsername(String name){

       String s="SELECT * FROM users WHERE username= ? ORDER  BY role DESC";

       User user=null;

       try(Connection con = DbHelper.getConnection();
           PreparedStatement st = con.prepareStatement(s) ){

          
          st.setString(1,name);

            try (ResultSet r = st.executeQuery()){
                
                if(r.next()){

                    user=setToUser(r);

                }
                

            }
        }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
        return user;
    } 

    @Override
    public List<User> findByFirstName(String name){

       String s="SELECT * FROM users WHERE first_name= ? ORDER  BY role DESC";

       List<User> l= new ArrayList<>();

       try(Connection con = DbHelper.getConnection();
         PreparedStatement st = con.prepareStatement(s) ){

         

          st.setString(1,name);

            try (ResultSet r = st.executeQuery()){
                
                while(r.next()){

                    l.add(setToUser(r));
                     
                }
                

            }
        }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
        return l;
    }
    
    @Override
    public List<User> findByLastName(String name){

       String s="SELECT * FROM users WHERE last_name= ? ORDER  BY role DESC";

       List<User> l= new ArrayList<>();

       try(Connection con = DbHelper.getConnection();
          PreparedStatement st = con.prepareStatement(s) ){

          

          st.setString(1,name);

            try (ResultSet r = st.executeQuery()){
                
                while(r.next()){


                     l.add(setToUser(r)) ;
                }
                

            }
        }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
        return l;
    } 

    @Override
    public List<User> findAll(){

       String s="SELECT * FROM users ORDER BY role";

       List<User> l= new ArrayList<>();

       try(Connection con = DbHelper.getConnection();
            Statement st = con.createStatement() ){

          
            try (ResultSet r = st.executeQuery(s)){
                
                while(r.next()){

                   l.add(setToUser(r));
                }
                
            }
        }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
        return l;
    } 


    private User setToUser(ResultSet r) throws SQLException {
    String dateStr = r.getString("birth_date");
    LocalDate birthDate = (dateStr != null) ? LocalDate.parse(dateStr) : null;

    return new User(
        r.getInt("id"),
        r.getString("first_name"),
        r.getString("last_name"),
        birthDate,
        r.getString("role"),
        r.getString("password"),
        r.getString("username"),
        r.getString("email"),
        r.getString("cellular")
    );
}
    
    @Override
    public boolean insert (User user){
        String s="INSERT INTO users (first_name,last_name,birth_date," 
                  +"role,password,username,email,cellular)"
                   +"VALUES(?,?,?,?,?,?,?,?)";
        String S = "SELECT last_insert_rowid()"; // get last assigned id
        
        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)){

            
            st.setString(1, user.getFirstName());
            st.setString(2, user.getLastName());
            st.setString(3, user.getBirthDate().toString());
            st.setString(4, user.getRole());
            st.setString(5, user.getPassword());
            st.setString(6, user.getUsername());
            st.setString(7, user.getEmail());
            st.setString(8, user.getCellular());
            
            int rowInserted = st.executeUpdate();

            if(rowInserted>0){// success

               // get the SQL generated id and assign it back to the user
               try (Statement stm = con.createStatement();

                     ResultSet r = stm.executeQuery(S)  ) {

                if (r.next()) {

                    user.setId(r.getInt(1));
                }
            }

            return true;

            } 

        }catch(SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(int id){
        String s="DELETE FROM users WHERE id= ?";

        try(Connection con= DbHelper.getConnection();
            PreparedStatement st= con.prepareStatement(s)) {

            

            st.setInt(1, id);

            int r =st.executeUpdate(); // n of deleted rows

            return r > 0 ;

        } catch (SQLException e) {
            System.out.println("Error: "+e.getMessage());
        }

        return false;
    }

    @Override
    public boolean update(User user){
        
        String s="UPDATE users SET first_name=?"
        +", last_name=?, birth_date=?, username=? , password=? , role=?, cellular=?, email=? WHERE id=?";

        try(Connection con= DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
            st.setString(1, user.getFirstName());
            st.setString(2, user.getLastName());
            st.setString(3, user.getBirthDate().toString());
            st.setString(4, user.getUsername());
            st.setString(5, user.getPassword());
            st.setString(6, user.getRole());
            st.setString(7, user.getCellular());
            st.setString(8, user.getEmail());
            st.setInt(9,user.getId());

            return st.executeUpdate()>0;

        } catch (SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }

        return false;
    }

}

