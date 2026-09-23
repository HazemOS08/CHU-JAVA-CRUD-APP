package DAO.DAO_Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.RequestTypesDAO;
import Table_Classes.RequestTypes;
import db.DbHelper;

public class RequestTypesDAOImp implements RequestTypesDAO{
    

    private RequestTypes setToReqType(ResultSet r)throws SQLException{

        return new RequestTypes(
            r.getInt("id"),
            r.getString("type")
            );

    }
    
    @Override
    public RequestTypes findById(int id){

       String s="SELECT * FROM RequestTypes WHERE id= ?";

       try(Connection con = DbHelper.getConnection();
         PreparedStatement st = con.prepareStatement(s) ){
          

          st.setInt(1,id);

            try (ResultSet r = st.executeQuery()){
                
                if(r.next()){

                    return setToReqType(r);
                }
            

       }
    }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
    return null;
    }
    
    @Override
    public RequestTypes findByType(String type){
       String s="SELECT * FROM RequestTypes WHERE type= ?";

       try(Connection con = DbHelper.getConnection();
         PreparedStatement st = con.prepareStatement(s) ){
          

          st.setString(1,type);

            try (ResultSet r = st.executeQuery()){
                
                if(r.next()){

                    return setToReqType(r);
                }
            

       }
    }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
    return null;
    }

    @Override
    public List<RequestTypes> findAll(){

       String s="SELECT * FROM RequestTypes";
       List<RequestTypes> l= new ArrayList<>();

       try(Connection con = DbHelper.getConnection();
         Statement st = con.createStatement() ){

            try (ResultSet r = st.executeQuery(s)){
                
                while(r.next()){

                    l.add(setToReqType(r));
                }

            }
    }catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        } 
    
    return l;
    }

    @Override
    public boolean insert(RequestTypes req){

        String s="INSERT INTO RequestTypes (type) VALUES(?)";
        String S = "SELECT last_insert_rowid()"; // get last assigned id


        try(Connection con= DbHelper.getConnection();
            PreparedStatement st=con.prepareStatement(s)) {
            
            st.setString(1, req.getType());
                       int rowInserted = st.executeUpdate();

            if(rowInserted>0){// success

               // get the SQL generated id and assign it back to the user
               try (Statement stm = con.createStatement();

                     ResultSet r = stm.executeQuery(S)  ) {

                if (r.next()) {

                    req.setId(r.getInt(1));
                }
            }

            return true;


        } 

        }catch (SQLException e) {

            System.out.println("Error : "+e.getMessage());    
        }
        return false;
    }

    @Override
    public boolean update(RequestTypes req){

        String s="UPDATE RequestTypes SET type=? WHERE id=?";
        try(Connection con= DbHelper.getConnection();

             PreparedStatement st= con.prepareStatement(s)) {
                
                st.setString(1,req.getType());
                st.setInt(2,req.getId());

            return st.executeUpdate()>0;

        } catch (SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }

        return false;
    }

    @Override
    public boolean delete(int id){
        String s="DELETE FROM RequestTypes WHERE id= ?";

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
