package com.example.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test","inside onCreate");
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Fragment fragment = new MainFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.myFragment,fragment);
                transaction.commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Test","inside onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Test","inside onSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Test","inside onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Test","inside onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Test","inside onPause");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("Test","inside onRestoreInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Test","inside onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Test","inside onRestart");
    }
}
