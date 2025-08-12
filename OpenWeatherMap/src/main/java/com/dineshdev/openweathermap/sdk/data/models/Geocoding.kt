package com.dineshdev.openweathermap.sdk.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Geocoding location result.
 * Used for both direct and zip code geocoding.
 */
@JsonClass(generateAdapter = true)
data class GeocodingLocation(
    @Json(name = "name") val name: String,
    @Json(name = "local_names") val localNames: Map<String, String>? = null,
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lon") val longitude: Double,
    @Json(name = "country") val country: String,
    @Json(name = "state") val state: String? = null,
    @Json(name = "zip") val zipCode: String? = null
)

/**
 * Reverse geocoding result.
 * Returns location information from coordinates.
 */
@JsonClass(generateAdapter = true)
data class ReverseGeocodingLocation(
    @Json(name = "name") val name: String,
    @Json(name = "local_names") val localNames: Map<String, String>? = null,
    @Json(name = "lat") val latitude: Double,
    @Json(name = "lon") val longitude: Double,
    @Json(name = "country") val country: String,
    @Json(name = "state") val state: String? = null
)