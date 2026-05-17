package library.management.system.dto;

public class UserTableDTO {
    
    private int userId;
    private String name;
    private String username;
    private String role;
    private String status;
    
    public UserTableDTO(int userId, String name, String username, String role, boolean is_active) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.role = role;
        this.status = is_active ? "Active" : "Inactive";
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getRole() {
        return role;
    }
    
    public String getStatus() {
        return status;
    }
}