package library.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @since 03 May 2026
 * Handles how students can search for the books
 */
public class StudentFrameSearchBooksCard implements ActionListener {
    private final JPanel searchBooksCard;
    private JButton searchButton;
    private JTextField searchBox;
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JTable searchBooksTable;

    public StudentFrameSearchBooksCard(JPanel searchBooksCard) {
        this.searchBooksCard = searchBooksCard;
        this.addCardHeading();
        this.addSearchBox();
        this.addSearchButton();
        this.addSearchTable();
        this.addVerticalFiller();
    }

    private void addCardHeading() {
        JLabel searchBooksLabel = new JLabel("Search books");
        searchBooksLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 30));
        searchBooksLabel.setForeground(Color.WHITE);
        JLabel studentLabel = new JLabel(new ImageIcon(ClassLoader.getSystemResource("student_label.png")));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 10, 5, 290);
        searchBooksCard.add(searchBooksLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 290, 5, 10);
        searchBooksCard.add(studentLabel, gbc);
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
        searchBooksCard.add(searchBox, gbc);
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
        searchBooksCard.add(searchButton, gbc);
    }

    private void addSearchTable() {
        model = new DefaultTableModel();

        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("Available");
        model.addColumn("");

        model.addRow(new Object[]{"Clean Code", "Robert Martin", "Yes", "Request"});
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "No", "Request"});
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "No", "Request"});
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "No", "Request"});
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "No", "Request"});
        model.addRow(new Object[]{"Data Structures", "Mark Allen", "No", "Request"});

        int buttonColumnIndex = 3;

        searchBooksTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == buttonColumnIndex) {
                    return !this.getValueAt(row, column).toString().equals("Requested");
                }
                
                return super.isCellEditable(row, column);
            }
        };

        JTableHeader header = searchBooksTable.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0x043029));
        header.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 16));
        searchBooksTable.setGridColor(Color.DARK_GRAY);
        searchBooksTable.setShowGrid(false);
        searchBooksTable.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 14));
        searchBooksTable.setRowHeight(25);

        searchBooksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                if (column == buttonColumnIndex) {
                    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                }

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBackground(new Color(0x388A7C));
                label.setOpaque(true);
                label.setForeground(Color.WHITE);
                return label;
            }
        });

        searchBooksTable.getColumnModel().getColumn(buttonColumnIndex).setCellRenderer(new TableCellRenderer() {
                    private final JButton button = new JButton("View");

                    {
                        button.setOpaque(true);
                        button.setBackground(new Color(0x852832));
                        button.setFont(new Font("FiraMono NerdFont", Font.BOLD, 18));
                        button.setForeground(Color.WHITE);
                        button.setFocusPainted(false);
                        button.setBorderPainted(false);
                    }

                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row, int column) {
                        button.setText(value.toString());
                        button.setEnabled(!value.toString().equals("Requested"));
                        return button;
                    }
                }
        );

        searchBooksTable.getColumnModel().getColumn(buttonColumnIndex).setCellEditor(
                new DefaultCellEditor(new JCheckBox()) {
                    private final JButton button = new JButton("View");
                    private int currentRow;

                    {
                        button.setOpaque(true);
                        button.setForeground(Color.WHITE);
                        button.setBackground(new Color(0x2E2D2D));
                        button.setFont(new Font("FiraMono NerdFont", Font.BOLD, 18));
                        button.setFocusPainted(false);
                        button.setBorderPainted(false);

                        button.addActionListener(e -> {
                            fireEditingStopped();
                            String bookTitle = searchBooksTable.getValueAt(currentRow, 0).toString();
                            String author = searchBooksTable.getValueAt(currentRow, 1).toString();
                            JOptionPane.showMessageDialog(
                                    null,
                                    "<html><font color='blue'> Book : </font><html> " + bookTitle + "\n" + "<html><font color = 'blue'> Author: </font><html>" + author,
                                    "Request Confirmed",
                                    JOptionPane.INFORMATION_MESSAGE
                                    );
                        });
                    }

                    @Override
                    public Component getTableCellEditorComponent(JTable table, Object value,
                                                                 boolean isSelected, int row, int column) {
                        currentRow = row;
                        button.setText("Requested");
                        return button;
                    }

                    @Override
                    public Object getCellEditorValue() {
                        return "Requested";
                    }
                }
        );

        searchBooksTable.setRowHeight(35);

        int rowHeight = searchBooksTable.getRowHeight();
        int noOfRows = searchBooksTable.getRowCount();
        searchBooksTable.setPreferredScrollableViewportSize(new Dimension(
                searchBooksTable.getPreferredSize().width,
                rowHeight * noOfRows
        ));

        GridBagConstraints gbc = new GridBagConstraints();

        scrollPane = new JScrollPane(searchBooksTable);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        searchBooksCard.add(scrollPane, gbc);
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
        searchBooksCard.add(Box.createVerticalGlue(), gbc);
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            model.setRowCount(0);
            scrollPane.getViewport().setBackground(new Color(0x212020));
            scrollPane.setBorder(BorderFactory.createEmptyBorder());

            String searchText = searchBox.getText().strip().toUpperCase();
            System.out.println(searchText);

            model.addRow(new Object[] {searchText, "Simon Sinek", "Yes", "Request"});
            model.addRow(new Object[] {"second", "Simon Sinek", "Yes", "Request"});
            model.addRow(new Object[] {"third", "Simon Sinek", "Yes", "Request"});
        }
    }
}