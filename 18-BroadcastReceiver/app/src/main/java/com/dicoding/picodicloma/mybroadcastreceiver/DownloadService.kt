package com.dicoding.picodicloma.mybroadcastreceiver

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.util.Log
import java.lang.Exception

class DownloadService : IntentService("DownloadService") {

    companion object {
        val TAG = DownloadService::class.java.simpleName
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "Download Service dijalankan")

        if (intent != null) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException){
                e.printStackTrace()
            }

            val notifyFinishIntent = Intent(MainActivity.ACTION_DOWNLOAD_STATUS)
            sendBroadcast(notifyFinishIntent)
        }
    }

}
