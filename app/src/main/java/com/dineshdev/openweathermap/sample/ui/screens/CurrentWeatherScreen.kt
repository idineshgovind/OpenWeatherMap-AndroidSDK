package com.dineshdev.openweathermap.sample.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
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
import com.dineshdev.openweathermap.sample.viewmodel.CurrentWeatherViewModel
import com.dineshdev.openweathermap.sdk.data.models.CurrentWeatherResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Screen demonstrating current weather API features.
 * Shows how to get current weather by:
 * - City name
 * - Geographic coordinates  
 * - ZIP code
 * - City ID
 */
@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cityName by viewModel.cityName.collectAsStateWithLifecycle()
    val countryCode by viewModel.countryCode.collectAsStateWithLifecycle()
    val stateCode by viewModel.stateCode.collectAsStateWithLifecycle()
    val latitude by viewModel.latitude.collectAsStateWithLifecycle()
    val longitude by viewModel.longitude.collectAsStateWithLifecycle()
    val zipCode by viewModel.zipCode.collectAsStateWithLifecycle()
    val zipCountryCode by viewModel.zipCountryCode.collectAsStateWithLifecycle()
    val cityId by viewModel.cityId.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionHeader(stringResource(R.string.current_weather_title))
        }

        // City Name Section
        item {
            WeatherCard(title = stringResource(R.string.get_weather_by_city)) {
                InputField(
                    value = cityName,
                    onValueChange = viewModel::updateCityName,
                    label = stringResource(R.string.city_name),
                    placeholder = stringResource(R.string.city_name_hint)
                )
                
                InputField(
                    value = countryCode,
                    onValueChange = viewModel::updateCountryCode,
                    label = stringResource(R.string.country_code),
                    placeholder = stringResource(R.string.country_code_hint)
                )
                
                InputField(
                    value = stateCode,
                    onValueChange = viewModel::updateStateCode,
                    label = stringResource(R.string.state_code),
                    placeholder = stringResource(R.string.state_code_hint)
                )
                
                SearchButton(
                    onClick = viewModel::getWeatherByCityName,
                    enabled = !uiState.isLoading && cityName.isNotBlank(),
                    text = stringResource(R.string.get_weather_by_city)
                )
            }
        }

        // Coordinates Section
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
                
                SearchButton(
                    onClick = viewModel::getWeatherByCoordinates,
                    enabled = !uiState.isLoading && latitude.isNotBlank() && longitude.isNotBlank(),
                    text = stringResource(R.string.get_weather_by_coordinates)
                )
            }
        }

        // ZIP Code Section
        item {
            WeatherCard(title = stringResource(R.string.get_weather_by_zip)) {
                InputField(
                    value = zipCode,
                    onValueChange = viewModel::updateZipCode,
                    label = stringResource(R.string.zip_code),
                    placeholder = stringResource(R.string.zip_code_hint),
                    keyboardType = KeyboardType.Number
                )
                
                InputField(
                    value = zipCountryCode,
                    onValueChange = viewModel::updateZipCountryCode,
                    label = stringResource(R.string.country_code),
                    placeholder = stringResource(R.string.country_code_hint)
                )
                
                SearchButton(
                    onClick = viewModel::getWeatherByZipCode,
                    enabled = !uiState.isLoading && zipCode.isNotBlank(),
                    text = stringResource(R.string.get_weather_by_zip)
                )
            }
        }

        // City ID Section
        item {
            WeatherCard(title = stringResource(R.string.get_weather_by_city_id)) {
                InputField(
                    value = cityId,
                    onValueChange = viewModel::updateCityId,
                    label = stringResource(R.string.city_id),
                    placeholder = stringResource(R.string.city_id_hint),
                    keyboardType = KeyboardType.Number
                )
                
                SearchButton(
                    onClick = viewModel::getWeatherByCityId,
                    enabled = !uiState.isLoading && cityId.isNotBlank(),
                    text = stringResource(R.string.get_weather_by_city_id)
                )
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

        // Weather Data
        uiState.weatherData?.let { weatherData ->
            item {
                CurrentWeatherCard(weatherData = weatherData)
            }
        }
    }
}

@Composable
private fun CurrentWeatherCard(
    weatherData: CurrentWeatherResponse,
    modifier: Modifier = Modifier
) {
    WeatherCard(
        title = weatherData.cityName,
        modifier = modifier
    ) {
        // Main weather info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${weatherData.main.temperature.roundToInt()}°C",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.feels_like, weatherData.main.feelsLike.roundToInt()),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                if (weatherData.weather.isNotEmpty()) {
                    WeatherIcon(
                        iconCode = weatherData.weather[0].icon,
                        description = weatherData.weather[0].description
                    )
                    Text(
                        text = weatherData.weather[0].description.replaceFirstChar { 
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() 
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        
        // Weather details
        InfoRow(
            label = stringResource(R.string.humidity),
            value = "${weatherData.main.humidity}%"
        )
        
        InfoRow(
            label = stringResource(R.string.pressure),
            value = "${weatherData.main.pressure} hPa"
        )
        
        weatherData.visibility?.let { visibility ->
            InfoRow(
                label = stringResource(R.string.visibility),
                value = "${visibility / 1000} km"
            )
        }
        
        InfoRow(
            label = stringResource(R.string.wind_speed),
            value = "${weatherData.wind.speed} m/s"
        )
        
        weatherData.wind.degree?.let { degree ->
            InfoRow(
                label = stringResource(R.string.wind_direction),
                value = "${degree}°"
            )
        }
        
        InfoRow(
            label = stringResource(R.string.cloudiness),
            value = "${weatherData.clouds.all}%"
        )
        
        // Sunrise/Sunset
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        
        InfoRow(
            label = stringResource(R.string.sunrise),
            value = timeFormat.format(Date(weatherData.system.sunrise * 1000))
        )
        
        InfoRow(
            label = stringResource(R.string.sunset),
            value = timeFormat.format(Date(weatherData.system.sunset * 1000))
        )
        
        // Coordinates
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        
        InfoRow(
            label = stringResource(R.string.coordinates),
            value = "${weatherData.coordinates.latitude}, ${weatherData.coordinates.longitude}"
        )
        
        // Last updated
        val lastUpdated = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
            .format(Date(weatherData.timestamp * 1000))
        
        Text(
            text = stringResource(R.string.last_updated, lastUpdated),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}