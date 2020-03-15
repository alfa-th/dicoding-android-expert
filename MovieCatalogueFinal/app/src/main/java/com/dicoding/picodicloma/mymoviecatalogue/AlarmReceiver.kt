package com.dicoding.picodicloma.mymoviecatalogue

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.picodicloma.mymoviecatalogue.activity.MainActivity
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import com.dicoding.picodicloma.mymoviecatalogue.repository.ReleaseTodayWatchableRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = AlarmReceiver::class.java.simpleName
        const val TYPE_DAILY = "daily_reminder"
        const val TYPE_RELEASE = "release_reminder"

        const val DAILY_CHANNEL_ID = "Channel_1"
        const val DAILY_CHANNEL_NAME = "Daily Reminder Channel"
        const val RELEASE_CHANNEL_ID = "Channel_1"
        const val RELEASE_CHANNEL_NAME = "Release Today Reminder Channel"

        var ID_DAILY_REMINDER = 101
        var ID_RELEASE_REMINDER = 201

        var ID_RELEASE_REMINDER_DIFF = 0

        private const val EXTRA_TYPE = "extra_type"

        fun showReleaseReminderNotification(
            context: Context,
            title: String,
            message: String
        ) {
            val notificationManagerCompat =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val builder =
                NotificationCompat.Builder(context, RELEASE_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_movie_black_80dp)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(ContextCompat.getColor(context, android.R.color.black))
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setSound(alarmSound)
                    .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    RELEASE_CHANNEL_ID,
                    RELEASE_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
                builder.setChannelId(RELEASE_CHANNEL_ID)
                notificationManagerCompat?.createNotificationChannel(
                    channel
                )
            }

            val notification = builder.build()

            notificationManagerCompat?.let {
                it.notify(ID_RELEASE_REMINDER++, notification)
                ++ID_RELEASE_REMINDER_DIFF
            }
        }

        fun onReceiveReleaseToday(
            context: Context,
            watchables: ArrayList<Watchable>
        ) {
            for (i in 0 until watchables.size) {
                val title: String = watchables[i].title
                val message =
                    String.format(context.getString(R.string.release_reminder_message), title)
                showReleaseReminderNotification(context, title, message)
            }

            ID_RELEASE_REMINDER -= ID_RELEASE_REMINDER_DIFF
            ID_RELEASE_REMINDER_DIFF = 0
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: intent.extras: ${intent.extras}")
        intent.getStringExtra(EXTRA_TYPE)?.let {
            if (it.equals(TYPE_DAILY, ignoreCase = true)) {
                Log.d(TAG, "onReceive: showDailyReminderNotification - Exec")
                showDailyReminderNotification(context)
            } else {
                Log.d(TAG, "onReceive: ReleaseTodayWatchableRepository - Exec")
                ReleaseTodayWatchableRepository(context)
            }
        }
    }

    fun setRepeatingAlarm(context: Context, type: String, time: String) {
        if (isTimeInvalid(time)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TYPE, type)

        val calendar = parseTime(time)
        val secondsToWaitBeforeNextNotification =
            (parseTime(getCurrentTime()).timeInMillis - calendar.timeInMillis + 1000)

        Log.d(TAG, "setRepeatingAlarm: ${calendar.timeInMillis + 1000}")
        Log.d(TAG, "currentTime: ${parseTime(getCurrentTime()).timeInMillis}")
        Log.d(TAG, "secondsToWaitBeforeNextNotification: $secondsToWaitBeforeNextNotification")

        val requestCode = if (type == TYPE_DAILY) ID_DAILY_REMINDER else ID_RELEASE_REMINDER

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis + 1000,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(
            context,
            "${context.getString(R.string.alarm_turned_on)}",
            Toast.LENGTH_SHORT
        )
            .show()
    }


    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = if (type == TYPE_DAILY) ID_DAILY_REMINDER else ID_RELEASE_REMINDER
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(
            context,
            context.getString(R.string.repeating_cancel),
            Toast.LENGTH_SHORT
        )
            .show()
    }

    private fun isTimeInvalid(time: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(time)
            false
        } catch (e: ParseException) {
            true
        }
    }

    private fun showDailyReminderNotification(context: Context) {
        val title = context.getString(R.string.app_name)
        val message = context.getString(R.string.daily_reminder_message)

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            ID_RELEASE_REMINDER,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder =
            NotificationCompat.Builder(context, DAILY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_black_80dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                DAILY_CHANNEL_ID,
                DAILY_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(DAILY_CHANNEL_ID)
            notificationManagerCompat?.createNotificationChannel(
                channel
            )
        }
        val notification = builder.build()

        notificationManagerCompat?.notify(
            ID_DAILY_REMINDER,
            notification
        )
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun parseTime(time: String): Calendar {
        val timeArray: Array<String> = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = timeArray[0].toInt()
        calendar[Calendar.MINUTE] = timeArray[1].toInt()
        calendar[Calendar.SECOND] = 0

        return calendar
    }
}
