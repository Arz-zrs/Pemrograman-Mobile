package com.example.scrollablemodul3

import android.app.Application
import com.example.scrollablemodul3.model.data.datastore.AppPreferencesRepository
import com.example.scrollablemodul3.model.data.local.MovieDatabase
import timber.log.Timber

class ScrollableApplication : Application() {
    val database by lazy { MovieDatabase.getInstance(this) }
    val preferencesRepository by lazy { AppPreferencesRepository(this) }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}