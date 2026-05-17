package library.management.system.ui;

import library.management.system.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * @since 16 May 2026
 * Handles the return book window
 */
public class LibrarianFrameReturnBookCard {
    private final JPanel returnBookCard;

    LibrarianFrameReturnBookCard(JPanel returnBookCard) {
        this.returnBookCard = returnBookCard;
        this.addCardHeader();
        this.addReturnBookPanel();
        this.addVerticalFiller();
    }

    private void addCardHeader() {
        JLabel returnBookLabel = new JLabel("Return book");
        returnBookLabel.setForeground(Color.WHITE);
        returnBookLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));

        JLabel libraryIcon = new JLabel(new ImageIcon(ClassLoader.getSystemResource("librarian_label.png")));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(30, 25, 30, 300);
        returnBookCard.add(returnBookLabel, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(30, 300, 20, 25);
        returnBookCard.add(libraryIcon, gridBagConstraints);
    }

    private void addReturnBookPanel() {
        JPanel returnBookPanel = new JPanel();
        returnBookPanel.setLayout(new GridBagLayout());
        returnBookPanel.setBackground(new Color(0x2E2D2D));

        // book title / isbn
        JLabel bookTitleLabel = new JLabel("Book title/ISBN");
        bookTitleLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        bookTitleLabel.setForeground(Color.WHITE);

        JTextField bookTitleBox = new JTextField();
        bookTitleBox.setBackground(Color.DARK_GRAY);
        bookTitleBox.setForeground(Color.WHITE);
        bookTitleBox.setCaretColor(Color.WHITE);
        bookTitleBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        bookTitleBox.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        bookTitleBox.setPreferredSize(new Dimension(100, 35));

        // member username
        JLabel memberIdLabel = new JLabel("Member Username");
        memberIdLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        memberIdLabel.setForeground(Color.WHITE);

        JTextField memberIdBox = new JTextField();
        memberIdBox.setBackground(Color.DARK_GRAY);
        memberIdBox.setForeground(Color.WHITE);
        memberIdBox.setCaretColor(Color.WHITE);
        memberIdBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        memberIdBox.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        memberIdBox.setPreferredSize(new Dimension(100, 35));

        // confirm return button
        JButton confirmReturnButton = new JButton("Confirm return");
        confirmReturnButton.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        confirmReturnButton.setForeground(Color.WHITE);
        confirmReturnButton.setBackground(Color.DARK_GRAY);
        confirmReturnButton.setFocusable(false);

        confirmReturnButton.addActionListener(e -> {
            String memberUsername = memberIdBox.getText().strip();
            String bookQuery      = bookTitleBox.getText().strip();

            if (memberUsername.isEmpty() || bookQuery.isEmpty()) {
                JOptionPane.showMessageDialog(returnBookCard,
                        "Both fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            returnBook(memberUsername, bookQuery);

            memberIdBox.setText("");
            bookTitleBox.setText("");
        });

        GridBagConstraints gbcReturnBook;

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 0;
        gbcReturnBook.anchor = GridBagConstraints.WEST;
        gbcReturnBook.insets = new Insets(10, 10, 1, 10);
        returnBookPanel.add(bookTitleLabel, gbcReturnBook);

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 1;
        gbcReturnBook.gridwidth = GridBagConstraints.REMAINDER;
        gbcReturnBook.fill = GridBagConstraints.HORIZONTAL;
        gbcReturnBook.weightx = 1.0;
        gbcReturnBook.anchor = GridBagConstraints.WEST;
        gbcReturnBook.insets = new Insets(1, 10, 10, 10);
        returnBookPanel.add(bookTitleBox, gbcReturnBook);

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 2;
        gbcReturnBook.anchor = GridBagConstraints.WEST;
        gbcReturnBook.insets = new Insets(10, 10, 1, 10);
        returnBookPanel.add(memberIdLabel, gbcReturnBook);

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 3;
        gbcReturnBook.gridwidth = GridBagConstraints.REMAINDER;
        gbcReturnBook.fill = GridBagConstraints.HORIZONTAL;
        gbcReturnBook.weightx = 1.0;
        gbcReturnBook.anchor = GridBagConstraints.WEST;
        gbcReturnBook.insets = new Insets(1, 10, 10, 10);
        returnBookPanel.add(memberIdBox, gbcReturnBook);

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 6;
        gbcReturnBook.gridwidth = 2;
        gbcReturnBook.anchor = GridBagConstraints.CENTER;
        gbcReturnBook.insets = new Insets(10, 10, 10, 10);
        returnBookPanel.add(confirmReturnButton, gbcReturnBook);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 15, 20, 25);
        returnBookCard.add(returnBookPanel, gbc);
    }

    /**
     * Marks the transaction as RETURNED, sets return_date to today, increments
     * available_copies, and inserts a Fine record if the book is overdue (Rs 1/day).
     */
    private void returnBook(String memberUsername, String bookQuery) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int userId = -1;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_id FROM Users WHERE username = ? AND role = 'STUDENT' AND is_active = TRUE")) {
                ps.setString(1, memberUsername);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) userId = rs.getInt("user_id");
            }
            if (userId == -1) {
                JOptionPane.showMessageDialog(returnBookCard,
                        "Student not found: " + memberUsername, "Error", JOptionPane.ERROR_MESSAGE);
                conn.rollback();
                return;
            }

            int transactionId = -1;
            int bookId        = -1;
            Date dueDate      = null;

            String tranSql = "SELECT t.transaction_id, t.book_id, t.due_date " +
                    "FROM Transactions t " +
                    "JOIN Books b ON t.book_id = b.book_id " +
                    "WHERE t.user_id = ? AND t.status = 'ISSUED' " +
                    "AND (LOWER(b.title) LIKE ? OR b.isbn = ?)";
            try (PreparedStatement ps = conn.prepareStatement(tranSql)) {
                ps.setInt(1, userId);
                ps.setString(2, "%" + bookQuery.toLowerCase() + "%");
                ps.setString(3, bookQuery);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    transactionId = rs.getInt("transaction_id");
                    bookId        = rs.getInt("book_id");
                    dueDate       = rs.getDate("due_date");
                }
            }
            if (transactionId == -1) {
                JOptionPane.showMessageDialog(returnBookCard,
                        "No active issue found for this member + book combination.",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
                conn.rollback();
                return;
            }

            Date today = Date.valueOf(java.time.LocalDate.now());

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Transactions SET status = 'RETURNED', return_date = ? WHERE transaction_id = ?")) {
                ps.setDate(1, today);
                ps.setInt(2, transactionId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Books SET available_copies = available_copies + 1 WHERE book_id = ?")) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            long overdueDays = 0;
            double fineAmount = 0;
            if (today.after(dueDate)) {
                overdueDays = (today.getTime() - dueDate.getTime()) / (1000L * 60 * 60 * 24);
                fineAmount = overdueDays * 5.0;
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO Fines (transaction_id, user_id, fine_amount, payment_status) VALUES (?, ?, ?, 'UNPAID')")) {
                    ps.setInt(1, transactionId);
                    ps.setInt(2, userId);
                    ps.setDouble(3, fineAmount);
                    ps.executeUpdate();
                }
            }

            conn.commit();

            String msg = "<html><font color='green'>Book returned successfully!</font>";
            if (fineAmount > 0) {
                msg += "<br><font color='red'>Fine charged: Rs " + (int) fineAmount + " (" + (int)(overdueDays) + " days overdue)</font>";
            }
            msg += "</html>";
            JOptionPane.showMessageDialog(returnBookCard, msg, "Return", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(returnBookCard, "Database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        returnBookCard.add(Box.createVerticalGlue(), gbc);
    }
}