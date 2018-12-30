package com.example.mats_.dnbquizapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Leaderboard extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    String difficulty = "EASY";

    public void loadLeaderboard(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp", Context.MODE_PRIVATE);
        difficulty = sharedPreferences.getString("difficulty","EASY");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://devbugger.com/")
                .build();

        Api api = retrofit.create(Api.class);
        api.getLeaderboard(difficulty).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                   JSONArray jsonArray = new JSONArray(response.body().string());

                    for (int i=0; i<jsonArray.length(); i++){
                        arrayList.add(jsonArray.getString(i));
                    }
                    arrayAdapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        listView = findViewById(R.id.listViewId);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this,R.layout.liste2textview,arrayList);
        listView.setAdapter(arrayAdapter);

        loadLeaderboard();
    }
}
