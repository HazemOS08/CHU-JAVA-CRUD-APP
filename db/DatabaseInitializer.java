package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
/* 
  
  NOTE: This main class was generated with AI assistance (Gemini) strictly 
  for executing the content of the file Create.sql
 
 */
public class DatabaseInitializer {
    
    private static final String DB_DIR = "db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + "/hospital.db";
    
    public static void initializeDatabase() {
        // Connection is automatically opened and closed here via try-with-resources
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            
            // Enable foreign key constraints in SQLite
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            // Run the schema creation script 
            // (Make sure Create.sql is in your root project folder, or adjust path if nested)
            executeSqlScript(conn, "Create.sql");
            executeSqlScript(conn, "Insert.sql");

        } catch (Exception e) {
            System.err.println("[ERROR] Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void executeSqlScript(Connection conn, String resourcePath) {
     try {
        // Read the file from the classpath/package
        java.io.InputStream is = DatabaseInitializer.class.getResourceAsStream(resourcePath);
        if (is == null) {
            throw new java.io.FileNotFoundException("Resource not found: " + resourcePath);
        }
        
        String scriptContent = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        String[] statements = scriptContent.split(";");

        try (Statement stmt = conn.createStatement()) {
            for (String rawSql : statements) {
                String sql = rawSql.trim();
                if (!sql.isEmpty() && !sql.startsWith("--")) {
                    stmt.execute(sql);
                }
            }
        }
        System.out.println("[SUCCESS] Successfully executed script: " + resourcePath);

    } catch (Exception e) {
        System.err.println("[ERROR] Failed to execute SQL script (" + resourcePath + "): " + e.getMessage());
        e.printStackTrace();
    }

   }
}



