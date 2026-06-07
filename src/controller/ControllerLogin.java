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

    /**
     * Melakukan proses login berdasarkan username.
     * @param username username yang dimasukkan
     * @return true jika login berhasil, false jika username tidak ditemukan
     */
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

    /**
     * Melakukan registrasi user baru.
     * @param username username baru
     * @param email    email user baru
     * @return true jika registrasi berhasil, false jika gagal
     */
    public boolean register(String username, String email) {
        if (username == null || username.trim().isEmpty()
                || email == null || email.trim().isEmpty()) {
            return false;
        }
        User user = forumService.registerUser(username.trim(), email.trim());
        return user != null;
    }

    /**
     * Mengeluarkan user yang sedang login (reset sesi).
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Mengambil user yang sedang login.
     * @return objek User atau null jika belum login
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Memeriksa apakah ada user yang sedang login.
     * @return true jika sudah login
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
