package com.naim.swiftnotes.presentation.screens.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.naim.swiftnotes.presentation.screens.settings.SettingsScaffold
import com.naim.swiftnotes.presentation.screens.settings.model.SettingsViewModel
import com.naim.swiftnotes.presentation.screens.settings.settings.shapeManager

@Composable
fun UserGuideScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val scrollState = rememberScrollState()
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = "User Guide",
        onBackNavClicked = {navController.popBackStack()}
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        shapeManager(
                            isBoth = true,
                            radius = settingsViewModel.settings.value.cornerRadius
                        )
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f)
                    )
                    .padding(1.dp)
                    .semantics {
                        contentDescription = "Terms"
                    }
            ) {
                Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
                    SectionTitle("1. List and Text Features")
                    FeatureItem("Bullet List", "Inserts a bullet list item using - ")
                    FeatureItem("Label", "Inserts [LABEL] for tagging and organizing notes.")
                    FeatureItem("Checkbox", "Adds a checklist item [ ]")
                    FeatureItem("Insert Image", "Opens the file picker to insert an image.")

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionTitle("2. Text Formatting")
                    FeatureItem("Bold", "Inserts **** Place the text inside the two asterisks to make it bold.")
                    FeatureItem("Header", "Adds a #  symbol to mark text as a header.")
                    FeatureItem("Italic", "Inserts ** Place the text inside the asterisks for italics.")

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionTitle("3. Links and Special Formatting")
                    FeatureItem("Link", "Inserts [text](url) Replace text with the link text and url with the actual URL.")
                    FeatureItem("Strikethrough", "Adds ~~~~ Place the text inside the tildes to strike it through.")
                    FeatureItem("Highlight", "Inserts ==== Place the text inside these markers to highlight it.")

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionTitle("4. Code and Quotes")
                    FeatureItem("Code Block", "Inserts a code block template:\n```\nInsert your code here\n```.")
                    FeatureItem("Underline", "Adds __ Place the text inside the underscores to underline it.")
                    FeatureItem("Quote", "Inserts > to format a blockquote.")

                    Spacer(modifier = Modifier.height(24.dp))

                    SectionTitle("Usage Tips")
                    FeatureItem("Cursor Placement", "Features like bold and italic automatically place the cursor inside the symbols for convenience.")
                    FeatureItem("Images", "The *Insert Image* option opens a file picker to easily insert images into your notes.")
                    FeatureItem("Navigation", "Use the arrows to move between toolbar sections efficiently.")
                    FeatureItem("Header", "You use six type of header # = h1, ## = h2, ### = h3, #### = h4, ##### = h5, ###### = h6.")
                    FeatureItem("Label", "you can use special labels [LABEL] Important, [LABEL] To-Do, [LABEL] Reminders, [LABEL] Idea.")
                }
            }
        }
    }
}




@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun FeatureItem(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = title, fontWeight = FontWeight.Medium, fontSize = 18.sp)
        Text(text = description, fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp))
    }
}