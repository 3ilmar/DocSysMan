/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package g10docmansys.gui;

import g10docmansys.db.UserDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import g10docmansys.db.ActivityLogDAO;

/**
 *
 * @author eilma
 */
public class ManageUsersPanel extends javax.swing.JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private UserDAO userDAO;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private ActivityLogDAO activityLogDAO;

    /**
     * Creates new form ManageUsersPanel
     */
    public ManageUsersPanel() {
        initComponents();
        userDAO = new UserDAO();
        activityLogDAO = new ActivityLogDAO();
        setupPanel();
    }

    private void setupPanel() {
        removeAll();

        setLayout(new BorderLayout());
        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        JLabel title = new JLabel("Manage Users");
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));

        JLabel subtitle = new JLabel("Create, view, and remove user accounts.");
        subtitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        subtitle.setForeground(new java.awt.Color(120, 120, 120));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(title);
        headerPanel.add(subtitle);

        JPanel formCard = new JPanel(new GridLayout(4, 2, 12, 15));
        formCard.setBackground(java.awt.Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cmbRole = new JComboBox<>(new String[]{"user", "admin"});

        formCard.add(new JLabel("Username:"));
        formCard.add(txtUsername);

        formCard.add(new JLabel("Password:"));
        formCard.add(txtPassword);

        formCard.add(new JLabel("Role:"));
        formCard.add(cmbRole);

        JButton btnAddUser = new JButton("Add User");
        JButton btnDeleteUser = new JButton("Delete Selected User");
        JButton btnRefresh = new JButton("Refresh");

        btnAddUser.setBackground(new java.awt.Color(255, 204, 128));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnAddUser);
        buttonPanel.add(btnDeleteUser);
        buttonPanel.add(btnRefresh);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Username", "Role"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(30);
        userTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        userTable.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        userTable.getColumnModel().getColumn(0).setPreferredWidth(80);    // ID
        userTable.getColumnModel().getColumn(1).setPreferredWidth(500);   // Username
        userTable.getColumnModel().getColumn(2).setPreferredWidth(180);   // Role

        userTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = userTable.rowAtPoint(e.getPoint());
                int column = userTable.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = userTable.getValueAt(row, column);
                    userTable.setToolTipText(value == null ? "" : value.toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(userTable);

        JPanel centerWrapper = new JPanel(new BorderLayout(0, 15));
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JPanel topSection = new JPanel(new BorderLayout(0, 10));
        topSection.setOpaque(false);
        topSection.add(formCard, BorderLayout.NORTH);
        topSection.add(buttonPanel, BorderLayout.SOUTH);

        centerWrapper.add(topSection, BorderLayout.NORTH);
        centerWrapper.add(scrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);

        btnAddUser.addActionListener(e -> addUser());
        btnDeleteUser.addActionListener(e -> deleteSelectedUser());
        btnRefresh.addActionListener(e -> loadUsers());

        loadUsers();

        revalidate();
        repaint();
    }

    private void loadUsers() {
        tableModel.setRowCount(0);

        try {
            List<String> users = userDAO.getAllUsers();

            for (String user : users) {
                String[] parts = user.split("\\|");

                if (parts.length >= 3) {
                    tableModel.addRow(new Object[]{
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim()
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not load users: " + e.getMessage());
        }
    }

    private void addUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String role = cmbRole.getSelectedItem().toString();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required.");
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required.");
            return;
        }

        boolean success = userDAO.addUser(username, password, role);

        if (success) {
            activityLogDAO.logActivity(
                    "system",
                    "Added user",
                    "Username: " + username + ", Role: " + role
            );

            JOptionPane.showMessageDialog(this, "User added successfully.");
            clearForm();
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "User could not be added. The username may already exist.");
        }
    }

    private void deleteUser() {
        String username = txtUsername.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a username to delete.");
            return;
        }

        if (username.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this, "The default admin account cannot be deleted.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete user: " + username + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = userDAO.deleteUser(username);

            if (success) {
                activityLogDAO.logActivity(
                        "system",
                        "Deleted user",
                        "Username: " + username
                );

                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                clearForm();
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "User could not be deleted.");
            }
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            return;
        }

        String username = tableModel.getValueAt(selectedRow, 1).toString();

        if (username.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this, "The default admin account cannot be deleted.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete user: " + username + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = userDAO.deleteUser(username);

            if (success) {
                activityLogDAO.logActivity(
                        "system",
                        "Deleted user",
                        "Username: " + username
                );

                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                clearForm();
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(this, "User could not be deleted.");
            }
        }
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
