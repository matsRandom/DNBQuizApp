package com.example.mats_.dnbquizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TakeQuiz extends AppCompatActivity {
    String difficulty;
    JSONArray jsonArray;
    String questionId;
    String questionDescription;
    ArrayList<String> answerIdList = new ArrayList<>();
    ArrayList<String> answerDescriptionList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView;

    //updated each time someone answers a question
    int arrayIndex;

    //Goes to the score activity, and reset the arrayIndex
    public void score(){
        arrayIndex = 0;
        Intent intent = new Intent(this,Score.class);
        startActivity(intent);
    }

    //update is called each time someone answers a question
    public void update(int position){
        arrayIndex++;
        postResponse(position);
        setQuestions(arrayIndex);
    }

    public void setQuestions(int i){
        if(i<jsonArray.length()){
            try{
                answerIdList.clear();
                answerDescriptionList.clear();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                questionId = jsonObject.getString("id");
                questionDescription = jsonObject.getString("description");

                JSONArray jsonArray1 = jsonObject.getJSONArray("answers");
                for (int y = 0; y < jsonArray1.length(); y++){
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(y);

                    answerIdList.add(jsonObject1.getString("id"));
                    answerDescriptionList.add(jsonObject1.getString("description"));
                }
                setQuestionAndAnswers();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(i == jsonArray.length()){
            score();
        }
    }

    public void setQuestionAndAnswers(){
        TextView questionDescriptionTextView = findViewById(R.id.questionDescription);
        questionDescriptionTextView.setText(questionDescription);
        arrayAdapter.notifyDataSetChanged();
    }

    public String setRequestBody(String questionId,String answerId){

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp",Context.MODE_PRIVATE);
        String registrationId = sharedPreferences.getString("registrationId","0");

        String answer = String.format("{\n" +
                "  \"registrationId\": %1s,\n" +
                "  \"questionId\": %2s,\n" +
                "  \"answerId\": %3s\n" +
                "}",registrationId,questionId,answerId);

        return answer;
    }

    public void postResponse(int position){
        if(position < answerIdList.size()){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://devbugger.com/")
                    .build();

            String jsonAnswer = setRequestBody(questionId,answerIdList.get(position));
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonAnswer);

            Api api = retrofit.create(Api.class);
            api.postResponse(requestBody).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),"Failed to save questions, the internet might be off",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_quiz);

        listView = findViewById(R.id.listViewId1);
        arrayIndex = 0;
        difficulty = "EASY";
        questionId = "";
        questionDescription = "";
        getQuestions();

        arrayAdapter = new ArrayAdapter<>(this,R.layout.listtextview,answerDescriptionList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                update(position);
            }
        });
    }

    public void getQuestions(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mats_.dnbquizapp",Context.MODE_PRIVATE);
        difficulty = sharedPreferences.getString("difficulty","EASY");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://devbugger.com/")
                .build();

        Api api = retrofit.create(Api.class);
        api.getQuestions(difficulty).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String list = response.body().string();
                    jsonArray = new JSONArray(list);
                    setQuestions(arrayIndex);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Failed to get questions, an error occurred",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed to get questions, the internet might be off",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
