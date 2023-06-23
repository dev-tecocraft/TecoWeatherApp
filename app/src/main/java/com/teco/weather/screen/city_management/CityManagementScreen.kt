package com.teco.weather.screen.city_management

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.teco.weather.R
import com.teco.weather.navigation.Screen
import com.teco.weather.ui.theme.Poppins
import com.teco.weather.ui.theme.WeatherTheme
import com.teco.weather.ui.theme.backgroundColor
import com.teco.weather.ui.theme.lightBlack
import com.teco.weather.ui.theme.lightGray
import com.teco.weather.ui.theme.themeColor
import com.teco.weather.ui.theme.white

@ExperimentalFoundationApi
@Composable
fun CityManagementScreen(navController: NavHostController,viewModel: CityManagementViewModel = hiltViewModel()) {
    viewModel.getCityList()
    WeatherTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopSection(onBackClick = {
                    navController.navigateUp()
                })
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 20.dp)) {
                    when (val result = viewModel.response.value) {
                        is CityListState.Empty -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp), contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Oops, You Haven't Added City!",
                                        fontSize = 20.sp,
                                        fontFamily = Poppins,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.03.sp,
                                        color = white,
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Please add city\nby pressing + button below!",
                                        fontSize = 13.sp,
                                        fontFamily = Poppins,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        letterSpacing = 0.03.sp,
                                        color = lightGray,
                                    )
                                }
                            }
                        }
                        is CityListState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(trackColor = themeColor)
                            }
                        }
                        is CityListState.Success-> {
                            result.data.let { cityList ->
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(vertical = 8.dp)
                                ) {
                                    items(cityList) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(lightBlack, shape = RoundedCornerShape(5.dp))
                                                .clip(shape = RoundedCornerShape(5.dp))
                                                .fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .clickable {
                                                        viewModel.saveCity(it)
                                                        navController.navigateUp()
                                                    }
                                                    .padding(12.dp)
                                                    .fillMaxWidth(), horizontalAlignment = Alignment.Start
                                            ) {
                                                Text(
                                                    text = it.name.toString(),
                                                    fontSize = 14.sp,
                                                    fontFamily = Poppins,
                                                    fontWeight = FontWeight.Normal,
                                                    letterSpacing = 0.03.sp,
                                                    color = white,
                                                )
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Text(
                                                    modifier = Modifier,
                                                    text = "${it.admin1.toString()}, ${it.country.toString()}",
                                                    fontSize = 14.sp,
                                                    fontFamily = Poppins,
                                                    letterSpacing = 0.03.sp,
                                                    color = lightGray,
                                                    style = TextStyle.Default,
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }
                                }
                            }
                            Log.d("TAG", "CityManagementScreen: ${result.data}")
                        }
                        is CityListState.Failure -> {

                            Log.d("TAG", "CityManagementScreen: ${result.msg}")

                        }
                    }

                    FloatingActionButton(
                        onClick = {
                            navController.navigate(route = Screen.SearchCity.withArgs(false))
                        },
                        modifier = Modifier.align(Alignment.BottomEnd),
                        backgroundColor = themeColor,
                        contentColor = white
                    ) {
                        Icon(Icons.Filled.Add, "")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
    Log.d("TAG", "CityManagementScreen: ")
}

@ExperimentalFoundationApi
@Composable
fun TopSection(onBackClick: () -> Unit = {}) {
    TopAppBar (backgroundColor = backgroundColor){
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back), contentScale = ContentScale.Fit, contentDescription = ""
                )
            }

            Text(
                text = "City Management",
                textAlign = TextAlign.Start,
                color = white,
                modifier = Modifier.align(Alignment.Center),
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