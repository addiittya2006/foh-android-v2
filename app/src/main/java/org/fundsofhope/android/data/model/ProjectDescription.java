package org.fundsofhope.android.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by anip on 18/12/16.
 */
public class ProjectDescription implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("cost")
    private long cost;
    @SerializedName("description")
    private String description;
    @SerializedName("images")
    private ArrayList<String> images;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
