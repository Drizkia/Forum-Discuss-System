package sistemforumdiskusi_19.controller;

import sistemforumdiskusi_19.model.Post;
import sistemforumdiskusi_19.model.User;

import java.util.List;

/**
 * Facade Controller: ForumController menjadi satu titik akses bagi seluruh GUI.
 * Mendelegasikan tugas ke:
 *   - ControllerLogin  : autentikasi (login, register, logout)
 *   - ControllerMain   : navigasi frame
 *   - ForumService     : operasi forum (post, komentar, edit)
 *
 * Prinsip: OCP – fitur baru cukup ditambahkan di service/controller spesifik
 *          tanpa mengubah kelas-kelas GUI yang sudah ada.
 */
public class ForumController {

    private final ControllerLogin controllerLogin;
    private final ControllerMain  controllerMain;
    private final IForumService   forumService;

    public ForumController() {
        this.forumService    = new ForumService();
        this.controllerLogin = new ControllerLogin(forumService);
        this.controllerMain  = new ControllerMain(this);
    }

    // =========================================================
    // Delegasi ke ControllerLogin (Autentikasi)
    // =========================================================

    /**
     * Login berdasarkan username.
     * @return true jika berhasil
     */
    public boolean login(String username) {
        return controllerLogin.login(username);
    }

    /**
     * Registrasi user baru.
     * @return true jika berhasil
     */
    public boolean register(String username, String email) {
        return controllerLogin.register(username, email);
    }

    /**
     * Logout – menghapus sesi user saat ini.
     */
    public void logout() {
        controllerLogin.logout();
        controllerMain.backToLogin();
    }

    /**
     * Mendapatkan user yang sedang login.
     */
    public User getCurrentUser() {
        return controllerLogin.getCurrentUser();
    }

    /**
     * Memeriksa apakah user sudah login.
     */
    public boolean isLoggedIn() {
        return controllerLogin.isLoggedIn();
    }

    // =========================================================
    // Delegasi ke ControllerMain (Navigasi Frame)
    // =========================================================

    /**
     * Membuka frame utama (MainFrame) setelah login sukses.
     */
    public void openMainFrame() {
        controllerMain.openMainFrame();
    }

    /**
     * Membuka frame login awal aplikasi.
     */
    public void openLoginFrame() {
        controllerMain.openLoginFrame();
    }

    /**
     * Entry point utama aplikasi – dipanggil dari main class.
     * Membuka LoginFrame sebagai layar pertama.
     */
    public void startApplication() {
        controllerMain.openLoginFrame();
    }

    // =========================================================
    // Delegasi ke ForumService (Operasi Forum)
    // =========================================================

    /**
     * Membuat postingan baru dengan author dari user yang sedang login.
     */
    public void createPost(String title, String content) {
        forumService.createPost(title, content, getCurrentUser());
    }

    /**
     * Mengedit postingan yang sudah ada berdasarkan ID.
     */
    public void editPost(int postId, String title, String content) {
        forumService.editPost(postId, title, content);
    }

    /**
     * Mengambil seluruh postingan beserta komentar-komentarnya.
     */
    public List<Post> getAllPosts() {
        return forumService.getPosts();
    }

    /**
     * Menambahkan komentar pada postingan tertentu.
     * @param postId  ID postingan yang dikomentari
     * @param content isi komentar
     */
    public void addCommentToPost(int postId, String content) {
        forumService.addCommentToPost(postId, content, getCurrentUser());
    }

    /**
     * Memeriksa apakah username sudah terdaftar (tanpa mengubah sesi).
     */
    public boolean usernameExists(String username) {
        return forumService.usernameExists(username);
    }

    /**
     * Menghapus komentar berdasarkan ID.
     */
    public void deleteComment(int commentId) {
        forumService.deleteComment(commentId);
    }
}
