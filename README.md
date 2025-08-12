# OpenWeatherMap Android SDK

A comprehensive Android SDK for integrating OpenWeatherMap API services into your Android applications. This repository includes both the SDK and a complete sample application demonstrating all available features.

## Features

The OpenWeatherMap Android SDK provides access to all major OpenWeatherMap API services:

### ✅ Current Weather
- Get current weather by city name
- Get current weather by geographic coordinates
- Get current weather by ZIP code
- Get current weather by city ID
- Bulk weather data for multiple cities

### ✅ Weather Forecast
- 5-day forecast with 3-hour intervals
- 16-day daily forecast
- Hourly forecast (4 days)

### ✅ One Call API 3.0
- Current weather
- Minutely precipitation forecast
- Hourly forecast (48 hours)
- Daily forecast (8 days)
- Weather alerts
- Historical weather data

### ✅ Air Pollution
- Current air pollution data
- Air pollution forecast (5 days)
- Historical air pollution data

### ✅ Geocoding
- Direct geocoding (location name to coordinates)
- Reverse geocoding (coordinates to location name)
- ZIP code geocoding

### ✅ Weather Maps
- Weather map tiles for various layers
- Support for precipitation, clouds, pressure, temperature, and wind maps

## Sample Application

This repository includes a comprehensive sample application that demonstrates how to use all SDK features. The sample app serves as both documentation and a reference implementation for developers.

### Sample App Features

- **Clean Architecture**: Demonstrates MVVM pattern with ViewModels and repositories
- **Modern UI**: Built with Jetpack Compose and Material Design 3
- **Comprehensive Examples**: Shows usage of every SDK feature
- **Error Handling**: Proper error states and loading indicators
- **API Key Management**: Secure API key configuration via local.properties
- **Professional Design**: Weather-themed UI with icons and intuitive navigation

## Quick Start

### 1. Get Your API Key

1. Sign up at [OpenWeatherMap](https://openweathermap.org/api)
2. Get your free API key from the dashboard

### 2. Configure the Project

1. Clone this repository:
```bash
git clone https://github.com/dineshdevkota001/OpenWeatherMap-Android-SDK.git
cd OpenWeatherMap-Android-SDK
```

2. Copy the example configuration file:
```bash
cp local.properties.example local.properties
```

3. Edit `local.properties` and add your API key:
```properties
OPEN_WEATHER_API_KEY=your_actual_api_key_here
```

### 3. Run the Sample App

1. Open the project in Android Studio
2. Build and run the `app` module
3. The sample app will start with all features ready to test

## Using the SDK in Your Project

### 1. Add Dependency

Add the SDK module to your project or include it as a dependency:

```kotlin
dependencies {
    implementation(project(":OpenWeatherMap"))
    // Or when published to Maven Central:
    // implementation("com.dineshdev.openweathermap:sdk:1.0.0")
}
```

### 2. Initialize the SDK

Initialize the SDK in your Application class:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize OpenWeatherMap SDK
        OpenWeatherMapSDK.initialize(
            context = this,
            config = SimpleConfigProvider(
                apiKey = "YOUR_API_KEY",
                units = Units.METRIC,
                language = "en"
            )
        )
    }
}
```

### 3. Use the SDK

```kotlin
class WeatherRepository {
    private val sdk = OpenWeatherMapSDK.getInstance()
    
    suspend fun getCurrentWeather(cityName: String): Result<CurrentWeatherResponse> {
        return sdk.getCurrentWeatherByCityName(cityName)
    }
}
```

## SDK Configuration

The SDK supports extensive configuration options:

```kotlin
val config = SimpleConfigProvider(
    apiKey = "YOUR_API_KEY",
    baseUrl = "https://api.openweathermap.org/",  // Custom base URL
    units = Units.METRIC,                          // STANDARD, METRIC, IMPERIAL
    language = "en",                               // Language code
    enableLogging = BuildConfig.DEBUG,            // Enable debug logging
    cacheExpirationMinutes = 10,                  // Cache duration
    networkTimeoutSeconds = 30L,                  // Network timeout
    maxRetryAttempts = 3                          // Retry attempts
)
```

## Architecture

### SDK Architecture
- **Repository Pattern**: Clean separation of data access logic
- **Retrofit**: HTTP client for API communication
- **Moshi**: JSON serialization/deserialization
- **Coroutines**: Asynchronous programming support
- **Caching**: Built-in response caching
- **Rate Limiting**: Automatic rate limit handling
- **Error Handling**: Comprehensive error types and handling

### Sample App Architecture
- **MVVM**: Model-View-ViewModel pattern
- **Clean Architecture**: Separation of concerns
- **Jetpack Compose**: Modern declarative UI
- **StateFlow**: Reactive state management
- **Navigation**: Type-safe navigation between screens

## API Examples

### Current Weather
```kotlin
// By city name
val result = sdk.getCurrentWeatherByCityName("London", "GB")

// By coordinates
val result = sdk.getCurrentWeatherByCoordinates(51.5074, -0.1278)

// By ZIP code
val result = sdk.getCurrentWeatherByZipCode("10001", "US")
```

### Forecast
```kotlin
// 5-day forecast
val forecast = sdk.getForecastByCityName("London")

// Daily forecast
val dailyForecast = sdk.getDailyForecastByCityName("London", days = 7)
```

### One Call API
```kotlin
// Comprehensive weather data
val oneCall = sdk.getOneCallData(
    latitude = 51.5074,
    longitude = -0.1278,
    exclude = listOf("minutely", "alerts")
)

// Historical data
val historical = sdk.getHistoricalWeather(
    latitude = 51.5074,
    longitude = -0.1278,
    timestamp = System.currentTimeMillis() / 1000 - 86400 // Yesterday
)
```

### Air Pollution
```kotlin
// Current air pollution
val airPollution = sdk.getCurrentAirPollution(51.5074, -0.1278)

// Air pollution forecast
val forecast = sdk.getAirPollutionForecast(51.5074, -0.1278)
```

### Geocoding
```kotlin
// Get coordinates from location name
val locations = sdk.getCoordinatesByLocationName("London, UK")

// Get location name from coordinates
val locations = sdk.getLocationNameByCoordinates(51.5074, -0.1278)
```

## Error Handling

The SDK provides comprehensive error handling:

```kotlin
when (val result = sdk.getCurrentWeatherByCityName("London")) {
    is Result.Success -> {
        val weather = result.data
        // Handle success
    }
    is Result.Error -> {
        val error = result.exception
        when (error) {
            is NetworkException -> // Handle network errors
            is ApiException -> // Handle API errors
            is RateLimitException -> // Handle rate limit errors
            else -> // Handle other errors
        }
    }
    Result.Loading -> {
        // Handle loading state
    }
}
```

## Requirements

- **Minimum SDK**: 21 (Android 5.0)
- **Target SDK**: 36 (Android 14)
- **Kotlin**: 2.0.21+
- **Android Gradle Plugin**: 8.10.1+

## Dependencies

### SDK Dependencies
- Retrofit 2.9.0
- Moshi 1.15.0
- OkHttp 4.12.0
- Kotlin Coroutines 1.7.3
- Timber 5.0.1

### Sample App Additional Dependencies
- Jetpack Compose (BOM 2024.09.00)
- Navigation Compose 2.9.0
- ViewModel Compose 2.9.2
- Coil 2.7.0 (for image loading)
- Material Design 3

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Testing

The SDK includes comprehensive unit tests and integration tests. The sample app serves as an end-to-end testing platform.

To run tests:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Documentation

- **API Documentation**: Generated with Dokka
- **Sample App**: Live documentation of all features
- **Code Comments**: Comprehensive inline documentation
- **README**: This file with quick start and examples

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Issues**: [GitHub Issues](https://github.com/dineshdevkota001/OpenWeatherMap-Android-SDK/issues)
- **OpenWeatherMap API**: [Official Documentation](https://openweathermap.org/api)
- **Android Development**: [Android Developer Guides](https://developer.android.com/guide)

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed history of changes.

---

**Built with ❤️ for the Android developer community**