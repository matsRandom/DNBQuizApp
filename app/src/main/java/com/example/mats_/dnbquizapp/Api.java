package com.example.mats_.dnbquizapp;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    //ContactInformation
    @POST("kq/contactinfo")
    Call<ResponseBody> contactinfo(@Body RequestBody requestBody);

    //
    @POST("kq/identity")
    Call<ResponseBody> postUser();

    //Registration
    @POST("kq/registration")
    Call<ResponseBody> registrer(@Body RequestBody requestBody);

    @GET("kq//registration/{id}")
    Call<ResponseBody> getRegistration(@Path("id") int id);

    //Save response
    @POST("kq/response")
    Call<ResponseBody> postResponse(@Body RequestBody requestBody);

    // Get Scores
    @GET("kq/score/{id}")
    Call<ResponseBody> getScore(@Path("id") int id);

    @GET("kq/score/leaderboard/{difficulty}")
    Call<ResponseBody> getLeaderboard(@Path("difficulty") String difficulty);


    @GET("kq/questions/difficulties")
    Call<ResponseBody> getQuestionDifficulty();
    @GET("kq/questions")
    Call<ResponseBody> getQuestions(@Query("difficulty") String diff);
    @GET("kq/questions/{id}")
    Call<ResponseBody> getQuestionWithId(@Path("id") int id);

    }
