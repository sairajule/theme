package com.rajule.themelauncher.ui.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.core.graphics.drawable.toBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rajule.themelauncher.data.AppResolver
import com.rajule.themelauncher.data.InstalledApp
import com.rajule.themelauncher.model.ThemePalette

@Composable
fun AppDrawerScreen(appResolver: AppResolver, palette: ThemePalette, onDismiss: () -> Unit) {
    val apps = remember { appResolver.listLaunchableApps() }
    var query by remember { mutableStateOf("") }
    val filtered = remember(query, apps) {
        if (query.isBlank()) apps else apps.filter { it.label.contains(query, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            Modifier
                .fillMaxSize()
                .background(palette.background)
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search apps") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = palette.onSurface,
                    unfocusedTextColor = palette.onSurface,
                    focusedBorderColor = palette.accent,
                    unfocusedBorderColor = palette.onSurfaceMuted,
                    cursorColor = palette.accent
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            LazyColumn(Modifier.fillMaxSize()) {
                items(filtered, key = { it.packageName }) { app ->
                    AppRow(app = app, palette = palette, onClick = {
                        appResolver.launchPackage(app.packageName)
                        onDismiss()
                    })
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun AppRow(app: InstalledApp, palette: ThemePalette, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(palette.surface.copy(alpha = 0.4f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val bitmap = remember(app.packageName) { app.icon.toBitmap(96, 96) }
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.size(36.dp))
        Spacer(Modifier.width(14.dp))
        Text(app.label, color = palette.onSurface, fontWeight = FontWeight.Medium)
    }
    Spacer(Modifier.height(6.dp))
}
