package com.example.testwetherapp.sharedprefs

import android.content.Context
import android.content.SharedPreferences

class SharedPref internal constructor(private  val context:Context) {
    companion object{
        private const val  SHARED_PERFS_NAME="my_sharedpref"
        private const val  KEY_CITY="city"
        private var instance:SharedPref? = null
        fun getInstance(context: Context):SharedPref{
            if (instance==null){
                instance=SharedPref(context.applicationContext)
            }
            return  instance!!
        }

        }
    private val prefs:SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PERFS_NAME,Context.MODE_PRIVATE)

    }

    fun setValue(key:String,value:String){
        prefs.edit().putString(key,value)
    }

    fun getvalue(key:String): String? {
        return prefs.getString(key,null)
    }
    fun setValueOrNull(key:String?,vale:String?){
        if (key!=null && vale!=null){
            prefs.edit().putString(key,vale).apply()
        }


    }
    fun getValueOrNull(key:String?):String?{
        if (key!=null ){
            prefs.edit().putString(key,null).apply()
        }
        return null

    }
    //clear
    fun clearCityValue(){
        prefs.edit().remove(KEY_CITY).apply()
    }
}