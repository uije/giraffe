package com.zettafantasy.giraffe.common

import androidx.navigation.NavController
import com.zettafantasy.giraffe.MainFragmentDirections
import com.zettafantasy.giraffe.R

fun NavController.navigateRecord() {
    if (this.currentDestination?.id == R.id.main) {
        if (Preferences.shownRecordIntro) {
            this.navigate(MainFragmentDirections.actionGoGoodOrBad())
        } else {
            this.navigate(MainFragmentDirections.actionGoIntroDesc())
        }
    }
}