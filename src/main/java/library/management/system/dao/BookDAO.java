import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // ── Insert a new book ─────────────────────────────────────────────────────
    public boolean insertBook(Book book) {
        String sql = "INSERT INTO books "
                   + "(book_id, title, author, category, total_copies, available_copies) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt   (1, book.getBookId());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory());
            ps.setInt   (5, book.getTotalCopies());
            ps.setInt   (6, book.getAvailableCopies());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Fetch all books ───────────────────────────────────────────────────────
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";

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

    // ── Search books by title, author, or category ────────────────────────────
    public List<Book> searchBooks(String keyword) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books "
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
                list.add(mapRow(rs));
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

    // ── Delete a book ─────────────────────────────────────────────────────────
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Check for duplicate book (same title + author) ────────────────────────
    public Book findByTitleAndAuthor(String title, String author) {
        String sql = "SELECT * FROM books WHERE LOWER(title) = LOWER(?) AND LOWER(author) = LOWER(?)";

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
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("category"),
            rs.getInt   ("total_copies"),
            rs.getInt   ("available_copies")
        );
    }
}