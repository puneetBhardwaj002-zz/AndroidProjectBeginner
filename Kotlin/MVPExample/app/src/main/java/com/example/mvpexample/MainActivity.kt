package com.example.mvpexample

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(),MainActivityPresenter.View {
    private var presenter:MainActivityPresenter?=null
    private lateinit var mTextView: TextView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mEtName:EditText
    private lateinit var mEtEmail:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI(){
        presenter = MainActivityPresenter(this)
        mTextView = findViewById(R.id.tv_detail)
        mProgressBar = findViewById(R.id.progressBar)
        mEtName = findViewById(R.id.et_name)
        mEtEmail = findViewById(R.id.et_email)
        initTextChangeListeners()
        showProgressBar()
    }

    private fun initTextChangeListeners() {
        mEtName.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter?.updateFullName(mEtName.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                hideProgressBar()
            }

        })
        mEtEmail.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                presenter?.updateEmail(mEtEmail.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                hideProgressBar()
            }

        })
    }

    override fun updateUserInfoTextView(info: String) {
        mTextView.text = info
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = View.INVISIBLE
    }
}
