package library.management.system.dao;
 
import library.management.system.dto.*;
import library.management.system.model.Book;
import library.management.system.util.DBConnection;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
public class BookDAO {
 
    // ── Insert a new book ─────────────────────────────────────────────────────
    public boolean addBook(Book book) {
        String sql = "INSERT INTO Books "
                   + "(isbn, title, author, category, total_copies, available_copies) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
 
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory());
            ps.setInt   (5, book.getTotalCopies());
            ps.setInt   (6, book.getAvailableCopies());
 
            ps.executeUpdate();
 
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Book could not be added");
        }
 
        return true;
    }

 
    // ── Fetch all books ───────────────────────────────────────────────────────
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM Books";
 
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
 
            while (rs.next()) {
                list.add(mapRow(rs));
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return list;
    }
 
    // ── Find a single book by ID ──────────────────────────────────────────────
    public Book findBookById(int bookId) {
        String sql = "SELECT * FROM Books WHERE book_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                return mapRow(rs);
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return null;
    }
 
    // ── Find a single book by title or ISBN ───────────────────────────────────
    public Book findByTitleOrIsbn(String query) {
        String sql = "SELECT * FROM Books "
                   + "WHERE LOWER(title) = LOWER(?) OR LOWER(isbn) = LOWER(?) "
                   + "LIMIT 1";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, query);
            ps.setString(2, query);
 
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                return mapRow(rs);
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return null;
    }
 
    // ── Search books by title, author, or category ────────────────────────────
    public List<BookTableDTO> searchBooks(String keyword) {
        List<BookTableDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Books "
                   + "WHERE LOWER(title)    LIKE ? "
                   + "   OR LOWER(author)   LIKE ? "
                   + "   OR LOWER(category) LIKE ?";
 
        String pattern = "%" + keyword.toLowerCase() + "%";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
 
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToDTO(rs));
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return list;
    }
 
    // ── Update an existing book ───────────────────────────────────────────────
    public boolean updateBook(Book book) {
        String sql = "UPDATE Books "
                   + "SET title = ?, author = ?, category = ?, "
                   + "    total_copies = ?, available_copies = ? "
                   + "WHERE book_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setInt   (4, book.getTotalCopies());
            ps.setInt   (5, book.getAvailableCopies());
            ps.setInt   (6, book.getBookId());
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    // ── Activate or deactivate a book ─────────────────────────────────────────
    public boolean setBookActive(int bookId, boolean active) {
        String sql = "UPDATE Books SET is_active = ? WHERE book_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setBoolean(1, active);
            ps.setInt    (2, bookId);
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    // ── Check if a book is active ─────────────────────────────────────────────
    public boolean isBookActive(int bookId) {
        String sql = "SELECT is_active FROM Books WHERE book_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                return rs.getBoolean("is_active");
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return false;
    }
 
    // ── Check for duplicate book (same title + author) ────────────────────────
    public Book findByTitleAndAuthor(String title, String author) {
        String sql = "SELECT * FROM Books "
                   + "WHERE LOWER(title) = LOWER(?) AND LOWER(author) = LOWER(?)";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, title);
            ps.setString(2, author);
 
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                return mapRow(rs);
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return null;
    }
 
    // ── Helper: map a ResultSet row → Book object ─────────────────────────────
    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
            rs.getInt   ("book_id"),
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("category"),
            rs.getInt   ("total_copies"),
            rs.getInt   ("available_copies")
        );
    }
 
    // ── Helper: map a ResultSet row → BookTableDTO object ─────────────────────
    private BookTableDTO mapRowToDTO(ResultSet rs) throws SQLException {
        return new BookTableDTO(
            rs.getInt    ("book_id"),
            rs.getString ("isbn"),
            rs.getString ("title"),
            rs.getString ("author"),
            rs.getString ("category"),
            rs.getInt    ("total_copies"),
            rs.getBoolean("is_active"),
            rs.getInt    ("available_copies")
        );
    }
    
    // librarian book catalog table
    public List<BookCatalogDTO> getBookCatalog() {

        List<BookCatalogDTO> books = new ArrayList<>();

        String sql = """
            SELECT
                isbn,
                title,
                author,
                total_copies,
                available_copies
            FROM Books
            WHERE is_active = TRUE
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                BookCatalogDTO book = new BookCatalogDTO(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load book catalog");
        }

        return books;
    }
    
    // search librarian book catalog
    public List<BookCatalogDTO> searchBookCatalog(String keyword) {

        List<BookCatalogDTO> books = new ArrayList<>();

        String sql = """
            SELECT isbn, title, author, total_copies, available_copies
            FROM Books
            WHERE is_active = TRUE
            AND (
                isbn LIKE ?
                OR title LIKE ?
                OR author LIKE ?
            )
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            String search = "%" + keyword + "%";

            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                BookCatalogDTO book = new BookCatalogDTO(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search book catalog");
        }

        return books;
    }
    
    // get books for student catalog
    public List<StudentBookCatalogDTO> getStudentBookCatalog() {

        List<StudentBookCatalogDTO> books = new ArrayList<>();

        String sql = """
            SELECT author, title, category, available_copies 
            FROM Books WHERE is_active = TRUE
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                
                StudentBookCatalogDTO book = new StudentBookCatalogDTO(
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("category"),
                        rs.getInt("available_copies")
                );
                
                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load student book catalog");
        }

        return books;
    }
    
    // search student books
    public List<StudentBookCatalogDTO> searchStudentBookCatalog(String keyword) {

        List<StudentBookCatalogDTO> books = new ArrayList<>();

        String sql = """
            SELECT author, title, category, available_copies
            FROM Books WHERE is_active = TRUE
            AND (
                author LIKE ?
                OR title LIKE ?
                OR category LIKE ?
            )
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            String search = "%" + keyword + "%";

            stmt.setString(1, search);
            stmt.setString(2, search);
            stmt.setString(3, search);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                StudentBookCatalogDTO book = new StudentBookCatalogDTO(
                        rs.getString("author"),
                        rs.getString("title"),
                        rs.getString("category"),
                        rs.getInt("available_copies")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search student book catalog");
        }

        return books;
    }
    
    // admin total books panel
    public int getTotalActiveBooksCount() {
        String sql = """
            SELECT COUNT(*)
            FROM Books
            WHERE is_active = TRUE
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load book count");
        }

        return 0;
    }
}