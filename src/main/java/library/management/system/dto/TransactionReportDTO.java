package library.management.system.dto;

import java.util.Date;

public class TransactionReportDTO {

    private int transactionId;
    private String username;
    private String bookTitle;
    private Date issueDate;
    private Date dueDate;
    private String status;

    public TransactionReportDTO(int transactionId, String username, String bookTitle,
                                Date issueDate, Date dueDate, String dbStatus) {
        this.transactionId = transactionId;
        this.username = username;
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = calculateDisplayStatus(dbStatus, dueDate);
    }

    private String calculateDisplayStatus(String dbStatus, Date dueDate) {
        Date today = new Date();

        if (dbStatus.equalsIgnoreCase("RETURNED")) {
            return "Returned";
        }

        if (dbStatus.equalsIgnoreCase("ISSUED") && dueDate.before(today)) {
            return "Overdue";
        }

        return "Issued";
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getUsername() {
        return username;
    }

    public String getBookTitle() {
        return bookTitle;
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