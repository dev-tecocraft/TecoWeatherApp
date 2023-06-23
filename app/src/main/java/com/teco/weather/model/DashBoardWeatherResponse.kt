package com.teco.weather.model

import com.google.gson.annotations.SerializedName

data class DashBoardWeatherResponse(

    @SerializedName("elevation") val elevation: Float,

    @SerializedName("hourly_units") val hourlyUnits: HourlyUnits,

    @SerializedName("generationtime_ms") val generationtimeMs: Double,

    @SerializedName("timezone_abbreviation") val timezoneAbbreviation: String,

    @SerializedName("daily_units") val dailyUnits: DailyUnits,

    @SerializedName("timezone") val timezone: String,

    @SerializedName("latitude") val latitude: Double,

    @SerializedName("daily") val daily: Daily,

    @SerializedName("utc_offset_seconds") val utcOffsetSeconds: Int,

    @SerializedName("hourly") val hourly: Hourly,

    @SerializedName("current_weather") val currentWeather: CurrentWeather,

    @SerializedName("longitude") val longitude: Double
)

data class CurrentWeather(

    @SerializedName("weathercode") val weathercode: Int,

    @SerializedName("temperature") val temperature: Double,

    @SerializedName("windspeed") val windspeed: Double,

    @SerializedName("is_day") val isDay: Int,

    @SerializedName("time") val time: String,

    @SerializedName("winddirection") val winddirection: Double
)

data class DailyUnits(

    @SerializedName("weathercode") val weathercode: String,

    @SerializedName("temperature_2m_max") val temperature2mMax: String,

    @SerializedName("temperature_2m_min") val temperature2mMin: String,

    @SerializedName("time") val time: String
)

data class Daily(

    @SerializedName("weathercode") val weathercode: List<Int>,
    @SerializedName("precipitation_probability_max") val precipitationProbabilityMax: List<Int>,

    @SerializedName("temperature_2m_max") val temperature2mMax: List<Double>,

    @SerializedName("temperature_2m_min") val temperature2mMin: List<Double>,

    @SerializedName("time") val time: List<String>
)

data class Hourly(

    @SerializedName("temperature_2m") val temperature2m: List<Double>,

    @SerializedName("relativehumidity_2m") val relativehumidity2m: List<Int>,

    @SerializedName("weathercode") val weathercode: List<Int>,

    @SerializedName("precipitation_probability") val precipitationProbability: List<Int>,

    @SerializedName("windspeed_10m") val windspeed10m: List<Double>,

    @SerializedName("time") val time: List<String>
)

data class HourlyUnits(

    @SerializedName("temperature_2m") val temperature2m: String,

    @SerializedName("relativehumidity_2m") val relativehumidity2m: String,

    @SerializedName("weathercode") val weathercode: String,

    @SerializedName("precipitation_probability") val precipitationProbability: String,

    @SerializedName("windspeed_10m") val windspeed10m: String,

    @SerializedName("time") val time: String
)
