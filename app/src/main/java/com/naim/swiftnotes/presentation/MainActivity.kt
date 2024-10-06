package com.naim.swiftnotes.presentation

import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.naim.swiftnotes.data.repository.SettingsRepositoryImpl
import com.naim.swiftnotes.presentation.navigation.AppNavHost
import com.naim.swiftnotes.presentation.screens.settings.model.SettingsViewModel
import com.naim.swiftnotes.presentation.theme.LeafNotesTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var settingsRepositoryImpl: SettingsRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
            val noteId = intent?.getIntExtra("noteId", -1) ?: -1

            if (settingsViewModel.settings.value.gallerySync) {
                contentResolver.registerContentObserver(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    true,
                    settingsViewModel.galleryObserver
                )
            }

            LeafNotesTheme(settingsViewModel) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    AppNavHost(settingsViewModel, noteId = noteId)
                }
            }
        }
    }
}
