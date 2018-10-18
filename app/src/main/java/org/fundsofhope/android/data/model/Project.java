package org.fundsofhope.android.data.model;

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
    @SerializedName("description")
    private String description;
    @SerializedName("header")
    private String image;
    @SerializedName("id")
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
