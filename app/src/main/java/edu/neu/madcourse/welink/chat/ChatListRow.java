package edu.neu.madcourse.welink.chat;

public class ChatListRow {
    private String imgUrl;
    private String username;

    public ChatListRow() {

    }

    public ChatListRow(String imgUrl, String username) {
        this.imgUrl = imgUrl;
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
