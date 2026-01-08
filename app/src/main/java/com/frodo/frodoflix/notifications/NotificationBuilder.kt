package com.frodo.frodoflix.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.frodo.frodoflix.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService(){
    companion object {
        const val CHANNEL_ID = "default_channel"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val sender = remoteMessage.data["sender"]
        val prefs = this.getSharedPreferences("login", MODE_PRIVATE)

        val currentUser = prefs.getString("username", "")

        if (sender == currentUser){
            return
        }
    }

    override fun onNewToken(token: String) {
        saveTokenToDatabase(token)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(title: String, body: String) {
        createChannelIfNeeded()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

    private fun createChannelIfNeeded() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Default Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    private fun saveTokenToDatabase(token: String) {
        // implement later
    }
}
