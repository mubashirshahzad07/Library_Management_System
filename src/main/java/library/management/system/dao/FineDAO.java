package library.management.system.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import library.management.system.model.Fine;
import library.management.system.service.FineService;
import library.management.system.util.DBConnection;
import library.management.system.dto.FineReportDTO;
        
public class FineDAO {
    
    // insert fine when book is returned late
    public void insertFine(Fine fine) {

        String sql = """
            INSERT INTO Fines (transaction_id, user_id, fine_amount, payment_status)
            VALUES (?, ?, ?, ?)
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, fine.getTransactionId());
            stmt.setInt(2, fine.getUserId());
            stmt.setDouble(3, fine.getFineAmount());
            stmt.setString(4, fine.getPaymentStatus());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert fine");
        }
    }
    
    // get system fines for admin
    public List<FineReportDTO> getFineReports() {

        List<FineReportDTO> fines = new ArrayList<>();

        String sql = """
            SELECT transaction_id, student_username, book_title, due_date, return_date, fine_amount, payment_status
            FROM fines_report
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                FineReportDTO fine = new FineReportDTO(
                        rs.getInt("transaction_id"),
                        rs.getString("student_username"),
                        rs.getString("book_title"),
                        rs.getDate("due_date"),
                        rs.getDate("return_date"),
                        rs.getDouble("fine_amount"),
                        rs.getString("payment_status")
                );

                fines.add(fine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load fine reports");
        }

        return fines;
    }
    
    // search admin fine reports based on transactionId, username, book title, status
    public List<FineReportDTO> searchFineReports(String keyword) {

        List<FineReportDTO> fines = new ArrayList<>();

        String sql = """
            SELECT
                transaction_id,
                student_username,
                book_title,
                due_date,
                return_date,
                fine_amount,
                payment_status
            FROM fines_report
            WHERE
                CAST(transaction_id AS CHAR) LIKE ?
                OR student_username LIKE ?
                OR book_title LIKE ?
                OR payment_status LIKE ?
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            String search = "%" + keyword + "%";

            String digits = keyword.replaceAll("[^0-9]", "").replaceFirst("^0+", "");

            String idSearch = digits.isEmpty() ? "%no_numeric_id%" : "%" + digits + "%";

            stmt.setString(1, idSearch);
            stmt.setString(2, search);
            stmt.setString(3, search);
            stmt.setString(4, search);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                FineReportDTO fine = new FineReportDTO(
                        rs.getInt("transaction_id"),
                        rs.getString("student_username"),
                        rs.getString("book_title"),
                        rs.getDate("due_date"),
                        rs.getDate("return_date"),
                        rs.getDouble("fine_amount"),
                        rs.getString("payment_status")
                );

                fines.add(fine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to search fine reports");
        }

        return fines;
    }
}
