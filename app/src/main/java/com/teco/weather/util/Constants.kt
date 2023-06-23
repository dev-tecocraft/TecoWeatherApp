package com.teco.weather.util

import com.google.gson.reflect.TypeToken
import com.teco.weather.model.ResultsItem
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Locale

object Constants {

    const val API_BASE_URL: String = "https://api.open-meteo.com/v1/"
    const val GEO_CODING_API_BASE_URL: String = "https://geocoding-api.open-meteo.com/v1/"

    val listType: Type = object : TypeToken<ArrayList<ResultsItem>?>() {}.type
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    const val PRIVACY_POLICY = "https://sites.google.com/view/teco-weather-app/privacy-policy"

}