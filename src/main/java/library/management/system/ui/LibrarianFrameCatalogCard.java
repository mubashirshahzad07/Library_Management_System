package library.management.system.ui;

import library.management.system.model.Book;
import library.management.system.service.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @since 16 May 2026
 * Handles the catalog window for librarians
 */
public class LibrarianFrameCatalogCard implements ActionListener {
    private final JPanel catalogCard;
    private JButton searchButton;
    private JTextField searchBox;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JTable catalogTable;

    private final BookService bookService = new BookService();

    public LibrarianFrameCatalogCard(JPanel catalogCard) {
        this.catalogCard = catalogCard;
        this.addCardHeading();
        this.addSearchBox();
        this.addSearchButton();
        this.addCatalogTable();
        this.addVerticalFiller();
    }

    private void addCardHeading() {
        JLabel catalogLabel = new JLabel("Catalog");
        catalogLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        catalogLabel.setForeground(Color.WHITE);

        JLabel libraryIcon = new JLabel(new ImageIcon(ClassLoader.getSystemResource("librarian_label.png")));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 10, 5, 340);
        catalogCard.add(catalogLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 340, 5, 10);
        catalogCard.add(libraryIcon, gbc);
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
        catalogCard.add(searchBox, gbc);
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
        catalogCard.add(searchButton, gbc);
    }

    private void addCatalogTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("Copies");
        model.addColumn("Status");

        // Load all books on startup
        populateTable(null);

        catalogTable = new JTable(model);
        applyTableStyling();

        scrollPane = new JScrollPane(catalogTable);
        scrollPane.getViewport().setBackground(new Color(0x212020));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        catalogCard.add(scrollPane, gbc);
    }

    private void applyTableStyling() {
        JTableHeader header = catalogTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        catalogTable.setGridColor(Color.DARK_GRAY);
        catalogTable.setShowGrid(false);
        catalogTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        catalogTable.setRowHeight(35);

        int statusColumn = 3;
        catalogTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBackground(new Color(0x388A7C));
                label.setOpaque(true);
                label.setForeground(Color.WHITE);

                if (column == statusColumn) {
                    String status = value != null ? value.toString() : "";
                    label.setBackground(status.equals("Unavailable")
                            ? new Color(0xB82323) : new Color(0x309912));
                    label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 16));
                }
                return label;
            }
        });
    }

    private void populateTable(String keyword) {
        model.setRowCount(0);
        try {
            List<Book> books = (keyword == null || keyword.isEmpty())
                    ? bookService.getAllBooks()
                    : bookService.searchBooks(keyword);

            for (Book b : books) {
                String status = b.getAvailableCopies() > 0 ? "Available" : "Unavailable";
                model.addRow(new Object[]{b.getTitle(), b.getAuthor(), b.getAvailableCopies(), status});
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(catalogCard, e.getMessage(), "Search", JOptionPane.INFORMATION_MESSAGE);
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
        catalogCard.add(Box.createVerticalGlue(), gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String keyword = searchBox.getText().strip();
            populateTable(keyword);
        }
    }
}