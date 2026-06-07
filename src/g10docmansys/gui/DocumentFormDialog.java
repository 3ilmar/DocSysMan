package g10docmansys.gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DocumentFormDialog extends JDialog {

    private JTextField txtTitle;
    private JTextField txtDescription;
    private JTextField txtFilePath;
    private JTextField txtCategory;
    private boolean saved;

    public DocumentFormDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setupForm();
    }

    public DocumentFormDialog(java.awt.Frame parent, boolean modal,
            String title, String description, String filePath, String category) {
        super(parent, modal);
        setupForm();

        txtTitle.setText(title);
        txtDescription.setText(description);
        txtFilePath.setText(filePath);
        txtCategory.setText(category);
    }

    private void setupForm() {
        setTitle("Document Form");
        setSize(450, 280);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new GridLayout(5, 2, 12, 12));

        txtTitle = new JTextField();
        txtDescription = new JTextField();
        txtFilePath = new JTextField();
        txtCategory = new JTextField();

        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        add(new JLabel("Title:"));
        add(txtTitle);

        add(new JLabel("Description:"));
        add(txtDescription);

        add(new JLabel("File Path:"));
        add(txtFilePath);

        add(new JLabel("Category:"));
        add(txtCategory);

        add(btnSave);
        add(btnCancel);

        btnSave.addActionListener(e -> saveDocument());
        btnCancel.addActionListener(e -> dispose());
    }

    private void saveDocument() {
        if (txtTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required.");
            return;
        }

        if (txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Description is required.");
            return;
        }

        if (txtFilePath.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "File path is required.");
            return;
        }

        if (txtCategory.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category is required.");
            return;
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    public String getDocumentTitle() {
        return txtTitle.getText().trim();
    }

    public String getDocumentDescription() {
        return txtDescription.getText().trim();
    }

    public String getDocumentFilePath() {
        return txtFilePath.getText().trim();
    }

    public String getDocumentCategory() {
        return txtCategory.getText().trim();
    }
}
