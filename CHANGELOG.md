# Changelog

All notable changes to the OpenWeatherMap Android SDK will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-01-15

### Added

#### Core Features
- Initial release of OpenWeatherMap Android SDK
- **OpenWeatherConfigProvider** interface for flexible API key and configuration management
- **SimpleConfigProvider** for easy SDK initialization

#### API Coverage
- **Current Weather API**
  - Get weather by city name, coordinates, ZIP code, and city ID
  - Bulk weather queries for rectangles and circles
  - Support for all units: metric (Celsius), imperial (Fahrenheit), standard (Kelvin)
  - Multi-language support (50+ languages via ISO 639-1 codes)

- **Forecast API**
  - 5-day/3-hour forecast (up to 40 timestamps)
  - 16-day daily forecast
  - Hourly forecast (4 days/96 hours)
  - All forecasts support metric, imperial, and standard units

- **One Call API 3.0**
  - Current weather with all parameters
  - Minutely precipitation forecast (60 minutes)
  - Hourly forecast (48 hours)
  - Daily forecast (8 days)
  - Weather alerts with severity levels
  - Historical weather data

- **Air Pollution API**
  - Current air pollution data with AQI (Air Quality Index)
  - Air pollution forecast (5 days)
  - Historical air pollution data
  - Components: CO, NO, NO2, O3, SO2, PM2.5, PM10, NH3

- **Geocoding API**
  - Direct geocoding (location name to coordinates)
  - Reverse geocoding (coordinates to location name)
  - ZIP code geocoding
  - Support for state codes (US) and country codes (ISO 3166)

- **Weather Maps API**
  - Cloud coverage layer (clouds_new)
  - Precipitation intensity layer (precipitation_new)
  - Sea level pressure layer (pressure_new)
  - Wind speed layer (wind_new)
  - Temperature layer (temp_new)
  - Zoom levels: 1-10
  - Tile coordinates: x, y based on map projection

#### Error Handling
- **Specific error types:**
  - `NetworkException`: No network, timeout, connection issues
  - `ApiException`: General API errors with HTTP codes
  - `RateLimitException`: Rate limit exceeded with retry info
  - `InvalidApiKeyException`: 401 authentication errors
  - `InvalidRequestException`: 400 bad request errors
- **Rate limit tracking:**
  - Automatic extraction of X-RateLimit headers
  - RateLimitInfo with limit, remaining, and reset time
  - Automatic throttling when approaching limits
  - Retry-After header support for 429 responses
- **Localized error messages** based on API language setting

#### Data Models
- All models are Kotlin data classes
- **Parcelable support** for Android state preservation
- Moshi annotations for JSON serialization
- Null-safe with proper default values
- Extension properties for computed values

#### Caching System
- **Storage**: SharedPreferences-based disk caching
- **Configurable expiration**: Default 10 minutes, customizable
- **Offline behavior**: Returns cached data when network unavailable
- **Cache management**:
  - Clear all cache
  - Clean expired entries
  - Get cache size in bytes
  - Per-endpoint cache keys

#### Architecture
- Clean Architecture with Repository pattern
- Automatic retry with exponential backoff (max 3 attempts)
- Kotlin coroutines for async operations
- Thread-safe singleton pattern
- Dependency injection ready

#### Developer Experience
- Extension functions for formatting and utilities
- Java interoperability with @JvmStatic methods
- ProGuard rules included (consumer-rules.pro)
- Comprehensive KDoc documentation
- Debug logging support (configurable)

### Technical Specifications

#### Dependencies
- **Networking**: Retrofit 2.9.0, OkHttp 4.12.0
- **JSON**: Moshi 1.15.0 with Kotlin support
- **Async**: Coroutines 1.7.3
- **Logging**: Timber 5.0.1
- **Android**: AndroidX Core KTX

#### Requirements
- **Minimum SDK**: API 21 (Android 5.0 Lollipop)
- **Target SDK**: API 36
- **Kotlin**: 1.9.0+
- **Gradle**: 8.0+

#### Publishing
- **Maven Coordinates**:
  - Group ID: `com.dineshdev`
  - Artifact ID: `openweathermap-sdk`
  - Version: `1.0.0`
- **License**: Apache License 2.0
- **Signing**: GPG signing for Maven Central
- **Artifacts**: AAR with sources and javadoc

#### Permissions (Auto-included)
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Testing
- Unit tests with MockWebServer (Planned)
- Integration tests (Planned)
- Code coverage target: 80% (Planned)

### Sample App (In Development)
- Demonstrates all API endpoints
- Kotlin examples for all features
- Java compatibility examples
- Error handling demonstrations
- Rate limit visualization
- Offline mode testing

## [Unreleased]

### Planned Features
- Complete unit test coverage with MockWebServer
- Integration tests with live API (debug builds)
- Comprehensive sample app with Java examples
- Room database for advanced caching
- Weather widgets
- Location-based automatic updates
- Weather notifications
- GraphQL API support (when available)
- Compose UI components library
- Weather animations library
- SDK metrics and analytics

### Known Issues
- Sample app not yet completed
- Unit tests not yet implemented
- Java usage examples pending

---

For more information, visit the [GitHub repository](https://github.com/dineshdev/openweathermap-android-sdk).