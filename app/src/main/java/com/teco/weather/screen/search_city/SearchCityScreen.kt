package com.teco.weather.screen.search_city

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.google.gson.Gson
import com.teco.weather.R
import com.teco.weather.model.SearchDataResponse
import com.teco.weather.navigation.Screen
import com.teco.weather.ui.theme.Poppins
import com.teco.weather.ui.theme.WeatherTheme
import com.teco.weather.ui.theme.backgroundColor
import com.teco.weather.ui.theme.lightBlack
import com.teco.weather.ui.theme.lightGray
import com.teco.weather.ui.theme.themeColor
import com.teco.weather.ui.theme.white

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalFoundationApi
@Composable
fun SearchCityScreen(navController: NavHostController, fromOnBoarding: Boolean = true, viewModel: SearchCityViewModel = hiltViewModel()) {
    var searchDataResponse: SearchDataResponse?
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }
    val searchedItems = remember { mutableStateListOf<String>() }
    var active by remember { mutableStateOf(false) }
    var closeIconActive by remember { mutableStateOf(false) }

    WeatherTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopSection(onBackClick = {
                    navController.navigateUp()
                }, fromOnBoarding)
                Spacer(modifier = Modifier.height(15.dp))
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SearchBar(query = searchText, onQueryChange = {
                        searchText = it
                    }, onSearch = {
                        active = false
                        closeIconActive = false
                        if (searchText.isNotEmpty()) {
                            if (searchedItems.contains(searchText).not())
                                searchedItems.add(searchText)
                            viewModel.callSearchCityAPI(searchText)
                        }
                    }, colors = SearchBarDefaults.colors(lightBlack), active = active, onActiveChange = {
                        active = false
                        closeIconActive = it
                    }, leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_search), modifier = Modifier, tint = lightGray, contentDescription = "Search Icon"
                        )
                    }, trailingIcon = {
                        if (closeIconActive) {
                            Icon(modifier = Modifier.clickable {
                                if (searchText.isNotEmpty()) {
                                    searchText = ""
                                }
                            }, imageVector = Icons.Default.Close, contentDescription = "Close Icon", tint = white)
                        }
                    }, placeholder = {
                        Text(
                            text = "search city",
                            fontSize = 12.sp,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false,
                                ),
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Normal,
                                letterSpacing = 0.03.sp,
                                color = lightGray,
                            ),
                        )
                    }, modifier = Modifier) {
                        searchedItems.forEach {
                            Row(modifier = Modifier
                                .padding(all = 14.dp)
                                .clickable {
                                    searchText = it
                                }, verticalAlignment = CenterVertically
                            ) {
                                Icon(modifier = Modifier.padding(end = 10.dp), imageVector = Icons.Default.History, contentDescription = "History", tint = lightGray)
                                Text(
                                    text = it,
                                    style = TextStyle(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false,
                                        ),
                                        fontFamily = Poppins,
                                        fontWeight = FontWeight.Normal,
                                        letterSpacing = 0.03.sp,
                                        color = lightGray,
                                        fontSize = 12.sp
                                    ),
                                )
                            }
                        }
                    }
                }

                when (val result = viewModel.response.value) {
                    is SearchCityApiState.Stable -> {}
                    is SearchCityApiState.Success -> {
                        searchDataResponse = result.data
                        searchDataResponse?.let { response ->
                            response.results?.let { cityList ->
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)
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
                                                        if (fromOnBoarding) {
                                                            navController.popBackStack()
                                                            navController.navigate(Screen.Dashboard.route)
                                                        } else {
                                                            navController.navigateUp()
                                                        }
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
                            Log.d("SearchCityScreen", "SearchCityScreen: ${Gson().toJson(response)}")
                        }
                    }

                    is SearchCityApiState.Failure -> {
                        Toast.makeText(context, "${result.msg}", Toast.LENGTH_SHORT).show()
                    }

                    SearchCityApiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                            CircularProgressIndicator(trackColor = themeColor)
                        }
                    }

                    SearchCityApiState.Empty -> {
                        if (searchText.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp), contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Oops, No result found!",
                                        fontSize = 20.sp,
                                        fontFamily = Poppins,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.03.sp,
                                        color = white,
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Search for something else to\nfind what you’re looking for!",
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
                    }
                }

            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun TopSection(onBackClick: () -> Unit = {}, fromOnBoarding: Boolean = false) {
    TopAppBar(backgroundColor = backgroundColor) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (!fromOnBoarding) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back), contentScale = ContentScale.Fit, contentDescription = ""
                    )
                }
            }

            Text(
                text = "Add Location",
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
//    Row(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//
//
//    }
}