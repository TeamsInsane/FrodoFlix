package com.frodo.frodoflix.data

data class Rating(
    val rating: Int = 1,
    val comment: String = "",
    val username: String = "",
    val timestamp: Long = 0L,
    val movieId: Int = 0
)
