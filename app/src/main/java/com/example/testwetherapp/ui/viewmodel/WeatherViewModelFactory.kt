package com.example.testwetherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testwetherapp.ui.repository.WeatherRepository

class WeatherViewModelFactory (private val repositoryApi: WeatherRepository): ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(repositoryApi) as T
    }
}