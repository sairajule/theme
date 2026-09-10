package com.rajule.themelauncher.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rajule.themelauncher.data.AppResolver
import com.rajule.themelauncher.data.PrefsRepository
import com.rajule.themelauncher.data.ThemeCatalog
import com.rajule.themelauncher.model.*
import com.rajule.themelauncher.ui.components.*
import com.rajule.themelauncher.ui.drawer.AppDrawerScreen
import com.rajule.themelauncher.ui.icons.Glyph
import com.rajule.themelauncher.ui.menu.ThemeMenuSheet
import com.rajule.themelauncher.ui.picker.AppPickerSheet
import com.rajule.themelauncher.util.WallpaperStore
import com.rajule.themelauncher.util.rememberBatteryPercent
import com.rajule.themelauncher.util.rememberNow
import kotlinx.coroutines.launch

@Composable
fun ThemeLauncherRoot() {
    val context = LocalContext.current
    val prefsRepo = remember { PrefsRepository(context) }
    val appResolver = remember { AppResolver(context) }
    val wallpaperStore = remember { WallpaperStore(context) }
    val scope = rememberCoroutineScope()

    val selectedThemeId by prefsRepo.selectedThemeId.collectAsState(initial = ThemeCatalog.forest.id)
    val theme = remember(selectedThemeId) { ThemeCatalog.byId(selectedThemeId) }

    var showAppDrawer by remember { mutableStateOf(false) }
    var showThemeMenu by remember { mutableStateOf(false) }
    var pickerTileId by remember { mutableStateOf<String?>(null) }
    var wallpaperVersion by remember { mutableStateOf(0) }

    Box(Modifier.fillMaxSize()) {
        HomeScreen(
            theme = theme,
            prefsRepo = prefsRepo,
            appResolver = appResolver,
            wallpaperStore = wallpaperStore,
            wallpaperVersion = wallpaperVersion,
            onOpenDrawer = { showAppDrawer = true },
            onOpenMenu = { showThemeMenu = true },
            onPickAppFor = { tileId -> pickerTileId = tileId }
        )

        if (showAppDrawer) {
            AppDrawerScreen(appResolver = appResolver, palette = theme.palette, onDismiss = { showAppDrawer = false })
        }

        if (showThemeMenu) {
            ThemeMenuSheet(
                currentThemeId = theme.id,
                wallpaperStore = wallpaperStore,
                onSelectTheme = { id -> scope.launch { prefsRepo.setSelectedTheme(id) } },
                onWallpaperChanged = { wallpaperVersion++ },
                onDismiss = { showThemeMenu = false }
            )
        }

        pickerTileId?.let { tileId ->
            AppPickerSheet(
                appResolver = appResolver,
                onPick = { pkg ->
                    scope.launch { prefsRepo.setBinding(theme.id, tileId, pkg) }
                    pickerTileId = null
                },
                onDismiss = { pickerTileId = null }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    theme: ThemeDefinition,
    prefsRepo: PrefsRepository,
    appResolver: AppResolver,
    wallpaperStore: WallpaperStore,
    wallpaperVersion: Int,
    onOpenDrawer: () -> Unit,
    onOpenMenu: () -> Unit,
    onPickAppFor: (String) -> Unit
) {
    val palette = theme.palette
    val now by rememberNow()
    val battery by rememberBatteryPercent()
    val pagerState = rememberPagerState(pageCount = { theme.pages.size })
    val wallpaper = remember(theme.id, wallpaperVersion) { wallpaperStore.bitmapFor(theme.id) }

    Box(Modifier.fillMaxSize().background(palette.background)) {
        if (wallpaper != null) {
            Image(
                bitmap = wallpaper.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(palette.gradientTop, palette.gradientBottom)))
            )
        }
        // subtle scrim so light-content tiles/icons stay legible over any wallpaper
        Box(Modifier.fillMaxSize().background(palette.background.copy(alpha = 0.18f)))

        Column(Modifier.fillMaxSize()) {
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { pageIndex ->
                FreeformGrid(
                    items = theme.pages[pageIndex],
                    columns = GRID_COLUMNS,
                    rows = GRID_ROWS,
                    modifier = Modifier.fillMaxSize()
                ) { item ->
                    RenderTile(
                        item = item,
                        theme = theme,
                        now = now,
                        battery = battery,
                        prefsRepo = prefsRepo,
                        appResolver = appResolver,
                        onPickAppFor = onPickAppFor
                    )
                }
            }

            PageIndicator(count = theme.pages.size, current = pagerState.currentPage)
            Dock(theme = theme, appResolver = appResolver, prefsRepo = prefsRepo, onOpenDrawer = onOpenDrawer, onPickAppFor = onPickAppFor)
        }

        Box(
            Modifier
                .padding(top = 18.dp, end = 14.dp)
                .align(Alignment.TopEnd)
                .size(34.dp)
                .clip(CircleShape)
                .background(palette.surface.copy(alpha = 0.5f))
        ) {
            androidx.compose.material3.IconButton(onClick = onOpenMenu, modifier = Modifier.fillMaxSize()) {
                Glyph(GlyphId.SETTINGS, palette.accent, Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun PageIndicator(count: Int, current: Int) {
    if (count <= 1) return
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(count) { index ->
            Box(
                Modifier
                    .padding(horizontal = 3.dp)
                    .size(if (index == current) 7.dp else 5.dp)
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color.White.copy(alpha = if (index == current) 0.9f else 0.4f))
            )
        }
    }
}

@Composable
private fun RenderTile(
    item: GridItem,
    theme: ThemeDefinition,
    now: java.time.LocalDateTime,
    battery: Int,
    prefsRepo: PrefsRepository,
    appResolver: AppResolver,
    onPickAppFor: (String) -> Unit
) {
    val palette = theme.palette
    when (val content = item.content) {
        is TileContent.Clock -> ClockTileView(now, palette, Modifier.fillMaxSize())
        is TileContent.Calendar -> CalendarTileView(now, palette, Modifier.fillMaxSize())
        is TileContent.Quote -> QuoteTileView(content.text, palette, Modifier.fillMaxSize())
        is TileContent.Battery -> BatteryTileView(battery, palette, Modifier.fillMaxSize())
        is TileContent.Weather -> WeatherTileView(palette, Modifier.fillMaxSize())
        is TileContent.Photo -> PhotoTileView(content.assetName, palette, Modifier.fillMaxSize())
        is TileContent.AppShortcut -> {
            val bound by prefsRepo.binding(theme.id, item.id).collectAsState(initial = null)
            val scope = rememberCoroutineScope()
            AppShortcutTileView(
                role = content.role,
                label = content.label,
                palette = palette,
                showLabel = true,
                onClick = {
                    val target = bound?.takeIf { appResolver.isInstalled(it) }
                        ?: appResolver.autoResolve(content.role)
                    if (target != null) {
                        if (bound == null) scope.launch { prefsRepo.setBinding(theme.id, item.id, target) }
                        appResolver.launchPackage(target)
                    } else {
                        onPickAppFor(item.id)
                    }
                },
                onLongClick = { onPickAppFor(item.id) },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun Dock(
    theme: ThemeDefinition,
    appResolver: AppResolver,
    prefsRepo: PrefsRepository,
    onOpenDrawer: () -> Unit,
    onPickAppFor: (String) -> Unit
) {
    val palette = theme.palette
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(28.dp))
            .background(palette.surface.copy(alpha = 0.55f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        theme.dock.forEachIndexed { index, role ->
            val tileId = "dock_$index"
            val bound by prefsRepo.binding(theme.id, tileId).collectAsState(initial = null)
            val scope = rememberCoroutineScope()
            Box(
                Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(palette.surfaceVariant.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.IconButton(
                    onClick = {
                        val target = bound?.takeIf { appResolver.isInstalled(it) } ?: appResolver.autoResolve(role)
                        if (target != null) {
                            if (bound == null) scope.launch { prefsRepo.setBinding(theme.id, tileId, target) }
                            appResolver.launchPackage(target)
                        } else {
                            onPickAppFor(tileId)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Glyph(role.glyph, palette.accent, Modifier.size(22.dp))
                }
            }
        }

        Box(
            Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(palette.accent.copy(alpha = 0.85f)),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.IconButton(onClick = onOpenDrawer, modifier = Modifier.fillMaxSize()) {
                Glyph(GlyphId.GRID, palette.background, Modifier.size(20.dp))
            }
        }
    }
}
