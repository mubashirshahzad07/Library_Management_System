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
 * Handles the dashboard of Student
 */
public class StudentFrameDashboardCard {
    private final JPanel dashboardCard;
    private final int userId;

    public StudentFrameDashboardCard(JPanel dashboardCard, User user) {
        this.dashboardCard = dashboardCard;
        this.userId = user.getUserId();
        this.addCardHeader();
        this.addBooksReturnInformation();
        this.addBooksBorrowedPanel();
        this.addBooksReturnedPanel();
        this.addFinesPanel();
        this.addCurrentlyBorrowedLabel();
        this.addDashboardTable();
        this.addVerticalFiller();
    }

    private void addCardHeader() {
        JLabel myDashboardLabel = new JLabel("My dashboard");
        myDashboardLabel.setForeground(Color.WHITE);
        myDashboardLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));

        JLabel dashboardStudentIcon = new JLabel(new ImageIcon(ClassLoader.getSystemResource("student_label.png")));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(30, 25, 30, 30);
        dashboardCard.add(myDashboardLabel, gridBagConstraints);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(30, 15, 20, 25);
        dashboardCard.add(dashboardStudentIcon, gridBagConstraints);
    }

    private void addBooksReturnInformation() {
        JLabel booksReturnInformation = new JLabel();
        booksReturnInformation.setOpaque(true);
        booksReturnInformation.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 10));
        booksReturnInformation.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 15));

        String sql = "SELECT b.title, t.due_date, " +
                "CASE WHEN t.due_date < CURDATE() THEN 1 ELSE 0 END AS overdue " +
                "FROM Transactions t JOIN Books b ON t.book_id = b.book_id " +
                "WHERE t.user_id = ? AND t.status = 'ISSUED' " +
                "ORDER BY overdue DESC, t.due_date ASC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String title   = rs.getString("title");
                Date dueDate   = rs.getDate("due_date");
                boolean overdue = rs.getInt("overdue") == 1;
                if (overdue) {
                    booksReturnInformation.setForeground(Color.WHITE);
                    booksReturnInformation.setBackground(new Color(0xF59E0B));
                    booksReturnInformation.setText(title + " is overdue since " + dueDate + " \u23AF please return the book immediately.");
                } else {
                    booksReturnInformation.setBackground(new Color(0xF0E526));
                    booksReturnInformation.setText(title + " is due on " + dueDate + " \u23AF please return on time.");
                }
            } else {
                booksReturnInformation.setBackground(new Color(0x29CF45));
                booksReturnInformation.setText("No books currently issued.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            booksReturnInformation.setBackground(new Color(0xF0E526));
            booksReturnInformation.setText("Could not load book due date information.");
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 25, 10, 30);
        dashboardCard.add(booksReturnInformation, gbc);
    }

    private void addBooksBorrowedPanel() {
        JPanel booksBorrowedPanel = new JPanel();
        booksBorrowedPanel.setLayout(new GridBagLayout());
        booksBorrowedPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 20));
        booksBorrowedPanel.setBackground(new Color(0x67ABD6));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel booksBorrowedLabel = new JLabel("Books borrowed");
        booksBorrowedLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 18));
        booksBorrowedLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        booksBorrowedPanel.add(booksBorrowedLabel, gbc);

        int noOfBooksBorrowed = fetchCount(
                "SELECT COUNT(*) FROM Transactions WHERE user_id = ? AND status = 'ISSUED'");
        JLabel noOfBooksBorrowedLabel = new JLabel(String.valueOf(noOfBooksBorrowed));
        noOfBooksBorrowedLabel.setFont(new Font("FiraMono NerdFont", Font.BOLD, 25));
        noOfBooksBorrowedLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        booksBorrowedPanel.add(noOfBooksBorrowedLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.20;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 25, 30, 5);
        dashboardCard.add(booksBorrowedPanel, gbc);
    }

    private void addBooksReturnedPanel() {
        JPanel booksReturnedPanel = new JPanel();
        booksReturnedPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 20));
        booksReturnedPanel.setLayout(new GridBagLayout());
        booksReturnedPanel.setBackground(new Color(0x29CF45));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel booksReturnedLabel = new JLabel("Books returned");
        booksReturnedLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 18));
        booksReturnedLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        booksReturnedPanel.add(booksReturnedLabel, gbc);

        int noOfBooksReturned = fetchCount(
                "SELECT COUNT(*) FROM Transactions WHERE user_id = ? AND status = 'RETURNED'");
        JLabel noOfBooksReturnedLabel = new JLabel(String.valueOf(noOfBooksReturned));
        noOfBooksReturnedLabel.setFont(new Font("FiraMono NerdFont", Font.BOLD, 25));
        noOfBooksReturnedLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        booksReturnedPanel.add(noOfBooksReturnedLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.43;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 30, 5);
        dashboardCard.add(booksReturnedPanel, gbc);
    }

    private void addFinesPanel() {
        JPanel finesPanel = new JPanel();
        finesPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 35));
        finesPanel.setLayout(new GridBagLayout());
        finesPanel.setBackground(new Color(0xCF2929));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel finesLabel = new JLabel("Fines             ");
        finesLabel.setForeground(Color.WHITE);
        finesLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        finesPanel.add(finesLabel, gbc);

        double fineAmount = fetchFineAmount();
        JLabel fineAmountLabel = new JLabel("Rs " + (int) fineAmount);
        fineAmountLabel.setFont(new Font("FiraMono NerdFont", Font.BOLD, 25));
        fineAmountLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        finesPanel.add(fineAmountLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.37;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 30, 30);
        dashboardCard.add(finesPanel, gbc);
    }

    private void addCurrentlyBorrowedLabel() {
        JLabel currentlyBorrowedLabel = new JLabel("Currently borrowed");
        currentlyBorrowedLabel.setForeground(Color.WHITE);
        currentlyBorrowedLabel.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 25, 10, 30);
        dashboardCard.add(currentlyBorrowedLabel, gbc);
    }

    private void addDashboardTable() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Book title");
        model.addColumn("Author");
        model.addColumn("Due date");
        model.addColumn("Status");

        String sql = "SELECT b.title, b.author, t.due_date, " +
                "CASE WHEN t.due_date < CURDATE() THEN 'Overdue' " +
                "     WHEN DATEDIFF(t.due_date, CURDATE()) <= 3 THEN 'Due soon' " +
                "     ELSE 'Issued' END AS status " +
                "FROM Transactions t JOIN Books b ON t.book_id = b.book_id " +
                "WHERE t.user_id = ? AND t.status = 'ISSUED' ORDER BY t.due_date ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDate("due_date").toString(),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JTable dashboardTable = new JTable(model);
        JTableHeader header = dashboardTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        dashboardTable.setGridColor(Color.DARK_GRAY);
        dashboardTable.setShowGrid(false);
        dashboardTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        dashboardTable.setRowHeight(25);

        dashboardTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        int rowHeight = dashboardTable.getRowHeight();
        int noOfRows = Math.max(dashboardTable.getRowCount(), 1);
        dashboardTable.setPreferredScrollableViewportSize(new Dimension(
                dashboardTable.getPreferredSize().width,
                rowHeight * noOfRows
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        JScrollPane scrollPane = new JScrollPane(dashboardTable);
        scrollPane.getViewport().setBackground(new Color(0x212020));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 25, 30, 30);
        dashboardCard.add(scrollPane, gbc);
    }

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        dashboardCard.add(Box.createVerticalGlue(), gbc);
    }

    private int fetchCount(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double fetchFineAmount() {
        String sql = "SELECT COALESCE(SUM(fine_amount), 0) FROM Fines WHERE user_id = ? AND payment_status = 'UNPAID'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}