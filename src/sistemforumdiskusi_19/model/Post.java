package sistemforumdiskusi_19.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Composition: Post memiliki daftar Comment (List<Comment>).
 * Encapsulation: Field privat dengan akses melalui getter.
 * SRP: Mengelola data postingan.
 */
public class Post {
    private int id;
    private String title;
    private String content;
    private User author;
    private LocalDateTime timestamp;
    private List<Comment> comments;

    public Post(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.timestamp = LocalDateTime.now();
        this.comments = new ArrayList<>();
    }

    public Post(int id, String title, String content, User author, LocalDateTime timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.comments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
