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

import library.management.system.service.FineService;
import library.management.system.service.TransactionService;
import library.management.system.dto.FineReportDTO;


 
 // Handles the System Fines panel for Admin with complete Layer Integration
 
public class AdminFrameSystemFinesCard {

    private final JPanel systemFinesCard;
    private final FineService fineService;
    private final TransactionService transactionService;

    private JTextField searchField;
    private DefaultTableModel model;
    private JTable finesTable;
    private TableRowSorter<DefaultTableModel> sorter;

    //references to labels 
    private JLabel totalOutstandingCountLabel;
    private JLabel activeLoansCountLabel;

    public AdminFrameSystemFinesCard(JPanel systemFinesCard) {
        this.systemFinesCard = systemFinesCard;
        this.fineService = new FineService();
        this.transactionService = new TransactionService();

        this.addCardHeading();
        this.addStatCounters();
        this.addSearchField();
        this.addFinesTable();
        this.addVerticalFiller();

        // fetch data + add it
        this.loadLiveTableData();
        this.updateDashboardMetrics();
    }

    // Heading

    private void addCardHeading() {
        JLabel headingLabel = new JLabel("System Fines");
        headingLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        headingLabel.setForeground(Color.WHITE);

        JLabel libraryIcon = new JLabel();      

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx  = 0;
        gbc.gridy  = 0;
        gbc.insets = new Insets(30, 10, 5, 10);
        systemFinesCard.add(headingLabel, gbc);

        gbc.gridx  = 1;
        gbc.gridy  = 0;
        gbc.insets = new Insets(30, 340, 5, 10);
        systemFinesCard.add(libraryIcon, gbc);
    }

    // Panels:

    private void addStatCounters() {
        totalOutstandingCountLabel = new JLabel("...");
        activeLoansCountLabel = new JLabel("...");

        //Leftover fines to be paid:
        JPanel outstandingPanel = buildStatCard(
                "Total Fines Outstanding",
                totalOutstandingCountLabel,
                new Color(0xCF2929)
        );

        //Active loans(only pending in fnes and issued in report, unpaid not included as bk has alrdy been returned but fine is due.)
        JPanel activeLoansPanel = buildStatCard(
                "Active Loans",
                activeLoansCountLabel,
                new Color(0x29CF45)
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx   = 0;
        gbc.gridy   = 1;
        gbc.weightx = 0.50;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(20, 25, 10, 5);
        systemFinesCard.add(outstandingPanel, gbc);

        gbc.gridx   = 1;
        gbc.gridy   = 1;
        gbc.weightx = 0.50;
        gbc.insets  = new Insets(20, 5, 10, 25);
        systemFinesCard.add(activeLoansPanel, gbc);
    }

    private JPanel buildStatCard(String labelText, JLabel countLabel, Color bgColor) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
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

    // search field

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
        systemFinesCard.add(searchField, gbc);
    }

    // Fines Table:

    private void addFinesTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("TXN ID");
        model.addColumn("STUDENT USERNAME");
        model.addColumn("BOOK TITLE");
        model.addColumn("DUE DATE");
        model.addColumn("RET. DATE");
        model.addColumn("FINE AMOUNT");
        model.addColumn("STATUS");

        finesTable = new JTable(model);
        finesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        finesTable.getColumnModel().getColumn(0).setPreferredWidth(75);   // txn_id
        finesTable.getColumnModel().getColumn(0).setMinWidth(70);        // transaction id width
        finesTable.getColumnModel().getColumn(1).setPreferredWidth(175);  // stu. usr name
        finesTable.getColumnModel().getColumn(2).setPreferredWidth(175);  // bk title
        finesTable.getColumnModel().getColumn(3).setPreferredWidth(110);  // due date
        finesTable.getColumnModel().getColumn(4).setPreferredWidth(105);  // Ret. date (renamed becuz would'nt fit otherwise),
        finesTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // fine amt
        finesTable.getColumnModel().getColumn(6).setPreferredWidth(95);   // status

        JTableHeader header = finesTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 15));

        finesTable.setGridColor(Color.DARK_GRAY);
        finesTable.setShowGrid(false);
        finesTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        finesTable.setRowHeight(35);

        int fineAmtColumn = 5;
        int statusColumn  = 6;

        finesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
                    String status = value.toString().toUpperCase();
                    label.setBackground("PAID".equals(status)
                            ? new Color(0x309912)
                            : new Color(0xB82323));
                    label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
                }

                if (column == fineAmtColumn) {
                    label.setFont(new Font("FiraMono NerdFont", Font.BOLD, 15));
                }

                return label;
            }
        });

        sorter = new TableRowSorter<>(model);
        finesTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(finesTable);
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
        systemFinesCard.add(scrollPane, gbc);
    }

    private void loadLiveTableData() {
        model.setRowCount(0);
        List<FineReportDTO> reports = fineService.getFineReport();
        for (FineReportDTO report : reports) {
            String returnStr = (report.getReturnDate() != null) ? report.getReturnDate().toString() : "PENDING";
            String dueStr    = (report.getDueDate() != null) ? report.getDueDate().toString() : "N/A";
            
            model.addRow(new Object[]{
                "TXN-" + String.format("%03d", report.getTransactionId()),
                report.getUsername(),
                report.getBookTitle(),
                dueStr,
                returnStr,
                "Rs. " + String.format("%.2f", report.getFineAmount()),
                report.getPaymentStatus()
            });
        }
    }

    // vertical filler:

    private void addVerticalFiller() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.gridy   = 4;
        gbc.weighty = 0.1;
        gbc.fill    = GridBagConstraints.VERTICAL;
        systemFinesCard.add(Box.createVerticalGlue(), gbc);
    }

    // ── LIVE FILTER ───────────────────────────────────────────────────────────

    private void applyFilter() {
        String text = searchField.getText().strip();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
        }
    }

    // service layer methods :

    private void updateDashboardMetrics() {
        try {
            // active loans logic:
            int activeLoans = transactionService.getActiveLoansCount();
            activeLoansCountLabel.setText(String.valueOf(activeLoans));

            // leftover fines / outstnding fines logic:
            double totalOutstanding = 0.0;
            List<FineReportDTO> reports = fineService.getFineReport();
            
            for (FineReportDTO report : reports) {
                if ("UNPAID".equalsIgnoreCase(report.getPaymentStatus()) || 
                    "PENDING".equalsIgnoreCase(report.getPaymentStatus())) {
                    totalOutstanding += report.getFineAmount();
                }
            }
            
            totalOutstandingCountLabel.setText("Rs. " + String.format("%.2f", totalOutstanding));

        } catch (Exception e) {
            e.printStackTrace();
            totalOutstandingCountLabel.setText("ERR");
            activeLoansCountLabel.setText("ERR");
        }
    }
}
