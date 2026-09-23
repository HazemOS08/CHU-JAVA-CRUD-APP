package DAO.DAO_Implementation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import DAO.AvailabilityDAO;
import Table_Classes.Availability;
import db.DbHelper;

public class AvailabilityDAOImp implements  AvailabilityDAO{
  
    private Availability setToAv(ResultSet r) throws SQLException{

        return new Availability(
            r.getInt("id"),
            LocalDate.parse(r.getString("last_updated")),
            r.getInt("days_week"),
            r.getString("info"),
            r.getInt("user_id"),
            r.getInt("is_active") == 1 ? true : false);

      };
    @Override
    public Availability findById(int id){

    String s="SELECT * FROM Availability WHERE id=?" ;
        
        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
            
                st.setInt(1,id);

                try(ResultSet r= st.executeQuery()){
                   
                    if(r.next()){

                        return setToAv(r);

                    }
                }
            
        } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }

        return null;

    }
    @Override
    public Availability findByUserId(int userId){
        String s="SELECT * FROM Availability WHERE user_id=?" ;
        
        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
            
                st.setInt(1,userId);

                try(ResultSet r= st.executeQuery()){
                   
                    if(r.next()){

                        return setToAv(r);

                    }
                }
            
        } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }

        return null;
    }
    @Override
    public List<Availability> findByDepartmentId(int id){

        String s = "SELECT a.* FROM Availability a " +
           "JOIN Works w ON a.user_id = w.user_id " +
           "JOIN Positions p ON w.pos_id = p.id " +
           "WHERE p.dept_id = ?";

         List<Availability> l = new ArrayList<>();

         try(Connection con= DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(s)) {
              
                st.setInt(1,id);

             try(ResultSet r=st.executeQuery()){
                
                while (r.next()) {
                    
                    l.add(setToAv(r));

                }

             }

         } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
         }

         return l;
    }
    @Override
    public boolean insert(Availability availability){
         String s="INSERT INTO Availability (last_updated,days_week,info,user_id,is_active)"
         +"VALUES(?,?,?,?,?)";
         String S="SELECT last_insert_rowid()"; // get last assigned id

         try(Connection con=DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(s)) {
            
                st.setString(1, availability.getLastUpdated().toString());
                st.setInt(2,availability.getFrequency());
                st.setString(3,availability.getInfo());
                st.setInt(4,availability.getUserId());
                st.setInt(5, availability.isActive() == true? 1:0);


                int rowInserted = st.executeUpdate();

                if(rowInserted >0){

                    try(Statement stm = con.createStatement()){

                        ResultSet r = stm.executeQuery(S);

                        if(r.next()){

                            availability.setId(r.getInt(1));
                        }
                    }
                }

        return true;

         } catch (SQLException e) {

            System.out.println("Error : "+e.getMessage());

         }

        return false;
    }
    @Override
    public boolean update(Availability availability){
        String s="UPDATE availability SET last_updated=?,days_week=?,info=?,"
        +"user_id=?, is_active=? WHERE id=?";

        try(Connection con= DbHelper.getConnection();

             PreparedStatement st= con.prepareStatement(s)) {
                
                st.setString(1,availability.getLastUpdated().toString());
                st.setInt(2,availability.getFrequency());
                st.setString(3,availability.getInfo());
                st.setInt(4,availability.getUserId());
                st.setInt(5,availability.isActive()==true? 1 : 0);


            return st.executeUpdate()>0;

        } catch (SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }

        return false;
    }
    @Override
    public boolean delete(int id){
        String s="DELETE FROM Availability WHERE id= ?";

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
}
