package com.example.notification

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo


class MyAccessibilityClass : AccessibilityService(){
    override fun onInterrupt() {

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if(event?.eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED){
            if(event.packageName.toString() == "com.whatsapp"){
                val message = StringBuilder()
                if(event.text.isNotEmpty()){
                    for (seq in event.text){
                        message.append(seq)
                    }
                }
                if(message.toString().contains("Message from")){
                    val name = message.toString().substring(13)
                }
            }
        }
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            if (event.packageName == "com.whatsapp") {
                val currentNode = rootInActiveWindow
                if (currentNode != null && currentNode.className == "android.widget.FrameLayout" && currentNode.getChild(
                        2
                    ) != null && currentNode.getChild(2).className == "android.widget.TextView" && currentNode.getChild(
                        2
                    ).contentDescription == "Search"
                ) {
                    currentNode.getChild(2).performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
        info.eventTypes=AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        info.notificationTimeout = 100
        info.packageNames = null
        serviceInfo = info
    }

}