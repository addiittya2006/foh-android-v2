package org.fundsofhope.android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anip on 02/08/16.
 */
public class Project implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("cost")
    private long cost;

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
}
