package library.management.system.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import library.management.system.service.UserService;
import library.management.system.dto.UserTableDTO;


 //Handles the Manage Users panel for Admin connected to Database
 
public class AdminFrameManageUsersCard implements ActionListener {

    private final JPanel manageUsersCard;
    private final UserService userService = new UserService();

    private JTextField nameField, usernameField, searchField;
    private JPasswordField passwordField;
    private JComboBox<String> roleDropdown;
    private JButton addUserButton, deactivateUserButton;
    private DefaultTableModel model;
    private JTable usersTable;

    public AdminFrameManageUsersCard(JPanel manageUsersCard) {
        this.manageUsersCard = manageUsersCard;
        this.addCardHeading();
        this.addInputForm();
        this.addSearchField();
        this.addUsersTable();
        this.addVerticalFiller();
        this.loadTableData();
    }

    // HEADING

    private void addCardHeading() {
        JLabel headingLabel = new JLabel("Manage Users");
        headingLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE);

        JLabel libraryIcon = new JLabel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 0;
        gbc.insets = new Insets(15, 10, 5, 10);
        manageUsersCard.add(headingLabel, gbc);

        gbc.gridx  = 1;
        gbc.gridy  = 0;
        gbc.insets = new Insets(15, 340, 5, 10);
        manageUsersCard.add(libraryIcon, gbc);
    }

    // INPUT

    private void addInputForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(0x2E2D2D));

        GridBagConstraints gbc = new GridBagConstraints();

        // Name
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 13));
        nameLabel.setForeground(Color.WHITE);

        nameField = new JTextField();
        nameField.setBackground(Color.DARK_GRAY);
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        nameField.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        nameField.setPreferredSize(new Dimension(100, 30));

        gbc.gridx  = 0;
        gbc.gridy  = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 0, 10);
        formPanel.add(nameLabel, gbc);

        gbc.gridx     = 0;
        gbc.gridy     = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;
        gbc.insets    = new Insets(0, 10, 5, 10);
        formPanel.add(nameField, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 13));
        usernameLabel.setForeground(Color.WHITE);

        usernameField = new JTextField();
        usernameField.setBackground(Color.DARK_GRAY);
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        usernameField.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        usernameField.setPreferredSize(new Dimension(100, 30));

        gbc.gridx     = 0;
        gbc.gridy     = 2;
        gbc.gridwidth = 1;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.weightx   = 0;
        gbc.insets    = new Insets(5, 10, 0, 10);
        formPanel.add(usernameLabel, gbc);

        gbc.gridx     = 0;
        gbc.gridy     = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;
        gbc.insets    = new Insets(0, 10, 5, 10);
        formPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 13));
        passwordLabel.setForeground(Color.WHITE);

        passwordField = new JPasswordField();
        passwordField.setBackground(Color.DARK_GRAY);
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        passwordField.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        passwordField.setPreferredSize(new Dimension(100, 30));

        gbc.gridx     = 0;
        gbc.gridy     = 4;
        gbc.gridwidth = 1;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.weightx   = 0;
        gbc.insets = new Insets(5, 10, 0, 10);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx     = 0;
        gbc.gridy     = 5;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;
        gbc.insets    = new Insets(0, 10, 5, 10);
        formPanel.add(passwordField, gbc);

        // Role bar
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 13));
        roleLabel.setForeground(Color.WHITE);

        roleDropdown = new JComboBox<>(new String[]{"LIBRARIAN", "STUDENT"});
        roleDropdown.setBackground(Color.DARK_GRAY);
        roleDropdown.setForeground(Color.WHITE);
        roleDropdown.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        roleDropdown.setFocusable(false);
        roleDropdown.setPreferredSize(new Dimension(100, 30));

        gbc.gridx     = 0;
        gbc.gridy     = 6;
        gbc.gridwidth = 1;
        gbc.fill      = GridBagConstraints.NONE;
        gbc.weightx   = 0;
        gbc.insets    = new Insets(5, 10, 0, 10);
        formPanel.add(roleLabel, gbc);

        gbc.gridx     = 0;
        gbc.gridy     = 7;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;
        gbc.insets    = new Insets(0, 10, 5, 10);
        formPanel.add(roleDropdown, gbc);

        // Button
        addUserButton = new JButton("Add User");
        addUserButton.setBackground(new Color(0x309912));
        addUserButton.setForeground(Color.WHITE);
        addUserButton.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        addUserButton.setFocusable(false);
        addUserButton.setPreferredSize(new Dimension(140, 30));
        addUserButton.addActionListener(this);

        deactivateUserButton = new JButton("Deactivate User");
        deactivateUserButton.setBackground(new Color(0xB82323));
        deactivateUserButton.setForeground(Color.WHITE);
        deactivateUserButton.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
        deactivateUserButton.setFocusable(false);
        deactivateUserButton.setPreferredSize(new Dimension(160, 30));
        deactivateUserButton.addActionListener(this);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(new Color(0x2E2D2D));
        buttonsPanel.add(addUserButton);
        buttonsPanel.add(deactivateUserButton);

        gbc.gridx     = 0;
        gbc.gridy     = 8;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;
        gbc.insets    = new Insets(5, 10, 5, 10);
        formPanel.add(buttonsPanel, gbc);

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.gridx     = 0;
        cardGbc.gridy     = 1;
        cardGbc.gridwidth = GridBagConstraints.REMAINDER;
        cardGbc.fill      = GridBagConstraints.HORIZONTAL;
        cardGbc.insets    = new Insets(5, 10, 5, 10);
        manageUsersCard.add(formPanel, cardGbc);
    }

    // SEARCH Bar

    private void addSearchField() {
        searchField = new JTextField();
        searchField.setBackground(Color.DARK_GRAY);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 15));
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchField.setPreferredSize(new Dimension(100, 32));

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { executeSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { executeSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { executeSearch(); }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5, 10, 5, 10);
        manageUsersCard.add(searchField, gbc);
    }

    // user table 

    private void addUsersTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("USER ID");
        model.addColumn("NAME");
        model.addColumn("USERNAME");
        model.addColumn("ROLE");
        model.addColumn("STATUS");

        usersTable = new JTable(model);
        usersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        usersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = usersTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = usersTable.convertRowIndexToModel(selectedRow);
                    
                    // Fill fields with selected user details instead of messing up the search field.
                    nameField.setText(model.getValueAt(modelRow, 1).toString());
                    usernameField.setText(model.getValueAt(modelRow, 2).toString());
                    
                    // Select Role in dropdown
                    String role = model.getValueAt(modelRow, 3).toString();
                    if (role.equalsIgnoreCase("LIBRARIAN") || role.equalsIgnoreCase("STUDENT")) {
                        roleDropdown.setSelectedItem(role.toUpperCase());
                    }
                }
            }
        });

        JTableHeader header = usersTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 15));

        usersTable.setGridColor(Color.DARK_GRAY);
        usersTable.setShowGrid(false);
        usersTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 13));
        usersTable.setRowHeight(30);

        int statusColumn = 4;
        int roleColumn   = 3;

        usersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBackground(new Color(0x388A7C));
                label.setOpaque(true);
                label.setForeground(Color.WHITE);

                if (column == statusColumn) {
                    String status = table.getValueAt(row, statusColumn).toString();
                    label.setBackground(status.equals("Active")
                            ? new Color(0x309912)
                            : new Color(0xB82323));
                    label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 14));
                }

                if (column == roleColumn) {
                    label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 13));
                }

                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.getViewport().setBackground(new Color(0x212020));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.BOTH; 
        gbc.weightx   = 1.0;
        gbc.weighty   = 1.0;
        gbc.insets    = new Insets(5, 10, 10, 10);
        manageUsersCard.add(scrollPane, gbc);
    }

    // vertical filler logic

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 4;
        gbc.weighty = 0.0;
        gbc.fill    = GridBagConstraints.VERTICAL;
        manageUsersCard.add(Box.createVerticalGlue(), gbc);
    }

    // DATABASE INTEGRATION CODES:

    private void loadTableData() {
        try {
            
            List<UserTableDTO> users = userService.getActiveUsers();
            populateTable(users);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(manageUsersCard, "Error loading users: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executeSearch() {
        String keyword = searchField.getText().strip();
        try {
            if (keyword.isEmpty()) {
                loadTableData();
            } else if (keyword.equalsIgnoreCase("inactive")) {
                
                List<UserTableDTO> inactiveUsers = userService.getInactiveUsers();
                populateTable(inactiveUsers);
            } else {
               
                if (keyword.toUpperCase().matches("^(STU-|LIB-|ADM-)\\d+")) {
                    keyword = keyword.substring(4).replaceFirst("^0+", "");
                }
                
                List<UserTableDTO> filteredUsers = userService.searchUsers(keyword.toLowerCase());
                populateTable(filteredUsers);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void populateTable(List<UserTableDTO> users) {
        model.setRowCount(0);
        for (UserTableDTO user : users) {
            String rolePrefix = "STU-";
            if (user.getRole().equalsIgnoreCase("LIBRARIAN")) {
                rolePrefix = "LIB-";
            } else if (user.getRole().equalsIgnoreCase("ADMIN")) {
                rolePrefix = "ADM-";
            }
            
            String formattedId = rolePrefix + String.format("%03d", user.getUserId());

            model.addRow(new Object[]{
                formattedId,
                user.getName(),
                user.getUsername(),
                user.getRole(),
                user.getStatus()
            });
        }
    }

    // action handlr:

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addUserButton) {
            String name     = nameField.getText().strip();
            String username = usernameField.getText().strip();
            String password = new String(passwordField.getPassword()).strip();
            String role     = (String) roleDropdown.getSelectedItem();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(manageUsersCard,
                        "Please fill in all fields before adding a user.",
                        "Missing Fields",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                userService.addUser(name, username, password, role);
                loadTableData();

                nameField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                roleDropdown.setSelectedIndex(0);
                
                JOptionPane.showMessageDialog(manageUsersCard, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(manageUsersCard, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == deactivateUserButton) {
            int selectedRow = usersTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(manageUsersCard,
                        "Please select a user from the table to deactivate.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int modelRow = usersTable.convertRowIndexToModel(selectedRow);

            String currentStatus = model.getValueAt(modelRow, 4).toString();
            if (currentStatus.equals("Inactive")) {
                JOptionPane.showMessageDialog(manageUsersCard,
                        "This user is already inactive.",
                        "Already Inactive",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // extract id from string:
            String rawIdStr = model.getValueAt(modelRow, 0).toString().replaceAll("[^0-9]", "");
            int userId = Integer.parseInt(rawIdStr);
            String name = model.getValueAt(modelRow, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(manageUsersCard,
                    "Deactivate user \"" + name + "\" (ID: " + userId + ")?",
                    "Confirm Deactivation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    userService.deactivateUser(userId);
                    loadTableData(); // hide the user since this loads only active users,
                    JOptionPane.showMessageDialog(manageUsersCard, "User deactivated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(manageUsersCard, ex.getMessage(),"Deactivation Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
