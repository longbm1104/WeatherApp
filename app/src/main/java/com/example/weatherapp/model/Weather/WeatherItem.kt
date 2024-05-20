package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName

//TODO
// Create data class WeatherItem (Refer to API Response)
// Hint: Refer to Wind Data Class
data class WeatherItem (

//    @SerializedName("dt")
//    var dt: Long,
//
//    @SerializedName("temp")
//    var temp: String,
//
//    @SerializedName("sunrise")
//    var sunrise: Long,
//
//    @SerializedName("sunset")
//    var sunset: Long,
//
//    @SerializedName("wind_speed")
//    var windSpeed: String,
//
//    @SerializedName("pressure")
//    var pressure: String,
//
//    @SerializedName("humidity")
//    var humidity: String,
//
//    @SerializedName("weather")
//    var weather: List<Main> = ArrayList()
    @SerializedName("main")
    var main: String
)
