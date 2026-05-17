package library.management.system.ui;

import library.management.system.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * @since 16 May 2026
 * Handles the members window for librarians
 */
public class LibrarianFrameMemberCard implements ActionListener {
    private final JPanel membersCard;
    private JButton searchButton;
    private JTextField searchBox;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JTable membersTable;

    public LibrarianFrameMemberCard(JPanel membersCard) {
        this.membersCard = membersCard;
        this.addCardHeading();
        this.addSearchBox();
        this.addSearchButton();
        this.addMembersTable();
        this.addVerticalFiller();
    }

    private void addCardHeading() {
        JLabel membersLabel = new JLabel("Members");
        membersLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        membersLabel.setForeground(Color.WHITE);

        JLabel libraryIcon = new JLabel(new ImageIcon(ClassLoader.getSystemResource("librarian_label.png")));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 10, 5, 340);
        membersCard.add(membersLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 340, 5, 10);
        membersCard.add(libraryIcon, gbc);
    }

    private void addSearchBox() {
        searchBox = new JTextField();
        searchBox.setBackground(Color.DARK_GRAY);
        searchBox.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 17));
        searchBox.setForeground(Color.WHITE);
        searchBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchBox.setCaretColor(Color.WHITE);
        searchBox.setPreferredSize(new Dimension(100, 42));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        membersCard.add(searchBox, gbc);
    }

    private void addSearchButton() {
        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0x294975));
        searchButton.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 17));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusable(false);
        searchButton.setPreferredSize(new Dimension(55, 42));
        searchButton.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        membersCard.add(searchButton, gbc);
    }

    private void addMembersTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Name");
        model.addColumn("Username");
        model.addColumn("Books out");
        model.addColumn("Status");

        populateTable(null);

        membersTable = new JTable(model);
        applyTableStyling();

        scrollPane = new JScrollPane(membersTable);
        scrollPane.getViewport().setBackground(new Color(0x212020));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        membersCard.add(scrollPane, gbc);
    }

    private void applyTableStyling() {
        JTableHeader header = membersTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        membersTable.setGridColor(Color.DARK_GRAY);
        membersTable.setShowGrid(false);
        membersTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        membersTable.setRowHeight(35);

        int statusColumn = 3;
        membersTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
                    String status = value != null ? value.toString() : "";
                    label.setBackground(status.equals("Overdue")
                            ? new Color(0xB82323) : new Color(0x309912));
                    label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 16));
                }
                return label;
            }
        });
    }

    private void populateTable(String keyword) {
        model.setRowCount(0);

        String sql = "SELECT u.name, u.username, " +
                "    (SELECT COUNT(*) FROM Transactions t WHERE t.user_id = u.user_id AND t.status = 'ISSUED') AS books_out, " +
                "    CASE WHEN EXISTS (" +
                "        SELECT 1 FROM Transactions t2 WHERE t2.user_id = u.user_id " +
                "        AND t2.status = 'ISSUED' AND t2.due_date < CURDATE()) " +
                "    THEN 'Overdue' ELSE 'Active' END AS status " +
                "FROM Users u " +
                "WHERE u.role = 'STUDENT' AND u.is_active = TRUE";

        if (keyword != null && !keyword.isEmpty()) {
            sql += " AND (LOWER(u.name) LIKE ? OR LOWER(u.username) LIKE ?)";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (keyword != null && !keyword.isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getInt("books_out"),
                        rs.getString("status")
                });
            }

            if (model.getRowCount() == 0 && keyword != null && !keyword.isEmpty()) {
                JOptionPane.showMessageDialog(membersCard, "No members found for: " + keyword,
                        "Search", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
        membersCard.add(Box.createVerticalGlue(), gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String keyword = searchBox.getText().strip();
            populateTable(keyword);
        }
    }
}