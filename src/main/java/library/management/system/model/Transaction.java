package library.management.system.model;

import java.util.Date;

public class Transaction {

    private int transactionId;
    private int userId;
    private int bookId;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private String status;

    // ── Full constructor  ──────────────────────────
    public Transaction(int transactionId, int userId, int bookId,
                       Date issueDate, Date dueDate,
                       Date returnDate, String status) {
        this.transactionId = transactionId;
        this.userId        = userId;
        this.bookId        = bookId;
        this.issueDate     = issueDate;
        this.dueDate       = dueDate;
        this.returnDate    = returnDate;
        this.status        = status;
    }

    // ── No-ID constructor (used when issuing — DB auto-generates the ID) ──────
    public Transaction(int userId, int bookId,
                       Date issueDate, Date dueDate,
                       Date returnDate, String status) {
        this.transactionId = 0;   // placeholder; DB assigns the real ID
        this.userId        = userId;
        this.bookId        = bookId;
        this.issueDate     = issueDate;
        this.dueDate       = dueDate;
        this.returnDate    = returnDate;
        this.status        = status;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getTransactionId() { return transactionId; }
    public int    getUserId()        { return userId;        }
    public int    getBookId()        { return bookId;        }
    public Date   getIssueDate()     { return issueDate;     }
    public Date   getDueDate()       { return dueDate;       }
    public Date   getReturnDate()    { return returnDate;    }
    public String getStatus()        { return status;        }

    // ── Setters (only mutable fields need them) ───────────────────────────────
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    public void setStatus(String status)       { this.status     = status;     }
}