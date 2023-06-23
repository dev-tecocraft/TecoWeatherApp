package com.teco.weather.activity.main

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teco.weather.data.DataStoreRepository
import com.teco.weather.navigation.Screen
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repository: DataStoreRepository
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf(Screen.Dashboard.route)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch {
            repository.readOnBoardingState().collect { completed ->
                if (completed) {
                    repository.readOnCityAddedState().collect {
                        if (it)
                            _startDestination.value = Screen.Dashboard.route
                        else
                            _startDestination.value = Screen.SearchCity.withArgs(true)

                        Handler(Looper.getMainLooper()).postDelayed({
                            _isLoading.value = false
                        }, 500)
                    }
                } else {
                    _startDestination.value = Screen.OnBoarding.route
                    Handler(Looper.getMainLooper()).postDelayed({
                        _isLoading.value = false
                    }, 500)
                }


            }
        }
    }
}