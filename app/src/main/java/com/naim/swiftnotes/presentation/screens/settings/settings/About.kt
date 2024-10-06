package com.naim.swiftnotes.presentation.screens.settings.settings

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ContactSupport
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.naim.swiftnotes.R
import com.naim.swiftnotes.core.constant.ConnectionConst
import com.naim.swiftnotes.core.constant.SupportConst.getSupportersMap
import com.naim.swiftnotes.presentation.screens.settings.SettingsScaffold
import com.naim.swiftnotes.presentation.screens.settings.model.SettingsViewModel
import com.naim.swiftnotes.presentation.screens.settings.widgets.ActionType
import com.naim.swiftnotes.presentation.screens.settings.widgets.ListDialog
import com.naim.swiftnotes.presentation.screens.settings.widgets.SettingsBox

@Composable
fun AboutScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.about),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = stringResource(id = R.string.support_list),
                    icon = Icons.Rounded.Coffee,
                    actionType = ActionType.CUSTOM,
                    radius = shapeManager(isBoth = true, radius = settingsViewModel.settings.value.cornerRadius),
                    customAction = { onExit -> ContributorsClicked(context, settingsViewModel = settingsViewModel) { onExit() } }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.build_type),
                    description = settingsViewModel.build,
                    icon = Icons.Rounded.Build,
                    actionType = ActionType.TEXT,
                    radius = shapeManager(isFirst = true, radius = settingsViewModel.settings.value.cornerRadius)
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.version),
                    description = settingsViewModel.version,
                    icon = Icons.Rounded.Info,
                    actionType = ActionType.TEXT,
                    radius = shapeManager(isLast = true, radius = settingsViewModel.settings.value.cornerRadius),
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.latest_release),
                    icon = Icons.Rounded.Verified,
                    actionType = ActionType.LINK,
                    radius = shapeManager(isFirst = true, radius = settingsViewModel.settings.value.cornerRadius),
                    linkClicked = { uriHandler.openUri(ConnectionConst.LATEST_RELEASE) }
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.source_code),
                    icon = Icons.Rounded.Download,
                    actionType = ActionType.LINK,
                    radius = shapeManager(isLast = true, radius = settingsViewModel.settings.value.cornerRadius),
                    linkClicked = { uriHandler.openUri(ConnectionConst.GITHUB_SOURCE_CODE) }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }

            item {
                SettingsBox(
                    title = stringResource(id = R.string.email),
                    icon = Icons.Rounded.Email,
                    clipboardText = ConnectionConst.SUPPORT_MAIL,
                    actionType = ActionType.CLIPBOARD,
                    radius = shapeManager(isFirst = true, radius = settingsViewModel.settings.value.cornerRadius),
                )
            }
            item {
                SettingsBox(
                    isBig = true,
                    title = stringResource(id = R.string.discord),
                    icon = Icons.AutoMirrored.Rounded.ContactSupport,
                    actionType = ActionType.LINK,
                    linkClicked = { uriHandler.openUri(ConnectionConst.SUPPORT_DISCORD) },
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                )
            }
            item {
                SettingsBox(
                    isBig = true,
                    title = stringResource(id = R.string.feature),
                    icon = Icons.Rounded.BugReport,
                    linkClicked = { uriHandler.openUri(ConnectionConst.GITHUB_FEATURE_REQUEST) },
                    actionType = ActionType.LINK,
                    radius = shapeManager(isLast = true, radius = settingsViewModel.settings.value.cornerRadius),
                )
            }

          item {
              Text(
                  text = "Made with ❤ by Naim Sarkar",
                  style = MaterialTheme.typography.bodyMedium,
                  textAlign = TextAlign.Center,
                  modifier = Modifier.padding(30.dp).fillMaxWidth()
              )
          }

        }
    }

}

@Composable
fun ContributorsClicked(
    context: Context,
    settingsViewModel: SettingsViewModel,
    onExit: () -> Unit
) {
    fun contributors(context: Context): List<Pair<String, String>> {
        val map = getSupportersMap(context)
        return map.flatMap { (role, supporters) ->
            supporters.map { supporter -> Pair(supporter, role) }
        }
    }

    ListDialog(
        text = stringResource(R.string.support_list),
        list = contributors(context),
        settingsViewModel = settingsViewModel,
        onExit = onExit,
        extractDisplayData = { it }
    ) { isFirstItem, isLastItem, displayData ->
        SettingsBox(
            title = displayData.first,
            description = displayData.second,
            radius = shapeManager(isFirst = isFirstItem, isLast = isLastItem, radius = settingsViewModel.settings.value.cornerRadius),
            actionType = ActionType.TEXT,
            customText = "❤"
        )
    }
}


