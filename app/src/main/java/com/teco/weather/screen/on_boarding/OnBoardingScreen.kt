package com.teco.weather.screen.on_boarding

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.teco.weather.R
import com.teco.weather.items.OnBoardingItems
import com.teco.weather.navigation.Screen
import com.teco.weather.ui.theme.Poppins
import com.teco.weather.ui.theme.WeatherTheme
import com.teco.weather.ui.theme.activeColor
import com.teco.weather.ui.theme.backgroundColor
import com.teco.weather.ui.theme.inactiveColor
import com.teco.weather.ui.theme.textGrayColor
import com.teco.weather.ui.theme.themeColor
import com.teco.weather.ui.theme.white
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun OnBoardingScreen(navController: NavHostController, viewModel: OnBoardingViewModel = hiltViewModel()) {
    val items = OnBoardingItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
       3
    }

    WeatherTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopSection(
                    onBackClick = {
                        if (pageState.currentPage + 1 > 1) scope.launch {
                            pageState.animateScrollToPage(pageState.currentPage - 1)
                        }
                    },
                    onSkipClick = {
                        scope.launch {
                            viewModel.saveOnBoardingState(true)
                            navController.popBackStack()
                        }
                    }, pageState
                )
                Spacer(modifier = Modifier.height(50.dp))
                HorizontalPager(
                    state = pageState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    // Our page content
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OnBoardingItem(items = items[page])
                    }
                }

                Spacer(modifier = Modifier.height(45.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        HorizontalPagerIndicator(
                            modifier = Modifier,
                            pageCount = pageState.pageCount,
                            pagerState = pageState,
                            activeColor = activeColor,
                            inactiveColor = inactiveColor,
                            indicatorHeight = 10.dp,
                            indicatorWidth = 10.dp,
                            indicatorShape = CircleShape,
                            spacing = 7.dp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                BottomSection(pageState) {
                    if (pageState.currentPage + 1 < items.size) scope.launch {
                        pageState.animateScrollToPage(pageState.currentPage + 1)
                    } else {
                        scope.launch {
                            viewModel.saveOnBoardingState(true)
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun OnBoardingItem(items: OnBoardingItems) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {

        Image(
            painter = painterResource(id = items.image),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(start = 50.dp, end = 50.dp)
                .height(230.dp),
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(id = items.title),
            textAlign = TextAlign.Center,
            color = white,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.03.sp,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(id = items.desc),
            textAlign = TextAlign.Center,
            color = textGrayColor,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.03.sp,
            fontSize = 13.sp
        )

    }
}

@ExperimentalFoundationApi
@Composable
fun BottomSection(pageState: PagerState, onButtonClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onButtonClick, modifier = Modifier
                .height(50.dp)
                .width(150.dp), shape = RoundedCornerShape(100.dp), colors = ButtonDefaults.buttonColors(contentColor = white, containerColor = themeColor)
        ) {
            Text(
                text = if (pageState.currentPage == 2) "Get Started" else "Next",
                color = white,
                fontFamily = Poppins,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                ),
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.03.sp,
                fontSize = 16.sp
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun TopSection(onBackClick: () -> Unit = {}, onSkipClick: () -> Unit = {}, pageState: PagerState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // Back button
        if (pageState.currentPage > 0) {
            IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(painterResource(id = R.drawable.ic_back), tint = white, contentDescription = null)
            }
        }

        // Skip Button
        TextButton(
            onClick = onSkipClick,
            modifier = Modifier.align(Alignment.CenterEnd),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Skip",
                modifier = Modifier,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                ),
                color = white,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.03.sp,
                fontSize = 14.sp
            )
        }
    }
}