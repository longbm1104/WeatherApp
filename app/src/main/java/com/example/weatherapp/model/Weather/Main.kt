package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName

//TODO
// Create data class Main (Refer to API Response)
// Hint: Refer to Wind Data Class

data class Main (

//    @SerializedName("main")
//    val main: String = ""
    @SerializedName("temp")
    var temp: Double,

    @SerializedName("temp_min")
    var tempMin: Double,

    @SerializedName("temp_max")
    var tempMax: Double,

    @SerializedName("pressure")
    var pressure: String,

    @SerializedName("humidity")
    var humidity: String,
)