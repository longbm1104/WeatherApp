package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName


//TODO
// Create data class CurrentWeatherResponse (Refer to API Response)
// Hint: Refer to Wind Data Class
data class CurrentWeatherResponse (

//    @SerializedName("current")
//    var current: WeatherItem
    @SerializedName("coord")
    var coord: Coord,

    @SerializedName("weather")
    var weather: List<WeatherItem> = ArrayList(),

    @SerializedName("main")
    var main: Main,

    @SerializedName("wind")
    var wind: Wind,

    @SerializedName("dt")
    var dt: Long,

    @SerializedName("sys")
    var sys: Sys,

    @SerializedName("name")
    var name: String = "",
)