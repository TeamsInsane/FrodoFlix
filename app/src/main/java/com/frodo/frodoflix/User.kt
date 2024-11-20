package com.frodo.frodoflix

data class User(
    val username: String,
    val password: String,
    var genres : List<String>
)
