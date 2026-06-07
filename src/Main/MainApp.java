package Main;



import sistemforumdiskusi_19.database.DatabaseHelper;
import sistemforumdiskusi_19.controller.ForumController;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main class untuk menjalankan sistem forum dengan GUI dan pola MVC.
 */

public class MainApp {
    public static void main(String[] args) {
        // Initialize Database
        DatabaseHelper.initializeDatabase();

        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Jalankan GUI di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ForumController controller = new ForumController();
            controller.startApplication();
        });
    }
}
