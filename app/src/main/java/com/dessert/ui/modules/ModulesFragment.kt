package com.dessert.ui.modules

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.SearchQuery
import com.apollographql.apollo.sample.UserQuery
import com.apollographql.apollo.sample.type.ModuleTypeEnum
import com.apollographql.apollo.sample.type.PaginationQueryInput
import com.dessert.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine


class ModulesFragment : Fragment() {
    val apolloClient = ApolloClient.builder().serverUrl("https://dev.dessert.vodka").build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_modules, container, false)

        val modulesRecyclerView = root.findViewById<RecyclerView>(R.id.modules_list)
        modulesRecyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.query(SearchQuery("", Input.fromNullable(ModuleTypeEnum.CORE), PaginationQueryInput(true, 0, 5))).toDeferred().await()
            } catch (e: ApolloException) {
                Log.i("Modules", "Failure", e)
                null
            }
            modulesRecyclerView.adapter = ModuleDescAdapter(response!!.data!!.search.result!!, childFragmentManager)
        }
        return root
    }
}