package com.github.llmaximll.gym.background

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.github.llmaximll.gym.LogInActivity
import com.github.llmaximll.gym.R
import java.util.concurrent.TimeUnit

private const val CHANNEL_ID = "channel_id"
private const val AM_INTENT_ACTION = "alarm_manager"
private const val AM_REQUEST_CODE = 0

class Receiver : BroadcastReceiver() {

    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == AM_INTENT_ACTION) {
            if (context != null) {
                addNotification(context)
            }
        }
    }

    fun setReceiver(context: Context) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Receiver::class.java)

        intent.action = AM_INTENT_ACTION
        pendingIntent = PendingIntent.getBroadcast(context, AM_REQUEST_CODE, intent, 0)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    TimeUnit.MINUTES.toMillis(5),
                    pendingIntent)
        } else {
            alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    TimeUnit.MINUTES.toMillis(5),
                    pendingIntent)
        }
    }

    fun stopAlarmManager(context: Context) {
        if (pendingIntent == null) {
            val intent = Intent(context, Receiver::class.java)
            intent.action = AM_INTENT_ACTION
            pendingIntent = PendingIntent.getBroadcast(context, AM_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }
        alarmManager?.cancel(pendingIntent)
    }

    private fun addNotification(context: Context) {
        val intent = Intent(context, LogInActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.mipmap.ic_launcher_round)
            setContentTitle("Тренировки")
            setContentText("Необходимы ежедневные тренировки для поддержания формы")
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Уведомления о тренировках",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(CHANNEL_ID)
        }
        notificationManager.notify(0, mBuilder.build())
    }
}