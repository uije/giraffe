package com.zettafantasy.giraffe.common

import androidx.navigation.NavController

enum class DestinationScreen {
    RECORD
}

fun DestinationScreen?.navigate(navController: NavController) {
    when (this) {
        DestinationScreen.RECORD -> navController.navigateRecord()
    }
}
