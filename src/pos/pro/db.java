package pos.pro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db {
   
    public static Connection mycon(){
        Connection con = null;
        try {
            // 1. Updated to the newer MySQL driver (com.mysql.cj.jdbc.Driver)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Updated URL with your port and database name (cmartdb)
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/cmartdb", "root", "78563");
            return con;
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database Connection Error: " + e);
            return null;
        }
    }   
}