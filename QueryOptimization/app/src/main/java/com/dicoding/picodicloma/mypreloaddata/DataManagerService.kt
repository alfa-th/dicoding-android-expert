package com.dicoding.picodicloma.mypreloaddata

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.dicoding.picodicloma.mypreloaddata.database.MahasiswaHelper
import com.dicoding.picodicloma.mypreloaddata.model.MahasiswaModel
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.coroutines.CoroutineContext

class DataManagerService : Service(), CoroutineScope {

    companion object {
        private val TAG = this::class.java.simpleName

        const val PREPARATION_MESSAGE = 0
        const val UPDATE_MESSAGE = 1
        const val SUCCESS_MESSAGE = 2
        const val FAILED_MESSAGE = 3
        const val CANCEL_MESSAGE = 4
        const val ACTIVITY_HANDLER = "activity_handler"
        const val MAX_PROGRESS = 100.0
    }

    private lateinit var job: Job
    private var mActivityMessenger: Messenger? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate() {
        super.onCreate()
        job = Job()
        Log.d(TAG, "onCreate : ")
    }

    override fun onBind(intent: Intent): IBinder? {
        mActivityMessenger = intent.getParcelableExtra(ACTIVITY_HANDLER)
        loadDataAsync()

        return mActivityMessenger.let { it?.binder }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind : ")
        job.cancel()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.d(TAG, "onDestroy : ")
    }

    private fun getData(): Boolean {
        val mahasiswaHelper = MahasiswaHelper.getInstance(applicationContext)
        val appPreference = AppPreference(applicationContext)

        val firstRun = appPreference.firstRun as Boolean

        if (firstRun) {
            val mahasiswaModels = preLoadRaw()

            mahasiswaHelper.open()

            var progress = 30.0
            publishProgress(progress.toInt())
            val progressMaxInsert = 80.0
            val progressDiff = (progressMaxInsert - progress) / mahasiswaModels.size

            var isInsertSuccess: Boolean

            try {
                mahasiswaHelper.beginTransaction()
                loop@ for (model in mahasiswaModels) {
                    when {
                        job.isCancelled -> break@loop
                        else -> {
                            mahasiswaHelper.insertTransaction(model)
                            progress += progressDiff
                            publishProgress(progress.toInt())
                        }
                    }
                }

                when {
                    job.isCancelled -> {
                        isInsertSuccess = false
                        appPreference.firstRun = true
                        sendMessage(CANCEL_MESSAGE)
                    }
                    else -> {
                        mahasiswaHelper.setTransactionProcess()
                        isInsertSuccess = true
                        appPreference.firstRun = false
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "doInBackground: Exception")
                isInsertSuccess = false
            } finally {
                mahasiswaHelper.endTransaction()
            }

            mahasiswaHelper.close()

            publishProgress(MAX_PROGRESS.toInt())

            return isInsertSuccess
        } else {
            try {
                synchronized(this) {
                    publishProgress(50)
                    publishProgress(MAX_PROGRESS.toInt())
                    return true
                }
            } catch (e: Exception) {
                return false
            }
        }
    }

    private fun publishProgress(progress: Int) {
        try {
            val message = Message.obtain(null, UPDATE_MESSAGE)
            val bundle = Bundle()
            bundle.putLong("KEY PROGRESS", progress.toLong())
            message.data = bundle
            mActivityMessenger?.send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun preLoadRaw(): ArrayList<MahasiswaModel> {
        val mahasiswaModels = ArrayList<MahasiswaModel>()
        var line: String?
        val reader: BufferedReader

        try {
            val rawText = resources.openRawResource(R.raw.data_mahasiswa)

            reader = BufferedReader(InputStreamReader(rawText))

            do {
                line = reader.readLine()
                val splitStr = line
                    .split("\t".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()

                val mahasiswaModel = MahasiswaModel()

                mahasiswaModel.nama = splitStr[0]
                mahasiswaModel.nim = splitStr[1]

                mahasiswaModels.add(mahasiswaModel)
            } while (line != null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return mahasiswaModels
    }

    private fun loadDataAsync() {
        sendMessage(PREPARATION_MESSAGE)

        job = launch {
            val isInsertSuccess = async(Dispatchers.IO) {
                getData()
            }

            if (isInsertSuccess.await()) {
                sendMessage(SUCCESS_MESSAGE)
            } else {
                sendMessage(FAILED_MESSAGE)
            }
        }

        job.start()
    }

    private fun sendMessage(messageStatus: Int) {
        val message = Message.obtain(null, messageStatus)

        try {
            mActivityMessenger?.send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
