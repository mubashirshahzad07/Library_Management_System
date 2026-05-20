//package library.management.system.ui;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//
// // Handles the Admin Window
//
//public class AdminFrame extends JFrame implements ActionListener {
//
//    private JButton manageUsers, manageBooks, systemReports, systemFines, signOut;
//    private JPanel manageUsersCard, manageBooksCard, systemReportsCard, systemFinesCard;
//    private JFrame loginFrame;
//
//    AdminFrame(JFrame loginFrame) {
//        this.loginFrame = loginFrame;
//
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int screenWidth  = (int) screenSize.getWidth();
//        int screenHeight = (int) screenSize.getHeight();
//
//        // sidebar:
//        JPanel optionsPanel = new JPanel();
//        optionsPanel.setLayout(new GridBagLayout());
//        optionsPanel.setBackground(new Color(0x2E2D2D));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx  = 0;
//        gbc.fill   = GridBagConstraints.HORIZONTAL;
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.insets = new Insets(5, 5, 5, 5);
//
//        // Admin avatar + name label
//        JLabel adminLabel = new JLabel("Admin");
//        try {
//
//            ImageIcon originalIcon = new ImageIcon(ClassLoader.getSystemResource("librarian_icon.png"));
//            Image scaledImage = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
//            adminLabel.setIcon(new ImageIcon(scaledImage));
//            adminLabel.setIconTextGap(10); // Adds a gap between icon and text
//        } catch (Exception ex) {
//            System.out.println("Icon 'librarian_icon.png' not found. Defaulting to text-only.");
//        }
//
//        adminLabel.setForeground(Color.WHITE);
//        adminLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 18));
//        gbc.gridy = 0;
//        optionsPanel.add(adminLabel, gbc);
//
//        // Sec heading
//        JLabel libraryLabel = new JLabel("LIBRARY");
//        libraryLabel.setForeground(Color.WHITE);
//        libraryLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 15));
//        gbc.gridy = 1;
//        optionsPanel.add(libraryLabel, gbc);
//
//        // Nav buttons
//        manageUsers = new JButton("Manage Users");
//        manageUsers.setBackground(Color.DARK_GRAY);
//        manageUsers.setForeground(Color.WHITE);
//        manageUsers.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
//        manageUsers.setFocusable(false);
//        gbc.gridy = 2;
//        optionsPanel.add(manageUsers, gbc);
//
//        manageBooks = new JButton("Manage Books");
//        manageBooks.setBackground(Color.DARK_GRAY);
//        manageBooks.setForeground(Color.WHITE);
//        manageBooks.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
//        manageBooks.setFocusable(false);
//        gbc.gridy = 3;
//        optionsPanel.add(manageBooks, gbc);
//
//        systemReports = new JButton("System Reports");
//        systemReports.setBackground(Color.DARK_GRAY);
//        systemReports.setForeground(Color.WHITE);
//        systemReports.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
//        systemReports.setFocusable(false);
//        gbc.gridy = 4;
//        optionsPanel.add(systemReports, gbc);
//
//        systemFines = new JButton("System Fines");
//        systemFines.setBackground(Color.DARK_GRAY);
//        systemFines.setForeground(Color.WHITE);
//        systemFines.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
//        systemFines.setFocusable(false);
//        gbc.gridy = 5;
//        optionsPanel.add(systemFines, gbc);
//
//        // signOut pushed to the bottom
//        signOut = new JButton("Sign Out");
//        signOut.setBackground(Color.DARK_GRAY);
//        signOut.setForeground(Color.WHITE);
//        signOut.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
//        signOut.setFocusable(false);
//        gbc.gridy  = 6;
//        gbc.insets = new Insets(200, 5, 5, 5);
//        optionsPanel.add(signOut, gbc);
//
//        // card layout:
//        CardLayout cardLayout  = new CardLayout();
//        JPanel     contentPanel = new JPanel(cardLayout);
//        contentPanel.setBackground(Color.BLACK);
//
//        // Manage Users
//        manageUsersCard = new JPanel(new GridBagLayout());
//        manageUsersCard.setBackground(new Color(0x212020));
//        String MANAGE_USERS = "MANAGE USERS";
//        contentPanel.add(manageUsersCard, MANAGE_USERS);
//        new AdminFrameManageUsersCard(manageUsersCard);
//
//        // Manage Books:
//        manageBooksCard = new JPanel(new GridBagLayout());
//        manageBooksCard.setBackground(new Color(0x212020));
//        String MANAGE_BOOKS = "MANAGE BOOKS";
//        contentPanel.add(manageBooksCard, MANAGE_BOOKS);
//        new AdminFrameManageBooksCard(manageBooksCard);
//
//        // System Reports
//        systemReportsCard = new JPanel(new GridBagLayout());
//        systemReportsCard.setBackground(new Color(0x212020));
//        String SYSTEM_REPORTS = "SYSTEM REPORTS";
//        contentPanel.add(systemReportsCard, SYSTEM_REPORTS);
//        new AdminFrameSystemReportsCard(systemReportsCard);
//
//        //System Fines
//        systemFinesCard = new JPanel(new GridBagLayout());
//        systemFinesCard.setBackground(new Color(0x212020));
//        String SYSTEM_FINES = "SYSTEM FINES";
//        contentPanel.add(systemFinesCard, SYSTEM_FINES);
//        new AdminFrameSystemFinesCard(systemFinesCard);
//
//        // buttn logc:
//        manageUsers.addActionListener(e  -> cardLayout.show(contentPanel, MANAGE_USERS));
//        manageBooks.addActionListener(e  -> cardLayout.show(contentPanel, MANAGE_BOOKS));
//        systemReports.addActionListener(e -> cardLayout.show(contentPanel, SYSTEM_REPORTS));
//        systemFines.addActionListener(e  -> cardLayout.show(contentPanel, SYSTEM_FINES));
//        signOut.addActionListener(this);
//
//        // split pane:
//        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, optionsPanel, contentPanel);
//        splitPane.setPreferredSize(new Dimension((2 * screenWidth / 3) + 150, 2 * screenHeight / 3));
//        splitPane.setDividerLocation((int) (screenWidth / 5));
//        splitPane.setDividerSize(0);
//
//        // pane setings:
//        this.setTitle("Admin");
//        this.getContentPane().setBackground(Color.BLACK);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setResizable(false);
//        this.add(splitPane);
//        this.pack();
//        this.setLocationRelativeTo(null);
//        this.setVisible(true);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent actionEvent) {
//        if (actionEvent.getSource() == signOut) {
//            this.dispose();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            loginFrame.setVisible(true);
//        }
//    }
//}
