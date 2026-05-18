package g10docmansys;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    private final String DOCUMENT_FILE = "documents.txt";
    private final String USER_FILE = "users.txt";

    // Save documents to documents.txt
    public void saveDocuments(ArrayList<Document> documents) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(DOCUMENT_FILE));

            for (Document doc : documents) {
                writer.println(doc.toFileString());
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving documents.");
        }
    }

    // Load documents from documents.txt
    public ArrayList<Document> loadDocuments() {
        ArrayList<Document> documents = new ArrayList<>();
        File file = new File(DOCUMENT_FILE);

        if (!file.exists()) {
            return documents;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 8) {
                    String id = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String category = parts[3];
                    String description = parts[4];
                    String status = parts[5];
                    double version = Double.parseDouble(parts[6]);
                    String owner = parts[7];

                    Document doc = new Document(id, title, author, category,
                            description, status, version, owner);

                    documents.add(doc);
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading documents.");
        } catch (NumberFormatException e) {
            System.out.println("Error reading document version number.");
        }

        return documents;
    }

    // Save users to users.txt
    public void saveUsers(ArrayList<User> users) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE));

            for (User user : users) {
                writer.println(user.getUsername() + "|" +
                               user.getPassword() + "|" +
                               user.getRole());
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    // Load users from users.txt
    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(USER_FILE);

        if (!file.exists() || file.length() == 0) {
            users.add(new AdminUser("admin", "admin123"));
            users.add(new StaffUser("user", "user123"));
            saveUsers(users);
            return users;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];

                    if (role.equalsIgnoreCase("Admin")) {
                        users.add(new AdminUser(username, password));
                    } else if (role.equalsIgnoreCase("User")) {
                        users.add(new StaffUser(username, password));
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading users.");
        }

        return users;
    }
}