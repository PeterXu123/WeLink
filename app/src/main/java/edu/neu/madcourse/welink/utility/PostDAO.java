package edu.neu.madcourse.welink.utility;

import java.util.List;

public class PostDAO {
    private String text;
    private String location;
    private long time;
    private String authorUID;
    private List<String> imageUrls;

    public PostDAO(){};

    public PostDAO(String text, String location, long time, String authorUID) {
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAuthorUID() {
        return authorUID;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }
}
