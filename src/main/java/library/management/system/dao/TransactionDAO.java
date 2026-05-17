package library.management.system.dao;

import library.management.system.model.Transaction;
import library.management.system.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // ── Issue a book (INSERT a new transaction row) ──────────────────────────
    public boolean issueBook(Transaction transaction) {
        String sql = "INSERT INTO transactions "
                   + "(transaction_id, user_id, book_id, issue_date, due_date, "
                   + " return_date, fine_amount, status, fine_paid) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt    (1, transaction.getTransactionId());
            ps.setInt    (2, transaction.getUserId());
            ps.setInt    (3, transaction.getBookId());
            ps.setDate   (4, new java.sql.Date(transaction.getIssueDate().getTime()));
            ps.setDate   (5, new java.sql.Date(transaction.getDueDate().getTime()));

            // return_date is null for a freshly issued book
            if (transaction.getReturnDate() != null) {
                ps.setDate(6, new java.sql.Date(transaction.getReturnDate().getTime()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setDouble (7, transaction.getFineAmount());
            ps.setString (8, transaction.getStatus());
            ps.setBoolean(9, transaction.finePaid());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Return a book (UPDATE return date and status) ─────────────────────────
    public boolean returnBook(int transactionId, java.util.Date returnDate) {
        String sql = "UPDATE transactions "
                   + "SET return_date = ?, status = 'RETURNED' "
                   + "WHERE transaction_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(returnDate.getTime()));
            ps.setInt (2, transactionId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Find a single transaction by ID ──────────────────────────────────────
    public Transaction findTransactionById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";

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
        String sql = "SELECT * FROM transactions WHERE user_id = ?";

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
        String sql = "SELECT * FROM transactions";

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
        String sql = "SELECT COUNT(*) FROM transactions "
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
        String sql = "SELECT * FROM transactions "
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
        String sql = "SELECT COUNT(*) FROM transactions "
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

    // ── Check if a user is active ──────────
    //    Used by TransactionService.issueBook() to block issuing books to
    //    users whose accounts have been deactivated by an admin.
    public boolean isUserActive(int userId) {
        String sql = "SELECT is_active FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("is_active");
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
            rs.getDouble("fine_amount"),
            rs.getString("status")
        );
    }
}
