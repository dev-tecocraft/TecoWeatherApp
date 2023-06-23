package com.teco.weather.screen.search_city

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.teco.weather.data.DataStoreRepository
import com.teco.weather.data.SharedPref
import com.teco.weather.model.ResultsItem
import com.teco.weather.model.SearchDataResponse
import com.teco.weather.rest.MainRepository
import com.teco.weather.util.Constants.listType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import javax.inject.Inject


@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val repository: DataStoreRepository,
    private val sharedPref: SharedPref,
    private val mainRepository: MainRepository,
) : ViewModel() {

    val response: MutableState<SearchCityApiState> = mutableStateOf(SearchCityApiState.Stable)

    fun callSearchCityAPI(search : String) = viewModelScope.launch {
        mainRepository.getSearchedCity(search)
            .onStart {
                response.value = SearchCityApiState.Loading
            }.catch {
                response.value = SearchCityApiState.Failure(it)
            }.collect {
                if (it.results.isNullOrEmpty()){
                    response.value = SearchCityApiState.Empty
                } else {
                    response.value = SearchCityApiState.Success(it)
                }
            }
    }

    fun saveCity(cityItem : ResultsItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOnCityAddedState(completed = true)
        }
        val cityList = ArrayList<ResultsItem>()
        val storedJsonString = sharedPref.getString("user_saved_city")
        val savedCityList : ArrayList<ResultsItem>? = Gson().fromJson(storedJsonString,listType)

        Log.d("Search City", "saveCity: $savedCityList")

        savedCityList?.let {
            cityList.addAll(it)
        }

        cityList.add(cityItem)


        Log.d("Search City", "cityList: $cityList")

        val jsonString = Gson().toJson(cityList, listType)
        sharedPref.put("user_city",cityItem)
        sharedPref.putString("user_saved_city",jsonString)

    }
}

sealed class SearchCityApiState {

    class Success(val data: SearchDataResponse) : SearchCityApiState()
    class Failure(val msg: Throwable) : SearchCityApiState()
    object Loading : SearchCityApiState()
    object Empty : SearchCityApiState()
    object Stable : SearchCityApiState()

}