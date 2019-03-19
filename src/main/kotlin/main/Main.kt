package main

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.github.FetchQuery
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun callGithub(): Response<FetchQuery.Data> {
    return suspendCoroutine { continuation ->
        client.query(query).enqueue(
            object : ApolloCall.Callback<FetchQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(response: Response<FetchQuery.Data>) {
                    continuation.resume(response)
                }

            }
        )
    }
}

val client = ApolloClient.builder()
    .serverUrl("https://api.github.com/graphql")
    .build()!!
val query = FetchQuery()

fun main() = runBlocking {
    println("graphql started!")

    val data = callGithub()

    println("graphql finished!")
}
