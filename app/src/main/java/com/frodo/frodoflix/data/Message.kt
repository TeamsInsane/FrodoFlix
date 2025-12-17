package com.frodo.frodoflix.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val groupId: String? = null,
    val username: String? = null,
    val content: String? = null,
    val timestamp: Long? = null
)
