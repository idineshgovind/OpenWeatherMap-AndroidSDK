package com.dineshdev.openweathermap.sdk.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Current weather response from OpenWeatherMap API.
 */
@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(
    @Json(name = "coord") val coordinates: Coordinates,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    @Json(name = "base") val base: String,
    @Json(name = "main") val main: MainWeatherData,
    @Json(name = "visibility") val visibility: Int? = null,
    @Json(name = "wind") val wind: Wind,
    @Json(name = "clouds") val clouds: Clouds,
    @Json(name = "rain") val rain: Precipitation? = null,
    @Json(name = "snow") val snow: Precipitation? = null,
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "sys") val system: SystemInfo,
    @Json(name = "timezone") val timezone: Int,
    @Json(name = "id") val cityId: Int,
    @Json(name = "name") val cityName: String,
    @Json(name = "cod") val code: Int
)

/**
 * Geographic coordinates.
 */
@JsonClass(generateAdapter = true)
data class Coordinates(
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lon") val longitude: Double
)

/**
 * Weather condition description.
 */
@JsonClass(generateAdapter = true)
data class WeatherDescription(
    @Json(name = "id") val id: Int,
    @Json(name = "main") val main: String,
    @Json(name = "description") val description: String,
    @Json(name = "icon") val icon: String
) {
    val iconUrl: String
        get() = "https://openweathermap.org/img/wn/$icon@2x.png"
}

/**
 * Main weather parameters.
 */
@JsonClass(generateAdapter = true)
data class MainWeatherData(
    @Json(name = "temp") val temperature: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "temp_min") val temperatureMin: Double,
    @Json(name = "temp_max") val temperatureMax: Double,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "sea_level") val seaLevelPressure: Int? = null,
    @Json(name = "grnd_level") val groundLevelPressure: Int? = null
)

/**
 * Wind data.
 */
@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed") val speed: Double,
    @Json(name = "deg") val degrees: Int? = null,
    @Json(name = "gust") val gust: Double? = null
)

/**
 * Cloud coverage data.
 */
@JsonClass(generateAdapter = true)
data class Clouds(
    @Json(name = "all") val cloudiness: Int // Percentage
)

/**
 * Precipitation data (rain or snow).
 */
@JsonClass(generateAdapter = true)
data class Precipitation(
    @Json(name = "1h") val oneHour: Double? = null,
    @Json(name = "3h") val threeHours: Double? = null
)

/**
 * System information.
 */
@JsonClass(generateAdapter = true)
data class SystemInfo(
    @Json(name = "type") val type: Int? = null,
    @Json(name = "id") val id: Int? = null,
    @Json(name = "country") val country: String? = null,
    @Json(name = "sunrise") val sunrise: Long? = null,
    @Json(name = "sunset") val sunset: Long? = null
)

/**
 * Bulk weather response for multiple cities.
 */
@JsonClass(generateAdapter = true)
data class BulkWeatherResponse(
    @Json(name = "cnt") val count: Int,
    @Json(name = "list") val list: List<CurrentWeatherResponse>
)