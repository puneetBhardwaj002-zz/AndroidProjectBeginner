package com.example.customview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPieChart = findViewById(R.id.rectangle);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.one:
                mPieChart.customPaddingUp(30);
                break;
            case R.id.two:
                mPieChart.swapColor();
                break;
            case R.id.three:
                mPieChart.customPaddingDown(30);
                break;
        }
    }
}
