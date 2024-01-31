package com.example.testwetherapp.myapp

import android.app.Application
import com.example.testwetherapp.network.WeatherApi
import com.example.testwetherapp.ui.repository.WeatherRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application(){



    override fun onCreate() {
        super.onCreate()


    }
}