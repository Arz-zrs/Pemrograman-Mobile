package com.example.scrollablemodul3

import android.app.Application
import com.example.scrollablemodul3.model.data.datastore.AppPreferencesRepository
import com.example.scrollablemodul3.model.data.local.MovieDatabase
import com.example.scrollablemodul3.model.data.repository.IMovieRepository
import com.example.scrollablemodul3.model.data.repository.MovieRepository
import timber.log.Timber

class ScrollableApplication : Application() {
    val database by lazy { MovieDatabase.getDatabase(this) }
    val preferencesRepository by lazy { AppPreferencesRepository(this) }

    val movieRepository: IMovieRepository by lazy {
        MovieRepository(movieDao = database.movieDao())
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}