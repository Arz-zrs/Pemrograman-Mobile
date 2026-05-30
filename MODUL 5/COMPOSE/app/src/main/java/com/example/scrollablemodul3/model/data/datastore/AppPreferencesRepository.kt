package com.example.scrollablemodul3.model.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class AppPreferencesRepository(private val context: Context) {
    companion object {
        private val KEY_LOCALE = stringPreferencesKey("selected_locale")
        private val KEY_MOVIE_CATEGORY = stringPreferencesKey("movie_category")
    }

    val selectedLocale: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading locale preference")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences -> preferences[KEY_LOCALE] ?: "en" }

    val selectedMovieCategory: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading movie category preference")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences -> preferences[KEY_MOVIE_CATEGORY] ?: "popular" }

    suspend fun saveLocale(locale: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LOCALE] = locale
        }
    }

    suspend fun saveMovieCategory(category: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_MOVIE_CATEGORY] = category
        }
    }
}