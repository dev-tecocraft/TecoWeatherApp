package com.teco.weather.screen.dashboard

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teco.weather.data.DataStoreRepository
import com.teco.weather.data.SharedPref
import com.teco.weather.model.DashBoardWeatherResponse
import com.teco.weather.model.ResultsItem
import com.teco.weather.rest.MainRepository
import com.teco.weather.util.Constants.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val repository: DataStoreRepository,
    private val sharedPref: SharedPref,
    private val mainRepository: MainRepository,
) : ViewModel() {

    val response: MutableState<DashboardWeatherApiState> = mutableStateOf(DashboardWeatherApiState.Empty)
    var city: ResultsItem? = null

    fun callGetWeatherDataAPI() {
        city = sharedPref.get<ResultsItem>("user_city")
        val cal = Calendar.getInstance()

        val startDate = dateFormat.format(cal.time)
        cal.add(DAY_OF_MONTH, 6)
        val endDate = dateFormat.format(cal.time)


        Log.d("TAG", "callGetWeatherDataAPI: $startDate")
        Log.d("TAG", "callGetWeatherDataAPI: $endDate")

        getWeatherData(
            latitude = city?.latitude.toString(),
            longitude = city?.longitude.toString(),
            start_date = startDate,
            end_date = endDate,
        )
    }

    private fun getWeatherData(
        latitude: String,
        longitude: String,
        hourly: String = "temperature_2m,relativehumidity_2m,precipitation_probability,weathercode,windspeed_10m",
        models: String = "best_match",
        daily: String = "weathercode,temperature_2m_max,temperature_2m_min,precipitation_probability_max",
        current_weather: Boolean = true,
        start_date: String,
        end_date: String,
        timezone: String = "auto",
    ) = viewModelScope.launch {
        mainRepository.getDashBoardWeatherData(latitude, longitude, hourly, models, daily, current_weather, start_date, end_date, timezone)
            .onStart {
                response.value = DashboardWeatherApiState.Loading
            }.catch {
                response.value = DashboardWeatherApiState.Failure(it)
            }.collect {
                response.value = DashboardWeatherApiState.Success(it)
            }
    }

}

sealed class DashboardWeatherApiState {

    class Success(val data: DashBoardWeatherResponse) : DashboardWeatherApiState()
    class Failure(val msg: Throwable) : DashboardWeatherApiState()
    object Loading : DashboardWeatherApiState()
    object Empty : DashboardWeatherApiState()

}