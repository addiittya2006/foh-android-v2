package org.fundsofhope.android.config;

import org.fundsofhope.android.ProjectDescription;
import org.fundsofhope.android.model.Project;
import org.fundsofhope.android.model.Project_Description;
import org.fundsofhope.android.model.SignupStatus;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by anip on 23/07/16.
 */
public interface ApiInterface {
    //SIGNUP
    @Headers("Content-Type: application/json")
    @POST("/user/signup/")
    void signup(
            @Query("name") String name,
            @Query("phoneNo") String phoneNo,
            @Query("email") String email,
            Callback<SignupStatus> callback);

    @GET("/project")
    void projects(
            Callback<ArrayList<Project>> callback);
    @GET("/project")
    void project_detail(
            @Query("_id") int id,
            Callback<Project_Description> callback);

}