package com.teco.weather.data

import android.content.Context
import com.google.gson.Gson

class SharedPref(context: Context) {

    companion object {
        private const val prefMode = Context.MODE_PRIVATE
        private const val prefName = "WeatherAppPref"
    }

    private val sharedPreferences = context.getSharedPreferences(
        prefName,
        prefMode
    )

    fun putString(key: String, value: String) =
        sharedPreferences.edit().putString(key, value).apply()

    fun getString(key: String) = sharedPreferences.getString(key, null)

    fun putInt(key: String, value: Int) =
        sharedPreferences.edit().putInt(key, value).apply()

    fun getInt(key: String) = sharedPreferences.getInt(key, -1)

    fun putBoolean(key: String, value: Boolean) =
        sharedPreferences.edit().putBoolean(key, value).apply()

    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)

    inline fun <reified T> put(key: String, value: T) {
        val jsonString = Gson().toJson(value, T::class.java)
        putString(key, jsonString)
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }

    inline fun <reified T> get(key: String): T? {
        val jsonString = getString(key) ?: return null
        return Gson().fromJson(jsonString, T::class.java)
    }

}