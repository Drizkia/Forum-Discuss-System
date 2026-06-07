package sistemforumdiskusi_19.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SRP: Mengelola data postingan.
 */

// Inheritance
public class Post extends ForumItem {
    // Encapsulation
    private String title;
    // Composition
    private List<Comment> comments;

    public Post(String title, String content, User author) {
        super(content, author);
        this.title = title;
        this.comments = new ArrayList<>();
    }

    public Post(int id, String title, String content, User author, LocalDateTime timestamp) {
        super(id, content, author, timestamp);
        this.title = title;
        this.comments = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Comment> getComments() {
        return comments;
    }

    // Polymorphism
    @Override
    public String getDisplayText() {
        return "Post: " + title + " - " + getContent() + " (by " + getAuthor().getUsername() + ")";
    }
}
