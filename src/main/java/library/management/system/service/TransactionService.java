package library.management.system.service;
 
import library.management.system.dao.TransactionDAO;
import library.management.system.dao.BookDAO;
import library.management.system.dao.UserDAO;
import library.management.system.model.Transaction;
import library.management.system.model.User;
import library.management.system.model.Book;
import library.management.system.dto.*;
 
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TransactionService {
 
    private final TransactionDAO transactionDAO;
    private final BookDAO        bookDAO;
    private final UserDAO        userDAO;
    private FineService fineService = new FineService();
 
    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.bookDAO        = new BookDAO();
        this.userDAO        = new UserDAO();
    }
 
    // ── Issue a book to a student ─────────────────────────────────────────────
    // bookQuery can be either a title or an ISBN 
    public boolean issueBook(String username, String bookQuery) {
 
        // Student exists and is active?
        User student = userDAO.findByUsernameAndRole(username, "STUDENT");
        if (student == null) {
            throw new RuntimeException("No active student found with username \""
                    + username + "\".");
        }
 
        int userId = student.getUserId();
 
        // Student blocked due to overdue book
        if (transactionDAO.hasOverdueBooks(userId)) {
            throw new RuntimeException("Student is blocked due to an overdue book. "
                    + "Please return the overdue book first.");
        }
 
        // Look up book by title or ISBN in one call
        Book book = bookDAO.findByTitleOrIsbn(bookQuery);
        if (book == null) {
            throw new RuntimeException("No book found matching \""
                    + bookQuery + "\". Please check the title or ISBN.");
        }
 
        // available_copies > 0
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available for \""
                    + book.getTitle() + "\" at the moment.");
        }
 
        // Build transaction — DB assigns the ID
        Date issueDate = new Date();
        Date dueDate   = new Date(issueDate.getTime() + TimeUnit.DAYS.toMillis(14));
 
        Transaction transaction = new Transaction(
            0, userId, book.getBookId(),
            issueDate, dueDate,
            null,    // return_date: null until returned
            "ISSUED"
        );
 
        // DAO inserts the row — DB generates and stores the transaction_id
        boolean saved = transactionDAO.issueBook(transaction);
        if (!saved) {
            throw new RuntimeException("Failed to issue book. Please try again.");
        }
 
        // Reduce available_copies by 1
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDAO.updateBook(book);
 
        return true;
    }
 
    // ── Return a book ─────────────────────────────────────────────────────────
    public boolean returnBook(String bookTitleOrIsbn, String memberUsername) {

        int updated = transactionDAO.returnBook(bookTitleOrIsbn, memberUsername);
        if (updated == -1) {
            throw new RuntimeException("No active issued transaction found for the given book and member.");
        }
        
        // create fine is returned late
        Transaction transaction = transactionDAO.findTransactionById(updated);
        fineService.createFine(transaction);

    // Find the book to increment available_copies
    Book book = bookDAO.findByTitleOrIsbn(bookTitleOrIsbn);
    if (book != null) {
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookDAO.updateBook(book);
    }

    return true;
}

    // ── Get student block status (for Admin / Librarian view) ─────────────────
    public String getStudentStatus(int userId) {
 
        List<Transaction> overdue = transactionDAO.getOverdueTransactionsByUser(userId);
 
        if (overdue.isEmpty()) {
            return "CLEAR — No overdue books.";
        }
 
        StringBuilder status = new StringBuilder("BLOCKED — Overdue books:\n");
        for (Transaction t : overdue) {
            status.append("  → Book ID : ").append(t.getBookId()).append("\n")
                  .append("    Due Date : ").append(t.getDueDate()).append("\n");
        }
        return status.toString();
    }
    
    // get all transactions
    public List<TransactionReportDTO> getTransactionReports() {
        return transactionDAO.getTransactionReports();
    }
    
    // search transactions
    public List<TransactionReportDTO> searchTransactionReports(String keyword) {
        return transactionDAO.searchTransactionReports(keyword);
    }
    
    // librarian dashboard issued books
    public List<IssuedBookDTO> getIssuedBooks() {
        return transactionDAO.getIssuedBooks();
    }
    
    // get student borrowed books
    public List<StudentBorrowedBookDTO> getStudentBorrowedBooks(int userId) {
        return transactionDAO.getStudentBorrowedBooks(userId);
    }
    
    // student history card
    public List<StudentHistoryDTO> getStudentHistory(int userId) {
        return transactionDAO.getStudentHistory(userId);
    }
}
 