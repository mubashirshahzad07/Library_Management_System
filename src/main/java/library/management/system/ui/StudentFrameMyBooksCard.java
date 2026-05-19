package library.management.system.ui;

import library.management.system.dto.StudentBorrowedBookDTO;
import library.management.system.model.User;
import library.management.system.service.TransactionService;
import library.management.system.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

import java.util.List;

/**
 * Handles the books that are currently borrowed by Student
 */
public class StudentFrameMyBooksCard {
    private final JPanel myBooksCard;
    private final TransactionService transactionService = new TransactionService();
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private User user;

    public StudentFrameMyBooksCard(JPanel myBooksCard, User user) {
        this.user = user;
        this.myBooksCard = myBooksCard;
        this.addCardHeading();
        this.addBooksReturnInformation();
        this.addMyBooksTable(user.getUserId());
        this.addVerticalFiller();
    }

    private void addCardHeading() {
        JLabel myBooksLabel = new JLabel("My books");
        myBooksLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        myBooksLabel.setForeground(Color.WHITE);
        JLabel studentLabel = new JLabel(new ImageIcon(ClassLoader.getSystemResource("student_label.png")));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 25, 5, 300);
        myBooksCard.add(myBooksLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(30, 300, 5, 25);
        myBooksCard.add(studentLabel, gbc);
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
            ps.setInt(1, user.getUserId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String title = rs.getString("title");
                Date dueDate = rs.getDate("due_date");
                boolean overdue = (rs.getInt("overdue") == 1);
                if (overdue) {
                    booksReturnInformation.setForeground(Color.WHITE);
                    booksReturnInformation.setBackground(new Color(0xF59E0B));
                    booksReturnInformation.setText(title + " is overdue since " + dueDate + " ⎯ please return immediately.");
                } else {
                    booksReturnInformation.setBackground(new Color(0xF0E526));
                    booksReturnInformation.setText(title + " is due on " + dueDate + " ⎯ please return on time.");
                }
            } else {
                booksReturnInformation.setBackground(new Color(0x29CF45));
                booksReturnInformation.setText("No books currently borrowed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            booksReturnInformation.setBackground(new Color(0xF0E526));
            booksReturnInformation.setText("Could not load book information.");
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 25, 10, 30);
        myBooksCard.add(booksReturnInformation, gbc);
    }

    private void addMyBooksTable(int userId) {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Book title");
        model.addColumn("Issue date");
        model.addColumn("Due date");
        model.addColumn("Status");

        getBooksIssued(userId);

        JTable myBooksTable = new JTable(model);
        JTableHeader header = myBooksTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        myBooksTable.setGridColor(Color.DARK_GRAY);
        myBooksTable.setShowGrid(false);
        myBooksTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        myBooksTable.setRowHeight(25);

        myBooksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        int rowHeight = myBooksTable.getRowHeight();
        int noOfRows = Math.max(myBooksTable.getRowCount(), 1);
        myBooksTable.setPreferredScrollableViewportSize(new Dimension(
                myBooksTable.getPreferredSize().width,
                rowHeight * noOfRows
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        scrollPane = new JScrollPane(myBooksTable);
        scrollPane.getViewport().setBackground(new Color(0x212020));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 25, 5, 25);
        myBooksCard.add(scrollPane, gbc);
    }

    private void getBooksIssued(int userId) {
        model.setRowCount(0);
        java.util.List<StudentBorrowedBookDTO> books = transactionService.getStudentBorrowedBooks(userId);

        for (StudentBorrowedBookDTO book : books) {
            model.addRow(new Object[] {book.getTitle(), book.getAuthor(), book.getDueDate(), book.getStatus()});
        }

        if (scrollPane != null) {
            scrollPane.getViewport().setBackground(new Color(0x212020));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        myBooksCard.add(Box.createVerticalGlue(), gbc);
    }
}