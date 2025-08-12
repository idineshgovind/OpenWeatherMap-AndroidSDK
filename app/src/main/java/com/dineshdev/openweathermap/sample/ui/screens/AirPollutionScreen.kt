package com.dineshdev.openweathermap.sample.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dineshdev.openweathermap.sample.R

@Composable
fun AirPollutionScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.air_pollution_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Air Pollution features coming soon...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}