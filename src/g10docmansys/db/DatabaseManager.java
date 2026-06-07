package g10docmansys.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:derby:db/dmsdb;create=true";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            createDocumentsTable(statement);
            createUsersTable(statement);
            createActivityLogTable(statement);
            insertDefaultUsers(connection);

            statement.close();
            connection.close();

            System.out.println("Derby database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Database initialization failed.");
            e.printStackTrace();
        }
    }

    private static void createDocumentsTable(Statement statement) throws SQLException {
        try {
            statement.executeUpdate(
                    "CREATE TABLE documents ("
                    + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                    + "title VARCHAR(255) NOT NULL, "
                    + "description VARCHAR(1000), "
                    + "file_path VARCHAR(500), "
                    + "category VARCHAR(100), "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")"
            );
        } catch (SQLException e) {

            if (!"X0Y32".equals(e.getSQLState())) {
                throw e;
            }
        }
    }

    private static void createUsersTable(Statement statement) throws SQLException {
        try {
            statement.executeUpdate(
                    "CREATE TABLE users ("
                    + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                    + "username VARCHAR(100) NOT NULL UNIQUE, "
                    + "password VARCHAR(100) NOT NULL, "
                    + "role VARCHAR(50) NOT NULL"
                    + ")"
            );
        } catch (SQLException e) {

            if (!"X0Y32".equals(e.getSQLState())) {
                throw e;
            }
        }
    }

    private static void createActivityLogTable(Statement statement) throws SQLException {
        try {
            statement.executeUpdate(
                    "CREATE TABLE activity_log ("
                    + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                    + "username VARCHAR(100), "
                    + "action VARCHAR(100) NOT NULL, "
                    + "details VARCHAR(1000), "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")"
            );
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) {
                throw e;
            }
        }
    }

    private static void insertDefaultUsers(Connection connection) throws SQLException {
        insertUserIfNotExists(connection, "admin", "admin123", "admin");
        insertUserIfNotExists(connection, "user", "user123", "user");
    }

    private static void insertUserIfNotExists(Connection connection, String username, String password, String role) throws SQLException {
        String checkSql = "SELECT id FROM users WHERE username = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkSql);
        checkStatement.setString(1, username);
        boolean exists = checkStatement.executeQuery().next();
        checkStatement.close();

        if (!exists) {
            String insertSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, username);
            insertStatement.setString(2, password);
            insertStatement.setString(3, role);
            insertStatement.executeUpdate();
            insertStatement.close();
        }
    }
}
