package com.example.testwetherapp.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testwetherapp.R
import com.example.testwetherapp.network.models.List
import com.example.testwetherapp.network.models.Main
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class TodaysAdapter(private val list: kotlin.collections.List<List?>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listofTodayWeather= listOf<List>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todays_forecast, parent, false)
        return TodaysAdapterViewHolder(view)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder , position: Int) {
            holder as TodaysAdapterViewHolder
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
        holder.time.text=partafterspace

        var weatherCode=todaysForeCast.weather[0].id
        Log.d("aaaa",weatherCode.toString())
        when(weatherCode){

            200, 201, 202 ->     holder.iv_weather_.setImageResource(R.drawable._0d)
            210, 211, 212, 221 -> holder.iv_weather_.setImageResource(R.drawable._1d)
            230, 231, 232 ->holder.iv_weather_.setImageResource(R.drawable._2d)
            300, 301, 302 ->holder.iv_weather_.setImageResource(R.drawable._3d)
            310, 311, 312,313, 314, 321 ->holder.iv_weather_.setImageResource(R.drawable._4d)
            500, 501, 502, 503, 504, 511 -> holder.iv_weather_.setImageResource(R.drawable._9d)
            520, 521,522, 531 ->holder.iv_weather_.setImageResource(R.drawable._9d)
            600, 601, 602, 611, 612, 615, 616, 620, 621, 622 ->holder.iv_weather_.setImageResource(R.drawable._3d)
            701, 711, 721, 731, 741, 751, 761, 762, 771, 781 ->holder.iv_weather_.setImageResource(R.drawable._2d)
            800, 801, 802, 803, 804 ->holder.iv_weather_.setImageResource(R.drawable.day_forecast_hot_svgrepo_com)
            900, 901, 902, 903, 904, 905, 906, 951, 952, 953, 954, 955, 956, 957, 958, 959, 960, 961, 962 -> holder.iv_weather_.setImageResource(R.drawable.unknown)
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




    class TodaysAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val time:TextView=itemView.findViewById(R.id.tv_time)
         val temp:TextView=itemView.findViewById(R.id.tv_temp_)
         val iv_weather_:ImageView=itemView.findViewById(R.id.iv_weather_)
    }


    fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) * 5 / 9
    }

}