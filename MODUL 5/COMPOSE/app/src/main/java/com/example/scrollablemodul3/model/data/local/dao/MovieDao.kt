package com.example.scrollablemodul3.model.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scrollablemodul3.model.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE category = :category AND language = :language ORDER BY voteAverage DESC")
    fun getMoviesByCategoryAndLanguage(category: String, language: String): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM movies WHERE category = :category AND language = :language")
    suspend fun deleteByCategoryAndLanguage(category: String, language: String)

    @Query("SELECT cachedAt FROM movies WHERE category = :category AND language = :language LIMIT 1")
    suspend fun getLastCachedTime(category: String, language: String): Long?

    @Query("SELECT * FROM movies WHERE category = :category ORDER BY voteAverage DESC")
    fun getAnyMoviesByCategory(category: String): Flow<List<MovieEntity>>
}