package com.example.samplewebview

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NewActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
        val intent = intent
        findViewById<TextView>(R.id.textView).text = intent.getStringExtra("message")
    }
}
