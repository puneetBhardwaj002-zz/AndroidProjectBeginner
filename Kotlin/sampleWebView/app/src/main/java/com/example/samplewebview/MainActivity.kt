package com.example.samplewebview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private val mUrl = "http://34.70.163.136:8080/verifier/doInit"
    private val mCallbackUrl = "\"https://paytm.com/recharge\""
    private lateinit var mWebView:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mWebView  = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled=true
        webView.settings.domStorageEnabled=true
        mWebView.addJavascriptInterface(WebViewJavaScriptInterface(this),"interface")
        callAPI()

    }
    private fun callAPI(){
        val body:MutableMap<String,Any> = HashMap()
        body["callbackUrl"] = mCallbackUrl
        body["userId"]=482
        body["verificationMethods"]="password"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST,mUrl,
            JSONObject(body.toString()),Response.Listener { response ->
                mWebView.loadDataWithBaseURL(null,response.getString("htmlView"), "text/html", "UTF-8",null)
        },
        Response.ErrorListener { error ->
            error.printStackTrace()
        })
        val queue = Volley.newRequestQueue(this)
        queue.add(jsonObjectRequest)
    }

    class WebViewJavaScriptInterface(private var context: Context) {

        @JavascriptInterface
        fun makeToast(message: String?) {
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

        @JavascriptInterface
        fun exit(param:String){
            Log.d("Test","Exit called")
            val intent = Intent(context,NewActivity::class.java)
            intent.putExtra("message",param)
            context.startActivity(intent)
        }
    }
}
