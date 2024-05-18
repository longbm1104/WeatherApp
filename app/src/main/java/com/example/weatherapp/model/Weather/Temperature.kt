package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName

data class Temperature (

    @SerializedName("max")
    val max: Double = 0.0,

    @SerializedName("min")
    val min: Double = 0.0
)