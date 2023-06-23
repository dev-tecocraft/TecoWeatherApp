package com.teco.weather.screen.dashboard

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.teco.weather.R
import com.teco.weather.model.DashBoardWeatherResponse
import com.teco.weather.model.HourlyDataModel
import com.teco.weather.model.ResultsItem
import com.teco.weather.navigation.Screen
import com.teco.weather.ui.theme.Nunito
import com.teco.weather.ui.theme.Poppins
import com.teco.weather.ui.theme.WeatherTheme
import com.teco.weather.ui.theme.backgroundColor
import com.teco.weather.ui.theme.dividerColor
import com.teco.weather.ui.theme.lightBlack
import com.teco.weather.ui.theme.lightGray
import com.teco.weather.ui.theme.white
import com.teco.weather.ui.theme.white_30
import com.teco.weather.util.dateFormatConvert
import com.teco.weather.util.getWeatherIcon
import com.teco.weather.util.getWeatherType


@ExperimentalFoundationApi
@Composable
fun DashBoardScreen(navController: NavHostController, viewModel: DashBoardViewModel = hiltViewModel()) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.callGetWeatherDataAPI()
                Log.d("TAG", "DashBoardScreen: ON_RESUME")
            } else if (event == Lifecycle.Event.ON_STOP) {
                Log.d("TAG", "DashBoardScreen: ON_STOP")
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var weatherDataResponse: DashBoardWeatherResponse?
    val hourlyData = arrayListOf<HourlyDataModel>()
    WeatherTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopSection(onCityClick = {
                    navController.navigate(Screen.CityManagement.route)
                }, onSettingClick = {
                    navController.navigate(Screen.Settings.route)

                },viewModel.city)
                when (val result = viewModel.response.value) {
                    is DashboardWeatherApiState.Success -> {
                        weatherDataResponse = result.data
                        Log.d("TAG", "DashBoardScreen: ${Gson().toJson(weatherDataResponse)}")
                        hourlyData.clear()

                        weatherDataResponse?.let { weatherResponse ->

                            weatherResponse.hourly.time.take(24).forEachIndexed { index, time ->
                                hourlyData.add(
                                    HourlyDataModel(
                                        time = time,
                                        temperature = weatherResponse.hourly.temperature2m[index].toString(),
                                        weatherCode = weatherResponse.hourly.weathercode[index].toString(),
                                    )
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                                    .weight(weight = 1f, fill = false)
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = "Today’s Report",
                                    textAlign = TextAlign.Start,
                                    color = white,
                                    style = TextStyle(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false,
                                        ),
                                    ),
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.03.sp,
                                    fontSize = 16.sp,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Box {
                                    Image(painterResource(id = R.drawable.ic_weather_gradient), modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds, contentDescription = "")
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Spacer(modifier = Modifier.height(14.dp))
                                        Image(
                                            painterResource(id = weatherResponse.currentWeather.weathercode.getWeatherIcon()), modifier = Modifier.height(120.dp), contentDescription = ""
                                        )

                                        Text(
                                            text = "${weatherResponse.currentWeather.temperature} ℃",
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            fontFamily = Nunito,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 50.sp,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                        )

                                        Text(
                                            text = weatherResponse.currentWeather.weathercode.getWeatherType(),
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            fontFamily = Nunito,
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 20.sp,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = weatherResponse.currentWeather.time.dateFormatConvert("dd MMM, yyyy", "yyyy-MM-dd'T'hh:mm"),
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            fontFamily = Nunito,
                                            fontWeight = FontWeight.SemiBold,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 12.sp,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Divider(modifier = Modifier.padding(12.dp, 0.dp), color = white_30, thickness = 1.dp)
                                        Spacer(modifier = Modifier.height(18.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(24.dp, 0.dp)
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painterResource(id = R.drawable.ic_wind), modifier = Modifier.height(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Text(
                                                    text = "${weatherResponse.currentWeather.windspeed} km/h",
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 12.sp,
                                                )
                                                Text(
                                                    text = "Wind",
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Normal,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 12.sp,
                                                )
                                            }

                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painterResource(id = R.drawable.ic_drop), modifier = Modifier.height(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Text(
                                                    text = weatherResponse.currentWeather.winddirection.toString(),
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 12.sp,
                                                )
                                                Text(
                                                    text = "Wind Direction",
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Normal,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 12.sp,
                                                )
                                            }

                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painterResource(id = R.drawable.ic_rain), modifier = Modifier.height(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Text(
                                                    text = "${weatherResponse.daily.precipitationProbabilityMax[0]}%",
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 12.sp,
                                                )
                                                Text(
                                                    text = "Precipitation",
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Normal,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 12.sp,
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(18.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Today", modifier = Modifier.align(Alignment.CenterStart), style = TextStyle(
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = false,
                                            ),
                                        ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 16.sp
                                    )

                                    Row(
                                        modifier = Modifier.align(Alignment.CenterEnd), verticalAlignment = Alignment.Bottom
                                    ) {
                                        Text(
                                            text = "${weatherResponse.daily.temperature2mMax[0]}℃/", modifier = Modifier, style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 16.sp
                                        )
                                        Text(
                                            text = "${weatherResponse.daily.temperature2mMin[0]}℃", modifier = Modifier, style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ), color = lightGray, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 15.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                LazyRow {
                                    items(items = hourlyData, itemContent = {
                                        Box(modifier = Modifier.padding(end = 10.dp)) {
                                            Column(
                                                modifier = Modifier
                                                    .background(lightBlack, shape = RoundedCornerShape(5.dp))
                                                    .clip(shape = RoundedCornerShape(5.dp)),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Spacer(
                                                    modifier = Modifier
                                                        .height(10.dp)
                                                        .width(80.dp)
                                                )
                                                Text(
                                                    text = it.time.dateFormatConvert("hha", "yyyy-MM-dd'T'hh:mm").uppercase(),
                                                    modifier = Modifier.fillMaxWidth(),
                                                    textAlign = TextAlign.Center,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Normal,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 12.sp,
                                                )
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Image(
                                                    painterResource(id = it.weatherCode.toInt().getWeatherIcon()), modifier = Modifier.height(30.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Text(
                                                    text = "${it.temperature} ℃",
                                                    textAlign = TextAlign.Center,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Normal,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 12.sp,
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                            }
                                        }

                                    })
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Daily Forecast", modifier = Modifier.align(Alignment.CenterStart), style = TextStyle(
                                            platformStyle = PlatformTextStyle(
                                                includeFontPadding = false,
                                            ),
                                        ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 16.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(lightBlack, shape = RoundedCornerShape(5.dp))
                                        .clip(shape = RoundedCornerShape(5.dp))
                                        .padding(18.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = "Today",
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 14.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                        )

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp, 0.dp)
                                        ) {
                                            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {

                                                Image(
                                                    painter = painterResource(id = weatherResponse.daily.weathercode[0].getWeatherIcon()), modifier = Modifier
                                                        .height(28.dp)
                                                        .width(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = weatherResponse.daily.weathercode[0].getWeatherType(),
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 14.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                    maxLines = 1,
                                                )

                                            }
                                        }

                                        Row(
                                            modifier = Modifier, verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMax[0]}/", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 14.sp
                                            )
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMin[0]}°", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = lightGray, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 13.sp
                                            )
                                        }
                                    }

                                    Divider(modifier = Modifier.padding(0.dp, 18.dp), color = dividerColor, thickness = 1.dp)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = weatherResponse.daily.time[1].dateFormatConvert("EEE","yyyy-MM-dd"),
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 14.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                        )

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp, 0.dp)
                                        ) {
                                            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {

                                                Image(
                                                    painter = painterResource(id = weatherResponse.daily.weathercode[1].getWeatherIcon()), modifier = Modifier
                                                        .height(28.dp)
                                                        .width(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = weatherResponse.daily.weathercode[1].getWeatherType(),
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 14.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                    maxLines = 1,
                                                )

                                            }
                                        }

                                        Row(
                                            modifier = Modifier, verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMax[1]}/", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 14.sp
                                            )
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMin[1]}°", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = lightGray, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 13.sp
                                            )
                                        }
                                    }

                                    Divider(modifier = Modifier.padding(0.dp, 18.dp), color = dividerColor, thickness = 1.dp)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = weatherResponse.daily.time[2].dateFormatConvert("EEE","yyyy-MM-dd"),
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 14.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                        )

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp, 0.dp)
                                        ) {
                                            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {

                                                Image(
                                                    painter = painterResource(id = weatherResponse.daily.weathercode[2].getWeatherIcon()), modifier = Modifier
                                                        .height(28.dp)
                                                        .width(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = weatherResponse.daily.weathercode[2].getWeatherType(),
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 14.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                    maxLines = 1,
                                                )

                                            }
                                        }

                                        Row(
                                            modifier = Modifier, verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMax[2]}/", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 14.sp
                                            )
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMin[2]}°", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = lightGray, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 13.sp
                                            )
                                        }
                                    }

                                    Divider(modifier = Modifier.padding(0.dp, 18.dp), color = dividerColor, thickness = 1.dp)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = weatherResponse.daily.time[3].dateFormatConvert("EEE","yyyy-MM-dd"),
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 14.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                        )

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp, 0.dp)
                                        ) {
                                            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {

                                                Image(
                                                    painter = painterResource(id = weatherResponse.daily.weathercode[3].getWeatherIcon()), modifier = Modifier
                                                        .height(28.dp)
                                                        .width(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = weatherResponse.daily.weathercode[3].getWeatherType(),
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 14.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                    maxLines = 1,
                                                )

                                            }
                                        }

                                        Row(
                                            modifier = Modifier, verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMax[3]}/", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 14.sp
                                            )
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMin[3]}°", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = lightGray, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 13.sp
                                            )
                                        }
                                    }

                                    Divider(modifier = Modifier.padding(0.dp, 18.dp), color = dividerColor, thickness = 1.dp)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = weatherResponse.daily.time[4].dateFormatConvert("EEE","yyyy-MM-dd"),
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 14.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                        )

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp, 0.dp)
                                        ) {
                                            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {

                                                Image(
                                                    painter = painterResource(id = weatherResponse.daily.weathercode[4].getWeatherIcon()), modifier = Modifier
                                                        .height(28.dp)
                                                        .width(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = weatherResponse.daily.weathercode[4].getWeatherType(),
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 14.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                    maxLines = 1,
                                                )

                                            }
                                        }

                                        Row(
                                            modifier = Modifier, verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMax[4]}/", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 14.sp
                                            )
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMin[4]}°", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = lightGray, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 13.sp
                                            )
                                        }
                                    }

                                    Divider(modifier = Modifier.padding(0.dp, 18.dp), color = dividerColor, thickness = 1.dp)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = weatherResponse.daily.time[5].dateFormatConvert("EEE","yyyy-MM-dd"),
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 14.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                        )

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp, 0.dp)
                                        ) {
                                            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {

                                                Image(
                                                    painter = painterResource(id = weatherResponse.daily.weathercode[5].getWeatherIcon()), modifier = Modifier
                                                        .height(28.dp)
                                                        .width(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = weatherResponse.daily.weathercode[5].getWeatherType(),
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 14.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                    maxLines = 1,
                                                )

                                            }
                                        }

                                        Row(
                                            modifier = Modifier, verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMax[5]}/", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 14.sp
                                            )
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMin[5]}°", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = lightGray, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 13.sp
                                            )
                                        }
                                    }

                                    Divider(modifier = Modifier.padding(0.dp, 18.dp), color = dividerColor, thickness = 1.dp)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            text = weatherResponse.daily.time[6].dateFormatConvert("EEE","yyyy-MM-dd"),
                                            textAlign = TextAlign.Start,
                                            color = white,
                                            style = TextStyle(
                                                platformStyle = PlatformTextStyle(
                                                    includeFontPadding = false,
                                                ),
                                            ),
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.03.sp,
                                            fontSize = 14.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                        )

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(8.dp, 0.dp)
                                        ) {
                                            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {

                                                Image(
                                                    painter = painterResource(id = weatherResponse.daily.weathercode[6].getWeatherIcon()), modifier = Modifier
                                                        .height(28.dp)
                                                        .width(28.dp), contentDescription = ""
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = weatherResponse.daily.weathercode[6].getWeatherType(),
                                                    textAlign = TextAlign.Start,
                                                    color = white,
                                                    style = TextStyle(
                                                        platformStyle = PlatformTextStyle(
                                                            includeFontPadding = false,
                                                        ),
                                                    ),
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Medium,
                                                    letterSpacing = 0.03.sp,
                                                    fontSize = 14.sp,
                                                    overflow = TextOverflow.Ellipsis,
                                                    maxLines = 1,
                                                )

                                            }
                                        }

                                        Row(
                                            modifier = Modifier, verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMax[6]}/", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = white, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 14.sp
                                            )
                                            Text(
                                                text = "${weatherResponse.daily.temperature2mMin[6]}°", modifier = Modifier, style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false,
                                                    ),
                                                ), color = lightGray, fontFamily = Poppins, fontWeight = FontWeight.Medium, letterSpacing = 0.03.sp, fontSize = 13.sp
                                            )
                                        }
                                    }
                                }

                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    is DashboardWeatherApiState.Failure -> {
                        Text(text = "${result.msg}")
                    }

                    DashboardWeatherApiState.Loading -> {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }

                    }

                    DashboardWeatherApiState.Empty -> {

                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun TopSection(onCityClick: () -> Unit = {}, onSettingClick: () -> Unit = {}, city: ResultsItem?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {


        IconButton(
            onClick = { },
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_setting), contentScale = ContentScale.Fit, contentDescription = "", alpha = 0f
            )
        }

        Box(
            modifier = Modifier.weight(1f)
        ) {
            Row(modifier = Modifier.align(Alignment.Center)) {

                TextButton(
                    onClick = onCityClick,
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location), modifier = Modifier.height(IntrinsicSize.Min), contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${city?.name},${city?.admin1}",
                        textAlign = TextAlign.Start,
                        color = white,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false,
                            ),
                        ),
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.03.sp,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            }
        }

        // Skip Button
        IconButton(
            onClick = onSettingClick,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_setting), contentScale = ContentScale.Fit, contentDescription = ""
            )
        }
    }
}