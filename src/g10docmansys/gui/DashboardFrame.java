package g10docmansys.gui;

import g10docmansys.db.DocumentDAO;
import g10docmansys.db.UserDAO;
import java.util.List;
import g10docmansys.db.ActivityLogDAO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author eilma
 */
public class DashboardFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardFrame.class.getName());
    private java.awt.CardLayout cardLayout;
    private javax.swing.JPanel contentPanel;
    private String currentUsername;
    private String currentRole;

    /**
     * Creates new form DashboardFrame
     */
    public DashboardFrame() {
        initComponents();
        setupDashboard("Test User", "user");
    }

    public DashboardFrame(String username, String role) {
        initComponents();
        setupDashboard(username, role);
    }

    private void setupDashboard(String username, String role) {
        setTitle("DocSysMan - Document Management System");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        this.currentUsername = username;
        this.currentRole = role;

        java.awt.Color sidebarColor = new java.awt.Color(255, 204, 128);
        java.awt.Color backgroundColor = new java.awt.Color(245, 247, 250);
        java.awt.Color cardColor = java.awt.Color.WHITE;
        java.awt.Color textColor = new java.awt.Color(30, 30, 30);

        java.awt.Font titleFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30);
        java.awt.Font subtitleFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15);
        java.awt.Font sidebarFont = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15);
        java.awt.Font normalFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);

        javax.swing.JPanel root = new javax.swing.JPanel(new java.awt.BorderLayout());
        root.setBackground(backgroundColor);

        // LEFT SIDEBAR
        javax.swing.JPanel sidebar = new javax.swing.JPanel();
        sidebar.setBackground(sidebarColor);
        sidebar.setPreferredSize(new java.awt.Dimension(230, 750));
        sidebar.setLayout(new javax.swing.BoxLayout(sidebar, javax.swing.BoxLayout.Y_AXIS));
        sidebar.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 22, 25, 22));

        javax.swing.JLabel logo = new javax.swing.JLabel("DocSysMan");
        logo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        logo.setForeground(textColor);
        logo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        javax.swing.JButton btnDashboard = createSidebarButton("Dashboard", sidebarFont);
        javax.swing.JButton btnDocuments = createSidebarButton("Documents", sidebarFont);
        javax.swing.JButton btnUsers = createSidebarButton("Manage Users", sidebarFont);
        javax.swing.JButton btnReports = createSidebarButton("Reports", sidebarFont);
        javax.swing.JButton btnLogout = createSidebarButton("Logout", sidebarFont);

        sidebar.add(logo);
        sidebar.add(javax.swing.Box.createVerticalStrut(45));
        sidebar.add(btnDashboard);
        sidebar.add(javax.swing.Box.createVerticalStrut(12));
        sidebar.add(btnDocuments);
        sidebar.add(javax.swing.Box.createVerticalStrut(12));
        if ("admin".equalsIgnoreCase(role)) {
            sidebar.add(btnUsers);
            sidebar.add(javax.swing.Box.createVerticalStrut(12));

            sidebar.add(btnReports);
            sidebar.add(javax.swing.Box.createVerticalStrut(12));
        }

        sidebar.add(javax.swing.Box.createVerticalGlue());

        javax.swing.JLabel version = new javax.swing.JLabel("<html>v1.0.0<br>© 2025 DocSysMan</html>");
        version.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        version.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        sidebar.add(btnLogout);
        sidebar.add(javax.swing.Box.createVerticalStrut(20));
        sidebar.add(version);

        // CARD LAYOUT FOR MAIN PAGES
        cardLayout = new java.awt.CardLayout();
        contentPanel = new javax.swing.JPanel(cardLayout);
        contentPanel.setBackground(backgroundColor);

        javax.swing.JPanel dashboardPanel = createDashboardPage(username, role);
        DocumentTablePanel documentsPanel = new DocumentTablePanel();

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(documentsPanel, "documents");

        if ("admin".equalsIgnoreCase(role)) {
            ManageUsersPanel usersPanel = new ManageUsersPanel();
            contentPanel.add(usersPanel, "users");

            javax.swing.JPanel reportsPanel = createReportsPage();
            contentPanel.add(reportsPanel, "reports");
        }

        root.add(sidebar, java.awt.BorderLayout.WEST);
        root.add(contentPanel, java.awt.BorderLayout.CENTER);

        setContentPane(root);

        btnDashboard.addActionListener(e -> refreshDashboardPage());
        btnDocuments.addActionListener(e -> cardLayout.show(contentPanel, "documents"));

        if ("admin".equalsIgnoreCase(role)) {
            btnUsers.addActionListener(e -> cardLayout.show(contentPanel, "users"));
            btnReports.addActionListener(e -> cardLayout.show(contentPanel, "reports"));
        }

        btnLogout.addActionListener(e -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            this.dispose();
        });

        cardLayout.show(contentPanel, "dashboard");
    }

    private void refreshDashboardPage() {
        contentPanel.removeAll();

        javax.swing.JPanel dashboardPanel = createDashboardPage(currentUsername, currentRole);
        DocumentTablePanel documentsPanel = new DocumentTablePanel();

        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(documentsPanel, "documents");

        if ("admin".equalsIgnoreCase(currentRole)) {
            ManageUsersPanel usersPanel = new ManageUsersPanel();
            contentPanel.add(usersPanel, "users");

            javax.swing.JPanel reportsPanel = createReportsPage();
            contentPanel.add(reportsPanel, "reports");
        }

        contentPanel.revalidate();
        contentPanel.repaint();

        cardLayout.show(contentPanel, "dashboard");
    }

    private javax.swing.JPanel createReportsPage() {
        javax.swing.JPanel page = new javax.swing.JPanel(new java.awt.BorderLayout());
        page.setBackground(new java.awt.Color(245, 247, 250));
        page.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 35, 30, 35));

        javax.swing.JLabel title = new javax.swing.JLabel("Reports");
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));

        javax.swing.JLabel subtitle = new javax.swing.JLabel("System summary and detailed activity log.");
        subtitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        subtitle.setForeground(new java.awt.Color(120, 120, 120));

        javax.swing.JPanel headerPanel = new javax.swing.JPanel(new java.awt.GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(title);
        headerPanel.add(subtitle);

        DocumentDAO documentDAO = new DocumentDAO();
        UserDAO userDAO = new UserDAO();
        ActivityLogDAO activityLogDAO = new ActivityLogDAO();

        int totalDocuments = documentDAO.getDocumentCount();
        int totalUsers = userDAO.getUserCount();
        int totalCategories = documentDAO.getCategoryCount();
        int totalActivities = activityLogDAO.getActivityCount();

        javax.swing.JTextArea reportArea = new javax.swing.JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));

        reportArea.setText(
                "System Report\n\n"
                + "Total Documents: " + totalDocuments + "\n"
                + "Total Users: " + totalUsers + "\n"
                + "Total Categories: " + totalCategories + "\n"
                + "Logged Activities: " + totalActivities + "\n\n"
                + "Category Breakdown:\n"
        );

        java.util.List<String> categoryReport = documentDAO.getCategoryReport();

        if (categoryReport.isEmpty()) {
            reportArea.append("No categories found.\n");
        } else {
            for (String row : categoryReport) {
                reportArea.append(row + "\n");
            }
        }

        javax.swing.JPanel reportCard = new javax.swing.JPanel(new java.awt.BorderLayout());
        reportCard.setBackground(java.awt.Color.WHITE);
        reportCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)),
                javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        javax.swing.JLabel reportTitle = new javax.swing.JLabel("Summary");
        reportTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 17));

        reportCard.add(reportTitle, java.awt.BorderLayout.NORTH);
        reportCard.add(new javax.swing.JScrollPane(reportArea), java.awt.BorderLayout.CENTER);

        String[] columns = {"ID", "Username", "Action", "Details", "Date/Time"};

        javax.swing.table.DefaultTableModel activityModel = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        java.util.List<String> activities = activityLogDAO.getAllActivities();

        for (String activity : activities) {
            String[] parts = activity.split("\\|");

            if (parts.length >= 5) {
                activityModel.addRow(new Object[]{
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim()
                });
            }
        }

        javax.swing.JTable activityTable = new javax.swing.JTable(activityModel);
        activityTable.setRowHeight(30);
        activityTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        activityTable.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        activityTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

        activityTable.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        activityTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Username
        activityTable.getColumnModel().getColumn(2).setPreferredWidth(180);  // Action
        activityTable.getColumnModel().getColumn(3).setPreferredWidth(350);  // Details
        activityTable.getColumnModel().getColumn(4).setPreferredWidth(180);  // Date/Time

        activityTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = activityTable.rowAtPoint(e.getPoint());
                int column = activityTable.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = activityTable.getValueAt(row, column);
                    activityTable.setToolTipText(value == null ? "" : value.toString());
                }
            }
        });

        javax.swing.JPanel activityCard = new javax.swing.JPanel(new java.awt.BorderLayout());
        activityCard.setBackground(java.awt.Color.WHITE);
        activityCard.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)),
                javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        javax.swing.JLabel activityTitle = new javax.swing.JLabel("Activity Log");
        activityTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 17));

        activityCard.add(activityTitle, java.awt.BorderLayout.NORTH);
        activityCard.add(new javax.swing.JScrollPane(activityTable), java.awt.BorderLayout.CENTER);

        javax.swing.JPanel contentPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 0, 0, 0));

        contentPanel.add(reportCard);
        contentPanel.add(activityCard);

        page.add(headerPanel, java.awt.BorderLayout.NORTH);
        page.add(contentPanel, java.awt.BorderLayout.CENTER);

        return page;
    }

    private javax.swing.JButton createSidebarButton(String text, java.awt.Font font) {
        javax.swing.JButton button = new javax.swing.JButton(text);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        button.setMaximumSize(new java.awt.Dimension(190, 42));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    private javax.swing.JPanel createDashboardPage(String username, String role) {
        java.awt.Color backgroundColor = new java.awt.Color(245, 247, 250);
        java.awt.Color cardColor = java.awt.Color.WHITE;

        javax.swing.JPanel page = new javax.swing.JPanel(new java.awt.BorderLayout());
        page.setBackground(backgroundColor);
        page.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 35, 30, 35));

        // TOP HEADER
        javax.swing.JPanel headerPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        headerPanel.setOpaque(false);

        javax.swing.JPanel titlePanel = new javax.swing.JPanel();
        titlePanel.setLayout(new javax.swing.BoxLayout(titlePanel, javax.swing.BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        javax.swing.JLabel title = new javax.swing.JLabel("Document Management System");
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));

        javax.swing.JLabel subtitle = new javax.swing.JLabel(role + " Dashboard");
        subtitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 15));
        subtitle.setForeground(new java.awt.Color(120, 120, 120));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        javax.swing.JButton profileButton = new javax.swing.JButton(username + "  ▼");
        profileButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        profileButton.setFocusPainted(false);
        profileButton.setBackground(java.awt.Color.WHITE);

        javax.swing.JPopupMenu profileMenu = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem profileItem = new javax.swing.JMenuItem("Profile");
        javax.swing.JMenuItem logoutItem = new javax.swing.JMenuItem("Logout");

        profileMenu.add(profileItem);
        profileMenu.addSeparator();
        profileMenu.add(logoutItem);

        profileButton.addActionListener(e -> profileMenu.show(profileButton, 0, profileButton.getHeight()));

        profileItem.addActionListener(e
                -> javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Username: " + username + "\nRole: " + role,
                        "Profile",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                )
        );

        logoutItem.addActionListener(e -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            this.dispose();
        });

        headerPanel.add(titlePanel, java.awt.BorderLayout.WEST);
        headerPanel.add(profileButton, java.awt.BorderLayout.EAST);

        // SUMMARY CARDS
        javax.swing.JPanel cardsPanel = new javax.swing.JPanel(new java.awt.GridLayout(1, 4, 18, 18));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 0, 25, 0));

        DocumentDAO documentDAO = new DocumentDAO();
        UserDAO userDAO = new UserDAO();

        int totalDocuments = documentDAO.getDocumentCount();
        int totalUsers = userDAO.getUserCount();
        int totalCategories = documentDAO.getCategoryCount();

        cardsPanel.add(createStatCard("Total Documents", String.valueOf(totalDocuments), "All time"));
        cardsPanel.add(createStatCard("Users", String.valueOf(totalUsers), "System users"));
        cardsPanel.add(createStatCard("Categories", String.valueOf(totalCategories), "Document groups"));
        cardsPanel.add(createStatCard("Status", "Active", "System running"));

        // MAIN CONTENT AREA
        javax.swing.JPanel mainContent = new javax.swing.JPanel(new java.awt.BorderLayout(20, 0));
        mainContent.setOpaque(false);

        javax.swing.JPanel recentDocuments = createRecentDocumentsPreview();
        javax.swing.JPanel activityPanel = createRecentActivityPanel();

        mainContent.add(recentDocuments, java.awt.BorderLayout.CENTER);
        mainContent.add(activityPanel, java.awt.BorderLayout.EAST);

        javax.swing.JPanel centerPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(cardsPanel, java.awt.BorderLayout.NORTH);
        centerPanel.add(mainContent, java.awt.BorderLayout.CENTER);

        page.add(headerPanel, java.awt.BorderLayout.NORTH);
        page.add(centerPanel, java.awt.BorderLayout.CENTER);

        return page;
    }

    private javax.swing.JPanel createStatCard(String title, String value, String subtitle) {
        javax.swing.JPanel card = new javax.swing.JPanel(new java.awt.BorderLayout());
        card.setBackground(java.awt.Color.WHITE);
        card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)),
                javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        javax.swing.JLabel titleLabel = new javax.swing.JLabel(title);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        titleLabel.setForeground(new java.awt.Color(120, 120, 120));

        javax.swing.JLabel valueLabel = new javax.swing.JLabel(value);
        valueLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));

        javax.swing.JLabel subtitleLabel = new javax.swing.JLabel(subtitle);
        subtitleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        subtitleLabel.setForeground(new java.awt.Color(120, 120, 120));

        card.add(titleLabel, java.awt.BorderLayout.NORTH);
        card.add(valueLabel, java.awt.BorderLayout.CENTER);
        card.add(subtitleLabel, java.awt.BorderLayout.SOUTH);

        return card;
    }

    private javax.swing.JPanel createRecentDocumentsPreview() {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout());
        panel.setBackground(java.awt.Color.WHITE);
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)),
                javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        javax.swing.JPanel top = new javax.swing.JPanel(new java.awt.BorderLayout());
        top.setOpaque(false);

        javax.swing.JLabel title = new javax.swing.JLabel("Recent Documents");
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 17));

        javax.swing.JPanel actions = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        actions.setOpaque(false);

        top.add(title, java.awt.BorderLayout.WEST);
        top.add(actions, java.awt.BorderLayout.EAST);

        String[] columns = {"ID", "Title", "Description", "File Path", "Category"};

        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DocumentDAO documentDAO = new DocumentDAO();
        List<String> recentDocuments = documentDAO.getRecentDocuments();

        for (String document : recentDocuments) {
            String[] parts = document.split("\\|");

            if (parts.length >= 5) {
                model.addRow(new Object[]{
                    parts[0].trim(),
                    parts[1].trim(),
                    parts[2].trim(),
                    parts[3].trim(),
                    parts[4].trim()
                });
            }
        }

        javax.swing.JTable table = new javax.swing.JTable(model);
        table.setRowHeight(32);
        table.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);

        table.getColumnModel().getColumn(0).setPreferredWidth(60);    // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(180);   // Title
        table.getColumnModel().getColumn(2).setPreferredWidth(260);   // Description
        table.getColumnModel().getColumn(3).setPreferredWidth(260);   // File Path
        table.getColumnModel().getColumn(4).setPreferredWidth(180);   // Category

        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = table.getValueAt(row, column);
                    table.setToolTipText(value == null ? "" : value.toString());
                }
            }
        });

        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);

        panel.add(top, java.awt.BorderLayout.NORTH);
        panel.add(scrollPane, java.awt.BorderLayout.CENTER);

        return panel;
    }

    private javax.swing.JPanel createRecentActivityPanel() {
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.setPreferredSize(new java.awt.Dimension(270, 0));
        panel.setBackground(java.awt.Color.WHITE);
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)),
                javax.swing.BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        javax.swing.JLabel title = new javax.swing.JLabel("Recent Activity");
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 17));
        title.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(javax.swing.Box.createVerticalStrut(18));

        ActivityLogDAO activityLogDAO = new ActivityLogDAO();
        java.util.List<String> activities = activityLogDAO.getRecentActivities();

        if (activities.isEmpty()) {
            panel.add(createActivityLabel("No recent activity yet."));
        } else {
            for (String activity : activities) {
                String[] parts = activity.split("\\|");

                if (parts.length >= 5) {
                    String username = parts[1].trim();
                    String action = parts[2].trim();
                    String details = parts[3].trim();

                    panel.add(createActivityLabel(username + " - " + action + ": " + details));
                }
            }
        }

        return panel;
    }

    private javax.swing.JLabel createActivityLabel(String text) {
        String displayText = "• " + text;

        javax.swing.JLabel label = new javax.swing.JLabel(
                "<html><body style='width:210px'>" + displayText + "</body></html>"
        );

        label.setToolTipText(displayText);
        label.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 8, 0));
        label.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        return label;
    }

    private javax.swing.JPanel createPlaceholderPage(String heading, String message) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout());
        panel.setBackground(new java.awt.Color(245, 247, 250));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));

        javax.swing.JLabel title = new javax.swing.JLabel(heading);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));

        javax.swing.JLabel body = new javax.swing.JLabel(message);
        body.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 16));

        panel.add(title, java.awt.BorderLayout.NORTH);
        panel.add(body, java.awt.BorderLayout.CENTER);

        return panel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new DashboardFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
