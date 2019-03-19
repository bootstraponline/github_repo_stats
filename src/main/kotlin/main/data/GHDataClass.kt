package main.data

data class GHList(
    val totalCount: Int,
    val issues: MutableSet<GHObject> = mutableSetOf()
)

data class GHObject(
    val url: String, // URI
    val createdAt: String, // ZonedDateTime
    // closedAt is null when the issue/PR hasn't been closed.
    val closedAt: String?
)
