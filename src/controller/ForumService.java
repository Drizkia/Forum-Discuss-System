package sistemforumdiskusi_19.controller;

import sistemforumdiskusi_19.model.*;
import sistemforumdiskusi_19.database.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * SRP: Class ini bertanggung jawab untuk logika bisnis forum.
 * Menyimpan dan mengelola data postingan ke SQLite.
 */
public class ForumService implements IForumService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ForumService() {
    }

    public User loginUser(String username) {
        String sql = "SELECT id, username, email FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("Error login: " + e.getMessage());
        }
        return null;
    }

    public User registerUser(String username, String email) {
        String insertSql = "INSERT INTO users(username, email) VALUES(?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return new User(rs.getInt(1), username, email);
            }
        } catch (SQLException e) {
            System.out.println("Error register: " + e.getMessage());
        }
        return null;
    }

    public User getOrCreateUser(String username, String email) {
        String selectSql = "SELECT id, username, email FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }

        String insertSql = "INSERT INTO users(username, email) VALUES(?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return new User(rs.getInt(1), username, email);
            }
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
        return null;
    }

    public void createPost(String title, String content, User author) {
        String sql = "INSERT INTO posts(title, content, author_id) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, author.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating post: " + e.getMessage());
        }
    }

    public void addCommentToPost(int postId, String content, User author) {
        String sql = "INSERT INTO comments(post_id, content, author_id) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setString(2, content);
            pstmt.setInt(3, author.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding comment: " + e.getMessage());
        }
    }

    public void editPost(int postId, String title, String content) {
        String sql = "UPDATE posts SET title = ?, content = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, postId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error editing post: " + e.getMessage());
        }
    }

    public void deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE id = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting comment: " + e.getMessage());
        }
    }

    public List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.title, p.content, p.timestamp, u.id as user_id, u.username, u.email " +
                     "FROM posts p JOIN users u ON p.author_id = u.id ORDER BY p.id ASC";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User author = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("email"));
                // SQLite datetime format from CURRENT_TIMESTAMP is "YYYY-MM-DD HH:MM:SS"
                String tsStr = rs.getString("timestamp");
                LocalDateTime timestamp = LocalDateTime.now();
                try {
                    if (tsStr != null) {
                        timestamp = LocalDateTime.parse(tsStr, formatter);
                    }
                } catch (Exception ex) {
                    // Ignore, fallback to now
                }
                Post post = new Post(rs.getInt("id"), rs.getString("title"), rs.getString("content"), author, timestamp);
                
                // Load comments for this post
                loadComments(conn, post);
                
                posts.add(post);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching posts: " + e.getMessage());
        }
        return posts;
    }

    /**
     * Memeriksa apakah username sudah terdaftar tanpa mengubah sesi.
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
        }
        return false;
    }

    private void loadComments(Connection conn, Post post) {
        String sql = "SELECT c.id, c.content, c.timestamp, u.id as user_id, u.username, u.email " +
                     "FROM comments c JOIN users u ON c.author_id = u.id WHERE c.post_id = ? ORDER BY c.id ASC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, post.getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User author = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("email"));
                String tsStr = rs.getString("timestamp");
                LocalDateTime timestamp = LocalDateTime.now();
                try {
                    if (tsStr != null) {
                        timestamp = LocalDateTime.parse(tsStr, formatter);
                    }
                } catch (Exception ex) {
                }
                Comment comment = new Comment(rs.getInt("id"), rs.getString("content"), author, timestamp);
                post.addComment(comment);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching comments: " + e.getMessage());
        }
    }
}
