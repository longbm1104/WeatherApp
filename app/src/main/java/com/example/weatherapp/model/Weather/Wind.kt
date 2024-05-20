package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed")
    var speed: Double
)
