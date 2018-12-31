package com.example.mats_.dnbquizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Options extends AppCompatActivity {

    String tag = "Mainactivity";


    public void leaderboard(View view){
        Intent intent = new Intent(this,Leaderboard.class);
        startActivity(intent);
    }
    public void score(View view){
        Intent intent = new Intent(this,Score.class);
        startActivity(intent);
    }
    public void takeQuiz(View view){
        Intent intent = new Intent(this,TakeQuiz.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }
}
