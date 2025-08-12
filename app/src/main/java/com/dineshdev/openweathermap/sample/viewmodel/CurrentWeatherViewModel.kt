package com.dineshdev.openweathermap.sample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dineshdev.openweathermap.sample.data.repository.WeatherRepository
import com.dineshdev.openweathermap.sdk.data.models.CurrentWeatherResponse
import com.dineshdev.openweathermap.sdk.data.models.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * ViewModel for the Current Weather screen.
 * Demonstrates how to use the OpenWeatherMap SDK in a ViewModel.
 */
class CurrentWeatherViewModel : ViewModel() {
    
    private val repository = WeatherRepository()
    
    // UI State
    private val _uiState = MutableStateFlow(CurrentWeatherUiState())
    val uiState: StateFlow<CurrentWeatherUiState> = _uiState.asStateFlow()
    
    // Input fields state
    private val _cityName = MutableStateFlow("")
    val cityName: StateFlow<String> = _cityName.asStateFlow()
    
    private val _countryCode = MutableStateFlow("")
    val countryCode: StateFlow<String> = _countryCode.asStateFlow()
    
    private val _stateCode = MutableStateFlow("")
    val stateCode: StateFlow<String> = _stateCode.asStateFlow()
    
    private val _latitude = MutableStateFlow("")
    val latitude: StateFlow<String> = _latitude.asStateFlow()
    
    private val _longitude = MutableStateFlow("")
    val longitude: StateFlow<String> = _longitude.asStateFlow()
    
    private val _zipCode = MutableStateFlow("")
    val zipCode: StateFlow<String> = _zipCode.asStateFlow()
    
    private val _zipCountryCode = MutableStateFlow("")
    val zipCountryCode: StateFlow<String> = _zipCountryCode.asStateFlow()
    
    private val _cityId = MutableStateFlow("")
    val cityId: StateFlow<String> = _cityId.asStateFlow()
    
    fun updateCityName(value: String) {
        _cityName.value = value
    }
    
    fun updateCountryCode(value: String) {
        _countryCode.value = value
    }
    
    fun updateStateCode(value: String) {
        _stateCode.value = value
    }
    
    fun updateLatitude(value: String) {
        _latitude.value = value
    }
    
    fun updateLongitude(value: String) {
        _longitude.value = value
    }
    
    fun updateZipCode(value: String) {
        _zipCode.value = value
    }
    
    fun updateZipCountryCode(value: String) {
        _zipCountryCode.value = value
    }
    
    fun updateCityId(value: String) {
        _cityId.value = value
    }
    
    fun getWeatherByCityName() {
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
                val result = repository.getCurrentWeatherByCityName(
                    cityName = _cityName.value.trim(),
                    countryCode = _countryCode.value.trim().ifBlank { null },
                    stateCode = _stateCode.value.trim().ifBlank { null }
                )
                
                handleWeatherResult(result)
            } catch (e: Exception) {
                Timber.e(e, "Error getting weather by city name")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get weather data: ${e.message}"
                )
            }
        }
    }
    
    fun getWeatherByCoordinates() {
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
                val result = repository.getCurrentWeatherByCoordinates(lat, lon)
                handleWeatherResult(result)
            } catch (e: Exception) {
                Timber.e(e, "Error getting weather by coordinates")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get weather data: ${e.message}"
                )
            }
        }
    }
    
    fun getWeatherByZipCode() {
        if (_zipCode.value.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "ZIP code cannot be empty"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            try {
                val result = repository.getCurrentWeatherByZipCode(
                    zipCode = _zipCode.value.trim(),
                    countryCode = _zipCountryCode.value.trim().ifBlank { null }
                )
                handleWeatherResult(result)
            } catch (e: Exception) {
                Timber.e(e, "Error getting weather by ZIP code")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get weather data: ${e.message}"
                )
            }
        }
    }
    
    fun getWeatherByCityId() {
        val id = _cityId.value.toIntOrNull()
        
        if (id == null) {
            _uiState.value = _uiState.value.copy(
                error = "Please enter a valid city ID"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            
            try {
                val result = repository.getCurrentWeatherByCityId(id)
                handleWeatherResult(result)
            } catch (e: Exception) {
                Timber.e(e, "Error getting weather by city ID")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get weather data: ${e.message}"
                )
            }
        }
    }
    
    private fun handleWeatherResult(result: Result<CurrentWeatherResponse>) {
        when (result) {
            is Result.Success -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    weatherData = result.data,
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
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearWeatherData() {
        _uiState.value = _uiState.value.copy(weatherData = null)
    }
}

data class CurrentWeatherUiState(
    val isLoading: Boolean = false,
    val weatherData: CurrentWeatherResponse? = null,
    val error: String? = null
)