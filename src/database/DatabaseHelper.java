package sistemforumdiskusi_19.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
    DatabaseHelper: Kelas ini bertanggung jawab untuk mengelola koneksi dan inisialisasi database.
    Secara spesifik, kelas ini memiliki method static getConnection() yang mengembalikan koneksi ke database SQLite 
    dan method initializeDatabase() yang membuat tabel users, posts, dan comments jika belum ada.
    
    Penjelasan: 
    - getConnection(): Mengembalikan koneksi ke database SQLite.
    - initializeDatabase(): Membuat tabel users, posts, dan comments jika belum ada.
    - Activate foreign key constraints: Mengaktifkan foreign key constraints.
*/

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:src/database/forum.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT UNIQUE NOT NULL,"
                + "email TEXT NOT NULL"
                + ");";

        String createPostsTable = "CREATE TABLE IF NOT EXISTS posts ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "title TEXT NOT NULL,"
                + "content TEXT NOT NULL,"
                + "author_id INTEGER NOT NULL,"
                + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(author_id) REFERENCES users(id)"
                + ");";

        String createCommentsTable = "CREATE TABLE IF NOT EXISTS comments ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "post_id INTEGER NOT NULL,"
                + "content TEXT NOT NULL,"
                + "author_id INTEGER NOT NULL,"
                + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(post_id) REFERENCES posts(id),"
                + "FOREIGN KEY(author_id) REFERENCES users(id)"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Activate foreign key constraints
            stmt.execute("PRAGMA foreign_keys = ON;");
            
            stmt.execute(createUsersTable);
            stmt.execute(createPostsTable);
            stmt.execute(createCommentsTable);
            
            System.out.println("Database SQLite berhasil diinisialisasi.");
        } catch (SQLException e) {
            System.out.println("Error saat inisialisasi database: " + e.getMessage());
        }
    }
}
