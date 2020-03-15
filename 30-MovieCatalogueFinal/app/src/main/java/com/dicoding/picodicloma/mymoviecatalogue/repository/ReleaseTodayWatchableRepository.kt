package com.dicoding.picodicloma.mymoviecatalogue.repository


import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dicoding.picodicloma.mymoviecatalogue.AlarmReceiver
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReleaseTodayWatchableRepository(context: Context) {

    companion object {
        private val TAG = ReleaseTodayWatchableRepository::class.java.simpleName

        const val POSTER_SIZE = "w185"
        private const val API_KEY = "973fc85b2235e680ee5c80eb58c21fb2"
    }

    var watchableList = ArrayList<Watchable>()

    init {
        
        val queue = Volley.newRequestQueue(context)

        val overviewPlaceholder =
            context.getString(R.string.overview_not_available)

        val url =
            "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY" +
                    "&primary_release_date.gte=${getCurrentDate().toString()}" +
                    "&primary_release_date.lte=${getCurrentDate().toString()}"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                val responseWatchableJSON = response.getJSONArray("results")

                for (i in 0 until responseWatchableJSON.length()) {
                    val responseWatchable = responseWatchableJSON.getJSONObject(i)
                    val watchable =
                        Watchable(
                            poster = "https://image.tmdb.org/t/p/$POSTER_SIZE/${responseWatchable.getString(
                                "poster_path"
                            )}",
                            title = responseWatchable.getString("original_title"),
                            popularity = responseWatchable.getDouble("popularity"),
                            overview = if (responseWatchable.getString("overview") != "") responseWatchable.getString(
                                "overview"
                            ) else overviewPlaceholder,
                            release = responseWatchable.getString("release_date"),
                            isMovie = true
                        )

                    Log.d(TAG, "init : $watchable")
                    watchableList.add(watchable)
                }


                AlarmReceiver.onReceiveReleaseToday(context, watchableList)
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "$error")
            })

        queue.add(jsonObjectRequest)
    }

    private fun getCurrentDate(): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}