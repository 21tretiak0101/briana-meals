package by.ttre16.enterprise.model;

public class AuthenticatedUser {
    private final Integer id;
    private final String email;

    public AuthenticatedUser(Integer id, String email) {
        this.id = id;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
