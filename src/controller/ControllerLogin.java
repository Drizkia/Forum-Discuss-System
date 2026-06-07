package sistemforumdiskusi_19.controller;

import sistemforumdiskusi_19.model.User;

/**
 * SRP: ControllerLogin bertanggung jawab hanya untuk logika autentikasi.
 * Menangani proses login dan registrasi user.
 */
public class ControllerLogin {

    private IForumService forumService;
    private User currentUser;

    public ControllerLogin(IForumService forumService) {
        this.forumService = forumService;
    }

    // Melakukan proses login berdasarkan username.
    public boolean login(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        User user = forumService.loginUser(username.trim());
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    // Melakukan registrasi user baru.
    public boolean register(String username, String email) {
        if (username == null || username.trim().isEmpty()
                || email == null || email.trim().isEmpty()) {
            return false;
        }
        User user = forumService.registerUser(username.trim(), email.trim());
        return user != null;
    }

    // Mengeluarkan user yang sedang login (reset sesi).
    public void logout() {
        this.currentUser = null;
    }

    // Mendapatkan user yang sedang login.
    public User getCurrentUser() {
        return currentUser;
    }
    
    // Memeriksa apakah ada user yang sedang login.
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
