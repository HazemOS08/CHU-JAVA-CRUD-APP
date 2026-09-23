package db;
import java.io.IOException;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DbHelper {

    private static final String DB_URL = "jdbc:sqlite:db/hospital.db";


     //establishes a database connection 

    public static Connection getConnection() throws SQLException {

        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) {

            // enforece foreigh key each time
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }



     //executes a raw SQL update string 
     
    public static int update(String sql) throws SQLException {
        try (Connection conn = getConnection();

             Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql); // returns how many rows were updated
        }
    }

    //execute .sql files

    public static void executeScriptFile(String filePath) throws IOException, SQLException {

        String script;

        try (FileInputStream f = new FileInputStream(filePath)) {
        script = new String(f.readAllBytes()); // reading the .sql files into a string
        }

        try (Connection conn = getConnection();

             Statement stmt = conn.createStatement()) {
        
            // Execute the entire script
            stmt.executeUpdate(script);
        }
    }

}
    
