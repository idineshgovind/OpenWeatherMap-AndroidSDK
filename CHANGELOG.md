# Changelog

All notable changes to the OpenWeatherMap Android SDK will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-01-15

### Added
- Initial release of OpenWeatherMap Android SDK
- Complete implementation of Current Weather API
  - Get weather by city name, coordinates, ZIP code, and city ID
  - Bulk weather queries for rectangles and circles
- Full Forecast API support
  - 5-day/3-hour forecast
  - 16-day daily forecast
  - Hourly forecast (4 days)
- One Call API 3.0 integration
  - Current weather
  - Minutely precipitation forecast
  - Hourly forecast
  - Daily forecast
  - Weather alerts
  - Historical weather data
- Air Pollution API
  - Current air pollution data
  - Air pollution forecast (5 days)
  - Historical air pollution data
- Geocoding API
  - Direct geocoding (location name to coordinates)
  - Reverse geocoding (coordinates to location name)
  - ZIP code geocoding
- Weather Maps API
  - Support for all weather map layers
  - Tile-based map data retrieval
- Smart caching system with configurable expiration
- Automatic retry with exponential backoff
- Comprehensive error handling
- Kotlin coroutines support
- Extension functions for common operations
- Extensive KDoc documentation
- ProGuard rules included
- Sample app with usage examples
- Unit tests with MockWebServer

### Technical Details
- Minimum SDK: API 21 (Android 5.0)
- Target SDK: API 36
- Kotlin: 1.9.0+
- Architecture: Clean Architecture with Repository pattern
- Networking: Retrofit 2.9.0 + OkHttp 4.12.0
- JSON Parsing: Moshi 1.15.0
- Coroutines: 1.7.3

### Dependencies
- androidx.core:core-ktx
- com.squareup.retrofit2:retrofit
- com.squareup.retrofit2:converter-moshi
- com.squareup.okhttp3:okhttp
- com.squareup.okhttp3:logging-interceptor
- com.squareup.moshi:moshi
- com.squareup.moshi:moshi-kotlin
- org.jetbrains.kotlinx:kotlinx-coroutines-core
- org.jetbrains.kotlinx:kotlinx-coroutines-android
- com.jakewharton.timber:timber

## [Unreleased]

### Planned Features
- Weather widgets
- Location-based automatic weather updates
- Offline mode improvements
- Additional language support
- Weather notifications
- GraphQL API support (when available)
- Advanced caching strategies
- Weather data persistence with Room
- Compose UI components library
- Weather animations library

### Known Issues
- None at this time

---

For more information, visit the [GitHub repository](https://github.com/dineshdev/openweathermap-android-sdk).