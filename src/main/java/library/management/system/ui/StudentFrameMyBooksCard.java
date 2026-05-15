package library.management.system.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * @since 03 May 2026
 * Handles the books that are currently borrowed by Student
 */
public class StudentFrameMyBooksCard {
    private final JPanel myBooksCard;

    public StudentFrameMyBooksCard(JPanel myBooksCard) {
        this.myBooksCard = myBooksCard;
        this.addCardHeading();
        this.addBooksReturnInformation();
        this.addMyBooksTable();
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

        myBooksCard.add(booksReturnInformation, gbc);
    }

    private void addMyBooksTable() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Book");
        model.addColumn("Issued");
        model.addColumn("Due date");
        model.addColumn("Status");

        model.addRow(new Object[]{"Data Structures", "Mar 1", "Apr 25", "Due soon"});
        model.addRow(new Object[]{"Data Structures", "Mar 1", "Apr 25", "Due soon"});
        model.addRow(new Object[]{"Data Structures", "Mar 1", "Apr 25", "Issued"});

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
        int noOfRows = myBooksTable.getRowCount();
        myBooksTable.setPreferredScrollableViewportSize(new Dimension(
                myBooksTable.getPreferredSize().width,
                rowHeight * noOfRows
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        JScrollPane scrollPane = new JScrollPane(myBooksTable);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 25, 5, 25);
        myBooksCard.add(scrollPane, gbc);
    }

    /**
     * pushes all the elements to the top of the window
     */
    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        myBooksCard.add(Box.createVerticalGlue(), gbc);
    }
}