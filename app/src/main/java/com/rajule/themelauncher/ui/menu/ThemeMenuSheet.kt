package com.rajule.themelauncher.ui.menu

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rajule.themelauncher.data.ThemeCatalog
import com.rajule.themelauncher.model.ThemeDefinition
import com.rajule.themelauncher.model.ThemePalette
import com.rajule.themelauncher.util.LauncherRole
import com.rajule.themelauncher.util.WallpaperStore

@Composable
fun ThemeMenuSheet(
    currentThemeId: String,
    wallpaperStore: WallpaperStore,
    onSelectTheme: (String) -> Unit,
    onWallpaperChanged: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? android.app.Activity

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            wallpaperStore.importFromUri(currentThemeId, uri)
            onWallpaperChanged()
        }
    }

    val current = remember(currentThemeId) { ThemeCatalog.byId(currentThemeId) }
    val palette = current.palette

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .background(palette.background)
                .padding(20.dp)
        ) {
            Text("Themes", color = palette.onSurface, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(14.dp))

            LazyColumn(Modifier.weight(1f)) {
                items(ThemeCatalog.all, key = { it.id }) { theme ->
                    ThemeRow(
                        theme = theme,
                        selected = theme.id == currentThemeId,
                        onClick = { onSelectTheme(theme.id) }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            MenuAction(text = "Change wallpaper for ${current.displayName}", palette = palette) {
                pickImage.launch("image/*")
            }
            Spacer(Modifier.height(8.dp))
            MenuAction(text = "Remove custom wallpaper", palette = palette) {
                wallpaperStore.clear(currentThemeId)
                onWallpaperChanged()
            }
            Spacer(Modifier.height(8.dp))
            MenuAction(text = if (LauncherRole.isDefaultLauncher(context)) "Default launcher ✓" else "Set as default launcher", palette = palette) {
                activity?.let { LauncherRole.requestDefaultLauncher(it) }
            }
            Spacer(Modifier.height(8.dp))
            MenuAction(text = "Close", palette = palette, onClick = onDismiss)
        }
    }
}

@Composable
private fun ThemeRow(theme: ThemeDefinition, selected: Boolean, onClick: () -> Unit) {
    val palette = theme.palette
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) palette.accent.copy(alpha = 0.25f) else palette.surface.copy(alpha = 0.35f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Brush.linearGradient(listOf(palette.gradientTop, palette.gradientBottom)))
        )
        Spacer(Modifier.width(14.dp))
        Text(theme.displayName, color = palette.onSurface, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun MenuAction(text: String, palette: ThemePalette, onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(palette.surface.copy(alpha = 0.45f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(text, color = palette.onSurface)
    }
}
