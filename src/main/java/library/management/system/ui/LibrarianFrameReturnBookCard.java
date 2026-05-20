package library.management.system.ui;

import library.management.system.service.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Handles the return book window
 */
public class LibrarianFrameReturnBookCard {
    private final JPanel returnBookCard;
    private final TransactionService transactionService = new TransactionService();

    LibrarianFrameReturnBookCard(JPanel returnBookCard) {
        this.returnBookCard = returnBookCard;
        this.addCardHeader();
        this.addReturnBookPanel();
        this.addVerticalFiller();
    }

    private void addCardHeader() {
        JLabel returnBookLabel = new JLabel("Return book");
        returnBookLabel.setForeground(Color.WHITE);
        returnBookLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));

        JLabel libraryIcon = new JLabel(new ImageIcon(ClassLoader.getSystemResource("librarian_label.png")));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(30, 25, 30, 300);
        returnBookCard.add(returnBookLabel, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(30, 300, 20, 25);
        returnBookCard.add(libraryIcon, gridBagConstraints);
    }

    private void addReturnBookPanel() {
        JPanel returnBookPanel = new JPanel();
        returnBookPanel.setLayout(new GridBagLayout());
        returnBookPanel.setBackground(new Color(0x2E2D2D));

        // book title / isbn
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

        // member username
        JLabel memberIdLabel = new JLabel("Member Username");
        memberIdLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        memberIdLabel.setForeground(Color.WHITE);

        JTextField memberIdBox = new JTextField();
        memberIdBox.setBackground(Color.DARK_GRAY);
        memberIdBox.setForeground(Color.WHITE);
        memberIdBox.setCaretColor(Color.WHITE);
        memberIdBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        memberIdBox.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        memberIdBox.setPreferredSize(new Dimension(100, 35));

        // confirm return button
        JButton confirmReturnButton = new JButton("Confirm return");
        confirmReturnButton.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        confirmReturnButton.setForeground(Color.WHITE);
        confirmReturnButton.setBackground(Color.DARK_GRAY);
        confirmReturnButton.setFocusable(false);

        confirmReturnButton.addActionListener(e -> {
            String memberUsername = memberIdBox.getText().strip();
            String bookQuery      = bookTitleBox.getText().strip();

            if (memberUsername.isEmpty() || bookQuery.isEmpty()) {
                JOptionPane.showMessageDialog(returnBookCard,
                        "Both fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            returnBook(memberUsername, bookQuery);

            memberIdBox.setText("");
            bookTitleBox.setText("");
        });

        GridBagConstraints gbcReturnBook;

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 0;
        gbcReturnBook.anchor = GridBagConstraints.WEST;
        gbcReturnBook.insets = new Insets(10, 10, 1, 10);
        returnBookPanel.add(bookTitleLabel, gbcReturnBook);

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 1;
        gbcReturnBook.gridwidth = GridBagConstraints.REMAINDER;
        gbcReturnBook.fill = GridBagConstraints.HORIZONTAL;
        gbcReturnBook.weightx = 1.0;
        gbcReturnBook.anchor = GridBagConstraints.WEST;
        gbcReturnBook.insets = new Insets(1, 10, 10, 10);
        returnBookPanel.add(bookTitleBox, gbcReturnBook);

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 2;
        gbcReturnBook.anchor = GridBagConstraints.WEST;
        gbcReturnBook.insets = new Insets(10, 10, 1, 10);
        returnBookPanel.add(memberIdLabel, gbcReturnBook);

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 3;
        gbcReturnBook.gridwidth = GridBagConstraints.REMAINDER;
        gbcReturnBook.fill = GridBagConstraints.HORIZONTAL;
        gbcReturnBook.weightx = 1.0;
        gbcReturnBook.anchor = GridBagConstraints.WEST;
        gbcReturnBook.insets = new Insets(1, 10, 10, 10);
        returnBookPanel.add(memberIdBox, gbcReturnBook);

        gbcReturnBook = new GridBagConstraints();
        gbcReturnBook.gridx = 0;
        gbcReturnBook.gridy = 6;
        gbcReturnBook.gridwidth = 2;
        gbcReturnBook.anchor = GridBagConstraints.CENTER;
        gbcReturnBook.insets = new Insets(10, 10, 10, 10);
        returnBookPanel.add(confirmReturnButton, gbcReturnBook);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 15, 20, 25);
        returnBookCard.add(returnBookPanel, gbc);
    }

    private void returnBook(String username, String bookQuery) {
        try {
            transactionService.returnBook(username, bookQuery);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(returnBookCard, e.getMessage(), "Validation", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        returnBookCard.add(Box.createVerticalGlue(), gbc);
    }
}