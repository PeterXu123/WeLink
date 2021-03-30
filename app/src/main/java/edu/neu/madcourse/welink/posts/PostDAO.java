package edu.neu.madcourse.welink.posts;

public class PostDAO {
    private String text;
    private String location;
    private String time;
    private String authorUID;

    public PostDAO(){};

    public PostDAO(String text, String location, String time, String authorUID) {
        this.text = text;
        this.location = location;
        this.time = time;
        this.authorUID = authorUID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getAuthorUID() {
        return authorUID;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }
}
