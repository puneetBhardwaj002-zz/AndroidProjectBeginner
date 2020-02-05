package com.example.keyguardscreenlock

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyAdmin : DeviceAdminReceiver(){
    override fun onEnabled(context: Context, intent: Intent) {
        Toast.makeText(context,"Enabled",Toast.LENGTH_SHORT).show()
    }

    override fun onDisabled(context: Context, intent: Intent) {
        Toast.makeText(context,"Disabled",Toast.LENGTH_SHORT).show()
    }
}