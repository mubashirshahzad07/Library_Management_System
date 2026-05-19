package library.management.system.dto;

import java.util.Date;

public class StudentBorrowedBookDTO {

    private String title;
    private String author;
    private Date issueDate;
    private Date dueDate;
    private String status;

    public StudentBorrowedBookDTO(String title, String author, Date issueDate, Date dueDate) {
        this.title = title;
        this.author = author;
        this.issueDate = issueDate;
        this.dueDate = dueDate;

        if (dueDate.before(new Date())) {
            this.status = "Overdue";
        } else {
            this.status = "Issued";
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }
}