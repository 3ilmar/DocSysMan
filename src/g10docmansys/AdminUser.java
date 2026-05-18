package g10docmansys;

public class AdminUser extends User {

    // Constructor
    public AdminUser(String username, String password) {
        super(username, password, "Admin"); // set role automatically
    }

    // Admin can delete documents
    @Override
    public boolean canDeleteDocuments() {
        return true;
    }
}