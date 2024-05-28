package com.example.pfemini.UI.Mecano;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NotificationService {

    @FormUrlEncoded
    @POST("/send_notification")
    Call<Void> sendNotification(
            @Field("recipient") String recipient,
            @Field("message") String message
    );

}
