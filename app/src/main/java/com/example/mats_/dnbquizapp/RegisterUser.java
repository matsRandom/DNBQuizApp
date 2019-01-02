package com.example.mats_.dnbquizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    EditText editText;
    String difficulty;
    String registrationId = "0";

    public void options(){
        Intent intent = new Intent(this,Options.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Hides the keyboard when user taps on something else
    @Override
    public void onClick(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String setRequestBody(String name, String difficulty){

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp",Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","0");

        String json = String.format( "{\n" +
                "  \"identityId\": %1s,\n" +
                "  \"name\": \"%2s\",\n" +
                "  \"difficulty\": \"%3s\"\n" +
                "}",userId,name,difficulty);
        return json;
    }

    public void register(View view){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://devbugger.com/")
                .build();

        String name = editText.getText().toString();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),setRequestBody(name,difficulty));

        Api api = retrofit.create(Api.class);
        api.register(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    registrationId = response.body().string();
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("registrationId", registrationId).apply();
                    sharedPreferences.edit().putString("difficulty", difficulty).apply();
                    options();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Failed to register, an error occurred",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to register, the internet might be off",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        spinner = findViewById(R.id.spinner);
        editText = findViewById(R.id.editTextId);
        difficulty = "EASY";

        ConstraintLayout constraintLayout = findViewById(R.id.constrainLayoutId);
        TextView createUserTextView = findViewById(R.id.createUserTextView);
        constraintLayout.setOnClickListener(this);
        createUserTextView.setOnClickListener(this);


        adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                difficulty = (String) adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
