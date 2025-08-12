package com.dineshdev.openweathermap.sdk

import android.content.Context
import com.dineshdev.openweathermap.sdk.api.WeatherApiService
import com.dineshdev.openweathermap.sdk.config.OpenWeatherConfigProvider
import com.dineshdev.openweathermap.sdk.config.SimpleConfigProvider
import com.dineshdev.openweathermap.sdk.data.models.*
import com.dineshdev.openweathermap.sdk.repository.WeatherRepository
import com.dineshdev.openweathermap.sdk.utils.CacheManager
import com.dineshdev.openweathermap.sdk.utils.NetworkUtils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Main SDK class for OpenWeatherMap API integration.
 * 
 * Usage:
 * ```kotlin
 * // Initialize the SDK
 * val config = SimpleConfigProvider(apiKey = "YOUR_API_KEY")
 * OpenWeatherMapSDK.initialize(context, config)
 * 
 * // Get SDK instance
 * val weatherSDK = OpenWeatherMapSDK.getInstance()
 * 
 * // Make API calls
 * val result = weatherSDK.getCurrentWeatherByCityName("London")
 * ```
 */
class OpenWeatherMapSDK private constructor(
    context: Context,
    private val config: OpenWeatherConfigProvider
) {
    
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    private val okHttpClient: OkHttpClient = buildOkHttpClient()
    private val retrofit: Retrofit = buildRetrofit()
    private val apiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)
    private val cacheManager: CacheManager = CacheManager(context, moshi)
    private val repository: WeatherRepository = WeatherRepository(apiService, config, cacheManager)
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    init {
        NetworkUtils.init(context)
        
        // Enable Timber logging if configured
        if (config.enableLogging) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Clean expired cache entries periodically
        scope.launch {
            cacheManager.cleanExpiredEntries()
        }
    }
    
    // ============== Current Weather ==============
    
    /**
     * Get current weather by city name.
     * 
     * @param cityName City name (e.g., "London")
     * @param countryCode Optional country code (e.g., "GB")
     * @param stateCode Optional state code for US cities
     */
    suspend fun getCurrentWeatherByCityName(
        cityName: String,
        countryCode: String? = null,
        stateCode: String? = null
    ): Result<CurrentWeatherResponse> {
        val query = WeatherQuery(
            cityName = cityName,
            countryCode = countryCode,
            stateCode = stateCode
        )
        return repository.getCurrentWeather(query)
    }
    
    /**
     * Get current weather by city ID.
     * 
     * @param cityId OpenWeatherMap city ID
     */
    suspend fun getCurrentWeatherByCityId(cityId: Int): Result<CurrentWeatherResponse> {
        val query = WeatherQuery(cityId = cityId)
        return repository.getCurrentWeather(query)
    }
    
    /**
     * Get current weather by geographic coordinates.
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     */
    suspend fun getCurrentWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<CurrentWeatherResponse> {
        val query = WeatherQuery(latitude = latitude, longitude = longitude)
        return repository.getCurrentWeather(query)
    }
    
    /**
     * Get current weather by ZIP code.
     * 
     * @param zipCode ZIP code
     * @param countryCode Optional country code (default: US)
     */
    suspend fun getCurrentWeatherByZipCode(
        zipCode: String,
        countryCode: String? = null
    ): Result<CurrentWeatherResponse> {
        val query = WeatherQuery(zipCode = zipCode, countryCode = countryCode)
        return repository.getCurrentWeather(query)
    }
    
    /**
     * Get current weather for multiple cities within a rectangle zone.
     * 
     * @param bbox Bounding box coordinates "lon-left,lat-bottom,lon-right,lat-top,zoom"
     */
    suspend fun getCurrentWeatherInRectangle(bbox: String): Result<BulkWeatherResponse> {
        return repository.getCurrentWeatherInRectangle(bbox)
    }
    
    /**
     * Get current weather for multiple cities around a point.
     * 
     * @param latitude Center latitude
     * @param longitude Center longitude
     * @param count Number of cities to return (default: 10)
     */
    suspend fun getCurrentWeatherInCircle(
        latitude: Double,
        longitude: Double,
        count: Int? = null
    ): Result<BulkWeatherResponse> {
        return repository.getCurrentWeatherInCircle(latitude, longitude, count)
    }
    
    // ============== Forecast ==============
    
    /**
     * Get 5-day forecast with 3-hour intervals by city name.
     * 
     * @param cityName City name
     * @param countryCode Optional country code
     * @param count Number of timestamps to return (max: 40)
     */
    suspend fun getForecastByCityName(
        cityName: String,
        countryCode: String? = null,
        count: Int? = null
    ): Result<ForecastResponse> {
        val query = WeatherQuery(cityName = cityName, countryCode = countryCode)
        return repository.getForecast(query, count)
    }
    
    /**
     * Get 5-day forecast with 3-hour intervals by coordinates.
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     * @param count Number of timestamps to return (max: 40)
     */
    suspend fun getForecastByCoordinates(
        latitude: Double,
        longitude: Double,
        count: Int? = null
    ): Result<ForecastResponse> {
        val query = WeatherQuery(latitude = latitude, longitude = longitude)
        return repository.getForecast(query, count)
    }
    
    /**
     * Get daily forecast (up to 16 days) by city name.
     * 
     * @param cityName City name
     * @param days Number of days (max: 16)
     */
    suspend fun getDailyForecastByCityName(
        cityName: String,
        days: Int? = null
    ): Result<DailyForecastResponse> {
        val query = WeatherQuery(cityName = cityName)
        return repository.getDailyForecast(query, days)
    }
    
    /**
     * Get daily forecast (up to 16 days) by coordinates.
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     * @param days Number of days (max: 16)
     */
    suspend fun getDailyForecastByCoordinates(
        latitude: Double,
        longitude: Double,
        days: Int? = null
    ): Result<DailyForecastResponse> {
        val query = WeatherQuery(latitude = latitude, longitude = longitude)
        return repository.getDailyForecast(query, days)
    }
    
    // ============== One Call API 3.0 ==============
    
    /**
     * Get comprehensive weather data from One Call API.
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     * @param exclude List of data to exclude: "current", "minutely", "hourly", "daily", "alerts"
     */
    suspend fun getOneCallData(
        latitude: Double,
        longitude: Double,
        exclude: List<String>? = null
    ): Result<OneCallResponse> {
        return repository.getOneCallData(latitude, longitude, exclude)
    }
    
    /**
     * Get historical weather data.
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     * @param timestamp Unix timestamp for the date
     */
    suspend fun getHistoricalWeather(
        latitude: Double,
        longitude: Double,
        timestamp: Long
    ): Result<HistoricalWeatherResponse> {
        return repository.getHistoricalWeather(latitude, longitude, timestamp)
    }
    
    // ============== Air Pollution ==============
    
    /**
     * Get current air pollution data.
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     */
    suspend fun getCurrentAirPollution(
        latitude: Double,
        longitude: Double
    ): Result<AirPollutionResponse> {
        return repository.getCurrentAirPollution(latitude, longitude)
    }
    
    /**
     * Get air pollution forecast (up to 5 days).
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     */
    suspend fun getAirPollutionForecast(
        latitude: Double,
        longitude: Double
    ): Result<AirPollutionForecastResponse> {
        return repository.getAirPollutionForecast(latitude, longitude)
    }
    
    /**
     * Get historical air pollution data.
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     * @param start Start timestamp (Unix)
     * @param end End timestamp (Unix)
     */
    suspend fun getAirPollutionHistory(
        latitude: Double,
        longitude: Double,
        start: Long,
        end: Long
    ): Result<AirPollutionHistoryResponse> {
        return repository.getAirPollutionHistory(latitude, longitude, start, end)
    }
    
    // ============== Geocoding ==============
    
    /**
     * Get coordinates by location name (direct geocoding).
     * 
     * @param locationName Location name (can include state and country)
     * @param limit Maximum number of results
     */
    suspend fun getCoordinatesByLocationName(
        locationName: String,
        limit: Int? = null
    ): Result<List<GeocodingLocation>> {
        return repository.getCoordinatesByLocationName(locationName, limit)
    }
    
    /**
     * Get coordinates by ZIP code.
     * 
     * @param zipCode ZIP code with optional country code (e.g., "10001,US")
     */
    suspend fun getCoordinatesByZipCode(zipCode: String): Result<GeocodingLocation> {
        return repository.getCoordinatesByZipCode(zipCode)
    }
    
    /**
     * Get location name by coordinates (reverse geocoding).
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     * @param limit Maximum number of results
     */
    suspend fun getLocationNameByCoordinates(
        latitude: Double,
        longitude: Double,
        limit: Int? = null
    ): Result<List<ReverseGeocodingLocation>> {
        return repository.getLocationNameByCoordinates(latitude, longitude, limit)
    }
    
    // ============== Weather Maps ==============
    
    /**
     * Get weather map tile as byte array.
     * 
     * @param layer Map layer type
     * @param z Zoom level
     * @param x X tile coordinate
     * @param y Y tile coordinate
     */
    suspend fun getWeatherMapTile(
        layer: MapLayer,
        z: Int,
        x: Int,
        y: Int
    ): Result<ByteArray> {
        val params = MapTileParams(layer, z, x, y)
        return when (val result = repository.getWeatherMapTile(params)) {
            is Result.Success -> {
                try {
                    Result.Success(result.data.bytes())
                } catch (e: Exception) {
                    Result.Error(OpenWeatherException("Failed to read map tile", cause = e))
                }
            }
            is Result.Error -> result
            Result.Loading -> Result.Loading
        }
    }
    
    // ============== Cache Management ==============
    
    /**
     * Clear all cached data.
     */
    fun clearCache() {
        cacheManager.clearAll()
    }
    
    /**
     * Clean expired cache entries.
     */
    fun cleanExpiredCache() {
        cacheManager.cleanExpiredEntries()
    }
    
    /**
     * Get current cache size in bytes.
     */
    fun getCacheSize(): Long {
        return cacheManager.getCacheSize()
    }
    
    // ============== Configuration ==============
    
    /**
     * Update SDK configuration at runtime.
     */
    fun updateConfig(newConfig: OpenWeatherConfigProvider) {
        synchronized(lock) {
            instance = OpenWeatherMapSDK(context!!, newConfig)
        }
    }
    
    /**
     * Get current configuration.
     */
    fun getConfig(): OpenWeatherConfigProvider {
        return config
    }
    
    // ============== Private Methods ==============
    
    private fun buildOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(config.networkTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(config.networkTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(config.networkTimeoutSeconds, TimeUnit.SECONDS)
        
        if (config.enableLogging) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                Timber.tag("OkHttp").d(message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }
        
        // Add custom interceptor for common headers
        builder.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("User-Agent", "OpenWeatherMapSDK-Android/1.0.0")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        
        return builder.build()
    }
    
    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    
    companion object {
        @Volatile
        private var instance: OpenWeatherMapSDK? = null
        private var context: Context? = null
        private val lock = Any()
        
        /**
         * Initialize the SDK with configuration.
         * This must be called before using the SDK.
         * 
         * @param context Application context
         * @param config Configuration provider
         */
        @JvmStatic
        fun initialize(context: Context, config: OpenWeatherConfigProvider) {
            synchronized(lock) {
                this.context = context.applicationContext
                instance = OpenWeatherMapSDK(context.applicationContext, config)
            }
        }
        
        /**
         * Initialize the SDK with simple configuration.
         * 
         * @param context Application context
         * @param apiKey OpenWeatherMap API key
         */
        @JvmStatic
        fun initialize(context: Context, apiKey: String) {
            initialize(context, SimpleConfigProvider(apiKey))
        }
        
        /**
         * Get SDK instance.
         * 
         * @throws IllegalStateException if SDK is not initialized
         */
        @JvmStatic
        fun getInstance(): OpenWeatherMapSDK {
            return instance ?: throw IllegalStateException(
                "OpenWeatherMapSDK is not initialized. Call initialize() first."
            )
        }
        
        /**
         * Check if SDK is initialized.
         */
        @JvmStatic
        fun isInitialized(): Boolean {
            return instance != null
        }
    }
}