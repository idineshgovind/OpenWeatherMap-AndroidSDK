# OpenWeatherMap Android SDK - Setup Guide

This guide will help you quickly set up and run the OpenWeatherMap Android SDK sample application.

## Prerequisites

- **Android Studio**: Arctic Fox (2020.3.1) or later
- **JDK**: 11 or later
- **Android SDK**: API level 21 (Android 5.0) or higher
- **OpenWeatherMap API Key**: Free from [OpenWeatherMap](https://openweathermap.org/api)

## Step 1: Get Your API Key

1. Visit [OpenWeatherMap](https://openweathermap.org/api)
2. Sign up for a free account or log in
3. Navigate to the API Keys section in your account dashboard
4. Copy your API key (it may take a few minutes to activate)

## Step 2: Clone and Configure

### Clone the Repository
```bash
git clone https://github.com/dineshdevkota001/OpenWeatherMap-Android-SDK.git
cd OpenWeatherMap-Android-SDK
```

### Configure API Key
1. Copy the example configuration file:
   ```bash
   cp local.properties.example local.properties
   ```

2. Edit `local.properties` and replace the placeholder with your actual API key:
   ```properties
   # Android SDK path (update to your actual path)
   sdk.dir=/path/to/your/android/sdk
   
   # Your OpenWeatherMap API key
   OPEN_WEATHER_API_KEY=your_actual_api_key_here
   ```

**Important**: Never commit your `local.properties` file to version control. It's already in `.gitignore`.

## Step 3: Open in Android Studio

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned repository folder
4. Select the root folder and click "OK"
5. Wait for Gradle sync to complete

## Step 4: Build and Run

### Option A: Using Android Studio
1. Select the `app` module in the run configuration dropdown
2. Choose your target device (emulator or physical device)
3. Click the "Run" button (green triangle)

### Option B: Using Command Line
```bash
# Build the project
./gradlew build

# Install on connected device
./gradlew installDebug

# Or build and install in one step
./gradlew installDebug
```

## Step 5: Test the Sample App

Once the app is running, you can test the following features:

### Current Weather Tab
- **By City Name**: Enter "London" and optionally "GB" for country code
- **By Coordinates**: Try latitude `51.5074` and longitude `-0.1278` (London)
- **By ZIP Code**: Enter `10001` for New York
- **By City ID**: Enter `2172797` for London

### Forecast Tab
- **5-Day Forecast**: Enter any city name to get 3-hour interval forecasts
- **Daily Forecast**: Get up to 16 days of daily weather forecasts

### Other Tabs
- Currently show placeholder screens with "coming soon" messages
- These demonstrate the app structure for implementing additional features

## Troubleshooting

### Common Issues

#### 1. Build Failed - SDK Not Found
**Error**: `SDK location not found`
**Solution**: Update the `sdk.dir` path in `local.properties` to point to your Android SDK installation.

#### 2. API Key Not Working
**Error**: API calls return authentication errors
**Solutions**:
- Verify your API key is correct in `local.properties`
- Wait a few minutes if you just created the key (activation delay)
- Check that there are no extra spaces in the key

#### 3. Network Errors in Emulator
**Error**: Network requests fail in emulator
**Solutions**:
- Ensure the emulator has internet access
- Try using a physical device
- Check firewall settings

#### 4. Compilation Errors
**Error**: Various compilation errors
**Solutions**:
- Ensure you're using Android Studio Arctic Fox or later
- Update to the latest Gradle version
- Invalidate caches and restart: `File > Invalidate Caches and Restart`

### Getting Help

1. **Check Logs**: Monitor Android Studio's Logcat for detailed error messages
2. **Clean Build**: Try `Build > Clean Project` then `Build > Rebuild Project`
3. **Update Dependencies**: Ensure all dependencies are up to date
4. **GitHub Issues**: Report issues at the [repository](https://github.com/dineshdevkota001/OpenWeatherMap-Android-SDK/issues)

## Project Structure

```
OpenWeatherMap-Android-SDK/
├── app/                           # Sample application
│   ├── src/main/java/.../sample/  # Sample app code
│   │   ├── data/repository/       # Repository layer
│   │   ├── ui/screens/           # Compose screens
│   │   ├── ui/components/        # Reusable UI components
│   │   ├── ui/theme/             # App theming
│   │   ├── viewmodel/            # ViewModels
│   │   ├── MainActivity.kt       # Main activity
│   │   └── SampleApplication.kt  # Application class
│   └── src/main/res/             # Resources
│       ├── values/strings.xml    # All UI strings
│       └── ...
├── OpenWeatherMap/               # SDK module
│   └── src/main/java/            # SDK implementation
├── local.properties              # Local configuration (you create this)
├── local.properties.example      # Example configuration
└── README.md                     # Project documentation
```

## Development Tips

### Adding New Features
1. Create a new screen in `ui/screens/`
2. Create corresponding ViewModel in `viewmodel/`
3. Add navigation in `MainActivity.kt`
4. Add strings to `res/values/strings.xml`

### Understanding the Architecture
- **MVVM Pattern**: ViewModels manage UI state
- **Repository Pattern**: Repositories wrap SDK calls
- **Compose UI**: Modern declarative UI framework
- **StateFlow**: Reactive state management

### Testing API Calls
- Use the app's built-in testing interface
- Monitor network calls in Android Studio's Network Inspector
- Check Logcat for detailed SDK logging (in debug builds)

## Next Steps

1. **Explore the Code**: Review the sample implementations
2. **Add Features**: Implement remaining API features (One Call, Air Pollution, etc.)
3. **Customize UI**: Modify the UI to match your design requirements
4. **Integrate into Your App**: Use the SDK in your own applications

## API Rate Limits

- **Free Tier**: 1,000 calls/month, 60 calls/minute
- **Paid Tiers**: Higher limits available
- The SDK automatically handles rate limiting and provides status information

For more details, visit the [OpenWeatherMap pricing page](https://openweathermap.org/price).

---

**Need help?** Open an issue on [GitHub](https://github.com/dineshdevkota001/OpenWeatherMap-Android-SDK/issues) or check the [OpenWeatherMap API documentation](https://openweathermap.org/api).