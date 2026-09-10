package com.rajule.themelauncher.ui.picker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.drawable.toBitmap
import com.rajule.themelauncher.data.AppResolver

@Composable
fun AppPickerSheet(appResolver: AppResolver, onPick: (String) -> Unit, onDismiss: () -> Unit) {
    val apps = remember { appResolver.listLaunchableApps() }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .background(Color(0xFF141414))
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Text("Choose an app", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(Modifier.height(12.dp))
            LazyColumn(Modifier.fillMaxSize()) {
                items(apps, key = { it.packageName }) { app ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { onPick(app.packageName) }
                            )
                            .padding(horizontal = 10.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val bitmap = remember(app.packageName) { app.icon.toBitmap(96, 96) }
                        Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.size(34.dp))
                        Spacer(Modifier.width(14.dp))
                        Text(app.label, color = Color.White)
                    }
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}
