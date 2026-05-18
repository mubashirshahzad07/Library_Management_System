package library.management.system.dto;

import java.util.Date;

public class IssuedBookDTO {

    private String username;
    private String bookTitle;
    private Date dueDate;
    private String status;

    public IssuedBookDTO(String username, String bookTitle, Date dueDate, String dbStatus) {
        this.username = username;
        this.bookTitle = bookTitle;
        this.dueDate = dueDate;

        if (dbStatus.equalsIgnoreCase("ISSUED") && dueDate.before(new Date())) {
            this.status = "Overdue";
        } else if (dbStatus.equalsIgnoreCase("ISSUED")) {
            this.status = "Issued";
        } else {
            this.status = dbStatus;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }
}