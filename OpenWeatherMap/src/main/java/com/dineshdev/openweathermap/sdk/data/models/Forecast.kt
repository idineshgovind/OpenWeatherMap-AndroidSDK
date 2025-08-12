package com.dineshdev.openweathermap.sdk.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 5-day weather forecast response (3-hour intervals).
 */
@JsonClass(generateAdapter = true)
data class ForecastResponse(
    @Json(name = "cod") val code: String,
    @Json(name = "message") val message: Int,
    @Json(name = "cnt") val count: Int,
    @Json(name = "list") val list: List<ForecastItem>,
    @Json(name = "city") val city: City
)

/**
 * Individual forecast item (3-hour interval).
 */
@JsonClass(generateAdapter = true)
data class ForecastItem(
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "main") val main: MainWeatherData,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    @Json(name = "clouds") val clouds: Clouds,
    @Json(name = "wind") val wind: Wind,
    @Json(name = "visibility") val visibility: Int? = null,
    @Json(name = "pop") val probabilityOfPrecipitation: Double? = null, // 0-1
    @Json(name = "rain") val rain: Precipitation? = null,
    @Json(name = "snow") val snow: Precipitation? = null,
    @Json(name = "sys") val sys: ForecastSys,
    @Json(name = "dt_txt") val dateTimeText: String
)

/**
 * Forecast system information.
 */
@JsonClass(generateAdapter = true)
data class ForecastSys(
    @Json(name = "pod") val partOfDay: String // "d" for day, "n" for night
)

/**
 * City information for forecast.
 */
@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "coord") val coordinates: Coordinates,
    @Json(name = "country") val country: String,
    @Json(name = "population") val population: Int? = null,
    @Json(name = "timezone") val timezone: Int,
    @Json(name = "sunrise") val sunrise: Long,
    @Json(name = "sunset") val sunset: Long
)

/**
 * Daily forecast response (16 days).
 */
@JsonClass(generateAdapter = true)
data class DailyForecastResponse(
    @Json(name = "city") val city: City,
    @Json(name = "cod") val code: String,
    @Json(name = "message") val message: Double? = null,
    @Json(name = "cnt") val count: Int,
    @Json(name = "list") val list: List<DailyForecastItem>
)

/**
 * Daily forecast item.
 */
@JsonClass(generateAdapter = true)
data class DailyForecastItem(
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "sunrise") val sunrise: Long? = null,
    @Json(name = "sunset") val sunset: Long? = null,
    @Json(name = "temp") val temperature: DailyTemperature,
    @Json(name = "feels_like") val feelsLike: DailyFeelsLike? = null,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    @Json(name = "speed") val windSpeed: Double,
    @Json(name = "deg") val windDegrees: Int,
    @Json(name = "gust") val windGust: Double? = null,
    @Json(name = "clouds") val clouds: Int,
    @Json(name = "pop") val probabilityOfPrecipitation: Double? = null,
    @Json(name = "rain") val rain: Double? = null,
    @Json(name = "snow") val snow: Double? = null
)

/**
 * Daily temperature variations.
 */
@JsonClass(generateAdapter = true)
data class DailyTemperature(
    @Json(name = "day") val day: Double,
    @Json(name = "min") val min: Double,
    @Json(name = "max") val max: Double,
    @Json(name = "night") val night: Double,
    @Json(name = "eve") val evening: Double,
    @Json(name = "morn") val morning: Double
)

/**
 * Daily feels-like temperature variations.
 */
@JsonClass(generateAdapter = true)
data class DailyFeelsLike(
    @Json(name = "day") val day: Double,
    @Json(name = "night") val night: Double,
    @Json(name = "eve") val evening: Double,
    @Json(name = "morn") val morning: Double
)

/**
 * Hourly forecast response.
 */
@JsonClass(generateAdapter = true)
data class HourlyForecastResponse(
    @Json(name = "cod") val code: String,
    @Json(name = "message") val message: Int? = null,
    @Json(name = "cnt") val count: Int,
    @Json(name = "list") val list: List<HourlyForecastItem>,
    @Json(name = "city") val city: City? = null
)

/**
 * Hourly forecast item.
 */
@JsonClass(generateAdapter = true)
data class HourlyForecastItem(
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "main") val main: MainWeatherData,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    @Json(name = "clouds") val clouds: Clouds,
    @Json(name = "wind") val wind: Wind,
    @Json(name = "visibility") val visibility: Int? = null,
    @Json(name = "pop") val probabilityOfPrecipitation: Double? = null,
    @Json(name = "rain") val rain: Precipitation? = null,
    @Json(name = "snow") val snow: Precipitation? = null,
    @Json(name = "sys") val sys: ForecastSys? = null,
    @Json(name = "dt_txt") val dateTimeText: String? = null
)