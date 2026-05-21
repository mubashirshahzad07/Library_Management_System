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

        int overdueBooks = transactionService.getTotalOverdueBooksCount(user.getUserId());
        int booksCurrentlyBorrowed = transactionService.getCurrentlyBorrowedBooksCount(user.getUserId());

        if (overdueBooks > 0) {
            booksReturnInformation.setForeground(Color.WHITE);
            booksReturnInformation.setBackground(new Color(0xF59E0B));
            booksReturnInformation.setText(overdueBooks + " book(s) overdue" +  " ⎯ please return as soon as possible.");
        } else if (booksCurrentlyBorrowed > 0) {
            booksReturnInformation.setBackground(new Color(0xF0E526));
            booksReturnInformation.setText(booksCurrentlyBorrowed + " book(s) borrowed" +  " ⎯ please return on time.");
        } else {
            booksReturnInformation.setBackground(new Color(0x29CF45));
            booksReturnInformation.setText("No books currently borrowed.");
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

        int statusColumn = 3;

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

                if (column == statusColumn) {
                    String status = table.getValueAt(row, column) != null
                            ? table.getValueAt(row, column).toString() : "";
                    label.setBackground(status.equals("Overdue")
                            ? new Color(0xB82323) : new Color(0x309912));
                    label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 16));
                }

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