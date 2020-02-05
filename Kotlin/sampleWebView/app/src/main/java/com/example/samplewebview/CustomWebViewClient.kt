package com.example.samplewebview

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient

class CustomWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if(Uri.parse(url).host == "paytm.com"){
            return false
        }
        return true
    }
}