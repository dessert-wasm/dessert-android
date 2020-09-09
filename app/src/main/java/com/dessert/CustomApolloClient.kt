package com.dessert
import com.apollographql.apollo.ApolloClient

object CustomApolloClient {
    private const val BaseUrl = "https://prod.dessert.vodka"

    val client: ApolloClient get() {
        return ApolloClient.builder().serverUrl(BaseUrl).build()
    }
}
