package com.naim.swiftnotes.presentation.components.markdown

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AudioFile
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.naim.swiftnotes.domain.model.Note
import com.naim.swiftnotes.presentation.screens.settings.model.SettingsViewModel
import kotlinx.coroutines.delay

@Composable
fun AudioPlayer(uri: String) {
    var isVisible by remember { mutableStateOf(true) } // Controls visibility of the player

    if (!isVisible) return // If not visible, exit from composable
    val mediaPlayer = remember { MediaPlayer() }
    val isPlaying = remember { mutableStateOf(false) }
    val currentProgress = remember { mutableFloatStateOf(0f) }
    val currentTime = remember { mutableIntStateOf(0) }
    val duration = remember { mutableIntStateOf(0) }

    DisposableEffect(Unit) {
        mediaPlayer.setDataSource(uri)
        mediaPlayer.prepare()

        // Set up onCompletion listener to reset player state
        mediaPlayer.setOnCompletionListener {
            isPlaying.value = false
            currentTime.intValue = 0
            currentProgress.floatValue = 0f
        }

        onDispose { mediaPlayer.release() }
    }

    // Update progress every second if playing
    LaunchedEffect(isPlaying.value) {
        if (isPlaying.value) {
            duration.intValue = mediaPlayer.duration
            while (mediaPlayer.isPlaying) {
                currentTime.intValue = mediaPlayer.currentPosition
                currentProgress.floatValue = mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration
                delay(1000) // Update every second
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium.copy(CornerSize(10.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            // Audio File Icon
            Icon(
                imageVector = Icons.Rounded.AudioFile,
                contentDescription = "Audio File Icon",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // Play/Pause Button
            IconButton(onClick = {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    isPlaying.value = false
                } else {
                    mediaPlayer.start()
                    isPlaying.value = true
                }
            }) {
                Icon(
                    imageVector = if (isPlaying.value) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = if (isPlaying.value) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = { currentProgress.floatValue },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(2f)
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            )

            // Current Time
            Text(
                text = formatTime(currentTime.intValue),
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            IconButton(
                onClick = {
                    mediaPlayer.stop() // Stop playback on close
                    isVisible = false // Hide the AudioPlayer
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

private fun formatTime(milliseconds: Int): String {
    val totalSeconds = milliseconds / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d.%d s", minutes, seconds)
}
