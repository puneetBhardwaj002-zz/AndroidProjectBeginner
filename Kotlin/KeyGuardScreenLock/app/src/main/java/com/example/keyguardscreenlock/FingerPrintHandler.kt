package com.example.keyguardscreenlock

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal

@TargetApi(Build.VERSION_CODES.M)
class FingerPrintHandler(private val context: Context) :FingerprintManagerCompat.AuthenticationCallback(){

    private var cancellationSignal:CancellationSignal? = null

    fun startAuth(fingerprintManager: FingerprintManagerCompat?, cryptoObject: FingerprintManagerCompat.CryptoObject) {
        cancellationSignal = CancellationSignal()
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
            return
        }
        fingerprintManager?.authenticate(cryptoObject,0,cancellationSignal,this,null)
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
        Toast.makeText(context, "Authentication error\n$errString", Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
        Toast.makeText(context, "Authentication error\n$helpString", Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
        Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show()
    }

}
