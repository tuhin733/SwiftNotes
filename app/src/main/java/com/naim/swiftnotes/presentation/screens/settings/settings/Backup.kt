package com.naim.swiftnotes.presentation.screens.settings.settings

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.EnhancedEncryption
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.ImportExport
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.naim.swiftnotes.R
import com.naim.swiftnotes.core.constant.DatabaseConst
import com.naim.swiftnotes.presentation.screens.edit.components.CustomTextField
import com.naim.swiftnotes.presentation.screens.settings.SettingsScaffold
import com.naim.swiftnotes.presentation.screens.settings.model.SettingsViewModel
import com.naim.swiftnotes.presentation.screens.settings.widgets.ActionType
import com.naim.swiftnotes.presentation.screens.settings.widgets.SettingsBox
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CloudScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current

    val exportBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/.zip"),
        onResult = { uri ->
            if (uri != null) settingsViewModel.onExportBackup(uri, context)
        }
    )
    val importBackupLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument(),
    onResult = { uri ->
        if (uri != null) settingsViewModel.onImportBackup(uri, context)
        }
    )

    val importFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris ->
            settingsViewModel.onImportFiles(uris, context)
        }
    )

    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.backup),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = stringResource(id = R.string.encrypt_databse),
                    description = stringResource(id = R.string.encrypt_databse_description),
                    icon = Icons.Rounded.EnhancedEncryption,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    variable = settingsViewModel.settings.value.encryptBackup,
                    actionType = ActionType.SWITCH,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(encryptBackup = it)) }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.backup),
                    description = stringResource(id = R.string.backup_description),
                    icon = Icons.Rounded.Backup,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isFirst = true),
                    actionType = ActionType.CUSTOM,
                    customAction = {onExit ->
                        if (settingsViewModel.settings.value.encryptBackup) {
                            PasswordPrompt(
                                icon = Icons.Rounded.Backup,
                                title = stringResource(id = R.string.backup),
                                context = context,
                                settingsViewModel = settingsViewModel,
                                onExit = { password ->
                                    if (password != null) {
                                        settingsViewModel.password = password.text
                                    }
                                    onExit()
                                },
                                onBackup = {
                                    exportBackupLauncher.launch("${DatabaseConst.NOTES_DATABASE_BACKUP_NAME}-${currentDateTime()}.zip")
                                }
                            )
                        }
                        else {
                            LaunchedEffect(true) {
                                exportBackupLauncher.launch("${DatabaseConst.NOTES_DATABASE_BACKUP_NAME}-${currentDateTime()}.zip")
                            }
                        }
                    }
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.restore),
                    description = stringResource(id = R.string.restore_description),
                    icon = Icons.Rounded.ImportExport,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                    actionType = ActionType.CUSTOM,
                    customAction = { onExit ->
                        if (settingsViewModel.settings.value.encryptBackup) {
                            PasswordPrompt(
                                icon = Icons.Rounded.ImportExport,
                                title = stringResource(id = R.string.restore),
                                context = context,
                                settingsViewModel = settingsViewModel,
                                onExit = { password ->
                                    if (password != null) {
                                        settingsViewModel.password = password.text
                                    }
                                    onExit()
                                },
                                onBackup = {
                                    importBackupLauncher.launch(arrayOf("application/zip"))
                                }
                            )
                        }
                        else {
                            LaunchedEffect(true) {
                                settingsViewModel.password = null
                                importBackupLauncher.launch(arrayOf("application/zip"))
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = context.getString(R.string.file_import_title),
                    description = context.getString(R.string.file_import_description),
                    icon = Icons.Rounded.FileOpen,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    actionType = ActionType.CUSTOM,
                    customAction = {
                        LaunchedEffect(true) {
                            importFileLauncher.launch(arrayOf("text/*"))
                        }
                    }
                )
            }
        }
    }
}

fun currentDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("MM-dd-HH-mm-ms")
    val formattedDateTime = currentDateTime.format(formatter)

    return formattedDateTime
}

@Composable
fun PasswordPrompt(
    context: Context,
    icon: ImageVector = Icons.Default.Lock, // New: Icon parameter
    title: String = "Vault", // New: Title parameter
    settingsViewModel: SettingsViewModel,
    onExit: (TextFieldValue?) -> Unit,
    onBackup: () -> Unit = {}
) {
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isPasswordVisible by remember { mutableStateOf(false) } // For eye icon toggle

    Dialog(
        onDismissRequest = { onExit(null) },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        LazyColumn{
            item {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.3f)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            shape = shapeManager(
                                isBoth = true,
                                radius = settingsViewModel.settings.value.cornerRadius
                            )
                        )
                ){
                    Spacer(modifier = Modifier.height(16.dp)) // Top padding

                    // Icon and Title
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )

                    // Password Input with Eye Icon Toggle
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                                shape = shapeManager(
                                    isBoth = true,
                                    radius = settingsViewModel.settings.value.cornerRadius
                                )
                            )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CustomTextField(
                                hideContent = !isPasswordVisible,
                                value = password,
                                onValueChange = { password = it },
                                placeholder = stringResource(id = R.string.password_prompt),
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        }
                    }

                    // Button with Password Validation
                    Button(
                        onClick = {
                            if (password.text.isNotBlank()) {
                                onExit(password)
                                onBackup()
                            } else {
                                Toast.makeText(context, R.string.invalid_input, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.End)
                    ) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}


