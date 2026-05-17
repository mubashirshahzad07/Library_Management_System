package library.management.system.ui;

import library.management.system.model.User;
import library.management.system.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

/**
 * @since 03 May 2026
 * Records all the books that were previously borrowed by student
 */
public class StudentFrameHistoryCard {
    private final JPanel historyCard;
    private final int userId;

    public StudentFrameHistoryCard(JPanel historyCard, User user) {
        this.historyCard = historyCard;
        this.userId = user.getUserId();
        this.addCardHeading();
        this.addHistoryTable();
        this.addVerticalFiller();
    }

    private void addCardHeading() {
        JLabel historyLabel = new JLabel("History");
        historyLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        historyLabel.setForeground(Color.WHITE);
        JLabel studentLabel = new JLabel(new ImageIcon(ClassLoader.getSystemResource("student_label.png")));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(30, 5, 30, 325);
        historyCard.add(historyLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(30, 325, 30, 5);
        historyCard.add(studentLabel, gbc);
    }

    private void addHistoryTable() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Book");
        model.addColumn("Issued");
        model.addColumn("Returned");
        model.addColumn("Fine");

        String sql = "SELECT book_title, issue_date, return_date, " +
                "COALESCE(fine_amount, 0) AS fine_amount " +
                "FROM student_transaction_history " +
                "WHERE user_id = ? AND status = 'RETURNED' " +
                "ORDER BY return_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double fine = rs.getDouble("fine_amount");
                model.addRow(new Object[]{
                        rs.getString("book_title"),
                        rs.getDate("issue_date").toString(),
                        rs.getDate("return_date").toString(),
                        fine > 0 ? "Rs " + (int) fine : "\u2013"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable historyTable = new JTable(model);
        JTableHeader header = historyTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        historyTable.setGridColor(Color.DARK_GRAY);
        historyTable.setShowGrid(false);
        historyTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        historyTable.setRowHeight(25);

        historyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBackground(new Color(0x388A7C));
                label.setOpaque(true);
                label.setForeground(Color.WHITE);
                return label;
            }
        });

        int rowHeight = historyTable.getRowHeight();
        int noOfRows = Math.max(historyTable.getRowCount(), 1);
        historyTable.setPreferredScrollableViewportSize(new Dimension(
                historyTable.getPreferredSize().width,
                rowHeight * noOfRows
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(new Color(0x212020));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        historyCard.add(scrollPane, gbc);
    }

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        historyCard.add(Box.createVerticalGlue(), gbc);
    }
}