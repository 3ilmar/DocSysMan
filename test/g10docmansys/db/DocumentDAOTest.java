package g10docmansys.db;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentDAOTest {

    @Test
    public void testAddAndSearchDocument() {
        DatabaseManager.initializeDatabase();

        DocumentDAO dao = new DocumentDAO();

        String uniqueTitle = "JUnit Test Document " + System.currentTimeMillis();

        boolean added = dao.addDocument(
                uniqueTitle,
                "Testing DocumentDAO with Derby",
                "files/junit-test.pdf",
                "Test",
                "junit"
        );

        assertTrue("Document should be added successfully", added);

        List<String> results = dao.searchDocuments(uniqueTitle);

        assertFalse("Search should return the added document", results.isEmpty());
    }

    @Test
    public void testGetAllDocuments() {
        DatabaseManager.initializeDatabase();

        DocumentDAO dao = new DocumentDAO();

        dao.addDocument(
                "All Documents Test " + System.currentTimeMillis(),
                "Testing getAllDocuments method",
                "files/all-documents-test.pdf",
                "Test",
                "junit"
        );

        List<String> documents = dao.getAllDocuments();

        assertFalse("Document list should not be empty", documents.isEmpty());
    }

    @Test
    public void testUpdateDocument() {
        DatabaseManager.initializeDatabase();

        DocumentDAO dao = new DocumentDAO();

        String originalTitle = "Update Test " + System.currentTimeMillis();

        dao.addDocument(
                originalTitle,
                "Before update",
                "files/update-before.pdf",
                "Test",
                "junit"
        );

        List<String> results = dao.searchDocuments(originalTitle);
        assertFalse("Inserted document should be found before update", results.isEmpty());

        String firstResult = results.get(0);
        int id = Integer.parseInt(firstResult.split("\\|")[0].trim());

        boolean updated = dao.updateDocument(
                id,
                "Updated JUnit Document",
                "After update",
                "files/update-after.pdf",
                "UpdatedTest"
        );

        assertTrue("Document should be updated successfully", updated);
    }

    @Test
    public void testDeleteDocument() {
        DatabaseManager.initializeDatabase();

        DocumentDAO dao = new DocumentDAO();

        String title = "Delete Test " + System.currentTimeMillis();

        dao.addDocument(
                title,
                "Testing delete method",
                "files/delete-test.pdf",
                "Test",
                "junit"
        );

        List<String> results = dao.searchDocuments(title);
        assertFalse("Inserted document should be found before delete", results.isEmpty());

        int id = Integer.parseInt(results.get(0).split("\\|")[0].trim());

        boolean deleted = dao.deleteDocumentById(id);

        assertTrue("Document should be deleted successfully", deleted);
    }

    @Test
    public void testDocumentCountIsNotNegative() {
        DatabaseManager.initializeDatabase();

        DocumentDAO dao = new DocumentDAO();

        int count = dao.getDocumentCount();

        assertTrue("Document count should not be negative", count >= 0);
    }
}