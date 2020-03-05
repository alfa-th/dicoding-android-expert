package com.dicoding.picodicloma.mymoviecatalogue.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dicoding.picodicloma.mymoviecatalogue.R
import com.dicoding.picodicloma.mymoviecatalogue.model.Watchable

class SearchableViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        val TAG = SearchableViewModel::class.java.simpleName

        const val TYPE_MOVIE = 0
        const val TYPE_TV = 1

        const val POSTER_SIZE = "w185"
        private const val API_KEY = "973fc85b2235e680ee5c80eb58c21fb2"
    }

    val listSearchWatchable = MutableLiveData<ArrayList<Watchable>>()
    var currentQuery = MutableLiveData<String>()
    var viewModelType: Int = TYPE_MOVIE

    fun queryByTitle(context: Context?, query: String) {
        val queue = Volley.newRequestQueue(context)
        val listWatchableRequestResult = ArrayList<Watchable>()
        lateinit var url: String
        val locale = context?.getString(R.string.language_code)
        val overviewPlaceholder =
            context?.getString(R.string.overview_not_available)

        currentQuery.postValue(query)

        when (viewModelType) {
            TYPE_MOVIE ->
                url =
                    "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=${locale}&query=$query"

            TYPE_TV ->
                url =
                    "https://api.themoviedb.org/3/search/tv?api_key=$API_KEY&language=${locale}&query=$query"
        }

        Log.d(TAG, url)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val responseWatchableJSON = response.getJSONArray("results")

                for (i in 0 until responseWatchableJSON.length()) {
                    val responseWatchable = responseWatchableJSON.getJSONObject(i)
                    lateinit var watchable: com.dicoding.picodicloma.mymoviecatalogue.model.Watchable

                    when (viewModelType) {
                        TYPE_MOVIE -> {
                            watchable =
                                com.dicoding.picodicloma.mymoviecatalogue.model.Watchable(
                                    poster = "https://image.tmdb.org/t/p/$POSTER_SIZE${responseWatchable.getString(
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
                        }

                        TYPE_TV -> {
                            watchable =
                                com.dicoding.picodicloma.mymoviecatalogue.model.Watchable(
                                    poster = "https://image.tmdb.org/t/p/$POSTER_SIZE${responseWatchable.getString(
                                        "poster_path"
                                    )}",
                                    title = responseWatchable.getString("original_name"),
                                    popularity = responseWatchable.getDouble("popularity"),
                                    overview = if (responseWatchable.getString("overview") != "") responseWatchable.getString(
                                        "overview"
                                    ) else overviewPlaceholder,
                                    release = if (responseWatchable.has("first_air_date")) responseWatchable.getString(
                                        "first_air_date"
                                    ) else "",
                                    isMovie = false
                                )
                        }
                    }

//                    Log.d(TAG, "$watchable")
                    listWatchableRequestResult.add(watchable)
                }

//                Log.d(TAG, listWatchableRequestResult.toString())
                listSearchWatchable.postValue(listWatchableRequestResult)
            },
            Response.ErrorListener { error ->
                Log.d(TAG, error.toString())
            }
        )

        queue.add(jsonObjectRequest)
    }
}
