package com.dicoding.picodicloma.mypreloaddata

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodicloma.mypreloaddata.DataManagerService.Companion.CANCEL_MESSAGE
import com.dicoding.picodicloma.mypreloaddata.DataManagerService.Companion.FAILED_MESSAGE
import com.dicoding.picodicloma.mypreloaddata.DataManagerService.Companion.PREPARATION_MESSAGE
import com.dicoding.picodicloma.mypreloaddata.DataManagerService.Companion.SUCCESS_MESSAGE
import com.dicoding.picodicloma.mypreloaddata.DataManagerService.Companion.UPDATE_MESSAGE
import com.dicoding.picodicloma.mypreloaddata.adapter.MahasiswaAdapter
import kotlinx.android.synthetic.main.activity_mahasiswa.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), HandlerCallback {

    companion object {
        private val TAG = this::class.java.simpleName
    }

    private lateinit var mBoundService: Messenger
    private var mServiceBound: Boolean = false

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBoundService = Messenger(service)
            mServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mServiceBound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mBoundServiceIntent = Intent(this, DataManagerService::class.java)
        val mActivityMessenger = Messenger(IncomingHandler(this))
        mBoundServiceIntent.putExtra(DataManagerService.ACTIVITY_HANDLER, mActivityMessenger)

        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()

        unbindService(mServiceConnection)
    }

    override fun onPreparation() {
        Toast.makeText(
            this,
            "Starting to load the data",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    override fun updateProgress(progress: Long) {
        Log.d(TAG, "updateProgress : ")
        progress_bar.progress = progress.toInt()
    }

    override fun loadSuccess() {
        Toast.makeText(
            this,
            "Loading file succeed",
            Toast.LENGTH_LONG
        )
            .show()

        startActivity(Intent(this, MahasiswaActivity::class.java))
        finish()
    }

    override fun loadFailed() {
        Toast.makeText(
            this,
            "Loading file failed",
            Toast.LENGTH_LONG
        )
            .show()
    }

    override fun loadCancel() {
        finish()
    }

    private class IncomingHandler(callback: HandlerCallback) : Handler() {
        private var weakCallback: WeakReference<HandlerCallback> = WeakReference(callback)

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                PREPARATION_MESSAGE -> {
                    weakCallback.get()?.onPreparation()
                }
                UPDATE_MESSAGE -> {
                    val bundle = msg.data
                    val progress = bundle.getLong("KEY PROGRESS")
                    weakCallback.get()?.updateProgress(progress)
                }
                SUCCESS_MESSAGE -> {
                    weakCallback.get()?.loadSuccess()
                }
                FAILED_MESSAGE -> {
                    weakCallback.get()?.loadFailed()
                }
                CANCEL_MESSAGE -> {
                    weakCallback.get()?.loadCancel()
                }
            }
        }
    }
}

interface HandlerCallback {
    fun onPreparation()

    fun updateProgress(progress: Long)

    fun loadSuccess()

    fun loadFailed()

    fun loadCancel()
}
