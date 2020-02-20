package com.dicoding.picodicloma.myjobsscheduler

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.DecimalFormat

class GetCurrentWeatherJobService : JobService() {

    companion object {
        private val TAG = GetCurrentWeatherJobService::class.java.simpleName

        private const val CITY = "Surabaya"
        private const val APP_ID = "6ef87d43c7f7ac72b8f06fa12d239a07"
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob()")

        getCurrentWeather(params)

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob()")

        return true
    }

    private fun getCurrentWeather(job: JobParameters?) {
        Log.d(TAG, "getCurrentWeather() : Mulai")
        val client = AsyncHttpClient()
        val url = "http://api.openweathermap.org/data/2.5/weather?q=$CITY&appid=$APP_ID"
        Log.d(TAG, "getCurrentWeather() : $url")

        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, "onSuccess() : $result")

                try {
                    val responseObject = JSONObject(result)

                    val currentWeather =
                        responseObject
                            .getJSONArray("weather")
                            .getJSONObject(0).getString("main")

                    val description =
                        responseObject
                            .getJSONArray("weather")
                            .getJSONObject(0).getString("description")

                    val tempInKelvin =
                        responseObject
                            .getJSONObject("main")
                            .getDouble("temp")

                    val tempInCelcius = tempInKelvin - 273
                    val temperature = DecimalFormat("##.##").format(tempInCelcius)

                    val title = "Current Weather"
                    val message = "$currentWeather, $description with $temperature celsius"
                    val notifId = 100

                    showNotification(applicationContext, title, message, notifId)

                    Log.d(TAG, "onSuccess() : Success")
                    jobFinished(job, false)
                } catch (e: Exception) {
                    Log.d(TAG, "onSuccess() : Failed")
                    jobFinished(job, true)
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG, "onFailure() : Failed")
                jobFinished(job, true)
            }
        })

    }

    private fun showNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {
        val CHANNEL_ID = "1"
        val CHANNEL_NAME = "Job Scheduler Channel"

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_replay_30_black_24dp)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.black))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)

        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val notification = builder.build()

        notificationManagerCompat.notify(notifId, notification)
    }

}