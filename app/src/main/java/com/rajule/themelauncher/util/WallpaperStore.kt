package com.rajule.themelauncher.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

// Optional per-theme wallpaper photo, stored on-device; falls back to a gradient until set.
class WallpaperStore(private val context: Context) {

    private fun dir(): File =
        File(context.getExternalFilesDir(null) ?: context.filesDir, "wallpapers").apply { mkdirs() }

    private fun fileFor(themeId: String): File = File(dir(), "$themeId.jpg")

    fun bitmapFor(themeId: String): Bitmap? {
        val file = fileFor(themeId)
        if (!file.exists()) return null
        return runCatching { BitmapFactory.decodeFile(file.absolutePath) }.getOrNull()
    }

    fun importFromUri(themeId: String, uri: Uri): Boolean = runCatching {
        val input = context.contentResolver.openInputStream(uri) ?: return false
        val bitmap = input.use { BitmapFactory.decodeStream(it) } ?: return false
        FileOutputStream(fileFor(themeId)).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
        }
        true
    }.getOrDefault(false)

    fun clear(themeId: String) {
        fileFor(themeId).delete()
    }
}
