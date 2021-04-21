package edu.neu.madcourse.welink.utility;

public class ChatMessage {
    private String message;
    private String senderUserId;
    private String senderUserName;
    private Long time;
    private String key1_key2;

    public String getKey1_key2() {
        return key1_key2;
    }

    public void setKey1_key2(String key1_key2) {
        this.key1_key2 = key1_key2;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }


    public ChatMessage(String message, String senderUserId, String senderUserName, Long time, String key1_key2) {
        this.message = message;
        this.senderUserId = senderUserId;
        this.senderUserName = senderUserName;
        this.time = time;
        this.key1_key2 = key1_key2;
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
