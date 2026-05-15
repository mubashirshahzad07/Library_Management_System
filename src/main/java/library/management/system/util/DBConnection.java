package library.management.system.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/LibraryManagement";
    private static final String USER = "root"; 
    private static final String PASSWORD = "admin123"; 

    private static Connection connection = null;

    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connection Successful!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
        return connection;
    }
}
