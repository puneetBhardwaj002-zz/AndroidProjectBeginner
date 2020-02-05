package com.example.keyguardscreenlock

import android.Manifest
import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.FragmentActivity
import java.security.KeyStore
import java.util.concurrent.Executors
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


class MainActivity : AppCompatActivity(), View.OnClickListener{

    private var biometricPrompt:BiometricPrompt? = null
    private var promptInfo:BiometricPrompt.PromptInfo? = null
    private var cipher: Cipher? = null
    private var keyStore: KeyStore? = null
    private var keyGenerator: KeyGenerator? = null
    private var textView: TextView? = null
    private var cryptoObject: FingerprintManagerCompat.CryptoObject? = null
    private var fingerprintManager: FingerprintManagerCompat? = null
    private var keyguardManager: KeyguardManager? = null
    private var lock: Button? = null
    private var disable:Button? = null
    private var enable:Button? = null
    private var devicePolicyManager: DevicePolicyManager? = null
    private var compName: ComponentName? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        fingerprintManager = FingerprintManagerCompat.from(this)
        devicePolicyManager = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
	    compName = ComponentName(this, MyAdmin::class.java)
	    lock =  findViewById(R.id.lock)
	    enable =  findViewById(R.id.enableBtn)
	    disable = findViewById(R.id.disableBtn)
	    lock?.setOnClickListener(this)
	    enable?.setOnClickListener(this)
	    disable?.setOnClickListener(this)
        val executor = Executors.newSingleThreadExecutor()
        val activity:FragmentActivity = this
        biometricPrompt = BiometricPrompt(activity,executor,object :BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if(errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON){
                    showToast(status = false)
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showToast(status = false)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showToast(status = true)
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Touch fingerprint")
            .setNegativeButtonText("Cancel")
            .build()
        textView = findViewById(R.id.tv_error_msg)
        showFingerprintPrompt()
    }

    private fun checkForPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            if(ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.USE_BIOMETRIC) == PackageManager.PERMISSION_GRANTED){
                showBiometricPrompt()
            } else {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.USE_BIOMETRIC),111)
            }
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED){
                showBiometricPrompt()
            } else {
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.USE_FINGERPRINT),111)
            }
        }
    }

    private fun showBiometricPrompt(){
        biometricPrompt?.authenticate(promptInfo!!)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 111){
            showBiometricPrompt()
        } else if (requestCode == 123){

        }
    }
    private fun showToast(status:Boolean){
        runOnUiThread{
            if(!status){
                Toast.makeText(this@MainActivity,"Invalid input", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity,"Authentication Success", Toast.LENGTH_SHORT).show()
                textView?.text = getString(R.string.success)
            }
        }
    }

    private fun showFingerprintPrompt() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && keyguardManager?.isKeyguardSecure!!){
            if(fingerprintManager?.isHardwareDetected!!){
                if(FingerprintManagerCompat.from(this).hasEnrolledFingerprints()){
                    checkForPermissions()
                } else {
                    textView?.text = getString(R.string.fingerprint_error_no_fingerprints)
                }
            } else {
                textView?.text = getString(R.string.fingerprint_error_hw_not_present)
            }
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try {
                generateKey()
            } catch (e:Exception){
                e.printStackTrace()
            }
            if(initCipher()){
                cryptoObject = FingerprintManagerCompat.CryptoObject(cipher!!)
                val helper  = FingerPrintHandler(this)
                helper.startAuth(fingerprintManager,cryptoObject!!)
            }
        }
        else {
            textView?.text = getString(R.string.android_version_not_supported)
        }
    }

    private fun initCipher(): Boolean {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7
                )
            }
            return try{
                keyStore?.load(null)
                val key = keyStore?.getKey(KEY_NAME,null) as SecretKey
                cipher?.init(Cipher.ENCRYPT_MODE,key)
                true
            } catch (e:Exception){
                e.printStackTrace()
                false
            }

        } catch (e:Exception){
            e.printStackTrace()
        }
        return false
    }

    private fun generateKey() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyStore = KeyStore.getInstance("AndroidKeyStore")
                keyGenerator =
                    KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                keyStore?.load(null)
                keyGenerator?.init(
                    KeyGenParameterSpec.Builder(
                        KEY_NAME,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build()
                )
            }
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    companion object{
        const val KEY_NAME = "mySecurityKey"
        const val RESULT_ENABLE = 11
    }

    override fun onResume() {
        super.onResume()
        val isActive = devicePolicyManager?.isAdminActive(compName!!)
        if(isActive!!){
            disable?.visibility = View.VISIBLE
            enable?.visibility = View.GONE
        } else {
            disable?.visibility = View.GONE
            enable?.visibility = View.VISIBLE
        }
    }

    override fun onClick(p0: View?) {
        if (p0 == lock) {
            val active = devicePolicyManager?.isAdminActive(compName!!)

            if (active!!) {
                devicePolicyManager?.lockNow()
            } else {
                Toast.makeText(this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show()
            }

        } else if (p0 == enable) {
              val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
              intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
              intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission")
              startActivity(intent)

        } else if (p0 == disable) {
          devicePolicyManager?.removeActiveAdmin(compName!!)
            disable?.visibility = View.GONE
            enable?.visibility = View.VISIBLE
        }
    }
}
