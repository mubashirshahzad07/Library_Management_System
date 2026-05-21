package library.management.system.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

import library.management.system.service.TransactionService;
import library.management.system.service.UserService;
import library.management.system.service.BookService;
import library.management.system.dto.TransactionReportDTO;


 // Handles the System Reports panel for Admin with complete Layer Integration

public class AdminFrameSystemReportsCard {

    private final JPanel systemReportsCard;
    private final TransactionService transactionService;
    private final UserService userService;
    private final BookService bookService;

    private JTextField searchField;
    private DefaultTableModel model;
    private JTable reportsTable;
    private TableRowSorter<DefaultTableModel> sorter;

    // Ref. to lbels:
    private JLabel totalBooksCountLabel;
    private JLabel activeUsersCountLabel;
    private JLabel overdueCountLabel;

    public AdminFrameSystemReportsCard(JPanel systemReportsCard) {
        this.systemReportsCard = systemReportsCard;
        this.transactionService = new TransactionService();
        this.userService = new UserService();
        this.bookService = new BookService();
        
        this.addCardHeading();
        this.addStatCounters();
        this.addSearchField();
        this.addReportsTable();
        this.addVerticalFiller();
        
        // Fetch and add data (service layer):
        this.loadLiveTableData();
        this.updateDashboardMetrics();
    }

    // headings:

    private void addCardHeading() {
        JLabel headingLabel = new JLabel("System Reports");
        headingLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE);

        JLabel libraryIcon = new JLabel(); // temp label      

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.gridy  = 0;
        gbc.insets = new Insets(30, 10, 5, 10);
        systemReportsCard.add(headingLabel, gbc);

        gbc.gridx  = 1;
        gbc.gridy  = 0;
        gbc.insets = new Insets(30, 340, 5, 10);
        systemReportsCard.add(libraryIcon, gbc);
    }

    // panels:

    private void addStatCounters() {
        totalBooksCountLabel = new JLabel("...");
        activeUsersCountLabel = new JLabel("...");
        overdueCountLabel = new JLabel("...");

        JPanel totalBooksPanel = buildStatCard("Total Books", totalBooksCountLabel, new Color(0x67ABD6));
        // active users: (i chnged it from active loans)
        JPanel activeUsersPanel = buildStatCard("Active Users", activeUsersCountLabel, new Color(0x29CF45));
        JPanel overduePanel = buildStatCard("Overdue", overdueCountLabel, new Color(0xCF2929));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx   = 0;
        gbc.gridy   = 1;
        gbc.weightx = 0.34;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(20, 25, 10, 5);
        systemReportsCard.add(totalBooksPanel, gbc);

        gbc.gridx   = 1;
        gbc.gridy   = 1;
        gbc.weightx = 0.33;
        gbc.insets  = new Insets(20, 5, 10, 5);
        systemReportsCard.add(activeUsersPanel, gbc);

        gbc.gridx   = 2;
        gbc.gridy   = 1;
        gbc.weightx = 0.33;
        gbc.insets  = new Insets(20, 5, 10, 25);
        systemReportsCard.add(overduePanel, gbc);
    }

    private JPanel buildStatCard(String labelText, JLabel countLabel, Color bgColor) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 20));
        panel.setBackground(bgColor);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("FiraMono NerdFont", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        gbc.gridx  = 0;
        gbc.gridy  = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        countLabel.setFont(new Font("FiraMono NerdFont", Font.BOLD, 25));
        countLabel.setForeground(Color.WHITE);
        gbc.gridx  = 0;
        gbc.gridy  = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(countLabel, gbc);

        return panel;
    }

    // Search field:

    private void addSearchField() {
        searchField = new JTextField();
        searchField.setBackground(Color.DARK_GRAY);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 17));
        searchField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchField.setPreferredSize(new Dimension(100, 42));

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { applyFilter(); }
            public void removeUpdate(DocumentEvent e)  { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(10, 10, 5, 10);
        systemReportsCard.add(searchField, gbc);
    }

    // transaction table:

    private void addReportsTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("TXN ID");
        model.addColumn("MEMBER USERNAME");
        model.addColumn("BOOK TITLE");
        model.addColumn("ISSUE DATE");
        model.addColumn("DUE DATE");
        model.addColumn("STATUS");

        reportsTable = new JTable(model);
        reportsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // adjusted widths for table:
        reportsTable.getColumnModel().getColumn(0).setPreferredWidth(90); 
        reportsTable.getColumnModel().getColumn(0).setMinWidth(90);
        reportsTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        reportsTable.getColumnModel().getColumn(2).setPreferredWidth(250); // wider now,(chng if issues still persist)
        reportsTable.getColumnModel().getColumn(3).setPreferredWidth(107);//issue date col. fixed.

        JTableHeader header = reportsTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));

        reportsTable.setGridColor(Color.DARK_GRAY);
        reportsTable.setShowGrid(false);
        reportsTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        reportsTable.setRowHeight(35);

        int statusColumn = 5;
        reportsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
                label.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));

                if (column == statusColumn && value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "Returned":
                            label.setBackground(new Color(0x309912));
                            break;
                        case "Issued":
                            label.setBackground(new Color(0x294975));
                            break;
                        case "Overdue":
                            label.setBackground(new Color(0xB82323));
                            break;
                        default:
                            label.setBackground(new Color(0x388A7C));
                            break;
                    }
                    label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
                    label.setForeground(Color.WHITE);
                }

                return label;
            }
        });

        sorter = new TableRowSorter<>(model);
        reportsTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(reportsTable);
        scrollPane.getViewport().setBackground(new Color(0x212020));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(850, 320));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx     = 0;
        gbc.gridy     = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill      = GridBagConstraints.BOTH;
        gbc.weightx   = 1.0;
        gbc.weighty   = 0.7;
        gbc.insets    = new Insets(5, 10, 5, 10);
        systemReportsCard.add(scrollPane, gbc);
    }

    private void loadLiveTableData() {
        model.setRowCount(0);
        List<TransactionReportDTO> reports = transactionService.getTransactionReports();
        for (TransactionReportDTO report : reports) {
            model.addRow(new Object[]{
                "TXN-" + String.format("%03d", report.getTransactionId()),
                report.getUsername(),
                report.getBookTitle(),
                report.getIssueDate().toString(),
                report.getDueDate().toString(),
                report.getStatus()
            });
        }
    }

    // vertical filler(similar logic as fines card:)

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 4;
        gbc.weighty = 0.1;
        gbc.fill    = GridBagConstraints.VERTICAL;
        systemReportsCard.add(Box.createVerticalGlue(), gbc);
    }

    // live update filter:(same logic used)

    private void applyFilter() {
        String text = searchField.getText().strip();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1, 2));
        }
    }

    // Service layer new methods intg.:
    private void updateDashboardMetrics() {
        try {
            //book counter:
            int totalBooks = bookService.getTotalActiveBooksCount();
            totalBooksCountLabel.setText(String.valueOf(totalBooks));

            // active usrs:
            int activeUsers = userService.getActiveUsersCount();
            activeUsersCountLabel.setText(String.valueOf(activeUsers));

            // overdue:
            int overdueBooks = transactionService.getTotalOverdueBooksCount();
            overdueCountLabel.setText(String.valueOf(overdueBooks));

        } catch (Exception e) {
            e.printStackTrace();
            
            totalBooksCountLabel.setText("ERR");
            activeUsersCountLabel.setText("ERR");
            overdueCountLabel.setText("ERR");
        }
    }
}
