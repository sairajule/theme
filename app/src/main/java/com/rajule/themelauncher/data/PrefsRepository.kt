package com.rajule.themelauncher.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "theme_launcher_prefs")

private val SELECTED_THEME_KEY = stringPreferencesKey("selected_theme_id")

/** All persisted state: which theme is active, and any manual app-tile overrides. Local only. */
class PrefsRepository(private val context: Context) {

    val selectedThemeId: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[SELECTED_THEME_KEY] ?: ThemeCatalog.forest.id
    }

    suspend fun setSelectedTheme(themeId: String) {
        context.dataStore.edit { it[SELECTED_THEME_KEY] = themeId }
    }

    private fun bindingKey(themeId: String, tileId: String) =
        stringPreferencesKey("binding_${themeId}_$tileId")

    fun binding(themeId: String, tileId: String): Flow<String?> =
        context.dataStore.data.map { it[bindingKey(themeId, tileId)] }

    suspend fun setBinding(themeId: String, tileId: String, packageName: String) {
        context.dataStore.edit { it[bindingKey(themeId, tileId)] = packageName }
    }

    suspend fun clearBinding(themeId: String, tileId: String) {
        context.dataStore.edit { it.remove(bindingKey(themeId, tileId)) }
    }
}
