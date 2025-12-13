package com.frodo.frodoflix.data

data class Group(
    val groupId: String = "",
    val groupName: String = "",
    val createdBy: String = ""
)

data class GroupMember(
    val role: String = "member",
    val joinedAt: Long = System.currentTimeMillis()
)
