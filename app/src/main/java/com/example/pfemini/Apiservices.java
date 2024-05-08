package com.example.pfemini;

import com.example.pfemini.Models.DriverTask;
import com.example.pfemini.Models.Tasks_driver;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Apiservices {

    @FormUrlEncoded
    @POST("/authenticate")
    Call<LoginResponse> authenticateUser(
            @Field("username") String username,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("change_password")
    Call<Changeresponse> changePassword(
            @Field("username") String username, // Include the session username
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword
    );


    @FormUrlEncoded
    @POST("/add_user")
    Call<Addresponse> addUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("role") String role
    );
    @GET("/tasks")
    Call<JsonObject> getTasks(@Query("username") String username);


    @GET("/tasks-driver")
    Call<List<DriverTask>> getTasksForScannedContent(@Query("content") String content);


    @FormUrlEncoded
    @POST("/update_task_status")
    Call<Void> updateTaskStatus(
            @Field("username") String username,
            @Field("taskName") String taskName,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("/report")
    Call<Void> saveReport(
            @Field("title") String title,
            @Field("content") String content,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("/register_token")
    Call<Void> saveChefDeviceToken(
            @Field("username") String username,
            @Field("deviceToken") String deviceToken
    );
    @FormUrlEncoded
    @POST("/send_notification")
    Call<Void> sendNotification(
            @Field("username") String username,
            @Field("message") String message
    );
}
