package library.management.system.ui;

import library.management.system.dto.BookTableDTO;
import library.management.system.model.User;
import library.management.system.service.BookService;
import library.management.system.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

/**
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
                return false;
            }
        };

        model.addColumn("Book title");
        model.addColumn("Author");
        model.addColumn("Book category");
        model.addColumn("Available copies");

        searchBooksTable = new JTable(model);

        JTableHeader header = searchBooksTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        searchBooksTable.setGridColor(Color.DARK_GRAY);
        searchBooksTable.setShowGrid(false);
        searchBooksTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        searchBooksTable.setRowHeight(35);

        getAllBooks();

        searchBooksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        scrollPane = new JScrollPane(searchBooksTable);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        searchBooksCard.add(scrollPane, gbc);
    }

    public void getAllBooks() {
        model.setRowCount(0);
        List<Book> books = bookService.getAllBooks();

        for (Book book : books) {
            model.addRow(new Object[] {book.getTitle(), book.getAuthor(), book.getCategory(), book.getAvailableCopies()});
        }

        if (scrollPane != null) {
            scrollPane.getViewport().setBackground(new Color(0x212020));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
        }

        int rowHeight = searchBooksTable.getRowHeight();
        int noOfRows = Math.max(searchBooksTable.getRowCount(), 1);
        searchBooksTable.setPreferredScrollableViewportSize(new Dimension(
                searchBooksTable.getPreferredSize().width,
                rowHeight * noOfRows
        ));
    }

    private void getSearchedBooks(String keyword) {
        model.setRowCount(0);
        List<BookTableDTO> books;
        try {
            books = bookService.searchBooks(keyword);

            for (BookTableDTO book : books) {
                model.addRow(new Object[] {book.getTitle(), book.getAuthor(), book.getCategory(), book.getAvailableDisplay()});
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(searchBooksCard, "No books found for \"" + keyword + "\"",
                        "Search", JOptionPane.INFORMATION_MESSAGE);
            }

            if (scrollPane != null) {
                scrollPane.getViewport().setBackground(new Color(0x212020));
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
            }
        } catch (RuntimeException e) {
            if (scrollPane != null) {
                scrollPane.getViewport().setBackground(new Color(0x212020));
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
            }

            JOptionPane.showMessageDialog(searchBooksCard, e.getMessage(), "Validation", JOptionPane.INFORMATION_MESSAGE);
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
            String keyword = searchBox.getText().strip();

            getSearchedBooks(keyword);
        }
    }
}