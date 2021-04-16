package edu.neu.madcourse.welink.chat;

import edu.neu.madcourse.welink.utility.User;

public class ChatListRow {
    private User curChater;
    private User curUser;

    public ChatListRow() {

    }

    public ChatListRow(User curChater, User curUser) {
        this.curChater = curChater;
        this.curUser = curUser;
    }

    public User getCurChater() {
        return curChater;
    }

    public void setCurChater(User curChater) {
        this.curChater = curChater;
    }

    public User getCurUser() {
        return curUser;
    }

    public void setCurUser(User curUser) {
        this.curUser = curUser;
    }
}
