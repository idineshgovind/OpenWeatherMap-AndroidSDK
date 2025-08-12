package com.dineshdev.openweathermap.sdk.data.models

/**
 * Sealed class representing API call results.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Weather map tile parameters.
 */
data class MapTileParams(
    val layer: MapLayer,
    val z: Int,  // Zoom level
    val x: Int,  // X tile coordinate
    val y: Int   // Y tile coordinate
)

/**
 * Available map layers for weather tiles.
 */
enum class MapLayer(val value: String) {
    CLOUDS_NEW("clouds_new"),
    PRECIPITATION_NEW("precipitation_new"),
    PRESSURE_NEW("pressure_new"),
    WIND_NEW("wind_new"),
    TEMP_NEW("temp_new")
}

/**
 * Query parameters for weather requests.
 */
data class WeatherQuery(
    val cityName: String? = null,
    val cityId: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val zipCode: String? = null,
    val countryCode: String? = null,
    val stateCode: String? = null,
    val units: String? = null,
    val language: String? = null
) {
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
 * Exception for OpenWeatherMap API errors.
 */
class OpenWeatherException(
    message: String,
    val code: Int? = null,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * Rate limit information.
 */
data class RateLimitInfo(
    val limit: Int,
    val remaining: Int,
    val reset: Long // Unix timestamp
)