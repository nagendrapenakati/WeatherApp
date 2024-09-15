package com.example.weatherapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constant
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.data.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherData = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherData: MutableLiveData<NetworkResponse<WeatherModel>> = _weatherData

    fun fetchWeather(city: String){
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(city, Constant.apikey)
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    weatherData?.let {
                        _weatherData.value = NetworkResponse.Success(it)
                    }
                    //Log.d("WeatherViewModel", "Weather data: $weatherData")
                }
                else {
                    _weatherData.value = NetworkResponse.Error("Error fetching weather data")
                    //Log.e("WeatherViewModel", "Error fetching weather data: ${response.code()}")
                }
            }
            catch (e: Exception) {
                _weatherData.value = NetworkResponse.Error("Error fetching weather data: ${e.message}")
                //Log.e("WeatherViewModel", "Error fetching weather data: ${e.message}")
            }
        }
    }
}