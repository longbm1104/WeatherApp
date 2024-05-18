package com.example.weatherapp

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("cod")
    val cod: Int = 0,

    @SerializedName("message")
    val message: String = ""
)
