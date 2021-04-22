package edu.neu.madcourse.welink.chat;

import java.util.Date;

import edu.neu.madcourse.welink.utility.User;

public class ChatListRow {
    private User curChater;
    private User curUser;
    private Date date;
    public ChatListRow() {

    }

    public ChatListRow(User curChater, User curUser) {
        this.curChater = curChater;
        this.curUser = curUser;
    }

    public ChatListRow(User curChater, User curUser, Date date) {
        this.curChater = curChater;
        this.curUser = curUser;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
