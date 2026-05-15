package library.management.system.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * @since 03 May 2026
 * Handles the dashboard of Student
 */
public class StudentFrameDashboardCard {
    private final JPanel dashboardCard;

    public StudentFrameDashboardCard(JPanel dashboardCard) {
        this.dashboardCard = dashboardCard;
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
        booksReturnInformation.setBackground(new Color(0xF0E526));
        booksReturnInformation.setText("Data Structures is due on Apr 25 ⎯ please return on time.");

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

        Integer noOfBooksBorrowed = 2; // Shumaail: run SQL query to fetch no of books borrowed
        JLabel noOfBooksBorrowedLabel = new JLabel(noOfBooksBorrowed.toString()); 
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

        Integer noOfBooksReturned = 8; // Shumaail: run SQL query to fetch no of books returned
        JLabel noOfBooksReturnedLabel = new JLabel(noOfBooksReturned.toString());
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

        Integer fines = 1;
        JLabel fineAmountLabel = new JLabel("Rs " + fines.toString()); // Shumaail: run SQL query to fetch amount of fines
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
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Book title");
        model.addColumn("Author");
        model.addColumn("Due date");
        model.addColumn("Status");

        // Shumaail: run SQL query to find books borrowed
        // add books using for loop or whatever best suits the situation
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "Apr 25", "Due soon"});
        model.addRow(new Object[]{"Java Programming", "Herbert Schildt", "May 5", "Issued"});
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "Apr 25", "Due soon"});
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "Apr 25", "Due soon"});
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "Apr 25", "Due soon"});
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "Apr 25", "Due soon"});

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
        int noOfRows = dashboardTable.getRowCount();
        dashboardTable.setPreferredScrollableViewportSize(new Dimension(
                dashboardTable.getPreferredSize().width,
                rowHeight * noOfRows
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        JScrollPane scrollPane = new JScrollPane(dashboardTable);
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
