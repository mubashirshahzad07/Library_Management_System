package library.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * @since 06 May 2026
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
        gridBagConstraints.insets = new Insets(30, 25, 30, 300);
        issueBookCard.add(issueBookLabel, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(30, 300, 20, 25);
        issueBookCard.add(dashboardStudentIcon, gridBagConstraints);
    }

    private void addIssueBookPanel() {
        JPanel issueBookPanel = new JPanel();
        issueBookPanel.setLayout(new GridBagLayout());
        issueBookPanel.setBackground(new Color(0x2E2D2D));

        // member id/ name
        JLabel memberIdLabel = new JLabel("Member ID/Name");
        memberIdLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        memberIdLabel.setForeground(Color.WHITE);

        JTextField memberIdBox = new JTextField();
        memberIdBox.setBackground(Color.DARK_GRAY);
        memberIdBox.setForeground(Color.WHITE);
        memberIdBox.setCaretColor(Color.WHITE);
        memberIdBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        memberIdBox.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        memberIdBox.setPreferredSize(new Dimension(100, 35));

        // book title/ isbn
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

        // issue book
        JButton issueBookButton = new JButton("Issue book");
        issueBookButton.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        issueBookButton.setForeground(Color.WHITE);
        issueBookButton.setBackground(Color.DARK_GRAY);
        issueBookButton.setForeground(Color.WHITE);
        issueBookButton.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        issueBookButton.setFocusable(false);

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
