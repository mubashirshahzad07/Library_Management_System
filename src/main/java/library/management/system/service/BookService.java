package library.management.system.service;

import library.management.system.dao.BookDAO;
import library.management.system.dao.TransactionDAO;
import library.management.system.model.Book;
import library.management.system.dto.BookTableDTO;

import java.util.List;

public class BookService {

    private final BookDAO        bookDAO;
    private final TransactionDAO transactionDAO;

    public BookService() {
        this.bookDAO        = new BookDAO();
        this.transactionDAO = new TransactionDAO();
    }

    // ── Add a new book ────────────────────────────────────────────────────────
    public boolean addBook(Book book) {

        // Copies must be > 0
        if (book.getTotalCopies() < 1) {
            throw new RuntimeException("Total copies must be at least 1.");
        }

        // Duplicate check — same title + author already exists?
        Book existing = bookDAO.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (existing != null) {
            throw new RuntimeException("Book \"" + book.getTitle()
                    + "\" by " + book.getAuthor() + " already exists.");
        }

        // available_copies must equal total_copies on a new book
        book.setAvailableCopies(book.getTotalCopies());
        return bookDAO.insertBook(book);
    }

    // ── Soft-delete a book (set is_active = false) ────────────────────────────
    //    Cannot deactivate a book if any copies are currently issued,
    //    because that would orphan active transactions.
    public boolean removeBook(int bookId) {

        // Verify book exists
        Book book = bookDAO.findBookById(bookId);
        if (book == null) {
            throw new RuntimeException("Book with ID " + bookId + " not found.");
        }

        // Block if any copy of this book is still issued to a student
        if (transactionDAO.isBookCurrentlyIssued(bookId)) {
            throw new RuntimeException(
                "Cannot remove \"" + book.getTitle() + "\": "
                + "one or more copies are currently issued. "
                + "Please wait until all copies are returned.");
        }

        return bookDAO.setBookActive(bookId, false);
    }

    // ── Restore a previously removed book (set is_active = true) ─────────────
    public boolean restoreBook(int bookId) {

        Book book = bookDAO.findBookById(bookId);
        if (book == null) {
            throw new RuntimeException("Book with ID " + bookId + " not found.");
        }

        return bookDAO.setBookActive(bookId, true);
    }

    // ── Get all active books ──────────────────────────────────────────────────
    public List<Book> getAllBooks() {

        List<Book> books = bookDAO.getAllBooks();

        if (books.isEmpty()) {
            throw new RuntimeException("No books found in the library.");
        }

        return books;
    }

    // ── Search books by keyword (title / author / category) ──────────────────
public List<BookTableDTO> searchBooks(String keyword) {

    List<BookTableDTO> results = bookDAO.searchBooks(keyword.trim());

    if (results.isEmpty()) {
        throw new RuntimeException(
            "No books found matching \"" + keyword.trim() + "\".");
    }

    return results;
}
}
