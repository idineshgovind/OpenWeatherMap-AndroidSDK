package com.dineshdev.openweathermap.sdk.extensions

import com.dineshdev.openweathermap.sdk.data.models.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Extension functions for weather data models.
 */

/**
 * Convert temperature based on unit preference.
 */
fun Double.toTemperatureString(unit: String = "metric"): String {
    return when (unit) {
        "imperial" -> "${this.roundToInt()}°F"
        "metric" -> "${this.roundToInt()}°C"
        else -> "${this.roundToInt()}K"
    }
}

/**
 * Convert Unix timestamp to formatted date string.
 */
fun Long.toFormattedDate(pattern: String = "MMM dd, yyyy HH:mm"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this * 1000))
}

/**
 * Convert Unix timestamp to time string.
 */
fun Long.toTimeString(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this * 1000))
}

/**
 * Extension for CurrentWeatherResponse to get formatted temperature.
 */
fun CurrentWeatherResponse.getFormattedTemperature(unit: String = "metric"): String {
    return main.temperature.toTemperatureString(unit)
}

/**
 * Extension for CurrentWeatherResponse to get weather description.
 */
fun CurrentWeatherResponse.getDescription(): String {
    return weather.firstOrNull()?.description?.capitalize(Locale.getDefault()) ?: "Unknown"
}

/**
 * Extension for Wind to get formatted wind speed.
 */
fun Wind.getFormattedSpeed(unit: String = "metric"): String {
    return when (unit) {
        "imperial" -> "${speed.roundToInt()} mph"
        "metric" -> "${speed.roundToInt()} m/s"
        else -> "${speed.roundToInt()} m/s"
    }
}

/**
 * Extension for MainWeatherData to get formatted humidity.
 */
fun MainWeatherData.getFormattedHumidity(): String {
    return "$humidity%"
}

/**
 * Extension for MainWeatherData to get formatted pressure.
 */
fun MainWeatherData.getFormattedPressure(): String {
    return "$pressure hPa"
}

/**
 * Extension for Clouds to get formatted cloudiness.
 */
fun Clouds.getFormattedCloudiness(): String {
    return "$cloudiness%"
}

/**
 * Extension for SystemInfo to get formatted sunrise time.
 */
fun SystemInfo.getFormattedSunrise(): String {
    return sunrise?.toTimeString() ?: "N/A"
}

/**
 * Extension for SystemInfo to get formatted sunset time.
 */
fun SystemInfo.getFormattedSunset(): String {
    return sunset?.toTimeString() ?: "N/A"
}

/**
 * Extension for ForecastItem to get day of week.
 */
fun ForecastItem.getDayOfWeek(): String {
    return SimpleDateFormat("EEEE", Locale.getDefault()).format(Date(timestamp * 1000))
}

/**
 * Extension for AirQualityIndex to get color representation.
 */
fun AirQualityIndex.getColorCode(): String {
    return when (aqi) {
        1 -> "#00E400" // Good - Green
        2 -> "#FFFF00" // Fair - Yellow
        3 -> "#FF7E00" // Moderate - Orange
        4 -> "#FF0000" // Poor - Red
        5 -> "#8F3F97" // Very Poor - Purple
        else -> "#808080" // Unknown - Gray
    }
}

/**
 * Extension for Result to handle success and error cases.
 */
inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

/**
 * Extension for Result to handle error cases.
 */
inline fun <T> Result<T>.onError(action: (exception: Exception) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(exception)
    }
    return this
}

/**
 * Extension for Result to handle loading state.
 */
inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) {
        action()
    }
    return this
}

/**
 * Extension to get data or null from Result.
 */
fun <T> Result<T>.getOrNull(): T? {
    return if (this is Result.Success) data else null
}

/**
 * Extension to get data or default value from Result.
 */
fun <T> Result<T>.getOrDefault(default: T): T {
    return if (this is Result.Success) data else default
}

/**
 * Extension for DailyTemperature to get temperature range.
 */
fun DailyTemperature.getTemperatureRange(unit: String = "metric"): String {
    return "${min.toTemperatureString(unit)} - ${max.toTemperatureString(unit)}"
}

/**
 * Extension for WeatherDescription to check if it's raining.
 */
fun List<WeatherDescription>.isRaining(): Boolean {
    return any { it.main.equals("Rain", ignoreCase = true) }
}

/**
 * Extension for WeatherDescription to check if it's snowing.
 */
fun List<WeatherDescription>.isSnowing(): Boolean {
    return any { it.main.equals("Snow", ignoreCase = true) }
}

/**
 * Extension for WeatherDescription to check if it's clear.
 */
fun List<WeatherDescription>.isClear(): Boolean {
    return any { it.main.equals("Clear", ignoreCase = true) }
}

/**
 * Extension for WeatherDescription to check if it's cloudy.
 */
fun List<WeatherDescription>.isCloudy(): Boolean {
    return any { it.main.equals("Clouds", ignoreCase = true) }
}