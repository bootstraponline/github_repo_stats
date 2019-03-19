package main

import com.apollographql.apollo.api.Input
import com.apollographql.apollo.github.FetchIssuesQuery
import com.apollographql.apollo.github.FetchPRsQuery
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import main.data.GHList
import main.data.GHObject
import main.util.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

private fun nullOrString(obj: Any?): String? {
    if (obj == null) return null
    return obj.toString()
}

fun FetchIssuesQuery.Issues.toGHObject(): List<GHObject> {
    return this.edges()?.mapNotNull {
        val node = it.node() ?: return@mapNotNull null

        GHObject(
            url = node.url(),
            createdAt = node.createdAt().toString(),
            closedAt = nullOrString(node.closedAt())
        )
    } ?: emptyList()
}

fun FetchPRsQuery.PullRequests.toGHObject(): List<GHObject> {
    return this.edges()?.mapNotNull {
        val node = it.node() ?: return@mapNotNull null

        GHObject(
            url = node.url(),
            createdAt = node.createdAt().toString(),
            closedAt = nullOrString(node.closedAt())
        )
    } ?: emptyList()
}

private fun writeFile(path: String, list: GHList) {
    Files.write(Paths.get(path), gson.toJson(list).toByteArray())
}

suspend fun saveAllPRs() {
    if (File(prsJson).exists()) return

    val pageOne = client.query(FetchPRsQuery(pageSize, Input.absent())).execute().data()
    var prs = pageOne?.repository()?.pullRequests() ?: throw RuntimeException("No PRs")

    val prTotalCount = prs.totalCount()
    println("Total PRs: $prTotalCount")
    val prsList = GHList(prTotalCount)
    prsList.issues.addAll(prs.toGHObject())

    while (prs.pageInfo().hasNextPage()) {
        val after = prs.pageInfo().endCursor()
        println("Paging... $after")
        val nextPage = client.query(FetchPRsQuery(pageSize, Input.fromNullable(after))).execute().data()
        prs = nextPage?.repository()?.pullRequests() ?: break

        val prsToAdd = prs.toGHObject()
        println("Adding ${prsToAdd.size} PRs")
        prsList.issues.addAll(prsToAdd)
    }

    writeFile(prsJson, prsList)
}

suspend fun saveAllIssues() {
    if (File(issuesJson).exists()) return

    val pageOne = client.query(FetchIssuesQuery(pageSize, Input.absent())).execute().data()
    var issues = pageOne?.repository()?.issues() ?: throw RuntimeException("No issues")

    val issueTotalCount = issues.totalCount()
    println("Total issues: $issueTotalCount")
    val issueList = GHList(issueTotalCount)
    issueList.issues.addAll(issues.toGHObject())

    while (issues.pageInfo().hasNextPage()) {
        val after = issues.pageInfo().endCursor()
        println("Paging... $after")
        val nextPage = client.query(FetchIssuesQuery(pageSize, Input.fromNullable(after))).execute().data()
        issues = nextPage?.repository()?.issues() ?: break

        val issuesToAdd = issues.toGHObject()
        println("Adding ${issuesToAdd.size} issues")
        issueList.issues.addAll(issuesToAdd)
    }

    writeFile(issuesJson, issueList)
}

/** Downloads Issue and PR data to JSON files. */
fun main() = runBlocking {
    saveAllIssues()
    saveAllPRs()

    System.exit(0)
}
