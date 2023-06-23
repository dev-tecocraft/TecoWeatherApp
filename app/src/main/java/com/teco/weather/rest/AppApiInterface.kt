package com.teco.weather.rest

import com.teco.weather.model.DashBoardWeatherResponse
import com.teco.weather.model.SearchDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApiInterface {

    @GET("forecast")
    suspend fun getDashboardData(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("hourly") hourly: String,
        @Query("models") models: String,
        @Query("daily") daily: String,
        @Query("current_weather") current_weather: Boolean,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("timezone") timezone: String,
    ): DashBoardWeatherResponse

    @GET("search")
    suspend fun getCity(
        @Query("name") latitude: String,
    ): SearchDataResponse
}