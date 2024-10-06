package com.naim.swiftnotes.presentation.screens.settings.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.naim.swiftnotes.R
import com.naim.swiftnotes.presentation.screens.settings.SettingsScaffold
import com.naim.swiftnotes.presentation.screens.settings.model.SettingsViewModel
import com.naim.swiftnotes.presentation.screens.settings.widgets.ActionType
import com.naim.swiftnotes.presentation.screens.settings.widgets.SettingsBox


@Composable
fun ToolsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    settingsViewModel.noteUseCase.observe()
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.tools),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = stringResource(id = R.string.notes),
                    description = settingsViewModel.noteUseCase.notes.size.toString(),
                    icon = Icons.Rounded.Build,
                    actionType = ActionType.TEXT,
                    radius = shapeManager(isBoth = true, radius = settingsViewModel.settings.value.cornerRadius)
                )
            }
        }
    }
}