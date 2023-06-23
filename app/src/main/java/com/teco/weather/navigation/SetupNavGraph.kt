package com.teco.weather.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.teco.weather.screen.city_management.CityManagementScreen
import com.teco.weather.screen.dashboard.DashBoardScreen
import com.teco.weather.screen.on_boarding.OnBoardingScreen
import com.teco.weather.screen.search_city.SearchCityScreen
import com.teco.weather.screen.settings.SettingScreen

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.OnBoarding.route) {
            OnBoardingScreen(navController = navController)
        }
        composable(route = Screen.Dashboard.route) {
            DashBoardScreen(navController = navController)
        }
        composable(route = Screen.CityManagement.route) {
            CityManagementScreen(navController = navController)
        }
        composable(route = "${Screen.SearchCity.route}/{fromOnBoarding}",
            arguments = listOf(
                navArgument("fromOnBoarding") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )) {

            SearchCityScreen(navController = navController, it.arguments?.getBoolean("fromOnBoarding")!!)
        }
        composable(route = Screen.Settings.route) {
            SettingScreen(navController = navController)
        }
    }
}