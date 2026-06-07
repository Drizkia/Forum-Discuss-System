package sistemforumdiskusi_19.model;

import java.time.LocalDateTime;

/**
 * Inheritance: Comment merupakan turunan dari ForumItem (menggunakan extends).
 * Encapsulation: Menggunakan field turunan dan setter/getter dari superclass.
 * Composition: Comment terkait dengan objek User (diwarisi dari ForumItem).
 * SRP: Bertanggung jawab hanya untuk data komentar.
 */
public class Comment extends ForumItem {
    
    public Comment(String content, User author) {
        super(content, author);
    }
    
    public Comment(int id, String content, User author, LocalDateTime timestamp) {
        super(id, content, author, timestamp);
    }

    @Override
    public String getDisplayText() {
        return "Comment: " + getContent() + " (by " + getAuthor().getUsername() + ")";
    }
}
