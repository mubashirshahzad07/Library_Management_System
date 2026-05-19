package library.management.system.dto;

import java.util.Date;

public class StudentHistoryDTO {

    private String bookTitle;
    private Date issueDate;
    private Date returnDate;
    private double fine;

    public StudentHistoryDTO(String bookTitle, Date issueDate, Date returnDate, double fine) {
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
        this.fine = fine;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public double getFine() {
        return fine;
    }
}