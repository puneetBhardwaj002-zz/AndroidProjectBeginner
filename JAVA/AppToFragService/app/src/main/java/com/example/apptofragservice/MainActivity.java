package com.example.apptofragservice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button vegButton = findViewById(R.id.button_veg);
        Button nonVegButton = findViewById(R.id.button_non_veg);

        vegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("Category","veg");
                final Fragment fragment = new MyFragment(MainActivity.this);
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        nonVegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("Category","non-veg");
                final Fragment fragment = new MyFragment(MainActivity.this);
                fragment.setArguments(args);
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title.toUpperCase());
    }
}
