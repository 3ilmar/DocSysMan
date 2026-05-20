package g10docmansys.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean addUser(String username, String password, String role) {
        String sql = "INSERT INTO users (username, password, role) "
                + "VALUES (?, ?, ?)";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            int rowsInserted = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Failed to add user.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            boolean loginSuccessful = resultSet.next();

            resultSet.close();
            statement.close();
            connection.close();

            return loginSuccessful;

        } catch (SQLException e) {
            System.out.println("Login check failed.");
            e.printStackTrace();
            return false;
        }
    }

    public String getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            String role = null;

            if (resultSet.next()) {
                role = resultSet.getString("role");
            }

            resultSet.close();
            statement.close();
            connection.close();

            return role;

        } catch (SQLException e) {
            System.out.println("Failed to get user role.");
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);

            int rowsDeleted = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Failed to delete user.");
            e.printStackTrace();
            return false;
        }
    }
}