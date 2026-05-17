package library.management.system.ui;

import library.management.system.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @since 04 May 2026
 * Handles the Librarian Window
 */
public class LibrarianFrame extends JFrame implements ActionListener {
    private JButton dashBoard, issueBook, returnBook, catalog, members, signOut;
    private JPanel dashboardCard, issueBookCard, returnBookCard, catalogCard, membersCard;
    private JFrame loginFrame;

    LibrarianFrame(JFrame loginFrame, User user) {
        this.loginFrame = loginFrame;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        optionsPanel.setBackground(new Color(0x2E2D2D));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        ImageIcon librarianIcon = new ImageIcon(ClassLoader.getSystemResource("librarian_icon.png"));

        // Display the logged-in user's real name
        JLabel librarianLabel = new JLabel(user.getName());
        librarianLabel.setIcon(librarianIcon);
        librarianLabel.setForeground(Color.WHITE);
        librarianLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 18));
        gridBagConstraints.gridy = 0;
        optionsPanel.add(librarianLabel, gridBagConstraints);

        JLabel libraryLabel = new JLabel("LIBRARY");
        libraryLabel.setForeground(Color.WHITE);
        libraryLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 15));
        gridBagConstraints.gridy = 1;
        optionsPanel.add(libraryLabel, gridBagConstraints);

        dashBoard = new JButton("Dashboard");
        dashBoard.setBackground(Color.DARK_GRAY);
        dashBoard.setForeground(Color.WHITE);
        dashBoard.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
        dashBoard.setFocusable(false);
        gridBagConstraints.gridy = 2;
        optionsPanel.add(dashBoard, gridBagConstraints);

        issueBook = new JButton("Issue book");
        issueBook.setBackground(Color.DARK_GRAY);
        issueBook.setForeground(Color.WHITE);
        issueBook.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
        issueBook.setFocusable(false);
        gridBagConstraints.gridy = 3;
        optionsPanel.add(issueBook, gridBagConstraints);

        returnBook = new JButton("Return book");
        returnBook.setBackground(Color.DARK_GRAY);
        returnBook.setForeground(Color.WHITE);
        returnBook.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
        returnBook.setFocusable(false);
        gridBagConstraints.gridy = 4;
        optionsPanel.add(returnBook, gridBagConstraints);

        catalog = new JButton("Catalog");
        catalog.setBackground(Color.DARK_GRAY);
        catalog.setForeground(Color.WHITE);
        catalog.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
        catalog.setFocusable(false);
        gridBagConstraints.gridy = 5;
        optionsPanel.add(catalog, gridBagConstraints);

        members = new JButton("Members");
        members.setBackground(Color.DARK_GRAY);
        members.setForeground(Color.WHITE);
        members.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
        members.setFocusable(false);
        gridBagConstraints.gridy = 6;
        optionsPanel.add(members, gridBagConstraints);

        signOut = new JButton("Sign out");
        signOut.setBackground(Color.DARK_GRAY);
        signOut.setForeground(Color.WHITE);
        signOut.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
        signOut.setFocusable(false);
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new Insets(200, 5, 5, 5);
        optionsPanel.add(signOut, gridBagConstraints);

        CardLayout cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.BLACK);

        // dashboard card
        dashboardCard = new JPanel(new GridBagLayout());
        dashboardCard.setBackground(new Color(0x212020));
        String DASHBOARD = "DASHBOARD CARD";
        contentPanel.add(dashboardCard, DASHBOARD);
        new LibrarianFrameDashboardCard(dashboardCard);

        // issue books card
        issueBookCard = new JPanel(new GridBagLayout());
        issueBookCard.setBackground(new Color(0x212020));
        String ISSUE_BOOK = "ISSUE BOOK CARD";
        contentPanel.add(issueBookCard, ISSUE_BOOK);
        new LibrarianFrameIssueBookCard(issueBookCard);

        // return book card
        returnBookCard = new JPanel(new GridBagLayout());
        returnBookCard.setBackground(new Color(0x212020));
        String RETURN_BOOK = "RETURN BOOK CARD";
        contentPanel.add(returnBookCard, RETURN_BOOK);
        new LibrarianFrameReturnBookCard(returnBookCard);

        // catalog card
        catalogCard = new JPanel(new GridBagLayout());
        catalogCard.setBackground(new Color(0x212020));
        String CATALOG = "CATALOG";
        contentPanel.add(catalogCard, CATALOG);
        new LibrarianFrameCatalogCard(catalogCard);

        // members card
        membersCard = new JPanel(new GridBagLayout());
        membersCard.setBackground(new Color(0x212020));
        String MEMBERS = "MEMBERS";
        contentPanel.add(membersCard, MEMBERS);
        new LibrarianFrameMemberCard(membersCard);

        dashBoard.addActionListener(actionEvent -> cardLayout.show(contentPanel, DASHBOARD));
        issueBook.addActionListener(actionEvent -> cardLayout.show(contentPanel, ISSUE_BOOK));
        returnBook.addActionListener(actionEvent -> cardLayout.show(contentPanel, RETURN_BOOK));
        catalog.addActionListener(actionEvent -> cardLayout.show(contentPanel, CATALOG));
        members.addActionListener(actionEvent -> cardLayout.show(contentPanel, MEMBERS));
        signOut.addActionListener(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, optionsPanel, contentPanel);
        splitPane.setPreferredSize(new Dimension(2 * screenWidth / 3, 2 * screenHeight / 3));
        splitPane.setDividerLocation((int) (screenWidth / 5));
        splitPane.setDividerSize(0);

        this.setTitle("Librarian");
        this.getContentPane().setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.add(splitPane);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == signOut) {
            this.dispose();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            loginFrame.setVisible(true);
        }
    }
}