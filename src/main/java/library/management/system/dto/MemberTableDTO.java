package library.management.system.dto;

public class MemberTableDTO {

    private String name;
    private String username;
    private int issuedBooks;
    private String status;

    public MemberTableDTO(String name, String username, int issuedBooks, String status) {
        this.name = name;
        this.username = username;
        this.issuedBooks = issuedBooks;
        this.status = status;
    }

    public String getName() { 
        return name; 
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public int getIssuedBooks() { 
        return issuedBooks; 
    }
    
    public String getStatus() { 
        return status; 
    }
}