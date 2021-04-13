package edu.neu.madcourse.welink.utility;

public class ChatMessage {
    private String message;
    private String senderUserId;
    private String senderUserName;
    private Long time;
    private String chater1_chater2;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getChater1_chater2() {
        return chater1_chater2;
    }

    public void setChater1_chater2(String chater1_chater2) {
        this.chater1_chater2 = chater1_chater2;
    }

    public ChatMessage(String message, String senderUserId, String senderUserName,
                       Long time, String chater1_chater2) {
        this.message = message;
        this.senderUserId = senderUserId;
        this.senderUserName = senderUserName;
        this.time = time;
        this.chater1_chater2 = chater1_chater2;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public ChatMessage(){};

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }
}