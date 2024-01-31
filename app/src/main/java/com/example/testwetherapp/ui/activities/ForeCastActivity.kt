package com.example.testwetherapp.ui.activities

import android.app.ProgressDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testwetherapp.R
import com.example.testwetherapp.databinding.ActivityForeCastBinding
import com.example.testwetherapp.databinding.ActivityMainBinding
import com.example.testwetherapp.network.ApiClient
import com.example.testwetherapp.network.WeatherApi
import com.example.testwetherapp.network.models.List
import com.example.testwetherapp.sharedprefs.SharedPref
import com.example.testwetherapp.ui.adapters.FiveDayAdapter
import com.example.testwetherapp.ui.adapters.TodaysAdapter
import com.example.testwetherapp.ui.repository.WeatherRepository
import com.example.testwetherapp.ui.viewmodel.WeatherViewModel
import com.example.testwetherapp.ui.viewmodel.WeatherViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ForeCastActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForeCastBinding
    private lateinit var mViewModelWeatherData: WeatherViewModel
    private var progressDialog: ProgressDialog? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForeCastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = WeatherRepository(ApiClient.getInstance().create(WeatherApi::class.java))
        val sharedPref= SharedPref.getInstance(this)
        mViewModelWeatherData = ViewModelProvider(
            this,
            WeatherViewModelFactory(repository)
        ).get(WeatherViewModel::class.java)
        mViewModelWeatherData.getWeatherData(44.34,10.99)
        progressDialog = ProgressDialog.show(this, "Loading", "Please wait...", true, false)

        initObserver()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObserver() {
        mViewModelWeatherData.WeatherDatadata.observe(this, Observer {
            //  Log.d("LOGINDATA", it.result.toString())
//            showHide(binding.progressLoading);

            if(it.cod=="200"){
                progressDialog?.dismiss()

                val forecastWeatherlist=mutableListOf<List>()
                forecastWeatherlist.clear()
                val currentDateTime= LocalDateTime.now()
                val currentDatepattern=currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))


                val wetherList=it.list
                val presentDate=currentDatepattern

                wetherList?.forEach{weather ->
                    if (weather.dtTxt!!.substring(11,16)== "12:00"){
                        forecastWeatherlist.add(weather)

                    }




                }
                if (forecastWeatherlist.isEmpty()){
                    Toast.makeText(this@ForeCastActivity,"No data  Available",Toast.LENGTH_LONG).show()
                }
                else{
                    Log.d(" datatatat", forecastWeatherlist.toString())

                    val adapter = FiveDayAdapter(forecastWeatherlist)
                    binding.rvForecast.adapter = adapter
                }
//                val closetWeather=findClosetWeather(todayWeatherlist)
//
//                val fahrenheitTemperature = closetWeather?.main?.temp// Replace with your actual temperature in Fahrenheit
//                val celsiusTemperature1 = fahrenheitToCelsius(fahrenheitTemperature!!)
////                binding.tvTemp.setText(celsiusTemperature1.toString())
//                binding.tvTemp.text=celsiusTemperature1.toString()


//                val kelvinTemperature =it.list[0].main?.feelsLike// Replace with your actual temperature in Kelvin
//                val celsiusTemperature = kelvinToCelsius(kelvinTemperature!!)
//                binding.tvTemp.setText(celsiusTemperature.toString())
            }
            else{

            }

        })
    }
}