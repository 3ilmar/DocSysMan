package g10docmansys.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    public boolean addDocument(String title, String description, String filePath, String category, String owner) {
        String sql = "INSERT INTO documents (title, description, file_path, category, owner) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, filePath);
            statement.setString(4, category);
            statement.setString(5, owner);

            int rowsInserted = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Failed to add document.");
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getAllDocuments() {
        List<String> documents = new ArrayList<String>();

        String sql = "SELECT id, title, description, file_path, category, owner "
                + "FROM documents "
                + "ORDER BY created_at DESC";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String document = resultSet.getInt("id") + " | "
                        + resultSet.getString("title") + " | "
                        + resultSet.getString("description") + " | "
                        + resultSet.getString("file_path") + " | "
                        + resultSet.getString("category") + " | "
                        + resultSet.getString("owner");

                documents.add(document);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to get documents.");
            e.printStackTrace();
        }

        return documents;
    }

    public List<String> searchDocuments(String keyword) {
        List<String> results = new ArrayList<String>();

        String sql = "SELECT id, title, description, file_path, category, owner "
                + "FROM documents "
                + "WHERE LOWER(title) LIKE ? "
                + "OR LOWER(description) LIKE ? "
                + "OR LOWER(category) LIKE ? "
                + "OR LOWER(owner) LIKE ? "
                + "ORDER BY created_at DESC";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            String searchPattern = "%" + keyword.toLowerCase() + "%";

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setString(4, searchPattern);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String document = resultSet.getInt("id") + " | "
                        + resultSet.getString("title") + " | "
                        + resultSet.getString("description") + " | "
                        + resultSet.getString("file_path") + " | "
                        + resultSet.getString("category") + " | "
                        + resultSet.getString("owner");

                results.add(document);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to search documents.");
            e.printStackTrace();
        }

        return results;
    }

    public boolean updateDocument(int id, String title, String description, String filePath, String category) {
        String sql = "UPDATE documents "
                + "SET title = ?, description = ?, file_path = ?, category = ? "
                + "WHERE id = ?";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, filePath);
            statement.setString(4, category);
            statement.setInt(5, id);

            int rowsUpdated = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Failed to update document.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDocumentById(int id) {
        String sql = "DELETE FROM documents WHERE id = ?";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.out.println("Failed to delete document.");
            e.printStackTrace();
            return false;
        }
    }

    public int getDocumentCount() {
        String sql = "SELECT COUNT(*) FROM documents";

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
            System.out.println("Failed to count documents.");
            e.printStackTrace();
            return 0;
        }
    }

    public int getCategoryCount() {
        String sql = "SELECT COUNT(DISTINCT category) FROM documents";

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
            System.out.println("Failed to count categories.");
            e.printStackTrace();
            return 0;
        }
    }

    public List<String> getRecentDocuments() {
        List<String> documents = new ArrayList<String>();

        String sql = "SELECT id, title, description, file_path, category, owner "
                + "FROM documents "
                + "ORDER BY created_at DESC "
                + "FETCH FIRST 5 ROWS ONLY";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String document = resultSet.getInt("id") + " | "
                        + resultSet.getString("title") + " | "
                        + resultSet.getString("description") + " | "
                        + resultSet.getString("file_path") + " | "
                        + resultSet.getString("category") + " | "
                        + resultSet.getString("owner");

                documents.add(document);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to get recent documents.");
            e.printStackTrace();
        }

        return documents;
    }

    public List<String> getCategoryReport() {
        List<String> report = new ArrayList<String>();

        String sql = "SELECT category, COUNT(*) AS total "
                + "FROM documents "
                + "GROUP BY category "
                + "ORDER BY total DESC";

        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                int total = resultSet.getInt("total");

                report.add(category + " | " + total);
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to get category report.");
            e.printStackTrace();
        }

        return report;
    }
}
