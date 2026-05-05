
import java.util.Date;

public class Transaction {

    private int transactionId;
    private int userId;
    private int bookId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private double fineAmount;
    private String status;
    private boolean finePaid;

    
    public Transaction(int transactionId, int userId, int bookId,
                       Date issueDate, Date dueDate,
                       Date returnDate, double fineAmount, String status) {

        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = status;
    }

    
    public int getTransactionId() {
        return transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public String getStatus() {
        return status;
    }

      public boolean finePaid() {
        return finePaid;
    }



    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}