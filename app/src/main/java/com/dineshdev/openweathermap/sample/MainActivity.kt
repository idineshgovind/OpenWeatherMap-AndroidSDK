package com.dineshdev.openweathermap.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dineshdev.openweathermap.sample.ui.theme.OpenWeatherMapAndroidSDKTheme
import com.dineshdev.openweathermap.sample.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenWeatherMapAndroidSDKTheme {
                WeatherSampleApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSampleApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val currentDestination = currentBackStackEntry?.destination
                
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(stringResource(item.titleRes)) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "current_weather",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("current_weather") {
                CurrentWeatherScreen()
            }
            composable("forecast") {
                ForecastScreen()
            }
            composable("one_call") {
                OneCallScreen()
            }
            composable("air_pollution") {
                AirPollutionScreen()
            }
            composable("geocoding") {
                GeocodingScreen()
            }
            composable("weather_maps") {
                WeatherMapsScreen()
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val titleRes: Int,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = "current_weather",
        titleRes = R.string.nav_current_weather,
        icon = Icons.Default.WbSunny
    ),
    BottomNavItem(
        route = "forecast",
        titleRes = R.string.nav_forecast,
        icon = Icons.Default.CalendarToday
    ),
    BottomNavItem(
        route = "one_call",
        titleRes = R.string.nav_one_call,
        icon = Icons.Default.Api
    ),
    BottomNavItem(
        route = "air_pollution",
        titleRes = R.string.nav_air_pollution,
        icon = Icons.Default.Air
    ),
    BottomNavItem(
        route = "geocoding",
        titleRes = R.string.nav_geocoding,
        icon = Icons.Default.LocationOn
    ),
    BottomNavItem(
        route = "weather_maps",
        titleRes = R.string.nav_weather_maps,
        icon = Icons.Default.Map
    )
)