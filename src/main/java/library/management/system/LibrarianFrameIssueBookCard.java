package library.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * @since 04 May 2026
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

        JLabel dashboardStudentIcon = new JLabel(new ImageIcon(ClassLoader.getSystemResource("librarian_label.png")));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(30, 25, 30, 30);
        issueBookCard.add(issueBookLabel, gridBagConstraints);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(30, 15, 20, 25);
        issueBookCard.add(dashboardStudentIcon, gridBagConstraints);
    }

    private void addIssueBookPanel() {
        JPanel issueBookPanel = new JPanel();
        issueBookPanel.setLayout(new GridBagLayout());
        issueBookPanel.setBackground(new Color(0x2E2D2D));

        // member id/ name
        JLabel memberIdLabel = new JLabel("Member ID/Name");
        memberIdLabel.setForeground(Color.WHITE);

        JTextField memberIdBox = new JTextField();

        // book title/ isbn
        JLabel bookTitleLabel = new JLabel("Book title/ISBN");
        bookTitleLabel.setForeground(Color.WHITE);


        JTextField bookTitleBox = new JTextField();

        // issue date
        JLabel issueDateLabel = new JLabel("Issue date");
        issueDateLabel.setForeground(Color.WHITE);

        JTextField issueDateBox = new JTextField();

        // due date
        JLabel dueDateLabel = new JLabel("Due date");
        dueDateLabel.setForeground(Color.WHITE);

        JTextField dueDateBox = new JTextField();

        // issue book
        JButton issueBook = new JButton("Issue book");
        issueBook.setForeground(Color.WHITE);

        GridBagConstraints gbcIssueBook = new GridBagConstraints();

        gbcIssueBook.gridx = 0;
        gbcIssueBook.gridy = GridBagConstraints.RELATIVE;
        gbcIssueBook.gridwidth = GridBagConstraints.REMAINDER;
        gbcIssueBook.insets = new Insets(10, 10, 10, 10);
        issueBookCard.add(memberIdLabel, gbcIssueBook);
        issueBookCard.add(memberIdBox, gbcIssueBook);
        issueBookCard.add(bookTitleLabel, gbcIssueBook);
        issueBookCard.add(bookTitleBox, gbcIssueBook);
        issueBookCard.add(issueDateLabel, gbcIssueBook);
        issueBookCard.add(issueDateBox, gbcIssueBook);
        issueBookCard.add(dueDateLabel, gbcIssueBook);
        issueBookCard.add(dueDateBox, gbcIssueBook);
        issueBookCard.add(issueBook, gbcIssueBook);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.REMAINDER;
        gbc.gridy = 1;
        gbc.insets = new Insets(30, 15, 20, 25);
        issueBookCard.add(issueBookPanel, gbc);
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
        issueBookCard.add(Box.createVerticalGlue(), gbc);
    }
}
