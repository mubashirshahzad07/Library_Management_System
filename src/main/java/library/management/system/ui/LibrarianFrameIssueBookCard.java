package library.management.system.ui;

import library.management.system.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * @since 16 May 2026
 * Handles the issue book window
 */
public class LibrarianFrameIssueBookCard {
    private final JPanel issueBookCard;

    LibrarianFrameIssueBookCard(JPanel issueBookCard) {
        this.issueBookCard = issueBookCard;
        this.addCardHeader();
        this.addIssueBookPanel();
        this.addVerticalFiller();
    }

    private void addCardHeader() {
        JLabel issueBookLabel = new JLabel("Issue book");
        issueBookLabel.setForeground(Color.WHITE);
        issueBookLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));

        JLabel libraryIcon = new JLabel(new ImageIcon(ClassLoader.getSystemResource("librarian_label.png")));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(30, 25, 30, 300);
        issueBookCard.add(issueBookLabel, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(30, 300, 20, 25);
        issueBookCard.add(libraryIcon, gridBagConstraints);
    }

    private void addIssueBookPanel() {
        JPanel issueBookPanel = new JPanel();
        issueBookPanel.setLayout(new GridBagLayout());
        issueBookPanel.setBackground(new Color(0x2E2D2D));

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

        // issue date
        JLabel issueDateLabel = new JLabel("Issue date");
        issueDateLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        issueDateLabel.setForeground(Color.WHITE);

        JTextField issueDateBox = new JTextField();
        issueDateBox.setBackground(Color.DARK_GRAY);
        issueDateBox.setForeground(Color.WHITE);
        issueDateBox.setCaretColor(Color.WHITE);
        issueDateBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        issueDateBox.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        issueDateBox.setPreferredSize(new Dimension(50, 35));
        issueDateBox.setText(java.time.LocalDate.now().toString());

        // due date
        JLabel dueDateLabel = new JLabel("Due date     ");
        dueDateLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        dueDateLabel.setForeground(Color.WHITE);

        JTextField dueDateBox = new JTextField();
        dueDateBox.setBackground(Color.DARK_GRAY);
        dueDateBox.setForeground(Color.WHITE);
        dueDateBox.setCaretColor(Color.WHITE);
        dueDateBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        dueDateBox.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        dueDateBox.setPreferredSize(new Dimension(50, 35));
        dueDateBox.setText(java.time.LocalDate.now().plusDays(14).toString());

        // issue book button
        JButton issueBookButton = new JButton("Issue book");
        issueBookButton.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        issueBookButton.setForeground(Color.WHITE);
        issueBookButton.setBackground(Color.DARK_GRAY);
        issueBookButton.setFocusable(false);

        issueBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberUsername = memberIdBox.getText().strip();
                String bookQuery = bookTitleBox.getText().strip();
                String issueDateStr = issueDateBox.getText().strip();
                String dueDateStr = dueDateBox.getText().strip();

                if (memberUsername.isEmpty() || bookQuery.isEmpty() || issueDateStr.isEmpty() || dueDateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(issueBookCard, "All fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    Date issueDate = Date.valueOf(issueDateStr);
                    Date dueDate = Date.valueOf(dueDateStr);

                    if (dueDate.before(issueDate)) {
                        JOptionPane.showMessageDialog(issueBookCard, "Due date cannot be before issue date.", "Validation", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    issueBook(memberUsername, bookQuery, issueDate, dueDate);

                    memberIdBox.setText("");
                    bookTitleBox.setText("");
                    issueDateBox.setText(java.time.LocalDate.now().toString());
                    dueDateBox.setText(java.time.LocalDate.now().plusDays(14).toString());

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(issueBookCard,
                            "Invalid date format. Use YYYY-MM-DD.", "Validation", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        GridBagConstraints gbcIssueBook;

        gbcIssueBook = new GridBagConstraints();
        gbcIssueBook.gridx = 0;
        gbcIssueBook.gridy = 0;
        gbcIssueBook.anchor = GridBagConstraints.WEST;
        gbcIssueBook.insets = new Insets(10, 10, 1, 10);
        issueBookPanel.add(memberIdLabel, gbcIssueBook);

        gbcIssueBook = new GridBagConstraints();
        gbcIssueBook.gridx = 0;
        gbcIssueBook.gridy = 1;
        gbcIssueBook.gridwidth = GridBagConstraints.REMAINDER;
        gbcIssueBook.fill = GridBagConstraints.HORIZONTAL;
        gbcIssueBook.anchor = GridBagConstraints.WEST;
        gbcIssueBook.insets = new Insets(1, 10, 10, 10);
        issueBookPanel.add(memberIdBox, gbcIssueBook);

        gbcIssueBook = new GridBagConstraints();
        gbcIssueBook.gridx = 0;
        gbcIssueBook.gridy = 2;
        gbcIssueBook.anchor = GridBagConstraints.WEST;
        gbcIssueBook.insets = new Insets(10, 10, 1, 10);
        issueBookPanel.add(bookTitleLabel, gbcIssueBook);

        gbcIssueBook = new GridBagConstraints();
        gbcIssueBook.gridx = 0;
        gbcIssueBook.gridy = 3;
        gbcIssueBook.gridwidth = GridBagConstraints.REMAINDER;
        gbcIssueBook.fill = GridBagConstraints.HORIZONTAL;
        gbcIssueBook.anchor = GridBagConstraints.WEST;
        gbcIssueBook.insets = new Insets(1, 10, 10, 10);
        issueBookPanel.add(bookTitleBox, gbcIssueBook);

        gbcIssueBook = new GridBagConstraints();
        gbcIssueBook.gridx = 0;
        gbcIssueBook.gridy = 4;
        gbcIssueBook.anchor = GridBagConstraints.WEST;
        gbcIssueBook.insets = new Insets(10, 10, 1, 10);
        issueBookPanel.add(issueDateLabel, gbcIssueBook);

        gbcIssueBook = new GridBagConstraints();
        gbcIssueBook.gridx = 1;
        gbcIssueBook.gridy = 4;
        gbcIssueBook.anchor = GridBagConstraints.WEST;
        gbcIssueBook.insets = new Insets(10, 10, 1, 10);
        issueBookPanel.add(dueDateLabel, gbcIssueBook);

        gbcIssueBook = new GridBagConstraints();
        gbcIssueBook.gridx = 0;
        gbcIssueBook.gridy = 5;
        gbcIssueBook.fill = GridBagConstraints.HORIZONTAL;
        gbcIssueBook.weightx = 0.47;
        gbcIssueBook.anchor = GridBagConstraints.WEST;
        gbcIssueBook.insets = new Insets(1, 10, 10, 5);
        issueBookPanel.add(issueDateBox, gbcIssueBook);

        gbcIssueBook = new GridBagConstraints();
        gbcIssueBook.gridx = 1;
        gbcIssueBook.gridy = 5;
        gbcIssueBook.fill = GridBagConstraints.HORIZONTAL;
        gbcIssueBook.weightx = 0.53;
        gbcIssueBook.anchor = GridBagConstraints.WEST;
        gbcIssueBook.insets = new Insets(1, 5, 10, 10);
        issueBookPanel.add(dueDateBox, gbcIssueBook);

        gbcIssueBook = new GridBagConstraints();
        gbcIssueBook.gridx = 0;
        gbcIssueBook.gridy = 6;
        gbcIssueBook.gridwidth = 2;
        gbcIssueBook.anchor = GridBagConstraints.CENTER;
        gbcIssueBook.insets = new Insets(10, 10, 10, 10);
        issueBookPanel.add(issueBookButton, gbcIssueBook);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 15, 20, 25);
        issueBookCard.add(issueBookPanel, gbc);
    }

    /**
     * Inserts a Transaction row and decrements available_copies on the book.
     * Accepts book title or ISBN as bookQuery.
     */
    private void issueBook(String memberUsername, String bookQuery, Date issueDate, Date dueDate) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Resolve student user_id
            int userId = -1;
            String userSql = "SELECT user_id FROM Users WHERE username = ? AND role = 'STUDENT' AND is_active = TRUE";
            try (PreparedStatement ps = conn.prepareStatement(userSql)) {
                ps.setString(1, memberUsername);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) userId = rs.getInt("user_id");
            }
            if (userId == -1) {
                JOptionPane.showMessageDialog(issueBookCard, "Student not found: " + memberUsername,
                        "Error", JOptionPane.ERROR_MESSAGE);
                conn.rollback();
                return;
            }

            int bookId = -1;
            String bookSql = "SELECT book_id, available_copies FROM Books WHERE (LOWER(title) LIKE ? OR isbn = ?) AND is_active = TRUE";
            try (PreparedStatement ps = conn.prepareStatement(bookSql)) {
                ps.setString(1, "%" + bookQuery.toLowerCase() + "%");
                ps.setString(2, bookQuery);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int available = rs.getInt("available_copies");
                    if (available <= 0) {
                        JOptionPane.showMessageDialog(issueBookCard,
                                "No copies available for: " + bookQuery,
                                "Unavailable", JOptionPane.WARNING_MESSAGE);
                        conn.rollback();
                        return;
                    }
                    bookId = rs.getInt("book_id");
                }
            }
            if (bookId == -1) {
                JOptionPane.showMessageDialog(issueBookCard, "Book not found: " + bookQuery,
                        "Error", JOptionPane.ERROR_MESSAGE);
                conn.rollback();
                return;
            }

            String dupSql = "SELECT 1 FROM Transactions WHERE user_id = ? AND book_id = ? AND status = 'ISSUED'";
            try (PreparedStatement ps = conn.prepareStatement(dupSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, bookId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(issueBookCard,
                            "This student already has this book issued.",
                            "Duplicate", JOptionPane.WARNING_MESSAGE);
                    conn.rollback();
                    return;
                }
            }

            String tranSql = "INSERT INTO Transactions (user_id, book_id, issue_date, due_date, status) VALUES (?, ?, ?, ?, 'ISSUED')";
            try (PreparedStatement ps = conn.prepareStatement(tranSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, bookId);
                ps.setDate(3, issueDate);
                ps.setDate(4, dueDate);
                ps.executeUpdate();
            }

            String updateBook = "UPDATE Books SET available_copies = available_copies - 1 WHERE book_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateBook)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(issueBookCard,
                    "<html><font color='green'>Book issued successfully!</font></html>",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(issueBookCard, "Database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        issueBookCard.add(Box.createVerticalGlue(), gbc);
    }
}