package library.management.system.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import library.management.system.model.User;
import library.management.system.model.Admin;
import library.management.system.model.Librarian;
import library.management.system.model.Student;
import library.management.system.util.DBConnection;
import library.management.system.dto.UserTableDTO;

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
    
    // return list of all users
    public List<UserTableDTO> getActiveUsers() {
        
        List<UserTableDTO> users = new ArrayList<>();
        
        String sql = "SELECT user_id, name, username, role, is_active FROM Users WHERE is_active = TRUE";
        
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                UserTableDTO user = new UserTableDTO(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getBoolean("is_active")
                );
                
                users.add(user);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load active users");
        }
         
        return users;
    }
    
    // search for users based on keyword
    public List<UserTableDTO> searchUsers(String keyword) {
        
        List<UserTableDTO> users = new ArrayList<>();
        
        String sql = """
           SELECT user_id, name, username, role, is_active 
           FROM Users 
           WHERE is_active = TRUE 
           AND (
                CAST(user_id AS CHAR) LIKE ?
                OR name LIKE ?
                OR username LIKE ?
                OR role LIKE ?
            )
        """;
        
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            String search = "%" + keyword + "%";
            
            // Extract numbers and strip leading zeros (e.g., "002" -> "2")
            String digits = keyword.replaceAll("[^0-9]", "").replaceFirst("^0+", "");
            
            // If no numbers are present, use a placeholder so it doesn't bypass other OR checks with '%%'
            String idSearch = digits.isEmpty() ? "%no_numeric_id_provided%" : "%" + digits + "%";
            
            stmt.setString(1, idSearch);
            stmt.setString(2, search);
            stmt.setString(3, search);
            stmt.setString(4, search);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                UserTableDTO user = new UserTableDTO(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getBoolean("is_active")
                );
                
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search users");
        }
        
        return users;
    }
    
    // return all inactive users
    public List<UserTableDTO> getInactiveUsers() {
        
        List<UserTableDTO> users = new ArrayList<>();
        
        String sql = "SELECT user_id, name, username, role, is_active FROM Users WHERE is_active = FALSE";
        
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                UserTableDTO user = new UserTableDTO(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getBoolean("is_active")
                );
                
                users.add(user);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load inactive users");
        }
         
        return users;
    }
}
