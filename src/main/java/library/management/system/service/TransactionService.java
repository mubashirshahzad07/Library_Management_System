package library.management.system.service;

import library.management.system.dao.TransactionDAO;
import library.management.system.model.Transaction;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TransactionService {

    private final TransactionDAO transactionDAO;
    private final BookDAO        bookDAO;
    private final UserDAO        userDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
        this.bookDAO        = new BookDAO();
        this.userDAO        = new UserDAO();
    }

    // ── Issue a book to a user ────────────────────────────────────────────────
    public boolean issueBook(int userId, int bookId) {

        //  Student exists?
        User student = userDAO.findUserById(userId);
        if (student == null) {
            throw new RuntimeException("Student with ID " + userId + " not found.");
        }

        //  Student account active? (is_active = TRUE in Users table)
        if (!transactionDAO.isUserActive(userId)) {
            throw new RuntimeException("Student account is inactive. "
                    + "Please contact an administrator.");
        }

        //  Student blocked due to overdue book?
        if (transactionDAO.hasOverdueBooks(userId)) {
            throw new RuntimeException("Student is blocked due to an overdue book. "
                    + "Please return the overdue book first.");
        }

        //  Book exists?
        Book book = bookDAO.findBookById(bookId);
        if (book == null) {
            throw new RuntimeException("Book with ID " + bookId + " not found.");
        }

        //  available_copies > 0?
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available for \""
                    + book.getTitle() + "\" at the moment.");
        }

        // Build the transaction — due 14 days from today
        Date issueDate = new Date();
        Date dueDate   = new Date(issueDate.getTime() + TimeUnit.DAYS.toMillis(14));
        int  newId     = generateTransactionId();

        Transaction transaction = new Transaction(
            newId, userId, bookId,
            issueDate, dueDate,
            null,    // return_date: null until returned
            0.0,     // fine_amount: not applicable
            "ISSUED" // status
        );

        // Persist transaction row
        boolean saved = transactionDAO.issueBook(transaction);
        if (!saved) {
            throw new RuntimeException("Failed to issue book. Please try again.");
        }

        //  Reduce available_copies by 1
        book.issueBook();
        bookDAO.updateBook(book);

        return true;
    }

    // ── Return a book ─────────────────────────────────────────────────────────
    public boolean returnBook(int transactionId) {

        //  Transaction exists?
        Transaction transaction = transactionDAO.findTransactionById(transactionId);
        if (transaction == null) {
            throw new RuntimeException("Transaction with ID " + transactionId + " not found.");
        }

        //  Book was actually issued (not already returned)?
        if (!"ISSUED".equalsIgnoreCase(transaction.getStatus())) {
            throw new RuntimeException("This book has already been returned.");
        }

        // Update transaction row
        Date returnDate = new Date();
        boolean updated = transactionDAO.returnBook(transactionId, returnDate);
        if (!updated) {
            throw new RuntimeException("Failed to process return. Please try again.");
        }

        //  Increase available_copies by 1
        Book book = bookDAO.findBookById(transaction.getBookId());
        if (book != null) {
            book.returnBook();
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

    // ── Generate a unique transaction ID ──────────────────────────────────────
    private int generateTransactionId() {
        return (int)(System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}
