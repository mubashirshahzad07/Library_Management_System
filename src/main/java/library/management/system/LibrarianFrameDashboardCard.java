package library.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * @since 04 May 2026
 * Handles the dashboard of Librarian
 */
public class LibrarianFrameDashboardCard {
    private final JPanel dashboardCard;

    public LibrarianFrameDashboardCard(JPanel dashboardCard) {
        this.dashboardCard = dashboardCard;
        this.addCardHeader();
        this.addBooksReturnInformation();
        this.addBooksIssuedToday();
        this.addReturnsToday();
        this.addOverdue();
        this.addCurrentlyIssuedLabel();
        this.addCurrentlyIssuedBooksTable();
        this.addVerticalFiller();
    }

    private void addCardHeader() {
        JLabel myDashboardLabel = new JLabel("Dashboard");
        myDashboardLabel.setForeground(Color.WHITE);
        myDashboardLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));

        JLabel libraryIcon = new JLabel(new ImageIcon(ClassLoader.getSystemResource("librarian_label.png")));

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
        dashboardCard.add(libraryIcon, gridBagConstraints);
    }

    private void addBooksReturnInformation() {
        JLabel booksReturnInformation = new JLabel();
        booksReturnInformation.setOpaque(true);
        booksReturnInformation.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 10));
        booksReturnInformation.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 15));
        booksReturnInformation.setBackground(new Color(0xF0E526));
        booksReturnInformation.setText("1 book(s) is overdue - follow up with members.");

        // if student has any overdue book
        // booksReturnInformation.setForeground(Color.WHITE);
        // booksReturnInformation.setBackground(new Color(0xF59E0B));
        // booksReturnInformation.setText(bookTitle + " is overdue since " + dueDate + " ⎯ please return the book immediately.");

        // else find the book with least due time
        // booksReturnInformation.setText(bookTitle + " is due on " + due date + " ⎯ please return on time.");

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

    private void addBooksIssuedToday() {
        JPanel booksIssuedToday = new JPanel();
        booksIssuedToday.setLayout(new GridBagLayout());
        booksIssuedToday.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 20));
        booksIssuedToday.setBackground(new Color(0x67ABD6));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel booksIssuedTodayLabel = new JLabel("Books issued today");
        booksIssuedTodayLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 18));
        booksIssuedTodayLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        booksIssuedToday.add(booksIssuedTodayLabel, gbc);

        Integer noOfBooksIssued = 2;
        JLabel noOfBooksBorrowedLabel = new JLabel(noOfBooksIssued.toString());
        noOfBooksBorrowedLabel.setFont(new Font("FiraMono NerdFont", Font.BOLD, 25));
        noOfBooksBorrowedLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        booksIssuedToday.add(noOfBooksBorrowedLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.20;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 25, 30, 5);
        dashboardCard.add(booksIssuedToday, gbc);
    }

    private void addReturnsToday() {
        JPanel booksReturnedToday = new JPanel();
        booksReturnedToday.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 20));
        booksReturnedToday.setLayout(new GridBagLayout());
        booksReturnedToday.setBackground(new Color(0x29CF45));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel booksReturnedLabel = new JLabel("Returns today");
        booksReturnedLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 18));
        booksReturnedLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        booksReturnedToday.add(booksReturnedLabel, gbc);

        Integer noOfBooksReturned = 8;
        JLabel noOfBooksReturnedLabel = new JLabel(noOfBooksReturned.toString());
        noOfBooksReturnedLabel.setFont(new Font("FiraMono NerdFont", Font.BOLD, 25));
        noOfBooksReturnedLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        booksReturnedToday.add(noOfBooksReturnedLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.43;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 30, 5);
        dashboardCard.add(booksReturnedToday, gbc);
    }

    private void addOverdue() {
        JPanel overdue = new JPanel();
        overdue.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 35));
        overdue.setLayout(new GridBagLayout());
        overdue.setBackground(new Color(0xCF2929));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel overdueLabel = new JLabel("Overdue ");
        overdueLabel.setForeground(Color.WHITE);
        overdueLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        overdue.add(overdueLabel, gbc);

        Integer overdueBooks = 1;
        JLabel overdueBooksLabel = new JLabel(overdueBooks.toString());
        overdueBooksLabel.setFont(new Font("FiraMono NerdFont", Font.BOLD, 25));
        overdueBooksLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        overdue.add(overdueBooksLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.37;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 30, 30);
        dashboardCard.add(overdue, gbc);
    }

    private void addCurrentlyIssuedLabel() {
        JLabel currentlyBorrowedLabel = new JLabel("Currently issued");
        currentlyBorrowedLabel.setForeground(Color.WHITE);
        currentlyBorrowedLabel.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 25, 10, 30);
        dashboardCard.add(currentlyBorrowedLabel, gbc);
    }

    private void addCurrentlyIssuedBooksTable() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Member");
        model.addColumn("Book");
        model.addColumn("Due date");
        model.addColumn("Status");

        model.addRow(new Object[]{"Dummy Borrower 1", "Data Structures", "Apr 25", "Overdue"});
        model.addRow(new Object[]{"Dummy Borrower 2", "Algorithms", "May 10", "Issued"});
        model.addRow(new Object[]{"Dummy Borrower 1", "Data Structures", "Apr 25", "Overdue"});
        model.addRow(new Object[]{"Dummy Borrower 1", "Data Structures", "Apr 25", "Overdue"});
        model.addRow(new Object[]{"Dummy Borrower 1", "Data Structures", "Apr 25", "Overdue"});
        model.addRow(new Object[]{"Dummy Borrower 2", "Algorithms", "May 10", "Issued"});
        model.addRow(new Object[]{"Dummy Borrower 2", "Algorithms", "May 10", "Issued"});
        model.addRow(new Object[]{"Dummy Borrower 2", "Algorithms", "May 10", "Issued"});

        JTable currentlyIssuedBooksTable = new JTable(model);
        JTableHeader header = currentlyIssuedBooksTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        currentlyIssuedBooksTable.setGridColor(Color.DARK_GRAY);
        currentlyIssuedBooksTable.setShowGrid(false);
        currentlyIssuedBooksTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        currentlyIssuedBooksTable.setRowHeight(25);

        currentlyIssuedBooksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        int rowHeight = currentlyIssuedBooksTable.getRowHeight();
        int noOfRows = currentlyIssuedBooksTable.getRowCount();
        currentlyIssuedBooksTable.setPreferredScrollableViewportSize(new Dimension(
                currentlyIssuedBooksTable.getPreferredSize().width,
                rowHeight * noOfRows
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        JScrollPane scrollPane = new JScrollPane(currentlyIssuedBooksTable);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 25, 30, 30);
        dashboardCard.add(scrollPane, gbc);
    }

    /**
     * pushes all the elements to the top of the window
     */
    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        dashboardCard.add(Box.createVerticalGlue(), gbc);
    }
}
