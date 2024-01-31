package com.example.testwetherapp.network

import com.example.testwetherapp.network.models.WeatherData
import com.example.testwetherapp.utils.Constant
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherApi {

    @POST("forecast?")
    suspend fun getWeatherData(
        @Query("lat")
        lat:Double,
        @Query("lon")
        lan:Double,
        @Query("appid")
        appid:String?="3ed165f86fd694d82481e7ad7f80e0c1",
    ): Response<WeatherData>

    @GET("forecast?")
    suspend fun getWeatherByCity(
        @Query("q")
        city:String,
        @Query("appid")
        appid:String="3ed165f86fd694d82481e7ad7f80e0c1",
    ): Response<WeatherData>
}