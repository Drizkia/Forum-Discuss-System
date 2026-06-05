package sistemforumdiskusi_19.main;

import sistemforumdiskusi_19.database.DatabaseHelper;
import sistemforumdiskusi_19.gui.LoginFrame;
import sistemforumdiskusi_19.service.ForumService;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main class untuk menjalankan sistem forum dengan GUI.
 */
public class SistemForumDiskusi_19 {
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
            ForumService forumService = new ForumService();
            LoginFrame loginFrame = new LoginFrame(forumService);
            loginFrame.setVisible(true);
        });
    }
}
