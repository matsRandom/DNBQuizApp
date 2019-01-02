package com.example.mats_.dnbquizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Score extends AppCompatActivity {
String tag = "Mainactivity";
TextView scoreTextView;

    public void options(View view){
        Intent intent = new Intent(this, Options.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void takeQuiz(View view){
        Intent intent = new Intent(this, TakeQuiz.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void getScore(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://devbugger.com/")
                .build();

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp",Context.MODE_PRIVATE);
        final int registrationId = Integer.parseInt(sharedPreferences.getString("registrationId","0"));

        Log.i(tag,"ScoreRegistrationInt"+Integer.toString(registrationId));
        Api api = retrofit.create(Api.class);
        api.getScore(registrationId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               try{
                   String score = response.body().string();
                   scoreTextView.setText(score);

                   Log.i(tag,"score"+ score);
               }catch (Exception e){
                   Toast.makeText(getApplicationContext(),"Failed to get score, an error occurred",Toast.LENGTH_SHORT).show();
                   e.printStackTrace();
               }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to get score, the internet might be off",Toast.LENGTH_SHORT).show();

            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        scoreTextView = findViewById(R.id.scoreTextView);
        getScore();
    }
}
