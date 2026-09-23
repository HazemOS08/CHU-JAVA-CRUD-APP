package DAO.DAO_Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DAO.PositionsDAO;
import Table_Classes.Positions;
import db.DbHelper;

public class PositionsDAOImp implements PositionsDAO{

    private Positions setToPos(ResultSet r) throws SQLException{

        return new Positions(r.getInt("id"),
                            r.getInt("dept_id"),
                            r.getString("name"));

    }
    @Override
    public Positions findById(int id){

        String s="SELECT * FROM Positions WHERE id=?" ;
        
        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
            
                st.setInt(1,id);

                try(ResultSet r= st.executeQuery()){
                   
                    if(r.next()){

                        return setToPos(r);

                    }
                }
            
        } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }

        return null;
    }

    @Override
    public List<Positions> findByDeptId(int id){

        String s="SELECT * FROM Positions WHERE dept_id=?" ;
        List<Positions> l = new ArrayList<>();

        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
            
                st.setInt(1,id);

                try(ResultSet r= st.executeQuery()){
                   
                    while(r.next()){

                       l.add(setToPos(r));

                    }
                }
            
        } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }

        return l;
    }

    @Override
    public List<Positions> findByName(String name){

        String s="SELECT * FROM Positions WHERE name=?" ;
        List<Positions> l = new ArrayList<>();

        try(Connection con = DbHelper.getConnection();
             PreparedStatement st= con.prepareStatement(s)) {
            
                st.setString(1,name);

                try(ResultSet r= st.executeQuery()){
                   
                    while(r.next()){

                       l.add(setToPos(r));

                    }
                }
            
        } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }

        return l;
    }
    @Override
    public List<Positions> findAll(){
        String s="SELECT * FROM Positions" ;
        List<Positions> l = new ArrayList<>();

        try(Connection con = DbHelper.getConnection();
             Statement st= con.createStatement()){
            
                

                try(ResultSet r= st.executeQuery(s)){
                   
                    while(r.next()){

                       l.add(setToPos(r));

                    }
                }
            
        } catch (SQLException e) {
            System.out.println("Error : "+ e.getMessage());
        }

        return l;
    }
    
    @Override
    public boolean insert(Positions pos){

        String s="INSERT INTO Positions (dept_id,name) VALUES(?,?)";
         String S="SELECT last_insert_rowid()"; // get last assigned id

         try(Connection con=DbHelper.getConnection();
              PreparedStatement st= con.prepareStatement(s)) {
            
                st.setInt(1,pos.getDeptId());
                st.setString(2, pos.getName());

                int rowInserted = st.executeUpdate();

                if(rowInserted >0){

                    try(Statement stm = con.createStatement()){

                        ResultSet r = stm.executeQuery(S);

                        if(r.next()){

                            pos.setId(r.getInt(1));
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
        String s="DELETE FROM Positions WHERE id= ?";

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
    public boolean update(Positions pos){
        String s="UPDATE Positions SET dept_id=? , name=? WHERE id=?";

        try(Connection con= DbHelper.getConnection();

             PreparedStatement st= con.prepareStatement(s)) {
                st.setInt(1,pos.getDeptId());
                st.setString(2,pos.getName());
                st.setInt(3,pos.getId());
                

            return st.executeUpdate()>0;

        } catch (SQLException e) {
            System.out.println("Error : "+e.getMessage());
        }

        return false;
    }

}
