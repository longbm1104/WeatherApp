package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName


//TODO
// Create data class CurrentWeatherResponse (Refer to API Response)
// Hint: Refer to Wind Data Class
data class CurrentWeatherResponse (

    @SerializedName("current")
    var current: WeatherItem


)