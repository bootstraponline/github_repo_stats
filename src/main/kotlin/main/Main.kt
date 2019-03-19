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

// Apollo dropped the sync API. We can bring it back by using a suspend coroutine.
// https://github.com/apollographql/apollo-android/issues/1054#issuecomment-474151322
// https://github.com/apollographql/apollo-android/issues/606
suspend fun <T> ApolloCall<T>.execute() = suspendCoroutine<Response<T>> { continuation ->
    enqueue(object : ApolloCall.Callback<T>() {
        override fun onResponse(response: Response<T>) {
            continuation.resume(response)
        }
        override fun onFailure(error: ApolloException) {
            continuation.resumeWithException(error)
        }
    })
}

val client = ApolloClient.builder()
    .serverUrl("https://api.github.com/graphql")
    .build()!!
val query = FetchQuery()

fun main() = runBlocking {
    println("graphql started!")

    // TODO: solve ApolloHttpException: HTTP 401 Unauthorized
    val data = client.query(query).execute()

    println("graphql finished!")
}
