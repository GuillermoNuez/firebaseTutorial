package com.example.firebasexml

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                baseContext, message.notification?.title ?: "No title",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}