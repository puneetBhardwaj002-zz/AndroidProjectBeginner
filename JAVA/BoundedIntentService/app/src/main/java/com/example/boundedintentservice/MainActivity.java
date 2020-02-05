package com.example.boundedintentservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ResponseListener {

    MyBoundedService boundedService;
    boolean isBounded = false;
    final String queryString = "http://demo8373470.mockable.io/foodlist";
    ProgressBar progressBar;
    TextView textView;
    FoodAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodAdapter(this,new ArrayList<Food>());
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        textView = findViewById(R.id.loadingText);
        textView.setText(R.string.loading_text);
        textView.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this,MyBoundedService.class);
        bindService(intent,myConnection, Context.BIND_AUTO_CREATE);
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (!(networkInfo != null && networkInfo.isConnected())) {
            textView.setText(R.string.no_internet);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    private ServiceConnection myConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyBoundedService.MyLocalBinder binder = (MyBoundedService.MyLocalBinder) iBinder;
            boundedService = binder.getService();
            isBounded = true;
            boundedService.loadDataFromJson(queryString, MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBounded = false;
        }
    };

    @Override
    public void onResponse(ArrayList<Food> foodArrayList) {
        adapter = new FoodAdapter(getApplicationContext(),foodArrayList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }
}
