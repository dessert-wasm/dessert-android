package com.dessert.ui.documentation
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import com.dessert.R

class DocumentationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_documentation, container, false)

        val whoAreWe = root.findViewById<TextView>(R.id.who_are_we_title)
        val cliDoc = root.findViewById<TextView>(R.id.cli_documentation_title)
        val quickstart = root.findViewById<TextView>(R.id.quickstart_guide_title)
        val whyDessert = root.findViewById<TextView>(R.id.why_dessert_title)

        whoAreWe.movementMethod = LinkMovementMethod.getInstance();
        cliDoc.movementMethod = LinkMovementMethod.getInstance();
        quickstart.movementMethod = LinkMovementMethod.getInstance();
        whyDessert.movementMethod = LinkMovementMethod.getInstance();

        return root;
    }
}
//class DocumentationFragment : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_documentation)
//    }
//}

