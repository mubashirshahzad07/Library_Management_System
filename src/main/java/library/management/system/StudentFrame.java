package library.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @since 28 April 2026
 * Handles the Student Window
 */
public class StudentFrame extends JFrame implements ActionListener {
       private JButton dashBoard, searchBooks, myBooks, history, signOut;
       private JPanel dashboardCard, myBooksCard, searchBookCard, historyCard;
       private JFrame loginFrame;

       StudentFrame(JFrame loginFrame) {
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

              ImageIcon studentIcon = new ImageIcon(ClassLoader.getSystemResource("student_icon.png"));

              JLabel studentLabel = new JLabel("Dummy Student");
              studentLabel.setIcon(studentIcon);
              studentLabel.setForeground(Color.WHITE);
              studentLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 18));
              gridBagConstraints.gridy = 0;
              optionsPanel.add(studentLabel, gridBagConstraints);

              JLabel myLibraryLabel = new JLabel("MY LIBRARY");
              myLibraryLabel.setForeground(Color.WHITE);
              myLibraryLabel.setFont(new Font("FiraMono NerdFonts", Font.BOLD, 15));
              gridBagConstraints.gridy = 1;
              optionsPanel.add(myLibraryLabel, gridBagConstraints);

              dashBoard = new JButton("Dashboard");
              dashBoard.setBackground(Color.DARK_GRAY);
              dashBoard.setForeground(Color.WHITE);
              dashBoard.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
              dashBoard.setFocusable(false);
              gridBagConstraints.gridy = 2;
              optionsPanel.add(dashBoard, gridBagConstraints);

              searchBooks = new JButton("Search books");
              searchBooks.setBackground(Color.DARK_GRAY);
              searchBooks.setForeground(Color.WHITE);
              searchBooks.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
              searchBooks.setFocusable(false);
              gridBagConstraints.gridy = 3;
              optionsPanel.add(searchBooks, gridBagConstraints);

              myBooks = new JButton("My books");
              myBooks.setBackground(Color.DARK_GRAY);
              myBooks.setForeground(Color.WHITE);
              myBooks.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
              myBooks.setFocusable(false);
              gridBagConstraints.gridy = 4;
              optionsPanel.add(myBooks, gridBagConstraints);

              history = new JButton("History");
              history.setBackground(Color.DARK_GRAY);
              history.setForeground(Color.WHITE);
              history.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
              history.setFocusable(false);
              gridBagConstraints.gridy = 5;
              optionsPanel.add(history, gridBagConstraints);

              signOut = new JButton("Sign out");
              signOut.setBackground(Color.DARK_GRAY);
              signOut.setForeground(Color.WHITE);
              signOut.setFont(new Font("FiraMono NerdFonts", Font.PLAIN, 18));
              signOut.setFocusable(false);
              gridBagConstraints.gridy = 6;
              gridBagConstraints.insets = new Insets(200, 5, 5, 5);
              optionsPanel.add(signOut, gridBagConstraints);

              CardLayout cardLayout = new CardLayout();
              JPanel contentPanel = new JPanel(cardLayout);
              contentPanel.setBackground(Color.BLACK);

              // dashboard card
              dashboardCard = new JPanel(new GridBagLayout());
              dashboardCard.setBackground(new Color(0x212020));
              contentPanel.add(dashboardCard, "DASHBOARD CARD");
              new StudentFrameDashboardCard(dashboardCard);

              // search books card
              searchBookCard = new JPanel(new GridBagLayout());
              searchBookCard.setBackground(new Color(0x212020));
              new StudentFrameSearchBooksCard(searchBookCard);
              contentPanel.add(searchBookCard, "SEARCH BOOKS CARD");

              // my books card
              myBooksCard = new JPanel(new GridBagLayout());
              myBooksCard.setBackground(new Color(0x212020));
              new StudentFrameMyBooksCard(myBooksCard);
              contentPanel.add(myBooksCard, "MY BOOKS CARD");

              // history card
              historyCard = new JPanel(new GridBagLayout());
              historyCard.setBackground(new Color(0x212020));
              new StudentFrameHistoryCard(historyCard);
              contentPanel.add(historyCard, "HISTORY CARD");

              dashBoard.addActionListener(actionEvent -> cardLayout.show(contentPanel, "DASHBOARD CARD"));
              searchBooks.addActionListener(actionEvent -> cardLayout.show(contentPanel, "SEARCH BOOKS CARD"));
              myBooks.addActionListener(actionEvent -> cardLayout.show(contentPanel, "MY BOOKS CARD"));
              history.addActionListener(actionEvent -> cardLayout.show(contentPanel, "HISTORY CARD"));
//              signOut.addActionListener(actionEvent -> this.dispose());
              signOut.addActionListener(this);

              JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, optionsPanel, contentPanel);
              splitPane.setPreferredSize(new Dimension(2 * screenWidth/3, 2 * screenHeight/3));
              splitPane.setDividerLocation((int) ( screenWidth / 5 ));
              splitPane.setDividerSize(0);

              this.setTitle("Student");
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