package com.dicoding.picodicloma.favoriteapp.activity

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodicloma.favoriteapp.R
import com.dicoding.picodicloma.favoriteapp.adapter.WatchableAdapter
import com.dicoding.picodicloma.favoriteapp.helper.MappingHelper
import com.dicoding.picodicloma.favoriteapp.model.Watchable
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: WatchableAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Favorite"

        rv_watchables.layoutManager = LinearLayoutManager(this)
        rv_watchables.setHasFixedSize(true)
        adapter = WatchableAdapter(this)
        rv_watchables.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadWatchablesAsync()
            }
        }

        contentResolver.registerContentObserver(Watchable.URI_WATCHABLE, true, myObserver)

        if (savedInstanceState == null) {
            loadWatchablesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Watchable>(EXTRA_STATE)
            list?.let { adapter.listWatchables = list }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listWatchables)
    }


    private fun loadWatchablesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progress_circular.visibility = View.VISIBLE
            val deferredWatchables = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(Watchable.URI_WATCHABLE, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor!!)
            }
            val watchables = deferredWatchables.await()
            progress_circular.visibility = View.INVISIBLE
            if (watchables.size > 0) {
                adapter.listWatchables = watchables
            } else {
                adapter.listWatchables = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_watchables, message, Snackbar.LENGTH_SHORT).show()
    }
}
