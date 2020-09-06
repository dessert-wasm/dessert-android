package com.dessert.ui.modules

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
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
import com.dessert.R


class ModulesFragment : Fragment() {
    private var moduleType = ModuleTypeEnum.CORE
    private val apolloClient = ApolloClient.builder().serverUrl("https://dev.dessert.vodka").build()
    private lateinit var modulesRecyclerView : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_modules, container, false)
        val searchBar = root.findViewById<SearchView>(R.id.search_modules)

        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar.clearFocus()
                searchBar.setQuery("", false)
                //TODO ADD PROGRESS BAR
                //TODO ADD NOT FOUND IF NO RESULT
                sendQuery(query!!, moduleType)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val spinner = root.findViewById<Spinner>(R.id.spinner)


        /*createFromResource(requireContext(), *arrayOf("CORE", "CONNECTOR"), android.R.layout.simple_spinner_item).also*/
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("CORE", "CONNECTOR")).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.setOnItemSelectedListener(object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    moduleType = ModuleTypeEnum.CORE
                } else {
                    moduleType = ModuleTypeEnum.CONNECTOR
                }
            }
        })

        modulesRecyclerView = root.findViewById<RecyclerView>(R.id.modules_list)
        modulesRecyclerView.layoutManager = LinearLayoutManager(context)

        sendQuery("", moduleType)
        return root
    }

    //TODO FAIRE LA PREMIERE QUERY DANS LA MAINACTIVITY COMME SI ON CHANGE ON GARDERA LE MEME RESULTAT
    fun sendQuery(query: String, moduleType: ModuleTypeEnum) {
        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.query(SearchQuery(query, Input.fromNullable(moduleType), PaginationQueryInput(true, 0, 8))).toDeferred().await()
            } catch (e: ApolloException) {
                Log.i("Modules", "Failure", e)
                null
            }
            modulesRecyclerView.adapter = ModuleDescAdapter(response!!.data!!.search.result!!, childFragmentManager)
        }
    }
}