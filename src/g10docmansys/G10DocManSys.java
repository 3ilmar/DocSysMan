package g10docmansys;

import g10docmansys.db.DatabaseManager;

public class G10DocManSys {

    public static void main(String[] args) {
        
        DatabaseManager.initializeDatabase();
        
        // Create MenuController object
        MenuController menu = new MenuController();
        
        // Start the system
        menu.start();
    }
}