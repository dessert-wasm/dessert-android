package com.dessert.ui.github

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import br.tiagohm.markdownview.MarkdownView
import br.tiagohm.markdownview.css.InternalStyleSheet
import br.tiagohm.markdownview.css.styles.Github
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.sample.ModuleQuery
import com.dessert.R


class GithubFragment : Fragment() {
    val apolloClient = ApolloClient.builder().serverUrl("https://dev.dessert.vodka").build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_github, container, false)

        if (requireArguments().containsKey("id")) {
            val id = requireArguments().getInt("id")

            lifecycleScope.launchWhenResumed {
                val response = try {
                    apolloClient.query(ModuleQuery(id)).toDeferred().await()
                } catch (e: ApolloException) {
                    Log.i("Modules", "Failure", e)
                    null
                }
                if (response!!.data!!.module.githubLink != null) {
                    val readmeLink = "https://raw.githubusercontent.com/" + response!!.data!!.module.githubLink.toString().substring(19) + "/master/README.md"
                    val mMarkdownView = root.findViewById(R.id.markdown_view) as MarkdownView
                    val css: InternalStyleSheet = Github()
                    css.removeRule(".scrollup")
                    mMarkdownView.addStyleSheet(css)
                    mMarkdownView.loadMarkdownFromUrl(readmeLink)
                }
            }
        }
        return root;
    }
}