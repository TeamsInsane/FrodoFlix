package com.frodo.frodoflix.data

data class Movie (
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val releaseDate: String,
    val popularity: Double = 0.0,
)