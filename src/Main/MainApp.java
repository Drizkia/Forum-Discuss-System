package Main;

import sistemforumdiskusi_19.database.DatabaseHelper;
import sistemforumdiskusi_19.controller.ForumController;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main class untuk menjalankan sistem forum
 */

public class MainApp {
    public static void main(String[] args) {
        // Inisiasi Database
        DatabaseHelper.initializeDatabase();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            ForumController controller = new ForumController();
            controller.startApplication();
        });
    }
}
