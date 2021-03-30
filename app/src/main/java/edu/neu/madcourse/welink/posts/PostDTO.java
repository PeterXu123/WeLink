package edu.neu.madcourse.welink.posts;

import edu.neu.madcourse.welink.utility.User;
import edu.neu.madcourse.welink.utility.UserDAO;

public class PostDTO {
    private String postID;
    private String text;
    private String location;
    private String time;
    private UserDAO author;

    public PostDTO(){};

    public PostDTO(String postID, String text, String location, String time, UserDAO author) {
        this.postID = postID;
        this.text = text;
        this.location = location;
        this.time = time;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserDAO getAuthor() {
        return author;
    }

    public void setAuthor(UserDAO author) {
        this.author = author;
    }
}
