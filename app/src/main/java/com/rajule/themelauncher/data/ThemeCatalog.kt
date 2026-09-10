package com.rajule.themelauncher.data

import androidx.compose.ui.graphics.Color
import com.rajule.themelauncher.model.*

/** Packs tiles into rows so no two overlap; each row-block shares one rowSpan. */
private class GridBuilder(private val pagePrefix: String) {
    private var cursorRow = 0
    private val items = mutableListOf<GridItem>()

    fun block(rowSpan: Int = 1, vararg cells: Pair<Int, TileContent>): GridBuilder {
        var col = 0
        for ((colSpan, content) in cells) {
            items += GridItem(
                id = "$pagePrefix-${items.size}",
                slot = GridSlot(col, cursorRow, colSpan, rowSpan),
                content = content
            )
            col += colSpan
        }
        cursorRow += rowSpan
        return this
    }

    fun build(): List<GridItem> = items.toList()
}

private fun buildPages(
    firstWidget: TileContent,
    secondWidget: TileContent,
    secondQuote: TileContent.Quote,
    page1Apps: List<Pair<AppRole, String>>,
    page2Apps: List<Pair<AppRole, String>>,
    photoNames: List<String>
): List<List<GridItem>> {
    fun app(list: List<Pair<AppRole, String>>, index: Int): TileContent =
        TileContent.AppShortcut(list[index].first, list[index].second)

    val page1 = GridBuilder("p0")
        .block(2, 4 to TileContent.Clock)
        .block(2, 2 to firstWidget, 2 to TileContent.Battery)
        .block(1, 1 to app(page1Apps, 0), 1 to app(page1Apps, 1), 2 to TileContent.Photo(photoNames[0]))
        .block(1, 1 to app(page1Apps, 2), 1 to app(page1Apps, 3), 2 to TileContent.Weather)
        .block(1, 1 to app(page1Apps, 4), 1 to app(page1Apps, 5), 1 to app(page1Apps, 6), 1 to app(page1Apps, 7))
        .build()

    val page2 = GridBuilder("p1")
        .block(2, 2 to secondWidget, 2 to TileContent.Photo(photoNames[1]))
        .block(1, 1 to app(page2Apps, 0), 1 to app(page2Apps, 1), 1 to app(page2Apps, 2), 1 to app(page2Apps, 3))
        .block(2, 2 to TileContent.Photo(photoNames[2]), 2 to TileContent.Photo(photoNames[3]))
        .block(1, 1 to app(page2Apps, 4), 1 to app(page2Apps, 5), 1 to app(page2Apps, 6), 1 to app(page2Apps, 7))
        .block(1, 4 to secondQuote)
        .build()

    return listOf(page1, page2)
}

object ThemeCatalog {

    val forest = ThemeDefinition(
        id = "forest",
        displayName = "Forest",
        palette = ThemePalette(
            background = Color(0xFF0F1A11),
            surface = Color(0xFF1E2E1F),
            surfaceVariant = Color(0xFF16220F),
            onSurface = Color(0xFFEFF5E9),
            onSurfaceMuted = Color(0xFFB9C9AF),
            accent = Color(0xFFCFE8C9),
            gradientTop = Color(0xFF23392A),
            gradientBottom = Color(0xFF0A100B)
        ),
        pages = buildPages(
            firstWidget = TileContent.Quote("Even if you don't realize it, you are moving forward."),
            secondWidget = TileContent.Calendar,
            secondQuote = TileContent.Quote("Slow down, the forest isn't in a hurry."),
            page1Apps = listOf<Pair<AppRole, String>>(
                AppRole.SOCIAL to "Pinterest", AppRole.CHAT to "Messenger",
                AppRole.CLOCK to "Alarm", AppRole.EMAIL to "Gmail",
                AppRole.PHONE to "Phone", AppRole.BROWSER to "Browser", AppRole.MUSIC to "Music", AppRole.NOTES to "Notes"
            ),
            page2Apps = listOf<Pair<AppRole, String>>(
                AppRole.VIDEO to "YouTube", AppRole.MAPS to "Maps", AppRole.STORE to "Store", AppRole.SETTINGS to "Settings",
                AppRole.CALCULATOR to "Calculator", AppRole.CLOUD to "Cloud", AppRole.CAMERA to "Camera", AppRole.GALLERY to "Gallery"
            ),
            photoNames = listOf<String>("forest_a", "forest_b", "forest_c", "forest_d")
        ),
        dock = listOf(AppRole.CHAT, AppRole.CAMERA, AppRole.CLOUD, AppRole.LOCK)
    )

    val mono = ThemeDefinition(
        id = "mono",
        displayName = "Monochrome",
        palette = ThemePalette(
            background = Color(0xFF17171A),
            surface = Color(0xFF2A2A2E),
            surfaceVariant = Color(0xFF1F1F22),
            onSurface = Color(0xFFF2EFE6),
            onSurfaceMuted = Color(0xFFB8B5AC),
            accent = Color(0xFFE8E4DA),
            gradientTop = Color(0xFF3A3A3E),
            gradientBottom = Color(0xFF0A0A0C)
        ),
        pages = buildPages(
            firstWidget = TileContent.Calendar,
            secondWidget = TileContent.Quote("Some days are just black and white."),
            secondQuote = TileContent.Quote("Draw your own outline."),
            page1Apps = listOf<Pair<AppRole, String>>(
                AppRole.NOTES to "Notes", AppRole.MAPS to "Maps",
                AppRole.STORE to "Store", AppRole.BROWSER to "Chrome",
                AppRole.EMAIL to "Gmail", AppRole.CHAT to "Messages", AppRole.SETTINGS to "Settings", AppRole.PHONE to "Phone"
            ),
            page2Apps = listOf<Pair<AppRole, String>>(
                AppRole.CLOCK to "Clock", AppRole.VIDEO to "YT Music", AppRole.SOCIAL to "Google", AppRole.CALCULATOR to "Calc",
                AppRole.CHAT to "WhatsApp", AppRole.CLOUD to "Drive", AppRole.CAMERA to "Photos", AppRole.SOCIAL to "LinkedIn"
            ),
            photoNames = listOf<String>("mono_a", "mono_b", "mono_c", "mono_d")
        ),
        dock = listOf(AppRole.CHAT, AppRole.SOCIAL, AppRole.CAMERA, AppRole.LOCK)
    )

    val teal = ThemeDefinition(
        id = "teal",
        displayName = "Misty Teal",
        palette = ThemePalette(
            background = Color(0xFF0E1B18),
            surface = Color(0xFF20423A),
            surfaceVariant = Color(0xFF16302A),
            onSurface = Color(0xFFE3F3EE),
            onSurfaceMuted = Color(0xFFA9C9C0),
            accent = Color(0xFFBFE3D6),
            gradientTop = Color(0xFF294f47),
            gradientBottom = Color(0xFF07100E)
        ),
        pages = buildPages(
            firstWidget = TileContent.Quote("You're in the best of me."),
            secondWidget = TileContent.Calendar,
            secondQuote = TileContent.Quote("Even lost in the fog, keep walking."),
            page1Apps = listOf<Pair<AppRole, String>>(
                AppRole.MUSIC to "Music", AppRole.CHAT to "WhatsApp",
                AppRole.SOCIAL to "Instagram", AppRole.BROWSER to "Browser",
                AppRole.CAMERA to "Camera", AppRole.PHONE to "Phone", AppRole.MAPS to "Maps", AppRole.CLOUD to "Cloud"
            ),
            page2Apps = listOf<Pair<AppRole, String>>(
                AppRole.EMAIL to "Mail", AppRole.NOTES to "Notes", AppRole.STORE to "Store", AppRole.SETTINGS to "Settings",
                AppRole.CLOCK to "Clock", AppRole.CALCULATOR to "Calc", AppRole.VIDEO to "YouTube", AppRole.GALLERY to "Gallery"
            ),
            photoNames = listOf<String>("teal_a", "teal_b", "teal_c", "teal_d")
        ),
        dock = listOf(AppRole.PHONE, AppRole.CHAT, AppRole.BROWSER, AppRole.CAMERA)
    )

    val midnight = ThemeDefinition(
        id = "midnight",
        displayName = "Midnight",
        palette = ThemePalette(
            background = Color(0xFF0A0A0A),
            surface = Color(0xFF232323),
            surfaceVariant = Color(0xFF161616),
            onSurface = Color(0xFFECECEC),
            onSurfaceMuted = Color(0xFFA3A3A3),
            accent = Color(0xFFD9D9D9),
            gradientTop = Color(0xFF2E2E2E),
            gradientBottom = Color(0xFF000000)
        ),
        pages = buildPages(
            firstWidget = TileContent.Quote("Not everyone is available."),
            secondWidget = TileContent.Calendar,
            secondQuote = TileContent.Quote("Fear does not prevent death. It prevents life."),
            page1Apps = listOf<Pair<AppRole, String>>(
                AppRole.SETTINGS to "Settings", AppRole.MAPS to "Maps",
                AppRole.BROWSER to "Safari", AppRole.MUSIC to "Spotify",
                AppRole.CHAT to "WhatsApp", AppRole.SOCIAL to "Snapchat", AppRole.STORE to "App Store", AppRole.SOCIAL to "Instagram"
            ),
            page2Apps = listOf<Pair<AppRole, String>>(
                AppRole.CLOCK to "Clock", AppRole.CLOUD to "Health", AppRole.NOTES to "Notes", AppRole.CALCULATOR to "Calc",
                AppRole.VIDEO to "YouTube", AppRole.EMAIL to "Mail", AppRole.CAMERA to "Camera", AppRole.GALLERY to "Photos"
            ),
            photoNames = listOf<String>("midnight_a", "midnight_b", "midnight_c", "midnight_d")
        ),
        dock = listOf(AppRole.PHONE, AppRole.CHAT, AppRole.BROWSER, AppRole.CAMERA)
    )

    val crimson = ThemeDefinition(
        id = "crimson",
        displayName = "Crimson",
        palette = ThemePalette(
            background = Color(0xFF241012),
            surface = Color(0xFF4A2226),
            surfaceVariant = Color(0xFF331619),
            onSurface = Color(0xFFF3E4E2),
            onSurfaceMuted = Color(0xFFCBA6A3),
            accent = Color(0xFFE8C9C9),
            gradientTop = Color(0xFF5C2027),
            gradientBottom = Color(0xFF130808)
        ),
        pages = buildPages(
            firstWidget = TileContent.Quote("A warrior must always be ready to face any challenge."),
            secondWidget = TileContent.Calendar,
            secondQuote = TileContent.Quote("Fear does not prevent death. It prevents life."),
            page1Apps = listOf<Pair<AppRole, String>>(
                AppRole.SOCIAL to "TikTok", AppRole.SOCIAL to "Instagram",
                AppRole.CHAT to "Messages", AppRole.PHONE to "Phone",
                AppRole.CAMERA to "Gallery", AppRole.STORE to "Store", AppRole.MAPS to "Maps", AppRole.CLOUD to "Uber"
            ),
            page2Apps = listOf<Pair<AppRole, String>>(
                AppRole.SETTINGS to "Settings", AppRole.CALCULATOR to "Calc", AppRole.NOTES to "Notes", AppRole.CLOCK to "Clock",
                AppRole.MUSIC to "Music", AppRole.BROWSER to "Browser", AppRole.EMAIL to "Mail", AppRole.VIDEO to "Video"
            ),
            photoNames = listOf<String>("crimson_a", "crimson_b", "crimson_c", "crimson_d")
        ),
        dock = listOf(AppRole.CHAT, AppRole.PHONE, AppRole.CAMERA, AppRole.SOCIAL)
    )

    val all: List<ThemeDefinition> = listOf(forest, mono, teal, midnight, crimson)

    fun byId(id: String): ThemeDefinition = all.firstOrNull { it.id == id } ?: forest
}
