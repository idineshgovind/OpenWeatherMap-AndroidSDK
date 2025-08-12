package com.dineshdev.openweathermap.sdk.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Air pollution response from OpenWeatherMap API.
 */
@JsonClass(generateAdapter = true)
data class AirPollutionResponse(
    @Json(name = "coord") val coordinates: Coordinates,
    @Json(name = "list") val list: List<AirPollutionData>
)

/**
 * Air pollution data point.
 */
@JsonClass(generateAdapter = true)
data class AirPollutionData(
    @Json(name = "dt") val timestamp: Long,
    @Json(name = "main") val main: AirQualityIndex,
    @Json(name = "components") val components: AirComponents
)

/**
 * Air Quality Index.
 * 1 = Good, 2 = Fair, 3 = Moderate, 4 = Poor, 5 = Very Poor
 */
@JsonClass(generateAdapter = true)
data class AirQualityIndex(
    @Json(name = "aqi") val aqi: Int
) {
    val qualityName: String
        get() = when (aqi) {
            1 -> "Good"
            2 -> "Fair"
            3 -> "Moderate"
            4 -> "Poor"
            5 -> "Very Poor"
            else -> "Unknown"
        }
}

/**
 * Air pollution components concentration.
 * All values in μg/m³
 */
@JsonClass(generateAdapter = true)
data class AirComponents(
    @Json(name = "co") val carbonMonoxide: Double,        // CO
    @Json(name = "no") val nitrogenMonoxide: Double,      // NO
    @Json(name = "no2") val nitrogenDioxide: Double,      // NO2
    @Json(name = "o3") val ozone: Double,                 // O3
    @Json(name = "so2") val sulphurDioxide: Double,       // SO2
    @Json(name = "pm2_5") val pm2_5: Double,              // PM2.5
    @Json(name = "pm10") val pm10: Double,                // PM10
    @Json(name = "nh3") val ammonia: Double               // NH3
)

/**
 * Air pollution forecast response.
 */
@JsonClass(generateAdapter = true)
data class AirPollutionForecastResponse(
    @Json(name = "coord") val coordinates: Coordinates,
    @Json(name = "list") val list: List<AirPollutionData>
)

/**
 * Air pollution history response.
 */
@JsonClass(generateAdapter = true)
data class AirPollutionHistoryResponse(
    @Json(name = "coord") val coordinates: Coordinates,
    @Json(name = "list") val list: List<AirPollutionData>
)