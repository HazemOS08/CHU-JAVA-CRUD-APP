package DAO.DAO_Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import DAO.RequestsDAO;
import Table_Classes.Requests;
import db.DbHelper;

public class RequestsDAOImp implements RequestsDAO{

    private Requests setToReq(ResultSet r)throws SQLException{
        boolean s = r.getString("status").equals("solved");
        LocalDate d = LocalDate.parse(r.getString("request_date"));

        return new Requests(r.getInt("id"),
            r.getInt("user_id"), 
             r.getInt("type_id"),
             r.getString("message"), 
             s,
             d);
    }
    @Override
    public Requests findById(int id){

        String s="SELECT * FROM Requests WHERE id=?" ;
        
        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
            
                st.setInt(1,id);

                try(ResultSet r= st.executeQuery()){
                   
                    if(r.next()){

                        return setToReq(r);

                    }
                }
            
        } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }

        return null;
    }

    @Override
    public List<Requests> findByUserId(int id){
        
         String s="SELECT * FROM Requests WHERE user_id=?";

         List<Requests> l = new ArrayList<>();
         
         try(Connection con= DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(s)) {
              
                st.setInt(1,id);
             try(ResultSet r=st.executeQuery()){
                
                while (r.next()) {
                    
                    l.add(setToReq(r));

                }

             }

         } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
         }

         return l;
      }
    

    @Override
    public List<Requests> findByTypeId(int id){

        String s="SELECT * FROM Requests WHERE type_id=?";

         List<Requests> l = new ArrayList<>();
         
         try(Connection con= DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(s)) {
              
                st.setInt(1,id);
             try(ResultSet r=st.executeQuery()){
                
                while (r.next()) {
                    
                    l.add(setToReq(r));

                }

             }

         } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
         }

         return l;
    }

    @Override
    public List<Requests> findByStatus(String s){

        String S="SELECT * FROM Requests WHERE status=?";

         List<Requests> l = new ArrayList<>();
         
         try(Connection con= DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(S)) {
              
                st.setString(1,s);
             try(ResultSet r=st.executeQuery()){
                
                while (r.next()) {
                    
                    l.add(setToReq(r));

                }

             }

         } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
         }

         return l;
    }

    @Override
    public List<Requests> findByDate(LocalDate date){
        String s="SELECT * FROM Requests WHERE request_date=?";

         List<Requests> l = new ArrayList<>();
         
         try(Connection con= DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(s)) {
              
                st.setString(1, date.toString());
             try(ResultSet r=st.executeQuery()){
                
                while (r.next()) {
                    
                    l.add(setToReq(r));

                }

             }

         } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
         }

         return l;
    }

    @Override
    public List<Requests> findByDepartmentId(int id){
        
         String s = "SELECT r.* FROM Requests r " +
           "JOIN Works w ON r.user_id = w.user_id " +
           "JOIN Positions p ON w.pos_id = p.id " +
           "WHERE p.dept_id = ?";

         List<Requests> l = new ArrayList<>();
         
         try(Connection con= DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(s)) {
              
                st.setInt(1,id);
             try(ResultSet r=st.executeQuery()){
                
                while (r.next()) {
                    
                    l.add(setToReq(r));

                }

             }

         } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
         }

         return l;
    }

    @Override
    public boolean insert(Requests req){

         String s="INSERT INTO Requests (user_id,type_id,message,status,request_date) VALUES(?,?,?,?,?)";
         String S="SELECT last_insert_rowid()"; // get last assigned id

         try(Connection con=DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(s)) {

                st.setInt(1, req.getUserId());
                st.setInt(2, req.getTypeId());
                st.setString(3, req.getMessage());
                st.setString(4, req.isStatus() ? "solved" : "pending" );
                st.setString(5,req.getDate().toString());
                
                int rowInserted = st.executeUpdate();

                if(rowInserted >0){

                    try(Statement stm = con.createStatement()){

                        ResultSet r = stm.executeQuery(S);

                        if(r.next()){

                            req.setId(r.getInt(1));
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
    public boolean delete(int id){
        String s="DELETE FROM Requests WHERE id= ?";

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
    public boolean update(Requests req){
        String s="UPDATE Requests SET user_id=?,type_id=?,message=?,status=? WHERE id=?";

        try(Connection con= DbHelper.getConnection();

             PreparedStatement st= con.prepareStatement(s)) {
                st.setInt(1,req.getUserId());
                st.setInt(2,req.getTypeId());
                st.setString(3,req.getMessage());
                st.setString(4,req.isStatus() ? "solved" : "pending");
                st.setInt(5,req.getId());

            return st.executeUpdate()>0;

        } catch (SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }

        return false;
    }
}

