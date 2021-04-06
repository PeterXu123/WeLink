package edu.neu.madcourse.welink.utility;

import java.util.List;

public class ChaterRelation {
    private String uid;
    private List<String> chatersId;

    public ChaterRelation() {

    }

    public ChaterRelation(String uid, List<String> chatersId) {
        this.uid = uid;
        this.chatersId = chatersId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getChatersId() {
        return chatersId;
    }

    public void setChatersId(List<String> chatersId) {
        this.chatersId = chatersId;
    }
}
