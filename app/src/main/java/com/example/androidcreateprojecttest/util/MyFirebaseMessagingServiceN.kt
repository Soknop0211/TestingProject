package com.example.androidcreateprojecttest.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.androidcreateprojecttest.R
import com.example.androidcreateprojecttest.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject

class MyFirebaseMessagingServiceN : FirebaseMessagingService() {

    private var messageBody = ""
    private var title = ""

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("com.eazy.daikou.fcm.MyFirebaseMessagingService.TAG", "From: " + remoteMessage.from)
        Log.d("com.eazy.daikou.fcm.MyFirebaseMessagingService.TAG", remoteMessage.toString())

        if (remoteMessage.notification != null){
            Log.d("com.eazy.daikou.fcm.MyFirebaseMessagingService", "From: " + remoteMessage.notification!!.body)
            messageBody = remoteMessage.notification!!.body!!
            title = remoteMessage.notification!!.title!!
        }

        if (remoteMessage.data.isNotEmpty()) {
            messageBody = remoteMessage.data["body"].toString()
            title = remoteMessage.data["title"].toString()
            if (remoteMessage.data.containsKey("additional_data")) {
                val data = remoteMessage.data["additional_data"]
                try {
                    if (data != null) {
                        val jsonObject = JSONObject(data)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.d("erkf", e.message!!)
                }
            }
        }

        //Click on Notification
        sendNotification()
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID tokenss
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.e("newToken", token)
//        logDebug(com.eazy.daikou.fcm.MyFirebaseMessagingService.TAG, "Refreshed_token: $token")
//        Utils.saveString(Constant.FIREBASE_TOKEN, token, applicationContext)
//        val tToken: String = Utils.getString(Constant.FIREBASE_TOKEN, applicationContext)
//        logDebug(com.eazy.daikou.fcm.MyFirebaseMessagingService.TAG, tToken)
//        UserSessionManagement.saveString(Constant.OUT_IN_APP, "true", applicationContext)
    }

    private fun sendNotification() {
        var pendingIntent: PendingIntent? = null
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (messageBody == "") {
            messageBody = "Description"
        }

        if (title == "") {
            title = "Notification"
        }

        // String channelId = "channel_id";
        val channelId = this.packageName
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageBody))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        notificationBuilder.setSmallIcon(R.drawable.chat_box)
        notificationBuilder.color = ContextCompat.getColor(this, R.color.colorPrimary)
        notificationBuilder.setSound(defaultSoundUri)

        notificationBuilder.setContentIntent(pendingIntent)

        // Since android Oreo notification channel is needed.
        val notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = this.getString(R.string.app_name)
            val description = this.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager.createNotificationChannel(channel)
        }
        val num = System.currentTimeMillis().toInt()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(num /* ID of notification */, notificationBuilder.build())
    }
}