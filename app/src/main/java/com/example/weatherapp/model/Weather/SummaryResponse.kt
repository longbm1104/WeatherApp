package com.example.weatherapp.model.Weather

import com.google.gson.annotations.SerializedName


//TODO
// Create data class Sys (Refer to API Response)
// Hint: Refer to Wind Data Class
data class SummaryResponse (

    @SerializedName("temperature")
    val temperature: Temperature,
)
