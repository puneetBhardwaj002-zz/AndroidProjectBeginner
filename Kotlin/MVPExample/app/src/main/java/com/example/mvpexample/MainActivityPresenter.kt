package com.example.mvpexample

class MainActivityPresenter(private val view: View) {
    private val user = User()
    fun updateFullName(fullName: String?) {
        user.name = fullName
        view.updateUserInfoTextView(user.toString())
    }

    fun updateEmail(email: String?) {
        user.email = email
        view.updateUserInfoTextView(user.toString())
    }

    interface View {
        fun updateUserInfoTextView(info: String)
        fun showProgressBar()
        fun hideProgressBar()
    }
}