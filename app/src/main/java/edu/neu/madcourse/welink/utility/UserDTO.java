package edu.neu.madcourse.welink.utility;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserDTO implements Parcelable {
    private List<String> followers;
    private String displayName;
    private String email;
    private String token;
    private String uid;
    private String iconUrl;
    private String location;

    public UserDTO(){};

    protected UserDTO(Parcel in) {
        followers = in.createStringArrayList();
        displayName = in.readString();
        email = in.readString();
        token = in.readString();
        uid = in.readString();
        iconUrl = in.readString();
        location = in.readString();
    }

    public static final Creator<UserDTO> CREATOR = new Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    public void setByUser(User user) {
        this.displayName = user.getDisplayName();
        this.email = user.getEmail();
        this.token = user.getToken();
        this.uid = user.getUid();
        this.iconUrl = user.getIconUrl();
        this.location = user.getLocation();
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(followers);
        dest.writeString(displayName);
        dest.writeString(email);
        dest.writeString(token);
        dest.writeString(uid);
        dest.writeString(iconUrl);
        dest.writeString(location);
    }
}
