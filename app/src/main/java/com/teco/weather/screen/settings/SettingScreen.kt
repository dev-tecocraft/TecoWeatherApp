package com.teco.weather.screen.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.teco.weather.R
import com.teco.weather.ui.theme.Poppins
import com.teco.weather.ui.theme.WeatherTheme
import com.teco.weather.ui.theme.backgroundColor
import com.teco.weather.ui.theme.lightBlack
import com.teco.weather.ui.theme.white
import com.teco.weather.util.contactUs
import com.teco.weather.util.gotoPlayStore
import com.teco.weather.util.gotoPrivacyPolicy
import com.teco.weather.util.shareApp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingScreen(navController: NavHostController) {
    val context = LocalContext.current
    WeatherTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                TopSection(onBackClick = {
                    navController.navigateUp()
                })
                Spacer(modifier = Modifier.height(15.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(), horizontalAlignment = Alignment.Start
                ) {
                    Box(
                        modifier = Modifier
                            .background(lightBlack, shape = RoundedCornerShape(5.dp))
                            .clip(shape = RoundedCornerShape(5.dp))
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(), horizontalAlignment = Alignment.Start
                        ) {
                            Spacer(modifier = Modifier.height(5.dp))
                            TextButton(
                                onClick = { context.gotoPlayStore() },
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "Rate Our App",
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
                                    fontSize = 14.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back), modifier = Modifier
                                        .height(IntrinsicSize.Min)
                                        .rotate(180f), contentDescription = ""
                                )

                            }
                            TextButton(
                                onClick = {context.gotoPrivacyPolicy() },
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "Privacy policy",
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
                                    fontSize = 14.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back), modifier = Modifier
                                        .height(IntrinsicSize.Min)
                                        .rotate(180f), contentDescription = ""
                                )
                            }
                            TextButton(
                                onClick = { context.contactUs("peterapps100@gmail.com")},
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "Contact us",
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
                                    fontSize = 14.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back), modifier = Modifier
                                        .height(IntrinsicSize.Min)
                                        .rotate(180f), contentDescription = ""
                                )
                            }
                            TextButton(
                                onClick = {context.shareApp() },
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = "Share",
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
                                    fontSize = 14.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_back), modifier = Modifier
                                        .height(IntrinsicSize.Min)
                                        .rotate(180f), contentDescription = ""
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun TopSection(onBackClick: () -> Unit = {}) {
    TopAppBar(backgroundColor = backgroundColor) {
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
                text = "Settings",
                textAlign = TextAlign.Start,
                color = white,
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false,
                    ),
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.03.sp,
                    fontSize = 18.sp,
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}