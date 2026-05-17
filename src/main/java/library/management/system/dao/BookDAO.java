package library.management.system.dao;

import library.management.system.dto.BookTableDTO;
import library.management.system.model.Book;
import library.management.system.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // ── Insert a new book ─────────────────────────────────────────────────────
    public boolean insertBook(Book book) {
        String sql = "INSERT INTO books "
                + "(book_id, isbn, title, author, category, total_copies, available_copies) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt   (1, book.getBookId());
            ps.setString(2, book.getIsbn());
            ps.setString(3, book.getTitle());
            ps.setString(4, book.getAuthor());
            ps.setString(5, book.getCategory());
            ps.setInt   (6, book.getTotalCopies());
            ps.setInt   (7, book.getAvailableCopies());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Fetch all active books  ──────────────────────
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE is_active = TRUE";

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
        String sql = "SELECT * FROM books WHERE book_id = ?";

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

    // ── Search books by keyword (active books only) ───────────────────────────
    public List<BookTableDTO> searchBooks(String keyword) {
        List<BookTableDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM books "
                + "WHERE (CAST(book_id AS CHAR) LIKE ? "
                + "    OR LOWER(isbn)     LIKE ? "
                + "    OR LOWER(title)    LIKE ? "
                + "    OR LOWER(author)   LIKE ? "
                + "    OR LOWER(category) LIKE ?)";

        String pattern = "%" + keyword.toLowerCase() + "%";

        // Extract digits for book_id search
        String digits = keyword.replaceAll("[^0-9]", "").replaceFirst("^0+", "");
        String idSearch = digits.isEmpty() ? "%no_numeric_id_provided%" : "%" + digits + "%";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idSearch);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            ps.setString(5, pattern);

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
        String sql = "UPDATE books "
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

    // ── Soft-delete  ──────────────────
    public boolean setBookActive(int bookId, boolean active) {
        String sql = "UPDATE books SET is_active = ? WHERE book_id = ?";

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

    // ── Check if a book is active (not soft-deleted) ──────────────────────────
    public boolean isBookActive(int bookId) {
        String sql = "SELECT is_active FROM books WHERE book_id = ?";

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
        String sql = "SELECT * FROM books "
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

    // ── Helper: map a ResultSet row → BookTableDTO object ────────────────────
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
}
