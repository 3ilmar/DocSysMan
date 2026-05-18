package g10docmansys;

import java.util.ArrayList;
import java.util.Scanner;

public class DocumentManager {

    private ArrayList<Document> documents;
    private Scanner input = new Scanner(System.in);

    // Constructor
    public DocumentManager(ArrayList<Document> documents) {
        this.documents = documents;
    }

    // Add document only if ID is unique
    public void addDocument(Document doc) {
        if (findDocumentById(doc.getId()) == null) {
            documents.add(doc);
            System.out.println("Document added successfully.");
        } else {
            System.out.println("Error: Document ID already exists.");
        }
    }

    // View all documents
    public void viewDocuments() {
        if (documents.isEmpty()) {
            System.out.println("No documents found.");
            return;
        }

        for (Document doc : documents) {
            System.out.println(doc);
        }
    }

    // Search by id, title, author, category, or status
    public void searchDocuments(String keyword) {
        boolean found = false;
        String lowerKeyword = keyword.toLowerCase();

        for (Document doc : documents) {
            if (doc.getId().toLowerCase().contains(lowerKeyword)
                    || doc.getTitle().toLowerCase().contains(lowerKeyword)
                    || doc.getAuthor().toLowerCase().contains(lowerKeyword)
                    || doc.getCategory().toLowerCase().contains(lowerKeyword)
                    || doc.getStatus().toLowerCase().contains(lowerKeyword)) {

                System.out.println(doc);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching documents found.");
        }
    }

    // Find document by ID
    public Document findDocumentById(String id) {
        for (Document doc : documents) {
            if (doc.getId().equalsIgnoreCase(id)) {
                return doc;
            }
        }
        return null;
    }

    // Update document
    public boolean updateDocument(String id, User currentUser) {
        Document doc = findDocumentById(id);

        if (doc == null) {
            System.out.println("Document not found.");
            return false;
        }

        // Admin can update any document
        // User can only update own document
        if (!currentUser.getRole().equals("Admin")
                && !doc.getOwner().equals(currentUser.getUsername())) {
            System.out.println("You can only update your own documents.");
            return false;
        }

        System.out.print("Enter new title: ");
        doc.setTitle(input.nextLine());

        System.out.print("Enter new author: ");
        doc.setAuthor(input.nextLine());

        System.out.print("Enter new category: ");
        doc.setCategory(input.nextLine());

        System.out.print("Enter new description: ");
        doc.setDescription(input.nextLine());

        doc.setVersion(doc.getVersion() + 0.1);

        System.out.println("Document updated successfully.");
        return true;
    }

    // Archive document
    public boolean archiveDocument(String id) {
        Document doc = findDocumentById(id);

        if (doc == null) {
            System.out.println("Document not found.");
            return false;
        }

        doc.setStatus("Archived");
        System.out.println("Document archived successfully.");
        return true;
    }

    // Delete document
    public boolean deleteDocument(String id) {
        Document doc = findDocumentById(id);

        if (doc == null) {
            System.out.println("Document not found.");
            return false;
        }

        documents.remove(doc);
        System.out.println("Document deleted successfully.");
        return true;
    }

    // Getter
    public ArrayList<Document> getDocuments() {
        return documents;
    }
}