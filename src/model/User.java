package model;

public class User {
    public int id;
    public String username, email, password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
