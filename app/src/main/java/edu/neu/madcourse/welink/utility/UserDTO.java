package edu.neu.madcourse.welink.utility;

import java.util.Set;

public class UserDTO {
    private Set<String> followers;
    private String displayName;
    private String email;
    private String token;
    private String uid;

    public UserDTO(){};

    public UserDTO(String displayName, String email, String token, String uid, Set<String> followers) {
        this.followers = followers;
        this.displayName = displayName;
        this.email = email;
        this.token = token;
        this.uid = uid;
    }

    public void setByUserDAO(UserDAO user) {
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.token = user.getToken();
        this.uid = user.getToken();
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<String> followers) {
        this.followers = followers;
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
