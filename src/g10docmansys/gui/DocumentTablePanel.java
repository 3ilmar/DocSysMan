/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package g10docmansys.gui;

import g10docmansys.db.DocumentDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import g10docmansys.db.ActivityLogDAO;

/**
 *
 * @author eilma
 */
public class DocumentTablePanel extends javax.swing.JPanel {

    private JTable documentTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private DocumentDAO documentDAO;
    private ActivityLogDAO activityLogDAO;

    private String currentUsername;
    private String currentRole;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;

    /**
     * Creates new form DocumentTablePanel
     */
    public DocumentTablePanel() {
        this("system", "user");
    }

    public DocumentTablePanel(String username, String role) {
        initComponents();
        this.currentUsername = username;
        this.currentRole = role;
        documentDAO = new DocumentDAO();
        activityLogDAO = new ActivityLogDAO();
        setupPanel();
        loadDocuments();
    }

    private void setupPanel() {
        removeAll();

        setLayout(new BorderLayout());
        setBackground(new java.awt.Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        javax.swing.JLabel title = new javax.swing.JLabel("Documents");
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        txtSearch = new JTextField(22);
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        btnAdd = new JButton("+ Add Document");
        btnEdit = new JButton("Edit Selected");
        btnDelete = new JButton("Delete Selected");

        btnAdd.setBackground(new java.awt.Color(255, 204, 128));

        boolean isAdmin = "admin".equalsIgnoreCase(currentRole);

        btnAdd.setVisible(true);
        btnEdit.setVisible(true);
        btnDelete.setVisible(isAdmin);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Title", "Description", "File Path", "Category", "Owner"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        documentTable = new JTable(tableModel);
        documentTable.setRowHeight(30);
        documentTable.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        documentTable.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

        documentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        documentTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        documentTable.getColumnModel().getColumn(1).setPreferredWidth(240);
        documentTable.getColumnModel().getColumn(2).setPreferredWidth(320);
        documentTable.getColumnModel().getColumn(3).setPreferredWidth(320);
        documentTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        documentTable.getColumnModel().getColumn(5).setPreferredWidth(160);

        documentTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = documentTable.rowAtPoint(e.getPoint());
                int column = documentTable.columnAtPoint(e.getPoint());

                if (row > -1 && column > -1) {
                    Object value = documentTable.getValueAt(row, column);
                    documentTable.setToolTipText(value == null ? "" : value.toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(documentTable);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setOpaque(false);
        topWrapper.add(topPanel, BorderLayout.NORTH);
        topWrapper.add(buttonPanel, BorderLayout.SOUTH);

        add(topWrapper, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadDocuments());
        btnSearch.addActionListener(e -> searchDocuments());
        btnAdd.addActionListener(e -> addDocument());
        btnEdit.addActionListener(e -> editSelectedDocument());
        btnDelete.addActionListener(e -> deleteSelectedDocument());

        revalidate();
        repaint();
    }

    private void loadDocuments() {
        tableModel.setRowCount(0);

        try {
            List<String> documents = documentDAO.getAllDocuments();

            for (String document : documents) {
                addDocumentStringToTable(document);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not load documents: " + e.getMessage());
        }
    }

    private void searchDocuments() {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadDocuments();
            return;
        }

        tableModel.setRowCount(0);

        try {
            List<String> results = documentDAO.searchDocuments(keyword);

            for (String document : results) {
                addDocumentStringToTable(document);
            }

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No documents found.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage());
        }
    }

    private void addDocumentStringToTable(String document) {
        String[] parts = document.split("\\|");

        if (parts.length >= 6) {
            tableModel.addRow(new Object[]{
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                parts[3].trim(),
                parts[4].trim(),
                parts[5].trim()
            });
        }
    }

    private void addDocument() {
        DocumentFormDialog dialog = new DocumentFormDialog(null, true);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            boolean success = documentDAO.addDocument(
                    dialog.getDocumentTitle(),
                    dialog.getDocumentDescription(),
                    dialog.getDocumentFilePath(),
                    dialog.getDocumentCategory(),
                    currentUsername
            );

            if (success) {
                activityLogDAO.logActivity(
                        currentUsername,
                        "Added document",
                        "Title: " + dialog.getDocumentTitle()
                );

                JOptionPane.showMessageDialog(this, "Document added successfully.");
                loadDocuments();
            } else {
                JOptionPane.showMessageDialog(this, "Document could not be added.");
            }
        }
    }

    private void editSelectedDocument() {
        int selectedRow = documentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a document to edit.");
            return;
        }

        Object ownerValue = tableModel.getValueAt(selectedRow, 5);
        String owner = ownerValue == null ? "" : ownerValue.toString();

        boolean isAdmin = "admin".equalsIgnoreCase(currentRole);
        boolean isOwner = owner.equalsIgnoreCase(currentUsername);

        if (!isAdmin && !isOwner) {
            JOptionPane.showMessageDialog(this, "You can only edit documents that you created.");
            return;
        }

        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String title = tableModel.getValueAt(selectedRow, 1).toString();
        String description = tableModel.getValueAt(selectedRow, 2).toString();
        String filePath = tableModel.getValueAt(selectedRow, 3).toString();
        String category = tableModel.getValueAt(selectedRow, 4).toString();

        DocumentFormDialog dialog = new DocumentFormDialog(null, true, title, description, filePath, category);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            boolean success = documentDAO.updateDocument(
                    id,
                    dialog.getDocumentTitle(),
                    dialog.getDocumentDescription(),
                    dialog.getDocumentFilePath(),
                    dialog.getDocumentCategory()
            );

            if (success) {
                activityLogDAO.logActivity(
                        currentUsername,
                        "Updated document",
                        "ID: " + id + ", Title: " + dialog.getDocumentTitle()
                );

                JOptionPane.showMessageDialog(this, "Document updated successfully.");
                loadDocuments();
            } else {
                JOptionPane.showMessageDialog(this, "Document could not be updated.");
            }
        }
    }

    private void deleteSelectedDocument() {
        if (!"admin".equalsIgnoreCase(currentRole)) {
            JOptionPane.showMessageDialog(this, "Only administrators can delete documents.");
            return;
        }
        int selectedRow = documentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a document to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this document?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

            boolean success = documentDAO.deleteDocumentById(id);

            if (success) {
                activityLogDAO.logActivity(
                        currentUsername,
                        "Deleted document",
                        "ID: " + id
                );

                JOptionPane.showMessageDialog(this, "Document deleted successfully.");
                loadDocuments();
            } else {
                JOptionPane.showMessageDialog(this, "Document could not be deleted.");
            }
        }
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
