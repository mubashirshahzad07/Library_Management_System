package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @since 29 April 2026
 * Handles the login window
 */
public class LoginFrame extends JFrame implements ActionListener {
    private JTextField username;
    private JPasswordField password;
    private JComboBox<String> roles;
    private JButton signIn;

    LoginFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        ImageIcon originalImage = new ImageIcon(ClassLoader.getSystemResource("library_background.jpg"));
        int backgroundImageWidth = 2 * (screenWidth / 3);
        int backgroundImageHeight = 2 * (screenHeight / 3);
        Image scaledImage = originalImage.getImage().getScaledInstance(backgroundImageWidth, backgroundImageHeight, Image.SCALE_SMOOTH);

        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(scaledImage));

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(0x2D2E2E));
        loginPanel.setBorder(BorderFactory.createEtchedBorder());
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setOpaque(true);
        int loginPanelWidth = (int) (1.5 * (backgroundImageWidth / 5));
        int loginPanelHeight = (int) (2.5 * (backgroundImageHeight / 5));
        loginPanel.setSize(loginPanelWidth, loginPanelHeight);
        loginPanel.setLocation((int) (backgroundImageWidth / 2.8), (int) (backgroundImageHeight / 2.5));

        background.add(loginPanel);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;

        JLabel signInLabel = new JLabel("Sign in to your Account");
        signInLabel.setFont(new Font("FiraMono NerdFont", Font.BOLD, 19));
        signInLabel.setForeground(Color.WHITE);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(10, 1, 5, 10);
        loginPanel.add(signInLabel, gridBagConstraints);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        usernameLabel.setForeground(Color.WHITE);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(10, 10, 1, 10);
        loginPanel.add(usernameLabel, gridBagConstraints);

        username = new JTextField();
        username.setPreferredSize(new Dimension((int) (4 * (loginPanelWidth / 5)), (int) (loginPanelHeight / 9)));
        username.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        username.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        username.setBackground(Color.DARK_GRAY);
        username.setForeground(Color.WHITE);
        username.setCaretColor(Color.WHITE);
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(1, 10, 10, 10);
        loginPanel.add(username, gridBagConstraints);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.WHITE);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(10, 10, 1, 10);
        loginPanel.add(passwordLabel, gridBagConstraints);

        password = new JPasswordField();
        password.setPreferredSize(new Dimension((int) (4 * (loginPanelWidth / 5)), (int) (loginPanelHeight / 9)));
        password.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        password.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        password.setBackground(Color.DARK_GRAY);
        password.setForeground(Color.WHITE);
        password.setCaretColor(Color.WHITE);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new Insets(1, 10, 10, 10);
        loginPanel.add(password, gridBagConstraints);

        JLabel rolesLabel = new JLabel("Role");
        rolesLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 14));
        rolesLabel.setForeground(Color.WHITE);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new Insets(10, 10, 1, 10);
        loginPanel.add(rolesLabel, gridBagConstraints);

        String[] rolesAvailable = {"Admin", "Librarian", "Student"};
        roles = new JComboBox<>(rolesAvailable);
        roles.setPreferredSize(new Dimension((int) (4 * (loginPanelWidth / 5)), (int) (loginPanelHeight / 9)));
        roles.setBackground(Color.DARK_GRAY);
        roles.setForeground(Color.WHITE);
        roles.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        roles.setFocusable(false);
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new Insets(1, 10, 10, 10);
        loginPanel.add(roles, gridBagConstraints);

        signIn = new JButton("Sign In");
        signIn.setFocusable(false);
        signIn.setPreferredSize(new Dimension((int) (4 * (loginPanelWidth / 5)), (int) (loginPanelHeight / 9)));
        signIn.setBackground(Color.DARK_GRAY);
        signIn.setForeground(Color.WHITE);
        signIn.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
        signIn.addActionListener(this);
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        loginPanel.add(signIn, gridBagConstraints);

        this.setTitle("Library Management System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.add(background);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == signIn) {
            String username = this.username.getText().strip();
            char[] passwordArray = this.password.getPassword();
            String password = new String(passwordArray).strip();
            String role = (String) this.roles.getSelectedItem();

            System.out.println("Username = " + username);
            System.out.println("Password = " + password);
        System.out.println("Role = " + role);

            this.username.setText("");
            this.password.setText("");
            this.username.requestFocusInWindow();
            this.username.setCaretPosition(0);
            roles.setSelectedIndex(0);

            // Shumaail: run the SQL query to find the current entry based on username, password and role

            // 1. if the login was successful
            JOptionPane.showMessageDialog(
                    null,
                    "<html><font color='Green'> Login successful! </font></html>",
                    "Login",
                    JOptionPane.INFORMATION_MESSAGE
            );

            this.setVisible(false);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new StudentFrame(this);
            // end of 1

        //    // 2. if the login failed
        //    JOptionPane.showMessageDialog(
        //            null,
        //            "<html><font color='red'> Incorrect username or password! </font></html>",
        //            "Login Error",
        //            JOptionPane.ERROR_MESSAGE
        //    );
        //    // end of 2
        }
    }
}