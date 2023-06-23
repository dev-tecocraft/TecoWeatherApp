package com.teco.weather.navigation

sealed class Screen(val route: String) {
    object OnBoarding : Screen(route = "onboard_screen")
    object Dashboard : Screen(route = "dashboard_screen")
    object CityManagement : Screen(route = "city_management_screen")
    object SearchCity : Screen(route = "search_city_screen")
    object Settings : Screen(route = "setting_screen")

    fun withArgs(vararg args: Boolean): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}