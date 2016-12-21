package org.fundsofhope.android.config;

import org.fundsofhope.android.model.Ngo;
import org.fundsofhope.android.model.NgoDescription;
import org.fundsofhope.android.model.Project;
import org.fundsofhope.android.model.ProjectDescription;
import org.fundsofhope.android.model.SignupStatus;

import java.util.ArrayList;

import retrofit.Callback;
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
            Callback<ProjectDescription> callback);
    @GET("/ngo")
    void ngo(
            Callback<ArrayList<Ngo>> callback
    );
    @GET("/trending")
    void trending(
            Callback<ArrayList<Project>> callback
    );
    @GET("/ngo")
    void ngo_detail(
            @Query("_id") int id,
            Callback<NgoDescription> callback
    );
    @FormUrlEncoded
    @POST("/project/donate")
    void donate(
            @Field("user_id") int user_id,
            @Field("project_id") int project_id,
            @Field("amount") int amount,
            Callback<SignupStatus> callback
    );


}