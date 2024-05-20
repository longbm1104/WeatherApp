package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country")
    var country: String,

    @SerializedName("sunrise")
    var sunrise: Long,

    @SerializedName("sunset")
    var sunset: Long
)
