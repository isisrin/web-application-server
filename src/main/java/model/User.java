package model;

import lombok.NonNull;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(@NonNull String userId, @NonNull String password) {
        this.userId = userId;
        this.password = password;
    }

    public User(@NonNull String userId, @NonNull String password, @NonNull String name, @NonNull String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
