package com.naim.swiftnotes.presentation.theme

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.naim.swiftnotes.presentation.screens.settings.model.SettingsViewModel


private fun getColorScheme(isDarkTheme: Boolean, isAmoledTheme: Boolean): ColorScheme {
    return if (isDarkTheme) {
        if (isAmoledTheme) {
            darkScheme.copy(surfaceContainerLow = Color.Black, surface = Color.Black)
        } else {
            darkScheme
        }
    } else {
        lightScheme
    }
}

@Composable
fun LeafNotesTheme(
    settingsModel: SettingsViewModel,
    content: @Composable () -> Unit
) {
    if (settingsModel.settings.value.automaticTheme) {
        settingsModel.update(settingsModel.settings.value.copy(darkTheme = isSystemInDarkTheme()))
    }

    val context = LocalContext.current
    val activity = LocalView.current.context as Activity
    WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
        isAppearanceLightStatusBars = !settingsModel.settings.value.darkTheme
    }
    if (settingsModel.settings.value.screenProtection) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    } else {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    MaterialTheme(
        colorScheme = getColorScheme(
            settingsModel.settings.value.darkTheme,
            settingsModel.settings.value.amoledTheme
        ),
        typography = Typography(),
        content = content
    )
}
