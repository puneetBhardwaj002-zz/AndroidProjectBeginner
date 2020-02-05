package com.example.colormyviews

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.colormyviews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        setListeners()

    }
    private fun makeColored(view: View){
        when(view.id){
            R.id.box_one_text -> view.setBackgroundColor(Color.DKGRAY)
            R.id.box_two_text -> view.setBackgroundColor(Color.GRAY)
            R.id.box_three_text -> view.setBackgroundColor(Color.BLUE)
            R.id.box_four_text -> view.setBackgroundColor(Color.MAGENTA)
            R.id.box_five_text -> view.setBackgroundColor(Color.BLUE)
            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }
    private fun setListeners(){
        binding.apply {
            boxOneText.setOnClickListener { makeColored(boxOneText) }
            boxTwoText.setOnClickListener { makeColored(boxTwoText) }
            boxThreeText.setOnClickListener { makeColored(boxThreeText) }
            boxFourText.setOnClickListener { makeColored(boxFourText) }
            boxFiveText.setOnClickListener { makeColored(boxFiveText) }
            constraintLayout.setOnClickListener { makeColored(constraintLayout) }
        }

    }
}
