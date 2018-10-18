package org.fundsofhope.android.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anip on 18/12/16.
 */
public class NgoDescription implements Serializable {
    @SerializedName("name")
    private String title;
    @SerializedName("phoneNo")
    private long cost;
    @SerializedName("email")
    private String email;
    @SerializedName("profile")
    private String profile;
    @SerializedName("head")
    private String header;

    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
