package com.teco.weather.screen.city_management

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.teco.weather.data.DataStoreRepository
import com.teco.weather.data.SharedPref
import com.teco.weather.model.ResultsItem
import com.teco.weather.rest.MainRepository
import com.teco.weather.screen.dashboard.DashboardWeatherApiState
import com.teco.weather.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityManagementViewModel @Inject constructor(
    private val repository: DataStoreRepository,
    private val sharedPref: SharedPref,
    private val mainRepository: MainRepository,
) : ViewModel() {

    val response: MutableState<CityListState> = mutableStateOf(CityListState.Empty)
    var cityList = listOf<ResultsItem>()

    fun getCityList(){
        response.value = CityListState.Loading


        val storedJsonString = sharedPref.getString("user_saved_city")
        val savedCityList : ArrayList<ResultsItem>? = Gson().fromJson(storedJsonString, Constants.listType)

        if (savedCityList.isNullOrEmpty()){
            response.value = CityListState.Empty
        } else {
            Log.d("TAG", "getCityList: ${Gson().toJson(savedCityList)}")
            cityList = savedCityList
            response.value = CityListState.Success(cityList)
        }
    }


    fun saveCity(cityItem : ResultsItem) {
        sharedPref.put("user_city",cityItem)
    }


}

sealed class CityListState {

    class Success(val data: List<ResultsItem>) : CityListState()
    class Failure(val msg: Throwable) : CityListState()
    object Loading : CityListState()
    object Empty : CityListState()

}