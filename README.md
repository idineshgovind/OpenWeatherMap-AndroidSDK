# OpenWeatherMap Android SDK

A comprehensive, easy-to-use Android SDK for the OpenWeatherMap API with full feature coverage, built with modern Android development best practices.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-orange.svg)](https://kotlinlang.org)

## Features

- ✅ **Complete API Coverage**: All OpenWeatherMap endpoints implemented
- ✅ **Current Weather Data**: Get real-time weather information
- ✅ **Forecasts**: 5-day/3-hour, 16-day daily, and hourly forecasts
- ✅ **One Call API 3.0**: Comprehensive weather data in a single call
- ✅ **Air Pollution**: Current, forecast, and historical air quality data
- ✅ **Geocoding**: Direct and reverse geocoding support
- ✅ **Weather Maps**: Tile-based weather map layers
- ✅ **Smart Caching**: Automatic response caching with configurable expiration
- ✅ **Retry Logic**: Automatic retry with exponential backoff
- ✅ **Coroutines Support**: First-class Kotlin coroutines integration
- ✅ **Extension Functions**: Convenient Kotlin extensions for common operations
- ✅ **Error Handling**: Comprehensive error handling and rate limiting

## Installation

### Gradle

Add the following to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.dineshdev:openweathermap-sdk:1.0.0")
}
```

### Maven

```xml
<dependency>
    <groupId>com.dineshdev</groupId>
    <artifactId>openweathermap-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

### 1. Initialize the SDK

Initialize the SDK in your Application class or before making any API calls:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Simple initialization with API key
        OpenWeatherMapSDK.initialize(this, "YOUR_API_KEY")
        
        // Or with custom configuration
        val config = SimpleConfigProvider(
            apiKey = "YOUR_API_KEY",
            units = Units.METRIC,
            language = "en",
            enableLogging = BuildConfig.DEBUG,
            cacheExpirationMinutes = 10
        )
        OpenWeatherMapSDK.initialize(this, config)
    }
}
```

### 2. Get Current Weather

```kotlin
class WeatherViewModel : ViewModel() {
    private val weatherSDK = OpenWeatherMapSDK.getInstance()
    
    fun getCurrentWeather(city: String) {
        viewModelScope.launch {
            weatherSDK.getCurrentWeatherByCityName(city)
                .onSuccess { weather ->
                    // Handle success
                    Log.d("Weather", "Temperature: ${weather.main.temperature}°")
                    Log.d("Weather", "Description: ${weather.weather.first().description}")
                }
                .onError { exception ->
                    // Handle error
                    Log.e("Weather", "Error: ${exception.message}")
                }
        }
    }
}
```

## API Usage Examples

### Current Weather

```kotlin
// By city name
val result = weatherSDK.getCurrentWeatherByCityName("London", "GB")

// By coordinates
val result = weatherSDK.getCurrentWeatherByCoordinates(51.5074, -0.1278)

// By ZIP code
val result = weatherSDK.getCurrentWeatherByZipCode("10001", "US")

// By city ID
val result = weatherSDK.getCurrentWeatherByCityId(2172797)
```

### Forecast

```kotlin
// 5-day forecast with 3-hour intervals
val forecast = weatherSDK.getForecastByCityName("London")

// Daily forecast (up to 16 days)
val dailyForecast = weatherSDK.getDailyForecastByCoordinates(51.5074, -0.1278, days = 7)
```

### One Call API 3.0

```kotlin
// Get comprehensive weather data
val oneCallData = weatherSDK.getOneCallData(
    latitude = 51.5074,
    longitude = -0.1278,
    exclude = listOf("minutely", "alerts")
)
```

### Air Pollution

```kotlin
// Current air pollution
val airQuality = weatherSDK.getCurrentAirPollution(51.5074, -0.1278)

// Air pollution forecast
val airForecast = weatherSDK.getAirPollutionForecast(51.5074, -0.1278)

// Historical air pollution
val historicalAir = weatherSDK.getAirPollutionHistory(
    latitude = 51.5074,
    longitude = -0.1278,
    start = startTimestamp,
    end = endTimestamp
)
```

### Geocoding

```kotlin
// Direct geocoding (location name to coordinates)
val locations = weatherSDK.getCoordinatesByLocationName("London,GB", limit = 5)

// Reverse geocoding (coordinates to location name)
val locations = weatherSDK.getLocationNameByCoordinates(51.5074, -0.1278)

// Geocoding by ZIP code
val location = weatherSDK.getCoordinatesByZipCode("10001,US")
```

### Weather Maps

```kotlin
// Get weather map tile
val mapTile = weatherSDK.getWeatherMapTile(
    layer = MapLayer.PRECIPITATION_NEW,
    z = 10,  // Zoom level
    x = 512, // X tile coordinate
    y = 256  // Y tile coordinate
)

// Use the byte array to display the image
mapTile.onSuccess { bytes ->
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    imageView.setImageBitmap(bitmap)
}
```

## Extension Functions

The SDK provides convenient extension functions for common operations:

```kotlin
// Temperature formatting
val tempString = weather.main.temperature.toTemperatureString("metric") // "20°C"

// Date/time formatting
val dateString = weather.timestamp.toFormattedDate() // "Jan 15, 2024 14:30"
val timeString = weather.system.sunrise?.toTimeString() // "06:45"

// Weather checks
if (weather.weather.isRaining()) {
    // Show rain UI
}

// Air quality
val colorCode = airPollution.list.first().main.getColorCode() // "#00E400" for good

// Result handling
weatherResult
    .onSuccess { data -> /* Handle success */ }
    .onError { error -> /* Handle error */ }
    .onLoading { /* Show loading */ }

val weather = weatherResult.getOrNull()
```

## Configuration Options

### Custom Configuration Provider

Implement `OpenWeatherConfigProvider` for advanced configuration:

```kotlin
class MyConfigProvider : OpenWeatherConfigProvider {
    override val apiKey: String = BuildConfig.OPENWEATHER_API_KEY
    override val baseUrl: String = "https://api.openweathermap.org/"
    override val units: Units = Units.METRIC
    override val language: String = Locale.getDefault().language
    override val enableLogging: Boolean = BuildConfig.DEBUG
    override val cacheExpirationMinutes: Int = 10
    override val networkTimeoutSeconds: Long = 30L
    override val maxRetryAttempts: Int = 3
}
```

### Runtime Configuration Updates

```kotlin
// Update configuration at runtime
val newConfig = SimpleConfigProvider(
    apiKey = "NEW_API_KEY",
    units = Units.IMPERIAL
)
weatherSDK.updateConfig(newConfig)
```

## Cache Management

```kotlin
// Clear all cache
weatherSDK.clearCache()

// Clean expired cache entries
weatherSDK.cleanExpiredCache()

// Get cache size
val cacheSize = weatherSDK.getCacheSize()
```

## Error Handling

The SDK uses a sealed `Result` class for API responses:

```kotlin
when (val result = weatherSDK.getCurrentWeatherByCityName("London")) {
    is Result.Success -> {
        val weather = result.data
        // Handle success
    }
    is Result.Error -> {
        val exception = result.exception
        when (exception) {
            is OpenWeatherException -> {
                // Handle API error
                val code = exception.code
                val message = exception.message
            }
            else -> {
                // Handle other errors
            }
        }
    }
    Result.Loading -> {
        // Show loading state
    }
}
```

## ProGuard Rules

The SDK includes consumer ProGuard rules that are automatically applied. If you need additional rules:

```proguard
# OpenWeatherMap SDK
-keep class com.dineshdev.openweathermap.sdk.** { *; }
-keep class com.dineshdev.openweathermap.sdk.data.models.** { *; }
```

## Requirements

- Android API 21+ (Android 5.0+)
- Kotlin 1.9.0+
- Internet permission (automatically included)

## Permissions

The SDK automatically includes the required permissions:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## Sample App

Check out the [sample app](app/) for complete implementation examples of all SDK features.

## API Documentation

- [SDK Documentation](https://dineshdev.github.io/openweathermap-android-sdk/)
- [OpenWeatherMap API Documentation](https://openweathermap.org/api)

## Testing

The SDK includes comprehensive unit tests using MockWebServer. To run tests:

```bash
./gradlew :OpenWeatherMap:test
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [OpenWeatherMap](https://openweathermap.org/) for providing the weather API
- [Retrofit](https://square.github.io/retrofit/) for networking
- [Moshi](https://github.com/square/moshi) for JSON parsing
- [OkHttp](https://square.github.io/okhttp/) for HTTP client
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for asynchronous programming

## Support

For issues, questions, or suggestions, please [create an issue](https://github.com/dineshdev/openweathermap-android-sdk/issues).

## Author

**Dinesh Developer**
- Email: dev@dineshdev.com
- GitHub: [@dineshdev](https://github.com/dineshdev)