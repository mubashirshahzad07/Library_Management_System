package library.management.system.ui;

import library.management.system.model.Book;
import library.management.system.model.User;
import library.management.system.service.BookService;
import library.management.system.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

/**
 * @since 15 May 2026
 * Handles how students can search for and borrow books
 */
public class StudentFrameSearchBooksCard implements ActionListener {
    private final JPanel searchBooksCard;
    private JButton searchButton;
    private JTextField searchBox;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JTable searchBooksTable;

    private final BookService bookService = new BookService();
    private final int userId;

    public StudentFrameSearchBooksCard(JPanel searchBooksCard, User user) {
        this.searchBooksCard = searchBooksCard;
        this.userId = user.getUserId();
        this.addCardHeading();
        this.addSearchBox();
        this.addSearchButton();
        this.addSearchTable();
        this.addVerticalFiller();
    }

    private void addCardHeading() {
        JLabel searchBooksLabel = new JLabel("Search books");
        searchBooksLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        searchBooksLabel.setForeground(Color.WHITE);
        JLabel studentLabel = new JLabel(new ImageIcon(ClassLoader.getSystemResource("student_label.png")));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 10, 5, 290);
        searchBooksCard.add(searchBooksLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 290, 5, 10);
        searchBooksCard.add(studentLabel, gbc);
    }

    private void addSearchBox() {
        searchBox = new JTextField();
        searchBox.setBackground(Color.DARK_GRAY);
        searchBox.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 17));
        searchBox.setForeground(Color.WHITE);
        searchBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchBox.setCaretColor(Color.WHITE);
        searchBox.setPreferredSize(new Dimension(100, 42));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        searchBooksCard.add(searchBox, gbc);
    }

    private void addSearchButton() {
        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0x294975));
        searchButton.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 17));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusable(false);
        searchButton.setPreferredSize(new Dimension(55, 42));
        searchButton.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        searchBooksCard.add(searchButton, gbc);
    }

    private void addSearchTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only the Borrow column is "editable" (used to trigger the button action)
                return column == 3 && !getValueAt(row, column).toString().equals("Borrowed");
            }
        };

        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("Available");
        model.addColumn("");

        int buttonColumnIndex = 3;

        searchBooksTable = new JTable(model);

        JTableHeader header = searchBooksTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        searchBooksTable.setGridColor(Color.DARK_GRAY);
        searchBooksTable.setShowGrid(false);
        searchBooksTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        searchBooksTable.setRowHeight(35);

        searchBooksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                if (column == buttonColumnIndex) {
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBackground(new Color(0x388A7C));
                label.setOpaque(true);
                label.setForeground(Color.WHITE);
                return label;
            }
        });

        searchBooksTable.getColumnModel().getColumn(buttonColumnIndex).setCellRenderer(
                new TableCellRenderer() {
                    private final JButton button = new JButton();
                    {
                        button.setOpaque(true);
                        button.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
                        button.setForeground(Color.WHITE);
                        button.setFocusPainted(false);
                        button.setBorderPainted(false);
                    }

                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row, int column) {
                        String text = value != null ? value.toString() : "Borrow";
                        button.setText(text);
                        button.setEnabled(!text.equals("Borrowed"));
                        button.setBackground(text.equals("Borrowed")
                                ? Color.DARK_GRAY : new Color(0x852832));
                        return button;
                    }
                }
        );

        searchBooksTable.getColumnModel().getColumn(buttonColumnIndex).setCellEditor(
                new DefaultCellEditor(new JCheckBox()) {
                    private final JButton button = new JButton("Borrow");
                    private int currentRow;

                    {
                        button.setOpaque(true);
                        button.setForeground(Color.WHITE);
                        button.setBackground(new Color(0x2E2D2D));
                        button.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
                        button.setFocusPainted(false);
                        button.setBorderPainted(false);

                        button.addActionListener(e -> {
                            fireEditingStopped();
                            String bookTitle = searchBooksTable.getValueAt(currentRow, 0).toString();
                            String author    = searchBooksTable.getValueAt(currentRow, 1).toString();
                            String available = searchBooksTable.getValueAt(currentRow, 2).toString();

                            if (available.equals("No")) {
                                JOptionPane.showMessageDialog(searchBooksCard,
                                        "No copies of \"" + bookTitle + "\" are available right now.",
                                        "Unavailable", JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            borrowBook(bookTitle, author, currentRow);
                        });
                    }

                    @Override
                    public Component getTableCellEditorComponent(JTable table, Object value,
                                                                 boolean isSelected, int row, int column) {
                        currentRow = row;
                        button.setText("Borrow");
                        return button;
                    }

                    @Override
                    public Object getCellEditorValue() {
                        return "Borrowed";
                    }
                }
        );

        scrollPane = new JScrollPane(searchBooksTable);
        scrollPane.getViewport().setBackground(new Color(0x212020));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        searchBooksCard.add(scrollPane, gbc);
    }

    /**
     * Issues the book to the logged-in student: inserts a Transaction (14-day loan)
     * and decrements available_copies. Updates the table row to "Borrowed" on success.
     */
    private void borrowBook(String bookTitle, String author, int tableRow) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Find book_id and confirm availability
            int bookId = -1;
            String bookSql = "SELECT book_id, available_copies FROM Books " +
                    "WHERE LOWER(title) = LOWER(?) AND LOWER(author) = LOWER(?) AND is_active = TRUE";
            try (PreparedStatement ps = conn.prepareStatement(bookSql)) {
                ps.setString(1, bookTitle);
                ps.setString(2, author);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int available = rs.getInt("available_copies");
                    if (available <= 0) {
                        JOptionPane.showMessageDialog(searchBooksCard,
                                "No copies of \"" + bookTitle + "\" are currently available.",
                                "Unavailable", JOptionPane.WARNING_MESSAGE);
                        conn.rollback();
                        return;
                    }
                    bookId = rs.getInt("book_id");
                }
            }
            if (bookId == -1) {
                JOptionPane.showMessageDialog(searchBooksCard,
                        "Book not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                conn.rollback();
                return;
            }

            // 2. Check student doesn't already have this book
            String dupSql = "SELECT 1 FROM Transactions WHERE user_id = ? AND book_id = ? AND status = 'ISSUED'";
            try (PreparedStatement ps = conn.prepareStatement(dupSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, bookId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(searchBooksCard,
                            "You already have \"" + bookTitle + "\" issued.",
                            "Duplicate", JOptionPane.WARNING_MESSAGE);
                    conn.rollback();
                    return;
                }
            }

            // 3. Insert transaction (14-day loan starting today)
            Date today   = Date.valueOf(java.time.LocalDate.now());
            Date dueDate = Date.valueOf(java.time.LocalDate.now().plusDays(14));
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Transactions (user_id, book_id, issue_date, due_date, status) VALUES (?, ?, ?, ?, 'ISSUED')")) {
                ps.setInt(1, userId);
                ps.setInt(2, bookId);
                ps.setDate(3, today);
                ps.setDate(4, dueDate);
                ps.executeUpdate();
            }

            // 4. Decrement available_copies
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Books SET available_copies = available_copies - 1 WHERE book_id = ?")) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            conn.commit();

            // Update table row to reflect borrowed status
            model.setValueAt("Borrowed", tableRow, 3);
            model.setValueAt("No", tableRow, 2);

            JOptionPane.showMessageDialog(searchBooksCard,
                    "<html><font color='green'>\"" + bookTitle + "\" has been issued to you.</font><br>" +
                            "Due date: " + dueDate + "</html>",
                    "Issued", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(searchBooksCard, "Database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        searchBooksCard.add(Box.createVerticalGlue(), gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            model.setRowCount(0);
            scrollPane.getViewport().setBackground(new Color(0x212020));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            String keyword = searchBox.getText().strip();
            if (keyword.isEmpty()) {
                return;
            }

            try {
                List<Book> books = bookService.searchBooks(keyword);
                for (Book b : books) {
                    // Check if this student already has this specific book issued
                    boolean alreadyBorrowed = isAlreadyBorrowed(b.getBookId());
                    String buttonLabel = alreadyBorrowed ? "Borrowed" : "Borrow";
                    model.addRow(new Object[]{
                            b.getTitle(),
                            b.getAuthor(),
                            b.getAvailableCopies() > 0 ? "Yes" : "No",
                            buttonLabel
                    });
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(searchBooksCard, ex.getMessage(),
                        "Search", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private boolean isAlreadyBorrowed(int bookId) {
        String sql = "SELECT 1 FROM Transactions WHERE user_id = ? AND book_id = ? AND status = 'ISSUED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            return ps.executeQuery().next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}