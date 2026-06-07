package sistemforumdiskusi_19.model;

import java.time.LocalDateTime;

/**
 * SRP: Bertanggung jawab hanya untuk data komentar.
 */

// Inheritance
public class Comment extends ForumItem {
    
    public Comment(String content, User author) {
        super(content, author);
    }
    
    // Encapsulation Constructor
    public Comment(int id, String content, User author, LocalDateTime timestamp) {
        super(id, content, author, timestamp);
    }

    // Polymorphism
    @Override
    public String getDisplayText() {
        return "Comment: " + getContent() + " (by " + getAuthor().getUsername() + ")";
    }
}
