package com.example.mats_.dnbquizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.MessageFormat;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterUser extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    EditText editText;
    String difficulty;
    String tag = "MainActivity";
    String registrationId="0";

    public String setRequestBody(String name, String difficulty){
        Log.i(tag,"sendRequestBody");

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp",Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","0");
        Log.i(tag,"userId" + userId);

        String json = String.format( "{\n" +
                "  \"identityId\": %1s,\n" +
                "  \"name\": \"%2s\",\n" +
                "  \"difficulty\": \"%3s\"\n" +
                "}",userId,name,difficulty);
        Log.i(tag,"json"+json);
        return json;
    }

    public void quizz(){
        Intent intent = new Intent(this,Quizz.class);
        Log.i(tag,difficulty);
        startActivity(intent);
    }

    public void registrer(View view){
        Log.i(tag,"register");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://devbugger.com/")
                    .build();

            String name = editText.getText().toString();
            Log.i(tag,"name");

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),setRequestBody(name,difficulty));

            Api api = retrofit.create(Api.class);
            api.registrer(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Log.i(tag,"registrer(body)");
                        registrationId = response.body().string();
                        Log.i(tag,"registrerid"+registrationId);

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp", Context.MODE_PRIVATE);

                        sharedPreferences.edit().putString("registrationId", registrationId).apply();
                        sharedPreferences.edit().putString("difficulty", difficulty).apply();
                        Log.i(tag,sharedPreferences.getString("registrationId","0"));
                        quizz();
                    } catch (Exception e) {
                        Log.i(tag,"responsebody failed");
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.i(tag, "resopnseFailed");
                }
            });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        spinner = (Spinner) findViewById(R.id.spinner);
        editText = findViewById(R.id.editTextId);
        difficulty ="EASY";

        adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(tag,"diff");
                difficulty = (String) adapter.getItem(position);
                Log.i(tag,difficulty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
