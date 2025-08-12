package com.dineshdev.openweathermap.sdk.api

import com.dineshdev.openweathermap.sdk.data.models.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit service interface for OpenWeatherMap API endpoints.
 */
interface WeatherApiService {
    
    // ============== Current Weather ==============
    
    /**
     * Get current weather by city name.
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<CurrentWeatherResponse>
    
    /**
     * Get current weather by city ID.
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByCityId(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<CurrentWeatherResponse>
    
    /**
     * Get current weather by coordinates.
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<CurrentWeatherResponse>
    
    /**
     * Get current weather by ZIP code.
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByZipCode(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<CurrentWeatherResponse>
    
    /**
     * Get current weather for multiple cities within a rectangle zone.
     */
    @GET("data/2.5/box/city")
    suspend fun getCurrentWeatherInRectangle(
        @Query("bbox") bbox: String, // lon-left,lat-bottom,lon-right,lat-top,zoom
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<BulkWeatherResponse>
    
    /**
     * Get current weather for multiple cities in a circle.
     */
    @GET("data/2.5/find")
    suspend fun getCurrentWeatherInCircle(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") count: Int? = null,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<BulkWeatherResponse>
    
    // ============== Forecast ==============
    
    /**
     * Get 5 day forecast (3 hour intervals) by city name.
     */
    @GET("data/2.5/forecast")
    suspend fun getForecastByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null,
        @Query("cnt") count: Int? = null
    ): Response<ForecastResponse>
    
    /**
     * Get 5 day forecast (3 hour intervals) by city ID.
     */
    @GET("data/2.5/forecast")
    suspend fun getForecastByCityId(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null,
        @Query("cnt") count: Int? = null
    ): Response<ForecastResponse>
    
    /**
     * Get 5 day forecast (3 hour intervals) by coordinates.
     */
    @GET("data/2.5/forecast")
    suspend fun getForecastByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null,
        @Query("cnt") count: Int? = null
    ): Response<ForecastResponse>
    
    /**
     * Get 5 day forecast (3 hour intervals) by ZIP code.
     */
    @GET("data/2.5/forecast")
    suspend fun getForecastByZipCode(
        @Query("zip") zipCode: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null,
        @Query("cnt") count: Int? = null
    ): Response<ForecastResponse>
    
    /**
     * Get daily forecast (up to 16 days) by city name.
     */
    @GET("data/2.5/forecast/daily")
    suspend fun getDailyForecastByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null,
        @Query("cnt") days: Int? = null
    ): Response<DailyForecastResponse>
    
    /**
     * Get daily forecast (up to 16 days) by coordinates.
     */
    @GET("data/2.5/forecast/daily")
    suspend fun getDailyForecastByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null,
        @Query("cnt") days: Int? = null
    ): Response<DailyForecastResponse>
    
    /**
     * Get hourly forecast (4 days) by coordinates.
     */
    @GET("data/2.5/forecast/hourly")
    suspend fun getHourlyForecastByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null,
        @Query("cnt") hours: Int? = null
    ): Response<HourlyForecastResponse>
    
    // ============== One Call API 3.0 ==============
    
    /**
     * Get One Call API data (current, minutely, hourly, daily, alerts).
     */
    @GET("data/3.0/onecall")
    suspend fun getOneCallData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("exclude") exclude: String? = null, // current,minutely,hourly,daily,alerts
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<OneCallResponse>
    
    /**
     * Get historical weather data (One Call API).
     */
    @GET("data/3.0/onecall/timemachine")
    suspend fun getHistoricalWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("dt") timestamp: Long,
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<HistoricalWeatherResponse>
    
    /**
     * Get weather overview (human-readable weather summary).
     */
    @GET("data/3.0/onecall/overview")
    suspend fun getWeatherOverview(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("date") date: String? = null, // Format: YYYY-MM-DD
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<ResponseBody> // Returns plain text
    
    // ============== Air Pollution ==============
    
    /**
     * Get current air pollution data.
     */
    @GET("data/2.5/air_pollution")
    suspend fun getCurrentAirPollution(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<AirPollutionResponse>
    
    /**
     * Get air pollution forecast (up to 5 days).
     */
    @GET("data/2.5/air_pollution/forecast")
    suspend fun getAirPollutionForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<AirPollutionForecastResponse>
    
    /**
     * Get historical air pollution data.
     */
    @GET("data/2.5/air_pollution/history")
    suspend fun getAirPollutionHistory(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("start") start: Long, // Unix timestamp
        @Query("end") end: Long,     // Unix timestamp
        @Query("appid") apiKey: String
    ): Response<AirPollutionHistoryResponse>
    
    // ============== Geocoding ==============
    
    /**
     * Direct geocoding - get coordinates by location name.
     */
    @GET("geo/1.0/direct")
    suspend fun getCoordinatesByLocationName(
        @Query("q") query: String, // {city},{state code},{country code}
        @Query("limit") limit: Int? = null,
        @Query("appid") apiKey: String
    ): Response<List<GeocodingLocation>>
    
    /**
     * Get coordinates by ZIP code.
     */
    @GET("geo/1.0/zip")
    suspend fun getCoordinatesByZipCode(
        @Query("zip") zipCode: String, // {zip code},{country code}
        @Query("appid") apiKey: String
    ): Response<GeocodingLocation>
    
    /**
     * Reverse geocoding - get location name by coordinates.
     */
    @GET("geo/1.0/reverse")
    suspend fun getLocationNameByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int? = null,
        @Query("appid") apiKey: String
    ): Response<List<ReverseGeocodingLocation>>
    
    // ============== Weather Maps ==============
    
    /**
     * Get weather map tile.
     * Note: This returns an image, so we use ResponseBody.
     */
    @GET("map/{layer}/{z}/{x}/{y}.png")
    suspend fun getWeatherMapTile(
        @Path("layer") layer: String,
        @Path("z") z: Int,
        @Path("x") x: Int,
        @Path("y") y: Int,
        @Query("appid") apiKey: String
    ): Response<ResponseBody>
    
    // ============== Bulk Download ==============
    
    /**
     * Get weather data for multiple cities by IDs.
     */
    @GET("data/2.5/group")
    suspend fun getWeatherForCityIds(
        @Query("id") cityIds: String, // Comma-separated city IDs
        @Query("appid") apiKey: String,
        @Query("units") units: String? = null,
        @Query("lang") language: String? = null
    ): Response<BulkWeatherResponse>
}