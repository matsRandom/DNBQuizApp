package com.example.mats_.dnbquizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Score extends AppCompatActivity {
String tag = "Mainactivity";
TextView scoreTextView;

    public void options(View view){
        Intent intent = new Intent(this, Quizz.class);
        startActivity(intent);
    }

    public void takeQuizz(View view){
        Intent intent = new Intent(this, TakeQuizz.class);
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
                   String score = response.body().string() + " / 30" ;
                   scoreTextView.setText(score);

                   Log.i(tag,"score"+ score);
               }catch (Exception e){
                   e.printStackTrace();
                   Log.i(tag,"scoreFailed");
               }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(tag, "scoreResponseFailed");
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
