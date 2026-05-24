package g10docmansys.db;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentDAOTest {

    @Test
    public void testAddAndSearchDocument() {
        DatabaseManager.initializeDatabase();

        DocumentDAO dao = new DocumentDAO();

        boolean added = dao.addDocument(
                "JUnit Test Document",
                "Testing DocumentDAO with Derby",
                "files/junit-test.pdf",
                "Test"
        );

        assertTrue(added);

        List<String> results = dao.searchDocuments("JUnit");

        assertFalse(results.isEmpty());
    }

    @Test
    public void testGetAllDocuments() {
        DatabaseManager.initializeDatabase();

        DocumentDAO dao = new DocumentDAO();

        dao.addDocument(
                "All Documents Test",
                "Testing getAllDocuments method",
                "files/all-documents-test.pdf",
                "Test"
        );

        List<String> documents = dao.getAllDocuments();

        assertFalse(documents.isEmpty());
    }
}