package com.example.intentservicemyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private MyBroadcastReceiver broadcastReceiver;
    FoodAdapter adapter=null;
    ProgressBar progressBar;
    TextView textView;
    ArrayList<Food> foodArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodAdapter(this,foodArrayList);
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.loadingText);
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(R.string.loading_text);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            Intent intent = new Intent(this, MyIntentService.class);
            intent.putExtra("QueryUrl", "http://demo8373470.mockable.io/foodlist");
            startService(intent);
        } else {
            textView.setText(R.string.no_internet);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        broadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyIntentService.FOOD_INFO);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    //Class to handle the data sent from task thread to UI
    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            foodArrayList = intent.getParcelableArrayListExtra("Data");
            adapter = new FoodAdapter(getApplicationContext(), foodArrayList);
            recyclerView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }
    }

}
