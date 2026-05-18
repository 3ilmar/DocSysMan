package g10docmansys;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuController {

    private Scanner input;
    private FileHandler fileHandler;
    private UserManager userManager;
    private DocumentManager documentManager;
    private User currentUser;

    public MenuController() {
        input = new Scanner(System.in);
        fileHandler = new FileHandler();

        ArrayList<User> users = fileHandler.loadUsers();
        ArrayList<Document> documents = fileHandler.loadDocuments();

        userManager = new UserManager(users);
        documentManager = new DocumentManager(documents);
    }

    public void start() {
        System.out.println("Welcome to G10 Document Management System");
        loginMenu();

        if (currentUser != null) {
            if (currentUser.getRole().equalsIgnoreCase("Admin")) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }
    }

    public void loginMenu() {
        while (currentUser == null) {
            System.out.print("Username: ");
            String username = input.nextLine();

            System.out.print("Password: ");
            String password = input.nextLine();

            currentUser = userManager.login(username, password);

            if (currentUser == null) {
                System.out.println("Invalid login. Try again.");
            } else {
                System.out.println("Login successful. Welcome " + currentUser.getUsername());
            }
        }
    }

    public void showAdminMenu() {
        int choice = 0;

        while (choice != 8) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Document");
            System.out.println("2. View Documents");
            System.out.println("3. Search Documents");
            System.out.println("4. Update Document");
            System.out.println("5. Archive Document");
            System.out.println("6. Delete Document");
            System.out.println("7. Manage Users");
            System.out.println("8. Exit");
            System.out.print("Choose option: ");

            choice = getIntInput();

            if (choice == 1) {
                addDocumentMenu();
            } else if (choice == 2) {
                documentManager.viewDocuments();
            } else if (choice == 3) {
                searchDocumentMenu();
            } else if (choice == 4) {
                updateDocumentMenu();
            } else if (choice == 5) {
                archiveDocumentMenu();
            } else if (choice == 6) {
                deleteDocumentMenu();
            } else if (choice == 7) {
                manageUsersMenu();
            } else if (choice == 8) {
                System.out.println("Exiting system...");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    public void showUserMenu() {
        int choice = 0;

        while (choice != 5) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Add Document");
            System.out.println("2. View Documents");
            System.out.println("3. Search Documents");
            System.out.println("4. Update Own Document");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            choice = getIntInput();

            if (choice == 1) {
                addDocumentMenu();
            } else if (choice == 2) {
                documentManager.viewDocuments();
            } else if (choice == 3) {
                searchDocumentMenu();
            } else if (choice == 4) {
                updateDocumentMenu();
            } else if (choice == 5) {
                System.out.println("Exiting system...");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    public void addDocumentMenu() {
        System.out.print("Enter document ID: ");
        String id = input.nextLine();

        if (documentManager.findDocumentById(id) != null) {
            System.out.println("Document ID already exists.");
            return;
        }

        System.out.print("Enter title: ");
        String title = input.nextLine();

        System.out.print("Enter author: ");
        String author = input.nextLine();

        System.out.print("Enter category: ");
        String category = input.nextLine();

        System.out.print("Enter description: ");
        String description = input.nextLine();

        String status = "Active";
        double version = 1.0;
        String owner = currentUser.getUsername();

        Document doc = new Document(id, title, author, category, description, status, version, owner);

        documentManager.addDocument(doc);
        fileHandler.saveDocuments(documentManager.getDocuments());
    }

    public void searchDocumentMenu() {
        System.out.print("Enter search keyword: ");
        String keyword = input.nextLine();

        documentManager.searchDocuments(keyword);
    }

    public void updateDocumentMenu() {
        System.out.print("Enter document ID to update: ");
        String id = input.nextLine();

        boolean updated = documentManager.updateDocument(id, currentUser);

        if (updated) {
            fileHandler.saveDocuments(documentManager.getDocuments());
        }
    }

    public void archiveDocumentMenu() {
        if (!currentUser.getRole().equalsIgnoreCase("Admin")) {
            System.out.println("Only Admin can archive documents.");
            return;
        }

        System.out.print("Enter document ID to archive: ");
        String id = input.nextLine();

        boolean archived = documentManager.archiveDocument(id);

        if (archived) {
            fileHandler.saveDocuments(documentManager.getDocuments());
        }
    }

    public void deleteDocumentMenu() {
        if (!currentUser.canDeleteDocuments()) {
            System.out.println("Only Admin can delete documents.");
            return;
        }

        System.out.print("Enter document ID to delete: ");
        String id = input.nextLine();

        boolean deleted = documentManager.deleteDocument(id);

        if (deleted) {
            fileHandler.saveDocuments(documentManager.getDocuments());
        }
    }

    public void manageUsersMenu() {
        if (!currentUser.getRole().equalsIgnoreCase("Admin")) {
            System.out.println("Only Admin can manage users.");
            return;
        }

        int choice = 0;

        while (choice != 3) {
            System.out.println("\n--- Manage Users ---");
            System.out.println("1. Add User");
            System.out.println("2. View Users");
            System.out.println("3. Back");
            System.out.print("Choose option: ");

            choice = getIntInput();

            if (choice == 1) {
                System.out.print("Enter username: ");
                String username = input.nextLine();

                if (userManager.usernameExists(username)) {
                    System.out.println("Username already exists.");
                    continue;
                }

                System.out.print("Enter password: ");
                String password = input.nextLine();

                System.out.print("Enter role (Admin/User): ");
                String role = input.nextLine();

                if (role.equalsIgnoreCase("Admin")) {
                    userManager.addUser(new AdminUser(username, password));
                    fileHandler.saveUsers(userManager.getUsers());
                    System.out.println("Admin user added.");
                } else if (role.equalsIgnoreCase("User")) {
                    userManager.addUser(new StaffUser(username, password));
                    fileHandler.saveUsers(userManager.getUsers());
                    System.out.println("User added.");
                } else {
                    System.out.println("Invalid role.");
                }

            } else if (choice == 2) {
                userManager.viewUsers();
            } else if (choice == 3) {
                System.out.println("Returning to Admin menu...");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private int getIntInput() {
        try {
            int number = Integer.parseInt(input.nextLine());
            return number;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}