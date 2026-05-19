package g10docmansys.db;

import java.sql.Connection;
import java.sql.DriverManager;
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
                "CREATE TABLE documents (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "title VARCHAR(255) NOT NULL, " +
                "description VARCHAR(1000), " +
                "file_path VARCHAR(500), " +
                "category VARCHAR(100), " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
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
                "CREATE TABLE users (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "username VARCHAR(100) NOT NULL UNIQUE, " +
                "password VARCHAR(100) NOT NULL, " +
                "role VARCHAR(50) NOT NULL" +
                ")"
            );
        } catch (SQLException e) {
            
            if (!"X0Y32".equals(e.getSQLState())) {
                throw e;
            }
        }
    }
}