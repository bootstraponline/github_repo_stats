package main

import com.apollographql.apollo.api.Input
import com.apollographql.apollo.github.FetchIssuesQuery
import com.apollographql.apollo.github.FetchPRsQuery
import kotlinx.coroutines.runBlocking
import java.lang.RuntimeException

fun FetchIssuesQuery.Data.printData() {
    val repo = this.repository() ?: throw RuntimeException("Null repository")
    val issues = repo.issues()

    println("Total issues: ${issues.totalCount()}")

    val issuesEdges = issues.edges()

    if (issuesEdges != null) {
        println("Fetched issues: ${issuesEdges.size}")
        issuesEdges.forEach {
            println("  ${it.node()?.url()}")
        }
    }
}

fun FetchPRsQuery.Data.printData() {
    val repo = this.repository() ?: throw RuntimeException("Null repository")

    val prs = repo.pullRequests()
    println("Total PRs: ${prs.totalCount()}")

    val prsEdges = prs.edges()
    if (prsEdges != null) {
        println("Fetched PRs: ${prsEdges.size}")
        prsEdges.forEach {
            println("  ${it.node()?.url()}")
        }
    }
}

fun main() = runBlocking {
    // TODO: pass cursor back into query for pagination.  `after: "$cursor") { ... }`
    client.query(FetchIssuesQuery(2, Input.absent())).execute().data()?.printData()
    client.query(FetchPRsQuery(2, Input.absent())).execute().data()?.printData()

    System.exit(0)
}
