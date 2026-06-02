package com.example.scrollablemodul3.model.data.repository

import com.example.scrollablemodul3.model.data.local.entity.MovieEntity
import com.example.scrollablemodul3.model.data.remote.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {

    fun getPopularMovies(language: String, forceRefresh: Boolean = false):
            Flow<ApiResponse<List<MovieEntity>>>
    fun getNowPlayingMovies(language: String, forceRefresh: Boolean = false):
            Flow<ApiResponse<List<MovieEntity>>>
    fun getTopRatedMovies(language: String, forceRefresh: Boolean = false):
            Flow<ApiResponse<List<MovieEntity>>>
}