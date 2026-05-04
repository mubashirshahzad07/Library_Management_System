package library.management.system.model;

public class Admin extends User {
    
    public Admin(int userId, String name, String username, String password) {
        super(userId, name, username, password, "ADMIN");
    }
}
