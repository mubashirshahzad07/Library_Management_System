package library.management.system.model;

public class Student extends User {
    
    public Student(int userId, String name, String username, String password) {
        super(userId, name, username, password, "STUDENT");
    }
}
