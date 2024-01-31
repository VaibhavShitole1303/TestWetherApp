package com.example.testwetherapp.ui.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBinderMapperImpl
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testwetherapp.network.models.List
import com.example.testwetherapp.R
import com.example.testwetherapp.databinding.ActivityMainBinding
import com.example.testwetherapp.network.ApiClient
import com.example.testwetherapp.network.WeatherApi
import com.example.testwetherapp.network.models.Weather
import com.example.testwetherapp.sharedprefs.SharedPref
import com.example.testwetherapp.ui.adapters.FiveDayAdapter
import com.example.testwetherapp.ui.adapters.TodaysAdapter
import com.example.testwetherapp.ui.repository.WeatherRepository
import com.example.testwetherapp.ui.viewmodel.WeatherViewModel
import com.example.testwetherapp.ui.viewmodel.WeatherViewModelFactory
import com.example.testwetherapp.utils.Constant
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private lateinit var mViewModelWeatherData: WeatherViewModel
    val todayWeatherlist= mutableListOf<List>()
    private var progressDialog: ProgressDialog? = null
//    val forecastWeatherlist= mutableListOf<List>()


    val forecastWeatherlist= mutableListOf<List>()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = WeatherRepository(ApiClient.getInstance().create(WeatherApi::class.java))
       val sharedPref=SharedPref.getInstance(this)
        mViewModelWeatherData = ViewModelProvider(
            this,
            WeatherViewModelFactory(repository)
        ).get(WeatherViewModel::class.java)
        progressDialog = ProgressDialog.show(this, "Loading", "Please wait...", true, false)
        binding.clMain.visibility= View.GONE
        initListener();
        initObserver()
        mViewModelWeatherData.getWeatherData(18.5204,73.8567)
         setData()
        if (checkLOcationPermission()){
            getCurrentLocation()
        }else{
            requestLocationPermission()
        }

    }

    private fun setData() {
        binding.ivRain.setImageResource(R.drawable._9d)
        binding.ivWind.setImageResource(R.drawable._0d)
        binding.ivHumidity.setImageResource(R.drawable.humidity_svgrepo_com)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            Constant.PERMISSION_REQUEST
        )
    }

    private fun getCurrentLocation() {
        val locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )==PackageManager.PERMISSION_GRANTED){
            val location :Location?=
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location !=null){
                val lat=location.latitude
                val lan=location.longitude

                val mypref=SharedPref.getInstance(this)
                mypref.setValue("lat",lat.toString())
                mypref.setValue("lon",lan.toString())

            }
        }
    }

    private fun checkLOcationPermission(): Boolean {
        val fineLocationPermission=ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission=ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocationPermission== PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission== PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== Constant.PERMISSION_REQUEST){
            if (grantResults.isNotEmpty()){
                getCurrentLocation()
            }else{

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObserver() {

        mViewModelWeatherData.WeatherDatadata.observe(this, Observer {
            //  Log.d("LOGINDATA", it.result.toString())
//            showHide(binding.progressLoading);

            if(it.cod=="200"){
                binding.clMain.visibility= View.VISIBLE
                progressDialog?.dismiss()
                todayWeatherlist.clear()
                val currentDateTime=LocalDateTime.now()
                val currentDatepattern=currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                binding.tvLocation.text=it.city?.name.toString()
                val wetherList=it.list
                val presentDate=currentDatepattern
                val mypref=SharedPref.getInstance(this)
                try {
                    mypref.setValue("city",it.city?.name.toString())

                }catch (e:Exception){

                }


                wetherList?.forEach{weather ->
                    //seperate all the wether object that have date of today
                    if (weather.dtTxt!!.split("\\s".toRegex()).contains(presentDate)){
                        todayWeatherlist.add(weather)
                        val adapter = TodaysAdapter(todayWeatherlist)
                        binding.recyclerview.adapter = adapter
                    }else{
                        forecastWeatherlist.add(weather)

                    }
                }

                val closetWeather=findClosetWeather(todayWeatherlist)
               binding.tvHumidityPercent.setText(closetWeather?.main?.humidity!!.toString())
               binding.tvWindPercent.setText(closetWeather.wind?.speed!!.toString())
                val fahrenheitTemperature = closetWeather.main?.temp// Replace with your actual temperature in Fahrenheit
                val celsiusTemperature1 =  fahrenheitTemperature?.minus(273.15)

//                binding.tvTemp.setText(celsiusTemperature1.toString())
                val decimalFormat = DecimalFormat("#.##")
                val formattedNumber = decimalFormat.format(celsiusTemperature1)
                binding.tvTemp.text=formattedNumber.toString() +"℃"
                val formattedDate = timestampToFormattedDate(closetWeather.dt!!.toLong())
                binding.tvRainPercent.text=closetWeather.pop.toString() + "%"
                binding.tvDate.text=formattedDate
                binding.tvWeatherCondition.text=closetWeather.weather[0].description.toString()
                var i = closetWeather.weather.size
                var weatherCode=closetWeather.weather[0].id
                Log.d("aaaa",i.toString())
                when(weatherCode){

                        200, 201, 202 ->     binding.ivWeather.setImageResource(R.drawable._0d)
                        210, 211, 212, 221 -> binding.ivWeather.setImageResource(R.drawable._1d)
                        230, 231, 232 ->binding.ivWeather.setImageResource(R.drawable._2d)
                        300, 301, 302 ->binding.ivWeather.setImageResource(R.drawable._3d)
                        310, 311, 312,313, 314, 321 ->binding.ivWeather.setImageResource(R.drawable._4d)
                        500, 501, 502, 503, 504, 511 -> binding.ivWeather.setImageResource(R.drawable._9d)
                        520, 521,522, 531 ->binding.ivWeather.setImageResource(R.drawable._9d)
                        600, 601, 602, 611, 612, 615, 616, 620, 621, 622 ->binding.ivWeather.setImageResource(R.drawable._3d)
                        701, 711, 721, 731, 741, 751, 761, 762, 771, 781 ->binding.ivWeather.setImageResource(R.drawable._3d)
                        800, 801, 802, 803, 804 ->binding.ivWeather.setImageResource(R.drawable.day_forecast_hot_svgrepo_com)
                        900, 901, 902, 903, 904, 905, 906, 951, 952, 953, 954, 955, 956, 957, 958, 959, 960, 961, 962 -> binding.ivWeather.setImageResource(R.drawable.unknown)
                        else -> null
                }        }
            else{
                binding.clMain.visibility= View.VISIBLE
            }

        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun timestampToFormattedDate(timestamp: Long): String {
        val instant = Instant.ofEpochSecond(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())

        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
        return localDateTime.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun findClosetWeather(weatherlist: kotlin.collections.List<List>): List? {

       val systemTime=LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
        var closestWeather: List? = null
        var minTimeDifference=Int.MAX_VALUE
        for (weather in weatherlist){
            val  weatherTime=weather.dtTxt!!.substring(11,16)
            val timeDifference=Math.abs(timeToMinutes(weatherTime) - timeToMinutes(systemTime))
            if (timeDifference < minTimeDifference){
                minTimeDifference=timeDifference
                closestWeather=weather
            }
        }
        return closestWeather
    }

    private fun timeToMinutes(time:String):Int{
        val parts =time.split(":")
        return parts[0].toInt() * 60 + parts[1].toInt()
    }
    fun kelvinToCelsius(kelvin: Double): Double {
        return kelvin - 273.15
    }

    fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) * 5 / 9
    }

    private fun initListener() {
        val searchEditText= binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
    searchEditText.setTextColor(Color.WHITE)
        binding.searchView.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val sharePref=SharedPref.getInstance(this@MainActivity)
                sharePref.setValueOrNull("city",query!!)
                if (!query.isNullOrEmpty()){
                    mViewModelWeatherData.getWeatherByCityData(query)
                    progressDialog = ProgressDialog.show(this@MainActivity, "Loading", "Please wait...", true, false)

                    binding.searchView.setQuery(" ",false)
                    binding.searchView.clearFocus()


                    binding.searchView.isIconified=true

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        } )
        binding.ivReload.setOnClickListener {
            val mypref=SharedPref.getInstance(this)
            val city=  mypref.getvalue("city")
            mViewModelWeatherData.getWeatherByCityData(city!!)
            progressDialog = ProgressDialog.show(this@MainActivity, "Loading", "Please wait...", true, false)


        }
        binding.tv5Days.setOnClickListener {
            var intent=Intent(this@MainActivity,ForeCastActivity::class.java)

            startActivity(intent)
        }
    }


}