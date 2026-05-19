package library.management.system.dto;

import java.util.Date;

public class FineReportDTO {

    private int transactionId;
    private String username;
    private String bookTitle;
    private Date dueDate;
    private Date returnDate;
    private double fineAmount;
    private String paymentStatus;

    public FineReportDTO(int transactionId, String username, String bookTitle,
                         Date dueDate, Date returnDate, double fineAmount, String paymentStatus) {
        this.transactionId = transactionId;
        this.username = username;
        this.bookTitle = bookTitle;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.paymentStatus = paymentStatus;
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
    
    public Date getDueDate() {
        return dueDate;
    } 
    
    public Date getReturnDate() { 
        return returnDate; 
    }
    
    public double getFineAmount() { 
        return fineAmount; 
    }
    
    public String getPaymentStatus() { 
        return paymentStatus; 
    }
}