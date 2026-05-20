package library.management.system.service;

import library.management.system.dao.BookDAO;
import library.management.system.dao.TransactionDAO;
import library.management.system.model.Book;
import library.management.system.dto.*;

import java.util.List;
import java.util.ArrayList;

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
        return bookDAO.addBook(book);
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

    // librarian catalog table
    public List<BookCatalogDTO> getBookCatalog() {
        return bookDAO.getBookCatalog();
    }
    
    // search librarian catalog
    public List<BookCatalogDTO> searchBookCatalog(String keyword) {
  
        keyword = keyword.trim();
        
        // books are searched based on status
        if (keyword.equalsIgnoreCase("available") || keyword.equalsIgnoreCase("unavailable")) {

            List<BookCatalogDTO> allBooks = bookDAO.getBookCatalog();
            List<BookCatalogDTO> filteredBooks = new ArrayList<>();
            
            // get all books then filter on availability status
            for (BookCatalogDTO book : allBooks) {                       
                if (book.getStatus().equalsIgnoreCase(keyword)) {
                    filteredBooks.add(book);
                }
            }

            return filteredBooks;
        }
        // if book is searched based on title or author
        return bookDAO.searchBookCatalog(keyword);
    }
    
    // Return books for student book catalog
    public List<StudentBookCatalogDTO> getStudentBookCatalog() {
        return bookDAO.getStudentBookCatalog();
    }
    
    public List<StudentBookCatalogDTO> searchStudentBookCatalog(String keyword) {

        keyword = keyword.trim();

        // availability filtering
        if (keyword.equalsIgnoreCase("available") || keyword.equalsIgnoreCase("unavailable")) {

            List<StudentBookCatalogDTO> books = bookDAO.getStudentBookCatalog();

            List<StudentBookCatalogDTO> filteredBooks = new ArrayList<>();

            for (StudentBookCatalogDTO book : books) {

                if (book.getAvailability().equalsIgnoreCase(keyword)) {
                    filteredBooks.add(book);
                }
            }

            return filteredBooks;
        }

        // if searched by title, author or category
        return bookDAO.searchStudentBookCatalog(keyword);
    }
}
