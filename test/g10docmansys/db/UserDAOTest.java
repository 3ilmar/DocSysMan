package g10docmansys.db;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserDAOTest {

    @Test
    public void testAddUserAndLogin() {
        DatabaseManager.initializeDatabase();

        UserDAO dao = new UserDAO();

        String username = "junit_user_" + System.currentTimeMillis();

        boolean added = dao.addUser(
                username,
                "testpass123",
                "user"
        );

        assertTrue(added);

        boolean loginSuccessful = dao.login(username, "testpass123");

        assertTrue(loginSuccessful);
    }

    @Test
    public void testGetUserRole() {
        DatabaseManager.initializeDatabase();

        UserDAO dao = new UserDAO();

        String username = "role_user_" + System.currentTimeMillis();

        dao.addUser(
                username,
                "rolepass123",
                "admin"
        );

        String role = dao.getUserRole(username);

        assertEquals("admin", role);
    }
}