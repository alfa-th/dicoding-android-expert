package com.dicoding.picodicloma.mymoviecatalogue.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable
import com.dicoding.picodicloma.mymoviecatalogue.model.WatchableEnum

class WatchableViewModel : ViewModel() {

    companion object {
        val TAG = WatchableViewModel::class.java.simpleName
        const val POSTER_SIZE = "w185"
        private const val API_KEY = "973fc85b2235e680ee5c80eb58c21fb2"
    }

    private lateinit var viewModelType: WatchableEnum
    private val listWatchable = MutableLiveData<ArrayList<Watchable>>()

    fun setType(watchableType: WatchableEnum) {
        viewModelType = watchableType
    }

    internal fun setWatchable(context: Context?) {
        val queue = Volley.newRequestQueue(context)
        val listWatchableRequestResult = ArrayList<Watchable>()
        val url: String
        val locale = context?.getString(R.string.language_code)
        val overviewPlaceholder =
            context?.getString(R.string.overview_not_available)

        when (viewModelType) {
            WatchableEnum.MOVIE ->
                url =
                    "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&language=${locale}"

            WatchableEnum.TV_SHOWS ->
                url =
                    "https://api.themoviedb.org/3/discover/tv?api_key=$API_KEY&language=${locale}"
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val responseWatchableJSON = response.getJSONArray("results")

                for (i in 0 until responseWatchableJSON.length()) {
                    val responseWatchable = responseWatchableJSON.getJSONObject(i)
                    var watchable: Watchable

                    when (viewModelType) {
                        WatchableEnum.MOVIE -> {
                            watchable =
                                Watchable(
                                    poster = "https://image.tmdb.org/t/p/$POSTER_SIZE${responseWatchable.getString(
                                        "poster_path"
                                    )}",
                                    title = responseWatchable.getString("original_title"),
                                    popularity = responseWatchable.getDouble("popularity"),
                                    overview = if (responseWatchable.getString("overview") != "") responseWatchable.getString(
                                        "overview"
                                    ) else overviewPlaceholder,
                                    release = responseWatchable.getString("release_date")
                                )
                        }

                        WatchableEnum.TV_SHOWS -> {
                            watchable =
                                Watchable(
                                    poster = "https://image.tmdb.org/t/p/$POSTER_SIZE${responseWatchable.getString(
                                        "poster_path"
                                    )}",
                                    title = responseWatchable.getString("original_name"),
                                    popularity = responseWatchable.getDouble("popularity"),
                                    overview = if (responseWatchable.getString("overview") != "") responseWatchable.getString(
                                        "overview"
                                    ) else overviewPlaceholder,
                                    release = responseWatchable.getString("first_air_date")
                                )
                        }
                    }

                    listWatchableRequestResult.add(watchable)
                }

                Log.d(TAG, listWatchableRequestResult.toString())
                listWatchable.postValue(listWatchableRequestResult)
            },
            Response.ErrorListener { error ->
                Log.d(TAG, error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }

    internal fun getWatchable(): MutableLiveData<ArrayList<Watchable>> {
        return listWatchable
    }
}