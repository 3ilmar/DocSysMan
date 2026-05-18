package g10docmansys;

public abstract class User {

    protected String username;
    protected String password;
    protected String role;

    // Constructor
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Setters (only if needed)
    public void setPassword(String password) {
        this.password = password;
    }

    // Abstract method (must be implemented by subclasses)
    public abstract boolean canDeleteDocuments();
}