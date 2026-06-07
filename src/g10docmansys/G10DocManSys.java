package g10docmansys;

import g10docmansys.db.DatabaseManager;
import g10docmansys.gui.LoginFrame;
import javax.swing.SwingUtilities;

public class G10DocManSys {

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}