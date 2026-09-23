package DAO.DAO_Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DAO.WorksDAO;
import Table_Classes.Works;
import db.DbHelper;

public class WorksDAOImp implements WorksDAO{

    private Works setToWorks(ResultSet r) throws SQLException{
        return new Works( 
           r.getInt("user_id") , r.getInt("pos_id")
         );
    }

    public List<Works> findByUserId(int userId){

       String s="SELECT * FROM Works WHERE user_id= ? ORDER BY user_id";
       List<Works> l = new ArrayList<>();

       try(Connection con = DbHelper.getConnection();
         PreparedStatement st = con.prepareStatement(s) ){
          

          st.setInt(1,userId);

            try (ResultSet r = st.executeQuery()){
                
                while(r.next()){

                    l.add(setToWorks(r));

                }
            

       }
    }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
    return l;


    }
    public List<Works> findByPosId(int posId){
       String s="SELECT * FROM Works WHERE pos_id= ? ORDER BY pos_id";
       List<Works> l = new ArrayList<>();

       try(Connection con = DbHelper.getConnection();
         PreparedStatement st = con.prepareStatement(s) ){
          

          st.setInt(1,posId);

            try (ResultSet r = st.executeQuery()){
                
                while(r.next()){

                    l.add(setToWorks(r));

                }
            

       }
    }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
    return l;

    }
   
    public List<Works> findAll(){

        String s="SELECT * FROM Works ORDER BY pos_id";
        List<Works> l= new ArrayList<>();

        try (Connection con= DbHelper.getConnection();
              Statement st= con.createStatement()){
            
                try(ResultSet r= st.executeQuery(s)){
                   
                    while(r.next()){
                        l.add(setToWorks(r));
                    }
                }
        } catch (SQLException e) {

            System.out.println("Error: "+e.getMessage());
           
        }

        return l;
    }
    
    public Works findByUserIdPosId(int userId,int posId){

       String s="SELECT * FROM Works WHERE user_id= ? AND pos_id=? ";

       Works l=null ;

       try(Connection con = DbHelper.getConnection();
         PreparedStatement st = con.prepareStatement(s) ){
          

          st.setInt(1,userId);
          st.setInt(2,posId);

            try (ResultSet r = st.executeQuery()){
                
                while(r.next()){

                    l=setToWorks(r);

                }
            

       }
     }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
     return l;
        
    }


    public List<Works> findByDepartmentId(int id){
        
        List<Works> l = new ArrayList<>();
        String s="SELECT * FROM Works Where pos_id IN ("+
         "SELECT id FROM Positions Where dept_id=?)";

        try( Connection con= DbHelper.getConnection() ;
             PreparedStatement st= con.prepareStatement(s))
        {
           st.setInt(1,id);

           try(ResultSet r = st.executeQuery()){

            while(r.next()){

                l.add(setToWorks(r));

            }

           }

        } catch (SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }

        return l;
    }

    public boolean insert(Works work){
        
        String s="INSERT INTO Works (user_id, pos_id) VALUES (?,?)";

        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
             
                st.setInt(1,work.getUserId());
                st.setInt(2,work.getPosId());

                return st.executeUpdate()>0;

        } catch (SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }
        
        return false;
        
    }
    public boolean delete(Works work){
        
        String s="DELETE FROM Works WHERE user_id=? AND pos_id=?";

        
        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
             
                st.setInt(1,work.getUserId());
                st.setInt(2,work.getPosId());

                return st.executeUpdate()>0;

        } catch (SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }
        
        return false;

    }
}
