package library.management.system.ui;

import library.management.system.dto.BookTableDTO;
import library.management.system.model.Book;
import library.management.system.service.BookService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;


 // Handles the Manage Books panel for Admin with Live Search, Deactivation, and Restoration
 
public class AdminFrameManageBooksCard implements ActionListener {

    private final JPanel manageBooksCard;

    private JTextField titleField, authorField, isbnField, totalCopiesField, searchField;
    private JComboBox<String> categoryDropdown;
    private JButton addBookButton, deactivateBookButton, restoreBookButton;
    private DefaultTableModel model;
    private JTable booksTable;

    // Backend service integration:
    private BookService bookService;

    public AdminFrameManageBooksCard(JPanel manageBooksCard) {
        this.manageBooksCard = manageBooksCard;
        this.bookService = new BookService();
        
        this.addCardHeading();
        this.addInputForm();
        this.addSearchField();
        this.addBooksTable();
        this.addVerticalFiller();
        
        // Load data from the database
        this.loadTableData();
    }

    // heading

    private void addCardHeading() {
        JLabel headingLabel = new JLabel("Manage Books");
        headingLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE);

        JLabel libraryIcon = new JLabel();

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx  = 0;
        gbc.gridy  = 0;
        gbc.insets = new Insets(15, 10, 5, 10); 
        manageBooksCard.add(headingLabel, gbc);

        gbc.gridx  = 1;
        gbc.gridy  = 0;
        gbc.insets = new Insets(15, 340, 5, 10);
        manageBooksCard.add(libraryIcon, gbc);
    }

    // input

    private void addInputForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(0x2E2D2D));

        GridBagConstraints gbc = new GridBagConstraints();

        // row Labels: Book Title & ISBN 
        JLabel titleLabel = new JLabel("Book Title");
        titleLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 13));
        titleLabel.setForeground(Color.WHITE);

        gbc.gridx     = 0;
        gbc.gridy     = 0;
        gbc.gridwidth = 1;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.weightx   = 0.6;
        gbc.anchor    = GridBagConstraints.WEST;
        gbc.insets    = new Insets(5, 10, 0, 5);
        formPanel.add(titleLabel, gbc);

        JLabel isbnLabel = new JLabel("ISBN");
        isbnLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 13));
        isbnLabel.setForeground(Color.WHITE);

        gbc.gridx     = 1;
        gbc.gridy     = 0;
        gbc.weightx   = 0.4;
        gbc.insets    = new Insets(5, 5, 0, 10);
        formPanel.add(isbnLabel, gbc);

        // row1 Fields: Book Title & ISBN 
        titleField = new JTextField();
        titleField.setBackground(Color.DARK_GRAY);
        titleField.setForeground(Color.WHITE);
        titleField.setCaretColor(Color.WHITE);
        titleField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        titleField.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        titleField.setPreferredSize(new Dimension(100, 30));

        gbc.gridx     = 0;
        gbc.gridy     = 1;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(0, 10, 5, 5);
        formPanel.add(titleField, gbc);

        isbnField = new JTextField();
        isbnField.setBackground(Color.DARK_GRAY);
        isbnField.setForeground(Color.WHITE);
        isbnField.setCaretColor(Color.WHITE);
        isbnField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        isbnField.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        isbnField.setPreferredSize(new Dimension(100, 30));

        gbc.gridx     = 1;
        gbc.gridy     = 1;
        gbc.insets    = new Insets(0, 5, 5, 10);
        formPanel.add(isbnField, gbc);

        // row 2 Labels: Author, Total Copies, & Category:
        JLabel authorLabel = new JLabel("Author");
        authorLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 13));
        authorLabel.setForeground(Color.WHITE);

        gbc.gridx     = 0;
        gbc.gridy     = 2;
        gbc.weightx   = 0.4;
        gbc.insets    = new Insets(5, 10, 0, 5);
        formPanel.add(authorLabel, gbc);

        JLabel copiesLabel = new JLabel("Total Copies");
        copiesLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 13));
        copiesLabel.setForeground(Color.WHITE);

        gbc.gridx     = 1;
        gbc.gridy     = 2;
        gbc.weightx   = 0.3;
        copiesLabel.setToolTipText("");
        copiesLabel.setInheritsPopupMenu(false);
        gbc.insets    = new Insets(5, 5, 0, 5);
        formPanel.add(copiesLabel, gbc);

        // ow 3 Fields: Author, Total Copies, & Category:
        
        authorField = new JTextField();
        authorField.setBackground(Color.DARK_GRAY);
        authorField.setForeground(Color.WHITE);
        authorField.setCaretColor(Color.WHITE);
        authorField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        authorField.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        authorField.setPreferredSize(new Dimension(100, 30));

        gbc.gridx     = 0;
        gbc.gridy     = 3;
        gbc.insets    = new Insets(0, 10, 5, 5);
        formPanel.add(authorField, gbc);

        totalCopiesField = new JTextField();
        totalCopiesField.setBackground(Color.DARK_GRAY);
        totalCopiesField.setForeground(Color.WHITE);
        totalCopiesField.setCaretColor(Color.WHITE);
        totalCopiesField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        totalCopiesField.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        totalCopiesField.setPreferredSize(new Dimension(100, 30));

        gbc.gridx     = 1;
        gbc.gridy     = 3;
        gbc.insets    = new Insets(0, 5, 5, 5);
        formPanel.add(totalCopiesField, gbc);

        categoryDropdown = new JComboBox<>(new String[]{
                "Fiction", "Science", "Technology", "Mathematics", "History",
                "Literature", "Philosophy", "Economics", "Arts", "Other"
        });
        categoryDropdown.setBackground(Color.DARK_GRAY);
        categoryDropdown.setForeground(Color.WHITE);
        categoryDropdown.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        categoryDropdown.setFocusable(false);
        categoryDropdown.setPreferredSize(new Dimension(100, 30));

        gbc.gridx     = 2;
        gbc.gridy     = 3;
        gbc.insets    = new Insets(0, 5, 5, 10);
        formPanel.add(categoryDropdown, gbc);

        // buttons:
        addBookButton = new JButton("Add Book");
        addBookButton.setBackground(new Color(0x309912));
        addBookButton.setForeground(Color.WHITE);
        addBookButton.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        addBookButton.setFocusable(false);
        addBookButton.setPreferredSize(new Dimension(120, 30));
        addBookButton.addActionListener(this);

        deactivateBookButton = new JButton("Deactivate Book");
        deactivateBookButton.setBackground(new Color(0xB82323));
        deactivateBookButton.setForeground(Color.WHITE);
        deactivateBookButton.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        deactivateBookButton.setFocusable(false);
        deactivateBookButton.setPreferredSize(new Dimension(160, 30));
        deactivateBookButton.addActionListener(this);

        restoreBookButton = new JButton("Restore Book");
        restoreBookButton.setBackground(new Color(0x1B6CA8));
        restoreBookButton.setForeground(Color.WHITE);
        restoreBookButton.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        restoreBookButton.setFocusable(false);
        restoreBookButton.setPreferredSize(new Dimension(140, 30));
        restoreBookButton.addActionListener(this);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setBackground(new Color(0x2E2D2D));
        buttonsPanel.add(addBookButton);
        buttonsPanel.add(deactivateBookButton);
        buttonsPanel.add(restoreBookButton);

        gbc.gridx     = 0;
        gbc.gridy     = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;
        gbc.insets    = new Insets(10, 10, 5, 10);
        formPanel.add(buttonsPanel, gbc);

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.gridx     = 0;
        cardGbc.gridy     = 1;
        cardGbc.gridwidth = GridBagConstraints.REMAINDER;
        cardGbc.fill      = GridBagConstraints.HORIZONTAL;
        cardGbc.insets    = new Insets(5, 10, 5, 10);
        manageBooksCard.add(formPanel, cardGbc);
    }

    // ── SEARCH FIELD ──────────────────────────────────────────────────────────

    private void addSearchField() {
        searchField = new JTextField();
        searchField.setBackground(Color.DARK_GRAY);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 15));
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchField.setPreferredSize(new Dimension(100, 32));

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { executeBackendSearch(); }
            public void removeUpdate(DocumentEvent e)  { executeBackendSearch(); }
            public void changedUpdate(DocumentEvent e) { executeBackendSearch(); }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5, 10, 5, 10);
        manageBooksCard.add(searchField, gbc);
    }

    // book table:

    private void addBooksTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Column header modified: made it bk id :
        model.addColumn("Bk ID"); 
        model.addColumn("ISBN");
        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("Category");
        model.addColumn("Total Copies");
        model.addColumn("Available Copies");

        booksTable = new JTable(model);
        booksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        booksTable.getColumnModel().getColumn(0).setPreferredWidth(75);
        booksTable.getColumnModel().getColumn(1).setPreferredWidth(110);
        booksTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        booksTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        booksTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        booksTable.getColumnModel().getColumn(5).setPreferredWidth(130);
        booksTable.getColumnModel().getColumn(6).setPreferredWidth(170);

        booksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = booksTable.getSelectedRow();
                if (selectedRow != -1) {
                    titleField.setText(model.getValueAt(selectedRow, 2).toString());
                    authorField.setText(model.getValueAt(selectedRow, 3).toString());
                    isbnField.setText(model.getValueAt(selectedRow, 1).toString());
                    totalCopiesField.setText(model.getValueAt(selectedRow, 5).toString());
                    categoryDropdown.setSelectedItem(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        JTableHeader header = booksTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 15));

        booksTable.setGridColor(Color.DARK_GRAY);
        booksTable.setShowGrid(false);
        booksTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 13));
        booksTable.setRowHeight(30);

        booksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBackground(new Color(0x388A7C));
                label.setOpaque(true);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 13));

                String cellValue = value != null ? value.toString() : "";

                if (column == 6 && cellValue.equals("Unavailable")) {
                    label.setBackground(new Color(0xB82323)); 
                    label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
                    return label;
                }

                if (column == 6) { 
                    try {
                        int avail = Integer.parseInt(cellValue);
                        if (avail == 0) {
                            label.setBackground(new Color(0xB82323)); 
                            label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
                        } else {
                            label.setBackground(new Color(0x309912)); 
                            label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
                        }
                    } catch (NumberFormatException ignored) {}
                }

                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.getViewport().setBackground(new Color(0x212020));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.weightx   = 1.0;
        gbc.weighty   = 1.0;
        gbc.insets    = new Insets(5, 10, 10, 10);
        manageBooksCard.add(scrollPane, gbc);
    }

    // database load:

    private void loadTableData() {
        try {
            // Passing empty string loads everything via DAO
            List<BookTableDTO> books = bookService.searchBooks("");
            populateTable(books);
        } catch (RuntimeException e) {
            model.setRowCount(0);
        }
    }

    //vertical filler:

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 4;
        gbc.weighty = 0.0;
        gbc.fill    = GridBagConstraints.VERTICAL;
        manageBooksCard.add(Box.createVerticalGlue(), gbc);
    }

    // ── BACKEND LIVE SEARCH QUERY HANDLING ────────────────────────────────────

    private void executeBackendSearch() {
        String keyword = searchField.getText().strip();
        try {
            if (keyword.isEmpty()) {
                loadTableData();
                return;
            }

            // Check if user is typing an explicit formatting pattern like "BK-001"
            String parsedKeyword = keyword;
            if (parsedKeyword.toUpperCase().matches("^BK-\\d+")) {
                parsedKeyword = parsedKeyword.substring(3).replaceFirst("^0+", "");
            }

            // Fetch absolute list to run our direct matches against IDs or ISBN strings
            List<BookTableDTO> allRecords = bookService.searchBooks("");
            List<BookTableDTO> results = new ArrayList<>();

            for (BookTableDTO b : allRecords) {
                String idStr = String.valueOf(b.getBookId());
                String isbnStr = b.getIsbn() != null ? b.getIsbn().toLowerCase() : "";
                String titleStr = b.getTitle() != null ? b.getTitle().toLowerCase() : "";
                String authorStr = b.getAuthor() != null ? b.getAuthor().toLowerCase() : "";
                String catStr = b.getCategory() != null ? b.getCategory().toLowerCase() : "";
                String lowerQuery = keyword.toLowerCase();

                // Match against raw internal ID number or alphanumeric padded formatting or explicit details
                if (idStr.equals(parsedKeyword) || 
                    isbnStr.contains(lowerQuery) || 
                    titleStr.contains(lowerQuery) || 
                    authorStr.contains(lowerQuery) || 
                    catStr.contains(lowerQuery)) {
                    results.add(b);
                }
            }
            populateTable(results);

        } catch (RuntimeException e) {
            model.setRowCount(0);
        }
    }

    private void populateTable(List<BookTableDTO> books) {
        model.setRowCount(0);
        for (BookTableDTO b : books) {
            String formattedBookId = "BK-" + String.format("%03d", b.getBookId());
            model.addRow(new Object[]{
                    formattedBookId,
                    b.getIsbn(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getCategory(),
                    b.getTotalCopies(),
                    b.getAvailableDisplay()
            });
        }
    }

    // ── CLEAR FIELDS HELPER ───────────────────────────────────────────────────

    private void clearInputFields() {
        titleField.setText("");
        isbnField.setText("");
        authorField.setText("");
        totalCopiesField.setText("");
        categoryDropdown.setSelectedIndex(0);
        searchField.setText("");
    }

    // Action handler

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addBookButton) {
            String title     = titleField.getText().strip();
            String isbn      = isbnField.getText().strip();
            String author    = authorField.getText().strip();
            String category  = (String) categoryDropdown.getSelectedItem();
            String copiesStr = totalCopiesField.getText().strip();

            if (title.isEmpty() || isbn.isEmpty() || author.isEmpty() || copiesStr.isEmpty()) {
                JOptionPane.showMessageDialog(manageBooksCard,
                        "Please fill in all layout fields before adding a book.",
                        "Missing Fields",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int totalCopies;
            try {
                totalCopies = Integer.parseInt(copiesStr);
                if (totalCopies < 1) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(manageBooksCard,
                        "Total Copies must be a valid positive number.",
                       
                        "Invalid Quantity",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Book newBook = new Book(0, isbn, title, author, category, totalCopies, totalCopies);

            try {
                if (bookService.addBook(newBook)) {
                    JOptionPane.showMessageDialog(manageBooksCard, 
                            "Book successfully added to the database.", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTableData();
                    clearInputFields();
                }
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(manageBooksCard,
                        ex.getMessage(),
                        "Error Adding Book",
JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == deactivateBookButton) {
            int selectedRow = booksTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(manageBooksCard,
                        "Please select a book from the table to deactivate.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (model.getValueAt(selectedRow, 6).toString().equals("Unavailable")) {
                JOptionPane.showMessageDialog(manageBooksCard,
                        "This book is already deactivated.",
                        "Already Deactivated",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            
            String rawIdStr = model.getValueAt(selectedRow, 0).toString().replaceAll("[^0-9]", "");
            int bookId = Integer.parseInt(rawIdStr);
            String title = model.getValueAt(selectedRow, 2).toString();

            int confirm = JOptionPane.showConfirmDialog(manageBooksCard,
                    "Deactivate \"" + title + "\" (ID: " + bookId + ") from the inventory?",
                    "Confirm Deactivation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (bookService.removeBook(bookId)) {
                        JOptionPane.showMessageDialog(manageBooksCard, 
                                "Book deactivated successfully.", 
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearInputFields();
                        loadTableData();
                    }
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(manageBooksCard,
                            ex.getMessage(),
                            "Cannot Deactivate",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        if (e.getSource() == restoreBookButton) {
            int selectedRow = booksTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(manageBooksCard,
                        "Please select a book from the table to reactivate.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!model.getValueAt(selectedRow, 6).toString().equals("Unavailable")) {
                JOptionPane.showMessageDialog(manageBooksCard,
                        "This book is already active.",
                        "Already Active",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            
            String rawIdStr = model.getValueAt(selectedRow, 0).toString().replaceAll("[^0-9]", "");
            int bookId = Integer.parseInt(rawIdStr);
            String title = model.getValueAt(selectedRow, 2).toString();

            int confirm = JOptionPane.showConfirmDialog(manageBooksCard,
                    "Reactivate \"" + title + "\" (ID: " + bookId + ") back into inventory tracking?",
                    "Confirm Restoration",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (bookService.restoreBook(bookId)) {
                        JOptionPane.showMessageDialog(manageBooksCard, 
                                "Book reactivated and inventory restored safely.", 
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearInputFields();
                        loadTableData();
                    }
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(manageBooksCard,
                            ex.getMessage(),
                            "Cannot Reactivate",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
