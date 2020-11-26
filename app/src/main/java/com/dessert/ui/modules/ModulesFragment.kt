package com.dessert.ui.modules

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.SearchQuery
import com.apollographql.apollo.sample.type.ModuleTypeEnum
import com.apollographql.apollo.sample.type.PaginationQueryInput
import com.dessert.CustomApolloClient
import com.dessert.R


class ModulesFragment : Fragment() {
    private var query = ""
    private var moduleType = ModuleTypeEnum.CONNECTOR
    private lateinit var modulesRecyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_modules, container, false)
        val searchBar = root.findViewById<SearchView>(R.id.search_modules)

        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                searchBar.clearFocus()
                query = text!!
                sendQuery(query, moduleType)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val spinner = root.findViewById<Spinner>(R.id.spinner)

        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("CONNECTOR", "CORE")).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    moduleType = ModuleTypeEnum.CONNECTOR
                    sendQuery(query, moduleType)
                } else {
                    moduleType = ModuleTypeEnum.CORE
                    sendQuery(query, moduleType)
                }
            }
        }

        modulesRecyclerView = root.findViewById<RecyclerView>(R.id.modules_list)
        modulesRecyclerView.layoutManager = LinearLayoutManager(context)

        sendQuery(query, moduleType)
        return root
    }

    fun sendQuery(query: String, moduleType: ModuleTypeEnum) {
        lifecycleScope.launchWhenResumed {
            val response = try {
                CustomApolloClient.client.query(SearchQuery(query, Input.fromNullable(moduleType), PaginationQueryInput(true, 0, 15))).toDeferred().await()
            } catch (e: ApolloException) {
                Log.i("Modules", "Failure", e)
                null
            }
            modulesRecyclerView.adapter = ModuleDescAdapter(response!!.data!!.search.result!!, childFragmentManager)
        }
    }
}