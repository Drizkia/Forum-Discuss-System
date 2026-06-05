package sistemforumdiskusi_19.model;

import java.time.LocalDateTime;

/**
 * Encapsulation: Menggunakan private fields dengan public getters.
 * Composition: Comment terdiri dari objek User.
 * SRP: Bertanggung jawab hanya untuk data komentar.
 */
public class Comment {
    private int id;
    private String content;
    private User author;
    private LocalDateTime timestamp;
    
    public Comment(String content, User author) {
        this.content = content;
        this.author = author;
        this.timestamp = LocalDateTime.now();
    }
    
    public Comment(int id, String content, User author, LocalDateTime timestamp) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
