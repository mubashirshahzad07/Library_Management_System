package library.management.system.dao;
 
import library.management.system.model.Transaction;
import library.management.system.util.DBConnection;
import library.management.system.dto.*;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
public class TransactionDAO {
 
    // ── Issue a book  ──────────────────────────
public boolean issueBook(Transaction transaction) {

    String checkSql = "SELECT COUNT(*) FROM Transactions "
                    + "WHERE user_id = ? "
                    + "AND book_id = ? "
                    + "AND status = 'ISSUED'";

    String insertSql = "INSERT INTO Transactions "
                     + "(user_id, book_id, issue_date, due_date, return_date, status) "
                     + "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

        checkPs.setInt(1, transaction.getUserId());
        checkPs.setInt(2, transaction.getBookId());

        ResultSet rs = checkPs.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            return false; // active loan already exists, block the issue
        }

        try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {

            insertPs.setInt(1, transaction.getUserId());
            insertPs.setInt(2, transaction.getBookId());
            insertPs.setDate(3, new java.sql.Date(transaction.getIssueDate().getTime()));
            insertPs.setDate(4, new java.sql.Date(transaction.getDueDate().getTime()));

            if (transaction.getReturnDate() != null) {
                insertPs.setDate(5, new java.sql.Date(transaction.getReturnDate().getTime()));
            } else {
                insertPs.setNull(5, Types.DATE);
            }

            insertPs.setString(6, transaction.getStatus());

            return insertPs.executeUpdate() > 0;
        }

    } catch (SQLException e) {
        throw new RuntimeException("Operation failed", e);
    }
}
    // ── Return a book (UPDATE return date and status) ─────────────────────────
public int returnBook(String bookTitleOrIsbn, String memberUsername) {
    String findSql = "SELECT t.transaction_id "
                   + "FROM Transactions t "
                   + "JOIN Books b ON t.book_id = b.book_id "
                   + "JOIN Users u ON t.user_id = u.user_id "
                   + "WHERE (b.title = ? OR b.isbn = ?) "
                   + "AND u.username = ? "
                   + "AND t.status = 'ISSUED' "
                   + "ORDER BY t.issue_date DESC "
                   + "LIMIT 1";

    String updateSql = "UPDATE Transactions "
                     + "SET return_date = ?, status = 'RETURNED' "
                     + "WHERE transaction_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement findPs = conn.prepareStatement(findSql)) {

        findPs.setString(1, bookTitleOrIsbn);
        findPs.setString(2, bookTitleOrIsbn);
        findPs.setString(3, memberUsername);

        ResultSet rs = findPs.executeQuery();

        if (!rs.next()) {
            return -1;
        }

        int transactionId = rs.getInt("transaction_id");

        try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
            updatePs.setDate(1, new java.sql.Date(System.currentTimeMillis()));
            updatePs.setInt(2, transactionId);

            int rowsAffected = updatePs.executeUpdate();

            if (rowsAffected > 0) {
                return transactionId;
            } else {
                throw new RuntimeException("Operation failed");
            }
        }

    } catch (SQLException e) {
        throw new RuntimeException("Operation failed", e);
    }
}
 
    // ── Find a single transaction by ID ──────────────────────────────────────
    public Transaction findTransactionById(int transactionId) {
        String sql = "SELECT * FROM Transactions WHERE transaction_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                return mapRow(rs);
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return null;
    }
 
    // ── All transactions for a specific user ─────────────────────────────────
    public List<Transaction> getTransactionsByUser(int userId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE user_id = ?";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
 
            while (rs.next()) {
                list.add(mapRow(rs));
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return list;
    }
 
    // ── All transactions in the system ───────────────────────────────────────
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM Transactions";
 
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
 
    // ── Check if a user has any overdue books (block check) ──────────────────
    public boolean hasOverdueBooks(int userId) {
        String sql = "SELECT COUNT(*) FROM Transactions "
                   + "WHERE user_id = ? "
                   + "AND status = 'ISSUED' "
                   + "AND due_date < CURDATE()";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return false;
    }
 
    // ── Get all overdue transactions for a specific user ──────────────────────
    public List<Transaction> getOverdueTransactionsByUser(int userId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM Transactions "
                   + "WHERE user_id = ? "
                   + "AND status = 'ISSUED' "
                   + "AND due_date < CURDATE()";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
 
            while (rs.next()) {
                list.add(mapRow(rs));
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return list;
    }
 
    // ── Check if a book has any unreturned (ISSUED) transactions ─────────────
    public boolean isBookCurrentlyIssued(int bookId) {
        String sql = "SELECT COUNT(*) FROM Transactions "
                   + "WHERE book_id = ? "
                   + "AND status = 'ISSUED'";
 
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
 
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
 
        return false;
    }
 
 
    // ── Helper: map a ResultSet row → Transaction object ─────────────────────
    private Transaction mapRow(ResultSet rs) throws SQLException {
        java.sql.Date returnSql = rs.getDate("return_date");
        java.util.Date returnDate = (returnSql != null)
                                  ? new java.util.Date(returnSql.getTime())
                                  : null;
 
        return new Transaction(
            rs.getInt   ("transaction_id"),
            rs.getInt   ("user_id"),
            rs.getInt   ("book_id"),
            new java.util.Date(rs.getDate("issue_date").getTime()),
            new java.util.Date(rs.getDate("due_date").getTime()),
            returnDate,
            rs.getString("status")
        );
    }
    
    // display transactions for admin
    public List<TransactionReportDTO> getTransactionReports() {
        
        List<TransactionReportDTO> reports = new ArrayList<>();
        
        String sql = """
            SELECT 
                transaction_id, 
                username,
                book_title,
                issue_date,
                due_date,
                status
            FROM student_transaction_history
        """;
        
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                TransactionReportDTO report = new TransactionReportDTO(
                        rs.getInt("transaction_id"),
                        rs.getString("username"),
                        rs.getString("book_title"),
                        rs.getDate("issue_date"),
                        rs.getDate("due_date"),
                        rs.getString("status")
                );
                
                reports.add(report);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load transaction reports");
        }
        
        return reports;
    }
    
    // search transactions
    public List<TransactionReportDTO> searchTransactionReports(String keyword) {

        List<TransactionReportDTO> reports = new ArrayList<>();

        String sql = """
            SELECT
                transaction_id,
                username,
                book_title,
                issue_date,
                due_date,
                status
            FROM student_transaction_history
            WHERE
                CAST(transaction_id AS CHAR) LIKE ?
                OR username LIKE ?
                OR book_title LIKE ?
                OR status LIKE ?
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            String search = "%" + keyword + "%";

            String digits = keyword.replaceAll("[^0-9]", "").replaceFirst("^0+", "");
        
            String idSearch = digits.isEmpty() ? "%no_numeric_id_provided%" : "%" + digits + "%";

            stmt.setString(1, idSearch);
            stmt.setString(2, search);
            stmt.setString(3, search);
            stmt.setString(4, search);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                TransactionReportDTO report = new TransactionReportDTO(
                        rs.getInt("transaction_id"),
                        rs.getString("username"),
                        rs.getString("book_title"),
                        rs.getDate("issue_date"),
                        rs.getDate("due_date"),
                        rs.getString("status")
                );

                reports.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search transaction reports");
            }

        return reports;
    }
    
    // librarian dashboard issued books
    public List<IssuedBookDTO> getIssuedBooks() {

        List<IssuedBookDTO> books = new ArrayList<>();

        String sql = """
            SELECT u.username, b.title AS book_title, t.due_date, t.status
            FROM Transactions t
            JOIN Users u
                ON t.user_id = u.user_id
            JOIN Books b
                ON t.book_id = b.book_id
            WHERE t.status = 'ISSUED'
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                IssuedBookDTO book = new IssuedBookDTO(
                        rs.getString("username"),
                        rs.getString("book_title"),
                        rs.getDate("due_date"),
                        rs.getString("status")
                );

                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load issued books");
        }

        return books;
    }
    
    // return student borrowed books 
    public List<StudentBorrowedBookDTO> getStudentBorrowedBooks(int userId) {

        List<StudentBorrowedBookDTO> books = new ArrayList<>();

        String sql = """
            SELECT b.title, b.author, t.issue_date, t.due_date
            FROM Transactions t
            JOIN Books b
                ON t.book_id = b.book_id
            WHERE t.user_id = ?
            AND t.status = 'ISSUED'
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StudentBorrowedBookDTO book = new StudentBorrowedBookDTO(
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getDate("issue_date"),
                                rs.getDate("due_date")
                        );

                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load borrowed books");
        }

        return books;
    }
    
    // student transaction history table
    public List<StudentHistoryDTO> getStudentHistory(int userId) {

        List<StudentHistoryDTO> history = new ArrayList<>();

        String sql = """
            SELECT book_title, issue_date, return_date, fine_amount
            FROM student_transaction_history
            WHERE user_id = ?
            AND status = 'RETURNED'
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double fine = rs.getDouble("fine_amount");

                if (rs.wasNull()) {
                    fine = 0;
                }

                history.add(new StudentHistoryDTO(
                        rs.getString("book_title"),
                        rs.getDate("issue_date"),
                        rs.getDate("return_date"),
                        fine
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load student history");
        }

        return history;
    }
    
    // librarian dashboard panels 
    public int getOverdueBooksTodayCount() {
        String sql = """
            SELECT COUNT(*)
            FROM Transactions
            WHERE status = 'ISSUED'
            AND due_date = CURDATE()
        """;

        return getCount(sql);
    }
    
    public int getBooksIssuedTodayCount() {
        String sql = """
            SELECT COUNT(*)
            FROM Transactions
            WHERE issue_date = CURDATE()
        """;

        return getCount(sql);
    }
    
    public int getReturnsTodayCount() {
        String sql = """
            SELECT COUNT(*)
            FROM Transactions
            WHERE status = 'RETURNED'
            AND return_date = CURDATE()
        """;

        return getCount(sql);
    }
    
    public int getTotalOverdueBooksCount() {
        String sql = """
            SELECT COUNT(*)
            FROM Transactions
            WHERE status = 'ISSUED'
            AND due_date < CURDATE()
        """;

        return getCount(sql);
    }
    
    // admin panel to show active loans
    public int getActiveLoansCount() {
        String sql = """
            SELECT COUNT(*)
            FROM Transactions
            WHERE status = 'ISSUED'
        """;

        return getCount(sql);
    }
   
    // helper method for panels
    private int getCount(String sql) {
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
            throw new RuntimeException("Failed to load count");
        }

        return 0;
    }
    
    // student dashboard panels
    public int getCurrentlyBorrowedBooksCount(int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM Transactions
            WHERE user_id = ?
            AND status = 'ISSUED'
        """;

        return getCountByUserId(sql, userId);
    }

    public int getTotalIssuedBooksCount(int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM Transactions
            WHERE user_id = ?
        """;

        return getCountByUserId(sql, userId);
    }

    public int getTotalReturnedBooksCount(int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM Transactions
            WHERE user_id = ?
            AND status = 'RETURNED'
        """;

        return getCountByUserId(sql, userId);
    }
    
    public int getTotalOverdueBooksCount(int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM Transactions
            WHERE user_id = ?
            AND status = 'ISSUED'
            AND due_date < CURDATE()
        """;

        return getCount(sql);
    }
    
    private int getCountByUserId(String sql, int userId) {
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load count");
        }

        return 0;
    }   
}
