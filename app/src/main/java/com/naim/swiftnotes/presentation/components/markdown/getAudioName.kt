package com.naim.swiftnotes.presentation.components.markdown


import android.content.Context
import android.net.Uri

fun getAudioName(context: Context, uri: Uri?): String {
    val extension = uri?.let {
        val mimeType = context.contentResolver.getType(it)
        mimeType?.let { type ->
            when {
                type.contains("audio/mpeg") -> ".mp3"
                type.contains("audio/wav") -> ".wav"
                type.contains("audio/ogg") -> ".ogg"
                type.contains("audio/mp4") -> ".m4a"
                else -> ".audio" // Default extension if unknown
            }
        }
    } ?: ".audio"

    return (System.currentTimeMillis()).toString() + extension // Unique name based on current time
}
