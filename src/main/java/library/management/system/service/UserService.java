package library.management.system.service;
        
import library.management.system.dao.UserDAO;
import library.management.system.model.User;

public class UserService {
        
    private UserDAO userDAO = new UserDAO();
    
    public User login(String username, String password, String selectedRole) {
        
        User user = userDAO.findByUsernameAndRole(username, selectedRole);
        
        if (user == null) {
            throw new RuntimeException("No " + selectedRole + " account found with this username");
        } 
        
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password");
        }
        
        return user;
    }
    
    // add user
    public void addUser(String name, String username, String password, String role) {
        
        userDAO.addUser(name, username, password, role);
    }
    
    // deactivate user
    public void deactivateUser(int userId) {
        
        userDAO.deactivateUser(userId);
    }
}
