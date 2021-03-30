package edu.neu.madcourse.welink.utility;

public class UserDAO {
    private String displayName;
    private String email;
    private String token;
    private String uid;

    public UserDAO(){};

    public UserDAO(String displayName, String email, String token, String uid) {
        this.displayName = displayName;
        this.email = email;
        this.token = token;
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
