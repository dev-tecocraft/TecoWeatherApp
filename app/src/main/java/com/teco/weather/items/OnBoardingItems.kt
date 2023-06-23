package com.teco.weather.items

import com.teco.weather.R

class OnBoardingItems(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object{
        fun getData(): List<OnBoardingItems>{
            return listOf(
                OnBoardingItems(R.drawable.ic_intro_1, R.string.onBoardingTitle1, R.string.onBoardingText1),
                OnBoardingItems(R.drawable.ic_intro_2, R.string.onBoardingTitle2, R.string.onBoardingText2),
                OnBoardingItems(R.drawable.ic_intro_3, R.string.onBoardingTitle3, R.string.onBoardingText3)
            )
        }
    }
}