package com.rajule.themelauncher.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import com.rajule.themelauncher.model.GridItem

// Places each item at its explicit (col,row,colSpan,rowSpan) cell - a fixed-grid freeform layout.
@Composable
fun FreeformGrid(
    items: List<GridItem>,
    columns: Int,
    rows: Int,
    modifier: Modifier = Modifier,
    itemContent: @Composable (GridItem) -> Unit
) {
    Layout(
        modifier = modifier,
        content = { items.forEach { item -> itemContent(item) } }
    ) { measurables, constraints ->
        val cellWidth = constraints.maxWidth / columns
        val cellHeight = constraints.maxHeight / rows

        val placeables = measurables.mapIndexed { index, measurable ->
            val slot = items[index].slot
            measurable.measure(
                Constraints.fixed(
                    width = (cellWidth * slot.colSpan).coerceAtLeast(1),
                    height = (cellHeight * slot.rowSpan).coerceAtLeast(1)
                )
            )
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val slot = items[index].slot
                placeable.placeRelative(x = slot.col * cellWidth, y = slot.row * cellHeight)
            }
        }
    }
}
