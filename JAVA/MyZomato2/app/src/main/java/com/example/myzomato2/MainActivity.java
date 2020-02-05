package com.example.myzomato2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL = "http://demo8373470.mockable.io/foodlist";
    ListView listView;
    ProgressBar progressBar;
    ArrayList<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        listView = findViewById(R.id.listView);
        foodList = new ArrayList<>();
        loadDataFromJSON();
    }
    private void loadDataFromJSON(){
        VolleyLog.DEBUG = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,JSON_URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.INVISIBLE);
                try{
                    JSONObject rootJsonObject = new JSONObject(response.toString());
                    JSONArray foodArray = rootJsonObject.getJSONArray("Food");
                    for(int i=0;i<foodArray.length();i++){
                        JSONObject foodObject = foodArray.getJSONObject(i);
                        String nameText = foodObject.getString("FoodName");
                        String priceText = foodObject.getString("FoodPrice");
                        String categoryText = foodObject.getString("FoodCategory");
                        String imageResourceText = foodObject.getString("FoodThumb");
                        //Log.d("JSON Object Parsing",nameText);
                        foodList.add(new Food(nameText,priceText,categoryText,imageResourceText));
                    }
                    FoodCustomAdapter adapter = new FoodCustomAdapter(getApplicationContext(),foodList);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
