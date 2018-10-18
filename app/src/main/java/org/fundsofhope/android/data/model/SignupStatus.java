package org.fundsofhope.android.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anip on 23/07/16.
 */
public class SignupStatus implements Serializable {
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
