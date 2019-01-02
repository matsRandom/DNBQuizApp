package com.example.mats_.dnbquizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.util.Objects;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {
    String userId;

    public void register(){
        Intent intent = new Intent(this, RegisterUser.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void setUserId(View view){
        if (Objects.equals(userId,"0")){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://devbugger.com/")
                    .build();

            Api api = retrofit.create(Api.class);
            api.postUser().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        userId = response.body().string();
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("userId", userId).apply();
                        register();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to set userID, an error occurred",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Failed to get userID, the internet might be off",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            register();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = "0";
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.mats_.dnbquizapp", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("userId")){
            userId = sharedPreferences.getString("userId", "0");
            if(!Objects.equals(userId,"0")){
                    register();
            }
        }
    }
}
