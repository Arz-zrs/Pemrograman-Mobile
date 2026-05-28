package com.example.scrollablemodul3.model.data.repository

import com.example.scrollablemodul3.model.data.local.dao.MovieDao
import com.example.scrollablemodul3.model.data.local.entity.MovieEntity
import com.example.scrollablemodul3.model.data.remote.ApiResponse
import com.example.scrollablemodul3.model.data.remote.NetworkModule
import com.example.scrollablemodul3.model.data.remote.api.TmdbApiService
import com.example.scrollablemodul3.model.data.remote.dto.MovieDto
import com.example.scrollablemodul3.model.data.remote.dto.MovieResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MovieRepository(
    private val apiService: TmdbApiService = NetworkModule.tmdbApiService,
    private val movieDao: MovieDao
) {
    companion object {
        private const val CACHE_DURATION_MS = 30 * 60 * 1000
    }

    private suspend fun isCacheValid(category: String): Boolean {
        val lastCache = movieDao.getLastCachedTime(category) ?: return false
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

        if (!forceRefresh && isCacheValid(category)) {
            Timber.d("Cache is valid for category $category")
            movieDao.getMoviesByCategory(category, language).collect { movies ->
                emit(ApiResponse.Success(movies))
            }
            return@flow
        }

        Timber.d("Cache is invalid or force refresh is true for category $category")
        try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.results.isNotEmpty()) {
                    val movies = body.results.map { it.toEntity(category, language) }
                    movieDao.deleteByCategory(category)
                    movieDao.insertAll(movies)
                    movieDao.getMoviesByCategory(category, language).collect {
                        emit(ApiResponse.Success(it))
                    }
                } else {
                    Timber.w("No movies found for category $category")
                    emit(ApiResponse.Error("No movies found"))
                }
            } else {
                val errorMsg = "HTTP ${response.code()}: ${response.errorBody()?.string()}"
                Timber.e("Error fetching movies for category $category: $errorMsg")
                emit(ApiResponse.Error(errorMsg))
            }
        } catch (e: UnknownHostException) {
            Timber.e(e, "[$category] No internet connection")
            serveStaleCache(category, language, "No internet connection. Showing cached data.")
        } catch (e: SocketTimeoutException) {
            Timber.e(e, "[$category] Connection timeout")
            serveStaleCache(category, language, "Connection timeout. Showing cached data.")
        } catch (e: Exception) {
            Timber.e(e, "[$category] Unexpected error")
            serveStaleCache(category, language, e.localizedMessage ?: "Unexpected error")
        }
    }

    private suspend fun FlowCollector<ApiResponse<List<MovieEntity>>>.serveStaleCache(
        category: String,
        language: String,
        message: String
    ) {
        val count = movieDao.getCountByCategory(category)
        if (count > 0) {
            Timber.d("[$category] Serving stale cache ($count items)")
            movieDao.getMoviesByCategory(category, language).collect { stale ->
                emit(ApiResponse.Success(stale))
            }
        } else {
            emit(ApiResponse.Error(message = message))
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