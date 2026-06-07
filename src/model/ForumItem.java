package sistemforumdiskusi_19.model;

import java.time.LocalDateTime;

/**
 * SOLID (SRP): Menangani atribut dasar dari sebuah entitas konten di forum.
 */

// Abstraction
public abstract class ForumItem implements Displayable {
    // Encapsulation 
    private int id;
    private String content;
    private User author;
    private LocalDateTime timestamp;

    public ForumItem(String content, User author) {
        this.content = content;
        this.author = author;
        this.timestamp = LocalDateTime.now();
    }
    
    public ForumItem(int id, String content, User author, LocalDateTime timestamp) {
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

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Polymorphism (Method Overriding)
    // Abstraction
    public abstract String getDisplayText();
}
