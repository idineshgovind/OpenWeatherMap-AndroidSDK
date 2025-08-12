package com.dineshdev.openweathermap.sdk.config

/**
 * Configuration provider interface for OpenWeatherMap SDK.
 * Implement this interface to provide API key and other configuration options.
 */
interface OpenWeatherConfigProvider {
    /**
     * OpenWeatherMap API key.
     * Get your API key from: https://openweathermap.org/api
     */
    val apiKey: String
    
    /**
     * Base URL for API calls. Default is OpenWeatherMap API v2.5.
     * You can override this for testing or if OpenWeatherMap changes their API version.
     */
    val baseUrl: String
        get() = "https://api.openweathermap.org/"
    
    /**
     * Default units for temperature.
     * Options: "standard" (Kelvin), "metric" (Celsius), "imperial" (Fahrenheit)
     */
    val units: Units
        get() = Units.METRIC
    
    /**
     * Default language for API responses.
     * See: https://openweathermap.org/current#multi
     */
    val language: String
        get() = "en"
    
    /**
     * Enable debug logging for network calls.
     * Warning: This will log sensitive information including API keys.
     */
    val enableLogging: Boolean
        get() = false
    
    /**
     * Cache duration in minutes for API responses.
     * Set to 0 to disable caching.
     */
    val cacheExpirationMinutes: Int
        get() = 10
    
    /**
     * Network timeout in seconds.
     */
    val networkTimeoutSeconds: Long
        get() = 30L
    
    /**
     * Maximum number of retry attempts for failed requests.
     */
    val maxRetryAttempts: Int
        get() = 3
}

/**
 * Temperature units for OpenWeatherMap API.
 */
enum class Units(val value: String) {
    STANDARD("standard"),  // Kelvin
    METRIC("metric"),      // Celsius
    IMPERIAL("imperial")   // Fahrenheit
}

/**
 * Simple implementation of OpenWeatherConfigProvider for easy SDK setup.
 */
data class SimpleConfigProvider(
    override val apiKey: String,
    override val baseUrl: String = "https://api.openweathermap.org/",
    override val units: Units = Units.METRIC,
    override val language: String = "en",
    override val enableLogging: Boolean = false,
    override val cacheExpirationMinutes: Int = 10,
    override val networkTimeoutSeconds: Long = 30L,
    override val maxRetryAttempts: Int = 3
) : OpenWeatherConfigProvider