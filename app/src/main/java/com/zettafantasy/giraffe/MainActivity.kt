package com.zettafantasy.giraffe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.zettafantasy.giraffe.common.DestinationScreen
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.common.navigate

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val model: MainViewModel by viewModels()
        model.destinationScreen = getDestinationScreen(intent)

        navController = findNavController(R.id.my_nav_host_fragment)
        navController.setGraph(R.navigation.navigation, getStartDestinationArgs())
        setupActionBarWithNavController(navController, AppBarConfiguration(navController.graph))
    }

    private fun getStartDestinationArgs(): Bundle? {
        return Bundle().apply {
            putSerializable(
                GiraffeConstant.EXTRA_KEY_SCREEN_DESTINATION,
                getDestinationScreen(intent)
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onPause() {
        Log.d(javaClass.simpleName, "onPause")
        super.onPause()
        Preferences.lastUsedTime = System.currentTimeMillis()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        getDestinationScreen(intent)?.navigate(navController)
    }

    private fun getDestinationScreen(intent: Intent?) =
        intent?.extras?.getSerializable(GiraffeConstant.EXTRA_KEY_SCREEN_DESTINATION) as DestinationScreen?
}