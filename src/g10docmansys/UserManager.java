package g10docmansys;

import java.util.ArrayList;

public class UserManager {

    private ArrayList<User> users;

    // Constructor
    public UserManager(ArrayList<User> users) {
        this.users = users;
    }

    // Login method
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) &&
                user.getPassword().equals(password)) {
                return user; // found matching user
            }
        }
        return null; // not found
    }

    // Add new user
    public void addUser(User user) {
        users.add(user);
    }

    // View all users
    public void viewUsers() {
        for (User user : users) {
            System.out.println("Username: " + user.getUsername() +
                               " | Role: " + user.getRole());
        }
    }

    // Check if username already exists
    public boolean usernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Getter for users list
    public ArrayList<User> getUsers() {
        return users;
    }
}