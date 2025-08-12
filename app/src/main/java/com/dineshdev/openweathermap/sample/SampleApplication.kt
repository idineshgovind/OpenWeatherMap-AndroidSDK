package com.dineshdev.openweathermap.sample

import android.app.Application
import com.dineshdev.openweathermap.sdk.OpenWeatherMapSDK
import com.dineshdev.openweathermap.sdk.config.SimpleConfigProvider
import timber.log.Timber

/**
 * Sample application class that initializes the OpenWeatherMap SDK.
 * 
 * This demonstrates how to properly initialize the SDK in an Android application.
 */
class SampleApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Initialize OpenWeatherMap SDK
        initializeOpenWeatherSDK()
    }
    
    private fun initializeOpenWeatherSDK() {
        try {
            val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
            
            if (apiKey.isBlank()) {
                Timber.e("OpenWeatherMap API key is missing! Please add OPEN_WEATHER_API_KEY to local.properties")
                return
            }
            
            // Create configuration provider
            val configProvider = SimpleConfigProvider(
                apiKey = apiKey,
                units = com.dineshdev.openweathermap.sdk.config.Units.METRIC,
                language = "en",
                enableLogging = BuildConfig.DEBUG,
                cacheExpirationMinutes = 10,
                networkTimeoutSeconds = 30L,
                maxRetryAttempts = 3
            )
            
            // Initialize SDK
            OpenWeatherMapSDK.initialize(this, configProvider)
            
            Timber.d("OpenWeatherMap SDK initialized successfully")
            
        } catch (e: Exception) {
            Timber.e(e, "Failed to initialize OpenWeatherMap SDK")
        }
    }
}