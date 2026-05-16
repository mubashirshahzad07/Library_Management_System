package library.management.system.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import library.management.system.model.User;
import library.management.system.model.Admin;
import library.management.system.model.Librarian;
import library.management.system.model.Student;
import library.management.system.util.DBConnection;

public class UserDAO {
    
    public User findByUsernameAndRole(String username, String role) {
        
        String sql = "SELECT * FROM Users WHERE username = ? AND role = ? AND is_active = TRUE" ;
        
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, username);
            stmt.setString(2, role);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String dbUsername = rs.getString("username");
                String password = rs.getString("password");
                String dbRole = rs.getString("role");
                
                if (dbRole.equalsIgnoreCase("ADMIN")) {
                    return new Admin(userId, name, dbUsername, password);
                } else if (dbRole.equalsIgnoreCase("LIBRARIAN")) {
                    return new Librarian(userId, name, dbUsername, password);
                } else if (dbRole.equalsIgnoreCase("STUDENT")) {
                    return new Student(userId, name, dbUsername, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // add new user
    public void addUser(String name, String username, String password, String role) {
        
        String sql = "INSERT INTO Users (name, username, password, role) VALUES (?, ?, ?, ?)";
        
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Username already exists for this role");
        }
    }
    
    // deactivate existing user
    public void deactivateUser(int userId) {
        
        String sql = "UPDATE Users SET is_active = FALSE WHERE user_id = ?";
        
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to deactivate user");
        }
    }
}
