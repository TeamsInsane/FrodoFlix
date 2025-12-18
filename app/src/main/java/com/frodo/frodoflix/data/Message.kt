package com.frodo.frodoflix.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val groupId: String = "",
    val username: String = "",
    val content: String = "",
    val timestamp: Long = 0,
)
