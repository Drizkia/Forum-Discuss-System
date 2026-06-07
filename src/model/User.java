package sistemforumdiskusi_19.model;

/**
 * SRP: Mengelola data user.
 */
public class User {
    // Encapsulation 
    private int id;
    private String username;
    private String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // Encapsulation Getter
    public int getId() {
        return id;
    }
    //Encapsulation Setter
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
