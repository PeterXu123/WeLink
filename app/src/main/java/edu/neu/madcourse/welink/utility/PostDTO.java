package edu.neu.madcourse.welink.utility;

public class PostDTO {
    private String postID;
    private String text;
    private String location;
    private String displayTime;
    private User author;
    private String imageUrl;

    public PostDTO(){};

    public PostDTO(String postID, String text, String location, String time, User author) {
        this.postID = postID;
        this.text = text;
        this.location = location;
        this.displayTime = time;
        this.author = author;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setByPostDAO(PostDAO post) {
        this.text = post.getText();
        this.location = post.getLocation();
        this.displayTime = Formatter.STORAGE_TIME_FORMATTER.format(post.getTime());
        this.imageUrl = post.getImageUrl();
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
        return displayTime;
    }

    public void setTime(String time) {
        this.displayTime = time;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
