package com.dineshdev.openweathermap.sdk.repository

import com.dineshdev.openweathermap.sdk.api.WeatherApiService
import com.dineshdev.openweathermap.sdk.config.OpenWeatherConfigProvider
import com.dineshdev.openweathermap.sdk.data.models.*
import com.dineshdev.openweathermap.sdk.utils.CacheManager
import com.dineshdev.openweathermap.sdk.utils.NetworkUtils
import com.dineshdev.openweathermap.sdk.utils.RateLimitManager
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import kotlin.math.pow

/**
 * Repository class handling API calls with error handling, retries, and caching.
 */
class WeatherRepository(
    private val apiService: WeatherApiService,
    private val config: OpenWeatherConfigProvider,
    private val cacheManager: CacheManager
) {
    
    // ============== Current Weather ==============
    
    /**
     * Get current weather with automatic query type detection.
     */
    suspend fun getCurrentWeather(query: WeatherQuery): Result<CurrentWeatherResponse> {
        val cacheKey = "current_weather_${query.hashCode()}"
        
        // Check cache first
        cacheManager.get(cacheKey, CurrentWeatherResponse::class.java)?.let { cached ->
            Timber.d("Returning cached current weather")
            return Result.Success(cached)
        }
        
        return executeWithRetry {
            when {
                query.cityName != null -> {
                    val cityQuery = if (query.stateCode != null && query.countryCode != null) {
                        "${query.cityName},${query.stateCode},${query.countryCode}"
                    } else if (query.countryCode != null) {
                        "${query.cityName},${query.countryCode}"
                    } else {
                        query.cityName
                    }
                    apiService.getCurrentWeatherByCityName(
                        cityQuery,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language
                    )
                }
                query.cityId != null -> {
                    apiService.getCurrentWeatherByCityId(
                        query.cityId,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language
                    )
                }
                query.latitude != null && query.longitude != null -> {
                    apiService.getCurrentWeatherByCoordinates(
                        query.latitude,
                        query.longitude,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language
                    )
                }
                query.zipCode != null -> {
                    val zipQuery = if (query.countryCode != null) {
                        "${query.zipCode},${query.countryCode}"
                    } else {
                        query.zipCode
                    }
                    apiService.getCurrentWeatherByZipCode(
                        zipQuery,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language
                    )
                }
                else -> throw IllegalArgumentException("Invalid query parameters")
            }
        }.also { result ->
            if (result is Result.Success) {
                cacheManager.put(cacheKey, result.data, CurrentWeatherResponse::class.java, config.cacheExpirationMinutes)
            }
        }
    }
    
    /**
     * Get current weather for multiple cities within a rectangle.
     */
    suspend fun getCurrentWeatherInRectangle(
        bbox: String,
        units: String? = null,
        language: String? = null
    ): Result<BulkWeatherResponse> {
        return executeWithRetry {
            apiService.getCurrentWeatherInRectangle(
                bbox,
                config.apiKey,
                units ?: config.units.value,
                language ?: config.language
            )
        }
    }
    
    /**
     * Get current weather for multiple cities in a circle.
     */
    suspend fun getCurrentWeatherInCircle(
        latitude: Double,
        longitude: Double,
        count: Int? = null,
        units: String? = null,
        language: String? = null
    ): Result<BulkWeatherResponse> {
        return executeWithRetry {
            apiService.getCurrentWeatherInCircle(
                latitude,
                longitude,
                count,
                config.apiKey,
                units ?: config.units.value,
                language ?: config.language
            )
        }
    }
    
    // ============== Forecast ==============
    
    /**
     * Get 5-day forecast (3-hour intervals).
     */
    suspend fun getForecast(
        query: WeatherQuery,
        count: Int? = null
    ): Result<ForecastResponse> {
        val cacheKey = "forecast_${query.hashCode()}_$count"
        
        cacheManager.get(cacheKey, ForecastResponse::class.java)?.let { cached ->
            Timber.d("Returning cached forecast")
            return Result.Success(cached)
        }
        
        return executeWithRetry {
            when {
                query.cityName != null -> {
                    val cityQuery = buildCityQuery(query)
                    apiService.getForecastByCityName(
                        cityQuery,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language,
                        count
                    )
                }
                query.cityId != null -> {
                    apiService.getForecastByCityId(
                        query.cityId,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language,
                        count
                    )
                }
                query.latitude != null && query.longitude != null -> {
                    apiService.getForecastByCoordinates(
                        query.latitude,
                        query.longitude,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language,
                        count
                    )
                }
                query.zipCode != null -> {
                    val zipQuery = buildZipQuery(query)
                    apiService.getForecastByZipCode(
                        zipQuery,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language,
                        count
                    )
                }
                else -> throw IllegalArgumentException("Invalid query parameters")
            }
        }.also { result ->
            if (result is Result.Success) {
                cacheManager.put(cacheKey, result.data, ForecastResponse::class.java, config.cacheExpirationMinutes)
            }
        }
    }
    
    /**
     * Get daily forecast (up to 16 days).
     */
    suspend fun getDailyForecast(
        query: WeatherQuery,
        days: Int? = null
    ): Result<DailyForecastResponse> {
        val cacheKey = "daily_forecast_${query.hashCode()}_$days"
        
        cacheManager.get(cacheKey, DailyForecastResponse::class.java)?.let { cached ->
            return Result.Success(cached)
        }
        
        return executeWithRetry {
            when {
                query.cityName != null -> {
                    val cityQuery = buildCityQuery(query)
                    apiService.getDailyForecastByCityName(
                        cityQuery,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language,
                        days
                    )
                }
                query.latitude != null && query.longitude != null -> {
                    apiService.getDailyForecastByCoordinates(
                        query.latitude,
                        query.longitude,
                        config.apiKey,
                        query.units ?: config.units.value,
                        query.language ?: config.language,
                        days
                    )
                }
                else -> throw IllegalArgumentException("Daily forecast requires city name or coordinates")
            }
        }.also { result ->
            if (result is Result.Success) {
                cacheManager.put(cacheKey, result.data, DailyForecastResponse::class.java, config.cacheExpirationMinutes)
            }
        }
    }
    
    // ============== One Call API ==============
    
    /**
     * Get One Call API data.
     */
    suspend fun getOneCallData(
        latitude: Double,
        longitude: Double,
        exclude: List<String>? = null,
        units: String? = null,
        language: String? = null
    ): Result<OneCallResponse> {
        val excludeStr = exclude?.joinToString(",")
        val cacheKey = "onecall_${latitude}_${longitude}_$excludeStr"
        
        cacheManager.get(cacheKey, OneCallResponse::class.java)?.let { cached ->
            return Result.Success(cached)
        }
        
        return executeWithRetry {
            apiService.getOneCallData(
                latitude,
                longitude,
                config.apiKey,
                excludeStr,
                units ?: config.units.value,
                language ?: config.language
            )
        }.also { result ->
            if (result is Result.Success) {
                cacheManager.put(cacheKey, result.data, OneCallResponse::class.java, config.cacheExpirationMinutes)
            }
        }
    }
    
    /**
     * Get historical weather data.
     */
    suspend fun getHistoricalWeather(
        latitude: Double,
        longitude: Double,
        timestamp: Long,
        units: String? = null,
        language: String? = null
    ): Result<HistoricalWeatherResponse> {
        return executeWithRetry {
            apiService.getHistoricalWeather(
                latitude,
                longitude,
                timestamp,
                config.apiKey,
                units ?: config.units.value,
                language ?: config.language
            )
        }
    }
    
    // ============== Air Pollution ==============
    
    /**
     * Get current air pollution data.
     */
    suspend fun getCurrentAirPollution(
        latitude: Double,
        longitude: Double
    ): Result<AirPollutionResponse> {
        val cacheKey = "air_pollution_${latitude}_$longitude"
        
        cacheManager.get(cacheKey, AirPollutionResponse::class.java)?.let { cached ->
            return Result.Success(cached)
        }
        
        return executeWithRetry {
            apiService.getCurrentAirPollution(latitude, longitude, config.apiKey)
        }.also { result ->
            if (result is Result.Success) {
                cacheManager.put(cacheKey, result.data, AirPollutionResponse::class.java, config.cacheExpirationMinutes)
            }
        }
    }
    
    /**
     * Get air pollution forecast.
     */
    suspend fun getAirPollutionForecast(
        latitude: Double,
        longitude: Double
    ): Result<AirPollutionForecastResponse> {
        return executeWithRetry {
            apiService.getAirPollutionForecast(latitude, longitude, config.apiKey)
        }
    }
    
    /**
     * Get air pollution history.
     */
    suspend fun getAirPollutionHistory(
        latitude: Double,
        longitude: Double,
        start: Long,
        end: Long
    ): Result<AirPollutionHistoryResponse> {
        return executeWithRetry {
            apiService.getAirPollutionHistory(latitude, longitude, start, end, config.apiKey)
        }
    }
    
    // ============== Geocoding ==============
    
    /**
     * Get coordinates by location name.
     */
    suspend fun getCoordinatesByLocationName(
        query: String,
        limit: Int? = null
    ): Result<List<GeocodingLocation>> {
        return executeWithRetry {
            apiService.getCoordinatesByLocationName(query, limit, config.apiKey)
        }
    }
    
    /**
     * Get coordinates by ZIP code.
     */
    suspend fun getCoordinatesByZipCode(zipCode: String): Result<GeocodingLocation> {
        return executeWithRetry {
            apiService.getCoordinatesByZipCode(zipCode, config.apiKey)
        }
    }
    
    /**
     * Get location name by coordinates.
     */
    suspend fun getLocationNameByCoordinates(
        latitude: Double,
        longitude: Double,
        limit: Int? = null
    ): Result<List<ReverseGeocodingLocation>> {
        return executeWithRetry {
            apiService.getLocationNameByCoordinates(latitude, longitude, limit, config.apiKey)
        }
    }
    
    // ============== Weather Maps ==============
    
    /**
     * Get weather map tile.
     */
    suspend fun getWeatherMapTile(params: MapTileParams): Result<ResponseBody> {
        return executeWithRetry {
            apiService.getWeatherMapTile(
                params.layer.value,
                params.z,
                params.x,
                params.y,
                config.apiKey
            )
        }
    }
    
    // ============== Helper Methods ==============
    
    /**
     * Execute API call with retry logic.
     */
    private suspend fun <T> executeWithRetry(
        apiCall: suspend () -> Response<T>
    ): Result<T> {
        var lastException: Exception? = null
        
        for (attempt in 0 until config.maxRetryAttempts) {
            try {
                if (!NetworkUtils.isNetworkAvailable()) {
                    return Result.Error(NetworkException("No network connection available"))
                }
                
                val response = apiCall()
                
                // Extract rate limit info from headers
                val rateLimitInfo = extractRateLimitInfo(response)
                
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        // Store rate limit info if available
                        rateLimitInfo?.let { RateLimitManager.updateRateLimitInfo(it) }
                        return Result.Success(data)
                    } ?: return Result.Error(ApiException("Empty response body", response.code()))
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    
                    // Handle specific error codes
                    val exception = when (response.code()) {
                        401 -> InvalidApiKeyException("Invalid API key: $errorMsg")
                        400 -> InvalidRequestException("Bad request: $errorMsg")
                        404 -> ApiException("Resource not found: $errorMsg", 404)
                        429 -> {
                            // Rate limit exceeded
                            val retryAfter = response.headers()["Retry-After"]?.toLongOrNull()
                            RateLimitException(
                                message = "Rate limit exceeded",
                                retryAfter = retryAfter,
                                limit = rateLimitInfo?.limit,
                                remaining = rateLimitInfo?.remaining,
                                reset = rateLimitInfo?.reset
                            )
                        }
                        in 500..599 -> ApiException("Server error: $errorMsg", response.code())
                        else -> ApiException(errorMsg, response.code())
                    }
                    
                    // Don't retry on client errors (4xx) except rate limit
                    if (response.code() in 400..499 && response.code() != 429) {
                        return Result.Error(exception)
                    }
                    
                    // For rate limit errors, wait if retry-after is provided
                    if (response.code() == 429) {
                        val retryAfter = response.headers()["Retry-After"]?.toLongOrNull()
                        if (retryAfter != null && attempt < config.maxRetryAttempts - 1) {
                            delay(retryAfter * 1000)
                            continue
                        }
                        return Result.Error(exception)
                    }
                    
                    lastException = exception
                }
            } catch (e: IOException) {
                Timber.e(e, "Network error on attempt ${attempt + 1}")
                lastException = NetworkException("Network error: ${e.message}", e)
            } catch (e: Exception) {
                Timber.e(e, "Unexpected error on attempt ${attempt + 1}")
                lastException = ApiException("Unexpected error: ${e.message}", -1, cause = e)
            }
            
            // Exponential backoff for retries
            if (attempt < config.maxRetryAttempts - 1) {
                val delayMs = (2.0.pow(attempt) * 1000).toLong()
                delay(delayMs)
            }
        }
        
        return Result.Error(lastException ?: ApiException("Max retry attempts reached", -1))
    }
    
    /**
     * Extract rate limit information from response headers.
     */
    private fun <T> extractRateLimitInfo(response: Response<T>): RateLimitInfo? {
        return try {
            val limit = response.headers()["X-RateLimit-Limit"]?.toIntOrNull()
            val remaining = response.headers()["X-RateLimit-Remaining"]?.toIntOrNull()
            val reset = response.headers()["X-RateLimit-Reset"]?.toLongOrNull()
            
            if (limit != null && remaining != null && reset != null) {
                RateLimitInfo(limit, remaining, reset)
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.w(e, "Failed to extract rate limit info from headers")
            null
        }
    }
    
    /**
     * Build city query string.
     */
    private fun buildCityQuery(query: WeatherQuery): String {
        return when {
            query.stateCode != null && query.countryCode != null -> {
                "${query.cityName},${query.stateCode},${query.countryCode}"
            }
            query.countryCode != null -> {
                "${query.cityName},${query.countryCode}"
            }
            else -> query.cityName ?: ""
        }
    }
    
    /**
     * Build ZIP code query string.
     */
    private fun buildZipQuery(query: WeatherQuery): String {
        return if (query.countryCode != null) {
            "${query.zipCode},${query.countryCode}"
        } else {
            query.zipCode ?: ""
        }
    }
}