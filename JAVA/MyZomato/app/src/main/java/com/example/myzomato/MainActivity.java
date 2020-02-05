package com.example.myzomato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    FoodAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "http://demo8373470.mockable.io/foodlist";
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        new GetFoodTask(this).execute(url);
    }

    public class GetFoodTask extends AsyncTask<String,Void,ArrayList<Food>> {
        Context context;

        public GetFoodTask(Context c){
            this.context=c;
        }
        @Override
        protected void onPreExecute() {
            Log.e("Main Activity","onPreExecute method started.");
            super.onPreExecute();
            Log.e("Main Activity","onPreExecute method executed.");
        }

        @Override
        protected ArrayList<Food> doInBackground(String... strings) {
            Log.e("Main Activity","doInBackground method started.");
            ArrayList<Food> foodList = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                if(urlConnection.getResponseCode()==200){
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String textLine = bufferedReader.readLine();
                    while (textLine!=null){
                        builder.append(textLine);
                        textLine = bufferedReader.readLine();
                    }
                    JSONObject rootJSONObject = new JSONObject(builder.toString());
                    JSONArray foodArray = rootJSONObject.getJSONArray("Food");
                    for(int i=0;i<foodArray.length();i++){
                        JSONObject foodObject = foodArray.getJSONObject(i);
                        String name = foodObject.getString("FoodName");
                        String price = foodObject.getString("FoodPrice");
                        String category = foodObject.getString("FoodCategory");
                        String imageResource = foodObject.getString("FoodThumb");
                        foodList.add(new Food(name,price,category,imageResource));
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            Log.e("Main Activity","doInBackground method executed.");
            return foodList;
        }

        @Override
        protected void onPostExecute(ArrayList<Food> foods) {
            Log.e("Main Activity","onPostExecute method started.");
            //TODO:Notify data set change on recycler view
            //TODO:using GSON -how it works
            //TODO:Volley -how it works
            Log.e("Main Activity","onPostExecute method executed.");
            mAdapter = new FoodAdapter(context,foods);
            mRecyclerView.setAdapter(mAdapter);
            if(!foods.isEmpty()){
                ProgressBar progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.GONE);
            }
            super.onPostExecute(foods);
        }
    }

}