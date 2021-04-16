package edu.neu.madcourse.welink.utility;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String displayName;
    private String uid;
    private String token;
    private String location;
    private String email;
    private String iconUrl;

    public User(String displayName) {
        this.displayName = displayName;
    }
    public User() {

    }

    protected User(Parcel in) {
        displayName = in.readString();
        uid = in.readString();
        token = in.readString();
        location = in.readString();
        email = in.readString();
        iconUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(uid);
        dest.writeString(token);
        dest.writeString(location);
        dest.writeString(email);
        dest.writeString(iconUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
