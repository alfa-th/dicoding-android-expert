package com.dicoding.picodicloma.myviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.DecimalFormat

class MainViewModel : ViewModel() {

    companion object {
        private const val API_KEY = "6ef87d43c7f7ac72b8f06fa12d239a07"
    }

    val listWeathersItems = MutableLiveData<ArrayList<WeatherItems>>()

    internal fun setWeather(cities: String) {
        val client = AsyncHttpClient()
        val listItems = ArrayList<WeatherItems>()
        val url =
            "https://api.openweathermap.org/data/2.5/group?id=$cities&units=metric&appid=$API_KEY"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("list")

                    for (i in 0 until list.length()) {
                        val weather = list.getJSONObject(i)
                        val weatherItems = WeatherItems()
                        weatherItems.id = weather.getInt("id")
                        weatherItems.name = weather.getString("name")
                        weatherItems.currentWeather = weather
                            .getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("main")
                        weatherItems.description = weather
                            .getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description")
                        val tempInKelvin = weather
                            .getJSONObject("main")
                            .getDouble("temp")
                        val tempInCelcius = tempInKelvin - 273
                        weatherItems.temperature = DecimalFormat("##.##")
                            .format(tempInCelcius)

                        listItems.add(weatherItems)
                    }

                    listWeathersItems.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }

    internal fun getWeathers() : MutableLiveData<ArrayList<WeatherItems>> {
        return listWeathersItems
    }
}
