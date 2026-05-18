package g10docmansys;

public class StaffUser extends User {

    // Constructor
    public StaffUser(String username, String password) {
        super(username, password, "User"); // set role automatically
    }

    // Staff/User cannot delete documents
    @Override
    public boolean canDeleteDocuments() {
        return false;
    }
}