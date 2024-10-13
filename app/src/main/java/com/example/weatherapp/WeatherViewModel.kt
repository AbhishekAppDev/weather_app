package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constant
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherModel
import com.example.weatherapp.api.networkResponse
import kotlinx.coroutines.launch
import javax.security.auth.login.LoginException

class WeatherViewModel : ViewModel() {
    private val weatherApi = RetrofitInstance .weatherApi
    val _weatherResponse = MutableLiveData<networkResponse<WeatherModel>>()
   fun  getData (cityName :String){
       _weatherResponse.value = networkResponse.Loading
       viewModelScope.launch {

           try {
               val response = weatherApi.getWeather(Constant.apiKey,cityName)
               if(response.isSuccessful){
                   response.body()?.let {
                       _weatherResponse.value = networkResponse.Success(it)
                   }
               }else{
                   _weatherResponse.value = networkResponse.Error("Failed to Load Data")


               }

           }
           catch (e : Exception){
               _weatherResponse.value = networkResponse.Error("Error Is Shown")

           }
       }

   }
}