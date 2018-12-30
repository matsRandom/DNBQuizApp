package com.example.mats_.dnbquizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {
    String userId="0";
    String tag = "MainActivity";

    //goes to new Activity
    public void registrer(){
        Log.i(tag,"register");
        Intent intent = new Intent(this, RegisterUser.class);
        Log.i(tag,"userid"+userId);
        startActivity(intent);
    }

    public void setUserId(View view){
        Log.i(tag,"setUserId");

        if (Objects.equals(userId,"0")){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://devbugger.com/")
                    .build();

            Api api = retrofit.create(Api.class);
            api.postUser().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Log.i(tag,"postUser()");
                        userId = response.body().string();
                        Log.i(tag, "userid)set"+userId);
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp", Context.MODE_PRIVATE);

                        sharedPreferences.edit().putString("userId", userId).apply();
                        Log.i(tag, sharedPreferences.getString("userId","0"));
                        registrer();
                    } catch (Exception e) {
                        Log.i(tag,"postUser()FAIL");
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i("Retrofitfailed", "failed");
                }
            });
        }else{
            registrer();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.mats_.dnbquizapp", Context.MODE_PRIVATE);
        try {
            Log.i(tag,"startingShared");
            if(sharedPreferences.contains("userId")){
            userId = sharedPreferences.getString("userId", "0");
                Log.i(tag,"userId"+userId);
                if(!Objects.equals(userId,"0")){
                    registrer();
                }
            }
        }catch (Exception e){
            Log.i(tag,"userIdFailed");
            e.printStackTrace();
        }
    }
}
