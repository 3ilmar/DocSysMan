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

        assertTrue("User should be added successfully", added);

        boolean loginSuccessful = dao.login(username, "testpass123");

        assertTrue("Newly added user should be able to login", loginSuccessful);
    }

    @Test
    public void testInvalidLoginFails() {
        DatabaseManager.initializeDatabase();

        UserDAO dao = new UserDAO();

        boolean loginSuccessful = dao.login("admin", "wrongpassword");

        assertFalse("Invalid password should not login", loginSuccessful);
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