package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName

//TODO
// Create data class WeatherItem (Refer to API Response)
// Hint: Refer to Wind Data Class
data class WeatherItem (
    @SerializedName("main")
    var main: String
)
