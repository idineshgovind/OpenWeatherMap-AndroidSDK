package com.dineshdev.openweathermap.sdk.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Sealed class representing API call results.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Base exception for OpenWeatherMap API errors.
 */
sealed class OpenWeatherException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * Network-related error (no connection, timeout, etc.).
 */
class NetworkException(
    message: String = "Network error occurred",
    cause: Throwable? = null
) : OpenWeatherException(message, cause)

/**
 * API error response from OpenWeatherMap.
 */
class ApiException(
    message: String,
    val code: Int,
    val errorBody: String? = null,
    cause: Throwable? = null
) : OpenWeatherException("API Error ($code): $message", cause)

/**
 * Rate limit exceeded error.
 */
class RateLimitException(
    message: String = "Rate limit exceeded",
    val retryAfter: Long? = null, // Seconds until retry
    val limit: Int? = null,
    val remaining: Int? = null,
    val reset: Long? = null // Unix timestamp
) : OpenWeatherException(
    if (retryAfter != null) "$message. Retry after $retryAfter seconds" else message
)

/**
 * Invalid API key error.
 */
class InvalidApiKeyException(
    message: String = "Invalid API key"
) : OpenWeatherException(message)

/**
 * Invalid request parameters error.
 */
class InvalidRequestException(
    message: String = "Invalid request parameters"
) : OpenWeatherException(message)

/**
 * Weather map tile parameters.
 */
@Parcelize
data class MapTileParams(
    val layer: MapLayer,
    val z: Int,  // Zoom level (1-10)
    val x: Int,  // X tile coordinate
    val y: Int   // Y tile coordinate
) : Parcelable

/**
 * Available map layers for weather tiles.
 * 
 * Supported layers:
 * - CLOUDS_NEW: Cloud coverage
 * - PRECIPITATION_NEW: Precipitation intensity
 * - PRESSURE_NEW: Sea level pressure
 * - WIND_NEW: Wind speed
 * - TEMP_NEW: Temperature
 */
enum class MapLayer(val value: String, val description: String) {
    CLOUDS_NEW("clouds_new", "Cloud coverage"),
    PRECIPITATION_NEW("precipitation_new", "Precipitation intensity"),
    PRESSURE_NEW("pressure_new", "Sea level pressure"),
    WIND_NEW("wind_new", "Wind speed"),
    TEMP_NEW("temp_new", "Temperature");
    
    companion object {
        fun fromValue(value: String): MapLayer? {
            return values().find { it.value == value }
        }
    }
}

/**
 * Query parameters for weather requests.
 * Supports metric, imperial, and standard units.
 */
@Parcelize
data class WeatherQuery(
    val cityName: String? = null,
    val cityId: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val zipCode: String? = null,
    val countryCode: String? = null,
    val stateCode: String? = null,
    val units: String? = null, // "metric", "imperial", or "standard"
    val language: String? = null // ISO 639-1 code (en, es, fr, etc.)
) : Parcelable {
    init {
        require(
            listOfNotNull(cityName, cityId, 
                if (latitude != null && longitude != null) true else null,
                zipCode).size == 1
        ) {
            "Exactly one location parameter must be specified"
        }
    }
}

/**
 * Rate limit information from API headers.
 */
@Parcelize
data class RateLimitInfo(
    val limit: Int,        // Calls per minute allowed
    val remaining: Int,    // Calls remaining in current window
    val reset: Long       // Unix timestamp when limit resets
) : Parcelable {
    
    /**
     * Check if rate limit is exceeded.
     */
    fun isExceeded(): Boolean = remaining <= 0
    
    /**
     * Get seconds until rate limit reset.
     */
    fun getSecondsUntilReset(): Long {
        val now = System.currentTimeMillis() / 1000
        return (reset - now).coerceAtLeast(0)
    }
}

/**
 * Response wrapper with rate limit info.
 */
data class ApiResponse<T>(
    val data: T,
    val rateLimitInfo: RateLimitInfo? = null
)