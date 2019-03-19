package main

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.github.FetchQuery
import kotlinx.coroutines.runBlocking

fun Response<FetchQuery.Data>.printData() {
    val repo = this.data()?.repository() ?: return
    val issues = repo.issues()
    val prs = repo.pullRequests()

    println("Total issues: ${issues.totalCount()}")
    println("Total PRs: ${prs.totalCount()}")

    val issuesEdges = issues.edges()

    if (issuesEdges != null) {
        println("Fetched issues: ${issuesEdges.size}")
        issuesEdges.forEach {
            println("  ${it.node()?.url()}")
        }
    }

    val prsEdges = prs.edges()
    if (prsEdges != null) {
        println("Fetched PRs: ${prsEdges.size}")
        prsEdges.forEach {
            println("  ${it.node()?.url()}")
        }
    }
}

fun main() = runBlocking {
    val response = client.query(FetchQuery()).execute()
    response.printData()

    System.exit(0)
}
