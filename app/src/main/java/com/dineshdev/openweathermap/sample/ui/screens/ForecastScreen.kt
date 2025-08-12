package com.dineshdev.openweathermap.sample.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dineshdev.openweathermap.sample.R
import com.dineshdev.openweathermap.sample.ui.components.*
import com.dineshdev.openweathermap.sample.viewmodel.ForecastViewModel
import com.dineshdev.openweathermap.sdk.data.models.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Screen demonstrating forecast API features.
 * Shows how to get:
 * - 5-day forecast with 3-hour intervals
 * - Daily forecast (up to 16 days)
 */
@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cityName by viewModel.cityName.collectAsStateWithLifecycle()
    val countryCode by viewModel.countryCode.collectAsStateWithLifecycle()
    val latitude by viewModel.latitude.collectAsStateWithLifecycle()
    val longitude by viewModel.longitude.collectAsStateWithLifecycle()
    val forecastCount by viewModel.forecastCount.collectAsStateWithLifecycle()
    val forecastDays by viewModel.forecastDays.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionHeader(stringResource(R.string.forecast_title))
        }

        // 5-Day Forecast Section
        item {
            WeatherCard(title = stringResource(R.string.get_5_day_forecast)) {
                InputField(
                    value = cityName,
                    onValueChange = viewModel::updateCityName,
                    label = stringResource(R.string.city_name),
                    placeholder = stringResource(R.string.city_name_hint)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InputField(
                        value = countryCode,
                        onValueChange = viewModel::updateCountryCode,
                        label = stringResource(R.string.country_code),
                        placeholder = stringResource(R.string.country_code_hint),
                        modifier = Modifier.weight(1f)
                    )
                    
                    InputField(
                        value = forecastCount,
                        onValueChange = viewModel::updateForecastCount,
                        label = stringResource(R.string.forecast_count),
                        placeholder = stringResource(R.string.forecast_count_hint),
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                SearchButton(
                    onClick = viewModel::get5DayForecastByCityName,
                    enabled = !uiState.isLoading && cityName.isNotBlank(),
                    text = stringResource(R.string.get_5_day_forecast)
                )
            }
        }

        // 5-Day Forecast by Coordinates Section
        item {
            WeatherCard(title = stringResource(R.string.get_weather_by_coordinates)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InputField(
                        value = latitude,
                        onValueChange = viewModel::updateLatitude,
                        label = stringResource(R.string.latitude),
                        placeholder = stringResource(R.string.latitude_hint),
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    
                    InputField(
                        value = longitude,
                        onValueChange = viewModel::updateLongitude,
                        label = stringResource(R.string.longitude),
                        placeholder = stringResource(R.string.longitude_hint),
                        keyboardType = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                InputField(
                    value = forecastCount,
                    onValueChange = viewModel::updateForecastCount,
                    label = stringResource(R.string.forecast_count),
                    placeholder = stringResource(R.string.forecast_count_hint),
                    keyboardType = KeyboardType.Number
                )
                
                SearchButton(
                    onClick = viewModel::get5DayForecastByCoordinates,
                    enabled = !uiState.isLoading && latitude.isNotBlank() && longitude.isNotBlank(),
                    text = "Get 5-Day Forecast by Coordinates"
                )
            }
        }

        // Daily Forecast Section
        item {
            WeatherCard(title = stringResource(R.string.get_daily_forecast)) {
                InputField(
                    value = cityName,
                    onValueChange = viewModel::updateCityName,
                    label = stringResource(R.string.city_name),
                    placeholder = stringResource(R.string.city_name_hint)
                )
                
                InputField(
                    value = forecastDays,
                    onValueChange = viewModel::updateForecastDays,
                    label = stringResource(R.string.forecast_days),
                    placeholder = stringResource(R.string.forecast_days_hint),
                    keyboardType = KeyboardType.Number
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SearchButton(
                        onClick = viewModel::getDailyForecastByCityName,
                        enabled = !uiState.isLoading && cityName.isNotBlank(),
                        text = "Get Daily Forecast",
                        modifier = Modifier.weight(1f)
                    )
                    
                    SearchButton(
                        onClick = viewModel::getDailyForecastByCoordinates,
                        enabled = !uiState.isLoading && latitude.isNotBlank() && longitude.isNotBlank(),
                        text = "By Coordinates",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Loading State
        if (uiState.isLoading) {
            item {
                LoadingIndicator()
            }
        }

        // Error State
        uiState.error?.let { error ->
            item {
                ErrorCard(
                    error = error,
                    onRetry = { viewModel.clearError() }
                )
            }
        }

        // 5-Day Forecast Data
        uiState.forecastData?.let { forecastData ->
            item {
                ForecastDataCard(forecastData = forecastData)
            }
        }

        // Daily Forecast Data
        uiState.dailyForecastData?.let { dailyForecastData ->
            item {
                DailyForecastDataCard(dailyForecastData = dailyForecastData)
            }
        }
    }
}

@Composable
private fun ForecastDataCard(
    forecastData: ForecastResponse,
    modifier: Modifier = Modifier
) {
    WeatherCard(
        title = "${forecastData.city.name} - 5-Day Forecast",
        modifier = modifier
    ) {
        Text(
            text = "${forecastData.list.size} forecasts",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Show first few forecast items
        forecastData.list.take(8).forEach { forecast ->
            ForecastItemRow(forecast = forecast)
            if (forecast != forecastData.list.take(8).last()) {
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
        
        if (forecastData.list.size > 8) {
            Text(
                text = "... and ${forecastData.list.size - 8} more",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun ForecastItemRow(
    forecast: ForecastItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
            Text(
                text = dateFormat.format(Date(forecast.timestamp * 1000)),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            if (forecast.weather.isNotEmpty()) {
                Text(
                    text = forecast.weather[0].description.replaceFirstChar { 
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() 
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (forecast.weather.isNotEmpty()) {
                WeatherIcon(
                    iconCode = forecast.weather[0].icon,
                    description = forecast.weather[0].description,
                    modifier = Modifier.size(32.dp)
                )
            }
            Text(
                text = "${forecast.main.temperature.roundToInt()}°C",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DailyForecastDataCard(
    dailyForecastData: DailyForecastResponse,
    modifier: Modifier = Modifier
) {
    WeatherCard(
        title = "${dailyForecastData.city.name} - Daily Forecast",
        modifier = modifier
    ) {
        Text(
            text = "${dailyForecastData.list.size} days",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        dailyForecastData.list.forEach { dailyForecast ->
            DailyForecastItemRow(forecast = dailyForecast)
            if (dailyForecast != dailyForecastData.list.last()) {
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
private fun DailyForecastItemRow(
    forecast: DailyForecastItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            val dateFormat = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
            Text(
                text = dateFormat.format(Date(forecast.timestamp * 1000)),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            if (forecast.weather.isNotEmpty()) {
                Text(
                    text = forecast.weather[0].description.replaceFirstChar { 
                        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() 
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (forecast.weather.isNotEmpty()) {
                WeatherIcon(
                    iconCode = forecast.weather[0].icon,
                    description = forecast.weather[0].description,
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${forecast.temperature.max.roundToInt()}°",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${forecast.temperature.min.roundToInt()}°",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}