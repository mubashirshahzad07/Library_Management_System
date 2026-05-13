import java.util.List;

public class BookService {

    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    // ── Add a new book ────────────────────────────────────────────────────────
   public boolean addBook(Book book) {

    //  Copies must be > 0
   if (book.getTotalCopies() < 1) {
        throw new RuntimeException("Total copies must be at least 1.");
    }

        // Duplicate check — same title + author already exists?
    Book existing = bookDAO.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
    if (existing != null) {
        throw new RuntimeException("Book \"" + book.getTitle()
                + "\" by " + book.getAuthor() + " already exists.");
    }

    //  available_copies must equal total_copies on a new book
    book.setAvailableCopies(book.getTotalCopies());
    return bookDAO.insertBook(book);
    }

    // ── Remove a book ─────────────────────────────────────────────────────────
    public boolean removeBook(int bookId) {
        return bookDAO.deleteBook(bookId);
    }

   // ── Get all books ─────────────────────────────────────────────────────────
public List<Book> getAllBooks() {

    List<Book> books = bookDAO.getAllBooks();

    if (books.isEmpty()) {
        throw new RuntimeException("No books found in the library.");
    }

    return books;
}

// ── Search books by keyword (title / author / category) ──────────────────
public List<Book> searchBooks(String keyword) {

    List<Book> results = bookDAO.searchBooks(keyword.trim());

    if (results.isEmpty()) {
        throw new RuntimeException("No books found matching \"" + keyword.trim() + "\".");
    }
    return results;
   }
}