package com.frodo.frodoflix.data

data class UserCard(
    val username: String = "Guest?",
    val description: String = "To je default description",
    val profileImageUrl: String = "https://sm.ign.com/ign_ap/review/s/sekiro-sha/sekiro-shadows-die-twice-review_3sf1.jpg",
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    var favlist: List<Int> = emptyList(),
    var watchlist: List<Int> = emptyList(),
)