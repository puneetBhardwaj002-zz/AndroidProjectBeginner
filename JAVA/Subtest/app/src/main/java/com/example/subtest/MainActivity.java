package com.example.subtest;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mSubscriptionRV;
    private ActiveSubscriptionAdapter mSubscriptionAdapter;
    private ArrayList<SubscriptionElements> mSubscriptionList;
    private static final String JSON_URL = "http://demo9063766.mockable.io";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        mSubscriptionList = new ArrayList<>();
        Log.d("Test","Loaddata called");
        loadDataFromJSON();
    }

    private void initUI() {
        mSubscriptionRV=findViewById(R.id.subscription_rv);
        mSubscriptionRV.setLayoutManager(new LinearLayoutManager(this));
    }
    private void loadDataFromJSON(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,JSON_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject rootJsonObject = new JSONObject(response.toString());
                    JSONArray foodArray = rootJsonObject.getJSONArray("Food");
                    for(int i=0;i<foodArray.length();i++){
                        JSONObject foodObject = foodArray.getJSONObject(i);
                        String nameText = foodObject.getString("FoodName");
                        String idText = foodObject.getString("FoodId");
                        String priceText = foodObject.getString("FoodPrice");
                        String imageResourceText = foodObject.getString("FoodThumb");
                        String dateText = foodObject.getString("FoodDate");
                        mSubscriptionList.add(new SubscriptionElements(nameText,idText,priceText,dateText,imageResourceText));
                        Log.d("Test",nameText);
                    }
                    mSubscriptionAdapter=new ActiveSubscriptionAdapter(mSubscriptionList,getApplicationContext());
                    mSubscriptionRV.setHasFixedSize(true);
                    mSubscriptionRV.setAdapter(mSubscriptionAdapter);
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


