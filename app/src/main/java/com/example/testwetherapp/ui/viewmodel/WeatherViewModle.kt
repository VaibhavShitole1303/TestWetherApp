package com.example.testwetherapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testwetherapp.network.NetworkResult
import com.example.testwetherapp.network.models.WeatherData
import com.example.testwetherapp.ui.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel (private  val repository: WeatherRepository):ViewModel() {



    val WeatherDatadata:LiveData<WeatherData>
        get()=repository.WeatherData

    fun getWeatherData(lat:Double,lan:Double) {
        viewModelScope.launch(Dispatchers.IO){
            repository.getWeatherData(lat,lan)
        }
    }

    val WeatherByCityData:LiveData<WeatherData>
        get()=repository.WeatherData

    fun getWeatherByCityData(city:String) {
        viewModelScope.launch(Dispatchers.IO){
            repository.getWeatherByCity(city)
        }
    }
}