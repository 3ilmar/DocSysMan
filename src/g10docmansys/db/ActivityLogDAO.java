package g10docmansys.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDAO {

    public boolean logActivity(String username, String action, String details) {
        String sql = "INSERT INTO activity_log (username, action, details) VALUES (?, ?, ?)";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, action);
            statement.setString(3, details);

            int rowsInserted = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Failed to log activity.");
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getRecentActivities() {
        List<String> activities = new ArrayList<String>();

        String sql = "SELECT id, username, action, details, created_at "
                + "FROM activity_log "
                + "ORDER BY created_at DESC "
                + "FETCH FIRST 5 ROWS ONLY";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String activity = resultSet.getInt("id") + " | "
                        + resultSet.getString("username") + " | "
                        + resultSet.getString("action") + " | "
                        + resultSet.getString("details") + " | "
                        + resultSet.getTimestamp("created_at");

                activities.add(activity);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to get recent activities.");
            e.printStackTrace();
        }

        return activities;
    }

    public List<String> getAllActivities() {
        List<String> activities = new ArrayList<String>();

        String sql = "SELECT id, username, action, details, created_at "
                + "FROM activity_log "
                + "ORDER BY created_at DESC";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String activity = resultSet.getInt("id") + " | "
                        + resultSet.getString("username") + " | "
                        + resultSet.getString("action") + " | "
                        + resultSet.getString("details") + " | "
                        + resultSet.getTimestamp("created_at");

                activities.add(activity);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to get all activities.");
            e.printStackTrace();
        }

        return activities;
    }

    public int getActivityCount() {
        String sql = "SELECT COUNT(*) FROM activity_log";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            int count = 0;

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            resultSet.close();
            statement.close();
            connection.close();

            return count;

        } catch (SQLException e) {
            System.out.println("Failed to count activities.");
            e.printStackTrace();
            return 0;
        }
    }
}
