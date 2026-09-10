package com.rajule.themelauncher.model

// A shortcut tile asks for a role; AppResolver maps it to whatever's actually installed.
enum class AppRole(val glyph: GlyphId, val candidatePackages: List<String>) {
    PHONE(GlyphId.PHONE, listOf("com.google.android.dialer", "com.samsung.android.dialer", "com.android.dialer")),
    MESSAGES(GlyphId.MESSAGE, listOf("com.google.android.apps.messaging", "com.samsung.android.messaging", "com.android.mms")),
    CAMERA(GlyphId.CAMERA, listOf("com.google.android.GoogleCamera", "com.sec.android.app.camera", "com.android.camera2")),
    GALLERY(GlyphId.IMAGE, listOf("com.google.android.apps.photos", "com.sec.android.gallery3d", "com.android.gallery3d")),
    BROWSER(GlyphId.BROWSER, listOf("com.android.chrome", "com.sec.android.app.sbrowser", "org.mozilla.firefox")),
    SETTINGS(GlyphId.SETTINGS, listOf("com.android.settings")),
    CLOCK(GlyphId.CLOCK, listOf("com.google.android.deskclock", "com.sec.android.app.clockpackage", "com.android.deskclock")),
    CALCULATOR(GlyphId.CALCULATOR, listOf("com.google.android.calculator", "com.sec.android.app.popupcalculator", "com.android.calculator2", "com.miui.calculator")),
    EMAIL(GlyphId.MAIL, listOf("com.google.android.gm")),
    CHAT(GlyphId.CHAT, listOf("com.whatsapp", "com.whatsapp.w4b")),
    SOCIAL(GlyphId.AT, listOf("com.instagram.android", "com.twitter.android", "com.zhiliaoapp.musically")),
    MUSIC(GlyphId.MUSIC, listOf("com.spotify.music", "com.google.android.apps.youtube.music")),
    VIDEO(GlyphId.PLAY, listOf("com.google.android.youtube")),
    MAPS(GlyphId.PIN, listOf("com.google.android.apps.maps")),
    NOTES(GlyphId.NOTE, listOf("com.google.android.keep", "com.samsung.android.app.notes", "com.microsoft.office.onenote")),
    STORE(GlyphId.STORE, listOf("com.android.vending")),
    CLOUD(GlyphId.CLOUD, listOf("com.google.android.apps.docs", "com.dropbox.android")),
    LOCK(GlyphId.LOCK, emptyList())
}

enum class GlyphId {
    PHONE, MESSAGE, CAMERA, IMAGE, BROWSER, SETTINGS, CLOCK, CALCULATOR,
    MAIL, CHAT, AT, MUSIC, PLAY, PIN, NOTE, STORE, CLOUD, LOCK, GRID, MOON, BATTERY
}
