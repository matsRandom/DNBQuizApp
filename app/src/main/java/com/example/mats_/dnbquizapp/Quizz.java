package com.example.mats_.dnbquizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Quizz extends AppCompatActivity {

    String tag = "Mainactivity";


    public void leaderboard(View view){
        Intent intent = new Intent(this,Leaderboard.class);
        startActivity(intent);
    }
    public void score(View view){
        Intent intent = new Intent(this,Score.class);
        startActivity(intent);
    }
    public void takeQuizz(View view){
        Intent intent = new Intent(this,TakeQuizz.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
    }
}
