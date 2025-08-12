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
fun OneCallScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.one_call_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "One Call API features coming soon...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}