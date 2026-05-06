package library.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @since 06 May 2026
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
        model = new DefaultTableModel();

        model.addColumn("Name");
        model.addColumn("ID");
        model.addColumn("Books out");
        model.addColumn("Status");

        model.addRow(new Object[]{"Usman Tahir", "STU-001", "0", "Active"});
        model.addRow(new Object[]{"Bilal Ahmed", "STU-002", "2", "Active"});
        model.addRow(new Object[]{"Sher Khan", "STU-003", "10", "Overdue"});

        membersTable = new JTable(model);

        JTableHeader header = membersTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        membersTable.setGridColor(Color.DARK_GRAY);
        membersTable.setShowGrid(false);
        membersTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        membersTable.setRowHeight(25);

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
                return label;
            }
        });

        membersTable.setRowHeight(35);

        int rowHeight = membersTable.getRowHeight();
        int noOfRows = membersTable.getRowCount();
        membersTable.setPreferredScrollableViewportSize(new Dimension(
                membersTable.getPreferredSize().width,
                rowHeight * noOfRows
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        scrollPane = new JScrollPane(membersTable);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        membersCard.add(scrollPane, gbc);
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
        membersCard.add(Box.createVerticalGlue(), gbc);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            model.setRowCount(0);
            scrollPane.getViewport().setBackground(new Color(0x212020));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            String searchId = searchBox.getText().strip().toUpperCase();
            System.out.println(searchId);

            model.addRow(new Object[]{"Sher Khan", "STU-003", "10", "Overdue"});
        }
    }
}
