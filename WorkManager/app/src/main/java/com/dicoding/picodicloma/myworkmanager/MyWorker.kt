package com.dicoding.picodicloma.myworkmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import java.text.DecimalFormat

class MyWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    companion object {
        private val TAG = MyWorker::class.java.simpleName

        const val APP_ID = "6ef87d43c7f7ac72b8f06fa12d239a07"

        const val EXTRA_CITY = "extra_city"

        const val NOTIFICATION_ID = 1

        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding_channel"
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        val dataCity = inputData.getString(EXTRA_CITY)
        val result = getCurrentWeather(dataCity)

        return result
    }

    private fun getCurrentWeather(city: String?): Result {
        Log.d(TAG, "getCurrentWeather : Starting")

        val client = SyncHttpClient()
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$APP_ID"

        Log.d(TAG, "getCurrentWeather() : $url")

        client.post(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)

                try {
                    val responseObject = JSONObject(result)

                    val currentWeather =
                        responseObject
                            .getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("main")
                    val description =
                        responseObject
                            .getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description")
                    val tempInKelvin =
                        responseObject
                            .getJSONObject("main")
                            .getDouble("temp")
                    val tempInCelcius = tempInKelvin - 273
                    val temperature =
                        DecimalFormat("##.##")
                            .format(tempInCelcius)
                    val title = "Current weather in $city"
                    val message = "$currentWeather, $description with $temperature celcius"

                    showNotification(title, message)

                    Log.d(TAG, "onSuccess() : Success")
                    resultStatus = Result.success()
                } catch (e: Exception) {
                    showNotification("Failed to get current weather", e.message)
                    Log.d(TAG, "onSuccess() : Failed")
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d(TAG, "onFailure() : Failed")
                showNotification("Failed to get current weather", error.message)
                resultStatus = Result.failure()
            }
        })

        return resultStatus as Result
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

}