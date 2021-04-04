package edu.neu.madcourse.welink.utility;

public class ChatMessage {
    private String message;
    private String senderUserName;


    public ChatMessage(String message, String senderUserName) {
        this.message = message;
        this.senderUserName = senderUserName;
    }

    public ChatMessage(){};

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }
}
