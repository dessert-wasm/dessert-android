package com.dessert

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.amulyakhare.textdrawable.TextDrawable
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.UserQuery
import com.apollographql.apollo.sample.type.PaginationQueryInput
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.InputStream
import java.net.URL


class MainActivity : AppCompatActivity() {
    fun LoadImageFromWebOperations(url: String?): Drawable? {
        return try {
            val `is`: InputStream = URL(url).getContent() as InputStream
            Drawable.createFromStream(`is`, "src name")
        } catch (e: Exception) {
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        navView.setupWithNavController(navController)



        val id = intent.extras!!.getInt("ID");
        val apolloClient = ApolloClient.builder().serverUrl("https://dev.dessert.vodka").build()

        apolloClient.query(UserQuery(id, PaginationQueryInput(true, 0, 20)))
            .enqueue(object : ApolloCall.Callback<UserQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(response: Response<UserQuery.Data>) {
                    //Response(operation=UserQuery(author=8, pagination=PaginationQueryInput(includeCount=true, pageNumber=0, pageSize=20)),
                    // data=Data(user=User(__typename=Account, id=8, nickname=Renaud, profilePicUrl=https://www.bbcgoodfood.com/sites/default/files/recipe-collections/collection-image/2018/09/dessert-main-image-molten-cake.jpg,
                    // tokens=[], modules=Modules(__typename=PaginatedResultOfModule, totalRecords=0, result=[]))), errors=null, dependentKeys=[],
                    // fromCache=false, extensions={}, executionContext=com.apollographql.apollo.http.OkHttpExecutionContext@80388d9)
                    Log.i("OUI", response.toString())
                }
            })
    }
}