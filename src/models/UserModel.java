package models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String username;

    public UserModel() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
}