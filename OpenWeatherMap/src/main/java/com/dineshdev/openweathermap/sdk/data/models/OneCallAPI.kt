package com.dineshdev.openweathermap.sdk.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * One Call API 3.0 response.
 * Provides current weather, minute forecast, hourly forecast, daily forecast, and weather alerts.
 */
@JsonClass(generateAdapter = true)
data class OneCallResponse(
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lon") val longitude: Double,
    @Json(name = "timezone") val timezone: String,
    @Json(name = "timezone_offset") val timezoneOffset: Int,
    @Json(name = "current") val current: CurrentWeather? = null,
    @Json(name = "minutely") val minutely: List<MinutelyWeather>? = null,
    @Json(name = "hourly") val hourly: List<HourlyWeather>? = null,
    @Json(name = "daily") val daily: List<DailyWeather>? = null,
    @Json(name = "alerts") val alerts: List<WeatherAlert>? = null
)

/**
 * Current weather data for One Call API.
 */
@JsonClass(generateAdapter = true)
data class CurrentWeather(
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "sunrise") val sunrise: Long? = null,
    @Json(name = "sunset") val sunset: Long? = null,
    @Json(name = "temp") val temperature: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "dew_point") val dewPoint: Double,
    @Json(name = "uvi") val uvIndex: Double,
    @Json(name = "clouds") val clouds: Int,
    @Json(name = "visibility") val visibility: Int,
    @Json(name = "wind_speed") val windSpeed: Double,
    @Json(name = "wind_deg") val windDegrees: Int,
    @Json(name = "wind_gust") val windGust: Double? = null,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    @Json(name = "rain") val rain: OneHourData? = null,
    @Json(name = "snow") val snow: OneHourData? = null
)

/**
 * Minutely precipitation forecast (1 minute intervals for 1 hour).
 */
@JsonClass(generateAdapter = true)
data class MinutelyWeather(
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "precipitation") val precipitation: Double
)

/**
 * Hourly weather forecast.
 */
@JsonClass(generateAdapter = true)
data class HourlyWeather(
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "temp") val temperature: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "dew_point") val dewPoint: Double,
    @Json(name = "uvi") val uvIndex: Double,
    @Json(name = "clouds") val clouds: Int,
    @Json(name = "visibility") val visibility: Int,
    @Json(name = "wind_speed") val windSpeed: Double,
    @Json(name = "wind_deg") val windDegrees: Int,
    @Json(name = "wind_gust") val windGust: Double? = null,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    @Json(name = "pop") val probabilityOfPrecipitation: Double,
    @Json(name = "rain") val rain: OneHourData? = null,
    @Json(name = "snow") val snow: OneHourData? = null
)

/**
 * Daily weather forecast.
 */
@JsonClass(generateAdapter = true)
data class DailyWeather(
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "sunrise") val sunrise: Long,
    @Json(name = "sunset") val sunset: Long,
    @Json(name = "moonrise") val moonrise: Long,
    @Json(name = "moonset") val moonset: Long,
    @Json(name = "moon_phase") val moonPhase: Double, // 0-1
    @Json(name = "temp") val temperature: DailyTemperature,
    @Json(name = "feels_like") val feelsLike: DailyFeelsLike,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "dew_point") val dewPoint: Double,
    @Json(name = "wind_speed") val windSpeed: Double,
    @Json(name = "wind_deg") val windDegrees: Int,
    @Json(name = "wind_gust") val windGust: Double? = null,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    @Json(name = "clouds") val clouds: Int,
    @Json(name = "pop") val probabilityOfPrecipitation: Double,
    @Json(name = "rain") val rain: Double? = null,
    @Json(name = "snow") val snow: Double? = null,
    @Json(name = "uvi") val uvIndex: Double
)

/**
 * Weather alert information.
 */
@JsonClass(generateAdapter = true)
data class WeatherAlert(
    @Json(name = "sender_name") val senderName: String,
    @Json(name = "event") val event: String,
    @Json(name = "start") val start: Long,
    @Json(name = "end") val end: Long,
    @Json(name = "description") val description: String,
    @Json(name = "tags") val tags: List<String>? = null
)

/**
 * One hour precipitation data.
 */
@JsonClass(generateAdapter = true)
data class OneHourData(
    @Json(name = "1h") val oneHour: Double
)

/**
 * Historical weather response.
 */
@JsonClass(generateAdapter = true)
data class HistoricalWeatherResponse(
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lon") val longitude: Double,
    @Json(name = "timezone") val timezone: String,
    @Json(name = "timezone_offset") val timezoneOffset: Int,
    @Json(name = "data") val data: List<HistoricalWeatherData>
)

/**
 * Historical weather data point.
 */
@JsonClass(generateAdapter = true)
data class HistoricalWeatherData(
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "sunrise") val sunrise: Long? = null,
    @Json(name = "sunset") val sunset: Long? = null,
    @Json(name = "temp") val temperature: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "pressure") val pressure: Int,
    @Json(name = "humidity") val humidity: Int,
    @Json(name = "dew_point") val dewPoint: Double,
    @Json(name = "uvi") val uvIndex: Double? = null,
    @Json(name = "clouds") val clouds: Int,
    @Json(name = "visibility") val visibility: Int? = null,
    @Json(name = "wind_speed") val windSpeed: Double,
    @Json(name = "wind_deg") val windDegrees: Int,
    @Json(name = "wind_gust") val windGust: Double? = null,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    @Json(name = "rain") val rain: OneHourData? = null,
    @Json(name = "snow") val snow: OneHourData? = null
)