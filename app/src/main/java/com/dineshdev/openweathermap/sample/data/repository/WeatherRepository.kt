package com.dineshdev.openweathermap.sample.data.repository

import com.dineshdev.openweathermap.sdk.OpenWeatherMapSDK
import com.dineshdev.openweathermap.sdk.data.models.*
import timber.log.Timber

/**
 * Repository class that wraps the OpenWeatherMap SDK for use in the sample app.
 * This demonstrates how to use the SDK in a clean architecture pattern.
 */
class WeatherRepository private constructor() {
    
    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null
        
        fun getInstance(): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WeatherRepository().also { INSTANCE = it }
            }
        }
    }
    
    private val sdk: OpenWeatherMapSDK
        get() = OpenWeatherMapSDK.getInstance()
    
    // ============== Current Weather ==============
    
    suspend fun getCurrentWeatherByCityName(
        cityName: String,
        countryCode: String? = null,
        stateCode: String? = null
    ): Result<CurrentWeatherResponse> {
        return try {
            Timber.d("Getting current weather for city: $cityName")
            sdk.getCurrentWeatherByCityName(cityName, countryCode, stateCode)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get current weather for city: $cityName")
            Result.Error(ApiException("Failed to get weather data", -1, cause = e))
        }
    }
    
    suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<CurrentWeatherResponse> {
        return try {
            Timber.d("Getting current weather for coordinates: $latitude, $longitude")
            sdk.getCurrentWeatherByCoordinates(latitude, longitude)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get current weather for coordinates")
            Result.Error(ApiException("Failed to get weather data", -1, cause = e))
        }
    }
    
    suspend fun getCurrentWeatherByZipCode(
        zipCode: String,
        countryCode: String? = null
    ): Result<CurrentWeatherResponse> {
        return try {
            Timber.d("Getting current weather for ZIP code: $zipCode")
            sdk.getCurrentWeatherByZipCode(zipCode, countryCode)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get current weather for ZIP code: $zipCode")
            Result.Error(ApiException("Failed to get weather data", -1, cause = e))
        }
    }
    
    suspend fun getCurrentWeatherByCityId(cityId: Int): Result<CurrentWeatherResponse> {
        return try {
            Timber.d("Getting current weather for city ID: $cityId")
            sdk.getCurrentWeatherByCityId(cityId)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get current weather for city ID: $cityId")
            Result.Error(ApiException("Failed to get weather data", -1, cause = e))
        }
    }
    
    // ============== Forecast ==============
    
    suspend fun getForecastByCityName(
        cityName: String,
        countryCode: String? = null,
        count: Int? = null
    ): Result<ForecastResponse> {
        return try {
            Timber.d("Getting forecast for city: $cityName")
            sdk.getForecastByCityName(cityName, countryCode, count)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get forecast for city: $cityName")
            Result.Error(ApiException("Failed to get forecast data", -1, cause = e))
        }
    }
    
    suspend fun getForecastByCoordinates(
        latitude: Double,
        longitude: Double,
        count: Int? = null
    ): Result<ForecastResponse> {
        return try {
            Timber.d("Getting forecast for coordinates: $latitude, $longitude")
            sdk.getForecastByCoordinates(latitude, longitude, count)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get forecast for coordinates")
            Result.Error(ApiException("Failed to get forecast data", -1, cause = e))
        }
    }
    
    suspend fun getDailyForecastByCityName(
        cityName: String,
        days: Int? = null
    ): Result<DailyForecastResponse> {
        return try {
            Timber.d("Getting daily forecast for city: $cityName")
            sdk.getDailyForecastByCityName(cityName, days)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get daily forecast for city: $cityName")
            Result.Error(ApiException("Failed to get forecast data", -1, cause = e))
        }
    }
    
    suspend fun getDailyForecastByCoordinates(
        latitude: Double,
        longitude: Double,
        days: Int? = null
    ): Result<DailyForecastResponse> {
        return try {
            Timber.d("Getting daily forecast for coordinates: $latitude, $longitude")
            sdk.getDailyForecastByCoordinates(latitude, longitude, days)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get daily forecast for coordinates")
            Result.Error(ApiException("Failed to get forecast data", -1, cause = e))
        }
    }
    
    // ============== One Call API ==============
    
    suspend fun getOneCallData(
        latitude: Double,
        longitude: Double,
        exclude: List<String>? = null
    ): Result<OneCallResponse> {
        return try {
            Timber.d("Getting One Call data for coordinates: $latitude, $longitude")
            sdk.getOneCallData(latitude, longitude, exclude)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get One Call data")
            Result.Error(ApiException("Failed to get weather data", -1, cause = e))
        }
    }
    
    suspend fun getHistoricalWeather(
        latitude: Double,
        longitude: Double,
        timestamp: Long
    ): Result<HistoricalWeatherResponse> {
        return try {
            Timber.d("Getting historical weather for coordinates: $latitude, $longitude")
            sdk.getHistoricalWeather(latitude, longitude, timestamp)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get historical weather")
            Result.Error(ApiException("Failed to get weather data", -1, cause = e))
        }
    }
    
    // ============== Air Pollution ==============
    
    suspend fun getCurrentAirPollution(
        latitude: Double,
        longitude: Double
    ): Result<AirPollutionResponse> {
        return try {
            Timber.d("Getting current air pollution for coordinates: $latitude, $longitude")
            sdk.getCurrentAirPollution(latitude, longitude)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get air pollution data")
            Result.Error(ApiException("Failed to get air pollution data", -1, cause = e))
        }
    }
    
    suspend fun getAirPollutionForecast(
        latitude: Double,
        longitude: Double
    ): Result<AirPollutionForecastResponse> {
        return try {
            Timber.d("Getting air pollution forecast for coordinates: $latitude, $longitude")
            sdk.getAirPollutionForecast(latitude, longitude)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get air pollution forecast")
            Result.Error(ApiException("Failed to get air pollution data", -1, cause = e))
        }
    }
    
    suspend fun getAirPollutionHistory(
        latitude: Double,
        longitude: Double,
        start: Long,
        end: Long
    ): Result<AirPollutionHistoryResponse> {
        return try {
            Timber.d("Getting air pollution history for coordinates: $latitude, $longitude")
            sdk.getAirPollutionHistory(latitude, longitude, start, end)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get air pollution history")
            Result.Error(ApiException("Failed to get air pollution data", -1, cause = e))
        }
    }
    
    // ============== Geocoding ==============
    
    suspend fun getCoordinatesByLocationName(
        locationName: String,
        limit: Int? = null
    ): Result<List<GeocodingLocation>> {
        return try {
            Timber.d("Getting coordinates for location: $locationName")
            sdk.getCoordinatesByLocationName(locationName, limit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get coordinates for location: $locationName")
            Result.Error(ApiException("Failed to get location data", -1, cause = e))
        }
    }
    
    suspend fun getCoordinatesByZipCode(zipCode: String): Result<GeocodingLocation> {
        return try {
            Timber.d("Getting coordinates for ZIP code: $zipCode")
            sdk.getCoordinatesByZipCode(zipCode)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get coordinates for ZIP code: $zipCode")
            Result.Error(ApiException("Failed to get location data", -1, cause = e))
        }
    }
    
    suspend fun getLocationNameByCoordinates(
        latitude: Double,
        longitude: Double,
        limit: Int? = null
    ): Result<List<ReverseGeocodingLocation>> {
        return try {
            Timber.d("Getting location name for coordinates: $latitude, $longitude")
            sdk.getLocationNameByCoordinates(latitude, longitude, limit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get location name for coordinates")
            Result.Error(ApiException("Failed to get location data", -1, cause = e))
        }
    }
    
    // ============== Weather Maps ==============
    
    suspend fun getWeatherMapTile(
        layer: MapLayer,
        z: Int,
        x: Int,
        y: Int
    ): Result<ByteArray> {
        return try {
            Timber.d("Getting weather map tile: $layer, z=$z, x=$x, y=$y")
            sdk.getWeatherMapTile(layer, z, x, y)
        } catch (e: Exception) {
            Timber.e(e, "Failed to get weather map tile")
            Result.Error(ApiException("Failed to get map data", -1, cause = e))
        }
    }
    
    // ============== Utility ==============
    
    fun clearCache() {
        try {
            sdk.clearCache()
            Timber.d("Cache cleared successfully")
        } catch (e: Exception) {
            Timber.e(e, "Failed to clear cache")
        }
    }
    
    fun getCacheSize(): Long {
        return try {
            sdk.getCacheSize()
        } catch (e: Exception) {
            Timber.e(e, "Failed to get cache size")
            0L
        }
    }
    
    fun getRateLimitStatus(): String {
        return try {
            sdk.getRateLimitStatus()
        } catch (e: Exception) {
            Timber.e(e, "Failed to get rate limit status")
            "Unknown"
        }
    }
}