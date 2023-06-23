package com.teco.weather.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.teco.weather.R
import com.teco.weather.util.Constants.PRIVACY_POLICY
import java.text.SimpleDateFormat
import java.util.Locale

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

inline fun <reified T : Any> Activity.launchActivity(
    isNeedToFinish: Boolean = false,
    isFinishAffinity: Boolean = false,
    noinline modify: Intent.() -> Unit = {}
) {
    val intent = Intent(this, T::class.java)
    intent.modify()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    if (isNeedToFinish) {
        if (isFinishAffinity)
            finishAffinity()
        else
            finish()
    }
}

fun String.dateFormatConvert(changeFormat: String, alreadyFormat: String): String {
    val parser = SimpleDateFormat(alreadyFormat, Locale.getDefault())
    val formatter = SimpleDateFormat(changeFormat, Locale.getDefault())
    return formatter.format(parser.parse(this)!!)
}

fun Int.getWeatherType(): String {
    return when (this) {
        0 -> "Clear Sky"
        1 -> "Mainly Clear"
        2 -> "Partly Cloudy"
        3 -> "Overcast"
        45 -> "Fog"
        48 -> "Depositing Rime Fog"
        51 -> "Light Drizzle"
        53 -> "Moderate Drizzle"
        55 -> "Dense Drizzle"
        56 -> "Light Freezing Drizzle"
        57 -> "Dense Freezing Drizzle"
        61 -> "Slight Rain"
        63 -> "Moderate Rain"
        65 -> "Heavy Rain"
        66 -> "Slight Freezing Rain"
        67 -> "Heavy Freezing Rain"
        71 -> "Slight Snow Fall"
        73 -> "Moderate Snow Fall"
        75 -> "Heavy Snow Fall"
        77 -> "Snow Grains"
        80 -> "Slight Rain Showers"
        81 -> "Moderate Rain Showers"
        82 -> "Violent Rain Showers"
        85 -> "Slight Snow Showers"
        86 -> "Heavy Snow Showers"
        95 -> "Slight Thunderstorm"
        96 -> "Moderate Thunderstorm"
        99 -> "Heavy Thunderstorm"
        else -> "Clear Sky"
    }

}

fun Int.getWeatherIcon(): Int {
    return when (this) {
        0, 1, 2 -> R.drawable.ic_sun_cloud
        3 -> R.drawable.ic_cloud
        45, 48 -> R.drawable.ic_fog_cloud
        51, 61, 80 -> R.drawable.ic_slow_rain
        53, 63, 81 -> R.drawable.ic_moderate_rain
        55, 65, 82 -> R.drawable.ic_heavy_rain
        56, 66, 85 -> R.drawable.ic_low_freezing
        57, 67, 86 -> R.drawable.ic_heavy_freezing
        71, 77 -> R.drawable.ic_light_snow_fall
        73 -> R.drawable.ic_moderate_snow_fall
        75 -> R.drawable.ic_heavy_snow_fall
        95 -> R.drawable.ic_thunderstorm_light
        96 -> R.drawable.ic_thunderstorm_slight_hail
        99 -> R.drawable.ic_thunderstorm_heavu_hail
        else -> R.drawable.ic_sun_cloud
    }
}


fun Context.gotoPlayStore() {
    try {
        startActivity(
            Intent(
                "android.intent.action.VIEW",
                Uri.parse("market://details?id=$packageName")
            )
        )
    } catch (unused: ActivityNotFoundException) {
        startActivity(
            Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun Context.gotoPrivacyPolicy() {
    startActivity(Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(PRIVACY_POLICY)
    })
}

fun Context.shareApp() {
    try {
        val intent = Intent("android.intent.action.SEND")
        intent.type = "text/plain"
        intent.putExtra("android.intent.extra.SUBJECT", resources.getString(R.string.app_name))
        intent.putExtra(
            "android.intent.extra.TEXT",
            "\nLet me recommend you this application\n\nhttps://play.google.com/store/apps/details?id=$packageName\n"
        )
        startActivity(Intent.createChooser(intent, "choose one"))
    } catch (unused: Exception) {
        unused.printStackTrace()
    }
}

fun Context.contactUs(contactMail: String) {
    val uriText = "mailto:$contactMail" +
            "?subject=" + "Contact Us" +
            "&body=" + " "
    val intent = Intent(
        Intent.ACTION_SENDTO
    ).apply {
        data = Uri.parse(uriText)
    }
    startActivity(Intent.createChooser(intent, "Choose an Email client :"))
}