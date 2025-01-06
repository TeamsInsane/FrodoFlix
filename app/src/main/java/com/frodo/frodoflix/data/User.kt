package com.frodo.frodoflix.data


data class User(
    var username: String = "",
    val email: String = "",
    var password: String = "",
    val salt: String = "",
    var genres: List<String> = emptyList(),
    var favlist: List<Int> = emptyList(),
    var watchlist: List<Int> = emptyList()
)