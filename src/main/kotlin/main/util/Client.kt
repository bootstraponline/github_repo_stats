package main.util

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.github.type.CustomType
import com.apollographql.apollo.response.CustomTypeAdapter
import com.apollographql.apollo.response.CustomTypeValue
import okhttp3.OkHttpClient
import java.time.ZonedDateTime

val GITHUB_TOKEN: String? by lazy {
    System.getenv("GITHUB_TOKEN") ?: throw RuntimeException("GITHUB_TOKEN is null")
}

val okHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder().addInterceptor { chain ->
        val bearerAuth = chain.request().newBuilder()
            .addHeader("Authorization", "bearer $GITHUB_TOKEN")
            .build()
        chain.proceed(bearerAuth)
    }.build()
}

val uriCustomTypeAdapter = object : CustomTypeAdapter<String> {
    override fun encode(value: String): CustomTypeValue<*> {
        return CustomTypeValue.GraphQLString(value)
    }

    override fun decode(value: CustomTypeValue<*>): String {
        return value.value.toString()
    }
}

val datetimeCustomTypeAdapter = object : CustomTypeAdapter<ZonedDateTime> {
    override fun encode(value: ZonedDateTime): CustomTypeValue<*> {
        return CustomTypeValue.GraphQLString(value.toString())
    }

    override fun decode(value: CustomTypeValue<*>): ZonedDateTime {
        return ZonedDateTime.parse(value.value.toString())
    }
}

val client: ApolloClient by lazy {
    ApolloClient.builder()
        .serverUrl("https://api.github.com/graphql")
        .okHttpClient(okHttpClient)
        .addCustomTypeAdapter(CustomType.URI, uriCustomTypeAdapter)
        .addCustomTypeAdapter(CustomType.DATETIME, datetimeCustomTypeAdapter)
        .build()
}
