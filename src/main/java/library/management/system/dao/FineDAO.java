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
        
public class FineDAO {
    
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
}
