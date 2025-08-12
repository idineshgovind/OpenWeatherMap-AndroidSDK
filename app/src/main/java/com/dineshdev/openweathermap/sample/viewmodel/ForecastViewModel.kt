package com.dineshdev.openweathermap.sample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshdev.openweathermap.sample.data.repository.WeatherRepository
import com.dineshdev.openweathermap.sdk.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for the Forecast screen.
 * Demonstrates how to use forecast APIs from the OpenWeatherMap SDK.
 */
class ForecastViewModel : ViewModel() {
    
    private val repository = WeatherRepository()
    
    // UI State
    private val _uiState = MutableStateFlow(ForecastUiState())
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()
    
    // Input fields state
    private val _cityName = MutableStateFlow("")
    val cityName: StateFlow<String> = _cityName.asStateFlow()
    
    private val _countryCode = MutableStateFlow("")
    val countryCode: StateFlow<String> = _countryCode.asStateFlow()
    
    private val _latitude = MutableStateFlow("")
    val latitude: StateFlow<String> = _latitude.asStateFlow()
    
    private val _longitude = MutableStateFlow("")
    val longitude: StateFlow<String> = _longitude.asStateFlow()
    
    private val _forecastCount = MutableStateFlow("")
    val forecastCount: StateFlow<String> = _forecastCount.asStateFlow()
    
    private val _forecastDays = MutableStateFlow("")
    val forecastDays: StateFlow<String> = _forecastDays.asStateFlow()
    
    fun updateCityName(value: String) {
        _cityName.value = value
    }
    
    fun updateCountryCode(value: String) {
        _countryCode.value = value
    }
    
    fun updateLatitude(value: String) {
        _latitude.value = value
    }
    
    fun updateLongitude(value: String) {
        _longitude.value = value
    }
    
    fun updateForecastCount(value: String) {
        _forecastCount.value = value
    }
    
    fun updateForecastDays(value: String) {
        _forecastDays.value = value
    }
    
    fun get5DayForecastByCityName() {
        if (_cityName.value.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "City name cannot be empty"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            try {
                val count = _forecastCount.value.toIntOrNull()
                val result = repository.getForecastByCityName(
                    cityName = _cityName.value.trim(),
                    countryCode = _countryCode.value.trim().ifBlank { null },
                    count = count
                )
                
                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            forecastData = result.data,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                    Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting 5-day forecast")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get forecast data: ${e.message}"
                )
            }
        }
    }
    
    fun get5DayForecastByCoordinates() {
        val lat = _latitude.value.toDoubleOrNull()
        val lon = _longitude.value.toDoubleOrNull()
        
        if (lat == null || lon == null) {
            _uiState.value = _uiState.value.copy(
                error = "Please enter valid latitude and longitude"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            try {
                val count = _forecastCount.value.toIntOrNull()
                val result = repository.getForecastByCoordinates(lat, lon, count)
                
                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            forecastData = result.data,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                    Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting 5-day forecast by coordinates")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get forecast data: ${e.message}"
                )
            }
        }
    }
    
    fun getDailyForecastByCityName() {
        if (_cityName.value.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "City name cannot be empty"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            try {
                val days = _forecastDays.value.toIntOrNull()
                val result = repository.getDailyForecastByCityName(
                    cityName = _cityName.value.trim(),
                    days = days
                )
                
                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            dailyForecastData = result.data,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                    Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting daily forecast")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get forecast data: ${e.message}"
                )
            }
        }
    }
    
    fun getDailyForecastByCoordinates() {
        val lat = _latitude.value.toDoubleOrNull()
        val lon = _longitude.value.toDoubleOrNull()
        
        if (lat == null || lon == null) {
            _uiState.value = _uiState.value.copy(
                error = "Please enter valid latitude and longitude"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            try {
                val days = _forecastDays.value.toIntOrNull()
                val result = repository.getDailyForecastByCoordinates(lat, lon, days)
                
                when (result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            dailyForecastData = result.data,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Unknown error occurred"
                        )
                    }
                    Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error getting daily forecast by coordinates")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get forecast data: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearForecastData() {
        _uiState.value = _uiState.value.copy(
            forecastData = null,
            dailyForecastData = null
        )
    }
}

data class ForecastUiState(
    val isLoading: Boolean = false,
    val forecastData: ForecastResponse? = null,
    val dailyForecastData: DailyForecastResponse? = null,
    val error: String? = null
)