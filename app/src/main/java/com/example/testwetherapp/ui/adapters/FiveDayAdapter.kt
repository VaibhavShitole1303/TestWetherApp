package com.example.testwetherapp.ui.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.testwetherapp.R
import com.example.testwetherapp.network.models.List
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class FiveDayAdapter(private val list: kotlin.collections.List<List?>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listofTodayWeather= listOf<List>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
        return FiveDayAdapterViewHolder(view)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as FiveDayAdapterViewHolder
        listofTodayWeather= list as kotlin.collections.List<List>
        val todaysForeCast=listofTodayWeather[position]

        val fahrenheitTemperature = todaysForeCast.main?.temp// Replace with your actual temperature in Fahrenheit
        val celsiusTemperature1 = fahrenheitTemperature?.minus(273.15)

        val decimalFormat = DecimalFormat("#.##")
        val formattedNumber = decimalFormat.format(celsiusTemperature1)
        holder.temp.setText(formattedNumber.toString()+"℃")

        val calender= Calendar.getInstance()
        val dateFormat=SimpleDateFormat("HH:MM")
        val formattedTime=dateFormat.format(calender.time)
        val timeFapi=todaysForeCast.dtTxt!!.split(" ")
        val partafterspace=timeFapi[1]
//        holder.time.text=partafterspace

        val formattedDate = timestampToFormattedDate(todaysForeCast.dt!!.toLong())

        holder.tv_date.text=formattedDate
        holder.tv_wind_percent.text=todaysForeCast.wind?.speed.toString()
        holder.tv_humidity_percent.text=todaysForeCast.main?.humidity.toString()
        holder.tv_rain_percent.text=todaysForeCast.pop.toString()
        var weatherCode=todaysForeCast.weather[0].id
        Log.d("aaaa",weatherCode.toString())
        when(weatherCode){

            200, 201, 202 ->     holder.iv_weather.setImageResource(R.drawable._0d)
            210, 211, 212, 221 -> holder.iv_weather.setImageResource(R.drawable._1d)
            230, 231, 232 ->holder.iv_weather.setImageResource(R.drawable._2d)
            300, 301, 302 ->holder.iv_weather.setImageResource(R.drawable._3d)
            310, 311, 312,313, 314, 321 ->holder.iv_weather.setImageResource(R.drawable._4d)
            500, 501, 502, 503, 504, 511 -> holder.iv_weather.setImageResource(R.drawable._9d)
            520, 521,522, 531 ->holder.iv_weather.setImageResource(R.drawable._9d)
            600, 601, 602, 611, 612, 615, 616, 620, 621, 622 ->holder.iv_weather.setImageResource(R.drawable._3d)
            701, 711, 721, 731, 741, 751, 761, 762, 771, 781 ->holder.iv_weather.setImageResource(R.drawable._2d)
            800, 801, 802, 803, 804 ->holder.iv_weather.setImageResource(R.drawable.day_forecast_hot_svgrepo_com)
            900, 901, 902, 903, 904, 905, 906, 951, 952, 953, 954, 955, 956, 957, 958, 959, 960, 961, 962 -> holder.iv_weather.setImageResource(R.drawable.unknown)
            else -> null
        }

        for (i in todaysForeCast.weather){
            if (i.icon=="01d"){

            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }




    class FiveDayAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_date:TextView=itemView.findViewById(R.id.tv_date)
        val temp:TextView=itemView.findViewById(R.id.tv_temp)
        val tv_rain_percent:TextView=itemView.findViewById(R.id.tv_rain_percent)
        val tv_wind_percent:TextView=itemView.findViewById(R.id.tv_wind_percent)
        val tv_humidity_percent:TextView=itemView.findViewById(R.id.tv_humidity_percent)
        val iv_weather: ImageView =itemView.findViewById(R.id.iv_weather)
    }


    fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) * 5 / 9
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun timestampToFormattedDate(timestamp: Long): String {
        val instant = Instant.ofEpochSecond(timestamp)
        val localDateTime = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())

        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
        return localDateTime.format(formatter)
    }
}