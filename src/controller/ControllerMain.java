package sistemforumdiskusi_19.controller;

import sistemforumdiskusi_19.gui.LoginFrame;
import sistemforumdiskusi_19.gui.MainFrame;
import sistemforumdiskusi_19.model.User;

/**
 * SRP: ControllerMain bertanggung jawab untuk navigasi antar frame utama.
 * Mengelola perpindahan dari LoginFrame ke MainFrame dan sebaliknya.
 */
public class ControllerMain {

    private ForumController forumController;

    public ControllerMain(ForumController forumController) {
        this.forumController = forumController;
    }

    /**
     * Membuka LoginFrame sebagai titik awal aplikasi.
     */
    public void openLoginFrame() {
        LoginFrame loginFrame = new LoginFrame(forumController);
        loginFrame.setVisible(true);
    }

    /**
     * Membuka MainFrame setelah user berhasil login.
     * Dipanggil setelah proses autentikasi sukses.
     */
    public void openMainFrame() {
        User currentUser = forumController.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("Tidak dapat membuka MainFrame: user belum login.");
        }
        MainFrame mainFrame = new MainFrame(forumController);
        mainFrame.setVisible(true);
    }

    /**
     * Kembali ke LoginFrame (misalnya setelah logout).
     * Membuka ulang LoginFrame.
     */
    public void backToLogin() {
        openLoginFrame();
    }
}
