package com.rajule.themelauncher.model

/** Position of a tile in the freeform grid, in whole cell units. */
data class GridSlot(
    val col: Int,
    val row: Int,
    val colSpan: Int = 1,
    val rowSpan: Int = 1
)

sealed class TileContent {
    data class AppShortcut(val role: AppRole, val label: String) : TileContent()
    data object Clock : TileContent()
    data object Calendar : TileContent()
    data class Quote(val text: String) : TileContent()
    data object Battery : TileContent()
    data object Weather : TileContent()
    data class Photo(val assetName: String) : TileContent()
}

data class GridItem(
    val id: String,
    val slot: GridSlot,
    val content: TileContent
)

/** Fixed columns per page; row height is derived at layout time from available screen height. */
const val GRID_COLUMNS = 4
const val GRID_ROWS = 7
