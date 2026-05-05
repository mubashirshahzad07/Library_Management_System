package library.management.system.model;

public class Librarian extends User {
   
    public Librarian(int userId, String name, String username, String password) {
        super(userId, name, username, password, "LIBRARIAN");
    }
}
