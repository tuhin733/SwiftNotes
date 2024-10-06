package com.naim.swiftnotes.presentation.screens.home.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naim.swiftnotes.R
import com.naim.swiftnotes.domain.model.Note
import com.naim.swiftnotes.presentation.components.markdown.MarkdownText
import com.naim.swiftnotes.presentation.screens.settings.model.SettingsViewModel
import coil.compose.AsyncImage
import com.naim.swiftnotes.presentation.components.markdown.ImageInsertion
import com.naim.swiftnotes.presentation.components.markdown.MarkdownElement

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    settingsViewModel: SettingsViewModel,
    containerColor: Color,
    note: Note,
    isBorderEnabled: Boolean,
    shape: RoundedCornerShape,
    onShortClick: () -> Unit,
    onLongClick: () -> Unit,
    onNoteUpdate: (Note) -> Unit
) {

    fun removeImagesFromMarkdown(markdown: String): String {
        // Regex to remove the image markdown syntax !()
        val imagePattern = Regex("!\\((.*?)\\)")
        return markdown.replace(imagePattern, "") // Remove image URIs
    }


    fun parseMarkdown(description: String): List<MarkdownElement> {
        val elements = mutableListOf<MarkdownElement>()

        // Simulate finding images in markdown (you can improve this with a real parser)
        val imagePattern = Regex("!\\((.*?)\\)")  // Matches !(imageUri)
        imagePattern.findAll(description).forEach { matchResult ->
            elements.add(ImageInsertion(photoUri = matchResult.groupValues[1]))
        }

        // Add more parsing for other MarkdownElement types (Heading, Text, etc.)
        return elements
    }


    val borderModifier = when {
        isBorderEnabled -> {
            Modifier.border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = shape
            )
        }
        containerColor != Color.Black -> {
            Modifier.border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = shape
            )
        }
        else -> {
            Modifier.border(
                width = 0.5.dp,
                color = if (isSystemInDarkTheme()) {
                    Color(0xFFE0E0E0).copy(alpha = 0.24f) // Dark mode border
                } else {
                    Color(0xFFE0E0E0) // Light mode border
                },
                shape = shape
            )
        }
    }

    ElevatedCard(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .clip(shape)
            .combinedClickable(
                onClick = { onShortClick() },
                onLongClick = { onLongClick() }
            )
            .then(borderModifier),       // Conditional border
        elevation = CardDefaults.cardElevation(defaultElevation = if (containerColor != Color.Black) 6.dp else 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 12.dp)
        ) {
            // Parse the description into markdown elements, including ImageInsertion
            val markdownElements = parseMarkdown(note.description)

            // Find the first ImageInsertion in the markdown elements
            val imageInsertion = markdownElements.filterIsInstance<ImageInsertion>().firstOrNull()

            // Display the image at the top if present
            imageInsertion?.let {
                AsyncImage(
                    model = it.photoUri,  // Get the URI from ImageInsertion
                    contentDescription = "Note Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)  // Set the desired height for the image
                        .clip(shape),
                    contentScale = ContentScale.Crop  // Crop the image to fit nicely
                )
                Spacer(modifier = Modifier.height(8.dp))  // Add spacing between image and title
            }

            // Display title
            if (note.name.isNotBlank()) {
                MarkdownText(
                    isPreview = true,
                    isEnabled = settingsViewModel.settings.value.isMarkdownEnabled,
                    markdown = note.name.replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .heightIn(max = dimensionResource(R.dimen.max_name_height))
                        .then(
                            if (note.description.isNotBlank() && !settingsViewModel.settings.value.showOnlyTitle) {
                                Modifier.padding(bottom = 9.dp)
                            } else {
                                Modifier
                            }
                        ),
                    weight = FontWeight.Bold,
                    spacing = 0.dp,
                    onContentChange = { onNoteUpdate(note.copy(name = it)) },
                    fontSize = 16.sp,
                    radius = settingsViewModel.settings.value.cornerRadius
                )
            }

            // Display note text (description)
            if (note.description.isNotBlank() && !settingsViewModel.settings.value.showOnlyTitle) {
                // Remove ImageInsertion elements from the description
                val filteredDescription = removeImagesFromMarkdown(note.description)

                MarkdownText(
                    isPreview = true,
                    markdown = filteredDescription,
                    isEnabled = settingsViewModel.settings.value.isMarkdownEnabled,
                    spacing = 0.dp,
                    modifier = Modifier
                        .heightIn(max = dimensionResource(R.dimen.max_description_height)),
                    onContentChange = { onNoteUpdate(note.copy(description = it)) },
                    fontSize = 14.sp,
                    radius = settingsViewModel.settings.value.cornerRadius
                )
            }

        }
    }
}

