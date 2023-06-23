package com.teco.weather.rest

import com.teco.weather.model.DashBoardWeatherResponse
import com.teco.weather.model.SearchDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class MainRepository
@Inject
constructor(private val apiService: AppApiInterface, @Named("GEO_CODING") private val geoService: AppApiInterface) {

    fun getDashBoardWeatherData(
        latitude: String,
        longitude: String,
        hourly: String,
        models: String,
        daily: String,
        current_weather: Boolean,
        start_date: String,
        end_date: String,
        timezone: String
    ): Flow<DashBoardWeatherResponse> = flow {
        emit(apiService.getDashboardData(latitude, longitude, hourly, models, daily, current_weather, start_date, end_date, timezone))
    }.flowOn(Dispatchers.IO)

    fun getSearchedCity(name: String): Flow<SearchDataResponse> = flow {
        emit(geoService.getCity(name))
    }.flowOn(Dispatchers.IO)

}