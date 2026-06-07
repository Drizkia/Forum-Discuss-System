package sistemforumdiskusi_19.controller;

import sistemforumdiskusi_19.model.Post;
import sistemforumdiskusi_19.model.User;
import java.util.List;

/**
 * Dependency Inversion Principle (DIP):
 * Abstraksi layanan (service) sehingga Controller bergantung pada Interface,
 * bukan bergantung pada implementasi konkret secara langsung.
 */
public interface IForumService {
    User loginUser(String username);
    User registerUser(String username, String email);
    User getOrCreateUser(String username, String email);
    void createPost(String title, String content, User author);
    void addCommentToPost(int postId, String content, User author);
    void editPost(int postId, String title, String content);
    void deleteComment(int commentId);
    List<Post> getPosts();
    boolean usernameExists(String username);
}
