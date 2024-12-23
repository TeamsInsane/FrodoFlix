package com.frodo.frodoflix.data;


data class User(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val salt: String = "",
    val genres: List<String> = emptyList()
)