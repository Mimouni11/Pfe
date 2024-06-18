package com.example.pfemini.Network;

import com.example.pfemini.Addresponse;
import com.example.pfemini.Models.Changeresponse;
import com.example.pfemini.Models.ResetPasswordRequest;
import com.example.pfemini.Models.StatusRequest;
import com.example.pfemini.Models.TaskCount;
import com.example.pfemini.Models.TaskDoneRatio;
import com.example.pfemini.UI.Chef.Notification;
import com.example.pfemini.Models.DestinationCount;
import com.example.pfemini.Models.DriverTask;
import com.example.pfemini.Models.LoginRequest;
import com.example.pfemini.Models.LoginResponse;
import com.example.pfemini.Models.MonthlyDistance;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Apiservices {

    @POST("/authenticate")
    Call<LoginResponse> authenticateUser(
           @Body LoginRequest loginRequest)
    ;
    @FormUrlEncoded
    @POST("change_password")
    Call<Changeresponse> changePassword(
            @Field("username") String username, // Include the session username
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword
    );
    @POST("/reset_password")
    Call<ResponseBody> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);



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


    @GET("tasks-driver")
    Call<List<DriverTask>> getTasksForScannedContent(@Query("content") String content, @Query("username") String username);


    @FormUrlEncoded
    @POST("/update_task_status")
    Call<Void> updateTaskStatus(
            @Field("username") String username,
            @Field("taskName") String taskName,
            @Field("status") String status
    );
    @GET("/get_pending_tasks")
    Call<JsonObject> getPendingTasks();
    @FormUrlEncoded
    @POST("/confirm_task")
    Call<Void> confirmTask(@Field("taskName") String taskName, @Field("matricule") String matricule, @Field("taskType") String taskType);
    @FormUrlEncoded
    @POST("/report")
    Call<Void> saveReport(
            @Field("vehicleId") String vehicleId,
            @Field("issueDescription") String issueDescription,
            @Field("workDescription") String workDescription,
            @Field("signature") String signature,
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("/register_token")
    Call<Void> saveChefDeviceToken(
            @Field("username") String username,
            @Field("deviceToken") String deviceToken
    );

    @FormUrlEncoded
    @POST("/register_tokendriver")
    Call<Void> saveDriverDeviceToken(
            @Field("username") String username,
            @Field("deviceToken") String deviceToken
    );


    @FormUrlEncoded
    @POST("/register_tokenmecano")
    Call<Void> saveMecanoDeviceToken(
            @Field("username") String username,
            @Field("deviceToken") String deviceToken
    );






    @FormUrlEncoded
    @POST("/send_notification")
    Call<Void> sendNotification(
            @Field("username") String username,
            @Field("title") String title,
            @Field("message") String message,
            @Field("mecano") String mecano

    );

    @GET("/Get_notifications")
    Call<List<Notification>> getNotifications(@Query("username") String username);


    @FormUrlEncoded
    @POST("/saveRehla")
    Call<Void> saveRehla(
            @Field("username") String username,
            @Field("addresses") String addresses,
            @Field("km") float km
    );

    @GET("/get-rehla")
    Call<List<String>> getRehla(@Query("username") String username);

    @GET("/getMonthlyDistance")
    Call<List<MonthlyDistance>> getMonthlyDistance(@Query("username") String username);
    @GET("/getDestinationCounts")
    Call<List<DestinationCount>> getDestinationCounts(@Query("username") String username);


    @POST("/updateStatus")
    Call<Void> updateStatus(@Body StatusRequest statusRequest);

    @GET("/mechanicTaskCounts") // Update the endpoint URL accordingly
    Call<List<TaskCount>> getTaskCounts(@Query("username") String username);


    @GET("/taskDoneRatio")
    Call<List<TaskDoneRatio>> getDoneRatio(@Query("username") String username);
}

