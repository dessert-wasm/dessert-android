package com.dessert.ui.modules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dessert.R

class ModulesFragment : Fragment() {

    private lateinit var modulesViewModel: ModulesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        modulesViewModel =
                ViewModelProviders.of(this).get(ModulesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_modules, container, false)
//        val textView: TextView = root.findViewById(R.id.modules_result)
//        modulesViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }
}