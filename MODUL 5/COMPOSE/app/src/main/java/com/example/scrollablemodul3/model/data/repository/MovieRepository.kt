package com.example.scrollablemodul3.model.data.repository

import android.content.Context
import com.example.scrollablemodul3.R
import com.example.scrollablemodul3.model.data.local.dao.MovieDao
import com.example.scrollablemodul3.model.data.local.entity.MovieEntity
import com.example.scrollablemodul3.model.data.remote.ApiResponse
import com.example.scrollablemodul3.model.data.remote.NetworkModule
import com.example.scrollablemodul3.model.data.remote.api.TmdbApiService
import com.example.scrollablemodul3.model.data.remote.dto.MovieDto
import com.example.scrollablemodul3.model.data.remote.MovieResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MovieRepository(
    private val context: Context,
    private val apiService: TmdbApiService = NetworkModule.tmdbApiService,
    private val movieDao: MovieDao
) {
    companion object {
        private const val CACHE_DURATION_MS = 30 * 60 * 1000
    }

    private suspend fun isCacheValid(category: String, language: String): Boolean {
        val lastCache = movieDao.getLastCachedTime(category, language) ?: return false
        val age = System.currentTimeMillis() - lastCache
        return age < CACHE_DURATION_MS
    }

    private fun MovieDto.toEntity(category: String, language: String) = MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        voteCount = voteCount,
        releaseDate = releaseDate,
        originalLanguage = originalLanguage,
        popularity = popularity,
        category = category,
        language = language
    )

    private fun getMovies(
        category: String,
        language: String,
        forceRefresh: Boolean,
        apiCall: suspend () -> Response<MovieResponse>
    ): Flow<ApiResponse<List<MovieEntity>>> = flow {
        emit(ApiResponse.Loading)

        if (!forceRefresh && isCacheValid(category, language)) {
            val cached = movieDao.getMoviesByCategory(category, language).first()
            if (cached.isNotEmpty()) {
                emit(ApiResponse.Success(cached))
                return@flow
            }
        }

        try {
            val response = apiCall()
            when {
                response.isSuccessful -> {
                    val results = response.body()?.results
                    if (!results.isNullOrEmpty()) {
                        val entities = results.map { it.toEntity(category, language) }
                        movieDao.deleteByCategory(category, language)
                        movieDao.insertAll(entities)
                        val data = movieDao.getMoviesByCategory(category, language).first()
                        emit(ApiResponse.Success(data))
                    } else {
                        serveStaleOrError(category, language, context.getString(R.string.error_no_movies))
                    }
                }
                else -> {
                    val code = response.code()
                    serveStaleOrError(category, language, context.getString(R.string.error_server, code))
                }
            }
        } catch (e: UnknownHostException) {
            Timber.e(e, "[$category] No Internet Connection")
            serveStaleOrError(category, language, context.getString(R.string.error_no_internet))
        } catch (e: SocketTimeoutException) {
            Timber.e(e, "[$category] Connection timed out")
            serveStaleOrError(category, language, context.getString(R.string.error_timeout))
        } catch (e: Exception) {
            Timber.e(e, "[$category] Unexpected error")
            serveStaleOrError(category, language, e.localizedMessage ?: context.getString(R.string.error_unexpected))
        }
    }

    private suspend fun FlowCollector<ApiResponse<List<MovieEntity>>>.serveStaleOrError(
        category: String,
        language: String,
        message: String
    ) {
        val stale = movieDao.getMoviesByCategory(category, language).first()
        if (stale.isNotEmpty()) {
            emit(ApiResponse.Success(data = stale))
        } else {
            val anyLanguage = movieDao.getAnyMoviesByCategory(category).first()
            if (anyLanguage.isNotEmpty()) {
                emit(ApiResponse.Success(data = anyLanguage))
            } else {
                emit(ApiResponse.Error(message = message))
            }
        }
    }

    fun getPopularMovies(language: String, forceRefresh: Boolean = false): Flow<ApiResponse<List<MovieEntity>>> =
        getMovies("popular", language, forceRefresh) {
            apiService.getPopularMovies(language = language)
        }

    fun getNowPlayingMovies(language: String, forceRefresh: Boolean = false): Flow<ApiResponse<List<MovieEntity>>> =
        getMovies("now_playing", language, forceRefresh) {
            apiService.getNowPlayingMovies(language = language)
        }

    fun getTopRatedMovies(language: String, forceRefresh: Boolean = false): Flow<ApiResponse<List<MovieEntity>>> =
        getMovies("top_rated", language, forceRefresh) {
            apiService.getTopRatedMovies(language = language)
        }
}