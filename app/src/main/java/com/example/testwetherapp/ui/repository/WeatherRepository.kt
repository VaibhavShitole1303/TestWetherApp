package com.example.testwetherapp.ui.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testwetherapp.network.NetworkResult
import com.example.testwetherapp.network.WeatherApi
import com.example.testwetherapp.network.models.WeatherData

class WeatherRepository (private  val productsAPI: WeatherApi) {

    private val WeatherDataData=MutableLiveData<WeatherData>()
    val WeatherData: LiveData<WeatherData>
        get()=WeatherDataData
    suspend fun getWeatherData(lat: Double,lan:Double)
    {
        val result=productsAPI.getWeatherData(lat,lan)
        if(result?.body()!=null){
            WeatherDataData.postValue(result.body())
        }
    }

//    getWeatherByCity

//    private val WeatherDataData=MutableLiveData<WeatherData>()
//    val WeatherData: LiveData<WeatherData>
//        get()=WeatherDataData
    suspend fun getWeatherByCity(city: String) {
        val result=productsAPI.getWeatherByCity(city)
        if(result?.body()!=null){
            WeatherDataData.postValue(result.body())
        }
    }
}