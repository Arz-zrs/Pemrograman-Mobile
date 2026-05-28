package com.example.scrollablemodul3.model.data.local.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "movies",
    primaryKeys = ["id", "language"],
    indices = [Index(value = ["category", "language"])]
)
data class MovieEntity(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val releaseDate: String,
    val originalLanguage: String,
    val popularity: Double,
    val category: String,
    val language: String,
    val cachedAt: Long = System.currentTimeMillis()
)