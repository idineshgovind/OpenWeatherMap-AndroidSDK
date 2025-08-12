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
- ✅ **Rate Limiting**: Built-in rate limit tracking and throttling
- ✅ **Multi-language**: Support for 50+ languages
- ✅ **All Units**: Metric, Imperial, and Standard (Kelvin)
- ✅ **Java Compatible**: Full Java interoperability
- ✅ **Coroutines Support**: First-class Kotlin coroutines integration
- ✅ **Extension Functions**: Convenient Kotlin extensions for common operations
- ✅ **Error Handling**: Comprehensive error handling with specific exception types

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

#### Kotlin

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

#### Java

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Simple initialization
        OpenWeatherMapSDK.initialize(this, "YOUR_API_KEY");
        
        // Or with custom configuration
        SimpleConfigProvider config = new SimpleConfigProvider(
            "YOUR_API_KEY",
            "https://api.openweathermap.org/",
            Units.METRIC,
            "en",
            BuildConfig.DEBUG,
            10,
            30L,
            3
        );
        OpenWeatherMapSDK.initialize(this, config);
    }
}
```

### 2. Get Current Weather

#### Kotlin

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
                    when (exception) {
                        is RateLimitException -> {
                            Log.e("Weather", "Rate limit exceeded. Retry after: ${exception.retryAfter}s")
                        }
                        is NetworkException -> {
                            Log.e("Weather", "Network error: ${exception.message}")
                        }
                        is InvalidApiKeyException -> {
                            Log.e("Weather", "Invalid API key")
                        }
                        else -> {
                            Log.e("Weather", "Error: ${exception.message}")
                        }
                    }
                }
        }
    }
}
```

#### Java

```java
public class WeatherActivity extends AppCompatActivity {
    private OpenWeatherMapSDK weatherSDK = OpenWeatherMapSDK.getInstance();
    
    private void getCurrentWeather(String city) {
        // Using coroutines from Java (requires kotlinx-coroutines-android)
        BuildersKt.launch(
            GlobalScope.INSTANCE,
            Dispatchers.getMain(),
            CoroutineStart.DEFAULT,
            (scope, continuation) -> {
                Object result = weatherSDK.getCurrentWeatherByCityName(
                    city, null, null, continuation
                );
                
                if (result instanceof Result.Success) {
                    CurrentWeatherResponse weather = ((Result.Success<CurrentWeatherResponse>) result).getData();
                    Log.d("Weather", "Temperature: " + weather.getMain().getTemperature());
                } else if (result instanceof Result.Error) {
                    Exception error = ((Result.Error) result).getException();
                    Log.e("Weather", "Error: " + error.getMessage());
                }
                return Unit.INSTANCE;
            }
        );
    }
}
```

## API Usage Examples

### Units and Languages

The SDK supports all OpenWeatherMap units and languages:

```kotlin
// Configure default units and language
val config = SimpleConfigProvider(
    apiKey = "YOUR_API_KEY",
    units = Units.METRIC,        // METRIC (°C), IMPERIAL (°F), STANDARD (K)
    language = "es"               // Spanish (50+ languages supported)
)

// Or override per request
val query = WeatherQuery(
    cityName = "Madrid",
    units = "metric",
    language = "es"
)
val weather = weatherSDK.getCurrentWeather(query)
```

Supported languages include: `en`, `es`, `fr`, `de`, `it`, `pt`, `ru`, `ja`, `ko`, `zh_cn`, `ar`, `hi`, and many more.

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

### Rate Limit Handling

The SDK automatically tracks and handles rate limits:

```kotlin
// Check current rate limit status
val rateLimitInfo = weatherSDK.getRateLimitInfo()
if (rateLimitInfo != null) {
    Log.d("RateLimit", "Remaining calls: ${rateLimitInfo.remaining}/${rateLimitInfo.limit}")
    Log.d("RateLimit", "Resets in: ${rateLimitInfo.getSecondsUntilReset()}s")
}

// Handle rate limit errors
weatherResult.onError { exception ->
    if (exception is RateLimitException) {
        // SDK automatically handles retry-after delays
        Log.e("API", "Rate limited. Retry after: ${exception.retryAfter}s")
    }
}
```

## Caching Details

The SDK includes an intelligent caching system:

### Cache Storage
- **Type**: SharedPreferences-based persistent disk cache
- **Location**: App's private SharedPreferences
- **Format**: JSON serialized with Moshi

### Cache Configuration
```kotlin
val config = SimpleConfigProvider(
    apiKey = "YOUR_API_KEY",
    cacheExpirationMinutes = 10  // Default: 10 minutes
)
```

### Offline Behavior
- When network is unavailable, cached data is returned if not expired
- Each endpoint has unique cache keys based on request parameters
- Cache is automatically cleaned of expired entries

### Cache Management
```kotlin
// Clear all cache
weatherSDK.clearCache()

// Clean expired cache entries
weatherSDK.cleanExpiredCache()

// Get cache size in bytes
val cacheSize = weatherSDK.getCacheSize()
Log.d("Cache", "Current cache size: ${cacheSize / 1024} KB")
```

## Error Handling

The SDK provides specific exception types for different error scenarios:

```kotlin
when (val result = weatherSDK.getCurrentWeatherByCityName("London")) {
    is Result.Success -> {
        val weather = result.data
        // Handle success
    }
    is Result.Error -> {
        when (val exception = result.exception) {
            is NetworkException -> {
                // No network connection, timeout, etc.
                showError("Network error: ${exception.message}")
            }
            is RateLimitException -> {
                // Rate limit exceeded
                showError("Rate limited. Retry after ${exception.retryAfter}s")
                showRateLimitInfo(exception.limit, exception.remaining, exception.reset)
            }
            is InvalidApiKeyException -> {
                // Invalid or missing API key
                showError("Invalid API key. Please check your configuration.")
            }
            is InvalidRequestException -> {
                // Bad request parameters
                showError("Invalid request: ${exception.message}")
            }
            is ApiException -> {
                // General API error with HTTP code
                showError("API error (${exception.code}): ${exception.message}")
            }
            else -> {
                // Other errors
                showError("Unexpected error: ${exception.message}")
            }
        }
    }
    Result.Loading -> {
        // Show loading state
        showLoading()
    }
}
```

## Data Models

All data models are:
- Kotlin data classes
- Parcelable for Android state preservation
- Null-safe with sensible defaults
- Annotated with Moshi for JSON parsing

Example:
```kotlin
@Parcelize
@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(
    @Json(name = "coord") val coordinates: Coordinates,
    @Json(name = "weather") val weather: List<WeatherDescription>,
    // ... other fields
) : Parcelable
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

## Testing

The SDK includes comprehensive unit tests using MockWebServer. To run tests:

```bash
./gradlew :OpenWeatherMap:test
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Support

For issues, questions, or suggestions, please [create an issue](https://github.com/dineshdev/openweathermap-android-sdk/issues).

## Author

**Dinesh Developer**
- Email: dev@dineshdev.com
- GitHub: [@dineshdev](https://github.com/dineshdev)