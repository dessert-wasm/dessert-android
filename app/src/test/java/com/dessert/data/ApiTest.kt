package com.dessert.data

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.LoginMutation
import com.apollographql.apollo.sample.ModuleQuery
import com.apollographql.apollo.sample.SearchQuery
import com.apollographql.apollo.sample.UserQuery
import com.apollographql.apollo.sample.type.ModuleTypeEnum
import com.apollographql.apollo.sample.type.PaginationQueryInput
import com.dessert.CustomApolloClient
import org.junit.Assert.*
import org.junit.Test

class ApiTest {
    @Test
    fun login() {
        val client: ApolloClient = CustomApolloClient.client

        assertEquals(true, client.serverUrl.isHttps)
        assertEquals("prod.dessert.vodka", client.serverUrl.host())

        client.mutate(LoginMutation("lucas.santoni@epitech.eu", "12345", true))
            .enqueue(object : ApolloCall.Callback<LoginMutation.Data>() {
                override fun onResponse(response: Response<LoginMutation.Data>) {
                    val id = response.data!!.login.id;

                    assertEquals(true, id > 0)

                    CustomApolloClient.client.query(
                        UserQuery(
                            id,
                            PaginationQueryInput(true, 0, 20)
                        )
                    )
                        .enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                            override fun onFailure(e: ApolloException) {
                                assert(false)
                            }

                            override fun onResponse(response: Response<UserQuery.Data>) {
                                assertEquals("Lucas", response.data?.user?.nickname)
                                assert(!response.data?.user?.profilePicUrl.toString().isEmpty())
                            }
                        })


                }

                override fun onFailure(e: ApolloException) {
                    assert(false)
                }
            }
            )
        Thread.sleep(2500)
    }

    @Test
    fun userProfile() {
        val client: ApolloClient = CustomApolloClient.client

        client.query(UserQuery(2, PaginationQueryInput(true, 0, 50)))
            .enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    print("FAILS")
                    assert(true)
                }

                override fun onResponse(response: Response<UserQuery.Data>) {
                    assertEquals(2, response.data!!.user.id)
                    assertEquals("Lucas", response.data!!.user.nickname)
                    println(response.data!!.user.tokens.size)
                    assert(response.data!!.user.modules.totalRecords!! > 0)
                    val module = response.data!!.user.modules.result!![0]!!

                    assert(module.id != 0)
                    assert(module.name.isNotEmpty())
                    assert(module.description.isNotEmpty())
                    assert(module.tags.isNotEmpty())
                }
            })
        Thread.sleep(3500)
    }

    @Test
    fun search() {
        val client: ApolloClient = CustomApolloClient.client
        val query = SearchQuery(
            "yaml",
            Input.fromNullable(ModuleTypeEnum.CONNECTOR),
            PaginationQueryInput(true, 0, 50)
        )

        assert(query.operationId().isNotEmpty())


        client.query(query).enqueue(object : ApolloCall.Callback<SearchQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                assert(false)
            }

            override fun onResponse(response: Response<SearchQuery.Data>) {
                val search = response.data!!.search

                assert(search.pageNumber == 1)
                assert(search.pageSize > 0)
                assert(search.totalPages != 0)
                assert(search.result!!.isNotEmpty())

                val module = search.result!![0]!!;

                client.query(ModuleQuery(module.id))
                    .enqueue(object : ApolloCall.Callback<ModuleQuery.Data>() {
                        override fun onFailure(e: ApolloException) {
                            assert(false)
                        }

                        override fun onResponse(response: Response<ModuleQuery.Data>) {
                            val module = response.data!!.module;

                            assert(!module.isCore)
                            assert(module.author.id != 0)
                            assert(module.author.nickname.isNotEmpty())
                            assert(module.name.isNotEmpty())
                            assert(module.description.isNotEmpty())
                            assert(module.githubLink.toString().isNotEmpty())
                        }

                    }
                    )
            }

        })
        Thread.sleep(3500)
    }
}