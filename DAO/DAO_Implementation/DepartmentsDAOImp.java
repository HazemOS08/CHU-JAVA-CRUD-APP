package DAO.DAO_Implementation;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.DepartmentsDAO;
import Table_Classes.Departments;
import db.DbHelper;

public class DepartmentsDAOImp implements DepartmentsDAO{
      
      private Departments setToDept(ResultSet r) throws SQLException{

        return new Departments(
            r.getInt("id"),
            r.getString("name")
        );

      };
      
      @Override
      public Departments findById(int id){
          
        String s="SELECT * FROM Departments WHERE id=?" ;
        
        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
            
                st.setInt(1,id);

                try(ResultSet r= st.executeQuery()){
                   
                    if(r.next()){

                        return setToDept(r);

                    }
                }
            
        } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }

        return null;
      }

      @Override
      public Departments findByName(String name){

        String s="SELECT * FROM Departments WHERE name=?" ;
        
        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
            
                st.setString(1,name);

                try(ResultSet r= st.executeQuery()){
                   
                    if(r.next()){

                        return setToDept(r);
                        
                    }
                }
            
        } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }

        return null;
      }

      @Override
      public List<Departments> findAll(){

         String s="SELECT * FROM Departments";
         List<Departments> l = new ArrayList<>();

         try(Connection con= DbHelper.getConnection();
              Statement st= con.createStatement()) {
            
             try(ResultSet r=st.executeQuery(s)){
                
                while (r.next()) {
                    
                    l.add(setToDept(r));

                }

             }

         } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
         }

         return l;
      }

      @Override
      public boolean insert(Departments depart){

         String s="INSERT INTO Departments (name) VALUES(?)";
         String S="SELECT last_insert_rowid()"; // get last assigned id

         try(Connection con=DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(s)) {
            
                st.setString(1,depart.getName());
                int rowInserted = st.executeUpdate();

                if(rowInserted >0){

                    try(Statement stm = con.createStatement()){

                        ResultSet r = stm.executeQuery(S);

                        if(r.next()){

                            depart.setId(r.getInt(1));
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
        String s="DELETE FROM Departments WHERE id= ?";

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
      public boolean update(Departments depart){

        String s="UPDATE Departments SET Name=? WHERE id=?";

        try(Connection con= DbHelper.getConnection();

             PreparedStatement st= con.prepareStatement(s)) {
                
                st.setString(1,depart.getName());
                st.setInt(2,depart.getId());

            return st.executeUpdate()>0;

        } catch (SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }

        return false;
      }
}
